#include "Matrix4.h"

#include "Tools.h"
#include "Vector4.h"
#include "Vector3.h"
#include "Quaternion.h"
#include "Matrix3.h"

#include <iostream>
#include <cmath>
#include <string>
#include <iomanip>



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

float Matrix4::_mf[16];
std::stack<double *> Matrix4::_stack; ///< for push/pop. Caution !!! static member (only one stack for all matrix4).

Matrix4::Matrix4() {
}

Matrix4::~Matrix4() {
}

Matrix4::Matrix4(const Matrix4 &m) {
    *this=m;
}

Matrix4 &Matrix4::operator=(const Matrix4 &m) {
  for(unsigned int i=0;i<16;++i) {
    _m[i]=m(i);
  }
  return *this;
}


Vector4 p3d::operator*(const Matrix4 &m,const Vector4 &p) {
  return m.transform(p);
}

void Matrix4::set(const Vector3 &o,const Vector3 &i,const Vector3 &j,const Vector3 &k) {
  _m[0]=i.x();
  _m[1]=i.y();
  _m[2]=i.z();
  _m[3]=0.0;

  _m[4]=j.x();
  _m[5]=j.y();
  _m[6]=j.z();
  _m[7]=0.0;

  _m[8]=k.x();
  _m[9]=k.y();
  _m[10]=k.z();
  _m[11]=0.0;

  _m[12]=o.x();
  _m[13]=o.y();
  _m[14]=o.z();
  _m[15]=1.0;
}



const double *Matrix4::dv() const {
    return _m;
}

const float *Matrix4::fv() const {
    for(int i=0;i<16;i++) {
        _mf[i]=_m[i];
    }
    return _mf;
}


float *Matrix4::tofv(float f[16]) {
  for(int i=0;i<16;++i) {
    f[i]=_m[i];
  }
  return f;
}

void Matrix4::transformPoint(Vector3 *p) const {
  Vector4 temp(*p,1);
  this->transform(&temp);
  p->point(temp);
}

void Matrix4::transformDirection(Vector3 *p) const {
  Vector4 temp(*p,0);
  transform(&temp);
  p->direction(temp);
}

Vector3 Matrix4::transformPoint(const Vector3 &p) const {
    Vector3 res=p;
    transformPoint(&res);
    return res;
}

Vector3 Matrix4::transformDirection(const Vector3 &p) const {
    Vector3 res=p;
    transformDirection(&res);
    return res;
}



void Matrix4::transform(Vector4 *p) const {
  Vector4 temp(*p);
  p->x(_m[0]*temp.x()+_m[4]*temp.y()+_m[8]*temp.z()+_m[12]*temp.w());
  p->y(_m[1]*temp.x()+_m[5]*temp.y()+_m[9]*temp.z()+_m[13]*temp.w());
  p->z(_m[2]*temp.x()+_m[6]*temp.y()+_m[10]*temp.z()+_m[14]*temp.w());
  p->w(_m[3]*temp.x()+_m[7]*temp.y()+_m[11]*temp.z()+_m[15]*temp.w());
}


Vector4 Matrix4::transform(const Vector4 &p) const {
    Vector4 res=p;
    transform(&res);
    return res;
}

void Matrix4::setIdentity() {
    for(unsigned int a=0;a<16;a++) {
        _m[a]=0.0;
    }

    for(int i=0;i<16;i+=5) {
      _m[i]=1.0;
    }

}

void Matrix4::set(const double *v) {
    for(int i=0;i<16;i++) {
      _m[i]=v[i];
    }
}

void Matrix4::set(float *v) {
    for(int i=0;i<16;i++) {
        _m[i]=v[i];
    }
}

void Matrix4::print(string mesg) const {
    cout << "matrix " << mesg << endl;
    int index=0;
    for(int i=0;i<4;i++) {
        cout << "(";
        for(int j=0;j<4;j++) {
          cout << setw(4) << setprecision(2) << _m[index] << " ";
          index+=4;
        }
        cout << ")" << endl;
        index-=15;
    }
}

void Matrix4::swapColumn(int i,int j) {
    double swap;
    int c1=i*4;
    int c2=j*4;
    for(int k=0;k<4;k++) {
        swap=_m[c1];
        _m[c1]=_m[c2];
        _m[c2]=swap;
        c1++;c2++;
    }
}


void Matrix4::scaleColumn(int i,double k) {
    int c=i*4;
    for(int j=0;j<4;j++) {
        _m[c++]*=k;
    }
}

void Matrix4::scaleRow(int i,double k) {
    int c=i;
    for(int j=0;j<4;++j) {
        _m[c]*=k;
        c+=4;
    }
}


