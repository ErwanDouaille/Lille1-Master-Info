/* ------------------------------
   $Id: ifile.c,v 1.3 2009/11/24 16:44:39 marquet Exp $
   ------------------------------------------------------------

   File manipulation. 
   Basic version; access file via inode number.
   Philippe Marquet, october 2009
   
*/

#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

#include "inode.h"
#include "tools.h"
#include "ifile.h"

#ifdef SOL
#   include "super+sol.h"
#   include "vol+sol.h"
#else
#   error "You need to put here your own .h includes."
#endif

/* the file bloc number of a given character position in a file */  
#define bloc_of_pos(pos) ((pos) / DATA_BLOC_SIZE)
/* the index in a bloc of given character position in a file */  
#define ibloc_of_pos(pos) ((pos) % DATA_BLOC_SIZE)

/*------------------------------
  Create and delete file
  ------------------------------------------------------------*/

unsigned int 
create_ifile(enum file_type_e type) 
{
    unsigned int inumber; 

    inumber = create_inode(type);
    ffatal(inumber, "unable to create inode"); 

    return inumber; 
}

int
delete_ifile(unsigned int inumber)
{
    int status;

    status = delete_inode(inumber);
    ffatal(status == RETURN_SUCCESS, "unable to delete inode");

    return RETURN_SUCCESS;
}

/*------------------------------
  Open, close and flush file
  ------------------------------------------------------------*/

int
open_ifile(file_desc_t *fd, unsigned int inumber)
{
    unsigned int first_bloc;
    struct inode_s inode; 
    /* we are opening the designed file! */
    fd->fds_inumber = inumber;
    read_inode (inumber, &inode);    
    
    /* other trivial init */
    fd->fds_size = inode.ind_size;
    fd->fds_pos = 0;

    /* the buffer is full of zeros if the first bloc is zero, loaded
       with this first bloc otherwise */
    first_bloc = vbloc_of_fbloc(inumber, 0, FALSE);
    if (! first_bloc) 
	memset(fd->fds_buf, 0, DATA_BLOC_SIZE);
    else
	read_bloc(current_volume, first_bloc, fd->fds_buf);

    /* last trivial */
    fd->fds_dirty = FALSE;

    return RETURN_SUCCESS;
}

void
close_ifile(file_desc_t *fd)
{
    struct inode_s inode;
    
    /* if the buffer is dirty, flush the file */
    flush_ifile(fd);
    
    /* update the inode information (size) */
    read_inode(fd->fds_inumber, &inode);
    inode.ind_size = fd->fds_size;
    write_inode(fd->fds_inumber, &inode);
}

/* note that flush don't need to worry about the bloc allocation; a
   previous write operation has already done it. */ 
void
flush_ifile(file_desc_t *fd)
{
    unsigned int fbloc; /* bloc index in the file */
    unsigned int vbloc; /* bloc index in the volume */

    if (fd-> fds_dirty) {
	/* compute the number of the bloc on the volume associated to
	   the buffer */ 
	fbloc = bloc_of_pos(fd->fds_pos);
	vbloc = vbloc_of_fbloc(fd-> fds_inumber, fbloc, TRUE);

	/* write back the buffer */
	write_bloc(current_volume, vbloc, fd->fds_buf);

	/* done */
	fd-> fds_dirty = FALSE ; 
    }
}

/*------------------------------
  Seek in a file
  ------------------------------------------------------------*/

/* move the cursor of offset positions. */
void 
seek_ifile(file_desc_t *fd, int offset)
{     
    unsigned int old_pos = fd->fds_pos;
    unsigned int fbloc, vbloc; 
    
    /* update the position */
    fd->fds_pos += offset;

    /* does the seek imply a jump in another bloc? */
    if (bloc_of_pos(fd->fds_pos) != bloc_of_pos(old_pos)) {
	/* flush */
	flush_ifile(fd);
    
	/* the bloc index of the new buffer */
	fbloc = bloc_of_pos(fd->fds_pos);
	vbloc = vbloc_of_fbloc(fd->fds_inumber, fbloc, FALSE);

	if (! vbloc)
	    /* the bloc #0 is full of zeros */
	    memset(fd->fds_buf, 0, BLOC_SIZE);
	else
	    /* load the bloc */
	    read_bloc(current_volume, vbloc, fd->fds_buf);
    }
}

/* move the cursor at offset */
void 
seek2_ifile(file_desc_t *fd, int offset)
{
    seek_ifile(fd, offset - fd->fds_pos);
}

/*------------------------------
  Read a char in a file 
  ------------------------------------------------------------*/
int 
readc_ifile(file_desc_t *fd)
{
    char c;
    
    /* eof? */
    if (fd->fds_pos > fd->fds_size)
	return READ_EOF; 

    /* the data is in the buffer, just return it */
    c = fd->fds_buf[ibloc_of_pos(fd->fds_pos)];
    
    /* seek + 1 */
    seek_ifile(fd, 1);
    
    return c; 
}

/*------------------------------
  Write a char in a file 
  ------------------------------------------------------------*/

/* return the  pos in the file ; RETURN_FAILURE in case of error */
int 
writec_ifile(file_desc_t *fd, char c)
{
    unsigned int ibloc;

    /* write the char in the buffer */
    fd->fds_buf[ibloc_of_pos(fd->fds_pos)] = c;

    /* first write in the bloc ? ensure the data bloc allocation */
    if (! fd->fds_dirty) {
        ibloc = vbloc_of_fbloc(fd->fds_inumber, bloc_of_pos(fd->fds_pos), TRUE);
        if (! ibloc) 
            return RETURN_FAILURE;
        fd->fds_dirty = TRUE;
    }
    
    /* is the buffer full? */
    if (ibloc_of_pos(fd->fds_pos) == BLOC_SIZE-1) {
	/* write the buffer */
        ibloc = vbloc_of_fbloc(fd->fds_inumber, bloc_of_pos(fd->fds_pos), FALSE);
	write_bloc(current_volume, ibloc, fd->fds_buf);
	/* read the new buffer */
	ibloc = vbloc_of_fbloc(fd->fds_inumber,
                               bloc_of_pos(fd->fds_pos+1), FALSE);
	if (! ibloc) 
	    memset(fd->fds_buf, 0, BLOC_SIZE);
	else
	    read_bloc(current_volume, ibloc, fd->fds_buf);
	fd->fds_dirty = FALSE;
    }
    
    /* update the file cursor and size */
    if (fd->fds_size < fd->fds_pos)
	fd->fds_size = fd->fds_pos;
    fd->fds_pos++;
    
    /* the position of the written char */
    return fd->fds_pos - 1;
}

/*------------------------------
  Read from file
  ------------------------------------------------------------*/
int
read_ifile(file_desc_t *fd, void *buf, unsigned int nbyte)
{
    unsigned int i;
    int c; 

    /* eof? */
    if (fd->fds_pos >= fd->fds_size)
	return READ_EOF; 

    /* read one by one */
    for (i = 0; i < nbyte; i++) {
	if ((c = readc_ifile(fd)) == READ_EOF) {
	    return i; 
	}
	*((char *)buf+i) = c; 
    }

    return i;
}

/*------------------------------
  Write to file 
  ------------------------------------------------------------*/
int
write_ifile(file_desc_t *fd, const void *buf, unsigned int nbyte)
{
    int i; 

    /* write one by one */
    for (i = 0; i < nbyte; i++) {
	if (writec_ifile(fd, *((char *)buf+i)) == RETURN_FAILURE)
	    return RETURN_FAILURE;
    }

    return nbyte;
}
