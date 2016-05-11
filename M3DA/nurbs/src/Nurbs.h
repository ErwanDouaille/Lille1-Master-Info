#ifndef NURBS_H
#define NURBS_H

#include "Vector2.h"

#include <vector>
#include <iostream>
#include <GL/gl.h>

using namespace std;

class Nurbs
{
public:
    vector<double> _knot;
    vector<Vector2> _controlPoints;
    int _degree;
    double _delta;

    Nurbs();
    void initialize(int m, int p);
    double evaluate(int k, int p, double t);
    Vector2 evalBSpline(double t);
    void draw(int k, int p);
    void drawOnly(int k, int p);
    void drawControlPoints();
    void drawBSpline();
    void addControlPoint(Vector2 point);

    vector<double> getKnot() { return _knot;}
    vector<Vector2> getControlPoints() { return _controlPoints;}

    double getDelta() {return _delta;}
    void setDelta(double delta) { _delta = delta;}
    void resetDelta() { _delta = 0.0;}

};

#endif // NURBS_H
