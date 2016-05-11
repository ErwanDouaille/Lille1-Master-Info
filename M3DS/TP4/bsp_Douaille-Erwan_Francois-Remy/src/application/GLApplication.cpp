/**

  @author F. Aubert
  **/


#include "GLApplication.h"
#include "GLTool.h"
#include <iostream>

using namespace std;
using namespace p3d;

static double myRand(double binf,double bsup) {
  return double(rand())/RAND_MAX*(bsup-binf)+binf;
}

/// ctor
GLApplication::GLApplication() {
  _leftPanelMenu << "Face Side" << "Segment Intersection" << "Face cutting" << "BSP Visualisation" << "Build BSP" << "Transparency";

  _camera.frustum(-0.01,0.01,-0.01,0.01,_frustum,1000);
  _camera.position(0,2,5);
  _camera.lookAt(Vector3(0,0,0));

  _nbSolution=3;
  _zCam=6.0;
  _modeDisplay=1;
  _solution=0;

  srand(time(NULL));

  // pour test signe et intersection
  _bsp1.read("une_face.obj");

  // pour test de découpe sur 2 faces
  _bsp2.read("deux_face.obj");
  _bsp2.face(0)->separe(*_bsp2.face(1));

  // pour test simple de découpe

  // TODO : à changer

  _bsp3.read("wolf.obj");

}

/// dtor
///
GLApplication::~GLApplication() {

}


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
  glPointSize(4.0);


  _ground.readMipmap("mosaic_pierre.jpg");

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
  updateCamera();

}

void GLApplication::leftPanel(int i,const std::string &s) {
//  cout << "GLApplication : button clicked " << i << " " << s << endl;

  switch (i) {
  case 0:case 1:case 2:
    _modeDisplay=i+1;
    break;
  case 3:
    _modeDisplay=4;
    if (_bsp3.bsp()) _solution=(_solution+1)%_nbSolution; else _solution=0;
    break;
  case 4:
    if (!_bsp3.bsp()) {
      cout << "BSP construction" << endl;
      cout << "Number of faces = " << _bsp3.nbFace() << endl;
      _bsp3.createBSP();
      cout << "After creation : number of faces = " << _bsp3.nbFace() << endl;
    }
    break;
  case 5:
    _withBlend=!_withBlend;
    break;
  default:break;
  }

}





/** ******************************************************************* **/
/**
  Drawings
**/

void GLApplication::display1() {

  // Test du signe (génération brutale à chaque passage => uniquement pour tester).

  _bsp1.drawDepth();
  srand(1);
  p3d::drawText("Face sign...",-1,-1);
  p3d::shaderVertexAmbient();
  vector<Vector3> pts;
  vector<Vector3> color;
  pts.clear();color.clear();
  for(int i=0; i<200; i++) {
    Vector3 p;
    p.x(myRand(-1.0,1.0));
    p.y(myRand(-1.0,1.0));
    p.z(myRand(-1.0,1.0));
    pts.push_back(p);
    FaceBSP *f=_bsp1.face(0);
    ESign s=f->sign(p);
    switch (s) {
    case SIGN_NONE:
      color.push_back(Vector3(0,0,1));
      break;
    case SIGN_PLUS:
      color.push_back(Vector3(0,1,0));
      break;
    case SIGN_MINUS:
      color.push_back(Vector3(1,0,0));
      break;
    }
  }
  p3d::shaderVertexColor();
  p3d::drawPoints(pts,color);
  p3d::shaderVertexAmbient();
//  _bsp1.drawNormal();

}

void GLApplication::display2() {
  _bsp1.drawDepth();
  srand(1);
  vector<Vector3> ptsLine,ptsPoint;
  p3d::drawText("Segment intersection...",-1,-1);
  for(unsigned int i=0; i<10; ++i) {
    Vector3 p1,p2;
    p1.set(myRand(-2.0,0.0),myRand(-1.0,1.0),myRand(0.0,2.0));
    p2.set(myRand(0.0,2.0),myRand(-1.0,1.0),myRand(-2.0,0.0));
    ptsLine.push_back(p1);
    ptsLine.push_back(p2);
    Vector3 p=_bsp1.face(0)->intersection(p1,p2);
    ptsPoint.push_back(p);
  }
  p3d::shaderVertexAmbient();
  p3d::uniformAmbient(0,0.2,0.8);
  p3d::drawLines(ptsLine);
  p3d::uniformAmbient(0.6,0.7,1);
  p3d::drawPoints(ptsPoint);
}

void GLApplication::display3() {
  p3d::drawText("Face cutting",-1,-1);
  _bsp2.drawDepth();
}


void GLApplication::display4() {
  if (_bsp3.bsp()==0) {
    p3d::uniformAmbient(1,0,0);
    p3d::drawText("Press \"Build BSP\" button...",-0.5,0);
  }
  else {
  switch (_solution) {
  case 0:
    p3d::drawText("Render BSP without hidden removal",-1,-1);
    _bsp3.drawBrut();
    break;
  case 1:
    p3d::drawText("Render BSP with depth buffer",-1,-1);
    p3d::uniformAmbient(1,0,0);
    if (_withBlend) {
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    }
    _bsp3.drawDepth();
    glDisable(GL_BLEND);
    break;
  case 2:
    p3d::drawText("Render BSP with painter algorithm",-1,-1);
    p3d::uniformAmbient(0,0.8,0.2);
    if (_withBlend) {
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    }
    _bsp3.drawBSP(_obs);
    glDisable(GL_BLEND);
    break;
  }
  }
}



void GLApplication::draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  p3d::apply(_camera);

  _obs=_camera.pointTo(Coordinate_World,Vector3(0,0,0));


  drawGround();

  switch (_modeDisplay) {
  case 1:

    display1();
    break;
  case 2:
    display2();
    break;
  case 3:
    display3();
    break;
  case 4:
    display4();
    break;
  }

  glUseProgram(0);

  snapshot(); // capture opengl window if requested

}


void GLApplication::updateCamera() {
  if (mouseLeft()) {
//    _camera.lookAt(Vector3(0,0,0));
    Vector3 t=_camera.pointTo(Coordinate_Local,Vector3(0,0,0));
    Vector3 vertical=_camera.directionTo(Coordinate_Local,Vector3(0,1,0));
    _camera.translate(t,Coordinate_Local);
    _camera.rotate(-deltaMouseX(),vertical,Coordinate_Local);
    _camera.rotate(deltaMouseY(),Vector3(1,0,0),Coordinate_Local);
    _camera.translate(-t,Coordinate_Local);
  }
  if (left()) _camera.left(0.1);
  if (right()) _camera.right(0.1);
  if (forward()) _camera.forward(0.1);
  if (backward()) _camera.backward(0.1);
  if (accelerateWheel()) {
    _frustum*=1.05;
    _camera.frustum(-0.01,0.01,-0.01,0.01,_frustum,1000);
  }
  if (decelerateWheel()) {
    _frustum/=1.05;
    _camera.frustum(-0.01,0.01,-0.01,0.01,_frustum,1000);
  }
}

void GLApplication::drawGround() {
  p3d::modelviewMatrix.push();
  p3d::textureMatrix.push();
  p3d::textureMatrix.scale(135,135,135);
  p3d::modelviewMatrix.translate(0,-3,0);
  p3d::modelviewMatrix.scale(10,10,10);
  p3d::drawGround(_ground,true);
  p3d::textureMatrix.pop();
  p3d::modelviewMatrix.pop();
}




