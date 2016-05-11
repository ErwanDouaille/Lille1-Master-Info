#include "Nurbs.h"

Nurbs::Nurbs()
{

}

void Nurbs::initialize(int m, int p) {
    _degree = p;
    _delta = 0.0;
    _knot = std::vector<double>();
    _controlPoints = std::vector<Vector2>();
    _controlPoints.clear();
    for(int i=1; i<=(m+p+1); i++) {
        _knot.push_back(i);
    }
}

void Nurbs::addControlPoint(Vector2 point) {
    _controlPoints.push_back(point);
}

double Nurbs::evaluate(int k, int p, double t) {
    if (p == 0)
        if(_knot.at(k) <= t && t < _knot.at(k+1))
            return 1.;
        else
            return 0.;
    double nA = (t-_knot.at(k))/(_knot.at(k+p)-_knot.at(k));
    double nB = evaluate(k, p-1, t);
    double nC = (_knot.at(p+k+1) -t)/(_knot.at(k+p+1)-_knot.at(k+1));
    double nD = evaluate(k+1, p-1, t);
    return (nA*nB)+(nC*nD);
}

Vector2 Nurbs::evalBSpline(double t) {
    Vector2 point;
    for(int i=0; i < _controlPoints.size(); i++) {
        double eval = evaluate(i, _degree, t);
        point = point + (_controlPoints.at(i) * eval);
    }
    return point;
}

void Nurbs::draw(int k, int p) {
    double step = 0.1;
    for(int i = 1; i < _knot.size(); i++) {
        double x1 = _knot.at(i-1);
        for (double j = step; j<1; j+=step) {
            double y1 = evaluate(k, p, _knot.at(i-1)+j-step), y2 = evaluate(k, p, _knot.at(i-1)+j);
            glBegin(GL_LINES);
            glColor3f(1.0f, 0.0f, 0.0f);
            glVertex2f(x1+j-step, y1);
            glVertex2f(x1+j, y2);
            glEnd();
        }
    }

    glPointSize(10);
    glColor3f(0,1,0);
    glBegin(GL_POINTS);
    for (int i = 0; i < _knot.size(); i++) {
        glVertex2f(_knot.at(i) ,0);
    }
    glEnd();
    glFlush();
}

void Nurbs::drawOnly(int k, int p) {
    double step = 0.1;
    for(int i = 1; i < _knot.size(); i++) {
        if(i > k && i <k+p+2) {
            double x1 = _knot.at(i-1), x2 = _knot.at(i);
            for (double j = step; j<1*_delta; j+=step) {
                double y1 = evaluate(k, p, _knot.at(i-1)+j-step), y2 = evaluate(k, p, _knot.at(i-1)+j);
                glBegin(GL_LINES);
                glColor3d(1.0f, 0.0f, 0.0f);
                glVertex2f(x1+j-step, y1);
                glVertex2f(x1+j, y2);
                glEnd();
            }
            glPointSize(10);
            glColor3f(0,1,0);
            glBegin(GL_POINTS);
            for (int i = 0; i < _knot.size(); ++i) {
                glVertex2f(_knot.at(i) ,0);
            }
            glEnd();
            glFlush();
        }
    }
}

void Nurbs::drawControlPoints() {
    for(int i = 1; i < _controlPoints.size(); i++) {
        Vector2 p1 = _controlPoints.at(i-1),
                p2 = _controlPoints.at(i);
        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_LINES);
        glVertex2f(p1.x(), p1.y());
        glVertex2f(p2.x(), p2.y());
        glEnd();
    }
    glPointSize(10);
    glColor3f(0,1,0);
    glBegin(GL_POINTS);
    for (int i = 0; i < _controlPoints.size(); ++i) {
        Vector2 point = _controlPoints.at(i);
        glVertex2f(point.x(), point.y());
    }
    glEnd();
    glFlush();
}

void Nurbs::drawBSpline() {
    glColor3f(0,0,1);
    glBegin(GL_LINE_STRIP);
    double tmin = _knot[_degree];
        double tmax = _knot[_controlPoints.size()];
        for (double t = tmin; t < tmax*_delta; t+=0.1) {
            Vector2 vec = evalBSpline(t);
            glVertex2f(vec.x(),vec.y());
        }
        glEnd();
    glFlush();
}
