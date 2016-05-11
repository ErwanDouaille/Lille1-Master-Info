#include "RollerCoaster.h"

using namespace p3d;
using namespace std;


RollerCoaster::~RollerCoaster() {
    _lastB=Vector3(0,0,1);
    _flip=false;
}

RollerCoaster::RollerCoaster() {
  way({Vector3(-10,1,0),Vector3(10,1,0),Vector3(10,10,1),Vector3(0,10,2),Vector3(0,1,3),
      Vector3(20,1,3),Vector3(30,5,10),Vector3(20,1,20),Vector3(-20,1,20),Vector3(-20,1,0),Vector3(-15,1,0),Vector3(-10,1,0)});
}

void RollerCoaster::way(const std::vector<p3d::Vector3> &p) {
  _curve.create(p.size());
  for(unsigned int i=0;i<p.size();++i) {
    _curve.point(i,p[i]);
  }
  _curve.setup();
  _curve.initAcceleration();
}



void RollerCoaster::draw() {
  _curve.draw();
  _curve.drawPoint();
}


p3d::Matrix4 RollerCoaster::tbn(double t) {
    return _curve.tbn(t);
}

p3d::Matrix4 RollerCoaster::frame(double t) {
    return _curve.frame(t);
}

void RollerCoaster::drawTBN(double t) {
  p3d::drawFrame(tbn(t),10);
}

void RollerCoaster::drawFrame(double t) {
  p3d::drawFrame(frame(t),10);
}



