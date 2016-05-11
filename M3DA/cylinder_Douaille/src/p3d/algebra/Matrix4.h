#ifndef Matrix4_H_INCLUDED
#define Matrix4_H_INCLUDED

#include <vector>
#include <string>
#include <stack>

#include "Matrix3.h"

/**
*
* @file
*
* @brief Matrix 4x4 class
* @author F. Aubert
*
*/

namespace p3d {
class Vector3;
class Vector4;
class Quaternion;

/**
@class Matrix4
@brief Matrix 4x4
*/
class Matrix4 {
    double _m[16]; ///< coefficients (column order)

    static float _mf[16]; ///< for casting to float. Caution !!! static member (can only pass *one* fv() anytime, anywhere)
    static std::stack<double *> _stack; ///< for push/pop. Caution !!! static member (only one stack for all matrix4).

    void init();

public:
    /// destructor
    virtual ~Matrix4();

    /// default constructor : elements of this matrix are undefined
    Matrix4();

    /// copy constructor : this matrix is set to m
    Matrix4(const Matrix4 &m);

    /// copy matrix m to this matrix
    Matrix4 &operator=(const Matrix4 &m);

    /// returns the i-th coefficient of this (Matrix4 is column-major)
    inline double &operator()(int i) {return _m[i];}

    /// returns the i-th coefficient of this (Matrix4 is column-major)
    inline const double &operator()(int i) const {return _m[i];}
    /// returns the i-th coefficient of this (Matrix4 is column-major)
    const double &at(int i) const {return _m[i];}
    /// returns the i-th coefficient of this (Matrix4 is column-major)
    double &at(int i) {return _m[i];}
    /// returns the element at row i and at column j
    inline double &operator()(int i,int j) {return _m[i|(j<<2)];}
    /// returns the element at row i and at column j
    inline const double &at(int i,int j) const {return _m[i|(j<<2)];}
    /// returns the element at row i and at column j
    inline double &at(int i,int j) {return _m[i|(j<<2)];}
    /// returns the element at row i and at column j
    inline const double &operator()(int i,int j) const {return _m[i|(j<<2)];}
    /// set Matrix with vectors (o,i,j,k) (if vectors of frame 1=(o,i,j,k) are given in frame 0 then this=M1->0; the last row is (0,0,0,1))
    void set(const Vector3 &o,const Vector3 &i,const Vector3 &j,const Vector3 &k);
    /// returns the i-th row
    Vector4 row(unsigned int i) const;
    Vector4 column(unsigned int i) const;
    /// direct access to coefficients
    const double *dv() const;
    /// cast to float * (caution !!! the pointer .fv() is shared between all Matrix4; only useful for gl instructions like glMultMatrixf(mat.fv())
    const float *fv() const;
    /// copy the 16 values from v to this (major-column order)
    void set(const double *v);
    /// copy the 16 values from v to this (major-column order)
    void set(float *v);
    /// returns f after the 16 values from this are copied to f (major-column order)
    float *tofv(float f[16]);
    /// transpose this
    Matrix4 &transpose();
    /// invert this
    Matrix4 &invert();
    /// returns the inverse (this unchanged)
    Matrix4 inverse() const;
    /// set this to the inverse of a
    Matrix4 &invert(const Matrix4 &a);
    /// set this to the rotation matrix from v1 to v2 (axis is \f$ v1\wedge v2\f$, and angle is (v1,v2))
    void setRotation(const Vector3 &v1,const Vector3 &v2);
    /// set this to the rotation from angle/axis (angle in degree)
    void setRotation(double angle,const Vector3 &axis);
    /// set this to the rotation from angle/axis (angle in degree)
    void setRotation(double angle,double xaxis,double yaxis,double zaxis);
    /// set this to the corresponding quaternion q
    void setRotation(const Quaternion &q);
    /// set this to the corresponding quaternion q
    void set(const Quaternion &q);

    /// set this to the scale matrix (kx,ky,kz)
    void setScale(double kx,double ky,double kz);
    /// set this to the scale matrix s
    void setScale(const Vector3 &s);
    /// set this to the translation matrix t
    void setTranslation(const Vector3 &t);
    /// set this to the translation matrix (x,y,z)
    void setTranslation(double x,double y,double z);

    /// the point p is transformed according to this (internally the w component is set to 1)
    void transformPoint(Vector3 *p) const;
    /// the direction u is transformed according to this (internally the w component is set to 0)
    void transformDirection(Vector3 *u) const;
    /// p is transformed according to this
    void transform(Vector4 *p) const;

    /// returns the transformation of the point p
    Vector3 transformPoint(const Vector3 &p) const;
    /// returns the transformation of the direction u
    Vector3 transformDirection(const Vector3 &u) const;
    /// returns the transformation of the point p
    Vector4 transform(const Vector4 &p) const;

    /// swap columns i and j
    void swapColumn(int i,int j);
    /// multiply column i by k
    void scaleColumn(int i,double k);
    /// multiply row i of this by k
    void scaleRow(int i,double k);
    /// scale the j-th column by k then substracts the column j : Ci = Ci-k*Cj
    void subScaleColumn(int i,int j,double k);
    /// Scale the j-th row by k then substracts the j-th row : Ri<-Ri-k*Rj
    void subScaleRow(int i,int j,double s);
    /// set the ith row to (x,y,z,w)
    void row(int i,double x,double y,double z,double w);
    /// set the ith column to (x,y,z,w)
    void column(int i,double x,double y,double z,double w);

