#ifndef POINT3D_H
#define POINT3D_H

#include <math.h>
#include <iostream>

class Point3D
{
public:
    Point3D(void);
    Point3D(float x, float y, float z);
    ~Point3D(void);

    float getX() const {return x;}
    float getY() const {return y;}
    float getZ() const {return z;}

    static Point3D Zero();
    static Point3D One();

    static float distance(Point3D a, Point3D b);
    static float distanceEuclidienne(Point3D a, Point3D b);
    static float dot(Point3D a, Point3D b);
    static float length(Point3D p);
    static Point3D cross(Point3D p1, Point3D p2);
    friend std::ostream& operator <<(std::ostream&, const Point3D&);

private:
    float x;
    float y;
    float z;
};


Point3D operator+(Point3D const& a, Point3D const& b);
Point3D operator+(Point3D const& a, int value);
Point3D operator-(Point3D const& a, Point3D const& b);
Point3D operator-(Point3D const& a, int value);
Point3D operator*(Point3D const& a, Point3D const& b);
Point3D operator*(Point3D const& a, int value);
Point3D operator/(Point3D const& a, Point3D const& b);
Point3D operator/(Point3D const& a, int value);
bool operator==(Point3D const& a, Point3D const& b);
bool operator!=(Point3D const& a, Point3D const& b);


#endif // POINT3D_H
