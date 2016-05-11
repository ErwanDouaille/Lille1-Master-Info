#ifndef MATERIAL_H_INCLUDED
#define MATERIAL_H_INCLUDED

#include <string>
#include "Texture.h"
#include "Vector3.h"
#include "Vector4.h"

/***
@file
@author Fabrice Aubert
@brief représentation matériel simple (phong, refraction, reflexion)
**/

namespace p3d {

class Material {
  std::string _name;
  Vector4 _ka;
  Vector3 _kd,_ks;
  double _alpha;
  double _shininess;
  double _refractionCoefficient,_reflexionCoefficient;
  double _refractionIndex;
	Texture *_mapKd;
	public:
	/// constructeur par défaut
	Material();
	/// destructeur
	virtual ~Material();

	///
  inline void ambient(const Vector4 &a) {_ka=a;}
  inline void diffuse(const Vector3 &d) {_kd=d;}
  inline void specular(const Vector3 &s) {_ks=s;}
  inline void shininess(double s) {_shininess=s;}
  inline void alpha(double a) {_alpha=a;}

	///
  inline const Vector4 &ambient() const {return _ka;}
  inline const Vector3 &diffuse() const {return _kd;}
  inline const Vector3 &specular() const {return _ks;}
  inline double shininess() const {return _shininess;}
  inline double alpha() const {return _alpha;}

  std::string name() const {return _name;}
  void name(const std::string &name) {_name=name;}

	///
//	void setDescartes(double n);
  void reflectionCoefficient(double alpha) {_reflexionCoefficient=alpha;}
  void refractionCoefficient(double alpha) {_refractionCoefficient=alpha;}
  void refractionIndex(double m) {_refractionIndex=m;}

  double refractionIndex() const {return _refractionIndex;}

	///
  //double descartes() const;
  double reflectionCoefficient() const {return _reflexionCoefficient;}
  double refractionCoefficient() const {return _refractionCoefficient;}

	void setGL();
  void enableDiffuseMap();

  void diffuseMap(Texture *t);
  Texture *diffuseMap();

  friend std::ostream& operator <<(std::ostream &s,const Material &q);

  void material(const p3d::Vector4 &ambient,const p3d::Vector3 &diffuse, const p3d::Vector3 &specular=p3d::Vector3(0.5,0.5,0.5), double shininess=100.0);
};

}


#endif // MATERIAL_H_INCLUDED

