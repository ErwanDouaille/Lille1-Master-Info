#include <math.h>
#include "Car.h"
#include "GLTool.h"
#include "GLRender.h"

using namespace p3d;
using namespace std;


// ************************************************************
Car::~Car() {
}

Car::Car() {
  _orientation.setIdentity();
  _position.set(0,0,0);
  _steering=0;
  _rotateWheel=0;
  _acceleration=0;
  _velocity=0;
}

// ******************************************************************
// méthodes à compléter lors du TP
void Car::drawRim() {
    p3d::modelviewMatrix.push();
    diffuseColor = Vector3(0.8,0.8,0.8);
    for (int i = 0 ; i < 4 ; i++){
        p3d::modelviewMatrix.push();
        modelviewMatrix.scale(0.1,0.1,2);
        modelviewMatrix.translate(0,0,-0.5);
        drawCylinder();
        p3d::modelviewMatrix.pop();
        modelviewMatrix.rotate(45,1,0,0);
    }
    p3d::modelviewMatrix.pop();
}

void Car::drawWheel() {

    modelviewMatrix.push();
    drawRim();
    diffuseColor = Vector3(0.235,0.19,0.19);
    modelviewMatrix.rotate(90,0,1,0);
    modelviewMatrix.scale(1.5,1.5,3);
    drawTorus();
    modelviewMatrix.pop();
}

void Car::drawAxle() {

    modelviewMatrix.push();
        drawWheel();
        modelviewMatrix.push();
            diffuseColor = Vector3(0.8,0.8,0.8);
            modelviewMatrix.rotate(90,0,1,0);
            modelviewMatrix.scale(0.1,0.1,5);
            drawCylinder();
        modelviewMatrix.pop();
        modelviewMatrix.translate(5,0,0);
        drawWheel();
    modelviewMatrix.pop();

}

void Car::drawBody() {
    p3d::modelviewMatrix.push();
        diffuseColor = Vector3(0.33,0.411,0.117);
        modelviewMatrix.scale(2,1,1);
        drawCube();
        modelviewMatrix.scale(0.5,2,1);
        modelviewMatrix.translate(3,0.5,0);
        drawCube();
    p3d::modelviewMatrix.pop();


}

void Car::draw() {
  p3d::modelviewMatrix.push();
    modelviewMatrix.translate(-2.5,-1.5,-2.5);
      p3d::modelviewMatrix.push();
        modelviewMatrix.rotate(-90,0,1,0);
        modelviewMatrix.translate(1.5,1.5,-2.5);
        modelviewMatrix.scale(1.5,1.5,1.5);
        this->drawBody();
      p3d::modelviewMatrix.pop();

      //canon
      p3d::modelviewMatrix.push();
        modelviewMatrix.translate(2.5,6,1);
        modelviewMatrix.rotate(20,1,0,0);
        modelviewMatrix.scale(0.5,0.5,5);
        drawCylinder();
      p3d::modelviewMatrix.pop();

      p3d::modelviewMatrix.push();
        modelviewMatrix.translate(2.5,0,0);
        modelviewMatrix.rotate(_steering,0,1,0);
        modelviewMatrix.translate(-2.5,0,0);
         modelviewMatrix.rotate(-_rotateWheel,1,0,0);
        drawAxle();

      p3d::modelviewMatrix.pop();
      p3d::modelviewMatrix.push();

      modelviewMatrix.translate(0,0,6);
        modelviewMatrix.rotate(-_rotateWheel,1,0,0);
      drawAxle();
       p3d::modelviewMatrix.pop();
  p3d::modelviewMatrix.pop();
}


void Car::drawWorld() {

  p3d::modelviewMatrix.push();

    modelviewMatrix.translate(_position);
    modelviewMatrix.rotate(_orientation);
    draw(); // tracé de la voiture dans son repère local
  p3d::modelviewMatrix.pop();
}

void Car::move() {
  _acceleration+=-_velocity/50;
  _velocity+=_acceleration;
  _rotateWheel+=_velocity*40*0.2;
  _steering-=_steering/10*fabs(_velocity);
  _orientation.rotate(_steering*_velocity/(1.0+fabs(_velocity)),Vector3(0,1,0)); // le /100 est déterminé par essai/erreur
  _position.add(_orientation*Vector3(0,0,-1)*_velocity*0.2);
}


void Car::accelerate() {
  _acceleration=0.05;

}

void Car::decelerate() {
  _acceleration=0;
}

void Car::brake() {
  _acceleration=-0.02;

}

void Car::turnLeft() {
  _steering+=0.5;
  if (_steering>35) _steering=35;
}

void Car::turnRight() {
  _steering-=0.5;
  if (_steering<-35) _steering=-35;
}