Matrix4 &Matrix4::mul(const Matrix4 &a) {
  Matrix4 temp;
  for(int i=0;i<4;++i) {
    for(int j=0;j<4;++j) {
      temp(i,j)=0;
      for(int k=0;k<4;k++) {
        temp(i,j)+=_m[i+k*4]*a(k,j);
      }
    }
  }
  return *this=temp;
}

Matrix4 &Matrix4::mulLeft(const Matrix4 &a) {
  Matrix4 temp;
  for(int i=0;i<4;++i) {
    for(int j=0;j<4;++j) {
      temp(i,j)=0;
      for(int k=0;k<4;++k) {
        temp(i,j)+=a(i,k)*_m[k+j*4];
      }
    }
  }
  return *this=temp;
}



Matrix4 &Matrix4::mul(const Matrix4 &a,const Matrix4 &b) {
  if ((this==&a) || (this==&b)) throw ErrorD("cant self mul : use mul(const Matrix4 &)");
  for(int i=0;i<4;++i) {
    for(int j=0;j<4;++j) {
      at(i,j)=0;
      for(int k=0;k<4;++k) {
        at(i,j)+=a(i,k)*b(k,j);
      }
    }
  }
  return *this;
}


Matrix4 &Matrix4::transpose() {
  double swapV;
  for(int i=0;i<4;++i) {
    for(int j=0;j<i;++j) {
      swapV=at(i,j);
      at(i,j)=at(j,i);
      at(j,i)=swapV;
    }
  }
  return *this;
}

Matrix4 &Matrix4::invert() {
    Matrix4 temp(*this);
    this->invert(temp);
    return *this;
}

Matrix4 Matrix4::inverse() const {
  Matrix4 res;
  return res.invert(*this);
}


void Matrix4::subScaleColumn(int i,int j,double s) {
    int c1=i*4;int c2=j*4;
    for(int k=0;k<4;k++) {
        _m[c1]-=(_m[c2]*s);
        c1++;c2++;
    }
}

void Matrix4::subScaleRow(int i,int j,double s) {
    int r1=i;int r2=j;
    for(int k=0;k<4;k++) {
        _m[r1]-=(_m[r2]*s);
        r1+=4;r2+=4;
    }
}

void Matrix4::row(int i,double x,double y,double z,double w) {
    _m[i]=x;_m[i+4]=y;_m[i+8]=z;_m[i+12]=w;
}

void Matrix4::column(int i,double x,double y,double z,double w) {
    int i4=i*4;
    _m[i4]=x;_m[i4+1]=y;_m[i4+2]=z;_m[i4+3]=w;
}

void Matrix4::column(int i,const p3d::Vector4 &v) {
    int i4=i*4;
    _m[i4++]=v(0);_m[i4++]=v(1);_m[i4++]=v(2);_m[i4++]=v(3);
}



