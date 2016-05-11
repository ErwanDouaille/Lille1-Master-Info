#include "Particle.h"
#include "Quaternion.h"
#include "GLTool.h"

/**
@file
@author F. Aubert
@brief Particle

*/

#include <ctime>
#include <cstdlib>


using namespace std;
using namespace p3d;




/** random number in [a,b] **/
double randDouble(double a, double b) {
    double c=double(rand())/RAND_MAX;
    return c*(b-a)+a;
}

double randUnit() {
    return double(rand())/RAND_MAX;
}


/** Données initiales d'une particule
**/
void Particle::initRandom() {
    // avant la chute des particules, backup au cas ou :)
    //  this->position(Vector3(randDouble(-1,1),randDouble(-1,1),0));
    //  this->velocity(2.0*Vector3(randDouble(-1,1),randDouble(0,5),randDouble(-1,1)));

    this->position(Vector3(randDouble(-1,1),randDouble(-5,5),randDouble(-5,5)));
    this->velocity(Vector3(0,0,0));
    _timeOfLife=randDouble(4,10); // en secondes

    _color.set(randDouble(0,1),randDouble(0,1),0.8);


    _mass=1.0;

    this->birth(); // se contente de conserver l'instant de la naissance (pour gérér la durée de vie)
}

/** *********************************************************** **/


Particle::Particle() {
    //ctor
    _mass=1;
    _velocity.set(0,0,0);
    _life=false;
    _radius=0.5;

    _pCorrec=Vector3(0,0,0);
    _vCorrec=Vector3(0,0,0);
}

Particle::~Particle() {
    //dtor
}


const Vector3 &Particle::position() const {
    return _position;
}

const Vector3 &Particle::velocity() const {
    return _velocity;
}


void Particle::position(const Vector3 &p) {
    _position=p;
}

void Particle::velocity(const Vector3 &v) {
    _velocity=v;
}

void Particle::kill() {
    _life=false;
}

void Particle::birth() {
    _birth=double(clock())/CLOCKS_PER_SEC;
    _life=true;
}


double Particle::mass() {
    return _mass;
}

void Particle::mass(double m) {
    _mass=m;
}


void Particle::drawSphere() {
    p3d::modelviewMatrix.push();
    p3d::modelviewMatrix.translate(_position.x(),_position.y(),_position.z());
    p3d::diffuseColor=Vector3(_color.x(),_color.y(),_color.z());
    p3d::modelviewMatrix.scale(_radius,_radius,_radius);
    p3d::shaderLightPhong();
    p3d::drawSphere();
    p3d::modelviewMatrix.pop();
}


void Particle::drawParticle() {

    //  glDepthMask(GL_FALSE);
    //  glEnable(GL_BLEND);
    //  glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_COLOR);
    p3d::modelviewMatrix.push();
    p3d::modelviewMatrix.translate(_position);
    Vector3 t=(p3d::modelviewMatrix.column(3)).xyz();
    p3d::modelviewMatrix.setTranslation(t);
    p3d::modelviewMatrix.scale(_radius/5.0);

    p3d::ambientColor=Vector4(_color,0.5);

    p3d::shaderTextureAmbient();
    p3d::drawSquare();
    p3d::modelviewMatrix.pop();

    glDepthMask(GL_TRUE);
    glDisable(GL_BLEND);

}

void Particle::updateLife() {
    double elapsed=double(clock())/CLOCKS_PER_SEC-_birth;
    if (elapsed>_timeOfLife) {
        kill();
    }
}

bool Particle::dead() {
    return !_life;
}

bool Particle::alive() {
    return _life;
}

void Particle::addForce(const Vector3 &f) {
    _cumulForce+=f;
}

const Vector3 &Particle::force() {
    return _cumulForce;
}

void Particle::resetForce() {
    _cumulForce.set(0,0,0);
}

void Particle::timeOfLife(double t) {
    _timeOfLife=t;
}

double Particle::timeOfLife() {
    return _timeOfLife;
}

double Particle::radius() {
    return _radius;
}

void Particle::radius(double r) {
    _radius=r;
}

void Particle::addVelocityCorrec(const Vector3 &v) {
    _vCorrec+=v;
}

void Particle::addPositionCorrec(const Vector3 &p) {
    _pCorrec+=p;
}



void Particle::velocityCorrection() {
    _velocity+=_vCorrec;
    _vCorrec=Vector3(0,0,0);
}

void Particle::positionCorrection() {
    _position+=_pCorrec;
    _pCorrec=Vector3(0,0,0);
}



