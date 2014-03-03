/* ------------------------------
   $Id: tools.c,v 1.1 2009/11/16 05:38:07 marquet Exp $
   ------------------------------------------------------------

   Misc. tools
   Philippe Marquet, october 2002
   
*/

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>

#include "tools.h"

/*------------------------------
  explain why exiting
  ------------------------------------------------------------*/
int
fatal(int assert, const char *fname, const char *fmt, ...)
{
    if (! assert) {
	va_list ap;
	va_start(ap, fmt);
	
	fprintf(stderr, "[Error] %s: ", fname);
	vfprintf(stderr, fmt, ap);
	fputc ('\n', stderr);

	exit(EXIT_FAILURE);
    }

    /* make gcc -W happy */
    return RETURN_SUCCESS;
}
