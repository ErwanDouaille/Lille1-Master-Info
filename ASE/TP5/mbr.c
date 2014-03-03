/*
Author:
	DOUAILLE Erwan
	MIRANDA Yoan
*/

#include "mbr.h"
#include "hw_ini.h"
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

void save_mbr(){
	assert(sizeof(struct mbr_s)<=HDA_SECTORSIZE);
	unsigned char buffer[HDA_SECTORSIZE];
	memcpy(buffer,&mbr,sizeof(struct mbr_s));
	write_sector(0,0,buffer);
}

void load_mbr(){
	int i;
	unsigned char buffer[HDA_SECTORSIZE];
	assert(sizeof(struct mbr_s)<=HDA_SECTORSIZE);
	read_sector_n(0,0,((unsigned char*)(&mbr)),sizeof(mbr));
	memcpy(&mbr,buffer,sizeof(HDA_SECTORSIZE));
	if(mbr.mbr_magic!=MBR_MAGIC){
		mbr.mbr_magic=MBR_MAGIC;
		for(i=0;i<MAXVOL;i++)
			mbr.mbr_vols[i].vol_voltype=VT_NO_VOL;
	}
}

unsigned cylinder_of_bloc(int vol, int n_bloc){
        unsigned cylinder = mbr.mbr_vols[vol].vol_cylinder;
        unsigned sector = mbr.mbr_vols[vol].vol_sector;
        return (cylinder+(sector+n_bloc)/HDA_MAXSECTOR);
}

unsigned sector_of_bloc(int vol, int n_bloc){
        unsigned sector = mbr.mbr_vols[vol].vol_sector;
        return ((sector+n_bloc)%HDA_MAXSECTOR);        
}

void read_bloc(unsigned int vol, unsigned int nbloc, unsigned char *buffer){
	unsigned cylinder = cylinder_of_bloc(vol, nbloc);
	unsigned sector = sector_of_bloc(vol, nbloc);
	read_sector(cylinder, sector, buffer);
}

void write_bloc(unsigned int vol, unsigned int nbloc, unsigned char *buffer){
	unsigned cylinder = cylinder_of_bloc(vol, nbloc);
	unsigned sector = sector_of_bloc(vol, nbloc);
	write_sector(cylinder, sector, buffer);
}
void format_vol(unsigned int vol){
	unsigned cylinder = mbr.mbr_vols[vol].vol_cylinder;
	unsigned sector = mbr.mbr_vols[vol].vol_sector;
	unsigned nsector = mbr.mbr_vols[vol].vol_nsector;
	format_sector(cylinder, sector, nsector, 0);

}

char* printf_vol_type(unsigned int type){
	switch (type){
                case VT_NO_VOL:
			return "VT_NO_VOL";
                case VT_BASE:
			return "VT_BASE";
                case VT_AUX:
			return "VT_AUX";
		case VT_OTHER:
			return "VT_OTHER";
        }
	return "UNKNOW";
}

void printf_vol(unsigned int i){
        printf("hd(%d)\t%d\t(%d,%d)\t(%d,%d)\t%s\n\n",
		i, 
		mbr.mbr_vols[i].vol_sector, 
		mbr.mbr_vols[i].vol_cylinder, 
		mbr.mbr_vols[i].vol_sector, 
		cylinder_of_bloc(i, mbr.mbr_vols[i].vol_sector),
 		sector_of_bloc(i,mbr.mbr_vols[i].vol_sector-1),
		printf_vol_type(mbr.mbr_vols[i].vol_voltype));
}

void list_volumes(){
	int i;
	if(mbr.mbr_nbvol>MAXVOL){
		printf("Invalid volume list\n");
		exit(EXIT_FAILURE);	
	}
	printf("Volume\tsize\t(c,s)\t(lc,ls)\ttype\n\n");
	for(i=0;i<mbr.mbr_nbvol;i++)
		printf_vol(i);
}

void check_sector_size(){
        int real_value;
        _out(HDA_CMDREG, CMD_DSKINFO);
        real_value = (_in(HDA_DATAREGS+4)<<8)|_in(HDA_DATAREGS+5);
        if(real_value != HDA_SECTORSIZE){
                fprintf(stderr, "Error in sectors size\n\tsize found: %d\n\tsize expected: %d\n", real_value,HDA_SECTORSIZE);
                exit(EXIT_FAILURE);
        }
}

static void empty_it(){
    return;
}

void init_mbr(){ 
	int i;
	if(init_hardware("hardware.ini") == 0) {
                fprintf(stderr, "Error in hardware initialization\n");
                exit(EXIT_FAILURE);
        }
        check_sector_size();
        for(i=0; i<16; i++)
               IRQVECTOR[i] = empty_it;
        
	load_mbr();
}

void new_volume(unsigned int sector, unsigned int cylinder, unsigned int nbsector, unsigned int type){
	if(mbr.mbr_nbvol == MAXVOL)
		fprintf(stderr, "Max number of volumes reached\n");
	else {
		struct vol_descr_s new_vol;
		new_vol.vol_sector = sector;
		new_vol.vol_cylinder = cylinder;
		new_vol.vol_nsector = nbsector;
		new_vol.vol_voltype = type;

		mbr.mbr_vols[mbr.mbr_nbvol] = new_vol;
		mbr.mbr_nbvol++;
	}
}

void delete_volume(unsigned int volume){
	int i;
	if (volume<0 || volume >= mbr.mbr_nbvol)
		fprintf(stderr, "Wrong volume\n");
	else {
		if(mbr.mbr_vols[volume].vol_voltype==VT_OTHER)
			fprintf(stderr, "Cannot be deleted due to type: %s\n", printf_vol_type(mbr.mbr_vols[volume+1].vol_voltype));
		else{
			for (i = volume+1;i<mbr.mbr_nbvol;i++){
				mbr.mbr_vols[i-1] = mbr.mbr_vols[i];
			}
			mbr.mbr_nbvol--;  
		}
	} 
}
