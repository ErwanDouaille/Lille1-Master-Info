#include "papersheet.h"

using namespace std;
using namespace cv;

PaperSheet::PaperSheet(int id, vector<cv::Point> points) {
    this->_id = id;
    this->_points = points;
    this->_precision = 20;
}

PaperSheet::~PaperSheet(void) {
}

bool PaperSheet::isCloseFrom(vector<cv::Point> points) {
    for (size_t i = 0; i < points.size(); i++) {
        if(!this->hasCornerPoint(points.at(i)))
            return false;
    }
    return true;
}

bool PaperSheet::isInside(Point point) {
    vector<Point> points = this->getPoints();
    int i, j, nvert = points.size();
    bool c = false;

    for(i = 0, j = nvert - 1; i < nvert; j = i++) {
        if( (( (points[i].y) >= point.y ) != (points[j].y >= point.y) ) &&
            (point.x <= (points[j].x - points[i].x) * (point.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)
                )
                c = !c;
    }
    return c;
}

bool PaperSheet::hasCornerPoint(Point aPoint) {
    for (size_t i = 0; i < _points.size(); i++) {
        if(pointsAreClose( _points.at(i), aPoint))
            return true;
    }
    return false;
}

bool PaperSheet::pointsAreClose(Point aPoint, Point anotherPoint) {
    return aPoint.x-_precision < anotherPoint.x && aPoint.x+_precision > anotherPoint.x &&
            aPoint.y-_precision < anotherPoint.y && aPoint.y+_precision > anotherPoint.y;
}

ostream& operator <<(ostream& Stream, const PaperSheet& Obj) {
    Stream << "ID: " << Obj.getId() << endl;
    return Stream;
}
