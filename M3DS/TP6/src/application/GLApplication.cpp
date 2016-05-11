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
  _leftPanelMenu << "PerVertex Lighting" << "PerPixel Lighting" << "Texture Transform" << "Shadow map" << "Shadow+Light" << ""
                 << "Animate" << "" << "Depth Texture visu. (switch)";


  _frustum=0.01;
  _camera.frustum(-_frustum,_frustum,-_frustum,_frustum,0.03,1000);
  _camera.position(Vector3(15,9,10));
  _camera.lookAt(Vector3(0,0,0),Vector3(0,1,0));



  _lightMatrix.setIdentity();
  _lightMatrix.translate(Vector3(0,5,0));
  _lightMatrix.rotate(20,Vector3(0,1,0));
  _lightMatrix.translate(Vector3(0,0,4));
  _lightMatrix.rotate(-30,Vector3(1,0,0));


  _car.readInit("porsche.obj");
  _car.translate(0,-1.8,-2);
  _car.rotate(30,0,1,0);
  _car.scale(4,4,4);

  _airplane.readInit("cessna.obj");
  _airplane.translate(0,3,1);
  _airplane.scale(1.5);

  _al.readInit("al.obj");
  _al.translate(2,-1,1);
  _al.rotate(45,0,1,0);
  _al.scale(2,2,2);

  _moveAngle=0;
  _angleStep=0.1;
  _moveZ=0;
  _zStep=0.1;
  _planeAngle=0;

  _animate=false;

  _drawDepthTexture=false;

  _textureEyeMatrix.setIdentity();

  _orbitalCamera=true;

}

/// dtor
///

GLApplication::~GLApplication() {

}


void GLApplication::initialize() {

  // appelée 1 seule fois à l'initialisation du contexte
  // => initialisations OpenGL

  glClearColor(1,1,1,1);

//  glLineWidth(4); // useless from spec
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

  glEnable(GL_DEPTH_TEST);
  glDepthFunc(GL_LESS);
  glClearDepth(1);

  initGLTool();
  glPointSize(4.0);


  glPixelStorei(GL_UNPACK_ALIGNMENT,4);
  glPixelStorei(GL_PACK_ALIGNMENT,4);


  p3d::initSphere(12,12);
  _earthMatrix.setIdentity();
  _earthMatrix.translate(-3,1,-3);
  _earthMatrix.rotate(-73,1,0,0);
  _earthMatrix.scale(1.5);

  _groundTexture.read("mosaic_pierre.jpg");
  _groundTexture.generateMipmap();


  _earthTexture.read("EarthDay512.jpg");
  _earthTexture.generateMipmap();

  _univLille1.read("univLille1.png");

  _univLille1.generateMipmap();
  _univLille1.bind(1);
  _univLille1.wrap(GL_CLAMP_TO_BORDER);

  _al.initDraw();
  _car.initDraw();
  _airplane.initDraw();


  _ambientTexture.attribute("position",0);
  _ambientTexture.attribute("texCoord",2);
  _perPixelLighting.attribute("position",0);
  _perPixelLighting.attribute("normal",1);
  _perVertexLighting.attribute("position",0);
  _perVertexLighting.attribute("normal",1);
  _shadowMap.attribute("position",0);
  _textureTransform.attribute("position",0);



  _ambientTexture.read("shader/ambientTexture");
  _perPixelLighting.read("shader/perPixelLighting");
  _perVertexLighting.read("shader/perVertexLighting");
  _shadowMap.read("shader/shadowMap");
  _textureTransform.read("shader/textureTransform");


  _firstPassShader=&_perVertexLighting;

  _rtt.create(256,256);       // création d'un Frame Buffer de 256x256 pixels

  _rtt.rtt(0,&_depthTexture); // Color Buffer = _depthTexture, Depth Buffer = 0 => généré lors de l'initialisation
                              // le Color Buffer et le Depth Buffer auront les dimensions du Frame Buffer

  _depthTexture.wrap(GL_CLAMP_TO_BORDER);
  _depthTexture.filterLinear();
  _depthTexture.bind(2);      // l'unité de texture 2 correspondra à _depthTexture
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

  _textureEyeMatrix.setIdentity();
  _textureEyeMatrix =  Matrix4::fromFrustum(-1,1,-1,1,1,100)*_lightMatrix.inverse()*_camera.worldLocal();


  if (mouseRight()) {
    Vector3 vertical=_lightMatrix.inverse().transformDirection(Vector3(0,1,0));
    _lightMatrix.mul(Matrix4::fromAngleAxis(deltaMouseX()*2,vertical));
    _lightMatrix.mul(Matrix4::fromAngleAxis(deltaMouseY()*2,Vector3(1,0,0)));
  }

  if (_animate) {
    _moveAngle+=fabs(_angleStep)*3.0;
    _moveZ+=_zStep;
    _planeAngle+=_angleStep;
    if (_moveZ>4.0 || _moveZ<-4.0) {
      _zStep=-_zStep;
      _angleStep=-_angleStep;
    }
    _car.translate(_zStep/2.0,0,0,Coordinate_Local);
    _airplane.translate(0,0,_zStep/3);
    _airplane.rotate(-_angleStep*10,1,0,0);
    _earthMatrix.rotate(-_angleStep*10,0,0,1);
  }

}

