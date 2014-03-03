CC=gcc
EXEC=hw.o chang_ctx


all: $(EXEC)

hw.o: hw.c hw.h
	$(CC) -c hw.c

chang_ctx:chang_ctx.o hw.o
	$(CC) -o $@ $^ 

clean:
	rm -rf *.o

mrproper: clean
	rm -rf $(EXEC);
