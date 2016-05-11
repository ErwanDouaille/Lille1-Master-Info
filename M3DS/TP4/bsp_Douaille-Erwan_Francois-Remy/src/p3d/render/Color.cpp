#include "Color.h"
#include <cmath>

/*!
*
* @file
*
* @brief Color Class (r,g,b,a)
* @author F. Aubert
*
*/

using namespace p3d;
using namespace std;

Color::~Color() {
}

Color &Color::operator=(const Color &c) {
  _c[0]=c.r();_c[1]=c.g();_c[2]=c.b();_c[3]=c.a();
  return *this;
}

Color::Color() {
  _c[0]=0.0;_c[1]=0.0;_c[2]=0.0;_c[3]=0.0;
}

Color::Color(const Vector3 &t,double a) {_c[0]=t.x();_c[1]=t.y();_c[2]=t.z();_c[3]=a;}

Color::Color(double r,double g,double b,double a) { _c[0]=r;_c[1]=g;_c[2]=b;_c[3]=a;}



unsigned char Color::rb() const {
  return min(max(int(r()*255),0),255);
}

unsigned char Color::gb() const {
  return min(max(int(g()*255),0),255);
}

unsigned char Color::bb() const {
  return min(max(int(b()*255),0),255);
}

void Color::rb(unsigned char c) {
  r(min(max(double(c)/255.0,0.0),1.0));
}

void Color::gb(unsigned char c) {
  g(min(max(double(c)/255.0,0.0),1.0));
}

void Color::bb(unsigned char c) {
  b(min(max(double(c)/255.0,0.0),1.0));
}

void Color::setbv(unsigned char *c) {
  rb(c[0]);gb(c[1]);bb(c[2]);
}


Color p3d::operator*(const Color &c1,const Color &c2) {
  return Color(c1.r()*c2.r(),c1.g()*c2.g(),c1.b()*c2.b(),c1.a()*c2.a());
}

Color p3d::operator*(double k,const Color &c) {
  Color res;
  res.set(k*c.r(),k*c.g(),k*c.b(),k*c.a());
  return res;
}

Color p3d::operator*(const Color &c,double k) {
  Color res;
  res.set(k*c.r(),k*c.g(),k*c.b(),k*c.a());
  return res;
}

Color p3d::operator-(const Color &c1,const Color &c2) {
  Color res;
  res.set(c1.r()-c2.r(),c1.g()-c2.g(),c1.b()-c2.b(),c1.a()-c2.a());
  return res;
}

Color p3d::operator+(const Color &c1,const Color &c2) {
  Color res;
  res.set(c1.r()+c2.r(),c1.g()+c2.g(),c1.b()+c2.b(),c1.a()+c2.a());
  return res;
}

const Color &Color::operator+=(const Color &c) {
  _c[0]+=c.r();
  _c[1]+=c.g();
  _c[2]+=c.b();
  _c[3]+=c.a();
  return *this;
}


const Color &Color::add(const Color &c) {
  r(_c[0]+c.r());
  g(_c[1]+c.g());
  b(_c[2]+c.b());
  return *this;
}

const Color &Color::addClamp(const Color &c) {
    this->add(c);
    if (this->r()>1) this->r(1);
    else if (this->r()<0) this->r(0);
    if (this->g()>1) this->g(1);
    else if (this->g()<0) this->b(0);
    if (this->b()>1) this->b(1);
    else if (this->b()<0) this->g(0);
    return *this;
}

ostream& p3d::operator <<(std::ostream &s,const Color &c) {
    s << "(" << c.r() << "," << c.g() << "," << c.b() << "," << c.a() << ")";
    return s;
}


