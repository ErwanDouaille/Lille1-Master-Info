#include "Intersection.h"
#include "Line.h"
#include "CsgTree.h"
#include <math.h>
#include "Primitive.h"
#include "Ray.h"


/**
@file
@author Fabrice Aubert
*/

using namespace p3d;
using namespace std;


double Intersection::lambda() const {
    return _lambda;
}

Intersection::Intersection(double lambda) {
  _node=NULL;
  _lambda=lambda;
}

Intersection::Intersection() {
  _node=NULL;
}

Intersection::~Intersection() {

}

const Material &Intersection::material() const {
    return _node->primitive()->material();
}


void Intersection::lambda(double k) {
    _lambda=k;
}

void Intersection::node(CsgTree *n) {
    _node=n;
}

const CsgTree *Intersection::node() const {
    return _node;
}


Ray Intersection::computeReflectRay() const {
    Ray res;
    Vector3 P=incident().point(lambda());
    res.point(P);

    Vector3 N=normal();

    N.normalize();
    Vector3 D=incident().direction();
    D.normalize();

    // N = normale, D = rayon incident : R=?

    if (N.dot(D)>0) N=-N;

    Vector3 R=-2.0*(D.dot(N))*N+D;
    res.direction(R);
    res.inside(incident().inside());
    return res;
}

Ray Intersection::computeRefractRay() const {
    Ray res;
    double m1=1.0,m2=1.0; // coefficients des milieux pour descartes

    if (incident().inside()) {
      m1=_node->primitive()->material().refractionIndex();
    }
    else {
      m2=_node->primitive()->material().refractionIndex();
    }

    //M1=1.0;M2=1.0;
    Vector3 P=incident().point(lambda());
    res.point(P);
    _node->pointRoot2Node(&P);
    Vector3 N=normal();
    N.normalize();
    Vector3 D=incident().direction();
    D.normalize();
    Vector3 T;
    double n=m1/m2;
    double ND=N.dot(D);
    if (ND<0) {N=-N;ND=-ND;}
    double sin22=1.0-n*n*(1.0-ND*ND);
    if (sin22 <0) { // angle critique => le rayon transmis est réfléchi
      T=-2.0*ND*N+D;
      res.inside(incident().inside());
    }
    else {
      T=n*D+(n*ND-sqrt(sin22))*N;
      res.inside(!incident().inside());
    }
    res.direction(T);
    return res;
}

void Intersection::setEmpty() {
	_node=NULL;
}

bool Intersection::isEmpty() const {
	return (_node==NULL);
}


void Intersection::computePointNormal() {
    Vector3 P=incident().point(lambda());
    _point=P;
    _node->pointRoot2Node(&P);
    _normal=_node->computeNormalLeaf(P);
    _node->normalNode2Root(&_normal);
//    _node->directionNode2Root(&_normal);
    _normal.normalize();
}




