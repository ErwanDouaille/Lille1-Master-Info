#include "pen.h"

using namespace std;
using namespace cv;

Pen::Pen(int id, vector<cv::Point> points) {
    this->_id = id;
    this->_points = points;
    this->_precision = 20;
}

Pen::~Pen(void) {
}

bool Pen::isCloseFrom(vector<cv::Point> points) {
    for (size_t i = 0; i < points.size(); i++) {
        if(!this->hasCornerPoint(points.at(i)))
            return false;
    }
    return true;
}

bool Pen::hasCornerPoint(Point aPoint) {
    for (size_t i = 0; i < _points.size(); i++) {
        if(pointsAreClose( _points.at(i), aPoint))
            return true;
    }
    return false;
}

bool Pen::pointsAreClose(Point aPoint, Point anotherPoint) {
    return aPoint.x-_precision < anotherPoint.x && aPoint.x+_precision > anotherPoint.x &&
            aPoint.y-_precision < anotherPoint.y && aPoint.y+_precision > anotherPoint.y;
}

ostream& operator <<(ostream& Stream, const Pen& Obj) {
    Stream << "ID: " << Obj.getId() << endl;
    return Stream;
}

