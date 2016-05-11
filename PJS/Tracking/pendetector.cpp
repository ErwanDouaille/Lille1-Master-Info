#include "pendetector.h"

using namespace cv;
using namespace std;

PenDetector::PenDetector(bool verbose)
{
    this->verbose = verbose;
    this->initializeCapture(0);
    cout << "init done" << endl;
}

PenDetector::~PenDetector(void)
{
    this->verbose = false;
    this->initializeCapture(0);
}

void PenDetector::initializeCapture(int deviceID) {
    this->cap=cvCreateCameraCapture(deviceID);
}

void PenDetector::initDebugWindows() {
    cvNamedWindow("Debug Window",CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Debug Edge Window",CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Debug binarize Window",CV_WINDOW_AUTOSIZE);
    cvNamedWindow("Debug threshold Window",CV_WINDOW_AUTOSIZE);
}

bool PenDetector::hasPen() {
    return !(penPosition.x == -1 && penPosition.y == -1);
}

void PenDetector::imageProcessing(IplImage* frame){
    Mat mat_img(frame);

    Mat threshold_mat_img, detected_edges;
    std::vector<std::vector<cv::Point> > contours;
    vector<Vec4i> hierarchy;
    CvSize s = cvSize(frame->width, frame->height);
    int d = frame->depth;
    IplImage* R=cvCreateImage(s,d,1);
    cvSplit(frame, NULL, NULL, R, NULL);
    Mat red_mat_img(R);

    blur(red_mat_img, red_mat_img, Size(10,10));
    cv::threshold(red_mat_img, threshold_mat_img, 25, 255, cv::THRESH_BINARY);
    Canny(threshold_mat_img, detected_edges, 100, 100, 3);
    cv::findContours(detected_edges, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));
    if (contours.size() != 0) {

        RNG rng(12345);
        Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255) );

        for( int i = 0; i < contours.size(); i++ ) {
            if( contours[i].size() > 5 ) {
                ellipse( mat_img, fitEllipse(Mat(contours[i])), color, 2, 8 );
                penPosition = fitEllipse(Mat(contours[i])).center;
            }
        }
    } else
        penPosition = Point(-1,-1);
    if (this->verbose) {
        imshow("Debug Window", mat_img);
        imshow("Debug binarize Window",  red_mat_img);
        imshow("Debug threshold Window",  threshold_mat_img);
        imshow("Debug Edge Window", detected_edges);
    }
    cvReleaseImage(&R);

}

void* PenDetector::run(void* arg) {
    PenDetector * detector = (PenDetector*) arg;
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
