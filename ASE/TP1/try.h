struct ctx_s{
   void* esp;
   void* ebp;
};

typedef int (func_t) (int);
int try(struct ctx_s *pctx,func_t *f,int arg);
int throw(struct ctx_s *pctx,int r);
