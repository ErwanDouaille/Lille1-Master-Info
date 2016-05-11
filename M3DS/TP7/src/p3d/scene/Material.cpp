#include "Material.h"
#include "GLRender.h"
#include <iostream>

using namespace p3d;
using namespace std;


Material::~Material() {}

Material::Material() {
  diffuse(Vector3(0.0,0.0,0.0));
  specular(Vector3(0,0,0.0));
  ambient(Vector4(0,0.0,0,1.0));
  shininess(100.0);
  reflectionCoefficient(0.0);
  refractionCoefficient(0.0);
  refractionIndex(0.0);
  _mapKd=NULL;
}

void Material::diffuseMap(Texture *t) {
  _mapKd=t;
}

Texture *Material::diffuseMap() {
  return _mapKd;
}

void Material::setGL() {
  p3d::ambientColor=ambient();
  p3d::diffuseColor=diffuse();
  p3d::specularColor=specular();
  p3d::shininess=shininess();
  p3d::uniformMaterial();
}

void Material::enableDiffuseMap() {
  if (_mapKd) {
    _mapKd->bind();
  }
}

ostream& p3d::operator <<(std::ostream &s,const Material &q) {
    s << "(a=" << q.ambient() << ",d=" << q.diffuse() << ",s=" << q.specular() << ")";
    return s;

}

void Material::material(const p3d::Vector4 &a,const p3d::Vector3 &d,const p3d::Vector3 &s,double r) {
  ambient(a);
  diffuse(d);
  specular(s);
  shininess(r);
}



