#ifndef INC_Movable_H
#define INC_Movable_H

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

#include "Quaternion.h"
#include <stack>
#include "Vector3.h"
#include "Line.h"

namespace p3d {

  typedef enum {Coordinate_Local,Coordinate_World} ECoordinate;

  class Object3D {

    static std::stack<Object3D> _stack;

    Vector3 _position;
    Quaternion _orientation;
    Vector3 _scale;

    public:
    Object3D();
    /// copy
    Object3D &operator=(const Object3D &m);
    /// copy constructor
    Object3D(const Object3D &f);
    virtual ~Object3D();

    Matrix4 worldLocal() const;
    Matrix4 localWorld() const;

    void compose(const Object3D &f);

    void translate(const Vector3 &tr,ECoordinate f=Coordinate_World);
    void translate(double x,double y,double z,ECoordinate f=Coordinate_World) {translate(Vector3(x,y,z),f);} // TODO : should be the right way... (Vector3 => x,y,z)
    void rotate(double angle,const Vector3 &axe,ECoordinate f=Coordinate_Local);
    void rotate(double angle,double x,double y,double z,ECoordinate f=Coordinate_Local);
    void rotate(double angle, const Vector3 &axe, const Vector3 &center,ECoordinate f=Coordinate_Local);
    void rotate(const Quaternion &q,ECoordinate f=Coordinate_Local);
    //! scale is always considered in LOCAL coordinate
    void scale(const Vector3 &s/*,EMovable f=LOCAL_COORDINATE*/) {_scale=s;}
    void scale(double x,double y,double z/*,EMovable f=LOCAL_COORDINATE*/) {scale(Vector3(x,y,z));}
    void scale(double k) {scale(k,k,k);}

    void set(const Object3D &f);

    void setIdentity();

    inline const Vector3 &position() const {return _position;}
    inline const Quaternion &orientation() const {return _orientation;}
    inline const Vector3 &scale() const {return _scale;}

    inline void position(const Vector3 &pos) {_position=pos;}
    inline void position(double x,double y,double z) {_position=Vector3(x,y,z);}
    inline void orientation(const Quaternion &q) {_orientation=q;}
    inline void scaling(const Vector3 &s) {_scale=s;}
    inline void scaling(double k) {_scale=Vector3(k,k,k);}

    void orientation(const Vector3 &i,const Vector3 &j,const Vector3 &k);
    void orientation(double *a,Vector3 *u);
    void orientation(double a,const Vector3 &u);

    // linear interpolation this=(1-t)f1+tf2 (i.e. linear interpolation of the translation and the quaternion)
    void mix(const Object3D &f1,const Object3D &f2,double t);

    // returns the point/direction in LOCAL (f=LOCAL_COORDINATE and then u supposed to be given in World) or in WORLD (f=WORLD_COORDINATE and then u supposed to be given in LOCAL)
    Vector3 pointTo(ECoordinate f,const Vector3 &u) const;
    void pointTo(ECoordinate f,Vector3 *u) const;
    Vector3 directionTo(ECoordinate f,const Vector3 &u) const;
    void directionTo(ECoordinate f,Vector3 *u) const;

    //! get the direction (i.e. the z axis) in the *WORLD* coordinate system
    p3d::Vector3 direction() const;
    //! set the direction (i.e. the z axis) with u expressed in the *WORLD* coordinate system. Caution : you may want use lookAt instead.
    void direction(const Vector3 &u);

    //! set the orientation such that the z direction point to pos (pos is expressed in *WORLD*).
    void lookAt(const Vector3 &pos,const Vector3 &up=Vector3(0,1,0));


    void lineTo(ECoordinate f, Line *l);
    Line lineTo(ECoordinate f, const Line &l);

    //! rotate this coordinate system around the *WORLD* axes (y then x). center is assumed to be in *WORLD* coordinates
    void rotateXY(double ax, double ay, const Vector3 &center, const Vector3 &verticalAxis=Vector3(0,1,0), ECoordinate f=Coordinate_Local);

    void push();
    void pop();
  };

}

#endif

