# relative paths to lib p3d and media
P3D_PATH = p3d
MEDIA_PATH = ../media

# common QT project configuration
QT += core gui opengl # should be ok for Qt 4.8 and Qt 5.1
CONFIG += debug_and_release
CONFIG(debug,debug|release) {
  TARGET=$$join(TARGET,,,"_d")
}
TEMPLATE = app

DESTDIR = $$_PRO_FILE_PWD_/bin
DEFINES += MEDIA_PATH=\\\"$$MEDIA_PATH\\\"


# QT >=5 and QT <5 compatibility
greaterThan(QT_MAJOR_VERSION, 4) {
QT += widgets
CONFIG += c++11
}
else {
  QMAKE_CXXFLAGS += -std=c++11 # C++ 11 / -std=c++0x for old compilers
}

# includes glew : for compatibility reason, glew *MUST* be installed
CONFIG += with_glew # uncomment this line if glew is required
with_glew:DEFINES+=WITH_GLEW # to check if #include <GL/glew.h> should be done (see glsupport.h and GLView.cpp)

# linux config : tested on ubuntu 12/13 32bits, fedora 20 64bits
unix:!macx:with_glew {
    CONFIG += link_pkgconfig
    PKGCONFIG += glew
}

# mac os config : ** not tested **
macx {
    INCLUDEPATH += /opt/local/include
    with_glew:LIBS+=-lglew
    LIBS += -framework OpenGL
}

# win32 config : tested on win7 32bits (qt mingw)
win32 {
  CONFIG += console
  with_glew {
DEFINES+=GLEW_STATIC
LIBS+=-lglew32s
}
  DEFINES+=_STDCALL_SUPPORTED
  LIBS+= -lopengl32
}


INCLUDEPATH += src/application/ src/p3d/gui/ 
DEPENDPATH += src/application/ src/p3d/gui/ 

SOURCES += \
	src/application/GLApplication.cpp \
	src/p3d/gui/Tools.cpp \
	src/p3d/gui/GLWidget.cpp \
	src/p3d/gui/GLText.cpp \
	src/p3d/gui/main.cpp \
	src/p3d/gui/MainWindow.cpp
HEADERS += \
	src/application/GLApplication.h \
	src/p3d/gui/Tools.h \
	src/p3d/gui/glsupport.h \
	src/p3d/gui/GLText.h \
	src/p3d/gui/GLWidget.h \
	src/p3d/gui/MainWindow.h
OTHER_FILES +=\
	media/lagoon.jpg\
	media/simple.frag\
	media/simple.vert
