void read_inode (unsigned int inumber, struct inode_s *inode){
	read_bloc_n(current_vol,inumber,inode,sizeof(inode));
}

void write_inode (unsigned int inumber, const struct inode_s *inode){
	write_bloc_n(current_vol,inumber,inode,sizeof(inode));
}

unsigned int create_inode(enum file_type_e type){
	struct inode_s inode;	
	inode.ino_type=type;
	inode.ino_size=0;
	inumber=new_bloc();
	if(inumber==0)
		return 0;
	write_inode(inumber,&inode);
	return inumber;
}

int delete_inode(unsigned int inumber){
	struct inode_s inode;
	read_inode(number,&inode);
	free_blocs(inode.ino_direct,NDIRECT);
	unsigned bloc[NINDIRECT];
	read_bloc(inode.ino_indirect,bloc);
	free_blocs(bloc,NINDIRECT);
	free_bloc(inode.ino_indirect);
	/*inode.ino_dbdirect*/
	free_bloc(inumber);
	return 1;	 
}

unsigned int vbloc_of_fbloc(unsigned int inumber, unsigned int fbloc, bool_t do_allocate){
	read_inode(inumber,&inode);
	if(fbloc<NDIRECT){
		return inode.ino_direct[fbloc];
	}
	fbloc-=NDIRECT;
	if(fbloc<NINDIRECT){
		if(inode.ino_indirect==0)
			return 0;
		unsigned bloc[NINDIRECT];
		read_bloc(,inode.ino_indirect,bloc,size);
		return bloc[fbloc];
	}
	fbloc-=NINDIRECT;
	if(inode.ino_dbdirect==0)
		return 0;
	unsigned bloc1[NINDIRECT],bloc2[NINDIRECT];
	unsigned index1=fbloc/NINDIRECT;
	unsigned index2=fbloc%NINDIRECT;
	/*bloc double direct*/
	return 1;
}

void free_blocs(unsigned blocs[],unsigned n){
	int i;
	for(i=0;i<n;i++)
		if(blocs[i]!=0)
			free_bloc(blocs[i]);
}
