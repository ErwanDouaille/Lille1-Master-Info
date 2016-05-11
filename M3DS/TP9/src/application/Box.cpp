#include "Box.h"
#include <iostream>
#include <cmath>
#include "Matrix4.h"
#include "GLTool.h"
#include "DebugTool.h"


/**
@file
@author F. Aubert
@brief opérations sur OBB pour mini simu physique (collision+impulsion)

*/

using namespace p3d;

using namespace std;



/** Indique si le point p, donné dans le repère du monde appartient à la boite
- Principe : on transforme dans le repère local et on teste dans le repère local (la définition de la boite étant naturelle dans ce repère).
- this->width() et this->height() donnent la largeur et hauteur de la boite.
**/

bool Box::isInside(const Vector3 &p) const {
    bool res;
    Vector3 v=this->toLocal(p);
    res=((v.x()<_width/2.0) && (v.y()<_height/2.0) && (v.x()>-_width/2.0) && (v.y()>-_height/2.0));
    return res;
}


/** Permet d'obtenir le point d'attache du ressort dans le repère du monde
**/
Vector3 Box::attachWorld() {
    return toWorld(_attach);
}




/** Projete les 4 sommets de la boite sur l'axe axe. Donne l'intervalle [*d,*f] de la projection (*d<*f)
  - this->vertex(i) : donne le i-ème sommet de la boite (dans le repère du monde)
  - il faut déterminer le minimum (dans kmin) et le maximum (dans kmax) des projections des 4 sommets pour déterminer l'intervalle de la boite sur l'axe
**/
void Box::project(const Vector3 &axe,double *mini,double *maxi) const {
    double kmin=0.0,kmax=0.0;
    // A compléter : l'intervalle [kmin,kmax] doit correspondre à la projection de la boite sur l'axe
    kmin = axe.dot(this->vertex(0));
    kmax = axe.dot(this->vertex(1));

    for (int i = 1; i<4;i++) {
        if (kmin>this->vertex(i).dot(axe))
            kmin = this->vertex(i).dot(axe);
        if (kmax<this->vertex(i).dot(axe))
            kmax = this->vertex(i).dot(axe);
    }
    *mini=kmin;
    *maxi=kmax;

}

/** calcule la distance de recouvrement des boites b1 et b2 sur l'axe : le resultat sera mis dans *distance
  le résultat *direction sera utilisé dans la question suivante, mais il faut l'affecter ici :
    - *direction = 1 si pour séparer b2 de b1 il faut bouger b2 dans le sens de u
    - *direction = -1 si pour séparer b2 de b1 il faut bouger b2 dans le sens opposé de u
**/

void Box::distance(Box *b1, Box *b2, const Vector3 &axe, double *distance, double *direction) {
    double d1,d2,f1,f2;
    double dist;
    b1->project(axe,&d1,&f1);
    b2->project(axe,&d2,&f2);

    drawDebugProject(b1,b2,axe,d1,f1,d2,f2);

    if(f1<d2 && d1<d2)
        *distance = fabs(d2-f1);
    else if (f2<d1 && d2<d1)
        *distance = fabs(d1-f2);
    else if(d2<f1 && d1<d2)
        *distance = -fabs(f1-d2);
    else if(f2>d1 && f2<f1)
        *distance = -fabs(f2-d1);
    else if (d1<d2 && f2<f1)
        *distance = d2-f2;
    else if (d1>d2 && f2>f1)
        *distance = d1-f1;
    else
        *distance = 0;


    if ( ((f2-d2)/2 + d2) < ((f1-d1/2)+d1) )
        *direction = 1;
    else
        *direction = -1;
    // A completer
    // d1,f1 : intervalle de projection pour la boite b1
    // d2,f2 : intervalle de projection pour la boite b2
    // quelle est la distance de recouvrement ? (*distance = ??)
    // affecter correctement *direction (-1 ou 1 ?)


}



