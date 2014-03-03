/* ------------------------------
   $Id: if_cfile.c,v 1.2 2009/11/30 09:08:02 marquet Exp $
   ------------------------------------------------------------

   Copy a file given by its inumber to another file.
   Return the new file inumber.
   Philippe Marquet, Nov 2009
   
*/

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include "ifile.h"
#include "mount.h"

static void
cfile(unsigned int sinumber)
{
    file_desc_t sfd, dfd;
    unsigned int dinumber;
    int status;
    int c;
    
    dinumber = create_ifile(FILE_FILE);
    ffatal(dinumber, "erreur creation fichier");
    printf("%d\n", dinumber);

    status = open_ifile(&dfd, dinumber);
    ffatal(!status, "erreur ouverture fichier %d", dinumber);
    
    status = open_ifile(&sfd, sinumber);
    ffatal(!status, "erreur ouverture fichier %d", sinumber);

    while((c=readc_ifile(&sfd)) != READ_EOF)
        writec_ifile(&dfd, c);

    close_ifile(&dfd);
    close_ifile(&sfd);
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
    cfile(inumber);
    umount();
    
    exit(EXIT_SUCCESS);         
}
