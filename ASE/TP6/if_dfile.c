/* ------------------------------
   $Id: if_dfile.c,v 1.1 2009/11/16 05:38:07 marquet Exp $
   ------------------------------------------------------------

   Delete a given file identified by its inumber
   Philippe Marquet, Nov 2009
   
*/

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "ifile.h"
#include "mount.h"

static void
dfile(unsigned int inumber)
{
    int status;
    
    status = delete_ifile(inumber);
    ffatal(!status, "erreur suppression fichier %d", inumber);

}

static void
usage(const char *prgm)
{
    fprintf(stderr, "[%s] usage:\n\t"
            "%s inumber\n", prgm, prgm);
    exit(EXIT_FAILURE);
}

int
main (int argc, char *argv[])
{
    unsigned inumber;
    
    if (argc != 2)
        usage(argv[0]);

    errno = 0;
    inumber = strtol(argv[1], NULL, 10);
    if (errno || inumber <= 0)
        usage(argv[0]);

    mount();
    dfile(inumber);
    umount();
    
    exit(EXIT_SUCCESS);         
}
