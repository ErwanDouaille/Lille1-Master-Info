#ifndef CURVE_H_INCLUDED
#define CURVE_H_INCLUDED

#include "InteractPosition.h"

/*!
*
* @file
*
* @brief Curve operations
* @author F. Aubert
*
*/


#include "Vector3.h"
#include "InteractPosition.h"
#include <vector>

/**
@class Curve
@brief Curve operations (control points are in an array of points)
*/

class Curve : public p3d::InteractPosition {
    std::vector<p3d::Vector3> _pts;



    public:

    Curve();
    virtual ~Curve();

    void create(int nb);
    void point(int i,const p3d::Vector3 &p);
    p3d::Vector3 *pointv(unsigned int i) {return &_pts[i];}


    inline const p3d::Vector3 &point(unsigned int i) {return _pts[i];}
    inline std::vector<p3d::Vector3> *pointv() {return &_pts;}
    inline unsigned int nbPoint() {return _pts.size();}

    void drawControl();
    void drawPoint();
    void drawBezier();
    p3d::Vector3 evalBezier(double t);
    p3d::Vector3 evalCubicBezier(double t);

    inline const p3d::Vector3 &point(int i) {return _pts[i];}
    void insert(unsigned int where,const p3d::Vector3 &p);

    /////
    p3d::Vector3 *interactPoint(unsigned int i) {return &_pts[i];}
    unsigned int interactSize() {return _pts.size();}
    void interactInsert(unsigned int i, const p3d::Vector3 &insertPoint);


    p3d::Vector3 evalCubicVelocity(double t);
    p3d::Vector3 evalCubicAcceleration(double t);
 //   void drawTBN(double t, p3d::Vector3 *oldB=NULL, bool *flip=NULL);
    p3d::Matrix4 tbn(double t, p3d::Vector3 *oldB=NULL, bool *flip=NULL);
    p3d::Matrix4 frame(double t, const p3d::Vector3 &oldAcc, const p3d::Vector3 &newAcc, p3d::Vector3 *oldB, bool *flip);
};

#endif

