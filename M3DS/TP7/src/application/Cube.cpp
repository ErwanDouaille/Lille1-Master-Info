#include <cmath>
#include "Cube.h"
#include "Line.h"
#include "GLTool.h"
#include <iostream>
#include "IntersectionArray.h"


/**
@file
@author Fabrice Aubert
*/

using namespace p3d;
using namespace std;

Cube::Cube() : Primitive() {}

Cube::ESpaceIntersect Cube::intersectBiPlane(double a,double u,double *res1,double *res2) {
  if (fabs(u)<EPSILON_PREC) { // cas parallèle
    if ((a>-1.0) && (a<1.0)) {
      return FULL_IN;
    }
    else {
      return FULL_OUT;
    }
  }
  else {
    *res1=(-a-1.0)/u;
    *res2=(-a+1.0)/u;
    swapIfMin(res1,res2);
    return INTERSECT;
  }
}

void Cube::intersectInterval(ESpaceIntersect isIntersected_new,double l1_new,double l2_new,ESpaceIntersect *isIntersected,double *l1,double *l2) {
  if (isIntersected_new==FULL_OUT) {
    *isIntersected=FULL_OUT;
  }
  else if (isIntersected_new==INTERSECT) {
    if (*isIntersected==FULL_IN) {
      *isIntersected=isIntersected_new;
      *l1=l1_new;
      *l2=l2_new;
    }
    else {
      *l1=max(*l1,l1_new);
      *l2=min(*l2,l2_new);
      if (*l2<*l1) *isIntersected=FULL_OUT;
    }
  }
}


// intersection du cube (très bourrin ici).
void Cube::intersection(const Ray &ray,IntersectionArray *res) {

  res->clear();

  ESpaceIntersect isIntersected,isIntersected_new;
  double l1,l2,l1_new,l2_new;
  Vector3 u=ray.direction();
  Vector3 a=ray.point();

  // intersection plan x=-1 x=1
  isIntersected=intersectBiPlane(a.x(),u.x(),&l1,&l2);
  if (isIntersected==FULL_OUT) goto FIN;

  // intersection plan y=-1 y=1
  isIntersected_new=intersectBiPlane(a.y(),u.y(),&l1_new,&l2_new);
  intersectInterval(isIntersected_new,l1_new,l2_new,&isIntersected,&l1,&l2);
  if (isIntersected==FULL_OUT) goto FIN;
  // intersection plan z=-1 z=1
  isIntersected_new=intersectBiPlane(a.z(),u.z(),&l1_new,&l2_new);
  intersectInterval(isIntersected_new,l1_new,l2_new,&isIntersected,&l1,&l2);
FIN:
  if (isIntersected==INTERSECT) {
    res->addIntersection(l1);
    res->addIntersection(l2);
  }
}

Vector3 Cube::computeNormal(const Vector3 &p) {
  Vector3 res(0,0,0);
  if ((fabs(p.x())>=fabs(p.y())) && (fabs(p.x())>=fabs(p.z())))
    res.x(1.0);
  else
  if ((fabs(p.y())>=fabs(p.x())) && (fabs(p.y())>=fabs(p.z())))
    res.y(1.0);
  else
    res.z(1.0);
  return res;
}

void Cube::drawGL() {
  p3d::shaderLightPhong();
  materialGL();
  p3d::drawCube();
}

