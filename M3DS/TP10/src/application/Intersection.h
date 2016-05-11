#ifndef INTERSECTION_H
#define INTERSECTION_H

#include <vector>
#include "Line.h"

#include "MeshObject3D.h"

class Intersection {
  p3d::MeshObject3D *_mesh;
  double _lambda;
  p3d::Line _rayWorld;
public:
  virtual ~Intersection();
  Intersection();
  void mesh(p3d::MeshObject3D *e) {_mesh=e;}
  void lambda(double a) {_lambda=a;}
  void rayWorld(const p3d::Line &ray) {_rayWorld=ray;}
  const p3d::Line &rayWorld() {return _rayWorld;}
  double lambda() {return _lambda;}
  p3d::MeshObject3D *mesh() {return _mesh;}
};



#endif // INTERSECTION_H

