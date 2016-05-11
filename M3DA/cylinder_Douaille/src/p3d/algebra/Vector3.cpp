#include "Vector3.h"

#include "Tools.h"
#include "Vector2.h"
#include "Vector4.h"
#include "Quaternion.h"

#include <cmath>
#include <iostream>

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

double p3d::toDegree(double a) {return a*180.0/M_PI;}
double p3d::toRadian(double a) {return a*M_PI/180.0;}



float Vector3::_fc[3];

Vector3::Vector3() {
  this->set(0,0,0);
}


Vector3::Vector3(double x,double y,double z) {
    this->set(x,y,z);
}

Vector3::Vector3(const Vector3 &t) {
    *this=t;
}


Vector3::Vector3(const Vector3 &a,const Vector3 &b) {
    this->setVector(a,b);
}

void Vector3::setVector(const Vector3 &a,const Vector3 &b) {
    *this=b-a;
}


void swap(int &a,int &b) {
  int c;
  c=a;
  a=b;
  b=c;
}


void Vector3::set(double x,double y,double z) {
    _c[0]=x;_c[1]=y;_c[2]=z;
}

void Vector3::interpolateDirection(const Vector3 &u,const Vector3 &v,double t) {
  // TODO : heavy computation
  Vector3 axis=p3d::cross(u,v);
  Quaternion q;
  if (axis.length()<0.00001) q.setIdentity();
  else {
    double angle=u.angle(v,u.cross(v));
    angle*=t;
    q=Quaternion::fromAngleAxis(toDegree(angle),axis);
  }
  double ilength=(1.0-t)*u.length()+t*v.length();
  Vector3 uu=u;
  uu.normalize();
  *this=(q*uu)*ilength;
}


void Vector3::clamp(double min, double max) {
  for(unsigned int i=0;i<3;++i) {
    if (_c[i]<min) _c[i]=min; else if (_c[i]>max) _c[i]=max;
  }
}

float *Vector3::fv() const {
    _fc[0]=_c[0];_fc[1]=_c[1];_fc[2]=_c[2];
    return _fc;
}


double Vector3::length2() const {
    return _c[0]*_c[0]+_c[1]*_c[1]+_c[2]*_c[2];
}

double Vector3::length() const {
    return sqrt(this->length2());
}

double Vector3::distance2(const Vector3 &a) const {
  return Vector3(*this,a).length2();
}

double Vector3::distance(const Vector3 &a) const {
  return Vector3(*this,a).length();
}


const Vector3 &Vector3::normalize(bool *testDiffZero) {
  double d=this->length();
  if (testDiffZero!=NULL) *testDiffZero=true;
  if (fabs(d)<EPSILON_PREC) {
    if (testDiffZero!=NULL) {*testDiffZero=false;return *this;}
    else {
      // todo : coherent with no error ?
      return *this;
      throw Error("Norme nulle",__LINE__,__FILE__);
    }
  }
  _c[0]/=d;
  _c[1]/=d;
  _c[2]/=d;
  return *this;

}

void Vector3::add(double x,double y,double z) {
    _c[0]+=x;_c[1]+=y;_c[2]+=z;
}

void Vector3::add(const Vector3 &a) {
  _c[0]+=a.x();
  _c[1]+=a.y();
  _c[2]+=a.z();
}

void Vector3::add(const Vector3 &a,const Vector3 &b) {
  this->set(b.x()+a.x(),
            b.y()+a.y(),
            b.z()+a.z());
}

void Vector3::sub(const Vector3 &a) {
    this->set(this->x()-a.x(),
        this->y()-a.y(),
        this->z()-a.z());
}


void Vector3::sub(const Vector3 &a,const Vector3 &b) {
    this->set(a.x()+b.x(),
        a.y()+b.y(),
        a.z()+b.z());
}

void Vector3::oppose() {
  _c[0]=-_c[0];
  _c[1]=-_c[1];
  _c[2]=-_c[2];
}



void Vector3::scale(double k) {
    _c[0]*=k;
    _c[1]*=k;
    _c[2]*=k;
}

void Vector3::mul(double k) {
    _c[0]*=k;
    _c[1]*=k;
    _c[2]*=k;
}


void Vector3::scale(double kx,double ky,double kz) {
    _c[0]*=kx;
    _c[1]*=ky;
    _c[2]*=kz;
}



void Vector3::scaleAdd(double k,const Vector3 &a) {
    this->scale(k);
    this->add(a);
}



// rotation autour de Y (classiquement rotation de profil)
Vector3 Vector3::rotationY(float angle) const {
    Vector3 res;
    res.x(cos(angle)*x()-sin(angle)*z());
    res.y(y());
    res.z(sin(angle)*x()+cos(angle)*z());
    return res;
}


void Vector3::rotate(double angle,const Vector3 &axe) {
  Quaternion q=Quaternion::fromAngleAxis(angle,axe);
  q.transform(this);
}


void Vector3::mid(const Vector3& n1,const Vector3 &n2) {
    this->add(n1,n2);
    this->scale(0.5);
}


void Vector3::mid(const Vector3& n1) {
    this->add(n1);
    this->scale(0.5);
}


double Vector3::dot(const Vector3 &a) const {
    return x()*a.x()+y()*a.y()+z()*a.z();
}

