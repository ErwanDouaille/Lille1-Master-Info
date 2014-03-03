/*Author:
DOUAIILE Erwan
MIRANDA Yoan
*/
#include<assert.h>
#include<stdio.h>
#include<stdlib.h>


#define CTX_MAGIC 0xABCD

struct ctx_s ctx_ping; 
struct ctx_s ctx_pong; 
struct ctx_s *ctx_cur; 

typedef void (func_t)(void *);

enum ctx_state_e{CTX_INIT,CTX_EXQ,CTX_END};

struct ctx_s{
	unsigned char *ctx_base;
	unsigned char *ctx_esp;
	unsigned char *ctx_ebp;
	unsigned ctx_size;
	func_t* ctx_f;
	void* ctx_arg;
	enum ctx_state_e ctx_state;
	unsigned ctx_magic;
};

void f_ping(void *arg);
void f_pong(void *arg);


int init_ctx(struct ctx_s *ctx,int stack_size, func_t f, void* args){
	ctx->ctx_f=f;
	ctx->ctx_arg=args;
	ctx->ctx_state=CTX_INIT;
	ctx->ctx_base=(void*)malloc(stack_size);
	ctx->ctx_size=stack_size;
	ctx->ctx_esp=ctx->ctx_base+stack_size-4;
	ctx->ctx_ebp=ctx->ctx_base+stack_size-4;
	ctx->ctx_magic=0xABCD;
	return 0;
}

void start_current_ctx(){
	ctx_cur->ctx_state=CTX_EXQ;
	ctx_cur->ctx_f(ctx_cur->ctx_arg);
	ctx_cur->ctx_state=CTX_END;
}

void switch_to_ctx(struct ctx_s *ctx){
	assert(ctx);
	assert(ctx->ctx_magic==CTX_MAGIC);
	assert(ctx->ctx_state==CTX_INIT||ctx->ctx_state==CTX_EXQ);
	if(ctx_cur){
		asm("movl %%esp,%0" "\n\t" "movl %%ebp,%1"
			:"=r"(ctx_cur->ctx_esp),"=r"(ctx_cur->ctx_ebp));
	}
	ctx_cur=ctx;

	asm("movl %0,%%esp" "\n\t" "movl %1,%%ebp"
		:
		:"r"(ctx->ctx_esp),"r"(ctx->ctx_ebp));
	if(ctx_cur->ctx_state==CTX_INIT)
		start_current_ctx();

	
	
}

int main(int argc, char *argv[])
{
    init_ctx(&ctx_ping, 16384, f_ping, NULL);
    init_ctx(&ctx_pong, 16384, f_pong, NULL);
    switch_to_ctx(&ctx_ping);

    exit(EXIT_SUCCESS);
}

void f_ping(void *args)
{
    while(1) {
        printf("A") ;
        switch_to_ctx(&ctx_pong);
        printf("B") ;
        switch_to_ctx(&ctx_pong);
        printf("C") ;
        switch_to_ctx(&ctx_pong);
    }
}

void f_pong(void *args)
{
    while(1) {
        printf("1") ;
        switch_to_ctx(&ctx_ping);
        printf("2") ;
        switch_to_ctx(&ctx_ping);
    }


} 
