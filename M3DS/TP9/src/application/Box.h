#ifndef BOITE_H
#define BOITE_H

/**
@file
@author F. Aubert
@brief opérations sur OBB pour mini simu physique (collision+impulsion)

*/


#include "Vector3.h"
#include "CollisionInfo.h"
#include "Texture.h"

class CollisionInfo;

class Box
{
  public:
    Box();

    void position(const p3d::Vector3 &p) {_position=p;}
    void velocity(const p3d::Vector3 &v) {_velocity=v;}

    void theta(double a) {_theta=a;}
    void omega(const p3d::Vector3 &w) {_omega=w;}

    void mass(double m) {_mass=m;}
    void dimension(double w,double h);
    void computeInertia();
    p3d::Vector3 computeMoment(const p3d::Vector3 &vect,const p3d::Vector3 &application);

    void texture(p3d::Texture *tex) {_texture=tex;}

    const p3d::Vector3 &position() const {return _position;}
    const p3d::Vector3 &velocity() const {return _velocity;}
    double mass() const {return _mass;}
    double width() const {return _width;}
    double height() const {return _height;}
    double theta() const {return _theta;}
    const p3d::Vector3 &omega() const {return _omega;}
    double inertia() {return _inertia;}

    void enableVisualEffect(unsigned int i);
    void disableVisualEffect(unsigned int i);

    void addForce(const p3d::Vector3 &f);
    void addMoment(const p3d::Vector3 &f,const p3d::Vector3 &application);
    void addMoment(const p3d::Vector3 &m);

    const p3d::Vector3 &force() const {return _cumulForce;}
    const p3d::Vector3 &moment() const {return _cumulMoment;}

    void resetForce() {_cumulForce.set(0,0,0);}
    void resetMoment() {_cumulMoment.set(0,0,0);}

    void addVelocityCorrec(const p3d::Vector3 &v);
    void addPositionCorrec(const p3d::Vector3 &p);
    void addOmegaCorrec(const p3d::Vector3 &w);

    void velocityCorrection();
    void positionCorrection();
    void omegaCorrection();


    p3d::Vector3 vertex(unsigned int i) const;

    p3d::Vector3 toWorld(const p3d::Vector3 &p) const;
    p3d::Vector3 toLocal(const p3d::Vector3 &p) const;

    void draw();

    void attachWorld(const p3d::Vector3 &p);
    const p3d::Vector3 &attachLocal();

    p3d::Vector3 attachWorld();


    static bool detectCollision(Box *b1, Box *b2, CollisionInfo *collision);
    static void distance(Box *b1,Box *b2,const p3d::Vector3 &u,double *distance,double *sign);
    static void drawDebugProject(const Box *b1,const Box *b2,const p3d::Vector3 &axe,double d1,double f1,double d2,double f2);

    void project(const p3d::Vector3 &axis,double *kmin,double *kmax) const;

    inline void color(const p3d::Vector3 &c) {_color=c;}
    inline const p3d::Vector3 &color() const {return _color;}

    // axes x et y de la boite
    p3d::Vector3 directionX() const;
    p3d::Vector3 directionY() const;

    bool isInside(const p3d::Vector3 &p) const;

    virtual ~Box();
  protected:
  private:

  // placement boite
  p3d::Vector3 _position;
  double _theta;
  double _width,_height;

  // propriétés physiques
  double _mass;
  double _inertia;
  p3d::Vector3 _velocity;


  // simulation
  p3d::Vector3 _omega;
  p3d::Vector3 _cumulForce;
  p3d::Vector3 _cumulMoment;

  p3d::Vector3 _vCorrec;
  p3d::Vector3 _pCorrec;
  p3d::Vector3 _wCorrec;

  p3d::Vector3 _color;
  p3d::Texture *_texture;


  unsigned int _visualEffect;

  // application force
  p3d::Vector3 _attach;

};

#endif // BOITE_H

