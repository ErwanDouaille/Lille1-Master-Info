#ifndef Plane_H_INCLUDED
#define Plane_H_INCLUDED

#include "Matrix4.h"
#include "Vector3.h"
#include "Line.h"
/*!
*
* @file
*
* @brief Plane Class : a Plane is defined by AM.n=0 (i.e. defined by a point A and a normal n)
* @author F. Aubert
*
*/

namespace p3d {
  class Line;
  enum ESide {SIDE_PLUS,SIDE_MINUS,SIDE_NONE}; // NONE => not computed

/**
@class Plane
@brief Representation of a plane (based on AM.n=0 : A is a point of the plane, and n its normal)
*/
class Plane {
    Vector3 _n; ///< normal
    Vector3 _a;  ///< point of the plane (named origin).

    public:
    /// destructor
    virtual ~Plane();
    /// copy
    Plane &operator=(const Plane &p);
    /// default constructor
    Plane();
    /// constructor from a point and its normal Plane(a,n)
    Plane(const Vector3 &a,const Vector3 &n);

    /// get the normal
    const Vector3 &normal() const;
    /// get the origin
    const Vector3 &point() const;
    /// sets the normal to n
    void normal(const Vector3 &n);
    /// sets the point to p
    void point(const Vector3 &p);

    /// sets the point and the normal
    void set(const Vector3 &a,const Vector3 &n);
    /// sets this plane according to the 3 given points
    void set(const Vector3 &p1,const Vector3 &p2,const Vector3 &p3);
    /// orthogonal projection of the point P
    Vector3 project(const Vector3 &p);
    /// projection of p along the direction u
    Vector3 project(const Vector3 &p,const Vector3 &u);
    /// intersection of the plane with the line d : returns k such P=a+ku
    double interK(const Line &d) const;

    /// returns the intersection point P between the plane and the line d
    Vector3 intersect(const Line &d) const;

    /// returns the signed distance of the point p from this plane (>0 if p is at the normal side).
    double distance(const Vector3 &p) const;
    /// returns the sign of the point p (normal side or the opposite side)
    ESide side(const Vector3 &p) const;
    /// output stream
    friend std::ostream& operator<<(std::ostream &s,const Plane &p);
    /// returns the transformation (a Matrix4) that corresponds to the projection on the plane about the direction u
    Matrix4 projectionDirection(const Vector3 &u);
    /// returns the transformation (a Matrix4) that corresponds to the projection on the plane from the point p
    Matrix4 projectionPoint(const Vector3 &p);
};

}


#endif // Plane_H_INCLUDED

