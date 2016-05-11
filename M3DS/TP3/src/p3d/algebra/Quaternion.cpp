#include <stdlib.h>
#include <cmath>

#include "Quaternion.h"
#include "Matrix4.h"
#include "Vector3.h"
#include "Tools.h"

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


const Quaternion &Quaternion::operator=(const Quaternion &q) {
  _x=q.x();_y=q.y();_z=q.z();_w=q.w();
  return *this;
}

Quaternion::Quaternion(double ww,double xx, double yy, double zz) {
  set(ww,xx,yy,zz);
  normalize();
}

void Quaternion::set(double ww,double xx,double yy,double zz) {
  _x=xx;
  _y=yy;
  _z=zz;
  _w=ww;
}

Quaternion::Quaternion(double a,const Vector3 &n) {
  set(a,n.x(),n.y(),n.z());
}

Quaternion::Quaternion(const Quaternion &q) {
    *this=q;
}



double Quaternion::length() const {
  return sqrt(_w*_w+_x*_x+_y*_y+_z*_z);
}

double Quaternion::lengthXYZ() const {
  return sqrt(_x*_x+_y*_y+_z*_z);
}

void Quaternion::normalize() {
  double n=length();
  _w/=n;
  _x/=n;
  _y/=n;
  _z/=n;
}


Quaternion p3d::conjugate(const Quaternion &q) {
  return Quaternion(q.w(),-q.x(),-q.y(),-q.z());
}

Quaternion p3d::normalize(const Quaternion &q) {
  Quaternion result;
  double l=q.length();
  result.w(q.w()/l);
  result.x(q.x()/l);
  result.y(q.y()/l);
  result.z(q.z()/l);
  return result;
}

void Quaternion::set(double scalar,const Vector3 &v) {
    _w=scalar;
    _x=v.x();_y=v.y();_z=v.z();
}


void Quaternion::toAngleAxis(double *angle,Vector3 *u) const {
  *angle=2.0*acos(_w);
  double s=sqrt(1-_w*_w);
  double ax,ay,az;
  if (s<0.0001) {
    ax=1.0;
    ay=0.0;
    az=0.0;
  } else {
    ax=_x/s;
    ay=_y/s;
    az=_z/s;
  }
  u->set(ax,ay,az);

  *angle=*angle*180.0/M_PI;
}


void Quaternion::setAngleAxis(double angle,const Vector3 &v){
  double a_radian=(angle-360.0*int(angle/360.0))*M_PI/180.0;
  Vector3 v_unit(v);
  v_unit.normalize();
  _w=cos(a_radian/2);
  double ss=sin(a_radian/2);
  _x=v_unit.x()*ss;
  _y=v_unit.y()*ss;
  _z=v_unit.z()*ss;
  this->normalize();
}

Quaternion Quaternion::fromAngleAxis(double angle,const Vector3 &v) {
  Quaternion q;
  q.setAngleAxis(angle,v);
  return q;
}

void Quaternion::setRotation(double angle, const Vector3 &u) {
  this->setAngleAxis(angle,u);
}

ostream &p3d::operator <<(ostream &s,const Quaternion &q) {
  s<<"(w= " << q.w() << " x= " << q.x() << " y= " << q.y() << " z= " << q.z() << ")";
  s<<"norme=" << q.length() << endl;
  return s;
}


Quaternion::Quaternion(const Matrix4 &src) {
  // from http://www.euclideanspace.com/
  double m[9];
  m[0]=src(0);m[1]=src(1);m[2]=src(2);
  m[3]=src(4);m[4]=src(5);m[5]=src(6);
  m[6]=src(8);m[7]=src(9);m[8]=src(10);
  double tr=m[0]+m[4]+m[8];
  double s;
  if (tr>0) {
    s=sqrt(1.0+tr)*2.0;
    _w=0.25*s;
    _x=(m[7]-m[5])/s;
    _y=(m[2]-m[6])/s;
    _z=(m[3]-m[1])/s;
  }
  else if ((m[0]>m[4]) && (m[0]>m[8])) {
    s=sqrt(1.0+m[0]-m[4]-m[8])*2.0;
    _w=(m[7]-m[5])/s;
    _x=0.25*s;
    _y=(m[1]+m[3])/s;
    _z=(m[2]+m[6])/s;
  }
  else if (m[4]>m[8]) {
    s=sqrt(1.0+m[4]-m[0]-m[8])*2.0;
    _w=(m[2]-m[6])/s;
    _x=(m[1]+m[3])/s;
    _y=0.25*s;
    _z=(m[5]+m[7])/s;
  }
  else {
    s=sqrt(1.0+m[8]-m[0]-m[4])*2.0;
    _w=(m[3]-m[1])/s;
    _x=(m[2]+m[6])/s;
    _y=(m[5]+m[7])/s;
    _z=0.25*s;
  }
  normalize();
}




