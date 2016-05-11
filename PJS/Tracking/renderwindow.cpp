#include "renderwindow.h"


using namespace cv;
using namespace std;

RenderWindow::RenderWindow()
{
    this->initializeCapture(0);
}

RenderWindow::~RenderWindow()
{

}

void RenderWindow::initializeCapture(int deviceID) {
    this->cap=cvCreateCameraCapture(deviceID);
}

static string intToString (int a)
{
    ostringstream temp;
    temp<<a;
    return temp.str();
}

void RenderWindow::draw(IplImage* frame) {
    Mat img(frame);
    RNG rng(12345);
    cv::Point aPoint = this->getPenPosition();
    vector<PaperSheet*> papers = this->getPapers();
    for (size_t i = 0; i < papers.size(); i++) {
        PaperSheet* aPaper = papers.at(i);
        vector<Point> pointsVector = aPaper->getPoints();
        Point *points;
        points = &pointsVector[0];
        int nbtab = pointsVector.size();
        polylines(img, (const cv::Point **) &points, &nbtab, 1, true, CV_RGB(255,0,0));
        putText(img, intToString(aPaper->getId()), pointsVector.at(0), FONT_HERSHEY_SIMPLEX, 1, CV_RGB(255,0,0));

        std::vector<VirtualButton*> buttonList = aPaper->getVirtualBouttons();
        for (size_t k = 0; k < buttonList.size(); k ++) {
            VirtualButton* aButton = buttonList.at(k);
            //aButton->get
            //rectangle(img, rec,  CV_RGB(255,0,0) );
        }
    }
    imshow("PJS-Window", img);
}

void RenderWindow::updateData(vector<PaperSheet*> papers, Point penPosition) {
    this->papers = papers;
    this->penPosition = penPosition;
}

void* RenderWindow::run(void* arg) {
    RenderWindow * renderWindow = (RenderWindow*) arg;
    IplImage* frame;
    cvNamedWindow("PJS-Window",CV_WINDOW_AUTOSIZE);
    while(true) {
        frame = cvQueryFrame(renderWindow->cap);
        if(frame) {
            renderWindow->draw(frame);
        }
        char key = cvWaitKey(10);
        if (key == 27)
            break;
    }
    cvReleaseCapture(&(renderWindow->cap));
    cvDestroyAllWindows();
    return 0;
}
