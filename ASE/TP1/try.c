#include "try.h"
#include <stdio.h>


static int temp;

int try(struct ctx_s *ctx,func_t *f,int arg){
   asm("movl %%esp,%0" "\n\t" "movl %%ebp,%1"
      :"=r"(ctx->esp),"=r"(ctx->ebp));
   return f(arg);
}

int throw(struct ctx_s *ctx,int r){
   temp=r;
   asm("movl %0,%%esp" "\n\t" "movl %1,%%ebp"
      :
      :"r"(ctx->esp),"r"(ctx->ebp));
   fflush(stdout);
   return temp;
}
