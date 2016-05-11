/*
 * Matrix3.h
 *
 *  Created on: 14 mars 2011
 *      @author: aubert
 */

#ifndef MATRIX3_H_
#define MATRIX3_H_


/*!
*
* @file
*
* @brief 3x3 Matrix Class
* @author F. Aubert
*
*/

namespace p3d {
  class Matrix4;
  class Vector3;


class Matrix3 {
  double _c[9];
  static float _cf[9]; ///< for casting in float

public:
  /// destructor
  virtual ~Matrix3();
  /// copy
  Matrix3 &operator=(const Matrix3 &m);
  /// constructor
  Matrix3();
  /// converts a 4x4 matrix to 3x3 : it is the "top-left" sub matrix
  explicit Matrix3(const Matrix4 &m);

  /// returns the pointer to data as float (!caution : the pointer is shared between all Matrix3)
  const float *fv() const ;
  /// returns the pointer to data (column-major order)
  inline const double *dv() const {return _c;}
  /// returns the i-th element (column-major order)
  inline const double &operator()(unsigned int i) const {return _c[i];}
  /// returns the i-th element (column-major order)
  inline double &operator()(unsigned int i) {return _c[i];}
  /// transforms p by this matrix
  void transform(p3d::Vector3 *p) const;



  Matrix3 &invert(const Matrix3 &a);
  Matrix3 &invert();
  Matrix3 inverse() const;
  Matrix3 &transpose();
  void subScaleColumn(int i, int j, double s);
  void setIdentity();
  void set(const double *v);


  /// returns the i-th coefficient of this (Matrix3 is column-major)
  inline double &operator()(int i) {return _c[i];}
  /// returns the i-th coefficient of this (Matrix3 is column-major)
  inline const double &operator()(int i) const {return _c[i];}
  /// returns the i-th coefficient of this (Matrix3 is column-major)
  const double &at(int i) const {return _c[i];}
  /// returns the i-th coefficient of this (Matrix3 is column-major)
  double &at(int i) {return _c[i];}
  /// returns the element at row i and at column j
  inline double &operator()(int i,int j) {return _c[i+(j*3)];}
  /// returns the element at row i and at column j
  inline const double &at(int i,int j) const {return _c[i+(j*2)];}
  /// returns the element at row i and at column j
  inline double &at(int i,int j) {return _c[i+(j*2)];}
  /// returns the element at row i and at column j
  inline const double &operator()(int i,int j) const {return _c[i+(j*3)];}


  void swapColumn(int i, int j);
  void scaleColumn(int i, double k);
};

/// returns the transformation of p by the matrix m. Ex : p2=m*p1
Vector3 operator*(const p3d::Matrix3 &m,const p3d::Vector3 &p);


}


#endif /* MATRIX3_H_ */

