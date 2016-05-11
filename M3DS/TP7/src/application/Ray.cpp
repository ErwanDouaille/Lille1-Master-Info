

#include "Ray.h"

/**
@file
@author Fabrice Aubert
*/


using namespace p3d;
using namespace std;

Ray::Ray() : Line() {
}

Ray::Ray(const Vector3 &p1,const Vector3 &p2) : Line(p1,p2) {
}


void Ray::transform(const Matrix4 &m) {
  point(m.transformPoint(point()));
  direction(m.transformDirection(direction()));
}



Ray::~Ray() {
  //dtor
}