/** détecte, entre b1 et b2, la collision et donne les informations nécessaires à la réponse dans *collision
  - principe : il faut déterminer les distances de recouvrement sur chacun des 4 axes possibles (2 axes pour b1 et 2 axes pour b2).
    si on trouve une distance négative, c'est que les boites ne se recouvrent pas sur cet axe => pas de collision (arrêt).
    si toutes les distances sont positives : b1 et b2 sont en intersection, et on renseigne *collision :
    - collision->mtd=un double : distance minimal sur les 4 axes (i.e. distance nécessaire pour séparer les 2 boites : sera utilisé pour la correction en position)
    - collision->normale=un Vector3 : l'axe de la distance minimal qui donne la normale au contact (i.e. direction de séparation)
    - collision->apply=un Vector3 : le point d'application qui sera utilisé pour l'impulsion (utilisé pour le calcul du moment de la force d'impulsion) :
      Le point d'application est déjà calculé (moyenne de tous les sommets intérieurs aux boites).
**/
bool Box::detectCollision(Box *b1,Box *b2,CollisionInfo *collision) {
    double dist;
    double dist_min;


    Vector3 axis[4]; // 4 axes à tester
    Vector3 axe_min; // l'axe qui correspond à la plus petite distance de séparation.


    double direction;

    // les 4 axes potentiellement séparateurs
    axis[0]=b1->directionX(); // axe x de b1
    axis[1]=b1->directionY(); // axe y de b1
    axis[2]=b2->directionX(); // axe x de b2
    axis[3]=b2->directionY(); // axe y de b2

    bool detect=true;

    // A completer (1 seul axe pour l'instant => il faut tenir compte des 4 axes) :
    // - déterminez la distance minimale dist_min de recouvrement entre les 4 axes axis[i] qui sont non séparateurs (Attention : minimale en valeur absolue !, mais dist_min est négative s'il y a recouvrement)
    // - vous devez affecter correctement axe_min (l'axe correspondant à dist_min) qui est un des axis[i] *mais* en tenant compte du sens de séparation de
    //   b2 par rapport à b1 (i.e. multiplier axis[i] par le signe (-1 ou 1) retourné par la méthode distance(b1,b2,...,)).
    // - assurez vous d'avoir affecté correctement detect à la fin (==true il y a collision, == false pas de collision).

    for (int i =0; i<4;i++){
        distance(b1,b2,axis[i],&dist,&direction);
        if (abs(dist)<abs(dist_min)){
            axe_min = axis[i]*direction;
            dist_min = dist;
        }
        if(dist > 0)
            detect = false;
    }

    // affecter les informations nécessaires à la réponse
    if (detect) {
        collision->mtd(dist_min);
        collision->axis(axe_min);

        // Calcul du point d'application de l'impulsion (ici barycentre des sommets intérieurs).
        // on créé la liste de tous les sommets inclus dans une des boites
        vector<Vector3> liste;
        liste.clear();
        for(unsigned int i=0; i<4; i++) {
            if (b1->isInside(b2->vertex(i))) liste.push_back(b2->vertex(i));
        }

        for(unsigned int i=0; i<4; i++) {
            if (b2->isInside(b1->vertex(i))) liste.push_back(b1->vertex(i));
        }

        Vector3 apply;
        apply.set(0,0,0);
        for(unsigned int i=0; i<liste.size(); i++) {
            apply.add(liste[i]);
        }
        apply=apply/liste.size();

        collision->applicationPoint(apply);

        collision->box1(b1);
        collision->box2(b2);
    }

    return detect;
}


/** **************************************************************************************************************************** **/

Box::Box() {
    //ctor
    _mass=0.1;
    _width=1;
    _height=1;
    _omega.set(0,0,0);
    computeInertia();

    _vCorrec=Vector3(0,0,0);
    _wCorrec=Vector3(0,0,0);
    _pCorrec=Vector3(0,0,0);
    _texture=NULL;
    _visualEffect=0;
    _color.set(0,0,0);
}

Box::~Box() {
    //dtor
}



void Box::computeInertia() {
    _inertia=1.0/12.0*_mass*(_width*_width+_height*_height);
}



void Box::dimension(double w,double h) {
    _width=w;
    _height=h;
    computeInertia();
}


void Box::addForce(const Vector3 &f) {
    _cumulForce+=f;
}

void Box::addMoment(const Vector3 &m) {
    _cumulMoment+=m;
}

void Box::addMoment(const Vector3 &f,const Vector3 &p) {
    addMoment(computeMoment(f,p));
}



void Box::enableVisualEffect(unsigned int i) {
    _visualEffect=_visualEffect | i;
}

void Box::disableVisualEffect(unsigned int i) {
    _visualEffect=_visualEffect & ~i;
}


void Box::draw() {
    if (_texture) {
        _texture->bind(0);
    }
    p3d::modelviewMatrix.push();
    p3d::modelviewMatrix.translate(_position);
    p3d::modelviewMatrix.rotate(_theta*180.0/M_PI,0,0,1);
    p3d::modelviewMatrix.scale(_width/2.0,_height/2.0,1);
    p3d::ambientColor=Vector4(1,1,1,1);
    p3d::shaderTextureAmbient();
    p3d::drawSquare();


    if (_visualEffect>0) {
        glDepthFunc(GL_ALWAYS);
        if (_visualEffect & 1) p3d::ambientColor=Vector4(1,0,0,1);
        else p3d::ambientColor=Vector4(_color,1);
        p3d::drawThickLineStrip({Vector3(-1,-1,0),Vector3(-1,1,0),Vector3(1,1,0),Vector3(1,-1,0),Vector3(-1,-1,0)});
    glDepthFunc(GL_LESS);
}

p3d::modelviewMatrix.pop();
}



