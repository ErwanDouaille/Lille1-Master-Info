#ifndef SCENEINTERSECTION_H
#define SCENEINTERSECTION_H

#include "Intersection.h"
#include "MeshObject3D.h"

class SceneIntersection {
  p3d::Line _pickingRay;
  std::vector<Intersection *> _result;

public:
  virtual ~SceneIntersection();
  SceneIntersection();

  /// number of intersections
  size_t size() {return _result.size();}
  /// delete all current intersections : should be called before the intersection
  void clean();

  /// intersection with mesh
  void intersect(p3d::MeshObject3D *e);

  /// intersection with all meshes (
  void intersect(const std::vector<p3d::MeshObject3D *> &allObj, const p3d::Line &pickingRay);

  /// returns the i-th intersection
  Intersection *operator[](unsigned int i) const {return _result[i];}
  Intersection *at(unsigned int i) const {return _result[i];}

  /// inserts intersection i in the result (sorted)
  void insert(Intersection *i);

  /// intersect the triangle s0,s1,s2 : ray should be expressed in the same frame
  bool intersect(const p3d::Line &ray, const p3d::Vector3 &s0, const p3d::Vector3 &s1, const p3d::Vector3 &s2, double *lambdaRes);
};


#endif // SCENEINTERSECTION_H

