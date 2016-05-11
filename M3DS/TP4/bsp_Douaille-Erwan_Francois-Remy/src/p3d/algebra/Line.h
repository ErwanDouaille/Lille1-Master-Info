#ifndef LINE_H_INCLUDED
#define LINE_H_INCLUDED

#include <string>
#include <ostream>

#include "Vector3.h"
/*!
*
* @file
*
* @brief Line Class. A line is defined by \f$P=A+\lambda u\f$
* @author F. Aubert
*
*/

namespace p3d {
  class Plane;
/**
@class Line
@brief Line representation \f$P=A+\lambda u\f$
*/
class Line {
  Vector3 _u; ///< direction of the line
  Vector3 _a; ///< origin of the line

	public:

  /// destructor
  virtual ~Line();
  /// constructor
  Line();
  /// copy
  Line &operator=(const Line &l);
  /// copy constructor
  Line(const Line &l);
  /// constructor from (origin,direction)
	Line(const Vector3 &aa,const Vector3 &uu);

  /// returns the origin
  const Vector3 &point() const {return _a;}
  /// returns the origin
  const Vector3 &origin() const {return _a;}
  /// returns the origin
  const Vector3 &a() const {return _a;}
  /// returns the direction
  const Vector3 &direction() const {return _u;}
  /// returns the direction
  const Vector3 &u() const {return _u;}

  /// returns the point on the line : a+ku
  Vector3 point(double k) const;


  /// sets the origin
  void a(const Vector3 &aa) {_a=aa;}
  /// sets the origin
  void point(const Vector3 &aa) {_a=aa;}
  /// sets the origin
  void origin(const Vector3 &aa) {_a=aa;}
  /// sets the direction
  void u(const Vector3 &uu) {_u=uu;}
  /// sets the direction
  void direction(const Vector3 &uu)  {_u=uu;}

  /// sets the origin and the direction
	void set(const Vector3 &aa,const Vector3 &uu);

  /// sets according to the intersection of the 2 planes p1 and p2
  bool set(const Plane &p1,const Plane &p2);

  /// returns the squared distance from the point m to the line
  double distance2(const Vector3 &m) const;
  /// print the line
	void print(std::string mesg) const;
  /// output stream. Ex : cout << line << endl; (that prints (A=...,U=...))
	friend std::ostream& operator<<(std::ostream &s,const Line &l);


    void segment(const Vector3 &a, const Vector3 &b);
    double distanceSegment2(const Vector3 &p);
};
}

#endif // LINE_H_INCLUDED

