#ifndef INC_CAMERA_H
#define INC_CAMERA_H

#include "Object3D.h"

#include <stack>
#include <vector>

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

namespace p3d {
class Vector2;
class Camera : public Object3D {
public:
  enum EProjection {Projection_Ortho,Projection_Frustum};
  /// constructor
  Camera();
  /// destructor
  virtual ~Camera();

  /// returns the matrix camToWorld
  Matrix4 cameraWorld() const;
  /// returns the matrix worldToCam
  Matrix4 worldCamera() const;

  /// sets the camera such it looks the point at and the up direction is given by up
  void lookAt(const Vector3 &at,const Vector3 &up=Vector3(0,1,0));

  /// translates in the $z$ direction of the camera
  void forward(double k);
  void backward(double k);

  /// translates in the $x$ direction
  void right(double k);
  void left(double k);

  /// set the viewport (unused)
  void setViewport(int x,int y,int w,int h);

  /// sets with linear interpolation between c1 and c2
  void mix(const Camera &c1,const Camera &c2,double k);

  void volumeView(double left, double right, double down, double top, double pnear, double pfar);
  void frustum(double left, double right, double down, double top, double pnear, double pfar);
  void ortho(double left, double right, double down, double top, double pnear, double pfar);
  void perspective(double fov, double znear, double zfar);
  p3d::Matrix4 projectionMatrix() const;
  void viewport(unsigned int x,unsigned int y,unsigned int w,unsigned int h);

  /// returns the conversion of pointWindow (given in pixels) in NDC coordinates (u undergoes the inverse of viewport)
  Vector2 windowToNDC(const Vector2 &pointWindow);
  /// sets *pointCam in camera coordinates from pointWindow given in pixels (returns false if pointWindow is out of the viewport)
  bool windowToCamera(const Vector2 &pointWindow, Vector3 *pointCam);
  /// sets *pointCam in camera coordinates from pointWindow. pointPlane (given in PARENT coordinates) indicates the z-plane of *pointCam.
  bool windowToCamera(const Vector2 &u,const Vector3 &pointPlane,Vector3 *mouseCam);
  bool windowToRayWorld(const p3d::Vector2 &mouse, p3d::Line *l);
  bool windowToWorld(const Vector2 &mouse, Vector3 *l);
  bool windowToWorld(const Vector2 &pointWindow,const Vector3 &pointPlane,Vector3 *pointCam);
  double windowToNDC(double r);

  double vLeft() {return _volumeView[0];}
  double vRight() {return _volumeView[1];}
  double vTop() {return _volumeView[3];}
  double vBottom() {return _volumeView[2];}
  double vNear() {return _volumeView[4];}
  double vFar() {return _volumeView[5];}

  int viewX() const {return _viewport[0];}
  int viewY() const {return _viewport[1];}
  int viewWidth() const {return _viewport[2];}
  int viewHeight() const {return _viewport[3];}

  int selectNearest(const Vector2 &mouse, const std::vector<Vector3> &point, double radius,const Matrix4 &modelview);
  int selectNearest(const Vector2 &mouse, const std::vector<Vector3> &point, double radius);
  int selectNearest(const Vector2 &mouse, const std::vector<Vector4> &point, double radius);
private:
  float _x,_y,_w,_h; ///< stores the viewport
  double _volumeView[6]; ///< left,right,down,top,near,far
  unsigned int _viewport[4]; ///< x,y,width,height
  EProjection _projection;

};

}

#endif

