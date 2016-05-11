#ifndef VIRTUALBUTTON_H
#define VIRTUALBUTTON_H


#include <opencv2/core/core.hpp>

using namespace cv;
using namespace std;

class VirtualButton
{
protected:
    int _id;
    bool _visible;
    Point _position;
    int width, height;

public:
    VirtualButton(int id, Point position);
    ~VirtualButton();

    Point getPosition() const { return _position;}
    bool isVisible() const { return _visible;}
    int getId() const { return _id;}
    bool isInside(Point aPoint);
    //Rect getRectangle() const { }

};

#endif // VIRTUALBUTTON_H
