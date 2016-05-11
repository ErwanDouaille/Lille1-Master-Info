#ifndef GLAPPLICATION_H
#define GLAPPLICATION_H


#include "GLWidget.h"
#include "Tools.h"
#include "Matrix4.h"
#include "Shader.h"
#include "Camera.h"
#include "Car.h"
#include "Mesh.h"
#include "GLTool.h"
#include "Airplane.h"

#include <string>


class GLApplication : public GLWidget {
  Q_OBJECT  
public:
  typedef enum {Camera_Follow_Car,Camera_Car_Setup,Camera_Follow_Plane,Camera_Switch} ECameraMode;

  virtual ~GLApplication();
  GLApplication();
  /** ***** **/
  /** GLApplication must implement these methods : */
  virtual void initialize();
  virtual void update();
  virtual void draw();
  virtual void resize(int width,int height);
  /** ***** **/
  virtual void leftPanel(int i, const std::string &s);
  /** ***** **/

  void drawGround();
  void updateCamera();


  QSize sizeHint() const {return QSize(512,512);}


private:
  Car _car;
  Airplane _airplane;
  p3d::Camera _camera,_cameraStart,_cameraStop;
  p3d::Texture _ground;
  ECameraMode _cameraMode;

  float _lambda=0.0;

  double _angle=0.0;


};

#endif // GLAPPLICATION_H

