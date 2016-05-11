#include "EngineBox.h"

#include <ctime>
#include <cstdlib>
#include <iostream>
#include "GLTool.h"

#include "Vector3.h"


#include <cmath>

using namespace p3d;
using namespace std;


/** Application des forces et des moments à chacune des obb
- b->addForce(un Vector3 f) => ajoute la force f à la boite b (appliquée au centre de masse)
- b->addMoment(un Vector3 m) => ajoute le moment m
- si l'attachement par un ressort est actif sur la boite b (_cursorActive == true) :
  - b=_boxList->selected() donne la boite qui subit le ressort (de type Box *)
  - b->attachWorld() donne le point d'attache du ressort de cette boite dans le repère du monde
  - _cursor donne la position de la souris (dans le repère du monde)
**/
void EngineBox::computeForce() {
    for(unsigned int i=0; i<_boxList->size(); i++) {
        Box *b=_boxList->at(i);
        Vector3 a(0,b->mass()*-9.8,0);
        b->addForce(a);
        b->addForce(-1*b->velocity());
        b->addMoment((-1*b->omega()));
    }
    if(_cursorActive) {
        Box *b=_boxList->selected();
        b->addForce(_cursor-b->attachWorld());
        b->addMoment((_cursor-b->attachWorld()).cross(b->position()-b->attachWorld()));
    }
}

/** Gère la collision entre toutes les boites
**/
void EngineBox::interCollision() {
  for(unsigned int i=0; i<_boxList->size(); i++) {
    Box *b1=(*_boxList)[i];
    for(unsigned int j=i+1; j<_boxList->size(); j++) {
      Box *b2=(*_boxList)[j];

      CollisionInfo collisionInfo;
      bool collide=Box::detectCollision(b1,b2,&collisionInfo);
      if (collide) {
        b1->enableVisualEffect(1);
        b2->enableVisualEffect(1); // sert uniquement à faire un retour visuel de collision.


         // début Réponse à la collision :

        // décomposition du calcul : normal, vP1_old : vitesse du point de contact par rapport à b1, r1xN : produit vectoriel PG x normal, etc
        Vector3 normal=collisionInfo.axis();
        normal.normalize();
        Vector3 r1=(b1->position()-collisionInfo.applicationPoint());
        Vector3 vP1_old=b1->velocity()+r1.cross(b1->omega());

        Vector3 r2=(b2->position()-collisionInfo.applicationPoint());
        Vector3 vP2_old=b2->velocity()+r2.cross(b2->omega());

        Vector3 r1xN=-r1.cross(normal);
        Vector3 r2xN=-r2.cross(normal);

        // calcul de l'impulsion.
        double impulseNum;
        double impulseDen;

        impulseNum=-(1+0.5)*(normal.dot(b1->velocity()-b2->velocity())+b1->omega().dot(r1xN)-b2->omega().dot(r2xN));
        impulseDen=1.0/b1->mass()+1.0/b2->mass()+1.0/b1->inertia()*r1xN.dot(r1xN)+1.0/b2->inertia()*r2xN.dot(r2xN);

        double impulse=impulseNum/impulseDen;

        // calcul de la force d'impulsion
        Vector3 force=impulse*normal;

        // correction du mouvement :
        // - on corrige la vitesse angulaire et la vitesse des boites
        // - on corrige la position (car on répond à une position en recouvrement).
        b1->addVelocityCorrec(force/b1->mass());
        b1->addOmegaCorrec(force.cross(r1)/b1->inertia());

        b2->addVelocityCorrec(-force/b2->mass());
        b2->addOmegaCorrec(-force.cross(r2)/b2->inertia());

        b1->addPositionCorrec(collisionInfo.axis()*collisionInfo.mtd());
        b2->addPositionCorrec(-collisionInfo.axis()*collisionInfo.mtd());
         // fin réponse à la collision


      }
      else {
        b1->disableVisualEffect(1); // pas d'effet visuel de détection de collision
        b2->disableVisualEffect(1);
      }

    }
  }
}
/** Intégration Euler
**/
void EngineBox::euler(double dt) {
    for(unsigned int i=0; i<_boxList->size(); i++) {
        Box *b=_boxList->at(i);
        b->position(b->position()+dt*b->velocity());
        b->velocity(b->velocity()+dt*b->force()/b->mass());
        // à compléter
        b->theta(b->theta()+b->omega().z()*dt);
        b->omega(b->omega()+(b->moment()/b->inertia())*dt);

        // à laisser en fin :
        b->resetForce();
        b->resetMoment();
    }
}



