#include "TreeBSP.h"
#include <string>

/**
* @author Fabrice Aubert
*/

using namespace std;
using namespace p3d;



/** ************************************************************** */

TreeBSP::~TreeBSP() {
  if (_left) delete _left;
  if (_right) delete _right;
}

TreeBSP::TreeBSP() {
  _face=NULL;
  _left=NULL;
  _right=NULL;
}

/// constructeur en affectant chacun des champs
TreeBSP::TreeBSP(FaceBSP *f,TreeBSP *g,TreeBSP *d) {
  _face=f;
  _left=g;
  _right=d;
}

/// affectation de chacun des champs
void TreeBSP::set(FaceBSP *f,TreeBSP *g,TreeBSP *d) {
  _face=f;
  _left=g;
  _right=d;
}

/// affecte la facette pivot avec f
void TreeBSP::node(FaceBSP *f) {
  _face=f;
}

/// affecte le noeud gauche
void TreeBSP::left(TreeBSP *g) {
  _left=g;
}

/// affecte le noeud droit
void TreeBSP::right(TreeBSP *d) {
  _right=d;
}

/// donne la facette du noeud
FaceBSP *TreeBSP::node() {
  return _face;
}

/// donne le sous arbre gauche
TreeBSP *TreeBSP::left() {
  return _left;
}

/// donne le sous arbre droit
TreeBSP *TreeBSP::right() {
  return _right;
}

/// nombre de faces
int TreeBSP::nbFace() {
  return 1+ (right()?right()->nbFace():0) + (left()?left()->nbFace():0);
}



