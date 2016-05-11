#ifndef QUATERNION_H_INCLUDED
#define QUATERNION_H_INCLUDED

#include <iostream>

/**
@file
@author F. Aubert
@brief Quaternion class for rotation
*/


namespace p3d {
  class Matrix4;
  class Vector3;

/**
@class Quaternion
@brief Quaternion class for rotation
*/
class Quaternion {
  double _w,_x,_y,_z;

public:
  /// destructor
  virtual ~Quaternion() {}
  /// copy operator
  const Quaternion &operator=(const Quaternion &q);
  /// copy constructor
  Quaternion(const Quaternion &q);
  /// constructor (default = unit quaternion (1,(0,0,0)))
  Quaternion(double w=1.0,double x=0.0,double y=0.0,double z=0.0);
  /// constructor : (w,(x,y,z))=(a,n)
  Quaternion(double a,const Vector3 &n);
  /// constructor from a Matrix4. !Caution : the 3x3 top-left matrix is assumed to be a pure rotation
  explicit Quaternion(const Matrix4 &m);

  /// returns w element
  inline double w() const {return _w;}
  /// sets this w to w
  inline void w(double w) {_w=w;}
  /// returns x element
  inline double x() const {return _x;}
  /// sets this x to x
  inline void x(double x) {_x=x;}
  /// returns y element
  inline double y() const {return _y;}
  /// sets this y to y
  inline void y(double y) {_y=y;}
  /// returns z element
  inline double z() const {return _z;}
  /// sets this z to z
  inline void z(double z) {_z=z;}

  /// set this to identity = (1,(0,0,0))
  void setIdentity();

  /// set this to (w,(x,y,z))
  void set(double w=1.0,double x=0.0,double y=0.0,double z=0.0);
  /// set this to (w,u)
  void set(double w,const Vector3 &u);
  /// set this according to the rotation between v1 and v2
  void setRotation(const Vector3 &v1,const Vector3 &v2);
  /// set this according to the rotation through angle about axis v (angle in degrees)
  void setAngleAxis(double angle,const Vector3 &v);
  /// "returns" the angle and the rotation axis in *angle and *u (TODO : no AngleAxis class)
  void toAngleAxis(double *angle,Vector3 *u) const;
  /// returns the length
  double length() const;
  /// returns the square of the length
  double length2();
  /// returns the length of the (x,y,z) element
  double lengthXYZ() const;
  /// normalize this
  void normalize();
  /// set this quaternion according to the matrix src : src is assumed to be a rotation matrix
  void set(const Matrix4 &src);
  /// set this quaternion according to the frame (i,j,k) (i.e. this represents the frame transformation from the reference frame)
  void set(const Vector3 &i, const Vector3 &j, const Vector3 &);

  /// right-multiplies this by q
  void mul(const Quaternion &q);

  /// left-mulitplies this by q
  void mulLeft(const Quaternion &q);

  /// set this to q1*q2
  void mul(const Quaternion &q1,const Quaternion &q2);
  /// conjugate this
  void conjugate();
  /// linear interpolation this=(1-t)q1+tq2  (then post normalized)
  void mix(double t,const Quaternion &q1, const Quaternion &q2);
  /// set this to q1+q2
  void add(const Quaternion &q1,const Quaternion &q2);
  /// set this to q1-q2
  void sub(const Quaternion &q1,const Quaternion &q2);
  /// scale the quaternion : this=s*this
  void mul(double s);
  /// scale the quaternion : this=s*this
  void scale(double s);
  /// multiply and add (no post normalized)
  void mad(double alpha,const Quaternion &q);
  /// add : this=this+q (no post normalized)
  void add(const Quaternion &q);
  /// sub : this=this-q (no post normalized)
  void sub(const Quaternion &q);
  /// set the quaternion according to the rotation angle/axis (same as fromAngleAxis)
  void setRotation(double angle,const Vector3 &u);

  /// this is composed with the rotation (angle in degrees)
  void rotate(double angle,const Vector3 &u);
  Matrix4 toMatrix();

  /// apply the quaternion (i.e. the rotation) to vector u
  void transform(Vector3 *u) const;

  /// returns the identity quaternion
  static Quaternion identity();
  /// returns the quaternion corresponding to the rotation through angle (in degrees) about v axis
  static Quaternion fromAngleAxis(double angle, const Vector3 &v);

  Quaternion &operator+=(const Quaternion &q2);
  Quaternion &operator-=(const Quaternion &q2);


  void rotate(double angle, double ax, double ay, double az);
//  static Vector3 interpolateDirection(const Vector3 &u, const Vector3 &v, double t);
};
/// returns the conjugate of q
Quaternion conjugate(const Quaternion &q);
/// returns the quaternion that is normalized q
Quaternion normalize(const Quaternion &q);
/// returns the transformation of u by the rotation q (i.e. conjugate(q)*u*q).
Vector3 operator*(const Quaternion &q,const Vector3 &u);
/// returns the quaternion q=q1*q2
Quaternion operator*(const Quaternion &q1,const Quaternion &q2);
/// returns the quaternion q=q1+q2
Quaternion operator+(const Quaternion &q1,const Quaternion &q2);
/// returns the quaternion q=q1-q2
Quaternion operator-(const Quaternion &q1,const Quaternion &q2);
/// returns the quaternion q=k*q
Quaternion operator*(double k,const Quaternion &q);
/// returns the quaternion q=q*k
Quaternion operator*(const Quaternion &q,double k);
/// out stream
std::ostream& operator<<(std::ostream &s,const Quaternion &q);


}

#include "Vector3.h"

#endif // QUATERNION_H_INCLUDED

