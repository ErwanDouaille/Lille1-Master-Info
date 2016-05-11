#ifndef PAPERSHEET_H
#define PAPERSHEET_H

#include <iostream>
#include <opencv2/core/core.hpp>

#include "virtualbutton.h"

class PaperSheet
{
protected:
    std::vector<cv::Point> _points;
    int _id, _precision;
    std::vector<VirtualButton*> _virtualButtons;


public:
    PaperSheet(int id, std::vector<cv::Point> points);
    ~PaperSheet(void);

    bool isCloseFrom(std::vector<cv::Point> points);
    bool hasCornerPoint(cv::Point aPoint);
    bool pointsAreClose(cv::Point aPoint, cv::Point anotherPoint);
    void addVirtualButton(VirtualButton* virtualButton) {std::cout << "ajout " << virtualButton->getId() << std::endl;_virtualButtons.push_back(virtualButton);}
    bool isInside(cv::Point aPoint);

    //SETTER
    void setPoints(std::vector<cv::Point> points){ _points = points;}
    void setPrecision(int precision) { _precision = precision;}
    void setVirtualButtons(std::vector<VirtualButton*> virtualbuttons) { _virtualButtons = virtualbuttons;}

    //GETTER
    const std::vector<cv::Point> getPoints() const { return _points;}
    const int getPrecision() const {return _precision;}
    const int getId() const {return _id;}
    const std::vector<VirtualButton*> getVirtualBouttons() { return _virtualButtons;}

    friend std::ostream& operator <<(std::ostream&, const PaperSheet&);
};


#endif // PAPERSHEET_H