/** ***************************************************************************************************************************** **/

EngineBox::EngineBox() {
    //ctor
    _dt=0.01;
    _boxList=NULL;
    _cursorActive=false;
    _planeList.clear();
    _start=clock();
}

EngineBox::~EngineBox() {
    //dtor
}

void EngineBox::step(double dt) {
    _dt=dt;
}


void EngineBox::boxList(BoxList *l) {
    _boxList=l;
}




void EngineBox::add(Plane *p) {
    _planeList.push_back(p);
}

void EngineBox::collisionPlane() {

    // pour chacun des plans
    for(unsigned int j=0; j<_planeList.size(); j++) {
        Plane *plane=_planeList[j];

        // pour chacune des boites
        for(unsigned int i=0; i<_boxList->size(); i++) {
            Box *b=(*_boxList)[i];

            // on détermine le sommet de la boite le plus négatif
            std::vector<Vector3> list;
            list.clear();
            for(unsigned int k=0;k<4;k++) {
                list.push_back(b->vertex(k));
            }

            unsigned int plus_loin=0;
            double dist=plane->distance(list[0]);
            for(unsigned int k=1; k<list.size(); k++) {
                double comp=plane->distance(list[k]);
                if (comp<dist) {
                    dist=comp;
                    plus_loin=k;
                }
            }
            Vector3 c_choice=list[plus_loin];
            // c_choice correspond au sommet le plus loin, dist donne la distance de recouvrement


            if (dist<0) { // il y a collision
                Vector3 normal=plane->normal();
                normal.normalize();

                // on calcule la vitesse du point choisi pour la collision dans vP_old
                Vector3 r1=(b->position()-c_choice);
                Vector3 vP_old=b->velocity()+r1.cross(b->omega());

                // on calcule l'impulsion (restitution à 0.5)
                Vector3 r1xN=-r1.cross(normal);
                double impulseNum;
                double impulseDen;

                impulseNum=-(1+0.5)*(normal.dot(b->velocity())+b->omega().dot(r1xN));
                impulseDen=1.0/b->mass()+1.0/b->inertia()*r1xN.dot(r1xN);

                double impulse=impulseNum/impulseDen;
                Vector3 force=impulse*normal; // "force" d'impulsion

                b->addVelocityCorrec(force/b->mass()); // correction en vitesse
                b->addOmegaCorrec((force/b->inertia()).cross(r1)); // correction en vitesse angulaire

                Vector3 PH;
                PH=(plane->point()-c_choice).dot(normal)/normal.dot(normal)*normal;
                b->addPositionCorrec((1.0+0.5)*PH); // correction en position


            }

        }
    }
}

void EngineBox::applyCorrection() {
    for(unsigned int i=0; i<_boxList->size(); i++) {
        Box *b=(*_boxList)[i];
        b->positionCorrection();
        b->velocityCorrection();
        b->omegaCorrection();
    }
}



void EngineBox::update() {
    double elapsed;
    do {
        elapsed=double(clock()-_start)/CLOCKS_PER_SEC;
    } while (elapsed<_dt);
    _start=clock();

    interCollision();
    collisionPlane();
    applyCorrection();
    computeForce();
    euler(elapsed);
}



void EngineBox::draw() {
    _boxList->draw();
}

void EngineBox::activeCursor(const Vector3 &p) {
    _boxList->select(p);
    Box *b=_boxList->selected();
    if (b!=NULL) {
        b->enableVisualEffect(2);
        _cursor=p;
        b->attachWorld(p);
        _cursorActive=true;
    } else
        _cursorActive=false;
}

void EngineBox::motionCursor(const Vector3 &p) {
    if (_cursorActive) {
        _cursor=p;
    }
}

void EngineBox::disableCursor() {
    Box *b=_boxList->selected();
    if (b) b->disableVisualEffect(2);
    _cursorActive=false;
}

void EngineBox::drawPlane2D() {
    p3d::ambientColor=Vector4(0.8,0.7,1,1);
    vector<Vector3> pts;
    for(unsigned int i=0; i<_planeList.size(); i++) {
        Plane *pl=_planeList[i];
        Vector3 direction=pl->normal().cross(Vector3(0,0,1));
        pts.push_back((pl->point()-100*direction));
        pts.push_back((pl->point()+100*direction));
    }
    p3d::shaderVertexAmbient();
    p3d::drawLines(pts);
}

bool EngineBox::isCursorActive() {
    return _cursorActive;
}


