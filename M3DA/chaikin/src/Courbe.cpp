#include "Courbe.h"

Courbe::Courbe()
{
    this->initialize();
}

void Courbe::initialize() {
    _points = std::vector<Vector2>();
}


void Courbe::reset() {
    this->initialize();
}

void Courbe::drawLines(vector<Vector2> points) {
    glBegin(GL_LINES);
    for(int i = 1; i < points.size(); i++) {
        Vector2 p1 = points.at(i-1),
                p2 = points.at(i);
        glVertex2f(p1.getX(), p1.getY());
        glVertex2f(p2.getX(), p2.getY());
    }
    glEnd();
}

void Courbe::drawLinesLoop(vector<Vector2> points) {
    glBegin(GL_LINE_LOOP);
    for(int i = 1; i < points.size(); i++) {
        Vector2 p1 = points.at(i-1),
                p2 = points.at(i);
        glVertex2f(p1.getX(), p1.getY());
        glVertex2f(p2.getX(), p2.getY());
    }
    glEnd();

}

void Courbe::drawPoints(vector<Vector2> points) {
    for (int i = 0; i < points.size(); i++) {
        Vector2 point = points.at(i);
        glPointSize(5);
        if (point.isFixe())
            glPointSize(10);
        glBegin(GL_POINTS);
        glVertex2f(point.getX(), point.getY());
        glEnd();
    }
}

void Courbe::closePolygon() {
    cout << _points.size() << endl;
    if (_points.size()<3)
        return;
    _points.push_back(_points.at(0));
}

void Courbe::draw() {
    if(_points.size()<1)
        return;
    glLineWidth(1);
    glColor3f(1,0,0);
    drawLines(_points);
    glColor3f(0,1,0);
    drawPoints(_points);
    glFlush();
}

vector<Vector2> Courbe::calculateSubdivisionFrom(vector<Vector2> subdivisionPoints) {
    vector<Vector2> sub ;
    Vector2 p0, p1;
    for(int i = 0; i< subdivisionPoints.size()-1; i++) {
        p0 = subdivisionPoints.at(i);
        p1 = subdivisionPoints.at(i+1);

        if(p0.isFixe())
            sub.push_back(p0);

        Vector2 f0 = (p0*(3./4.)) + (p1*(1./4.));
        f0.setFixe(false);
        sub.push_back(f0);

        Vector2 f1 = (p0*(1./4.)) + (p1*(3./4.));
        f1.setFixe(false);
        sub.push_back(f1);
    }
    return sub;
}

void Courbe::drawSubdivisedChaikin(int nbSubdivision) {
    if(_points.size()<1)
        return;

    vector<Vector2> subdivisionPoints = _points;
    //RED LINES
    glLineWidth(1);
    glColor3f(1,0,0);
    drawLines(subdivisionPoints);
    drawPoints(subdivisionPoints);

    for(int i = 0; i < nbSubdivision; i++)
        subdivisionPoints = calculateSubdivisionFrom(subdivisionPoints);

    //BLUE LINES
    glLineWidth(2);
    glColor3f(0,0,1);
    drawLines(subdivisionPoints);
    drawPoints(subdivisionPoints);

    glFlush();
}

vector<Vector2> Courbe::calculate4pointsSubdivisionFrom(vector<Vector2> subdivisionPoints) {
    vector<Vector2> sub ;
    Vector2 pa, pb, pc, pd;
    for(int i = 0; i< subdivisionPoints.size(); i++) {
        if(i==0) {
            pa = subdivisionPoints.at(subdivisionPoints.size()-1);
        } else  {
            pa = subdivisionPoints.at(i-1);
        }
        pb = subdivisionPoints.at(i);
        pc = subdivisionPoints.at((i+1)%subdivisionPoints.size());
        pd = subdivisionPoints.at((i+2)%subdivisionPoints.size());
        sub.push_back(pb);
        sub.push_back( ((pd+pa)*(-1./16.)) + ((pc+pb)*(9./16.)));
    }
    return sub;
}

void Courbe::drawSubdivised4pointsChaikin(int nbSubdivision) {
    if(_points.size()<1)
        return;
    //RED LINES
    glLineWidth(1);
    glColor3f(1,0,0);
    drawLinesLoop(_points);
    drawPoints(_points);

    vector<Vector2> subdivisionPoints = _points;
    for(int i = 0; i < nbSubdivision; i++) {
        subdivisionPoints = calculate4pointsSubdivisionFrom(subdivisionPoints);
        //BLUE LINES
        glLineWidth(2);
        glColor3f(0,0,1);
        drawLinesLoop(subdivisionPoints);
        drawPoints(subdivisionPoints);
    }
    glFlush();
}
