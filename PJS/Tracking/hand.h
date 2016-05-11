#ifndef HAND_H
#define HAND_H

#include "Point3D.h"
#include <vector>
#include <iostream>

class Hand
{

private:
    Point3D handPosition;
    std::vector<Point3D> fingerPositions;

public:
    Hand(void);
    ~Hand(void);

    const Point3D getHandPosition() const {return handPosition;}
    void setHandPosition(Point3D pos) {handPosition = pos;}

    std::vector<Point3D> getFingerPositions() {return fingerPositions;}
    friend std::ostream& operator <<(std::ostream&, const Hand&);
};



#endif // HAND_H
