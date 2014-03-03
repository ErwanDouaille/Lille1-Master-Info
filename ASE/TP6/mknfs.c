#include "mknfs.h"

void init_super(unsigned int vol){
	super.super_magic=SUPER_MAGIC;
	super.super_root_inumber=0;
	super.super_first_free=1;
	write_bloc_n(vol, SUPER, (unsigned char*)&super, sizeof(struct super_s));
	fb.fb_nbbloc=mbr.mbr_vols[vol].vol_nsector;
	fb.fb_next=0;
	write_bloc_n(vol, 1, (unsigned char*)&fb, sizeof(struct free_bloc_s));
}

void load_super(unsigned int vol){
	read_bloc_n(vol,SUPER, (unsigned char*)&super,sizeof(struct super_s));
	assert(super.super_magic==SUPER_MAGIC);
	current_volume = vol;
}

unsigned int new_bloc(){
	int new;
	//cas ou il n'y a plus de bloc libre
	if(super.super_first_free==0)
		return 0;
	read_bloc_n(current_volume, super.super_first_free, (unsigned char*)&fb, sizeof(struct free_bloc_s));
	new = super.super_first_free;
	if(fb.fb_nbbloc==1)	
		super.super_first_free = fb.fb_next;
	else {
		super.super_first_free++;
		fb.fb_nbbloc--;
		write_bloc_n(current_volume, super.super_first_free, (unsigned char*)&fb, sizeof(struct free_bloc_s));
	}
	return new;
}


void free_bloc(unsigned int bloc){
	fb.fb_nbbloc = 1;
	fb.fb_next = super.super_first_free;
	super.super_first_free = bloc;
	write_bloc_n(current_volume, bloc, (unsigned char*)&fb, sizeof(struct free_bloc_s));
}
