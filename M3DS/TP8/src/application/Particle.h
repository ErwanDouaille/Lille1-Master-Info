#ifndef PARTICULE_H
#define PARTICULE_H

#include "Vector3.h"
#include "Texture.h"
/**
@file
@author F. Aubert
@brief Particle

*/

class Particle {
  public:
    Particle();
    virtual ~Particle();


    const p3d::Vector3 &position() const;
    const p3d::Vector3 &velocity() const;

    void position(const p3d::Vector3 &p);
    void velocity(const p3d::Vector3 &v);

    void addForce(const p3d::Vector3 &f);
    void resetForce();

    void addVelocityCorrec(const p3d::Vector3 &v);
    void addPositionCorrec(const p3d::Vector3 &p);

    void velocityCorrection();
    void positionCorrection();

    const p3d::Vector3 &force();

    void mass(double m);
    double mass();


    void drawSphere();
    void drawParticle();

    bool dead();
    bool alive();
    void initRandom();
    void birth();
    void updateLife();
    void kill();

    void timeOfLife(double t);
    double timeOfLife();
    double radius();
    void radius(double r);

  protected:
  private:
    p3d::Vector3 _position;
    p3d::Vector3 _velocity;

    p3d::Vector3 _pCorrec;
    p3d::Vector3 _vCorrec;

    p3d::Vector3 _color;

    double _mass;

    p3d::Vector3 _cumulForce;


    bool _life;
    double _timeOfLife;
    double _birth;

    double _radius;

};

#endif // PARTICULE_H