// mult quaternion
void Quaternion::mul(const Quaternion &q1,const Quaternion &q2)
{
  if ((&q1==this) || (&q2==this)) throw ErrorD("MULT ref=this");
//Q1 * Q2 =( w1.w2 - v1.v2, w1.v2 + w2.v1 + v1*v2)
	_w=  q1.w()*q2.w() - q1.x()*q2.x() - q1.y()*q2.y() - q1.z()*q2.z();
	_x = q1.w()*q2.x() + q1.x()*q2.w() + q1.y()*q2.z() - q1.z()*q2.y();
	_y = q1.w()*q2.y() + q1.y()*q2.w() + q1.z()*q2.x() - q1.x()*q2.z();
	_z = q1.w()*q2.z() + q1.z()*q2.w() + q1.x()*q2.y() - q1.y()*q2.x();
}


void Quaternion::mul(const Quaternion &q) {
  Quaternion q2(*this);
  this->mul(q2,q);
}

void Quaternion::mulLeft(const Quaternion &q) {
  Quaternion q2(*this);
  this->mul(q,q2);
}

Quaternion p3d::operator*(const Quaternion &q1,const Quaternion &q2) {
    Quaternion res;
    res.mul(q1,q2);
    return res;
}

Quaternion p3d::operator+(const Quaternion &q1,const Quaternion &q2) {
    Quaternion res;
    res.add(q1,q2);
    return res;
}


// conjugate
void Quaternion::conjugate() {
  _x=-_x;
  _y=-_y;
  _z=-_z;
}



// square norme
double Quaternion::length2() {
  return _w*_w+_x*_x+_y*_y+_z*_z;
}


/// multiply and add (no normalized)
void Quaternion::mad(double alpha,const Quaternion &q) {
  this->mul(alpha);
  this->add(q);
}

// linear interpolation of unit quaternions in [0,1]
void Quaternion::mix(double lambda,const Quaternion &q1, const Quaternion &q2) {
  Quaternion temp(q2);
  *this=q1;
  temp.mul(lambda);
  this->mad(1.0-lambda,temp);
  this->normalize();
}


// scale
void Quaternion::scale(double s) {
	_w*=s;
	_x*=s;
	_y*=s;
	_z*=s;
}

void Quaternion::mul(double s) {
	_w*=s;
	_x*=s;
	_y*=s;
	_z*=s;
}

void Quaternion::add(const Quaternion &q1,const Quaternion &q2) {
	_w=q1.w()+q2.w();
	_x=q1.x()+q2.x();
	_y=q1.y()+q2.y();
	_z=q1.z()+q2.z();
}

void Quaternion::add(const Quaternion &q) {
	_w+=q.w();
	_x+=q.x();
	_y+=q.y();
	_z+=q.z();
}


Quaternion p3d::operator*(double k,const Quaternion &q) {
    Quaternion res(q);
    res.scale(k);
    return res;
}

Quaternion p3d::operator*(const Quaternion &q,double k) {
    Quaternion res(q);
    res.scale(k);
    return res;
}

Vector3 p3d::operator*(const Quaternion &q,const Vector3 &u) {
  Quaternion uq(0,u);
  Quaternion resq=q*uq*conjugate(q);
  return Vector3(resq.x(),resq.y(),resq.z());
}

void Quaternion::transform(Vector3 *u) {
  *u=(*this)*(*u);
}


void Quaternion::setRotation(const Vector3 &v1,const Vector3 &v2) {

  double angle=v1.angle(v2,v1.cross(v2));
  Vector3 axe=cross(v1,v2);
  if (axe.length()<0.0001) {
	  this->setIdentity();
  }
  else {
	  axe.normalize();
	  this->set(cos(angle/2),sin(angle/2)*axe);
  }
}

void Quaternion::setIdentity() {
  this->set(1,0,0,0);
}

void Quaternion::set(const Vector3 &i,const Vector3 &j,const Vector3 &k) {
  // from http://www.euclideanspace.com/
  double t=i.x()+j.y()+k.z();
  double s,x,y,z,w;
  if (t>0) {
    s=sqrt(t+1.0)*2.0;

    x=(j.z()-k.y())/s;
    y=(k.x()-i.z())/s;
    z=(i.y()-j.x())/s;
    w=0.25*s;
  }
  else {
    if ((i.x()>j.y()) && (i.x()>k.z())) {
      s=sqrt(1.0+i.x()-j.y()-k.z())*2.0;
      w=(j.z()-k.y())/s;
      x=0.25*s;
      y=(j.x()+i.y())/s;
      z=(k.x()+i.z())/s;
    } else
    if (j.y()>k.z()) {
      s=sqrt(1.0-i.x()+j.y()-k.z())*2.0;
      w=(k.x()-i.z())/s;
      x=(j.x()+i.y())/s;
      y=0.25*s;
      z=(k.y()+j.z())/s;
    } else {
      s=sqrt(1.0-i.x()-j.y()+k.z())*2.0;
      w=(i.y()-j.x())/s;
      x=(k.x()+i.z())/s;
      y=(k.y()+j.z())/s;
      z=0.25*s;
    }


  }
  set(w,x,y,z);
  normalize();
}


void Quaternion::rotate(double angle,const Vector3 &u) {
  Quaternion q1;
  q1.setAngleAxis(angle,u);
  this->mul(q1);
}

void Quaternion::rotate(double angle,double ax,double ay,double az) {
  rotate(angle,Vector3(ax,ay,az));
}


Quaternion Quaternion::identity() {
  Quaternion q;
  q.setIdentity();
  return q;
}

Matrix4 Quaternion::toMatrix() {
  return Matrix4::fromQuaternion(*this);
}




