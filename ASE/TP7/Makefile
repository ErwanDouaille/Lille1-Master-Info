# $Id: Makefile,v 1.1 2002/12/06 14:25:18 marquet Exp $
##############################################################################

ROOTDIR=back

CC	= gcc
CFLAGS	= -Wall
LIBDIR  = -L$(ROOTDIR)/lib
INCDIR  = -I$(ROOTDIR)/include
LIBS    = -lhardware

###------------------------------
### Main targets 
###------------------------------------------------------------
BINARIES= mkhd dmps frmt vm mkvol mknfs
OBJECTS	= $(addsuffix .o,\
	  drive mbr )

all: $(BINARIES) $(OBJECTS)

###------------------------------
### Binaries
###------------------------------------------------------------
mkhd: mkhd.o
	$(CC) $(CFLAGS) -o $@ $^ $(LIBDIR) $(LIBS)
dmps: dmps.o drive.o
	$(CC) $(CFLAGS) -o $@ $^ $(LIBDIR) $(LIBS)
frmt: frmt.o drive.o
	$(CC) $(CFLAGS) -o $@ $^ $(LIBDIR) $(LIBS)
vm: vm.o mbr.o drive.o 
	$(CC) $(CFLAGS) -o $@ $^ $(LIBDIR) $(LIBS)
mkvol: mkvol.o mbr.o drive.o 
	$(CC) $(CFLAGS) -o $@ $^ $(LIBDIR) $(LIBS)
mknfs: current.o mbr.o mknfs.o
	$(CC) $(CFLAGS) -o $@ $^ $(LIBDIR) $(LIBS)

###------------------------------
### #include dependences 
###------------------------------------------------------------
# you may fill these lines with "make depend"
mkhd.o: mkhd.c 
dmps.o: dmps.c 
frmt.o: frmt.c 
vm.o: vm.c 
mknfs.o: mknfs.c 

drive.o: drive.c drive.h
mbr.o: mbr.c drive.h mbr.h
mkvol.o: mkvol.c drive.h mbr.h
current.o: current.c

%.o: %.c
	$(CC) $(CFLAGS) -c $< $(INCDIR)

###------------------------------
### Misc.
###------------------------------------------------------------
.PHONY: clean realclean depend
clean:
	$(RM) *.o $(BINARIES)
realclean: clean 
	$(RM) vdiskA.bin vdiskB.bin
depend : 
	$(CC) -MM $(INCDIR) *.c