double p3d::dot(const Vector3 &a,const Vector3 &b) {
  return a.x()*b.x()+a.y()*b.y()+a.z()*b.z();
}

void Vector3::setCross(const Vector3 &a,const Vector3 &b) {
    this->set(a.y()*b.z()-a.z()*b.y(),
        a.z()*b.x()-b.z()*a.x(),
        a.x()*b.y()-b.x()*a.y());
}

Vector3 Vector3::cross(const Vector3 &u) const {
  Vector3 res;
  res.setCross(*this,u);
  return res;
}

Vector3 p3d::cross(const Vector3 &v1, const Vector3 &v2) {
  Vector3 res;
  res.setCross(v1,v2);
  return res;
}


void Vector3::mul(const Vector3 &u) {
  _c[0]*=u.x();
  _c[1]*=u.y();
  _c[2]*=u.z();
}


void Vector3::print(string mesg) const {
    cout << mesg << "(" << this->x() << "," << this->y() << "," << this->z() << ")" << endl;
}

Vector3 p3d::operator+(const Vector3 &a,const Vector3 &b) {
    Vector3 p(a);
    p.add(b);
    return p;
}

Vector3 p3d::operator-(const Vector3 &a,const Vector3 &b) {
    Vector3 p(a);
    p.sub(b);
    return p;
}

Vector3 p3d::operator*(double k,const Vector3 &b) {
    Vector3 p(b);
    p.scale(k);
    return p;
}

Vector3 p3d::operator*(const Vector3 &b,double k) {
    return k*b;
}

Vector3 p3d::operator/(const Vector3 &b,double k) {
    return 1.0/k*b;
}



Vector3 p3d::operator*(const Vector3 &u,const Vector3 &v) {
  return Vector3(u.x()*v.x(),u.y()*v.y(),u.z()*v.z());
}


Vector3 p3d::operator-(const Vector3 &a) {
    Vector3 res(a);
    res.set(-res.x(),-res.y(),-res.z());
    return res;
}


ostream& p3d::operator<<(std::ostream &s,const Vector3 &q) {
    s << "(" << q.x() << "," << q.y() << "," << q.z() << ")";
    return s;
}


Vector3 &Vector3::operator=(const Vector3 &a) {
  _c[0]=a(0);
  _c[1]=a(1);
  _c[2]=a(2);
  return *this;
}

bool p3d::operator==(const Vector3 &a,const Vector3 &b) {
  return (a.x()==b.x() && a.y()==b.y() && a.z()==b.z());
}

Vector3 &Vector3::operator +=(const Vector3 &a) {
    this->add(a);
    return *this;
}

Vector3 &Vector3::operator -=(const Vector3 &a) {
    this->sub(a);
    return *this;
}

Vector3 Vector3::operator -() {
  Vector3 res(*this);
  res.oppose();
  return res;
}


Vector3 &Vector3::operator *=(double k) {
  this->mul(k);
  return *this;
}

Vector3 &Vector3::operator /=(double k) {
  this->mul(1.0/k);
  return *this;
}


void Vector3::mad(double k,const Vector3 &t) {
  this->mul(k);
  this->add(t);
}

void Vector3::mix(double t,const Vector3 &t1,const Vector3 &t2) {
  Vector3 temp(t2);
  *this=t1;
  temp.mul(t);
  this->mad(1.0-t,temp);
}

Vector3::~Vector3() {}


double Vector3::angle(const Vector3 &u,const Vector3 &vertical) const {
  Vector3 u1(*this);
  Vector3 v1(u);

  u1.normalize();
  v1.normalize();

  double angle=acos(u1.dot(v1));

  Vector3 n=u1.cross(v1);
  if (n.dot(vertical)<0) angle=-angle;

  return angle;
}

void Vector3::point(const Vector4 &u) {
  this->set(u.x()/u.w(),u.y()/u.w(),u.z()/u.w());
}

void Vector3::direction(const Vector4 &u) {
  this->set(u.x(),u.y(),u.z());
}

void Vector3::setMinCoordinate(const Vector3 &a) {
  if (a.x()<this->x()) this->x(a.x());
  if (a.y()<this->y()) this->y(a.y());
  if (a.z()<this->z()) this->z(a.z());
}

void Vector3::setMaxCoordinate(const Vector3 &a) {
  if (a.x()>this->x()) this->x(a.x());
  if (a.y()>this->y()) this->y(a.y());
  if (a.z()>this->z()) this->z(a.z());
}

double Vector3::min(unsigned int *which) {
	unsigned int w;
  if (*_c<*(_c+1)) {
    w=(*_c<*(_c+2))?0:2;
  }
  else {
    w=(*(_c+1)<*(_c+2))?1:2;
  }
  if (which!=NULL) *which=w;
  return *(_c+w);
}

double Vector3::max(unsigned int *which) const {
	unsigned int w;
  if (*_c>*(_c+1)) {
    w=(*_c>*(_c+2))?0:2;
  }
  else {
    w=(*(_c+1)>*(_c+2))?1:2;
  }
  if (which!=NULL) *which=w;
  return *(_c+w);
}

Vector3 p3d::normalize(const Vector3 &t) {
  Vector3 res=t;
  res.normalize();
  return res;
}





