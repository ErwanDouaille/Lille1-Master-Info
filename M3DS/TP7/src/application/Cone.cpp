#include "Cone.h"
#include <cmath>
#include "GLTool.h"
#include <iostream>

/**
@file
@author Fabrice Aubert
*/

using namespace p3d;
using namespace std;

Cone::Cone() : Primitive() {}

Cone::ESpaceIntersect Cone::intersectCap(double a,double u,double *res1,double *res2) {
  if (fabs(u)<EPSILON_PREC) { // cas parallèle
    if (a<1.0) {
      return FULL_IN;
    }
    else {
      return FULL_OUT;
    }
  }
  else {
    *res1=(-a+1.0)/u;
    *res2=(-a)/u;
    swapIfMin(res1,res2);
    return Cone::INTERSECT_IN;
  }
}



// donne la liste des réels k issus de l'intersection de l=A+kv et du cone x^2+y^2=z^2 (attention à la base : plan z=1)
void Cone::intersection(const Ray &ray,IntersectionArray *resArray) {

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
  Cone::ESpaceIntersect isCapIntersected;
  double interCap1,interCap2;
  isCapIntersected=intersectCap(a.z(),u.z(),&interCap1,&interCap2);
  if (isCapIntersected!=FULL_OUT) {
    double res1=0.0,res2=0.0;
    Cone::ESpaceIntersect isConeIntersected;
    double A=u.x()*u.x()+u.y()*u.y()-u.z()*u.z();
    double B=2.0*(u.x()*a.x()+u.y()*a.y()-u.z()*a.z());
    double C=a.x()*a.x()+a.y()*a.y()-a.z()*a.z();
    if (fabs(A)<0.0001) { // only one intersection
      if (fabs(B)>0.00001) {
        double res=-C/B;
        // fusion with cap
        if (u.z()<0) {
          // point intersection is an OU point for cone
          if ((res>interCap1) && (res<interCap2)) {
            resArray->addIntersection(interCap1);
            resArray->addIntersection(res);
          }
        }
        else {
          // point intersection is an IN point for cone
          if ((res>interCap1) && (res<interCap2)) {
            resArray->addIntersection(res);
            resArray->addIntersection(interCap2);
          }
        }
      }
    }
    else {
      double delta=B*B-4.0*A*C;
      if (delta>0) {
        res1=(-B+sqrt(delta))/2.0/A;
        res2=(-B-sqrt(delta))/2.0/A;
        swapIfMin(&res1,&res2);
        if (A<0) isConeIntersected=INTERSECT_OUT; // first is a out point
        else isConeIntersected=INTERSECT_IN; // first is a in point
        //fusion with cap
        if (isConeIntersected==INTERSECT_OUT && isCapIntersected==INTERSECT_IN) {
          if (interCap1<=res1 && interCap2>=res1) {
            resArray->addIntersection(interCap1);
            resArray->addIntersection(res1);
          }
          else if ((interCap1<=res2 && interCap2>=res2)) {
            resArray->addIntersection(res2);
            resArray->addIntersection(interCap2);
          }
        }
        else if (isConeIntersected==INTERSECT_IN) {
          if (isCapIntersected==FULL_IN) {
            resArray->addIntersection(res1);
            resArray->addIntersection(res2);
          }
          else {
            res1=max(res1,interCap1);
            res2=min(res2,interCap2);
            if (res1<res2) {
              resArray->addIntersection(res1);
              resArray->addIntersection(res2);
            }
          }
        }

      }
    }
  }
}

Vector3 Cone::computeNormal(const Vector3 &p) {
  if (fabs(p.z()-1.0)<0.001) {
    return Vector3(0,0,1);
  }
  else if (fabs(p.z())<0.0001) {
    return Vector3(0,0,-1);
  }
  else
    return Vector3(p.x(),p.y(),-p.z());
}


void Cone::drawGL() {
    materialGL();
    p3d::modelviewMatrix.push();
    p3d::shaderLightPhong();
    p3d::drawCone();
    p3d::modelviewMatrix.pop();
}

