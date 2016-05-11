#ifndef RAY_H
#define RAY_H

#include "Vector3.h"
#include "Line.h"
#include "Matrix4.h"

class Ray : public p3d::Line {
  public:
    Ray();
    virtual ~Ray();

    Ray(const p3d::Vector3 &p,const p3d::Vector3 &u);
    Ray(const Ray &ray);

    void transform(const p3d::Matrix4 &m);

    void inside(bool a) {_inside=a;}
    bool inside() const {return _inside;}

  protected:
  private:
    bool _inside;
};

#endif // RAY_H