    /// set to identity
    void setIdentity();
    /// right-multiplies this by a : this = this*a
    Matrix4 &mul(const Matrix4 &a);
    /// left-multiplies this by a : this = a*this
    Matrix4 &mulLeft(const Matrix4 &a);
    /// set this to a*b
    Matrix4 &mul(const Matrix4 &a,const Matrix4 &b);
    /// printf the matrix
    void print(std::string mesg) const;

    /// this matrix is composed with a translation : right-multiplies this by the translation matrix from u
    Matrix4 &translate(const Vector3 &u);
    /// this matrix is composed with a translation : right-multiplies this by the translation matrix from (x,y,z)
    Matrix4 &translate(double x,double y,double z);

    /// this matrix is composed with a rotation (angle in degree) : right-multiplies this by the rotation matrix from (angle,u)
    Matrix4 &rotate(double angle,const Vector3 &u);
    /// this matrix is composed with a rotation (angle in degree) : right-multiplies this by the rotation matrix from (angle,(x,y,z))
    Matrix4 &rotate(double angle, double xaxis, double yaxis, double zaxis);
    /// this matrix is composed with a rotation : right-multiplies this by the rotation matrix from q
    Matrix4 &rotate(const Quaternion &q);
    /// this matrix is composed with a scaling : right-multiplies this by the scaling matrix from u
    Matrix4 &scale(const Vector3 &u);
    /// this matrix is composed with a scaling : right-multiplies this by the scaling matrix from k (actually multiplies all the matrix elements by k)
    Matrix4 &scale(double k);
    /// this matrix is composed with a scaling : right-multiplies this by the scaling matrix from k (actually multiplies all the matrix elements by (x,y,z))
    Matrix4 &scale(double x,double y,double z=1.0);
    /// this matrix is composed with the lookAt matrix
    Matrix4 &lookAt(const Vector3 &position,const Vector3 &at,const Vector3 &up);
    /// set this with the linear mix of m1 and m2 : this = (1-t)*m1+t*m2
    void mix(double t,const Matrix4 &m1,const Matrix4 &m2);
    /// set this with the linear mix of this and m : this = (1-t)*this+t*m
    Matrix4 &mix(double t,const Matrix4 &m);
    /// right-multiplies this by the frustum matrix (prepared for 1/z interpolation, i.e. same as openGL 2.1)
    Matrix4 &frustum(double left, double right, double bottom, double top, double dnear, double dfar);
    /// right-multiplies this by the orthogonal projection matrix
    Matrix4 &ortho(double left,double right,double bottom,double top,double near,double far);
    /// right-multiplies this by the perspective matrix (same as gluPerspective)
    Matrix4 &perspective(double fov, double aspect, double dnear, double dfar);
    /// returns the normal matrix (i.e. the top-left 3x3 matrix from transpose(inverse(this))
    Matrix3 normalMatrix() const;
    /// saves the content of this onto the top of the stack (!caution : only one stack for all matrices)
    void push();
    /// restores the content of this from the top of the stack
    void pop();


    /// right-multiplies this matrix by m
    Matrix4 &operator*=(const Matrix4 &m);





    /// converts translation v to the corresponding matrix
    static Matrix4 fromTranslation(const Vector3 &v);
    /// converts translation (x,y,z) to the corresponding matrix
    static Matrix4 fromTranslation(double x,double y,double z);
    /// converts scale v to the corresponding matrix
    static Matrix4 fromScale(const Vector3 &v);
    /// converts scale (k,k,k) to the corresponding matrix
    static Matrix4 fromScale(double k);
    /// converts scale (x,y,z) to the corresponding matrix
    static Matrix4 fromScale(double x,double y,double z=1.0);
    /// converts angle/axis to the corresponding matrix (angle in degrees).
    static Matrix4 fromAngleAxis(double angle, const Vector3 &axe);
    static Matrix4 fromAngleAxis(double angle,double xAxis,double yAxis,double zAxis);
    /// returns a frustum projection matrix
    static Matrix4 fromFrustum(double left, double right, double bottom, double top, double dnear, double dfar);
    /// returns an orthogonal projection matrix
    static Matrix4 fromOrtho(double left, double right, double bottom, double top, double dnear, double dfar);
    /// returns a perspective projection matrix
    static Matrix4 fromPerspective(double fov, double aspect, double dnear, double dfar);
    /// converts a quaternion to the corresponding matrix
    static Matrix4 fromQuaternion(const Quaternion &q);
    /// returns the identity
    static Matrix4 identity() {Matrix4 m;m.setIdentity();return m;}
    void column(int i, const p3d::Vector4 &v);
    void setColumn(const Vector4 &c0, const Vector4 &c1, const Vector4 &c2, const Vector4 &c3);
    void setFrustum(double left, double right, double bottom, double top, double dnear, double dfar);
    void setOrtho(double left, double right, double bottom, double top, double dnear, double dfar);
};


/// returns the opposite of m
Matrix4 operator-(const Matrix4 &m);
/// returns m1*m2 (composition of m1 with m2)
Matrix4 operator*(const Matrix4 &m1,const Matrix4 &m2);
/// returns m1*p (transformation of p according to m1)
Vector4 operator*(const Matrix4 &m,const Vector4 &p);



/// use it only for conversion from Matrix4 (double coefficients) to float coefficients when necessary
class Matrix4f {
  float _m[16];
public:
  explicit Matrix4f(const Matrix4 &m) {for(unsigned int i=0;i<16;i++) _m[i]=m(i);}
  float *fv() {return _m;}
};


}


#endif // Matrix4_H_INCLUDED

