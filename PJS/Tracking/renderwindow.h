#ifndef RENDERWINDOW_H
#define RENDERWINDOW_H

#include <stdio.h>
#include <tchar.h>
#include <iostream>
#include <vector>
#include <math.h>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include "papersheet.h"
#include "virtualbutton.h"

class RenderWindow
{
private:
    CvCapture* cap;
    vector<PaperSheet*> papers;
    Point penPosition;

public:
    RenderWindow();
    ~RenderWindow();
    void initializeCapture(int deviceID);

    void draw(IplImage* img);
    vector<PaperSheet*> getPapers() const {return papers;}
    Point getPenPosition() const {return penPosition;}
    void updateData(vector<PaperSheet*> papers, Point penPosition);
    static void* run(void* arg);
};

#endif // RENDERWINDOW_H
