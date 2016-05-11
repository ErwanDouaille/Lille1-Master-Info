#include "Airplane.h"
#include "GLTool.h"
#include "Tools.h"

using namespace p3d;
using namespace std;

Airplane::Airplane() {
  _increment=1.0;
  _accelerate=0.1;
  _velocity=0.0;
  _orientation.setIdentity();
  _position.set(0,0,0);
  _angleX=_angleY=_angleZ=0.0;
}

Airplane::~Airplane() {

}

void Airplane::initDraw() {
  _obj.initDraw();

}


/* Tracer l'objet :
Utilisez les attributs suivants :
  - _vertex : tableau de type Vector3 contenant les coordonnées des sommets (exemple : _vertex[3].x() = coordonnée x du sommet d'indice 3)
  - _face : un tableau d'entiers à deux dimensions (un tableau de tableau) contient les indices des sommets de chaque polygone :
      Exemple : _face[3] décrit la face 3; _face[3][4] donne l'indice (dans le tableau _vertex) du 5ième sommet de la face 3.
  - _face.size() et _vertex.size() donnent le nombre de faces et de sommets de l'objet.
  - _face[i].size() donne le nombre de sommets de la face i
*/
void Airplane::draw() {

  p3d::diffuseColor=Vector3(1,0,0);
  p3d::shaderLightPhong();
  _obj.draw();
}

void Airplane::drawWorld() {
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix.translate(_position);
  modelviewMatrix.rotate(_orientation);
//  p3d::modelviewMatrix.roate(_angleY,0,1,0); // yaw
//  p3d::modelviewMatrix.rotate(_angleX,1,0,0); // pitch
//  p3d::modelviewMatrix.rotate(_angleZ,0,0,1); // roll


  p3d::modelviewMatrix.scale(2,2,2); // grossir un peu l'avion
  draw();

  p3d::modelviewMatrix.pop();
}

void Airplane::move() {
//    _orientation.rotate(_velocity/(1.0+fabs(_velocity)),Vector3(0,1,0)); // le /100 est déterminé par essai/erreur
    _position.add(_orientation*Vector3(0,0,1)*_velocity*0.2);
}

void Airplane::pitchDown() {
//  _angleX+=_increment;
  _orientation.rotate(_increment,Vector3(1,0,0));

}

void Airplane::pitchUp() {

_orientation.rotate(-_increment,Vector3(1,0,0));

}

void Airplane::rollRight() {
  _orientation.rotate(_increment,Vector3(0,0,1));

}

void Airplane::rollLeft() {
  _orientation.rotate(-_increment,Vector3(0,0,1));

}

void Airplane::yawLeft() {
  _orientation.rotate(_increment,Vector3(0,1,0));


}

void Airplane::yawRight() {
  _orientation.rotate(-_increment,Vector3(0,1,0));

}

void Airplane::accelerate() {
  _velocity+=_accelerate;
  if (_velocity>3) _velocity=3;
}

void Airplane::decelerate() {
  _velocity-=_accelerate;
  if (_velocity<0) _velocity=0;
}

// lecture fichier .obj
void Airplane::read(const string &filename) {
  _obj.readInit(filename);
  _obj.rotateY(90);
}



