#ifndef INC_VERTEXBSP_H
#define INC_VERTEXBSP_H

#include "Vector3.h"

class ObjectBSP;

class VertexBSP {
  p3d::Vector3 _point;
  p3d::Vector3 _normal;

  ObjectBSP *_owner;

public:
  VertexBSP(ObjectBSP *owner);
  virtual ~VertexBSP();

  inline void point(const p3d::Vector3 &p) {_point=p;}
  inline void normal(const p3d::Vector3 &n) {_normal=n;}

  inline const p3d::Vector3 &point() const {return _point;}
  inline const p3d::Vector3 &normal() const {return _normal;}
  void interpolateNormal(const VertexBSP &v1, const VertexBSP &v2);
};


#endif

