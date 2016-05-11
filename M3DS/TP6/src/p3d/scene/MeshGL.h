#ifndef MeshGL_H
#define MeshGL_H

#include <string>
#include "Mesh.h"
#include "glsupport.h"

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/


namespace p3d {

class MeshGL {
  GLuint _allAttribBuffer;
  GLuint _vao;
  Mesh *_mesh;
  unsigned int _nbVertex;
public:
  MeshGL(Mesh *mesh);
  virtual ~MeshGL();
  void draw();
  void initBuffer();
  void drawBuffer();
  void drawNormal(double kLength=1.0);
};

}

#endif // MeshGL_H

