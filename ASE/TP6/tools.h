/* ------------------------------
   $Id: tools.h,v 1.1 2009/11/16 05:38:07 marquet Exp $
   ------------------------------------------------------------

   Misc. tools
   Philippe Marquet, october 2009
   
*/

#ifndef _TOOLS_H_
#define _TOOLS_H_

typedef enum { TRUE  = 1, FALSE = 0 } bool_t;

#define RETURN_FAILURE	(-1)
#define RETURN_SUCCESS	(0)
#if RETURN_FAILURE >= 0
#  error "RETURN_FAILURE must be negative"
#endif


/* will not return but exit.
   return an int in order you can return fatal() in a non void function. */   
int fatal(int assert, const char *fname, const char *fmt, ...);
/* function fatal */
#define ffatal(assert,...) fatal(assert, __func__, __VA_ARGS__)
/* not yet */
#define NYI() ffatal(FALSE, "Not Yet Implemented")

#endif
