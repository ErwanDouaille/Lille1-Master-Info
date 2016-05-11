#ifndef PARTICULELIST_H
#define PARTICULELIST_H

#include "Particle.h"
#include "Texture.h"
#include <vector>
#include <list>

class ParticleList : public std::vector<Particle *> {
  public:
    ParticleList();
    virtual ~ParticleList();

    void computeForce();

    void euler(double dt);

    void add(Particle *p);

    void draw();
    void modeParticle(bool mode);


    void updateLife();

    void collisionGround();

    void birthRate(double nb);

    void texture(p3d::Texture *t);

    void drawData();

    void maxi(unsigned int nb);

    void link(unsigned int i,unsigned int j);

    unsigned int nbParticle();

    void startTime();

  protected:
  private:

  std::list<Particle *> _freeList;
  double _nbLatent;
  double _lastTime;
  double _nbpersec;
  p3d::Texture *_texture;
  unsigned int _nbMaxi;

  bool _modeParticle;
  Particle *_tabParticle;
};

#endif // PARTICULELIST_H

