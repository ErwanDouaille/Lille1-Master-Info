#ifndef COLLISIONINFO_H
#define COLLISIONINFO_H


#include "Vector3.h"
#include "Box.h"

/**
@file
@author F. Aubert
@brief opérations sur OBB pour simu physique (collision+impulsion)

*/

class Box;

class CollisionInfo {
  public:
    CollisionInfo();
    virtual ~CollisionInfo();

    inline Box *box1() const {return _b1;}
    inline Box *box2() const {return _b2;}
    inline const p3d::Vector3 &applicationPoint() {return _applicationPoint;}
    inline const p3d::Vector3 &axis() {return _axis;}
    inline double mtd() {return _mtd;}

    inline void box1(Box *b1) {_b1=b1;}
    inline void box2(Box *b2) {_b2=b2;}

    inline void applicationPoint(const p3d::Vector3 &p) {_applicationPoint=p;}
    inline void axis(const p3d::Vector3 &u) {_axis=u;}
    inline void mtd(double k) {_mtd=k;}


  protected:
  private:
  Box *_b1;
  Box *_b2;
  p3d::Vector3 _applicationPoint;
  p3d::Vector3 _axis;
  double _mtd;
};

#endif // COLLISIONINFO_H

