#ifndef VECTOR4_H_INCLUDED
#define VECTOR4_H_INCLUDED

namespace p3d {
class Vector3;
}

#include <string>
#include "Vector3.h"

/**
@file
@author F. Aubert
@brief Operations on homegeneous co-ordinates (x,y,z,w)
*/

namespace p3d {

/**
@class Vector4
@brief Operations on homegeneous co-ordinates (x,y,z,w)
*/
class Vector4 {
    double _c[4];
    static float _fc[4]; // for casting

    public:
    /// destructor
    virtual ~Vector4();
    /// copy
    Vector4 &operator=(const Vector4 &u);
    /// default constructor
    Vector4();

    /// copy constructor
    Vector4(const Vector4 &p);
    /// construction from Vector3
    explicit Vector4(const Vector3 &p,double w=1.0);
    /// constructor from (x,y,z,w)
    explicit Vector4(double x,double y,double z=0.0,double w=1.0);

    /// sets
    void set(const Vector3 &p,double w=1.0);
    void set(const Vector4 &p);
    void set(double x, double y, double z, double w);

    double &operator()(unsigned int i) {return _c[i];}
    const double &operator()(unsigned int i) const {return _c[i];}

    /// returns
    inline double x() const {return _c[0];}
    inline double y() const {return _c[1];}
    inline double z() const {return _c[2];}
    inline double w() const {return _c[3];}
    inline double r() const {return _c[0];}
    inline double g() const {return _c[1];}
    inline double b() const {return _c[2];}
    inline double a() const {return _c[3];}

    /// sets
    void x(double x) {_c[0]=x;}
    void y(double y) {_c[1]=y;}
    void z(double z) {_c[2]=z;}
    void w(double w) {_c[3]=w;}
    void r(double x) {_c[0]=x;}
    void g(double y) {_c[1]=y;}
    void b(double z) {_c[2]=z;}
    void a(double w) {_c[3]=w;}
    void rgb(double x,double y,double z) {_c[0]=x;_c[1]=y;_c[2]=z;}
    void xyz(double x,double y,double z) {_c[0]=x;_c[1]=y;_c[2]=z;}



    /// divide by w (projection of a homogeneous point)
    Vector3 project() const;

    /// returns the 3 components
    Vector3 xyz() const;

    /// returns a pointer to data
    const double *dv() const;
    /// returns a pointer to data (converted into floats). !caution : the float pointer is shared between all Vector4. For example : foo(a.fv(),b.fv()) => you will get the same coordinates in foo
    const float *fv() const;
    /// print this
    void print(std::string mesg) const;
    /// output stream this

    Vector4 &operator *=(double k);
    Vector4 &operator +=(const Vector4 &u);
    Vector4 &operator -=(const Vector4 &u);
    Vector4 operator -();

    /// sets this to the conversion of p (i.e. this=(p*w,w) ).
    void fromPoint(const Vector3 &p, double w);
    void oppose();
};
std::ostream& operator<<(std::ostream &s,const Vector4 &p);
Vector4 operator *(double k,const Vector4 &u);
Vector4 operator *(const Vector4 &u,double k);
Vector4 operator +(const Vector4 &u,const Vector4 &v);
Vector4 operator -(const Vector4 &u,const Vector4 &v);

}


#endif // POINT4D_H_INCLUDED

