TEMPLATE = app
CONFIG += console
CONFIG -= app_bundle
CONFIG -= qt

SOURCES += main.cpp \
    papersheet.cpp \
    papersheetdetector.cpp \
    virtualbutton.cpp \
    handtrackinglistener.cpp \
    hand.cpp \
    point3d.cpp \
    androidtcpclient.cpp \
    pen.cpp \
    pendetector.cpp \
    renderwindow.cpp

HEADERS += \
    papersheet.h \
    papersheetdetector.h \
    virtualbutton.h \
    handtrackinglistener.h \
    hand.h \
    point3d.h \
    androidtcpclient.h \
    pen.h \
    pendetector.h \
    renderwindow.h

INCLUDEPATH += C:\opencv-mingw\install\include
INCLUDEPATH += C:\Programmes\OpenNI2\Include
win32: LIBS += -L$$PWD -lwsock32 -lws2_32
LIBS += -LC:\opencv-mingw\install\x64\mingw\bin \
                -lopencv_core2410 \
                -lopencv_highgui2410 \
                                -lopencv_imgproc2410 \
                                -lopencv_calib3d2410

LIBS += -L C:\Programmes\OpenNI2\Lib \
                -OpenNI2.lib
