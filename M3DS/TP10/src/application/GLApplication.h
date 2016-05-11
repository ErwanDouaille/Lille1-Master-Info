#ifndef GLAPPLICATION_H
#define GLAPPLICATION_H

#include "GLWidget.h"

#include "MeshObject3D.h"
#include "Texture.h"
#include "Camera2.h"
#include "SceneIntersection.h"
#include "DebugTool.h"


/*!
*
* @file
*
* @brief
* @author F. Aubert - Univ Lille 1
*
*/

class GLApplication : public GLWidget
{
public:
  virtual ~GLApplication();
  GLApplication();

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



  void drawScene();

  void drawCow();
  void drawTriceratops();
  void drawTriangle();

  void drawCursor();

  void testIntersection();
  void selectObject();
  void moveSelectedObject();

private:
  typedef enum {Manipulation_Translation,Manipulation_Orientation} EManipulation;

  p3d::MeshObject3D _cow, _triceratops,_triangle;

  Camera2 _camera;
  p3d::Line _pickingRay;
  p3d::Texture _groundTexture;

  p3d::DebugTool _debug;

  std::vector<p3d::MeshObject3D *> _meshArray;

  SceneIntersection _sceneIntersection;
  Intersection *_selectedIntersection;
  p3d::Vector3 _attachPointWorld;

  EManipulation _controlMouse;
};

#endif // GLAPPLICATION_H

