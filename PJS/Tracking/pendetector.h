#ifndef PENDETECTOR_H
#define PENDETECTOR_H

#include <stdio.h>
#include <tchar.h>
#include <iostream>
#include <vector>
#include <math.h>

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include "Pen.h"

class PenDetector
{
protected:
    CvCapture* cap;
    bool verbose;
    Point penPosition;

    void initDebugWindows();
    void initializeCapture(int deviceID);
    void imageProcessing(IplImage* frame);

public:
    PenDetector(bool verbose);
    PenDetector(void);
    virtual ~PenDetector(void);
    Point getPenPosition() const {return penPosition;}
    bool hasPen();
    static void* run(void* arg);
};

#endif // PENDETECTOR_H
