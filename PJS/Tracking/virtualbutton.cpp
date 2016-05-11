#include "virtualbutton.h"

VirtualButton::VirtualButton(int id, cv::Point position)
{
    _id = id;
    _position = position;
    width = 50;
    height = 50;
}


bool VirtualButton::isInside(Point point) {
    vector<Point> points;

    points.push_back(Point(_position.x - width/2, _position.y - height/2));
    points.push_back(Point(_position.x + width/2, _position.y - height/2));
    points.push_back(Point(_position.x - width/2, _position.y + height/2));
    points.push_back(Point(_position.x + width/2, _position.y + height/2));

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

VirtualButton::~VirtualButton()
{

}

