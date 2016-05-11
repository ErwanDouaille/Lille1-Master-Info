#ifndef PAPERSHEETDETECTOR_H
#define PAPERSHEETDETECTOR_H

#include <stdio.h>
#include <tchar.h>
#include <iostream>
#include <vector>
#include <math.h>

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include "papersheet.h"

class PaperSheetDetector
{
protected:
    CvCapture* cap;
    bool verbose;
    std::vector<PaperSheet*> _paperSheetVector;

    void initDebugWindows();
    void initializeCapture(int deviceID);
    void imageProcessing(IplImage* frame);
    void process(IplImage* frame);
    void paperSheetTracking(cv::Mat* mat_img, std::vector<std::vector<cv::Point_<int> > > contours, std::vector<cv::Vec<int, 4> > hierarchy);
    void paperSheetFollower(cv::Mat* mat_img, std::vector<cv::Point> approximationPoints);
    PaperSheet* addPaper(std::vector<cv::Point> points);

public:
    PaperSheetDetector(bool verbose);
    PaperSheetDetector(void);
    virtual ~PaperSheetDetector(void);
    std::vector<PaperSheet*> getPapers() const {return _paperSheetVector;}
    static void* run(void* arg);
};

#endif // PAPERSHEETDETECTOR_H
