#ifndef INC_OBJET_H
#define INC_OBJET_H

#include "Quaternion.h"
#include "Vector3.h"
#include "Mesh.h"
#include <string>


class Airplane {
private:
  // représentation de l'orientation par angles de Cardans
  double _angleX,_angleY,_angleZ;

  // ou bien par quaternion
  p3d::Quaternion _orientation;

  p3d::Vector3 _position;

  double _increment,_accelerate,_velocity;

  p3d::Mesh _obj;

public:
  Airplane();
  virtual ~Airplane();
  void initDraw();
  void drawWorld();
  void draw();
  void read(const std::string &filename);

  inline const p3d::Vector3 &position() const {return _position;}
  inline const p3d::Quaternion &orientation() const {return _orientation;}

  void move();
  void pitchUp();
  void pitchDown();
  void rollLeft();
  void rollRight();
  void yawLeft();
  void yawRight();
  void accelerate();
  void decelerate();


  void position(const p3d::Vector3 &position) {_position=position;}
  void position(double x,double y, double z) {position(p3d::Vector3(x,y,z));}

};

#endif

