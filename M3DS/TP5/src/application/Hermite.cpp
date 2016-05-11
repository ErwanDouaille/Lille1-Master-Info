#include "Hermite.h"

#include <iostream>
#include <sstream>
#include "GLTool.h"
#include "Matrix4.h"

/**
@file
@author F. Aubert
*/


using namespace p3d;
using namespace std;

/**
* Evaluation de la courbe de hermite P(t) :
* - _a,_b,_ta et _tb sont les données géométriques (points extrémités et tangentes).
* - on peut utiliser les opérateurs *, + entre les points et des doubles. Exemple : p=matrix[i]*_a+_b, ...
**/
Vector3 Hermite::eval(double t) {
  // initialiser la matrice 4x4 avec les valeurs correctes pour une courbe de Hermite (cf cours)
  double x,y,z;
  x=y=z=0.0;
  double matrix[16]={2.0, -2.0, 1.0, 1.0,
                     -3.0, 3.0, -2.0, -1.0,
                     0.0, 0.0, 1.0, 0.0,
                     1.0, 0.0, 0.0, 0.0};

  Matrix4 m;
  m.set(matrix);

  Vector4 p(t*t*t, t*t, t, 1);
  Vector4 a = m * p;
  Vector3 g[4] = {_a, _b, _ta, _tb};

  x = a.x()*g[0].x() + a.y()*g[1].x() + a.z()*g[2].x() + a.w()*g[3].x();
  y = a.x()*g[0].y() + a.y()*g[1].y() + a.z()*g[2].y() + a.w()*g[3].y();
  z = a.x()*g[0].z() + a.y()*g[1].z() + a.z()*g[2].z() + a.w()*g[3].z();

  Vector3 res(x,y,z);
  return res;
}

/**
* Trace la courbe de hermite (100 points)
**/
void Hermite::draw() {
    vector<Vector3> lPoints;
    float div = 1.0/100;

    // A COMPLETER : calculer 100 points pour décrire la courbe de hermite
    // Il faut faire des lPoints.push_back avec les points calculés (lPoints déjà tracé à la fin de la méthode en GL_LINE_STRIP).
    for (int i = 0; i <= 100; i++) {
        lPoints.push_back(this->eval(i*div));
    }

    p3d::drawThickLineStrip(lPoints);

}


/** **************************************************************************************** */
Hermite::Hermite(const Vector3 &a,const Vector3 &na,const Vector3 &b,const Vector3 &nb) {
  _a=a;
  _b=b;
  _ta=na;
  _tb=nb;

  _nbInput=0;
}

p3d::Vector3 *Hermite::interactPoint(unsigned int i) {
    switch(i) {
    case 0:return &_a;
    case 2:return &_b;
    case 1:return &_interactTa;
    case 3:return &_interactTb;
    default:return NULL;
    }
}

void Hermite::interactUpdate(unsigned int i) {
    if (i==1) _ta=(_interactTa-_a)*5;
    if (i==3) _tb=(_interactTb-_b)*5;
    if (i==0) _interactTa=(_a+_ta/5);
    if (i==2) _interactTb=(_b+_tb/5);
}

void Hermite::interactInsert(unsigned int i, const Vector3 &p) {

    switch(i) {
    case 0:_a=p;break;
    case 2:_b=p;break;
    case 1:_interactTa=p;break;
    case 3:_interactTb=p;break;
    case 4:_nbInput=0;_a=p;break;
    default:break;
    }
    _nbInput++;
}

void Hermite::set(const Vector3 &a,const Vector3 &ta,const Vector3 &b,const Vector3 &tb) {
  _a=a;
  _b=b;
  _ta=ta;
  _tb=tb;
}

void Hermite::drawControl() {
p3d::diffuseColor=Vector3(0,0,1);
p3d::ambientColor=Vector4(0,0,1,1);
glPointSize(5);
switch(nbInput()) {
case 4:
    p3d::shaderLightPhong();
  p3d::drawArrow(b(),tb()/5,0.01,"","T_B");
case 3:
  p3d::draw("B",b()+Vector3(0.02,0.02,0.0));
  p3d::shaderVertexAmbient();
  p3d::drawPoints({b()});
case 2:
    p3d::shaderLightPhong();
    p3d::drawArrow(a(),ta()/5,0.01,"","T_A");
case 1:
  p3d::draw("A",a()+Vector3(0.01,0.01,0.0));
  p3d::shaderVertexAmbient();
  p3d::drawPoints({a()});
}
}


Hermite::Hermite() {
  //ctor
}

Hermite::~Hermite() {
  //dtor
}