void GLApplication::leftPanel(int i,const std::string &s) {
  cout << "GLApplication : button clicked " << i << " " << s << endl;
  switch(i) {
  case 0:_firstPassShader=&_perVertexLighting;_secondPassShader=0;break;
  case 1:_firstPassShader=&_perPixelLighting;_secondPassShader=0;break;
  case 2:_firstPassShader=&_textureTransform;_secondPassShader=0;break;
  case 3:_firstPassShader=&_shadowMap;_secondPassShader=0;break;
  case 4:_firstPassShader=&_perPixelLighting;_secondPassShader=&_shadowMap;break;
  case 6:_animate=!_animate;break;
  case 8:_drawDepthTexture=!_drawDepthTexture;break;
  }

}

void GLApplication::updateCamera() {
  if (mouseLeft()) {
    Vector3 center=_camera.pointTo(Coordinate_Local,Vector3(0,0,0));
    Vector3 vertical=Vector3(0,1,0);
    if (_orbitalCamera) {
      _camera.translate(center,Coordinate_Local);
      _camera.rotate(-deltaMouseX()/2.0,vertical,Coordinate_World);
      _camera.rotate(deltaMouseY()/2.0,Vector3(1,0,0),Coordinate_Local);
      _camera.translate(-center,Coordinate_Local);
    }
    else {
      _camera.rotate(-deltaMouseX()/2.0,vertical,Coordinate_World);
      _camera.rotate(deltaMouseY()/2.0,Vector3(1,0,0),Coordinate_Local);
    }
  }
  if (left()) _camera.left(0.3);
  if (right()) _camera.right(0.3);
  if (forward()) _camera.forward(0.3);
  if (backward()) _camera.backward(0.3);
  if (accelerateWheel()) {
    _frustum*=1.05;
    _camera.frustum(-_frustum,_frustum,-_frustum,_frustum,0.03,1000);
  }
  if (decelerateWheel()) {
    _frustum/=1.05;
    _camera.frustum(-_frustum,_frustum,-_frustum,_frustum,0.03,1000);
  }
  if (keyPressed(Qt::Key_Space)) {
    _orbitalCamera=!_orbitalCamera;
  }
}



void GLApplication::renderToTexture() {
  _rtt.begin(); // activation : tous les tracés qui suivent seront faits directement dans _depthTexture (en tant que Color Buffer).

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  glViewport(0,0,_rtt.width(),_rtt.height()); // viewport aux dimensions du frame buffer


  //Matrix4::fromFrustum(-1,1,-1,1,1,100)*_lightMatrix.inverse()*_camera.worldLocal();

  p3d::projectionMatrix=Matrix4::fromFrustum(-1,1,-1,1,1,100);
  p3d::modelviewMatrix=_lightMatrix.inverse();

  _currentShader=&_perVertexLighting; // activer ce shader pour les tracés qui suivent
  lightPosition();
  drawGround();
  drawEarth();
  drawObject();

  _rtt.end(); // retour au frame buffer par défaut "normal"
}


void GLApplication::draw() {

  renderToTexture();

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  p3d::apply(_camera);

  lightPosition();
  if (_firstPassShader) {
    _currentShader=_firstPassShader;
    drawGround();
    drawEarth();
    drawObject();
  }



  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
  glDepthFunc(GL_EQUAL);
  if (_secondPassShader) {
    _currentShader=_secondPassShader;
    drawGround();
    drawEarth();
    drawObject();
  }
  _materialAmbient.a(0.5);
  _currentShader=&_ambientTexture;
  _groundTexture.bind(0);
  drawGround();
  _earthTexture.bind(0);
  drawEarth();
  glDepthFunc(GL_LESS);
  glDisable(GL_BLEND);

  drawLight();

  if (_drawDepthTexture) {
    drawIncrustation();
  }

  glUseProgram(0);

  snapshot(); // capture opengl window if requested

}

