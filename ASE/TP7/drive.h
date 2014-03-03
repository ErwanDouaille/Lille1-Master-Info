/*
Author:
	DOUAILLE Erwan
	MIRANDA Yoan
*/

#include "hw_ini.h"
#include "hardware.h"
#include <string.h>

void read_sector(unsigned int,unsigned int,unsigned char*);

void write_sector(unsigned int,unsigned int,unsigned char*);

void format_sector(unsigned int,unsigned int,unsigned int,unsigned int);

void read_sector_n(unsigned int, unsigned int, unsigned char *, int);

void
dump(unsigned char *buffer,unsigned int buffer_size,int ascii_dump,int octal_dump);
