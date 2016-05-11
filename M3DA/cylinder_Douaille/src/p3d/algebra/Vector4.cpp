#include "Vector4.h"
#include "Vector3.h"
#include <iostream>

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

float Vector4::_fc[4];

Vector4::~Vector4() {}

Vector4 &Vector4::operator=(const Vector4 &u) {
  _c[0]=u.x();_c[1]=u.y();_c[2]=u.z();_c[3]=u.w();
  return *this;
}

Vector4::Vector4() {
}

Vector4::Vector4(const Vector3 &p,double w) {
  this->set(p,w);
}

void Vector4::set(const Vector3 &p,double w) {
  _c[0]=p.x();_c[1]=p.y();_c[2]=p.z();_c[3]=w;
}

Vector4::Vector4(const Vector4 &p) {
  this->set(p);
}

void Vector4::set(const Vector4 &p) {
  _c[0]=p.x();_c[1]=p.y();_c[2]=p.z();_c[3]=p.w();
}

Vector4::Vector4(double x,double y,double z,double w) {
  this->set(x,y,z,w);
}

void Vector4::set(double x,double y,double z,double w) {
  _c[0]=x;_c[1]=y;_c[2]=z;_c[3]=w;
}


const double *Vector4::dv() const {
    return _c;
}

const float *Vector4::fv() const {
    _fc[0]=_c[0];_fc[1]=_c[1];_fc[2]=_c[2];_fc[3]=_c[3];
    return _fc;
}

void Vector4::print(string mesg) const {
    cout << mesg << "(" << _c[0] << "," << _c[1] << "," << _c[2] << "," << _c[3] << ")\n";
}


Vector3 Vector4::xyz() const  {return Vector3(x(),y(),z());}


Vector3 Vector4::project() const {
  return Vector3(this->x()/this->w(),this->y()/this->w(),this->z()/this->w());
}

void Vector4::fromPoint(const Vector3 &p,double w) {
  *this=Vector4(p*w,w);
}

ostream& p3d::operator<<(ostream &s,const Vector4 &p) {
    s << "(" << p.x() << "," << p.y() << "," << p.z() << "," << p.w() << ")";
    return s;
}

Vector4 &Vector4::operator *=(double k) {
  _c[0]*=k;
  _c[1]*=k;
  _c[2]*=k;
  _c[3]*=k;
  return *this;
}

Vector4 &Vector4::operator +=(const Vector4 &a) {
  _c[0]+=a.x();
  _c[1]+=a.y();
  _c[2]+=a.z();
  _c[3]+=a.w();
  return *this;
}

Vector4 &Vector4::operator -=(const Vector4 &a) {
  _c[0]-=a.x();
  _c[1]-=a.y();
  _c[2]-=a.z();
  _c[3]-=a.w();
  return *this;
}

void Vector4::oppose() {
  _c[0]=-_c[0];
  _c[1]=-_c[1];
  _c[2]=-_c[2];
  _c[3]=-_c[3];
}


Vector4 Vector4::operator -() {
  Vector4 res(*this);
  res.oppose();
  return res;
}

Vector4 p3d::operator*(double k,const Vector4 &u) {
  Vector4 res=u;
  res*=k;
  return res;
}

Vector4 p3d::operator*(const Vector4 &u,double k) {
  return k*u;
}

Vector4 p3d::operator+(const Vector4 &a,const Vector4 &b) {
  Vector4 res=a;
  res+=b;
  return res;
}

Vector4 p3d::operator-(const Vector4 &a,const Vector4 &b) {
  Vector4 res=a;
  res-=b;
  return res;
}



