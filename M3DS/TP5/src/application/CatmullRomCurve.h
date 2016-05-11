#ifndef CATMULLROMCURVE_H_INCLUDED
#define CATMULLROMCURVE_H_INCLUDED

#include "Curve.h"
#include "Vector3.h"
#include <iostream>

/**
@author F. Aubert
@brief representation/computation of a catmull rom curve (inherits Curve)
*/

class CatmullRomCurve: public Curve {
    std::vector<p3d::Vector3> _intermediate; // list of intermediate Bezier control points
                    // between pts[i] and pts[i+1] there should be inter[i*2] and inter[i*2+1] (cubic)

    public:
    CatmullRomCurve();
    virtual ~CatmullRomCurve();

    std::vector<p3d::Vector3> *intermediatev(); // get the reference

    void intermediate(unsigned int i,unsigned int j,const p3d::Vector3 &p);
    const p3d::Vector3 &intermediate(unsigned int i,unsigned int j) const;

    void setup();
    void draw();
    void drawControl(int mode=0);
    void drawPoint();

    //////
    p3d::Vector3 *interactPoint(unsigned int i) {if (i<nbPoint()) return pointv(i); else return &(_intermediate[i-nbPoint()]);}
    unsigned int interactSize() {return nbPoint()+_intermediate.size();}
    void interactInsert(unsigned int i,const p3d::Vector3 &p);


    void create(int nb);
    void drawTBN(double t);
    p3d::Matrix4 tbn(double t);
    p3d::Matrix4 frame(double t);

    void initAcceleration();
    void drawFrame(double t);
private:
    void addIntermediate();

    p3d::Vector3 _oldB=p3d::Vector3(0,0,1);
    bool _flip=false;
    std::vector<p3d::Vector3> _acceleration;

};

#endif

