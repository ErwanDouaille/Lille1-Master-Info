#include "Object3D.h"
#include "Matrix4.h"
#include "Quaternion.h"
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


std::stack<Object3D> Object3D::_stack;

Object3D::Object3D() {
  _orientation.setIdentity();
  _position.set(0,0,0);
  _scale.set(1,1,1);
}

Object3D::~Object3D() {
}

Object3D::Object3D(const Object3D &f) : _position(f.position()), _orientation(f.orientation()), _scale(f.scale()) {
}

void Object3D::push() {
  _stack.push(*this);
}

void Object3D::pop() {
  if (_stack.empty()) throw ErrorD("Object3D::pop : stack is empty (more pop than push ?)");
  *this=_stack.top();
  _stack.pop();
}


Matrix4 Object3D::worldLocal() const {
  Matrix4 result;
  result.setTranslation(_position);
  result.rotate(_orientation);
  result.scale(_scale);
  return result;
}

Matrix4 Object3D::localWorld() const {
  Matrix4 result;
  result=worldLocal();
  result.invert();
  return result;
}


void Object3D::compose(const Object3D &f) {
  _position=_position+_orientation*f.position();
  _orientation=_orientation*f.orientation();
  _scale=_scale*f.scale(); // !!!! no scale for this ! (difficult to find the orientation after scale)
}


Object3D &Object3D::operator=(const Object3D &f) {
  _orientation=f.orientation();
  _scale=f.scale();
  _position=f.position();
  return *this;
}


void Object3D::orientation(const Vector3 &i,const Vector3 &j,const Vector3 &k) {
  _orientation.set(i,j,k);
}


void Object3D::translate(const Vector3 &tr,ECoordinate f){
  if (f==Coordinate_Local) {
    _position=_position+_orientation*tr;
  }
  else {
    _position=_position+tr;
  }
}

void Object3D::rotate(double angle,const Vector3 &axe,ECoordinate f) {
  if (f==Coordinate_Local) {
    _orientation.rotate(angle,axe);
  }
  else {
    Quaternion q;
    q.setRotation(angle,axe);
    _orientation.mulLeft(q);
  }
}

void Object3D::rotate(double angle,double x,double y,double z,ECoordinate f) {
  rotate(angle,Vector3(x,y,z),f);
}

void Object3D::rotate(double angle,const Vector3 &axe,const Vector3 &center,ECoordinate f) {
  translate(center,f);
  rotate(angle,axe,f);
  translate(-center,f);
}

void Object3D::rotateXY(double ax,double ay,const Vector3 &center,const Vector3 &verticalAxis,ECoordinate f) {
  Vector3 trans=this->pointTo(f,center);
  translate(trans,Coordinate_Local);
  rotate(ax,Vector3(1,0,0),Coordinate_Local);
  Vector3 vertical=this->directionTo(f,verticalAxis);
  rotate(ay,vertical,Coordinate_Local);
  translate(-trans,Coordinate_Local);
}




void Object3D::rotate(const Quaternion &q,ECoordinate f) {
  if (f==Coordinate_Local) {
    _orientation.mul(q);
  }
  else {
    _orientation.mulLeft(q);
  }
}



void Object3D::lookAt(const Vector3 &at,const Vector3 &up) {
  Vector3 i,j,k;

  k=_position-at;
  i.setCross(up,k);

  j.setCross(k,i);

  i.normalize();
  j.normalize();
  k.normalize();

  Quaternion q;
  q.set(i,j,k);
  orientation(q);
}


void Object3D::setIdentity() {
  _position.set(0,0,0);
  _orientation.set(1,0,0,0);
}

void Object3D::orientation(double *a,Vector3 *u) {
  _orientation.toAngleAxis(a,u);
}

void Object3D::orientation(double a,const Vector3 &u) {
  _orientation.setAngleAxis(a,u);
}


// linear interpolation this=(1-t)f1+tf2 (i.e. linear interpolation of the translation and the quaternion)
void Object3D::mix(const Object3D &f1,const Object3D &f2,double t) {
  _position.mix(t,f1.position(),f2.position());
  _orientation.mix(t,f1.orientation(),f2.orientation());
  _scale.mix(t,f1.scale(),f2.scale());
}

Vector3 Object3D::pointTo(ECoordinate f,const Vector3 &p) const {
  Vector3 res(p);
  if (f==Coordinate_Local) {
	    res.sub(_position);
	    res=conjugate(_orientation)*res;
	    res.mul(Vector3(1.0/_scale.x(),1.0/_scale.y(),1.0/_scale.z()));
 }
  else {
    res.mul(_scale);
     res=_orientation*res;
    res+=_position;
  }
  return res;
}

Vector3 Object3D::directionTo(ECoordinate f,const Vector3 &p) const {
  Vector3 res(p);
  if (f==Coordinate_Local) {
	    res=conjugate(_orientation)*res;
	    res.mul(Vector3(1.0/_scale.x(),1.0/_scale.y(),1.0/_scale.z()));
  }
  else {
	    res.mul(_scale);
	    res=_orientation*res;
  }
  return res;
}


void Object3D::pointTo(ECoordinate f,Vector3 *p) const {
  if (f==Coordinate_Local) {
	    p->sub(_position);
	    conjugate(_orientation).transform(p);
	    p->mul(Vector3(1.0/_scale.x(),1.0/_scale.y(),1.0/_scale.z()));
  }
  else {
    p->mul(_scale);
    _orientation.transform(p);
    p->add(_position);
  }
}

void Object3D::directionTo(ECoordinate f,Vector3 *u) const {
  if (f==Coordinate_Local) {
	    conjugate(_orientation).transform(u);
	    u->mul(Vector3(1.0/_scale.x(),1.0/_scale.y(),1.0/_scale.z()));
  }
  else {
    u->mul(_scale);
    _orientation.transform(u);
  }
}

void Object3D::lineTo(ECoordinate f,Line *l) {
  l->point(pointTo(f,l->point()));
  l->direction(directionTo(f,l->direction()));
}


Line Object3D::lineTo(ECoordinate f,const Line &l) {
  Line res=l;
  res.point(pointTo(f,l.point()));
  res.direction(directionTo(f,l.direction()));
  return res;
}


Vector3 Object3D::direction() const {
  return _orientation*Vector3(0,0,1);
}

/// caution : might be what you dont want (use lookAt ?)
void Object3D::direction(const Vector3 &u) {
  _orientation.setRotation(Vector3(0,0,1),u);
}



