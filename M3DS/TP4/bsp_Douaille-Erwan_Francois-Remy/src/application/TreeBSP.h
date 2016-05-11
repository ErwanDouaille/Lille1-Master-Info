#ifndef INC_TREEBSP_H
#define INC_TREEBSP_H

#include "FaceBSP.h"
#include <string>

/**
@file
@author F. Aubert
@brief
*/

/**
@class TreeBSP
@brief
*/
class TreeBSP {
  /// les sous arbres gauche et droit
  TreeBSP *_left,*_right;
  /// la facette pivot
  FaceBSP *_face;
public:
	/// destructeur
  virtual ~TreeBSP();
	/// constructeur par défaut
  TreeBSP();
	/// constructeur en affectant chacun des champs
  TreeBSP(FaceBSP *f,TreeBSP *g,TreeBSP *d);

	/// affectation de chacun des champs
  void set(FaceBSP *f,TreeBSP *g,TreeBSP *d);
	/// affecte la facette pivot avec f
	void node(FaceBSP *f);
	/// affecte le noeud gauche
  void left(TreeBSP *g);
	/// affecte le noeud droit
  void right(TreeBSP *d);
	/// donne la facette du noeud
	FaceBSP *node();
	/// donne le sous arbre gauche
  TreeBSP *left();
	/// donne le sous arbre droit
  TreeBSP *right();
	/// trace le BSP selon technique du peintre
  void draw(const p3d::Vector3 &obs);
	/// compte le nombre de faces
	int nbFace();
};



#endif

