#ifndef LAMBDA_H_INCLUDED
#define LAMBDA_H_INCLUDED

class CsgTree;
#include "Ray.h"
#include "Material.h"


/***
@file
@author Fabrice Aubert
@brief Représentation d'une intersection (P=A+lambda u avec référence sur la primitive)
**/


class Intersection {
    CsgTree *_node; // feuille de l'arbre
    double _lambda;
    Ray _incident;
    p3d::Vector3 _normal;
    p3d::Vector3 _point;

    public:
    Intersection(double lambda);
    Intersection();
    virtual ~Intersection();


    Ray computeReflectRay() const;
    Ray computeRefractRay() const;
    void setEmpty();
    bool isEmpty() const;
    const p3d::Material &material() const;
    double lambda() const;
    void lambda(double k);
    void node(CsgTree *n);
    const CsgTree *node() const;

    inline void incident(const Ray &r) {_incident=r;}
    inline const Ray &incident() const {return _incident;}

    inline void normal(const p3d::Vector3 &n) {_normal=n;}
    inline const p3d::Vector3 &normal() const {return _normal;}

    inline void point(const p3d::Vector3 &p) {_point=p;}
    inline const p3d::Vector3 &point() const {return _point;}

    void computePointNormal();
};



#endif // LAMBDA_H_INCLUDED

