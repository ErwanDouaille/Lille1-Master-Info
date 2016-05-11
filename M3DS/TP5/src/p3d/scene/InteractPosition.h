#ifndef CONTROLVECTOR3_H_INCLUDED
#define CONTROLVECTOR3_H_INCLUDED

/*!
*
* @file
*
* @brief Control an array of points
* @author F. Aubert
*
*/

#include <vector>
#include "Vector3.h"
#include "Vector2.h"
#include "Camera.h"

namespace p3d {
/**
@class ControlMouse
@brief allows to control points in an array with mouse (select/move/add/remove)
*/

class InteractPosition {
  std::vector<Vector3> _backup; // in case we want to backup the points (undo motion)
  Camera *_camera;              // contains the tool to transform 2d screen in 3d world
  int _selected;                // the point that can be moved by motion
  Vector3 _startingMouse;       // where the mouse is pressed in world coordinates

public:
  InteractPosition();
  virtual ~InteractPosition();
  void camera(Camera *c) {_camera=c;}

  virtual p3d::Vector3 *interactPoint(unsigned int i)=0;
  virtual void interactUpdate(unsigned int) {}
  virtual unsigned int interactSize()=0;
  virtual void interactInsert(unsigned int i,const p3d::Vector3 &p)=0;

  void backup();
  void restore();


  unsigned int nearestPoint(const Vector2 &m,float radius);

  void movePoint(unsigned int i, const Vector2 &m);


  void moveSelected(const Vector2 &m);

  void startMouse(const Vector2 &m);

  void add(const Vector2 &mouse);
  void addEnd(const Vector2 &mouse);
  unsigned int selectNearest(const Vector2 &m, double seuil);
};


}

#endif

