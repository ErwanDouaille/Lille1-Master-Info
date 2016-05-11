#include "Vector2.h"
#include <math.h>
#include "Tools.h"

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/



using namespace p3d;
using namespace std;


float Vector2::_fc[2];

Vector2 &Vector2::operator=(const Vector2 &u) {
  _c[0]=u(0);
  _c[1]=u(1);
  return *this;
}

Vector2::Vector2() {
}

Vector2::Vector2(double x,double y) {
  _c[0]=x;_c[1]=y;
}



Vector2::Vector2(const Vector2 &a,const Vector2 &b) {
  *this=b-a;
}


const double *Vector2::dv() const {return _c;}

const float *Vector2::fv() const {
    _fc[0]=_c[0];_fc[1]=_c[1];
    return _fc;
}

double Vector2::length2() {
    return _c[0]*_c[0]+_c[1]*_c[1];
}

double Vector2::length() {
    return sqrt(this->length2());
}

void Vector2::clamp(double min, double max) {
  if (x()<min) x(min);
  else if (x()>max) x(max);
  if (y()<min) y(min);
  else if (y()>max) y(max);
}

void Vector2::normalize() {
    double d=this->length();
    if (fabs(d)<EPSILON_PREC) {
        throw "Normale nulle";
    }
    _c[0]/=d;
    _c[1]/=d;

}


Vector2 &Vector2::operator+=(const Vector2 &u) {
  _c[0]+=u(0);
  _c[1]+=u(1);
  return *this;
}

Vector2 &Vector2::operator-=(const Vector2 &u) {
  _c[0]-=u(0);
  _c[1]-=u(1);
  return *this;
}

void Vector2::add(const Vector2 &a,const Vector2 &b) {
  *this=a;*this+=b;
}

void Vector2::sub(const Vector2 &a,const Vector2 &b) {
  *this=a;*this-=b;
}

double Vector2::dot(const Vector2 &a) {
    return x()*a.x()+y()*a.y();
}


void Vector2::scale(double k) {
    _c[0]*=k;
    _c[1]*=k;
}

void Vector2::scale(const Vector2 &k) {
  _c[0]*=k.x();
  _c[1]*=k.y();
}

void Vector2::invScale(double k) {
  _c[0]=k/_c[0];
  _c[1]=k/_c[1];
}



void Vector2::mid(const Vector2& n1,const Vector2 &n2) {
    this->add(n1,n2);
    this->scale(0.5);
}


void Vector2::mid(const Vector2& n1) {
    *this+=n1;
    this->scale(0.5);
}



void Vector2::print() {
    cout << "x=" << x() << ",y=" << y() << endl;
}


double Vector2::x() const {
  return _c[0];
}

double Vector2::y() const {
  return _c[1];
}

void Vector2::x(double x) {
  _c[0]=x;
}

void Vector2::y(double y) {
  _c[1]=y;
}


Vector2 p3d::operator+(const Vector2 &a,const Vector2 &b) {
    Vector2 p(a);
    p+=b;
    return p;
}

Vector2 p3d::operator-(const Vector2 &a,const Vector2 &b) {
    Vector2 p(a);
    return p-=b;
}

Vector2 p3d::operator*(double k,const Vector2 &b) {
    Vector2 p(b);
    p.scale(k);
    return p;
}

Vector2 p3d::operator*(const Vector2 &b,double k) {
    return k*b;
}


Vector2 p3d::operator/(const Vector2 &b,double k) {
  Vector2 res(b);
  res.scale(1.0/k);
  return res;
}

Vector2 p3d::operator*(const Vector2 &a,const Vector2 &b) {
  Vector2 res(a);
  res.scale(b);
  return res;
}

Vector2 p3d::operator/(const Vector2 &a,const Vector2 &b) {
  Vector2 res;
  res.x(a.x()/b.x());
  res.y(a.y()/b.y());
  return res;
}


ostream& p3d::operator <<(std::ostream &s,const Vector2 &q) {
    s << "(" << q.x() << "," << q.y() << ")";
    return s;
}


Vector2 Vector2::normalSegment(const Vector2 &p2) {
    Vector2 res(this->y()-p2.y(),p2.x()-this->x());
    res.normalize();
    return res;
}

double Vector2::distance2(const Vector2 &a) const {
  Vector2 l(*this,a);
  return l.length2();
}

double Vector2::distance(const Vector2 &a) const {
  Vector2 l(*this,a);
  return l.length();
}





