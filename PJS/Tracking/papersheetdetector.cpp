#include "papersheetdetector.h"


using namespace cv;
using namespace std;

PaperSheetDetector::PaperSheetDetector(bool verbose)
{
    this->verbose = verbose;
    this->initializeCapture(0);
    cout << "init done" << endl;
}

PaperSheetDetector::~PaperSheetDetector(void)
{
    this->verbose = false;
    this->initializeCapture(0);
}

static string intToString (int a)
{
    ostringstream temp;
    temp<<a;
    return temp.str();
}

static double angle( Point pt1, Point pt2, Point pt0 )
{
    double dx1 = pt1.x - pt0.x;
    double dy1 = pt1.y - pt0.y;
    double dx2 = pt2.x - pt0.x;
    double dy2 = pt2.y - pt0.y;
    return (dx1*dx2 + dy1*dy2)/sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
}

static Point point4(Point p1 ,Point p2, Point p3){
    Point oppose;
    std::vector<Point> hypothenuse;
    double p12, p13, p23;
    p12=pow(p1.x-p2.x,2)+pow(p1.y-p2.y,2);
    p13=pow(p1.x-p3.x,2)+pow(p1.y-p3.y,2);
    p23=pow(p2.x-p3.x,2)+pow(p2.y-p3.y,2);
    if(p12>p13 && p12>p23){
        oppose=p3;
        hypothenuse.push_back(p1-p3);
        hypothenuse.push_back(p2-p3);
    }
    else if(p13>p12 && p13>p23){
        oppose=p2;
        hypothenuse.push_back(p1-p2);
        hypothenuse.push_back(p3-p2);
    }
    else {
        oppose=p1;
        hypothenuse.push_back(p2-p1);
        hypothenuse.push_back(p3-p1);
    }
    return oppose+hypothenuse.at(0)+hypothenuse.at(1);
}

void PaperSheetDetector::initializeCapture(int deviceID) {
    this->cap=cvCreateCameraCapture(deviceID);
}

void PaperSheetDetector::initDebugWindows() {
    cvNamedWindow("Debug Window",CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Debug Edge Window",CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Debug binarize Window",CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Debug threshold Window",CV_WINDOW_AUTOSIZE);
}

static bool containsSimilarPoint(PaperSheet* paper, vector<Point> pointList, Point aPoint){
    for (size_t i = 0; i < pointList.size(); i++) {
        if(paper->pointsAreClose(pointList.at(i), aPoint))
            return true;
    }
    return false;
}

void PaperSheetDetector::paperSheetFollower(Mat* mat_img, std::vector<cv::Point> approximationPoints) {
    if (approximationPoints.size() > 4) {
        int nb = 0;
        for (size_t j = 0; j < this->_paperSheetVector.size(); j++) {
            PaperSheet* paper = this->_paperSheetVector.at(j);
            int nb = 0;
            vector<Point> listOfPoint;
            for (size_t k = 0; k < approximationPoints.size(); k++) {
                Point aPoint = approximationPoints.at(k);
                if(paper->hasCornerPoint(aPoint)) {
                    if (!containsSimilarPoint(paper, listOfPoint, aPoint)) {
                        nb++;
                        listOfPoint.push_back(aPoint);
                    }
                }
            }
            if (nb == 3) {
                Point aPoint = point4(listOfPoint.at(0),listOfPoint.at(1),listOfPoint.at(2));
                listOfPoint.push_back(aPoint);
                paper->setPoints(listOfPoint);
            }
        }
    }
}

void PaperSheetDetector::paperSheetTracking(Mat* mat_img, std::vector<std::vector<cv::Point_<int> > > contours, std::vector<cv::Vec<int, 4> > hierarchy) {
    RNG rng(12345);
    vector<Point> approx;

    for (size_t i = 0; i < contours.size(); i++) {
        approxPolyDP(Mat(contours[i]), approx, arcLength(Mat(contours[i]), true)*0.02, true);
        if (approx.size() == 4 && fabs(contourArea(Mat(approx))) > 100 && isContourConvex(Mat(approx))) {
            double maxCosine = 0;
            for (int j = 2; j < 5; j++) {
                double cosine = fabs(angle(approx[j%4], approx[j-2], approx[j-1]));
                maxCosine = MAX(maxCosine, cosine);
            }
            if (maxCosine < 0.3) {
                Scalar color = Scalar(rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255));
                vector<Point> listPoint;
                for (size_t p=0; p < approx.size(); p++ )
                    listPoint.push_back(approx.at(p));
                drawContours(*mat_img, contours, i, color, 2, 8, hierarchy, 0, Point());
                PaperSheet* paper = this->addPaper(listPoint);
                putText(*mat_img, intToString(paper->getId()), listPoint.at(0), FONT_HERSHEY_SIMPLEX, 1, color);
            }
        }
        if (approx.size() > 4)
            this->paperSheetFollower(mat_img, approx);
    }
}

void PaperSheetDetector::imageProcessing(IplImage* frame){
    Mat mat_img(frame);
    Mat gray_mat_img, threshold_mat_img, detected_edges;
    std::vector<std::vector<cv::Point> > contours;
    vector<Vec4i> hierarchy;

    cvtColor(mat_img, gray_mat_img, CV_BGR2GRAY);
    blur(gray_mat_img, gray_mat_img, Size(10,10));
    cv::threshold(gray_mat_img, threshold_mat_img, 140, 255, cv::THRESH_BINARY | cv::THRESH_OTSU);
    Canny(threshold_mat_img, detected_edges, 100, 100, 3);
    cv::findContours(detected_edges, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));

    this->paperSheetTracking(&mat_img, contours, hierarchy);

    if (this->verbose) {
        imshow("Debug Window", mat_img);
        imshow("Debug binarize Window",  gray_mat_img);
        imshow("Debug threshold Window",  threshold_mat_img);
        imshow("Debug Edge Window", detected_edges);
    }
}

void checkRemovedPaper() {
    //TODO
}

PaperSheet* PaperSheetDetector::addPaper(vector<Point> listPoint) {
    PaperSheet* paper;
    if(this->_paperSheetVector.empty()) {
        paper = new PaperSheet(this->_paperSheetVector.size(), listPoint);
        this->_paperSheetVector.push_back(paper);
        return paper;
    }
    vector<PaperSheet*> list = this->_paperSheetVector;
    int similarId = -1;
    for (size_t i = 0; i < list.size(); i++) {
        if(list.at(i)->isCloseFrom(listPoint)) {
            similarId = i;
            i = list.size();
        }
    }
    if (similarId==-1) {
        paper = new PaperSheet(this->_paperSheetVector.size(), listPoint);
        this->_paperSheetVector.push_back(paper);
    } else {
        paper = this->_paperSheetVector.at(similarId);
        paper->setPoints(listPoint);
    }
    return paper;
}

void* PaperSheetDetector::run(void* arg) {
    PaperSheetDetector * detector = (PaperSheetDetector*) arg;
    IplImage* frame;

    if (detector->verbose)
        detector->initDebugWindows();
    while(true) {
        frame = cvQueryFrame(detector->cap);
        if(frame)
            detector->imageProcessing(frame);
        char key = cvWaitKey(10);
        if (key == 27)
            break;
    }
    cvReleaseCapture(&(detector->cap));
    cvDestroyAllWindows();
    return 0;
}
