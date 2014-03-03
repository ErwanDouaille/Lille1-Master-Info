CC=gcc
EXEC=chang_ctx


all: $(EXEC)

chang_ctx:chang_ctx.o
	$(CC) -o $@ $^ 


clean:
	rm -rf *.o

mrproper: clean
	rm -rf $(EXEC);
