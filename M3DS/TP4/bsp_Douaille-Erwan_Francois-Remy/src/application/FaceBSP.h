#ifndef FACEBSP_H_INCLUDED
#define FACEBSP_H_INCLUDED

#include <vector>
#include "VertexBSP.h"
#include "Vector3.h"

/**
@file
@author Fabrice Aubert
*/

/**
@class Représentation d'une face dans un bsp
*/

class ObjectBSP;

typedef enum {SIGN_PLUS,SIGN_MINUS,SIGN_NONE} ESign;

class FaceBSP {
  std::vector<VertexBSP *> _tabVertex;
  p3d::Vector3 _normal;

  FaceBSP *_facePositive,*_faceNegative;

  ObjectBSP *_owner;

  bool _markDelete;

public:
  virtual ~FaceBSP();
  FaceBSP(ObjectBSP *owner);

  /// donne les coordonnées du sommet i directement
  inline const p3d::Vector3 &point(unsigned int i) const {return _tabVertex[i]->point();}

  /// normale :
  inline const p3d::Vector3 &normal() const {return _normal;}
  inline void normal(const p3d::Vector3 &n) {_normal=n;}

	/// donne le signe du point p par rapport à la facette
	ESign sign(const p3d::Vector3 &p) const;

	inline FaceBSP *facePositive() const {return _facePositive;}
	inline FaceBSP *faceNegative() const {return _faceNegative;}


	inline void markDelete() {_markDelete=true;}
	inline bool isDelete() const {return _markDelete;}

	/// donne le point d'intersection entre la droite (p1p2) et le plan de la facette
	p3d::Vector3 intersection(const p3d::Vector3 &p1,const p3d::Vector3 &p2) const;
	/// separe this selon f
  void separe(const FaceBSP &f);

	///
	VertexBSP *createVertex(const p3d::Vector3 &p);

	FaceBSP *createFace(const std::vector<VertexBSP *> &vertex);
	void createFace(const std::vector<VertexBSP *> &moins,const std::vector<VertexBSP *> &plus);


	void add(VertexBSP *v);

	void draw();

  void copyVertexFrom(const std::vector<VertexBSP *> &liste);
  void addDrawWire();
  void addDrawFill();
  void addDraw();
};



#endif // FACEBSP_H_INCLUDED

