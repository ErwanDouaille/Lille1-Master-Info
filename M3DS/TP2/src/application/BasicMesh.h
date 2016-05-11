#ifndef BASICMESH_H
#define BASICMESH_H

#include "ObjLoader.h"
#include "glsupport.h"

class BasicMesh
{
public:
  virtual ~BasicMesh();
  BasicMesh();


  void initTetrahedron();
  void initObj(const p3d::ObjLoader &mesh);

  void initDraw();
  void draw();

private:
  void initBuffer();
  void updateBuffer();
  void initVAO();

  std::vector<float> _attribute;
  std::vector<unsigned int> _element;

  GLuint _vao;
  GLuint _attributeBuffer;
  GLuint _elementBuffer;
};

#endif // BASICMESH_H