Matrix4 &Matrix4::invert(const Matrix4 &a) {
  Matrix4 temp(a);
  this->setIdentity();

  double t,t2;
  int i,j,k;
  int swap;

  // down
  for(i=0;i<4;++i) {
    double max=temp(i,i);
    swap=i;
    for(j=(i+1);j<4;++j) {
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
    for(k=i+1;k<4;++k) {
      t2=temp(i,k);
      this->subScaleColumn(k,i,t2);
      temp.subScaleColumn(k,i,t2);
    }
  }

  // up
  for(i=3;i>=0;--i) {
    for(k=i-1;k>=0;--k) {
      t2=temp(i,k);
      this->subScaleColumn(k,i,t2);
      temp.subScaleColumn(k,i,t2);
    }
  }
  return *this;
}



void Matrix4::set(const Quaternion &q) {
  double x=q.x(),y=q.y(),z=q.z(),w=q.w();
  double x2=x*x,y2=y*y,z2=z*z,w2=w*w;
  double xy=2.0*x*y,xz=2.0*x*z,xw=2.0*x*w,yz=2.0*y*z,yw=2.0*y*w,zw=2.0*z*w;
  this->column(0,w2+x2-y2-z2,xy+zw,xz-yw,0);
  this->column(1,xy-zw,w2-x2+y2-z2,yz+xw,0);
  this->column(2,xz+yw,yz-xw,w2-x2-y2+z2,0);
  this->column(3,0,0,0,1);
}

void Matrix4::setColumn(const Vector4 &c0,const Vector4 &c1,const Vector4 &c2,const Vector4 &c3) {
  column(0,c0);
  column(1,c1);
  column(2,c2);
  column(3,c3);
}

Matrix4 Matrix4::fromQuaternion(const Quaternion &q) {
  Matrix4 res;
  res.set(q);
  return res;
}

void Matrix4::setRotation(const Vector3 &v1,const Vector3 &v2) {
    Quaternion q;
    q.setRotation(v1,v2);
    this->set(q);
}

void Matrix4::setRotation(double angle,const Vector3 &axis) {
    Quaternion q;
    q.setAngleAxis(angle,axis);
    this->set(q);
}

void Matrix4::setRotation(const Quaternion &q) {
    this->set(q);
}

void Matrix4::setRotation(double angle,double xaxis,double yaxis,double zaxis) { // TODO : should be Vector3 version from x,y,z version
  setRotation(angle,Vector3(xaxis,yaxis,zaxis));
}


void Matrix4::setScale(double kx,double ky,double kz) {
  row(0,kx,0,0,0);
  row(1,0,ky,0,0);
  row(2,0,0,kz,0);
  row(3,0,0,0,1);
}

void Matrix4::setScale(const Vector3 &s) {
  row(0,s.x(),0,0,0);
  row(1,0,s.y(),0,0);
  row(2,0,0,s.z(),0);
  row(3,0,0,0,1);
}

void Matrix4::setTranslation(double x,double y,double z) {
  row(0,1,0,0,x);
  row(1,0,1,0,y);
  row(2,0,0,1,z);
  row(3,0,0,0,1);
}


void Matrix4::setTranslation(const Vector3 &v) {
  setTranslation(v.x(),v.y(),v.z());
}

Matrix4 Matrix4::fromTranslation(const Vector3 &v) {
  return fromTranslation(v.x(),v.y(),v.z());
}

Matrix4 Matrix4::fromTranslation(double x,double y,double z) {
  Matrix4 res;
  res.setTranslation(x,y,z);
  return res;
}

Matrix4 Matrix4::fromScale(const Vector3 &v) {
  Matrix4 res;
  res.setScale(v);
  return res;
}

Matrix4 Matrix4::fromScale(double x,double y,double z) {
  Matrix4 res;
  res.setScale(x,y,z);
  return res;
}


Matrix4 Matrix4::fromScale(double k) {
  Matrix4 res;
  res.setScale(k,k,k);
  return res;
}

Matrix4 Matrix4::fromAngleAxis(double angle,const Vector3 &axe) {
  Matrix4 res;
  res.setRotation(angle,axe);
  return res;
}

Matrix4 Matrix4::fromAngleAxis(double angle,double xAxis,double yAxis,double zAxis) {
  Matrix4 res;
  res.setRotation(angle,Vector3(xAxis,yAxis,zAxis));
  return res;
}


Matrix4 &Matrix4::scale(const Vector3 &u) {
  Matrix4 m;
  m.setScale(u);
  this->mul(m);
  return *this;
}

Matrix4 &Matrix4::scale(double k) {
  for(unsigned int i=0;i<12;++i) { // last column unchanged by scale
    _m[i]*=k;
  }
  return *this;
}

Matrix4 &Matrix4::scale(double x,double y,double z) {
  for(unsigned int i=0;i<4;++i) {
    _m[i]*=x;
  }
  for(unsigned int i=4;i<8;++i) {
    _m[i]*=y;
  }
  for(unsigned int i=8;i<12;++i) {
    _m[i]*=z;
  }
  return *this;
}



Vector4 Matrix4::row(unsigned int i) const {
  Vector4 res(_m[i],_m[i+4],_m[i+8],_m[i+12]);
	return res;
}

Vector4 Matrix4::column(unsigned int i) const {
  int ii=i*4;
  return Vector4(_m[ii],_m[ii+1],_m[ii+2],_m[ii+3]);
}

Matrix4 &Matrix4::translate(double x,double y,double z) {
  Matrix4 m=Matrix4::fromTranslation(x,y,z);
  return this->mul(m);
}


Matrix4 &Matrix4::translate(const Vector3 &u) {
  return translate(u.x(),u.y(),u.z());
}

Matrix4 &Matrix4::rotate(double angle,const Vector3 &u) {
  Matrix4 m;
  m.setRotation(angle,u);
  return this->mul(m);
}

Matrix4 &Matrix4::rotate(const Quaternion &q) {
  Matrix4 m;
  m.setRotation(q);
  return this->mul(m);
}

Matrix4 &Matrix4::rotate(double angle,double xaxis,double yaxis,double zaxis) {
  return this->rotate(angle,Vector3(xaxis,yaxis,zaxis));
}

void Matrix4::mix(double t,const Matrix4 &m1,const Matrix4 &m2) {
  for(int k=0;k<16;++k) {
    at(k)=m1(k)+t*(m1(k)+m2(k));
  }
}

Matrix4 &Matrix4::mix(double t,const Matrix4 &m) {
  for(int k=0;k<16;++k) {
    at(k)+=t*(at(k)+m(k));
  }
  return *this;
}


Matrix4 p3d::operator*(const Matrix4 &m1,const Matrix4 &m2) {
  Matrix4 res(m1);
  return res.mul(m2);
}

Matrix4 &Matrix4::operator*=(const Matrix4 &m) {
  return this->mul(m);
}

Matrix4 p3d::operator-(const Matrix4 &m) {
  Matrix4 res;
  for(unsigned int i=0;i<16;i++) {
    res(i)=-m(i);
  }
  return res;
}

Matrix4 &Matrix4::frustum(double left,double right,double bottom,double top,double dnear,double dfar) {
  return this->mul(Matrix4::fromFrustum(left,right,bottom,top,dnear,dfar));
}

void Matrix4::setFrustum(double left,double right,double bottom,double top,double dnear,double dfar) {
  *this=fromFrustum(left,right,bottom,top,dnear,dfar);
}


Matrix4 Matrix4::fromFrustum(double left,double right,double bottom,double top,double dnear,double dfar) {
  Matrix4 mat;
  mat.row(0,2.0*dnear/(right-left)  ,0                        ,(right+left)/(right-left)      ,0                           );
  mat.row(1,0                       ,2.0*dnear/(top-bottom)   ,(top+bottom)/(top-bottom)      ,0                           );
  mat.row(2,0                       ,0                        ,-(dfar+dnear)/(dfar-dnear)     ,-2.0*dfar*dnear/(dfar-dnear));
  mat.row(3,0                       ,0                        ,-1                             ,0                           );
  return mat;
}

Matrix4 &Matrix4::ortho(double left,double right,double bottom,double top,double dnear,double dfar) {
  return this->mul(Matrix4::fromOrtho(left,right,bottom,top,dnear,dfar));
}

void Matrix4::setOrtho(double left,double right,double bottom,double top,double dnear,double dfar) {
  *this=fromOrtho(left,right,bottom,top,dnear,dfar);
}


Matrix4 Matrix4::fromOrtho(double left,double right,double bottom,double top,double dnear,double dfar) {
  Matrix4 mat;
  mat.row(0,2.0/(right-left)  ,0                 ,0                    ,-(right+left)/(right-left));
  mat.row(1,0                 ,2.0/(top-bottom)  ,0                    ,-(top+bottom)/(top-bottom));
  mat.row(2,0                 ,0                 ,-2.0/(dfar-dnear)    ,-(dfar+dnear)/(dfar-dnear));
  mat.row(3,0                 ,0                 ,0                    ,1                         );
  return mat;
}

Matrix4 &Matrix4::perspective(double fov,double aspect,double dnear,double dfar) {
  return this->mul(Matrix4::fromPerspective(fov,aspect,dnear,dfar));
}

Matrix4 Matrix4::fromPerspective(double fov,double aspect,double dnear,double dfar) {
  double f=1.0/tan(fov/2.0);
  Matrix4 mat;
  mat.row(0,f/aspect  ,0  ,0                         ,0                          );
  mat.row(1,0         ,f  ,0                         ,0                          );
  mat.row(2,0         ,0  ,(dfar+dnear)/(dnear-dfar) ,2.0*dnear*dfar/(dnear-dfar));
  mat.row(3,0         ,0  ,-1                        ,0                          );
  return mat;
}


Matrix4 &Matrix4::lookAt(const Vector3 &position,const Vector3 &at,const Vector3 &up) {
  Vector3 i,j,k;

  k=position-at;
  i.setCross(up,k);

  j.setCross(k,i);

  i.normalize();
  j.normalize();
  k.normalize();

  Matrix4 look;
  look.set(position,i,j,k);
  look.invert();

  return this->mul(look);
}


void Matrix4::push() {
  double *s,*p;
  s=new double[16];
  p=s;
  for(unsigned int i=0;i<16;i++) {
    *(p++)=_m[i];
  }
  _stack.push(s);
}

void Matrix4::pop() {
  double *s;
  s=_stack.top();
  for(unsigned int i=0;i<16;i++) {
    _m[i]=s[i];
  }
  _stack.pop();
  delete[] s;
}


Matrix3 Matrix4::normalMatrix() const {
  /*
  Matrix4 res4(*this);
  res4.invert();
  res4.transpose();
  Matrix3 res(res4);
  return res;
  */

  Matrix3 res(*this);
  res.invert();
  res.transpose();
  return res; // TODO : may be risky // why ?

}




