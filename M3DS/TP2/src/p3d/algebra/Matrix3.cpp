#include <cmath>

#include "Tools.h"

#include "Matrix3.h"
#include "Matrix4.h"
#include "Vector3.h"


/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

using namespace p3d;

float Matrix3::_cf[9];

Matrix3::Matrix3() {
	// TODO Auto-generated constructor stub

}

Matrix3::~Matrix3() {
	// TODO Auto-generated destructor stub
}

const float *Matrix3::fv() const {
    for(unsigned int i=0;i<9;i++) {
        _cf[i]=_c[i];
    }
    return _cf;
}


Matrix3::Matrix3(const Matrix4 &m) {
  _c[0]=m(0);
  _c[1]=m(1);
  _c[2]=m(2);

  _c[3]=m(4);
  _c[4]=m(5);
  _c[5]=m(6);

  _c[6]=m(8);
  _c[7]=m(9);
  _c[8]=m(10);
}

Matrix3 &Matrix3::operator=(const p3d::Matrix3 &m) {
  for(unsigned int i=0;i<9;++i) {
    _c[i]=m(i);
  }
  return *this;
}

void Matrix3::transform(Vector3 *p) const {
  Vector3 temp(*p);
  p->x(_c[0]*temp.x()+_c[3]*temp.y()+_c[6]*temp.z());
  p->y(_c[1]*temp.x()+_c[4]*temp.y()+_c[7]*temp.z());
  p->z(_c[2]*temp.x()+_c[5]*temp.y()+_c[8]*temp.z());
}

Vector3 p3d::operator*(const p3d::Matrix3 &m,const p3d::Vector3 &p) {
  Vector3 res(p);
  m.transform(&res);
  return res;
}

Matrix3 &Matrix3::invert(const Matrix3 &a) {
  Matrix3 temp(a);
  this->setIdentity();

  double t,t2;
  int i,j,k;
  int swap;

  // down
  for(i=0;i<3;++i) {
    double max=temp(i,i);
    swap=i;
    for(j=(i+1);j<3;++j) {
      if (fabs(temp(i,j))>fabs(max)) {
        max=temp(i,j);
        swap=j;
      }
    }
    if (fabs(max)<1e-08)
      throw Error("matrix inverse",__LINE__,__FILE__);
    if (swap!=i) {
      temp.swapColumn(i,swap);
      this->swapColumn(i,swap);
    }
    t=temp(i,i);

    this->scaleColumn(i,1.0/t);
    temp.scaleColumn(i,1.0/t);
    for(k=i+1;k<3;++k) {
      t2=temp(i,k);
      this->subScaleColumn(k,i,t2);
      temp.subScaleColumn(k,i,t2);
    }
  }

  // up
  for(i=2;i>=0;--i) {
    for(k=i-1;k>=0;--k) {
      t2=temp(i,k);
      this->subScaleColumn(k,i,t2);
      temp.subScaleColumn(k,i,t2);
    }
  }
  return *this;
}


Matrix3 &Matrix3::invert() {
    Matrix3 temp(*this);
    this->invert(temp);
    return *this;
}

Matrix3 Matrix3::inverse() const {
  Matrix3 res;
  return res.invert(*this);
}

void Matrix3::subScaleColumn(int i,int j,double s) {
    int c1=i*3;int c2=j*3;
    for(int k=0;k<3;k++) {
        _c[c1]-=(_c[c2]*s);
        c1++;c2++;
    }
}

void Matrix3::setIdentity() {
  set((const double[9]){1,0,0,0,1,0,0,0,1});
}

void Matrix3::set(const double *v) {
    for(int i=0;i<9;i++) {
      _c[i]=v[i];
    }
}

void Matrix3::swapColumn(int i,int j) {
    double swap;
    int c1=i*3;
    int c2=j*3;
    for(int k=0;k<3;k++) {
        swap=_c[c1];
        _c[c1]=_c[c2];
        _c[c2]=swap;
        c1++;c2++;
    }
}


void Matrix3::scaleColumn(int i,double k) {
    int c=i*3;
    for(int j=0;j<3;j++) {
        _c[c++]*=k;
    }
}

