#include "mbr.h"


#define SUPER 0
#define SUPER_MAGIC 0xACD


static struct super_s super;
static struct free_bloc_s fb;
static unsigned current_volume;

struct super_s{
	unsigned super_magic;
	unsigned super_serial;
	char super_name[32];
	unsigned super_root_inumber;
	unsigned super_first_free;
};

struct free_bloc_s{
	unsigned fb_nbbloc;
	unsigned fb_next;
};

void init_super(unsigned int vol);
void load_super(unsigned int vol);
unsigned int new_bloc();
void free_bloc(unsigned int bloc);
