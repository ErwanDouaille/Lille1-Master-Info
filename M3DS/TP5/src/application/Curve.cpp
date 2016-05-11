#include "Curve.h"
#include <cstdlib>
#include <iostream>
#include <sstream>

#include "GLTool.h"

/**
@file
@author F. Aubert
*/

using namespace p3d;
using namespace std;

/**
* Evaluation de la Curve de Bézier en $t$
**/
Vector3 Curve::evalBezier(double t) {
    Vector3 result;
    if (nbPoint()>1) {
        vector<Vector3> castel=_pts;      // tableau de points sur lesquels on applique deCasteljau
        // on recopie les points de controles dans le tableau castel (castel est donc initialisé avec la première ligne de l'algo triangulaire).

        // A COMPLETER : appliquer la méthode de De Casteljau
        for (int i = 0; i < castel.size()-1; i++) {
            for (int j = 0; j < castel.size()-i; j++) {
                castel[j] = (t*castel[j+1]) + ((1-t)*castel[j]);
            }
        }

        // le point de la courbe doit se trouver dans castel[0] à la fin de l'algo
        result=castel[0];
    }
    return result;
}


/**
* evalCubicBezier est une alternative à evalBezier mais uniquement pour les cubiques.
**/
Vector3 Curve::evalCubicBezier(double t) {
    if (_pts.size()!=4) throw Error("Curve should have 4 control points",__LINE__,__FILE__);

    Vector3 res;
    float mat[16]={-1,3,-3,1,3,-6,3,0,-3,3,0,0,1,0,0,0}; // bezier matrix

    vector<Vector3> q;
    q.resize(4);
    for(int i=0;i<4;i++) {
        q[i].set(0,0,0);
        for(int j=0;j<4;j++) {
            q[i]+=mat[i*4+j]*point(j);
        }
    }
    res=t*t*t*q[0]+t*t*q[1]+t*q[2]+q[3];

    return res;
}

Vector3 Curve::evalCubicVelocity(double t) {
    if (_pts.size()!=4) throw Error("Curve should have 4 control points",__LINE__,__FILE__);
    Vector3 res;
    float mat[16]={-1,3,-3,1,3,-6,3,0,-3,3,0,0,1,0,0,0}; // bezier matrix

    vector<Vector3> q;
    q.resize(4);
    for(int i=0;i<4;i++) {
        q[i].set(0,0,0);
        for(int j=0;j<4;j++) {
            q[i]+=mat[i*4+j]*point(j);
        }
    }
    res=3*t*t*q[0]+2*t*q[1]+q[2];

    return res;

}

Vector3 Curve::evalCubicAcceleration(double t) {
    if (_pts.size()!=4) throw Error("Curve should have 4 control points",__LINE__,__FILE__);
    Vector3 res;
    float mat[16]={-1,3,-3,1,3,-6,3,0,-3,3,0,0,1,0,0,0}; // bezier matrix

    vector<Vector3> q;
    q.resize(4);
    for(int i=0;i<4;i++) {
        q[i].set(0,0,0);
        for(int j=0;j<4;j++) {
            q[i]+=mat[i*4+j]*point(j);
        }
    }
    res=6*t*q[0]+2*q[1];

    return res;

}



/** *********************************************************************************************************************** **/


Curve::Curve() {
    _pts.clear();
}

Curve::~Curve() {
}


void Curve::create(int nb) {
    _pts.resize(nb);
}

void Curve::point(int i,const Vector3 &p) {
    _pts[i]=p;
}


Matrix4 Curve::frame(double t,const p3d::Vector3 &oldAcc,const p3d::Vector3 &newAcc,p3d::Vector3 *oldB,bool *flip) {
    Vector3 p=evalCubicBezier(t);

    Vector3 tt=evalCubicVelocity(t);
    Vector3 acc;
    acc.interpolateDirection(oldAcc,newAcc,t);

    Vector3 b=tt.cross(acc);
    if (b.length()<0.000001) {
        if (flip) {
            b=*oldB;
        }
        else {
            b=Vector3(0,1,0);
        }
    }
    tt.normalize();
    b.normalize();
    if (flip) {
        if (*flip) b=-b;
        if (fabs(b.dot(*oldB)+1)<0.1) {
            *flip=!*flip;
            b=-b;
        }
        *oldB=b;
    }
    Vector3 n=cross(b,tt);

    Matrix4 res;
    res.set(p,tt,b,n);

    return res;
}


Matrix4 Curve::tbn(double t,p3d::Vector3 *oldB,bool *flip) {
    Vector3 p=evalCubicBezier(t);


    Vector3 tt=evalCubicVelocity(t);
    Vector3 acc=evalCubicAcceleration(t);

    Vector3 b=tt.cross(acc);
    if (b.length()<0.000001) {
        if (flip) {
            b=*oldB;
        }
        else {
            b=Vector3(0,0,1);
        }
    }
    tt.normalize();
    b.normalize();
    if (flip) {
        if (*flip) b=-b;
        if (fabs(b.dot(*oldB)+1)<0.00001) {
            *flip=!*flip;
            b=-b;
        }
        *oldB=b;
    }
    Vector3 n=cross(b,tt);

    Matrix4 res;
    res.set(p,tt,b,n);

    return res;
}

void Curve::drawBezier() {
    float pas=1.0/(100.0-1);
    float t=0;

    vector<Vector3> drawPts;

    for(int i=0;i<100;i++) {
        drawPts.push_back(evalBezier(t));
        t+=pas;
    }
    p3d::drawThickLineStrip(drawPts);
}


void Curve::drawControl() {
    vector<Vector3> linePts;
    glPointSize(10);
    p3d::shaderVertexAmbient();
    if (nbPoint()>0) {
        unsigned int i;
        for(i=0;i<nbPoint();++i) {
            linePts.push_back(point(i));
            p3d::draw(i,point(i)+Vector3(0.01,0.01,0));
        }
        p3d::drawPoints(linePts);
        p3d::drawLineStrip(linePts);
    }
}



void Curve::interactInsert(unsigned int i, const Vector3 &insertPoint) {
    _pts.insert(_pts.begin()+i,insertPoint);
}

