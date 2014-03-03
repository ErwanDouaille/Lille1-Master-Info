/*
Author:
	DOUAILLE Erwan
	MIRANDA Yoan
*/

#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "mbr.h"
#include "drive.h"

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
	init_mbr();
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
	

	printf("b\n");
	struct vol_descr_s vol_desc;

	for(i=0;i<vol;i++){
		vol_desc.vol_cylinder=firstcylinder;
		vol_desc.vol_sector=firstsector;
		vol_desc.vol_nsector=nsectors;
		vol_desc.vol_voltype=VT_BASE;
		mbr.mbr_vols[i]= vol_desc;
	}
	
	mbr.mbr_magic=0xA5E;
	mbr.mbr_nbvol=vol;


    if (optind != argc) {
        fprintf(stderr, "Argument(s): ");
        for (i = optind; i < argc; i++)
            fprintf(stderr, "%s ", argv[i]);
        fprintf(stderr, "ignored.\n");
        usage();
    }        
    save_mbr();
    save_mbr();
    printf("mkvol(vol=%d, nsectors=%d, firstcylinder=%d, firstsector=%d)\n",
           vol, nsectors, firstcylinder, firstsector);

    exit(EXIT_SUCCESS);
}
