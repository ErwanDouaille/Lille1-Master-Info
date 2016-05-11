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
#include "Scene.h"
#include "Raytrace.h"



/**
  @class GLView

  The widget to draw with OpenGL.
  */
class GLApplication : public GLWidget {
  typedef enum {Draw_Bezier,Draw_Catmull,Draw_Hermite,Draw_Roller} EDraw;
  typedef enum {Control_Bezier,Control_Catmull,Control_Hermite} EControl;
  typedef enum {Mouse_Camera,Mouse_Interact,Mouse_Nothing} EMouse;

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
  QSize sizeHint() const {return QSize(1000,500);}


  void updateCamera();

  // ****************************
  // TP :

  void loadScene(unsigned int n);

  void restartRaytrace();
private:

  p3d::Camera _cameraGL;
  p3d::Camera _cameraRaytrace;
  double _frustum=0.01;


  // ****************************
  // TP :
  Scene _scene;
  unsigned int _sceneNumber;
  Raytrace _raytrace;
  p3d::Texture _raytraceTexture;



signals:

public slots:

};



#endif // GLAPPLICATION_H

