#include "ParticleList.h"

#include "Texture.h"

#include <ctime>
#include <cstdlib>

#include "GLTool.h"

using namespace std;
using namespace p3d;

ParticleList::ParticleList() {
  //ctor
  clear();
  _lastTime=clock()/CLOCKS_PER_SEC;
  _nbMaxi=2000;
  _freeList.clear();
  _modeParticle=true;
  _tabParticle=NULL;
  _texture=NULL;
}

ParticleList::~ParticleList() {
  //dtor
  delete[] _tabParticle;
}


void ParticleList::add(Particle *p) {
  push_back(p);
}


void ParticleList::texture(Texture *t) {
  _texture=t;
}


void ParticleList::draw() {

  _texture->bind();


  for(unsigned int i=0;i<size();i++) {
    if ((*this)[i]->alive()) {
      if (_modeParticle) {
        (*this)[i]->drawParticle();
      }
      else (*this)[i]->drawSphere();
    }
  }

}

void ParticleList::birthRate(double nb) {
  _nbpersec=nb;
}

void ParticleList::updateLife() {
  double elapsed=double(clock())/CLOCKS_PER_SEC-_lastTime;
  _nbLatent+=elapsed*_nbpersec;
  while ((_nbLatent>=1) && (_freeList.size()>0)) {
    Particle *newp;
    newp=_freeList.front();
    _freeList.pop_front();
    newp->initRandom();
    _nbLatent-=1;
  }
  _lastTime+=elapsed;


  unsigned int nbAlive=0;
  for(unsigned int i=0;i<size();i++) {
    if ((*this)[i]->alive()) {
      (*this)[i]->updateLife();
      if ((*this)[i]->dead()) {
        _freeList.push_back((*this)[i]);
      }
      nbAlive++;
    }
  }
}

void ParticleList::drawData() {
  for(unsigned int i=0;i<size();i++) {
    Particle *p=(*this)[i];
    glColor3f(0,0,1);
    p3d::drawArrow(p->position(),Vector3(2.0*p->velocity()),0.2,"","Vitesse");
    glColor3f(0,1,0);
    if (p->force().length()>0.1)
    p3d::drawArrow(p->position(),Vector3(10.0*p->force()),0.2,"","Force");
  }
}

void ParticleList::maxi(unsigned int nb) {
  clear();
  _nbMaxi=nb;
  _freeList.clear();
  delete[] _tabParticle;
  _tabParticle=new Particle[nb];
  for(unsigned int i=0;i<nb;i++) {
    this->push_back(_tabParticle+i);
    _freeList.push_back(_tabParticle+i);
  }
}

unsigned int ParticleList::nbParticle() {
  return size()-_freeList.size();
}


void ParticleList::link(unsigned int i,unsigned int j) {
  i=0;
  j=0;
}

void ParticleList::modeParticle(bool mode) {
  _modeParticle=mode;
}

void ParticleList::startTime() {
  _lastTime=clock()/CLOCKS_PER_SEC;
}


