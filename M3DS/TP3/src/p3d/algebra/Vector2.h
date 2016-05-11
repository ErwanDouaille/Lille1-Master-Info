#ifndef VECTOR2_H_INCLUDED
#define VECTOR2_H_INCLUDED


/*!
*
* @file
*
* @brief Operations on (x,y) (no distinction between points and vectors)
* @author F. Aubert
*
*/

#include <iostream>

#define EPSILON_PREC 0.00001

namespace p3d {
/**
@class Vector2
@brief Operations on (x,y) (no distinction between points and vectors)
*/
class Vector2 {
    double _c[2];
    static float _fc[2];
public:
    /// destructor
    virtual ~Vector2() {}
    /// copy
    Vector2 &operator=(const Vector2 &u);
    /// default constructor
    Vector2();
    /// copy constructor
    Vector2(const Vector2 &v) {_c[0]=v.x();_c[1]=v.y();}
    /// constructor from (x,y)
    Vector2(double x,double y);
    /// constructor from a-b
    Vector2(const Vector2 &a,const Vector2 &b);
    /// returns x
    double x() const;
    /// returns y
    double y() const;
    /// returns the pointer to data (direct access to coordinates)
    const double *dv() const;
    /// returns the pointer to data with cast to float * !caution : the pointer is shared between all Vector2
    const float *fv() const;

    /// returns the ith coordinate of this
    inline double &operator()(int i) {return _c[i];}
    /// returns the ith coordinate of this
    inline const double &operator()(int i) const {return _c[i];}

    /// sets x
    void x(double x);
    /// sets y
    void y(double y);
    /// adds a to this. Ex : p1+=p2
    Vector2 &operator+=(const Vector2 &a);
    /// subs a to this. Ex : p1-=p2
    Vector2 &operator-=(const Vector2 &a);
    /// sets this to a+b
    void add(const Vector2 &a,const Vector2 &b);
    /// sets this to a-b
    void sub(const Vector2 &a,const Vector2 &b);
    /// multiplies this by the number k. Ex : p1*=a
    void operator*=(double k);
    /// returns the squared length of this
    double length2();
    /// returns the length of this
    double length();
    /// returns the squared distance from this to a.
    double distance2(const Vector2 &a) const;
    /// returns the distance from this to a
    double distance(const Vector2 &a) const;
    /// normalize this
    void normalize();
    /// sets this to k*this. Ex : p.scale(3.0)
    void scale(double k);
    /// sets this to (this.x*a.x, ...) (multiplies each coordinate of this by the corresponding coordinate of a)
    void scale(const Vector2 &a);
    /// sets this to k/this (i.e. this.x = k/this.x, ... for each coordinate)
    void invScale(double k);
    /// returns dot product between this and a. Ex : p1.dot(p2)
    double dot(const Vector2 &a);
    /// sets this to the mid-point \f$\frac{(n1+n2)}{2}\f$
    void mid(const Vector2& n1,const Vector2 &n2);
    /// sets this to the mid-point this=\f$\frac{this+n1}{2}\f$
    void mid(const Vector2& n1);
    /// print (x,y,z)
    void print();
    /// returns a normal vector to the edge [this,p2]
    Vector2 normalSegment(const Vector2 &p2);

    void clamp(double min,double max);
};

/// Ex : p=a+b
Vector2 operator+(const Vector2 &a,const Vector2 &b);
/// Ex : p=a-b
Vector2 operator-(const Vector2 &a,const Vector2 &b);
/// @brief compatibilité avec cout (affiche les coordonnées).
std::ostream& operator <<(std::ostream &s,const Vector2 &t);
/// Ex : p=3.0*a
Vector2 operator*(double k,const Vector2 &b);
/// Ex : p=a*3.0
Vector2 operator*(const Vector2 &b,double k);
/// Ex : p=a/3.0
Vector2 operator /(const Vector2 &b,double k);
/// Ex : p=p1*p2 => p.x()=p1.x()*p2.x(), ... (for each coordinate)
Vector2 operator *(const Vector2 &p1,const Vector2 &p2);
/// Ex : p=p1/p2 => p.x()=p1.x()/p2.x(), ... (for each coordinate)
Vector2 operator /(const Vector2 &p1,const Vector2 &p2);
}




#endif // VECTOR2_H_INCLUDED

