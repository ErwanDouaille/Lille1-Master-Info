#ifndef SCENE_H
#define SCENE_H

#include <QXmlStreamReader>

#include "Vector3.h"
#include <string>
#include "Camera.h"
#include "CsgTree.h"
#include "Ray.h"
#include "Intersection.h"
#include "Material.h"
#include "Object3D.h"


class Scene : public p3d::Object3D {
  public:
    Scene();
    virtual ~Scene();

    void init();

    const std::string &name() {return _name;}
    void name(const std::string &name) {_name=name;}

    void read(const std::string &name);

    inline void csg(CsgTree *csg) {_csg=csg;}
    inline CsgTree *csg() {return _csg;}

    const p3d::Vector3 &lightPosition(unsigned int i) const;
    void lightPosition(unsigned int i,const p3d::Vector3 &p);
    void addLight(const p3d::Vector3 &p,double intensity);
    double lightIntensity(unsigned int i);

    inline unsigned int nbLight() {return _light.size();}

    inline const p3d::Camera &camera() const {return _camera;}
    inline void camera(const p3d::Camera &cam) {_camera=cam;}

    Intersection *intersection(const Ray &ray,double seuil);

    void drawGL();

    void prepareCsg();



  protected:
    CsgTree *readNode(QXmlStreamReader *elem, p3d::Material currentMat);
  private:
    CsgTree *_csg;
    std::vector<p3d::Vector3> _light;
    std::vector<double> _intensity;

    p3d::Camera _camera;
    std::string _name;
};

#endif // SCENE_H

