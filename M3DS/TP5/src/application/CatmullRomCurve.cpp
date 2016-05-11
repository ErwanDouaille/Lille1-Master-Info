#include "GLTool.h"

#include "CatmullRomCurve.h"
#include "Vector3.h"

/**
@file
@author F. Aubert
*/


using namespace p3d;
using namespace std;


/**
* Tracé de la courbe d'interpolation
*/
void CatmullRomCurve::draw() {


  // A COMPLETER :
  // on doit tracer nbPoint()-1 courbes de bézier cubiques : la ième courbe a pour points de controle point(i),intermediate(i,0),intermediate(i,1) et point(i+1)
  // il suffit donc d'affecter la variable "cubic" avec ces points (affectez les points avec le setter cubic.point(j,un_point). Ex : cubic.point(j,intermediate(i,0)), puis de la tracer (en appelant simplement cubic.drawBezier() ).


  Curve cubic;
  cubic.create(4);


  if (nbPoint()<2) return; // tracé uniquement si plus de 2 points saisis.

  for (int i = 0 ; i < nbPoint()-1 ; i++){
     cubic.point(0,point(i));
     cubic.point(1,intermediate(i,0));
     cubic.point(2,intermediate(i,1));
     cubic.point(3,point(i+1));
     cubic.drawBezier();
  }


}

/**
*  Calcul des points intermédiaires pour obtenir une continuité G1 par Catmull-Rom
*/
void CatmullRomCurve::setup() {
  Vector3 parallele;

  // A COMPLETER : il s'agit d'initialiser les points intermédiaires (les points verts) en appelant les setters intermediate(i,0,<un Vector3>) et intermediate(i,1,<un Vector3>) )
  // les points intermédiaires doivent être fixés par la méthode vue en cours (tangentes parallèles aux segments [point(i-1),point(i+1)]).

  double k = 0.4;


  for (int i =  1 ; i < nbPoint()-1 ; i++){

      parallele = Vector3(point(i-1), point(i+1));
intermediate(i-1,1, point(i) - (parallele/2)*k);
      if (i== 1)
          intermediate(0,0,0.5*point(0)+0.5*intermediate(0,1));

            intermediate(i,0,point(i) + (parallele/2)*k);
  }
    int i = nbPoint()-2;
    intermediate(i,1,0.5*point(i+1)+0.5*intermediate(i,0));
}



/** *********************************************************************************************************** */
/** *********************************************************************************************************** */
/** *********************************************************************************************************** */
/** *********************************************************************************************************** */

CatmullRomCurve::CatmullRomCurve() {
  _intermediate.clear();
}

CatmullRomCurve::~CatmullRomCurve() {
}

void CatmullRomCurve::create(int nb) {
  Curve::create(nb);
  _intermediate.resize(2*(nb-1));
}

void CatmullRomCurve::interactInsert(unsigned int i, const Vector3 &insertP) {
    Curve::interactInsert(i/3+i%3,insertP);
    addIntermediate();
}


const Vector3 &CatmullRomCurve::intermediate(unsigned int i,unsigned int j) const {
  return _intermediate[2*i+j];
}

void CatmullRomCurve::intermediate(unsigned int i,unsigned int j,const Vector3 &p) {
  _intermediate[2*i+j]=p;
}

void CatmullRomCurve::addIntermediate() {
  int i=nbPoint()-2;
  if (i>=0) {
    if (_intermediate.size()!=(nbPoint()-2)*2) throw Error("incoherence between pts and intermediate",__LINE__,__FILE__);
    Vector3 p1=point(i)+0.33*(point(i+1)-point(i));
    Vector3 p2=point(i)+0.66*(point(i+1)-point(i));
    _intermediate.push_back(p1);
    _intermediate.push_back(p2);
  }
}



void CatmullRomCurve::drawTBN(double t) {
  p3d::drawFrame(tbn(t));
}

void CatmullRomCurve::drawFrame(double t) {
  p3d::drawFrame(frame(t));
}


void CatmullRomCurve::drawPoint() {
  vector<Vector3> allPts;

  for(unsigned int i=0; i<nbPoint(); i++) {
    allPts.push_back(point(i));
    p3d::draw(i,point(i)+Vector3(0.02,0.02,0.0));
  }
  p3d::shaderVertexAmbient();
  p3d::draw(GL_POINTS,allPts);
}

vector<Vector3> *CatmullRomCurve::intermediatev() {
  return &_intermediate;
}

void CatmullRomCurve::drawControl(int mode) {
  if (nbPoint()>0) {
    if (mode>0) {
      p3d::uniformAmbient(1,0,0);
      if (mode==1) {
        drawPoint();
      }
      if (mode>1) {
        Curve::drawControl();
        if (mode>2) {
          p3d::uniformAmbient(0,0,1);
          for(unsigned int i=0; i<nbPoint()-1; i++) {
            p3d::drawLines({point(i),intermediate(i,0),
                            intermediate(i,0),intermediate(i,1),
                            intermediate(i,1),point(i+1)});
            p3d::drawPoints({intermediate(i,0),intermediate(i,1)});
          }
        }
      }
    }
  }
}


Matrix4 CatmullRomCurve::frame(double t) {
  if (nbPoint()<2) return Matrix4::identity();
  double intpart,fracpart;
  fracpart=modf(t*(nbPoint()-1.0),&intpart);
  double realT=fracpart;
  int whichCurve=int(intpart);


  Curve cubic;
  cubic.create(4);
  Vector3 p1,p2;

  cubic.point(0,point(whichCurve));
  cubic.point(1,intermediate(whichCurve,0));
  cubic.point(2,intermediate(whichCurve,1));
  cubic.point(3,point(whichCurve+1));
  return cubic.frame(realT,_acceleration[whichCurve],_acceleration[whichCurve+1],&_oldB,&_flip);

}


Matrix4 CatmullRomCurve::tbn(double t) {
  if (nbPoint()<2) return Matrix4::identity();
  double intpart,fracpart;
  fracpart=modf(t*(nbPoint()-1.0),&intpart);
  double realT=fracpart;
  int whichCurve=int(intpart);


  Curve cubic;
  cubic.create(4);
  Vector3 p1,p2;

  cubic.point(0,point(whichCurve));
  cubic.point(1,intermediate(whichCurve,0));
  cubic.point(2,intermediate(whichCurve,1));
  cubic.point(3,point(whichCurve+1));
//  return cubic.tbn(realT,&_oldB,&_flip);
  return cubic.tbn(realT);

}

void CatmullRomCurve::initAcceleration() {
  if (nbPoint()>2) {
    Curve cubic;
    cubic.create(4);


    _acceleration.clear();
    _acceleration.push_back(Vector3(0,1,0)); //cubic.evalCubicAcceleration(1));
    for(unsigned int i=0;i<nbPoint()-2;++i) {
      cubic.point(0,point(i));
      cubic.point(1,intermediate(i,0));
      cubic.point(2,intermediate(i,1));
      cubic.point(3,point(i+1));
      _acceleration.push_back(cubic.evalCubicAcceleration(1));
    }
    _acceleration.push_back(Vector3(0,1,0));
  }
}



