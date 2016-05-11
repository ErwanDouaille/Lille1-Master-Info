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

#include "EngineBox.h"



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



  // ****************************
  // TP :

private:

  p3d::Camera _camera;


  // ****************************
  // TP :

  bool _applyForce;
  p3d::Vector3 _pointForce;
  BoxList _boxList;
  EngineBox _engineBox;



signals:

public slots:

};



#endif // GLAPPLICATION_H





