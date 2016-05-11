#ifndef ROLLERCOASTER_H
#define ROLLERCOASTER_H

#include "CatmullRomCurve.h"
#include "GLTool.h"
#include "Camera.h"
#include "InteractPosition.h"

class RollerCoaster : public p3d::InteractPosition
{
  CatmullRomCurve _curve;
  p3d::Vector3 _lastB;
  bool _flip;
public:
  virtual ~RollerCoaster();
  RollerCoaster();

  CatmullRomCurve *curve() {return &_curve;}

  virtual p3d::Vector3 *interactPoint(unsigned int i) {return _curve.interactPoint(i);}
  virtual void interactUpdate(unsigned int) {_curve.setup();}
  virtual unsigned int interactSize() {return _curve.interactSize();}
  virtual void interactInsert(unsigned int i,const p3d::Vector3 &p) {_curve.interactInsert(i,p);_curve.setup();}

  void draw();
  void way(const std::vector<p3d::Vector3> &p);
  p3d::Matrix4 tbn(double t);
  p3d::Matrix4 frame(double t);
  void drawTBN(double t);
  void drawFrame(double t);
};

#endif // ROLLERCOASTER_H

