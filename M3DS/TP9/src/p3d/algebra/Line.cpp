#include <iostream>

#include "Line.h"
#include "Plane.h"

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

using namespace std;
using namespace p3d;


Line::~Line() {
}

Line &Line::operator=(const Line &l) {
  _a=l.a();
  _u=l.u();
  return *this;
}


Line::Line() {
  _a.set(0,0,0);
  _u.set(1,0,0);
}

Line::Line(const Line &l) {
  point(l.point());
  direction(l.direction());
}


Line::Line(const Vector3 &aa,const Vector3 &uu) {
  _u=uu;
  _a=aa;
}

void Line::set(const Vector3 &aa,const Vector3 &uu) {
  _u=uu;
  _a=aa;
}

Vector3 Line::point(double k) const {
  return a()+k*u();
}

void Line::segment(const Vector3 &a,const Vector3 &b) {
    _u=b-a;
    _a=a;
}

double Line::distanceSegment2(const Vector3 &p) {
        Vector3 ab=_u;
        ab.sub(_a);
        Vector3 pab(p);
        pab.sub(_a);
        if (pab.dot(ab)<0) {
            return p.distance2(_a);
        }
        pab=p-_u-_a;
        if (pab.dot(ab)>0) {
            return p.distance2(_u+_a);
        }


        return distance2(p);
    }




double Line::distance2(const Vector3 &m) const {
  Vector3 am(m);
  am.sub(_a);

  double k=am.dot(_u)/_u.dot(_u);

  Vector3 h(_u);
  h.scaleAdd(k,_a);

  am=Vector3(h,m);
  return am.length2();

}


void Line::print(string mesg) const {
  cout << mesg << "A=" << this->a() << ", U=" << this->u() << endl;
}

ostream& p3d::operator<<(ostream &s,const Line &l) {
  s << "[A=" << l.a() << ",U=" << l.u() << "]";
  return s;
}

bool Line::set(const Plane &p1,const Plane &p2) {
  _u.setCross(p1.normal(),p2.normal());
  if (_u.length2()<0.0001) {
    return false;
  }
  else {
    Vector3 v;
    _u.normalize();
    v.setCross(_u,p1.normal());
    _a=p2.intersect(Line(p1.point(),v));
    return true;
  }
}



