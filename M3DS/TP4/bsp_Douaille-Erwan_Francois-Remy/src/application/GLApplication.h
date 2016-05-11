/**
*
*  @author F. Aubert
*  @file GLView
*
*/



#ifndef GLAPPLICATION_H
#define GLAPPLICATION_H

#include "GLWidget.h"

#include "ObjectBSP.h"

#include "Camera.h"
#include "Texture.h"


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


  void display1();
  void display2();
  void display3();
  void display4();

  void updateCamera();
  void drawGround();



  // TP :

private:
  double _zCam;

  unsigned int _modeDisplay,_solution,_nbSolution;

  ObjectBSP _bsp1,_bsp2,_bsp3;
  p3d::Vector3 _obs;

  p3d::Texture _ground;

  p3d::Camera _camera; // contrôle du placement de la scène à la souris
  double _frustum=0.01;
  bool _withBlend=false;


signals:

public slots:

};

#endif // GLAPPLICATION_H

