#include <iostream>

#include "papersheetdetector.h"
#include "handtrackinglistener.h"
#include "androidtcpclient.h"
#include "pendetector.h"
#include "renderwindow.h"

#include <opencv2/core/core.hpp>
#include <sys/time.h>

using namespace std;
using namespace cv;

timespec timer, old_timer;
Point old_point = Point(0,0);

HandTrackingListener* handTrackingListener = new HandTrackingListener();
PaperSheetDetector* paperSheetDetector = new PaperSheetDetector(false);
PenDetector* penDetector = new PenDetector(true);
AndroidTCPClient* androidListener = new AndroidTCPClient();
RenderWindow* renderWindow = new RenderWindow();

pthread_t paperSheetDetectorThread, penDetectorThread, handTrackingListenerThread, androidListenerThread, renderWindowThread;

bool didntMove(Point point, Point old_point, int precision) {
    return point.x-precision < old_point.x && point.x+precision > old_point.x &&
            point.y-precision < old_point.y && point.y+precision > old_point.y;
}

void initThreads() {
    pthread_create(&handTrackingListenerThread, NULL, &HandTrackingListener::run, handTrackingListener);
    pthread_create(&paperSheetDetectorThread, NULL, &PaperSheetDetector::run, paperSheetDetector);
    pthread_create(&renderWindowThread, NULL, &RenderWindow::run, renderWindow);
    pthread_create(&penDetectorThread, NULL, &PenDetector::run, penDetector);
    pthread_create(&androidListenerThread, NULL, &AndroidTCPClient::run, androidListener);
}

void addVirtualButton(vector<PaperSheet*> papers, Point aPoint, infoServer info) {
    for (size_t j = 0; j < papers.size(); j++) {
        PaperSheet* aPaper = papers.at(j);
        if(aPaper->isInside(aPoint)) {
            if (didntMove(aPoint, old_point, 2)) {
                clock_gettime(CLOCK_REALTIME, &timer);
                if(timer.tv_sec - old_timer.tv_sec >= 2) {
                    aPaper->addVirtualButton(new VirtualButton(info.id, aPoint));
                    androidListener->stopEditMode();
                }
            } else {
                old_point = aPoint;
                clock_gettime(CLOCK_REALTIME, &old_timer);
            }
        }
    }
}

void clickOnVirtualButton(vector<PaperSheet*> papers, Point aPoint) {
    for (size_t j = 0; j < papers.size(); j++) {
        PaperSheet* aPaper = papers.at(j);
        if(aPaper->isInside(aPoint)) {
            std::vector<VirtualButton*> buttonList = aPaper->getVirtualBouttons();
            for (size_t k = 0; k < buttonList.size(); k ++) {
                VirtualButton* aButton = buttonList.at(k);
                if(aButton->isInside(aPoint)){
                    cout << "inside " << endl;
                    if (didntMove(aPoint, old_point, 10)) {
                        clock_gettime(CLOCK_REALTIME, &timer);
                        if(timer.tv_sec - old_timer.tv_sec >= 1) {
                            androidListener->sendMessage(aButton->getId(), ACTIVATION_BOUTON);
                        }
                    } else {
                        clock_gettime(CLOCK_REALTIME, &old_timer);
                        old_point = aPoint;
                    }
                }
            }
        }
    }
}

int main() {
    initThreads();
    clock_gettime(CLOCK_REALTIME, &timer);
    clock_gettime(CLOCK_REALTIME, &old_timer);
    while (true) {
        infoServer info = androidListener->getInfo();
        cv::Point aPoint;
        vector<PaperSheet*> papers = paperSheetDetector->getPapers();
        //vector<Hand> listOfHands = handTrackingListener->getHands();
        //for (size_t i = 0; i< listOfHands.size(); i++)
        //    cout << listOfHands.at(i) << endl;
        aPoint = Point(0,0);
        if (penDetector->hasPen()) {
            aPoint = penDetector->getPenPosition();
            if(info.editMode) {
                addVirtualButton(papers, aPoint, info);
            }else{
                clickOnVirtualButton(papers, aPoint);
            }
        }
        renderWindow->updateData(papers, aPoint);
    }
    pthread_join(paperSheetDetectorThread, NULL);
    return 0;
}
