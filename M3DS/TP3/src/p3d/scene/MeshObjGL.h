#ifndef MESHOBJGL_H
#define MESHOBJGL_H

#include <string>
#include "MeshObj.h"
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

class MeshObjGL {
  GLuint _vertexBuffer;
  GLuint _normalBuffer;
  GLuint _texCoordBuffer;
  GLuint _indexBuffer;
  GLuint _vao;
  MeshObj *_mesh;
public:
  MeshObjGL(MeshObj *mesh);
  virtual ~MeshObjGL();
  void draw();
  void initBuffer();
  void drawBuffer();
  GLuint vertexBuffer() {return _vertexBuffer;}
};

}

#endif // MESHOBJGL_H

