/**

  @author F. Aubert
  **/


#include "GLApplication.h"
#include "GLTool.h"
#include <iostream>

#include "Hermite.h"
#include "Curve.h"
#include "CatmullRomCurve.h"


using namespace std;
using namespace p3d;


/// ctor
GLApplication::GLApplication() {
  _leftPanelMenu << "Hermite" << "Bezier" << "Bezier Interpolation" << "Catmull-Rom" << "Roller" << "" << "Mouse Camera" << "Mouse Interact";
  _leftPanelMenu << "" << "Switch Control Points" << "Switch TBN" << "Camera on curve";

  _frustum=1;
  _camera.frustum(-_frustum,_frustum,-_frustum,_frustum,_frustum,1000);
  _camera.position(0,2,5);
  _camera.lookAt(Vector3(0,0,0));

  // ************************
  _modeDraw=Draw_Catmull;
  _modeMouse=Mouse_Nothing;
  _viewControlPoint=0;

  _bezier.camera(&_camera);
  _catmullRom.camera(&_camera);
  _hermite.camera(&_camera);
  _roller.camera(&_camera);

  _whichInteraction=&_catmullRom;
  _hermiteInput=0;

  p3d::lightPosition[0]=Vector4(0,0,10);

  _tAnimation=0;
  _animateTBN=false;
  _followRoller=false;

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
  if (_modeMouse==Mouse_Camera) updateCamera();
  else {
    if (_whichInteraction) {
      Vector2 mouse(mouseX(),mouseY());
      if (mouseLeftPressed()) {
        _whichInteraction->selectNearest(mouse,10);
        _modeMouse=Mouse_Interact;
      }
      if (mouseLeft()) {
        if (_modeMouse==Mouse_Interact) {
          _whichInteraction->moveSelected(mouse);
        }
      }
      if (mouseRightPressed()) {
        _whichInteraction->addEnd(mouse);
      }

    }
  }
  if (_animateTBN) {
    _tAnimation+=0.001;
    if (_tAnimation>1) _tAnimation=0;
  }

}

void GLApplication::leftPanel(int i,const std::string &s) {
  cout << "GLApplication : button clicked " << i << " " << s << endl;
  switch(i) {
    case 0:_modeDraw=Draw_Hermite;_whichInteraction=&_hermite;break;
    case 1:_modeDraw=Draw_Bezier;_whichInteraction=&_bezier;break;
    case 2:_modeDraw=Draw_Catmull;_whichInteraction=&_catmullRom;break;
    case 3:_modeDraw=Draw_Catmull;_whichInteraction=&_catmullRom;_catmullRom.setup();break;
    case 4:_modeDraw=Draw_Roller;_whichInteraction=&_roller;_modeMouse=Mouse_Camera;break;
    case 6:_modeMouse=Mouse_Camera;break;
    case 7:_modeMouse=Mouse_Interact;break;
    case 9:_viewControlPoint=(_viewControlPoint+1)%2;break;
    case 10:_animateTBN=!_animateTBN;break;
    case 11:_modeDraw=Draw_Roller;_whichInteraction=&_roller;_modeMouse=Mouse_Camera;_followRoller=!_followRoller;_animateTBN=true;break;
  }
  if (i<4) {
    _camera.position(0,2,5);
    _camera.lookAt(Vector3(0,0,0));
    _modeMouse=Mouse_Interact;
  }
  else if (i==4) {
    _camera.position(40,15,20);
    _camera.lookAt(Vector3(0,0,0));
    _modeMouse=Mouse_Camera;
  }

  if ((i==10 || i==11) && _animateTBN) {
    if (_modeDraw==Draw_Roller) _roller.curve()->initAcceleration();
  }


}

void GLApplication::updateCamera() {
  if (_followRoller) {
    Matrix4 frame=_roller.frame(_tAnimation);

    // Complétez :
    _camera.position(frame.column(3).xyz());
    _camera.orientation(
                frame.column(1).xyz(),
                frame.column(2).xyz(),
                frame.column(0).xyz());
  }
  else {
    if (mouseLeft()) {
      _camera.rotate(-deltaMouseX(),Vector3(0,1,0),Coordinate_World);
      _camera.rotate(deltaMouseY(),Vector3(1,0,0),Coordinate_Local);
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



void GLApplication::draw() {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  p3d::apply(_camera);

  printMode();


  switch (_modeDraw) {
    case Draw_Bezier:
      drawBezier();
      break;
    case Draw_Catmull:
      drawCatmullRom();
      break;
    case Draw_Hermite:
      drawHermite();
      break;
    case Draw_Roller:
      drawRoller();
      break;
    default:
      break;
  }

  if (_animateTBN) {

    if (_modeDraw==Draw_Catmull) {
      _catmullRom.drawTBN(_tAnimation);
    }
    if (_modeDraw==Draw_Roller) {
      _roller.drawFrame(_tAnimation);
    }
  }


  glUseProgram(0);

  snapshot(); // capture opengl window if requested

}






/** ******************************************************************* **/
/**
  Drawings
**/

void GLApplication::drawBezier() {
  p3d::shaderVertexAmbient();
  glLineWidth(3);
  if (_viewControlPoint==0) {
    p3d::uniformAmbient(Vector4(1,0,0));
    _bezier.drawControl();
  }
  p3d::uniformAmbient(Vector4(0,1,0.3));
  _bezier.drawBezier();
  glLineWidth(1);
}


void GLApplication::drawCatmullRom() {
  p3d::shaderVertexAmbient();
  if (_viewControlPoint==0) {
    p3d::uniformAmbient(1,0,0);
    _catmullRom.drawControl(3-_viewControlPoint);
  }
  p3d::uniformAmbient(0,1,0.3);
  _catmullRom.draw();
}

void GLApplication::drawHermite() {
  _hermite.drawControl();
  if (_hermite.nbInput()==4) {
    p3d::uniformAmbient(1,0,0);
    glLineWidth(3);
    _hermite.draw();
    glLineWidth(1);
  }
}

void GLApplication::drawRoller() {
  drawGround();
  _roller.draw();
}

void GLApplication::printMode() {
  switch (_modeDraw) {
  case Draw_Bezier:
    p3d::draw("Bezier",Vector2(-1,-1));
    break;
  case Draw_Catmull:
    p3d::draw("Catmull Rom",Vector2(-1,-1));
    break;
  case Draw_Hermite:
    p3d::draw("Hermite",Vector2(-1,-1));
    break;
  case Draw_Roller:
    p3d::draw("Roller",Vector2(-1,-1));
    break;
  }
}







