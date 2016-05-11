/**

  @author F. Aubert
  **/


#include "GLApplication.h"
#include "GLTool.h"
#include "DebugTool.h"
#include <iostream>


using namespace std;
using namespace p3d;


/// ctor
GLApplication::GLApplication() {
//  _leftPanelMenu << "";


  _camera.ortho(-10,10,-10,10,-10,10);
  _camera.position(Vector3(0,0,-2));


  // ajout des plans de collisions (les bords de l'environnement)
  _engineBox.add(new Plane(Vector3(0,-9,0),Vector3(0,1,0)));
  _engineBox.add(new Plane(Vector3(-9,0,0),Vector3(1,0,0)));
  _engineBox.add(new Plane(Vector3(9,0,0),Vector3(-1,0,0)));
  _engineBox.add(new Plane(Vector3(0,9,0),Vector3(0,-1,0)));


  // Définition première boite
  Box *b=new Box();
  b->dimension(3,2);
  b->position(Vector3(-2,5,0));
  b->theta(-0.3);
  b->mass(1);
  b->computeInertia();
  b->color(Vector3(1,0,1));
  _boxList.add(b);

  // Définition seconde boite
  b=new Box();
  b->dimension(10,2);
  b->position(Vector3(3,0,0));
  b->theta(0.5);
  b->mass(1);
  b->computeInertia();
  b->color(Vector3(0,1,0));
  _boxList.add(b);


  _engineBox.boxList(&_boxList);

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


  glPixelStorei(GL_UNPACK_ALIGNMENT,4);
  glPixelStorei(GL_PACK_ALIGNMENT,4);



  Texture *tex1=new Texture();
  tex1->read("Tyrol.jpg");
  tex1->filterLinear();
  Texture *tex2=new Texture();
  tex2->read("Tranquil_Lagoon.jpg");
  tex2->filterLinear();

  _boxList[0]->texture(tex1);
  _boxList[1]->texture(tex2);


}



/** ***************************************************************************
resize
  **/


void GLApplication::resize(int width,int height) {
  // appelée à chaque dimensionnement du widget OpenGL
  // (inclus l'ouverture de la fenêtre)
  // => réglages liés à la taille de la fenêtre
  _camera.viewport(0,0,width,height);
}





void GLApplication::update() {
//  updateCamera();


  _engineBox.update();

  if (mouseLeftPressed()) {
    _applyForce=true;
    _pointForce=_camera.windowToWorld(mouseX(),mouseY());
    _pointForce.z(0);
    _engineBox.activeCursor(_pointForce);
  }
  if (mouseLeft()) {
    _pointForce=_camera.windowToWorld(mouseX(),mouseY());
    _pointForce.z(0);
    _engineBox.motionCursor(_pointForce);
  }
  else {
    _applyForce=false;
    _engineBox.disableCursor();
  }


}

void GLApplication::leftPanel(int i,const std::string &s) {
  cout << "GLApplication : button clicked " << i << " " << s << endl;
  switch(i) {
  }

}




void GLApplication::draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  p3d::apply(_camera);


  _engineBox.draw();

  _engineBox.drawPlane2D();

  glDepthFunc(GL_ALWAYS);
  if (_engineBox.isCursorActive()) {
    p3d::ambientColor=Vector4(0,0,1,0);
    p3d::drawThickLineStrip({_pointForce,(_boxList.selected())->attachWorld()});
  }

  p3d::drawDebug();
  p3d::clearDebug();
  glDepthFunc(GL_LESS);


  glUseProgram(0);

  snapshot(); // capture opengl window if requested

}

/** ********************************************************************************
 *
 *
**/








