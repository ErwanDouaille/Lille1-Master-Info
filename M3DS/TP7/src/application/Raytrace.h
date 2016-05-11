#ifndef RAYTRACE_H_INCLUDED
#define RAYTRACE_H_INCLUDED

#include "Vector3.h"
#include "Ray.h"
#include "CsgTree.h"
#include "Intersection.h"
#include "Scene.h"
#include <QThread>
#include <QImage>




class Raytrace : public QThread {
public:
  virtual ~Raytrace();
  Raytrace();
  Raytrace(unsigned int width,unsigned int height);

    void close();
    void run();
    inline void scene(Scene *s) {_scene=s;}

    QImage *image() {return _image;}

private:
    Scene *_scene;
    QImage *_image;
    p3d::Camera _camera;
    unsigned int _width,_height;
    bool _stopRequest;

    // boucle principale (calcul pour chaque pixel)
    void computeImage();

    // calcul de la couleur portée par un rayon (récursif si objets réfléchissant ou réfractant)
    p3d::Vector3 computeRayColor(const Ray &ray, int level, double contribution);

    // calcule de la couleur par le modèle de Phong en un point d'intersection
    p3d::Vector3 computeLocalColor(const Intersection &inter);


 };


#endif // RAYTRACE_H_INCLUDED

