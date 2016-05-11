#include "Cylinder.h"
#include <cmath>
#include "GLTool.h"
#include <iostream>

/**
@file
@author Fabrice Aubert
*/

using namespace p3d;
using namespace std;

Cylinder::Cylinder() : Primitive() {}

Cylinder::ESpaceIntersect Cylinder::intersectCap(double a,double u,double *res1,double *res2) {
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
    return INTERSECT_IN;
  }
}

void Cylinder::intersectInterval(ESpaceIntersect isIntersected_new,double l1_new,double l2_new,ESpaceIntersect *isIntersected,double *l1,double *l2) {
  if (isIntersected_new==FULL_OUT) {
    *isIntersected=FULL_OUT;
  }
  else if (isIntersected_new==INTERSECT_IN) {
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


// donne la liste des réels k issus de l'intersection de l=A+kv et du Cylindre x^2+y^2=1 (attention aux bases : plan z=1, z=-1)
void Cylinder::intersection(const Ray &ray,IntersectionArray *resArray) {

   /**
   * l est dans le repère local du cône
   * il faut retourner 0 ou 2 intersections
   * il faut que l'intersection minimale soit en premier
   *
   * ray.point(), ray.direction() donnent l'origine et le vecteur directeur de ray
   * u1.dot(u2) donne le produit scalaire entre 2 vecteurs ou points
   * !! pour ajouter une intersection à la fin de la liste res, utilisez : res->addIntersection(lambda)
   */

  resArray->clear();
  Vector3 a=ray.point();
  Vector3 u=ray.direction();
  Cylinder::ESpaceIntersect isCapIntersected;
  double interCap1,interCap2;
  isCapIntersected=intersectCap(a.z(),u.z(),&interCap1,&interCap2);
  if (isCapIntersected!=FULL_OUT) {
    double res1=0.0,res2=0.0;
    double A=u.x()*u.x()+u.y()*u.y();
    double B=2.0*(u.x()*a.x()+u.y()*a.y());
    double C=a.x()*a.x()+a.y()*a.y()-1;
    double delta=B*B-4.0*A*C;
    if (delta>0) {
        res1=(-B+sqrt(delta))/2.0/A;
        res2=(-B-sqrt(delta))/2.0/A;
        swapIfMin(&res1,&res2);
        intersectInterval(INTERSECT_IN,res1,res2,&isCapIntersected,&interCap1,&interCap2);
        if (isCapIntersected==INTERSECT_IN) {
          resArray->addIntersection(interCap1);
          resArray->addIntersection(interCap2);
        }
    }
    else if (C<0) {
      resArray->addIntersection(interCap1);
      resArray->addIntersection(interCap2);
    }
  }
}

Vector3 Cylinder::computeNormal(const Vector3 &p) {
  if (fabs(p.z()-1.0)<0.001) {
    return Vector3(0,0,1);
  }
  else if (fabs(p.z()+1)<0.0001) {
    return Vector3(0,0,-1);
  }
  else
    return Vector3(p.x(),p.y(),0);
}


void Cylinder::drawGL() {
    materialGL();
    p3d::modelviewMatrix.push();
    p3d::modelviewMatrix.translate(0,0,-1);
    p3d::modelviewMatrix.scale(1,1,2);
    p3d::shaderLightPhong();
    p3d::drawCylinder();
    p3d::modelviewMatrix.pop();
}

