#ifndef COURBE_H
#define COURBE_H

#include <vector>
#include <Vector2.h>
#include <iostream>
#include <GL/gl.h>

using namespace std;

class Courbe
{
public:
    Courbe();
    void initialize();
    void reset();

    vector<Vector2> getPoints(){return this->_points;}
    void addPoint(Vector2 point) {_points.push_back(point);}

    void closePolygon();
    void draw();
    void drawSubdivisedChaikin(int nbSubdivision);
    void drawSubdivised4pointsChaikin(int nbSubdivision);

private:
    void drawLines(vector<Vector2> points);
    void drawLinesLoop(vector<Vector2> points);
    void drawPoints(vector<Vector2> points);
    vector<Vector2> _points;
    vector<Vector2> calculateSubdivisionFrom(vector<Vector2> subdivisionPoints);
    vector<Vector2> calculate4pointsSubdivisionFrom(vector<Vector2> subdivisionPoints);


};

#endif // COURBE_H
