#ifndef MESHOBJECT3D_H
#define MESHOBJECT3D_H

#include "Mesh.h"
#include "Object3D.h"

namespace p3d {

class MeshObject3D : public Mesh, public Object3D {
public:
  virtual ~MeshObject3D();
  MeshObject3D();
};
}

#endif // MESHOBJECT3D_H

