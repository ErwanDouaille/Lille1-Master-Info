#ifndef VECTOR3_H
#define VECTOR3_H


/**
@file
@author F. Aubert
@brief Vector3 operations (x,y,z)

*/

#include <iostream>

#include "Vector2.h"
#include "Vector4.h"

namespace p3d {
  class Vector4;
  class Vector2;

/**
@class Vector3
@brief Vector3 operations (x,y,z)
*/

#ifndef M_PI
#define M_PI 3.141592653
#endif

template<class T> void swapIfMin(T *a,T *b) {T c;if (*b<*a) {c=*a;*a=*b;*b=c;}}

double toDegree(double a);
double toRadian(double a);


class Vector3
{

  double _c[3];
  static float _fc[3]; // for casting

public:
  /// @brief destructor
  ~Vector3();
  /// copy
  Vector3 &operator=(const Vector3 &u);
  /// @brief default constructor
  Vector3();
  /// @brief copy constructor
  Vector3(const Vector3 &t);

    /// @brief constructs the vector (x,y,z)
  explicit Vector3(double x,double y,double z=0.0);
  Vector3(const p3d::Vector2 &p,double z) :Vector3(p.x(),p.y(),z) {}

  /// @brief construct from 2 points (a,b) (the vector b-a)
  Vector3(const Vector3 &a,const Vector3 &b);
  /// returns the i-th element
  inline double &operator()(int i) {return _c[i];}
  /// returns the i-th element
  inline const double &operator()(int i) const {return _c[i];}
  /// @brief get X
  inline double x() const {return _c[0];}
  inline double r() const {return _c[0];}
  /// @brief get Y
  inline double y() const {return _c[1];}
  inline double g() const {return _c[1];}
  /// @brief get Z
  inline double z() const {return _c[2];}
  inline double b() const {return _c[2];}

  /// @brief returns Vector2
  inline Vector2 xy() const {return Vector2(_c[0],_c[1]);}

  /// @brief if this is (close to) 0
  bool isZero(double epsilon=0.00001) {return length2()<epsilon;}

    /// @brief set this with b-a (a and b interpreted as points).
    void setVector(const Vector3 &a,const Vector3 &b);

    /// @brief set X
    inline void x(double k) {_c[0]=k;}
    inline void r(double k) {_c[0]=k;}
    /// @brief set Y
    inline void y(double k) {_c[1]=k;}
    inline void g(double k) {_c[1]=k;}
    /// @brief set Z
    inline void z(double k) {_c[2]=k;}
    inline void b(double k) {_c[2]=k;}

    /// @brief get the float * casting of the vector (copy of (x,y,z)). Ex : glVertex3fv(p.fv())
    float *fv() const;
    /// @brief get a double * pointing to (x,y,z) (no copy). Ex : glVertex3dv(p.dv())
    const double *dv() const {return _c;}

    /// @brief copy this to a
    void copyTo(Vector3 *a);

    /// @brief copy a to this.
    void copyFrom(const Vector3 &a);

    /// @brief Ex : p1+=p2
    Vector3 &operator+=(const Vector3 &a);
    /// @brief Ex : p1-=p2
    Vector3 &operator-=(const Vector3 &a);
    /// @brief Ex : p1=-p2
    Vector3 operator-();
    /// @brief Ex : p1*=3.0;
    Vector3 &operator*=(double k);
    /// @brief Ex : p1/=3.0;
    Vector3 &operator/=(double k);


    /// @brief set x,y,z
    void set(double x,double y,double z);
    /// @brief add (x,y,z) to this
    void add(double x,double y,double z);
    /// @brief add a to this. Ex : p.add(a)
    void add(const Vector3 &a);
    /// @brief substract a from this. Ex : p.sub(a)
    void sub(const Vector3 &a);
    /// @brief change sign of this.
    void oppose();


    /// @brief set this with a+b
    void add(const Vector3 &a,const Vector3 &b);
    /// @brief set this with a-b
    void sub(const Vector3 &a,const Vector3 &b);

    /// @brief get the square length
    double length2() const;
    /// @brief get the length
    double length() const;
    /// returns the distance between this and a
    double distance(const Vector3 &a) const;
    /// returns the squared distance between this and a
    double distance2(const Vector3 &a) const;


