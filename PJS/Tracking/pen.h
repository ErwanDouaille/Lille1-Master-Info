#ifndef PEN_H
#define PEN_H

#include <iostream>
#include <opencv2/core/core.hpp>

#include "virtualbutton.h"

class Pen
{
protected:
    std::vector<cv::Point> _points;
    int _id, _precision;
    std::vector<VirtualButton> _virtualButtons;


public:
    Pen(int id, std::vector<cv::Point> points);
    ~Pen(void);

    bool isCloseFrom(std::vector<cv::Point> points);
    bool hasCornerPoint(cv::Point aPoint);
    bool pointsAreClose(cv::Point aPoint, cv::Point anotherPoint);
    void addVirtualButton(VirtualButton virtualButton) {_virtualButtons.push_back(virtualButton);}

    //SETTER
    void setPoints(std::vector<cv::Point> points){ _points = points;}
    void setPrecision(int precision) { _precision = precision;}
    void setVirtualButtons(std::vector<VirtualButton> virtualbuttons) { _virtualButtons = virtualbuttons;}

    //GETTER
    const std::vector<cv::Point> getPoints() const { return _points;}
    const int getPrecision() const {return _precision;}
    const int getId() const {return _id;}
    const std::vector<VirtualButton> getVirtualBouttons() { return _virtualButtons;}

    friend std::ostream& operator <<(std::ostream&, const Pen&);
};

#endif // PEN_H
