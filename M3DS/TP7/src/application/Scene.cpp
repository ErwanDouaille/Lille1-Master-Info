#include <QXmlStreamReader>

#include "Scene.h"
#include "IntersectionArray.h"
#include "Cube.h"
#include "Sphere.h"
#include "Cone.h"
#include "Cylinder.h"

#include "GLTool.h"

/**
@file
@author Fabrice Aubert
*/

using namespace std;
using namespace p3d;



/**
Calcul de l'intersection avec la scène : retourne le point le plus proche parmi toutes les intersections calculées (par csg()->intersection).
Remarque : seuil permet de considérer uniquement les points tels que lambda>seuil
*/
Intersection *Scene::intersection(const Ray &ray,double seuil) {
  IntersectionArray intersectionArray;
  Intersection *result=NULL;

  intersectionArray.clear();
  if (csg()) csg()->intersection(ray,&intersectionArray); // calcul de l'intersection entre le rayon et l'arbre csg => au retour interList contient toutes les intersections

  if (intersectionArray.empty()) {
    return NULL;
  }

  // on sélectionne celle qui est la plus proche et dont le lambda est plus grand que seuil (pour par exemple considérer uniquement ce qui se trouve devant l'écran, et pas derriere avec lambda >0).
  IntersectionArray::iterator i=intersectionArray.begin();

  while (!result && i<intersectionArray.end()) {
    if ((*i)->lambda()>seuil) {
      result=*i;
      *i=NULL;
    }
    else i++;
  }

  if (result) {

    result->incident(ray);
    result->computePointNormal();
  }

  intersectionArray.deleteAll();

  return result;

}

/** ********************************************************************** */


Scene::Scene() {
  //ctor
  _csg=NULL;
  _light.clear();
  _intensity.clear();
  _camera.position(Vector3(0,0,0));
}

void Scene::init() {
  delete _csg;
  _csg=NULL;
  _light.clear();
  _intensity.clear();
  _camera.position(Vector3(0,0,0));
}

Scene::~Scene() {
  //dtor
  delete _csg;
}

const Vector3 &Scene::lightPosition(unsigned int i) const {
  return _light[i];
}

double Scene::lightIntensity(unsigned int i) {
  return _intensity[i];
}

void Scene::addLight(const Vector3 &p,double intensity) {
  _light.push_back(p);
  _intensity.push_back(intensity);
}


CsgTree *Scene::readNode(QXmlStreamReader *doc,Material currentMat) {
  CsgTree *res=NULL;
  QXmlStreamAttributes attribute;
  QStringRef value;
  res=new CsgTree();
  unsigned int node=0;
  if (doc->name()=="node") {
    attribute=doc->attributes();
    value=attribute.value("operation");
    if (value=="difference") {
      res->nodeType(CsgTree::Node_Difference);
    }
    else if (value=="union") {
      res->nodeType(CsgTree::Node_Union);
    }
    else if (value=="intersection") {
      res->nodeType(CsgTree::Node_Intersection);
    }

  }
  else if (doc->name()=="primitive") {
    attribute=doc->attributes();
    value=attribute.value("type");
    if (value=="cube") {
      res->primitive(new Cube());
    }
    else if (value=="sphere") {
      res->primitive(new Sphere());
    }
    else if (value=="cone") {
      res->primitive(new Cone());
    }
    else if (value=="cylinder") {
      res->primitive(new Cylinder());
    }

  }
  Matrix4 translate,rotate,scale;
  translate.setIdentity();
  rotate.setIdentity();
  scale.setIdentity();
  double x,y,z,angle,red,green,blue,shininess,coefficient;
  doc->readNext();
  while(doc->tokenType()!=QXmlStreamReader::EndElement || (doc->name()!="node" && doc->name()!="primitive")) {
    if (doc->tokenType()==QXmlStreamReader::StartElement) {
      attribute=doc->attributes();
      if (doc->name()=="translate") {
        x=attribute.value("x").toString().toDouble();
        y=attribute.value("y").toString().toDouble();
        z=attribute.value("z").toString().toDouble();
        translate.setTranslation(Vector3(x,y,z));
      }
      else if (doc->name()=="rotate") {
        x=attribute.value("x").toString().toDouble();
        y=attribute.value("y").toString().toDouble();
        z=attribute.value("z").toString().toDouble();
        angle=attribute.value("angle").toString().toDouble();
        rotate.setRotation(angle,Vector3(x,y,z));
      }
      else if (doc->name()=="scale") {
        x=attribute.value("x").toString().toDouble();
        y=attribute.value("y").toString().toDouble();
        z=attribute.value("z").toString().toDouble();
        angle=attribute.value("angle").toString().toDouble();
        scale.setScale(Vector3(x,y,z));
      }
      else if (doc->name()=="diffuse") {
        red=attribute.value("red").toString().toDouble();
        green=attribute.value("green").toString().toDouble();
        blue=attribute.value("blue").toString().toDouble();
        currentMat.diffuse(Vector3(red,green,blue));
      }
      else if (doc->name()=="ambient") {
        red=attribute.value("red").toString().toDouble();
        green=attribute.value("green").toString().toDouble();
        blue=attribute.value("blue").toString().toDouble();
        currentMat.ambient(Vector4(red,green,blue,1.0));
      }
      else if (doc->name()=="specular") {
        red=attribute.value("red").toString().toDouble();
        green=attribute.value("green").toString().toDouble();
        blue=attribute.value("blue").toString().toDouble();
        shininess=attribute.value("shininess").toString().toDouble();
        currentMat.specular(Vector3(red,green,blue));
        currentMat.shininess(shininess);
      }
      else if (doc->name()=="reflection") {
        coefficient=attribute.value("coefficient").toString().toDouble();
        currentMat.reflectionCoefficient(coefficient);
      }
      else if (doc->name()=="refraction") {
        coefficient=attribute.value("coefficient").toString().toDouble();
        currentMat.refractionCoefficient(coefficient);
      }
      else if ((doc->name()=="node") || (doc->name()=="primitive")) {
        node++;
        if ((node>2) || res->isLeaf()) throw Error("Reading CSG : node found into primitive or there are more than 2 children",__LINE__,__FILE__);
        if (node==1) res->left(readNode(doc,currentMat));
        else res->right(readNode(doc,currentMat));
      }

    }
    doc->readNext();
  }
  res->matrix(translate*rotate*scale);
  if (res->isLeaf()) {
    res->primitive()->material(currentMat);
  }

  return res;
}