/** ********************************************************************************
 *
 *
**/


void GLApplication::drawEarth() {
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix*=_earthMatrix;
  _materialAmbient.rgb(0.1,0.1,0.1);
  _materialDiffuse=Vector3(0.4,0.4,0.4);
  _materialSpecular=Vector3(0.8,0.8,0.8);
  useCurrentShader();
  _currentShader->uniform("repeat",1.0f);
  p3d::drawSphere();
  p3d::modelviewMatrix.pop();
}

void GLApplication::drawGround() {
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix.translate(0,-3,0);
  p3d::modelviewMatrix.rotate(-90,1,0,0);
  p3d::modelviewMatrix.scale(20,20,1);
  _materialAmbient.rgb(0.1,0.1,0.1);
  _materialDiffuse=Vector3(0.5,0.5,0.5);
  _materialSpecular=Vector3(1,1,1);
  useCurrentShader();
  _currentShader->uniform("repeat",5.0f);
  p3d::drawSquare();
  p3d::modelviewMatrix.pop();
}








/** ******************************************************************* **/
/**
  Drawings
**/



void GLApplication::drawObject() {
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix*=_car.worldLocal();
  _materialAmbient.rgb(0.1,0.1,0.1);
  _materialDiffuse=Vector3(0.4,0.7,0.4);
  _materialSpecular=Vector3(0.0,0.4,0.4);
  useCurrentShader();
  _car.draw();
  p3d::modelviewMatrix.pop();

  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix*=_airplane.worldLocal();
  _materialAmbient.rgb(0.1,0.1,0.1);
  _materialDiffuse=Vector3(0.3,0.2,0.9);
  _materialSpecular=Vector3(0.0,0.4,0.4);
  useCurrentShader();
  _airplane.draw();
  p3d::modelviewMatrix.pop();

  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix*=_al.worldLocal();
  _materialAmbient.rgb(0.1,0.1,0.1);
  _materialDiffuse=Vector3(0.3,0.7,0.6);
  _materialSpecular=Vector3(0.0,0.4,0.4);
  useCurrentShader();
  _al.draw();
  p3d::modelviewMatrix.pop();
}






void GLApplication::useCurrentShader() {
  _currentShader->use();
  _currentShader->uniform("mvp",p3d::projectionMatrix*p3d::modelviewMatrix);
  _currentShader->uniform("normalMatrix",p3d::modelviewMatrix.normalMatrix());
  _currentShader->uniform("modelviewMatrix",p3d::modelviewMatrix);
  _currentShader->uniform("lightPosition",_lightPositionEye);
  _currentShader->uniform("materialAmbient",_materialAmbient);
  _currentShader->uniform("materialDiffuse",_materialDiffuse);
  _currentShader->uniform("materialSpecular",_materialSpecular);
  _currentShader->uniform("image0",0);
  _currentShader->uniform("image1",1);
  _currentShader->uniform("depthTexture",2);
  _currentShader->uniform("textureEyeMatrix",_textureEyeMatrix);
}

void GLApplication::useAmbientTextureShader() {
  _ambientTexture.use();
  _ambientTexture.uniform("mvp",p3d::projectionMatrix*p3d::modelviewMatrix);
  _ambientTexture.uniform("materialAmbient",_materialAmbient);
  _ambientTexture.uniform("image",0);
  _groundTexture.bind(0);
}


void GLApplication::drawLight() {
  p3d::material(Vector4(0.1,0.1,0.1,1.0),Vector3(0.1,0.1,0.8),Vector3(0.0,0.4,0.4),40);
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix*=_lightMatrix;
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix.scale(0.2);
  p3d::shaderLightPhong();
  p3d::drawSphere();
  p3d::modelviewMatrix.pop();
  p3d::modelviewMatrix.translate(0,0,-0.1);
  p3d::modelviewMatrix.scale(0.5,0.5,0.1);
  p3d::shaderLightPhong();
  p3d::drawCube();
  p3d::modelviewMatrix.pop();
}

void GLApplication::lightPosition() { // should be called after camera setup in modelview
  _lightPositionEye=(p3d::modelviewMatrix*_lightMatrix).transformPoint(Vector3(0,0,0)); // the shaders require the lightPosition in Eye Coordinates
}

void GLApplication::drawIncrustation() {
  p3d::ambientColor=Vector4(1,1,1,1);

  p3d::drawTexture(_depthTexture,0,0,0.3,0.3,false);

}



