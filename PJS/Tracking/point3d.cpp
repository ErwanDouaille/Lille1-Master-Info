#include "point3D.h"

using namespace std;

Point3D::Point3D(void)
{
    this->x = 0;
    this->y = 0;
    this->z = 0;
}

Point3D::Point3D(float x, float y, float z){
    this->x = x;
    this->y = y;
    this->z = z;
}


Point3D::~Point3D(void)
{
}

Point3D Point3D::Zero(){
    return Point3D(0,0,0);
}
Point3D Point3D::One(){
    return Point3D(1,1,1);
}

float Point3D::distance(Point3D a, Point3D b){
    return abs(a.getX() - b.getX()) + abs(a.getY()+ b.getY()) + abs(a.getZ()+ b.getZ());
}

float Point3D::distanceEuclidienne(Point3D a, Point3D b){
    return sqrtf(powf((a.x - b.x),2) + powf((a.y - b.y),2) + powf((a.z - b.z),2));
}

float Point3D::dot(Point3D a, Point3D b){
    return (a.x * b.x + a.y * b.y + a.z * b.z) / length(a) * length(b);
}

float Point3D::length(Point3D p){
    return sqrtf(p.x * p.x + p.y * p.y + p.z * p.z);
}


Point3D operator+(Point3D a, Point3D b){
    return Point3D(a.getX() +b.getX() , a.getY() + b.getY(), a.getZ() + b.getZ());
}

Point3D operator+(Point3D a, int value){
    return Point3D(a.getX() + value, a.getY() + value, a.getZ() + value);
}

Point3D operator-(Point3D a, Point3D b){
    return Point3D(a.getX() - b.getX() , a.getY() - b.getY(), a.getZ() - b.getZ());
}

Point3D operator-(Point3D a, int value){
    return Point3D(a.getX() - value, a.getY() - value, a.getZ() - value);
}

Point3D operator*(Point3D a, Point3D b){
    return Point3D(a.getX() * b.getX() , a.getY() * b.getY(), a.getZ() * b.getZ());
}

Point3D operator*(Point3D a, int value){
    return Point3D(a.getX() * value, a.getY() * value, a.getZ() * value);
}

Point3D operator/(Point3D a, Point3D b){
    return Point3D(a.getX() / b.getX() , a.getY() / b.getY(), a.getZ() / b.getZ());
}

Point3D operator/(Point3D a, int value){
    return Point3D(a.getX() / value, a.getY() / value, a.getZ() / value);
}

bool operator==(Point3D a, Point3D b){
    return (a.getX() == b.getX() && b.getX() == b.getY() && a.getZ() == b.getZ());
}

bool operator!=(Point3D a, Point3D b){
    return (a.getX() != b.getX() || b.getX() != b.getY() || b.getZ() != b.getZ());
}

ostream& operator <<(ostream& Stream, const Point3D& Obj) {
    Stream << "x: " << Obj.getX() << "y: " << Obj.getY() <<"z: " << Obj.getZ() << endl;
    return Stream;
}
