CC=g++
LDFLAGS=-lm -lopencv_core -lopencv_highgui -lopencv_calib3d
LIBS=
INCLUDES= -I/usr/include
TARGET= $(wildcard *.cpp)
OBJ= $(TARGET:.cpp=.o)

all: dense-matching

dense-matching: $(OBJ)
	$(CC) -o $@ $^ $(LDFLAGS)

%.o: %.cpp
	$(CC) -o $@ -c $< $(CFLAGS) $(INCLUDES)

clean:
	rm *.o

