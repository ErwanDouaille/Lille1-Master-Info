/*Author:
  DOUAIILE Erwan
  MIRANDA Yoan
*/
#include<assert.h>
#include<stdio.h>
#include<stdlib.h>
#include "hw.h"

#define CTX_MAGIC 0xABCD
#define MAXCOUNT 10000000

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
  struct ctx_s * next;
};

static struct ctx_s * ring = (struct ctx_s*) 0; 
void f_ping(void *arg);
void f_pong(void *arg);

int create_ctx(int stack_size, func_t f, void* args){
  struct ctx_s *ctx = (void*)malloc(sizeof(struct ctx_s));
  if(!ring){
    ring = ctx;
    ctx->next = ctx;
  } 
  else{
    ctx->next = ring->next;
    ring->next = ctx;
  }
  return  init_ctx(ctx,stack_size, f, args);    
}


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

  while( ctx->ctx_state == CTX_END) {
    ctx_cur->next = ctx->next;
    free(ctx->ctx_base);
    free(ctx);
    ctx = ctx_cur->next;
  }
  irq_disable();
  if(ctx_cur){
    asm("movl %%esp,%0" "\n\t" "movl %%ebp,%1"
	:"=r"(ctx_cur->ctx_esp),"=r"(ctx_cur->ctx_ebp));
  }
  ctx_cur=ctx;
  irq_enable();
  asm("movl %0,%%esp" "\n\t" "movl %1,%%ebp"
      :
      :"r"(ctx->ctx_esp),"r"(ctx->ctx_ebp));
  if(ctx_cur->ctx_state==CTX_INIT)
    start_current_ctx();
	
}

void yield(){
  if(ctx_cur!=NULL)
    switch_to_ctx(ctx_cur->next); 
  else if (ring!=NULL)
    switch_to_ctx(ring->next);
  else 
    return;
}

void start_schedule(){

  setup_irq(TIMER_IRQ,yield);
  start_hw();  
  yield();
} 


int main(int argc, char *argv[])
{
  create_ctx(16384, f_ping, NULL);
  create_ctx( 16384, f_pong, NULL);
  
  start_schedule();

  exit(EXIT_SUCCESS);
}

void f_ping(void *args)
{
  int i;
  while(1) {
    printf("A\n") ;
    for(i=0;i<MAXCOUNT;i++)
      ;
    printf("B\n") ;
    for(i=0;i<MAXCOUNT;i++)
      ;
    printf("C\n") ;
  }
}

void f_pong(void *args)
{
  int i;
  while(1) {
    for(i=0;i<MAXCOUNT;i++)
      ;
    printf("1\n") ;
    for(i=0;i<MAXCOUNT;i++)
      ;
    printf("2\n") ;
  }


} 
