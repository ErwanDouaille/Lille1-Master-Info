
CC = gcc
CFLAGS  = -Wall -Werror 
CFLAGS  += -g
OBJ = display_stack try.o try_mul

all: $(OBJ)

try.o: try.c try.h
	$(CC) -c $(CFLAGS) try.c

display_stack: display_stack.o
	$(CC) $(CFLAGS) -o $@ display_stack.o
display_stack.o: display_stack.c 
	$(CC) -c $(CFLAGS) display_stack.c

try_mul: try_mul.o try.o
	$(CC) $(CFLAGS) -o $@ try_mul.o try.o
try_mul.o: try_mul.c 
	$(CC) -c $(CFLAGS) try_mul.c

.PHONY: clean realclean 
clean: 
	$(RM) *.o
realclean:clean
	$(RM) *~
	$(RM) $(OBJ)
