#ifndef GLAPPLICATION_H
#define GLAPPLICATION_H


#include "GLWidget.h"

#include "Camera.h"
#include "Vector2.h"

#include <string>

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/


class GLApplication : public GLWidget {
  Q_OBJECT
public:
  GLApplication();
  virtual ~GLApplication();


  /** ***** **/
  /** GLApplication must implement these methods : */
  virtual void initialize();
  virtual void update();
  virtual void draw();
  virtual void resize(int width,int height);

  QSize sizeHint() const {return QSize(512,512);}
  /** ***** **/

  virtual void leftPanel(int i, const std::string &s);
  void updateCamera();

  /** ***** **/
  void drawSquare();
  void drawCrossSection();

  void crossSectionSquare();
  void crossSectionCircle();
  void normalCrossSection();

  void pathSegment();
  void pathCircle();

  p3d::Vector3 tangentInputPath(unsigned int i);

  p3d::Vector3 pointSpline(double tNormalized);
  p3d::Vector3 tangentSpline(double tNormalized);

  void extrudeLineStrip();
  void extrudeSpline();

  void drawInputPath();
  void drawSpline();

  void drawCylinder();
  void drawRevolution();

  p3d::Vector3 pointHermite(double t);
  void drawHermite();

  p3d::Vector3 transform(const p3d::Vector3 &p,const p3d::Vector3 &normal);
  p3d::Vector3 tangentCurve(int i);

  double scale(double tNormalized);


private:
  int _activeMenu;
  p3d::Camera _camera2D,_camera3D;
  double _frustum=0.01;


  std::vector<p3d::Vector3> _inputPath;
  std::vector<p3d::Vector2> _inputCrossSection;

  std::vector<p3d::Vector3> _extrude;

  std::vector<p3d::Vector2> _normalCrossSection;
  std::vector<p3d::Vector3> _normalExtrude;


};

#endif // GLAPPLICATION_H

