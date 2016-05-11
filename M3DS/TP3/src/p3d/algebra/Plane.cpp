#include "Plane.h"
#include "Matrix4.h"
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



Plane::~Plane() {
}


Plane &Plane::operator=(const Plane &p) {
  _a=p.point();
  _n=p.normal();
  return *this;
}

Plane::Plane() {
}

Plane::Plane(const Vector3 &a,const Vector3 &n) {
    this->set(a,n);
}

void Plane::set(const Vector3 &p1,const Vector3 &p2,const Vector3 &p3) {
    Vector3 u1(p1,p2);
    Vector3 u2(p2,p3);
    _n.setCross(u1,u2);
    _a=p1;
}

void Plane::set(const Vector3 &a,const Vector3 &n) {
    _n=n;
    _a=a;
}

double Plane::interK(const Line &d) const {
    return (_n.dot(_a-d.a()))/_n.dot(d.u());
}

double Plane::distance(const Vector3 &p) const {
  return (p-_a).dot(_n)/_n.dot(_n);
}

ESide Plane::side(const Vector3 &p) const {
  Vector3 ap(_a,p);
  if (ap.dot(_n)>=0) return SIDE_PLUS;
  else return SIDE_MINUS;
}

Vector3 Plane::project(const Vector3 &p) {
    Line d(p,_n);
    double k=this->interK(d);
    return k*_n+p;
}

Vector3 Plane::project(const Vector3 &p,const Vector3 &u) {
    Line d(p,u);
    double k=this->interK(d);
//    UtilGL::add(k*u+p);
    return k*u+p;
}

Vector3 Plane::intersect(const Line &d) const {
  double k=this->interK(d);
  return k*d.u()+d.a();
}

const Vector3 &Plane::normal() const {
  return _n;
}

const Vector3 &Plane::point() const {
  return _a;
}

void Plane::point(const Vector3 &p) {
  _a=p;
}

void Plane::normal(const Vector3 &norm) {
  _n=norm;
}

ostream& p3d::operator<<(ostream &s,const Plane &p) {
    s << "[POINT=" << p.point() << ",N=" << p.normal() << "]";
    return s;
}

Matrix4 Plane::projectionDirection(const Vector3 &u) {
  Matrix4 res;
    Vector3 n=normal();
    double un=u.dot(n);
    double an=point().dot(n);
    res.row(0,un-n.x()*u.x(),-n.y()*u.x(),-n.z()*u.x(),an*u.x());
    res.row(1,-n.x()*u.y(),un-n.y()*u.y(),-n.z()*u.y(),an*u.y());
    res.row(2,-n.x()*u.z(),-n.y()*u.z(),un-n.z()*u.z(),an*u.z());
    res.row(3,0,0,0,un);
    return res;
}

Matrix4 Plane::projectionPoint(const Vector3 &L) {
  Matrix4 res;
    Vector3 n=normal();
    Vector3 A=point();
    double LAn=(A-L).dot(n);
    double An=A.dot(n);
    double Ln=L.dot(n);
    res.row(0,LAn+n.x()*L.x(),n.y()*L.x(),n.z()*L.x(),-An*L.x());
    res.row(1,n.x()*L.y(),LAn+n.y()*L.y(),n.z()*L.y(),-An*L.y());
    res.row(2,n.x()*L.z(),n.y()*L.z(),LAn+n.z()*L.z(),-An*L.z());
    res.row(3,n.x(),n.y(),n.z(),-Ln);
    return res;
}




