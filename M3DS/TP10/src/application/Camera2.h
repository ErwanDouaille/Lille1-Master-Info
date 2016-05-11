#ifndef CAMERA2_H
#define CAMERA2_H

#include "GLTool.h"
#include "Matrix4.h"


class Camera2 {
  p3d::Matrix4 _worldCamera; // world -> camera matrix
  p3d::Matrix4 _projection;
  int _viewX,_viewY,_viewWidth,_viewHeight; // viewport


public:
  virtual ~Camera2();
  Camera2();

  void frustum(double left,double right,double bottom,double top,double znear,double zfar);
  void viewport(int x,int y,int width,int height);

  void applyGL();

  /// returns worldCamera matrix
  const p3d::Matrix4 &worldCamera() const;
  /// return cameraWorld matrix
  p3d::Matrix4 cameraWorld() const;

  /// translates camera : x,y,z are given in local coordinates or world coordinates (frame=Frame_Camera or Frame_World)
  void translate(double x,double y, double z,p3d::ECoordinate frame);
  void translate(const p3d::Vector3 &t,p3d::ECoordinate frame);
  /// rotates camera : angle in degrees; xAxis,yAxis,zAxis are given in local coordinates or world coordinates (frame=Frame_Camera or Frame_World)
  void rotate(double angle,double xAxis,double yAxis,double zAxis,p3d::ECoordinate frame);
  void rotate(double angle,const p3d::Vector3 &axis,p3d::ECoordinate frame);

  /// returns the point p from local coordinates to world coordinates (frame = Frame_World) or from world to local (frame = Frame_Local)
  p3d::Vector3 pointTo(p3d::ECoordinate frame, const p3d::Vector3 &p);
  /// returns the direction u from local coordinates to world coordinates (frame = Frame_World) or from world to local (frame = Frame_Local)
  p3d::Vector3 directionTo(p3d::ECoordinate frame, const p3d::Vector3 &u);

  /// returns the window point (x,y) (usually mouse coordinates) in NDC coordinates. Z coordinates is set to -1 (near plane).
  p3d::Vector3 windowToNDC(int x,int y);
  /// returns the window point (x,y) in Camera coordinates
  p3d::Vector3 windowToCamera(int x,int y);
  /// returns the window point (x,y) in World coordinates
  p3d::Vector3 windowToWorld(int x,int y);

  /// returns the line from Camera position to the pixel (x,y) in world coordinates (useful for picking)
  p3d::Line pickingRay(int x,int y);


};

#endif // CAMERA2_H

