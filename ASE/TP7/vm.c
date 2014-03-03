/*
Author:
	DOUAILLE Erwan
	MIRANDA Yoan
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "mbr.h"


/* ------------------------------
   command list
   ------------------------------------------------------------*/
struct _cmd {
    char *name;
    void (*fun) (struct _cmd *c);
    char *comment;
};

static void list(struct _cmd *c);
static void new(struct _cmd *c);
static void del(struct _cmd *c);
static void help(struct _cmd *c) ;
static void save(struct _cmd *c);
static void quit(struct _cmd *c);
static void xit(struct _cmd *c);
static void none(struct _cmd *c) ;

static struct _cmd commands [] = {
    {"list", list, 	"display the partition table"},
    {"new",  new,	"create a new partition"},
    {"del",  del,	"delete a partition"},
    {"save", save,	"save the MBR"},
    {"quit", quit,	"save the MBR and quit"},
    {"exit", xit,	"exit (without saving)"},
    {"help", help,	"display this help"},
    {0, none, 		"unknown command, try help"}
} ;

/* ------------------------------
   dialog and execute 
   ------------------------------------------------------------*/

static void
execute(const char *name)
{
    struct _cmd *c = commands; 
  
    while (c->name && strcmp (name, c->name))
	c++;
    (*c->fun)(c);
}

static void
loop(void)
{
    char name[64];
    
    while (printf("> "), scanf("%62s", name) == 1)
	execute(name) ;
}

/* ------------------------------
   command execution 
   ------------------------------------------------------------*/
static void
list(struct _cmd *c)
{
	list_volumes();
}

static void
new(struct _cmd *c)
{
	unsigned sector;
	unsigned cylinder;
	unsigned nbsector;
	unsigned i;

	printf("Please enter the first cylinder:\n");
	scanf("%i", &cylinder);
	printf("Please enter the first sector:\n");
	scanf("%i", &sector);
	printf("Please enter the number of sectors:\n");
	scanf("%i", &nbsector);
	printf("Please enter the volume type (0- VT_NO_VOL\n1- VT_BASE\n2- VT_AUX\n3- VT_OTHER):\n");
	scanf("%i", &i);
        
	new_volume(sector, cylinder, nbsector, i);
}

static void
del(struct _cmd *c)
{
	unsigned volume;

	printf("Please enter the volume which has to been deleted:\n");
	scanf("%i", &volume);
	delete_volume(volume);
}

static void
save(struct _cmd *c)
{
		save_mbr();
}

static void
quit(struct _cmd *c)
{
	unsigned yesno;
	printf("Quit without saving:\n1- yes\n2- no\n");
	scanf("%i", &yesno);
	if(yesno==1) {

		// pour une raison inconnue si on veux sauvegarder il faut faire 2 save_mbr
		
		save_mbr();   
	}
   	exit(EXIT_SUCCESS);
}

static void
do_xit()
{
    exit(EXIT_SUCCESS);
}

static void
xit(struct _cmd *dummy)
{
    do_xit(); 
}

static void
help(struct _cmd *dummy)
{
    struct _cmd *c = commands;
  
    for (; c->name; c++) 
	printf ("%s\t-- %s\n", c->name, c->comment);
}

static void
none(struct _cmd *c)
{
    printf ("%s\n", c->comment) ;
}

int
main(int argc, char **argv)
{
	/*initialisation*/
	init_mbr();
	/* dialog with user */ 
	loop();

	/* abnormal end of dialog (cause EOF for xample) */
	do_xit();

	/* make gcc -W happy */
	exit(EXIT_SUCCESS);
}
