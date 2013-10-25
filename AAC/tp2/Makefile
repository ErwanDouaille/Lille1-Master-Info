CC=gcc
EXEC=jeuAlgo


all: $(EXEC)


jeuAlgo:jeuAlgo.o
	$(CC) -o $@ $^ 

clean:
	rm -rf *.o

mrproper: clean
	rm -rf $(EXEC);
