OSTYPE := $(shell uname -s)
INC_DIRS = /usr/include/ni
SRC_FILES = \
	main.cpp 

EXE_NAME = PJI-Kinect
USED_LIBS += -lOpenNI `pkg-config --libs clutter-1.0 liblo`
TARGET_SYS_ROOT = /

# take this file's dir
COMMON_CPP_MAKE_FILE_DIR = $(dir $(lastword $(MAKEFILE_LIST)))
CFG = Release

ifndef PLATFORM
    MACHINE = $(shell uname -m)
    $(info Platform=$(shell uname -m))
    ifneq (,$(findstring x86,$(MACHINE)))
        PLATFORM = x86
    else ifneq (,$(findstring i686,$(MACHINE)))
        PLATFORM = x86
    else ifneq (,$(findstring i386,$(MACHINE)))
        PLATFORM = x86
    else ifneq (,$(findstring arm,$(MACHINE)))
        PLATFORM = Arm
    else
        $(error Unknown Platform)
    endif
endif

# expand file list
SRC_FILES_LIST = $(wildcard $(SRC_FILES))

# define the intermediate directory
INT_DIR = $(CFG)

# define output directory
OUT_DIR = $(CFG)

# full path to output file
OUTPUT_FILE = $(OUT_DIR)/$(OUTPUT_NAME)

# take this file's dir
COMMON_MAK_DIR = $(dir $(lastword $(MAKEFILE_LIST)))

# define a function to figure .o file for each source file (placed under intermediate directory)
SRC_TO_OBJ = $(addprefix ./$(INT_DIR)/,$(addsuffix .o,$(notdir $(basename $1))))

# create a list of all object files
OBJ_FILES = $(call SRC_TO_OBJ,$(SRC_FILES_LIST))

# define a function to translate any source file to its dependency file (note that the way we create
# dep files, as a side affect of compilation, always puts the files in the INT_DIR with suffix .d)
SRC_TO_DEP = $(addprefix ./$(INT_DIR)/,$(addsuffix .d,$(notdir $(basename $1))))

# create a list of all dependency files
DEP_FILES = $(call SRC_TO_DEP,$(SRC_FILES_LIST))

# append the -I switch to each include directory
INC_DIRS_OPTION = $(foreach dir,$(INC_DIRS),-I$(dir))

# append the -l switch to each library used
USED_LIBS_OPTION = $(foreach lib,$(USED_LIBS), $(lib))

CFLAGS += $(INC_DIRS_OPTION) $(DEFINES_OPTION)
LDFLAGS += $(USED_LIBS_OPTION)

ifneq "$(EXE_NAME)" ""
	OUTPUT_NAME = $(EXE_NAME)
	OUTPUT_COMMAND = $(CXX) -o $(OUTPUT_FILE) $(OBJ_FILES) $(LDFLAGS)
endif

define CREATE_SRC_TARGETS
# create a target for the object file (the CXX command creates both an .o file
# and a .d file)
$(call SRC_TO_OBJ,$1) : $1 | $(INT_DIR)
	$(CXX) -c $(CFLAGS) -o $$@ $$<
endef


#############################################################################
# Targets
#############################################################################
.PHONY: all clean clean-$(OUTPUT_FILE)

# define the target 'all' (it is first, and so, default)
all: $(OUTPUT_FILE)

# Intermediate directory
$(INT_DIR): 
	mkdir -p $(INT_DIR)

# Output directory
$(OUT_DIR):
	mkdir -p $(OUT_DIR)
# create targets for each source file
$(foreach src,$(SRC_FILES_LIST),$(eval $(call CREATE_SRC_TARGETS,$(src))))

# include all dependency files (we don't need them the first time, so we can use -include)
-include $(DEP_FILES)

$(OUTPUT_FILE): $(OBJ_FILES)
	$(OUTPUT_COMMAND)

# Final output file
$(OUTPUT_FILE): $(SRC_FILES_LIST) | $(OUT_DIR)

clean-$(OUTPUT_FILE):
	rm -rf $(OUTPUT_FILE)

clean-objs:
	rm -rf $(OBJ_FILES)
	
clean-defs:
	rm -rf $(DEP_FILES)
	
clean: clean-$(OUTPUT_FILE) clean-defs clean-objs




