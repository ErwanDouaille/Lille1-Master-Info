/* ------------------------------
   $Id: mkvol.c 7089 2013-10-19 08:27:30Z marquet $
   ------------------------------------------------------------

   mkvol - create a new volume
   Philippe Marquet, Oct 2013
   
*/

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

/* default values */
#define NSECTORS_DFLT           421
#define FIRST_CYLINDER_DFLT     3
#define FIRST_SECTOR_DFLT       7
#define VOL_DFLT                2

static char *cmdname;

static void
usage ()
{
    fprintf(stderr, "usage: %s [options]... [args]...\n", cmdname);    
    fprintf(stderr, "\toptions : -l nsectors -c first_cylinder -s first_sector -v vol_number\n");
    exit(EXIT_FAILURE);
}

static void
unknown_opt (char opt)
{
    if (isprint(opt))
        fprintf(stderr, "Unknown option `-%c'.\n", opt);
    else
        fprintf(stderr, "Unknown option character `\\x%x'.\n", opt);

    usage();
}

int
main (int argc, char **argv)
{
    int vol = VOL_DFLT,
        nsectors = NSECTORS_DFLT,
        firstcylinder = FIRST_CYLINDER_DFLT,
        firstsector = FIRST_SECTOR_DFLT;
    int i, c;

    cmdname = argv[0];
    opterr = 0;
    
    while ((c = getopt(argc, argv, "l:c:s:v:")) != -1) {
        switch (c) {
            case 'l':
                nsectors = atol(optarg);
                break;
            case 'c':
                firstcylinder = atol(optarg);
                break;
            case 's':
                firstsector = atol(optarg);
                break;
            case 'v':
                vol = atol(optarg);
                break;
            case '?':           /* missing option argument */
                if (optopt == 'l' || optopt == 'c' || optopt == 's') {
                    fprintf(stderr,
                            "Option -%c requires an argument.\n", optopt);
                    usage();
                } else
                    unknown_opt(optopt);
            default:
                unknown_opt(c);
        }
    }

    if (optind != argc) {
        fprintf(stderr, "Argument(s): ");
        for (i = optind; i < argc; i++)
            fprintf(stderr, "%s ", argv[i]);
        fprintf(stderr, "ignored.\n");
        usage();
    }        
    
    printf("mkvol(vol=%d, nsectors=%d, firstcylinder=%d, firstsector=%d)\n",
           vol, nsectors, firstcylinder, firstsector);

    exit(EXIT_SUCCESS);
}
