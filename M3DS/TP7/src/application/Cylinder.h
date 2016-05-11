#ifndef CYLINDRE_H
#define CYLINDRE_H

class IntersectionArray;

#include "Primitive.h"

class Cylinder:public Primitive {
  typedef enum {FULL_IN, FULL_OUT, INTERSECT_IN} ESpaceIntersect;

    public:
    Cylinder();
    Cylinder(const p3d::Vector4 &a,const p3d::Vector3 &d,const p3d::Vector3 &s);
    virtual p3d::Vector3 computeNormal(const p3d::Vector3 &p);
    virtual void drawGL();
    void intersection(const Ray &ray, IntersectionArray *res);

private:
    Cylinder::ESpaceIntersect intersectCap(double a,double u,double *res1,double *res2);
    void intersectInterval(Cylinder::ESpaceIntersect isIntersected_new, double l1_new, double l2_new, ESpaceIntersect *isIntersected, double *l1, double *l2);

};

#endif // CYLINDRE_H

