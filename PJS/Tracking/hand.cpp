#include "hand.h"

using namespace std;

Hand::Hand(void)
{
    handPosition = Point3D(0,0,0);
    fingerPositions = vector<Point3D>();
}


Hand::~Hand(void)
{
}

ostream& operator <<(ostream& Stream, const Hand& Obj) {
    Stream << "center: " << Obj.getHandPosition() << endl;
    return Stream;
}