    /// @brief this is set with the normalized vector.
    const Vector3 &normalize(bool *testDiffZero=NULL);
    /// @brief this is set with k*this
    void scale(double k);
    /// @brief this is set with (this.x*kx,this.y*ky,this.z*kz)
    void scale(double kx,double ky,double kz);
    /// @brief this is set with a+k*this (scale *then* add).
    void scaleAdd(double k,const Vector3 &a);
    /// @brief multiply with k : this=k*this (same as scale).
    void mul(double k);
    /// @brief multiply with u : this.x=this.x*u.x, etc
    void mul(const Vector3 &u);

    /// @brief the dot product
    double dot(const Vector3 &a) const;

    /// @brief set this with the cross product a x b
    void setCross(const Vector3 &a,const Vector3 &b);
    /// @brief set this with the cross product this x b
    void setCross(const Vector3 &a);
    Vector3 cross(const Vector3 &a) const;

    /// @brief multiply then add : this=k*this+t
    void mad(double k,const Vector3 &t);

    /// @brief linear interpolation : this=(1-t)*t1+t*t2
    void mix(double t,const Vector3 &t1,const Vector3 &t2);


    /// @brief set the vector with the mid-point \f$ \frac{n1+n2}{2}\f$
    void mid(const Vector3& n1,const Vector3 &n2);
    /// @brief this is set with (this+n1)/2
    void mid(const Vector3& n1);

    /// @brief the result is "this" turned of angle (radian) around Y
    Vector3 rotationY(float angle) const;

    /// @brief returns the angle between this and u (along vertical; between -PI and PI)
    double angle(const Vector3 &u, const Vector3 &vertical=Vector3(0,0,1)) const;


    /// @brief print the vector
    void print(std::string mesg="") const;


    /// @brief returns a normal vector (cross product with x, y or z)
    Vector3 anyNormal();

    /// @brief rotate this about axe with angle (radian)
    void rotate(double angle,const Vector3 &axe);

    /// @brief return true if u is (almost) parallel with u
    bool isParallel(const Vector3 &u);

    /// @brief set "this" with (p.x/p.w,p.y/p.w,p.z/p.w) (homogeneous co-ordinate).
    void point(const Vector4 &p);
    void direction(const Vector4 &p);

    /// @brief set x with the min of this.x and a.x (same with y and z).
    void setMinCoordinate(const Vector3 &a);

    /// @brief set x with the max of this.x and a.x (same with y and z).
    void setMaxCoordinate(const Vector3 &a);

    /// @brief set this with b-a (i.e. vector from 2 points)
    void set(const Vector3 &a,const Vector3 &b);

    /// @brief returns the min of the coordinates
    double min(unsigned int *which=NULL);

    /// @brief returns the max of the coordinates
    double max(unsigned int *which=NULL) const;

    void interpolateDirection(const Vector3 &u, const Vector3 &v, double t);
    void clamp(double min,double max);
};
/// @brief cout the vector
std::ostream& operator <<(std::ostream &s,const Vector3 &t);

/// @brief return the vector t which is normalized (function)
Vector3 normalize(const Vector3 &t);
/// @brief Ex : a=b*c (=> a.x = b.x*c.x, a.y=b.y*c.y, ...)
Vector3 operator*(const Vector3 &a,const Vector3 &b);
/// @brief Ex : p=p1+p2
Vector3 operator+(const Vector3 &a,const Vector3 &b);
/// @brief Ex : p=p1-p2
Vector3 operator-(const Vector3 &a,const Vector3 &b);
/// @brief Ex : p1=3.0*p2
Vector3 operator*(double k,const Vector3 &b);
/// @brief Ex : p1=p2*3.0
Vector3 operator*(const Vector3 &b,double k);
/// @brief Ex : p1=p2/3.0
Vector3 operator/(const Vector3 &b,double k);
/// @brief Ex : p1=-p2
Vector3 operator-(const Vector3 &a);
/// @brief Ex : p1==p2 !caution : dont use it ! (precision problem => may not do what you expect).
bool operator==(const Vector3 &a,const Vector3 &b);
/// @brief returns the cross product v1 x v2
Vector3 cross(const Vector3 &v1,const Vector3 &v2);
/// @brief returns the dot product a.b
double dot(const Vector3 &a,const Vector3 &b);
}


#endif // VECTOR3_H





