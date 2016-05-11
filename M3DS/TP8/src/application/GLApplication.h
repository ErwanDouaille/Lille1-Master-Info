/**
*
*  @author F. Aubert
*  @file GLView
*
*/



#ifndef GLAPPLICATION_H
#define GLAPPLICATION_H

#include "GLWidget.h"

#include "Camera.h"
#include "Texture.h"

#include "Particle.h"
#include "ParticleList.h"
#include "Engine.h"



/**
  @class GLView

  The widget to draw with OpenGL.
  */
class GLApplication : public GLWidget {

  Q_OBJECT
public:
  //! Ctor
  GLApplication();
  //! Dtor
  virtual ~GLApplication();

  /** ***** **/
  /** GLApplication must implement these methods : */
  virtual void initialize();
  virtual void update();
  virtual void draw();
  virtual void resize(int width,int height);
  /** ***** **/

  virtual void leftPanel(int i, const std::string &s);
  QSize sizeHint() const {return QSize(500,500);}


  void drawGround();
  void updateCamera();

  // ****************************
  // TP :

private:

  p3d::Camera _camera;
  double _frustum;


  // ****************************
  // TP :

  Particle _particle;
  ParticleList _particleList;
  Engine _engine;

  bool _modeParticle;
  bool _animate;

  p3d::Texture _particleTexture,_groundTexture;
  p3d::Line _windRay;



signals:

public slots:

};



#endif // GLAPPLICATION_H