Vector3 Box::computeMoment(const Vector3 &f,const Vector3 &a) {
    return f.cross(position()-a);
}


void Box::addVelocityCorrec(const Vector3 &v) {
    _vCorrec+=v;
}

void Box::addPositionCorrec(const Vector3 &p) {
    _pCorrec+=p;
}

void Box::addOmegaCorrec(const Vector3 &w) {
    _wCorrec+=w;
}

void Box::omegaCorrection() {
    _omega+=_wCorrec;
    _wCorrec.set(0,0,0);
}

void Box::velocityCorrection() {
    _velocity+=_vCorrec;
    _vCorrec=Vector3(0,0,0);
}

void Box::positionCorrection() {
    _position+=_pCorrec;
    _pCorrec=Vector3(0,0,0);
}

void Box::attachWorld(const Vector3 &p) {
    _attach=toLocal(p);
}

const Vector3 &Box::attachLocal() {
    return _attach;
}



Vector3 Box::vertex(unsigned int i) const {
    Vector3 res;
    switch(i) {
    case 0:
        res=toWorld(Vector3(-_width/2.0,-_height/2.0,0));
        break;
    case 1:
        res=toWorld(Vector3(_width/2.0,-_height/2.0,0));
        break;
    case 2:
        res=toWorld(Vector3(_width/2.0,_height/2.0,0));
        break;
    case 3:
        res=toWorld(Vector3(-_width/2.0,_height/2.0,0));
        break;
    default:
        throw ErrorD("Box::Vertex");
    }
    return res;
}



Vector3 Box::directionX() const {
    Vector3 res;
    res.set(1,0,0);
    res.rotate(this->theta()*180.0/M_PI,Vector3(0,0,1));
    return res;
}

Vector3 Box::directionY() const {
    Vector3 res;
    res.set(0,1,0);
    res.rotate(this->theta()*180.0/M_PI,Vector3(0,0,1));
    return res;
}



void Box::drawDebugProject(const Box *b1,const Box *b2,const Vector3 &axe,double d1,double f1,double d2,double f2) {

    Vector3 ref=0.5*(b1->position()+b2->position());
    double lambda=ref.dot(axe);
    Vector3 decal=axe.cross(Vector3(0,0,1));
    decal.normalize();
    decal=decal/10.0;
    p3d::addDebug(ref+decal+(d1-lambda)*axe,ref+decal+(f1-lambda)*axe,"",b1->color());
    p3d::addDebug(ref-decal+(d2-lambda)*axe,ref-decal+(f2-lambda)*axe,"",b2->color());
}


/** Idem que toLocal mais on donne un point dans le repère local pour obtenir ses coordonnées dans le repère du monde (i.e. res= M_W->L*p)
**/

Vector3 Box::toWorld(const Vector3 &p) const {
    Vector3 res;
    Matrix4 mat; // matrice de passage W->L

    mat.setIdentity();
    mat.translate(position());
    mat.rotate(theta()*180.0/M_PI,Vector3(0,0,1));
    res=mat.transformPoint(p);

    return res;
}

/** Transforme les coordonnées d'un point données dans le repère du monde pour les obtenir dans le repère local de la boite
(i.e. res=M_L->W * p avec M_L->W matrice de passage de Local à World)
- la position du centre de la boite est donnée par this->position()
- l'angle de rotation par rapport au centre de la boite est donné par this->theta()
- vous pouvez utiliser le type Matrix4 pour calculer la matrice M_W->L :
  - mat.setIdentity() => affecte à l'identité
  - mat.translate(un Vector3 ou un Vector3) => multiplie à droite par la matrice de translation
  - mat.rotate(un angle (en degrés ! attention à la conversion), un Vector3) => multiplie à droite par la matrice de rotation
  - un Vector3 p'= mat.transform(un Vector3 p) => affecte p' avec mat*p
**/

Vector3 Box::toLocal(const Vector3 &p) const {
    Vector3 res;
    Matrix4 mat;

    mat.setIdentity();
    mat.rotate(-theta()*180.0/M_PI,Vector3(0,0,1));
    mat.translate(-position());
    res=mat.transformPoint(p);
    return res;
}