void Scene::read(const string &resource) {
  init();
  QFileInfo qfile=resourceFile(resource);
  QFile file(qfile.absoluteFilePath());
  file.open(QIODevice::ReadOnly);
  QXmlStreamReader doc(&file);
  QXmlStreamAttributes attribute;


  double x,y,z,intensity;

  Material currentMat;
  while (!doc.atEnd() && !doc.hasError()) {
    QXmlStreamReader::TokenType token=doc.readNext();
    if (doc.hasError()) cout << doc.errorString().toStdString() << endl;
    if (token==QXmlStreamReader::StartElement) {
      if (doc.name()=="scene") continue;
      else if ((doc.name()=="node") || (doc.name()=="primitive")) {
        _csg=readNode(&doc,currentMat);
      }
      else if (doc.name()=="light") {
        attribute=doc.attributes();
        x=attribute.value("x").toString().toDouble();
        y=attribute.value("y").toString().toDouble();
        z=attribute.value("z").toString().toDouble();
        intensity=attribute.value("intensity").toString().toDouble();
        addLight(Vector3(x,y,z),intensity);
      }
    }
  }
  file.close();
}


void Scene::drawGL() {


  p3d::modelviewMatrix.push();

  glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);

  for(unsigned int i=0;i<p3d::nbLight;++i) {
    p3d::lightPosition[i]=Vector4(lightPosition(i),1.0);
    p3d::lightIntensity[i]=0;
  }


  for(unsigned int i=0; i<min(nbLight(),p3d::nbLight); ++i) {
    p3d::modelviewMatrix.push();
    p3d::modelviewMatrix.translate(lightPosition(i).x(),lightPosition(i).y(),lightPosition(i).z());
    p3d::modelviewMatrix.scale(0.2,0.2,0.2);
    p3d::ambientColor=Vector4(0,1,0);
    p3d::shaderVertexAmbient();
    p3d::drawSphere();
    p3d::modelviewMatrix.pop();
  }

  for(unsigned int i=0;i<min(nbLight(),p3d::nbLight);++i) {
    p3d::lightPosition[i]=_camera.cameraWorld()*Vector4(lightPosition(i),1.0);
    p3d::lightIntensity[i]=lightIntensity(i);
  }

  for(unsigned int i=0;i<min(nbLight(),p3d::nbLight);++i) {
//    cout << "light : " << p3d::lightPosition[i] << "," << p3d::lightIntensity[i] << endl;
  }

  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);


  if (_csg) _csg->drawParent();
  p3d::modelviewMatrix.pop();
}

void Scene::prepareCsg() {
  if (_csg) _csg->cumulateMatrix();
}







