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

#include "Hermite.h"
#include "Curve.h"
#include "CatmullRomCurve.h"
#include "InteractPosition.h"
#include "RollerCoaster.h"


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
  QSize sizeHint() const {return QSize(500,500);}


  void updateCamera();
  void drawGround();

  // ****************************
  // TP :

  void initProjection();
  void drawHermite();
  void drawBezier();
  void drawCatmullRom();
  void drawRoller();
  void printMode();

private:

  p3d::Texture _ground;
  p3d::Camera _camera; // contrôle du placement de la scène à la souris
  double _frustum=0.01;

  // ****************************
  // TP :
  EDraw _modeDraw;
  EMouse _modeMouse;
//  EControl _modeControl;

  Hermite _hermite;
  Curve _bezier;
  CatmullRomCurve _catmullRom;
  RollerCoaster _roller;

  p3d::InteractPosition *_whichInteraction;

  int _hermiteInput;

  unsigned int _viewControlPoint;
  double _tAnimation;
  bool _animateTBN;
  bool _followRoller;


signals:

public slots:

};




#endif // GLAPPLICATION_H

