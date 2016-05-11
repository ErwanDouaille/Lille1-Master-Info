/**

  @author F. Aubert
  **/


#include "GLApplication.h"
#include "GLTool.h"
#include <iostream>


using namespace std;
using namespace p3d;


/// ctor
GLApplication::GLApplication() {
  _leftPanelMenu << "Start raytrace" << "Next scene";

  _cameraGL.frustum(-_frustum,_frustum,-_frustum,_frustum,0.3,1000);
  _cameraGL.position(0,2,100);
  _cameraGL.lookAt(Vector3(0,0,0));

  _cameraRaytrace.ortho(-1,1,-1,1,-1,1);

  _sceneNumber=0;

  loadScene(_sceneNumber);




}

/// dtor
///
GLApplication::~GLApplication() {

}


void GLApplication::initialize() {
  // appelée 1 seule fois à l'initialisation du contexte
  // => initialisations OpenGL

  glClearColor(1,1,1,1);

  glLineWidth(4);
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

  glEnable(GL_DEPTH_TEST);
  glDepthFunc(GL_LESS);
  glClearDepth(1);

  initGLTool();
  glPointSize(4.0);

  glPixelStorei(GL_UNPACK_ALIGNMENT,1);
  glPixelStorei(GL_PACK_ALIGNMENT,1);

  _raytraceTexture.set(_raytrace.image()->width(),_raytrace.image()->height(),GL_RGBA);


}



/** ***************************************************************************
resize
  **/


void GLApplication::resize(int width,int height) {
  // appelée à chaque dimensionnement du widget OpenGL
  // (inclus l'ouverture de la fenêtre)
  // => réglages liés à la taille de la fenêtre
  _cameraGL.viewport(0,0,width/2,height);
  _cameraRaytrace.viewport(width/2,0,width/2,height);
}





void GLApplication::update() {
  updateCamera();

  if (keyPressed(Qt::Key_Space)) {
    restartRaytrace();
  }
}

void GLApplication::leftPanel(int i,const std::string &s) {
  cout << "GLApplication : button clicked " << i << " " << s << endl;
  switch(i) {
  case 0:
    restartRaytrace();
    break;
  case 1:
    _raytrace.close();
    _sceneNumber=(_sceneNumber+1)%4;
    switch (_sceneNumber) {
    case 0:_cameraGL.position(0,2,100);break;
    case 1:_cameraGL.position(80,40,80);break;
    case 2:_cameraGL.position(350,60,350);break;
    }

    _cameraGL.lookAt(Vector3(0,0,0));

    loadScene(_sceneNumber);
    break;
  }

}

void GLApplication::updateCamera() {
  if (mouseLeft()) {
    Vector3 center=_cameraGL.pointTo(Coordinate_Local,Vector3(0,0,0));
    Vector3 vertical=Vector3(0,1,0);
    _cameraGL.translate(center,Coordinate_Local);
    _cameraGL.rotate(-deltaMouseX()/2.0,vertical,Coordinate_World);
    _cameraGL.rotate(deltaMouseY()/2.0,Vector3(1,0,0),Coordinate_Local);
    _cameraGL.translate(-center,Coordinate_Local);
  }
  if (left()) _cameraGL.left(1);
  if (right()) _cameraGL.right(1);
  if (forward()) _cameraGL.forward(1);
  if (backward()) _cameraGL.backward(1);
  if (accelerateWheel()) {
    _frustum*=1.05;
    _cameraGL.frustum(-_frustum,_frustum,-_frustum,_frustum,0.3,1000);
  }
  if (decelerateWheel()) {
    _frustum/=1.05;
    _cameraGL.frustum(-_frustum,_frustum,-_frustum,_frustum,0.3,1000);
  }
}




void GLApplication::draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  p3d::apply(_cameraGL);

  _scene.camera(_cameraGL); // TODO : encapsulate into _scene
  _scene.drawGL();
  p3d::ambientColor=Vector4(1,0,0,0);
  p3d::draw(_scene.name(),Vector2(-1,-1));

  p3d::apply(_cameraRaytrace);
  _raytraceTexture.set(_raytrace.image()->mirrored());


  _raytraceTexture.bind(0);


  p3d::ambientColor=Vector4(1,1,1,1);
  p3d::shaderTextureAmbient();
  p3d::drawSquare();

  glUseProgram(0);

  snapshot();

}

/** ********************************************************************************
 *
 *
**/

void GLApplication::loadScene(unsigned int number) {
    string name="scene";
    ostringstream nb;
    nb << number;
    name+=nb.str();

    name+=".csg";
    cout << "Loading csg : " << "\"" << name << "\"" << endl;
    _scene.read(name);
    _scene.prepareCsg();
    _scene.camera(_cameraGL);
    _raytrace.scene(&_scene);
    _raytrace.start();
}

void GLApplication::restartRaytrace() {
  _raytrace.close();
  _scene.camera(_cameraGL);
  _raytrace.start();

}

