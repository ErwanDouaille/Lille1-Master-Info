#ifndef CUBE_H_INCLUDED
#define CUBE_H_INCLUDED

class IntersectionArray;

#include "Primitive.h"

/**
@file
@author Fabrice Aubert
*/


class Cube: public Primitive {
  typedef enum {FULL_IN, FULL_OUT, INTERSECT} ESpaceIntersect;

public:
  Cube();
  virtual p3d::Vector3 computeNormal(const p3d::Vector3 &p);
  void drawGL();

  void intersection(const Ray &ray, IntersectionArray *res);
private:
  ESpaceIntersect intersectBiPlane(double a,double u,double *res1,double *res2);
  void intersectInterval(ESpaceIntersect isIntersected_new,double l1_new,double l2_new,ESpaceIntersect *isIntersected,double *l1,double *l2);


};

#endif // CUBE_H_INCLUDED

