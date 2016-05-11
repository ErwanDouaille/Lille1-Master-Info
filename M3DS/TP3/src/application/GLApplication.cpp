#include "GLApplication.h"
#include "GLTool.h"

#include "Car.h"

#include <iostream>

using namespace std;
using namespace p3d;

GLApplication::~GLApplication() {
}

GLApplication::GLApplication() {

  _leftPanelMenu << "Car Setup" << "Follow Plane" << "Follow Car";

  _airplane.read("cessna.obj");
  _airplane.position(0,10,-10);

  _camera.frustum(-0.01,0.01,-0.01,0.01,0.01,1000);
  _camera.position(Vector3(0,1,10));
  _cameraMode = Camera_Car_Setup;

}




/** ********************************************************************** **/
void GLApplication::initialize() {
  // appelée 1 seule fois à l'initialisation du contexte
  // => initialisations OpenGL

  glClearColor(1,1,1,1);

//  glLineWidth(4);
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

  glEnable(GL_DEPTH_TEST);
  glDepthFunc(GL_LESS);
  glClearDepth(1);

  initGLTool();

  p3d::diffuseBackColor=Vector3(0,1,0);

  _ground.readMipmap("mosaic_pierre.jpg");


  _airplane.initDraw();

}

void GLApplication::resize(int width,int height) {
  // appelée à chaque dimensionnement du widget OpenGL
  // (inclus l'ouverture de la fenêtre)
  // => réglages liés à la taille de la fenêtre
  glViewport(0,0,width,height);
  _camera.viewport(0,0,width,height);
}

void GLApplication::update() {
  // appelée toutes les 20ms (60Hz)
  // => mettre à jour les données de l'application
  // avant l'affichage de la prochaine image (animation)
  // ...


  if (keyPressed(Qt::Key_F)) _cameraMode=Camera_Car_Setup;
  if (keyPressed(Qt::Key_G)) _cameraMode=Camera_Follow_Plane;
  if (keyPressed(Qt::Key_H)) _cameraMode=Camera_Follow_Car;

  _car.decelerate();
  if (_cameraMode==Camera_Follow_Car || _cameraMode==Camera_Car_Setup) {
    if (left()) {_car.turnLeft();}
    if (right()) {_car.turnRight();}
    if (forward()) {_car.accelerate();}
    if (backward()) {_car.brake();}

  }
  else {
    if (left()) _airplane.rollLeft();
    if (right()) _airplane.rollRight();
    if (forward()) _airplane.pitchDown();
    if (backward()) _airplane.pitchUp();
    if (rotateLeft()) _airplane.yawLeft();
    if (rotateRight()) _airplane.yawRight();
    if (accelerateWheel()) _airplane.accelerate();
    if (decelerateWheel()) _airplane.decelerate();
  }



  _car.move();
  _airplane.move();
  updateCamera();

}

void GLApplication::draw() {
  // appelée après chaque update
  // => tracer toute l'image
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  p3d::apply(_camera);

  _car.drawWorld();

  p3d::diffuseColor=Vector3(1,0,0);
  _airplane.drawWorld();
  drawGround();

  glUseProgram(0);


  snapshot(); // capture opengl window if requested
}

/** ********************************************************************** **/
/** i = button number, s = button text
 */
void GLApplication::leftPanel(int i,const std::string &s) {
  cout << "GLApplication : button clicked " << i << " " << s << endl;

  switch (i) {
  case 0:_cameraMode=Camera_Car_Setup;
      break;
  case 1:_cameraMode=Camera_Follow_Plane;
      break;
  case 2:_cameraMode=Camera_Follow_Car;
      break;
  }
  _cameraStart=_camera;
  _lambda=0;
}

/** *********************************************************** **/

void GLApplication::drawGround() {
  p3d::modelviewMatrix.push();
  p3d::textureMatrix.push();
  p3d::textureMatrix.scale(135,135,135);
  p3d::modelviewMatrix.translate(0,-3,0);
  p3d::modelviewMatrix.scale(10,10,10);
  p3d::drawGround(_ground);
  p3d::textureMatrix.pop();
  p3d::modelviewMatrix.pop();
}

void GLApplication::updateCamera() {
  _lambda+=0.02;
  if (_lambda>1) _lambda=1;


  switch (_cameraMode) {
    case Camera_Car_Setup:
      if (mouseLeft()) {
        _camera.lookAt(_car.position());
        Vector3 t=_cameraStop.pointTo(Coordinate_Local,_car.position());
        Vector3 vertical=_cameraStop.directionTo(Coordinate_Local,Vector3(0,1,0));
        _camera.translate(t,Coordinate_Local);
        _camera.rotate(-deltaMouseX(),vertical,Coordinate_Local);
        _camera.rotate(deltaMouseY(),Vector3(1,0,0),Coordinate_Local);
        _camera.translate(-t,Coordinate_Local);
      }
      break;
    case Camera_Follow_Car: {
      _cameraStop.position(_car.position());
      _cameraStop.orientation(_car.orientation());

//        _camera.translate(0,0,-3);

      _cameraStop.position(_cameraStop.position()+_cameraStop.orientation()*Vector3(0,8,10));
      _cameraStop.rotate(-30,Vector3(1,0,0));
        _camera.position(_cameraStart.position()+(_cameraStop.position()-_cameraStart.position())*_lambda);
        _camera.orientation(_cameraStart.orientation()+(_cameraStop.orientation()+(-1)*_cameraStart.orientation())*_lambda);

      }
      break;
    case Camera_Follow_Plane: {

        _cameraStop.position(_airplane.position());
        _cameraStop.orientation(_airplane.orientation());

//        _camera.translate(0,0,-3);

        _cameraStop.position(_cameraStop.position()+_cameraStop.orientation()*Vector3(0,0,-4));
         _cameraStop.rotate(180, Vector3(0,1,0));
         _camera.position(_cameraStart.position()+(_cameraStop.position()-_cameraStart.position())*_lambda);
          _camera.orientation(_cameraStart.orientation()+(_cameraStop.orientation()+(-1)*_cameraStart.orientation())*_lambda);

      }
      break;
    case Camera_Switch:
      break;
    default:break;

  }
}


