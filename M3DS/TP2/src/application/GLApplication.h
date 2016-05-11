#ifndef GLAPPLICATION_H
#define GLAPPLICATION_H


#include "GLWidget.h"
#include "Tools.h"
#include "Matrix4.h"
#include "Shader.h"
#include "ObjLoader.h"
#include "BasicMesh.h"

#include <string>

class GLApplication : public GLWidget {
  Q_OBJECT  
public:
  GLApplication();
  virtual ~GLApplication();


  /** ***** **/
  /** GLApplication must implement these methods : */
  virtual void initialize();
  virtual void update();
  virtual void draw();
  virtual void resize(int width,int height);
  /** ***** **/
  virtual void leftPanel(int i, const std::string &s);
  /** ***** **/

private:
  p3d::Shader _shader;

  p3d::ObjLoader _obj;
  BasicMesh _meshGL;
  p3d::Matrix4 _projection;
  p3d::Matrix4 _transform;
  p3d::Vector3 _lightPosition;

  double _angle;



};

#endif // GLAPPLICATION_H

