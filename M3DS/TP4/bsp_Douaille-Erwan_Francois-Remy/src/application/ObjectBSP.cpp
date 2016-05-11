/**
Objet représenté sous forme BSP
(container : liste de faces, sommets, arbre)

@author Fabrice Aubert
*/


#include "ObjectBSP.h"
#include "FaceBSP.h"
#include <iostream>
#include <algorithm>
#include "GLTool.h"
#include "Mesh.h"

using namespace p3d;
using namespace std;



/// Construction d'un arbre BSP selon une liste de facettes (statique et récursif)
TreeBSP *ObjectBSP::consBSP(const vector<FaceBSP *> &tabFace) {
  /// Il s'agit de prendre un pivot dans tabFace, et de séparer l'ensemble des autres faces dans listeNegative et listePositive.
  /// puis on récursive en construisant l'arbre.
  /// Les setters de ArbreBSP sont node(une_face), left(un_arbre), right(un_arbre)
  /// Après un appel à une_face1->separe(une_face2), vous récupérez les faces qui y sont construites par
  ///     une_face1->facePositive() et une_face1->faceNegative() vous donneront les faces (éventuellement NULL)
  /// Remarque : l'appel initial de cette récursivité est fait par la méthode ObjetBSP::createBSP()

  TreeBSP *res=NULL;
  vector<FaceBSP *> listeNegative,listePositive;
  listeNegative.clear();
  listePositive.clear();

  if (tabFace.empty()) {
    return NULL;
  } else {
    res=new TreeBSP();
    // TODO : A COMPLETER (construire listeNegative et listePositive; mettre le pivot comme noeud de l'arbre res)
    res->node(tabFace[0]);
    for (int i = 1 ; i < tabFace.size() ; i++){
        tabFace[i]->separe(*tabFace[0]);
        if (tabFace[i]->facePositive() != NULL){
            listePositive.push_back(tabFace[i]->facePositive());
        }

        if (tabFace[i]->faceNegative() != NULL){
            listeNegative.push_back(tabFace[i]->faceNegative());
        }
    }

    // à laisser à la fin : appels récursifs
    res->left(consBSP(listeNegative));
    res->right(consBSP(listePositive));
    return res;
  }
}


/** **************************************************************** */
/** Affichage de l'arbre BSP par le peintre */

/// méthode statique récursive (appel initial fait par ObjetBSP::drawBSP(Const Vector3 &))
void ObjectBSP::drawBSP(TreeBSP *tree,const Vector3 &eye) {
  /// les sélecteurs sont tree->node() (de type FaceBSP *), et tree->left(), tree->right() (de type TreeBSP *)
  /// pour provoquer le tracé d'une face de type FaceBSP * il sufft de faire une_face->addDraw()

  // TODO : à compléter
    if (tree != NULL){
        if (tree->node()->sign(eye) == SIGN_MINUS){
            this->drawBSP(tree->right(), eye);
            tree->node()->addDraw();
            this->drawBSP(tree->left(),eye);
        } else {
            this->drawBSP(tree->left(), eye);
            tree->node()->addDraw();
            this->drawBSP(tree->right(),eye);
        }

    }
}

/** **************************************************************************** */
/** **************************************************************************** */
/** **************************************************************************** */
/** **************************************************************************** */
/** **************************************************************************** */

/// construction de l'arbre BSP (i.e. construction de l'attribut _bsp) : initialisation de la récursivité
/// appel à consBSP.
void ObjectBSP::createBSP() {
  if (!_bsp) delete _bsp;

  // copier toutes les faces de l'objet dans la liste initiale.
  vector<FaceBSP *> listeFace;
  listeFace.clear();
  for(vector<FaceBSP *>::iterator i=_allFace.begin(); i!=_allFace.end(); i++) {
    listeFace.push_back(*i);
  }

  _bsp=ObjectBSP::consBSP(listeFace);

  // A LAISSER ! On efface toutes les faces de la liste initiale (sauf pivot d'indice 0)
  vector<FaceBSP *>::iterator i=_allFace.begin();
  while(i!=_allFace.end()) {
    if ((*i)->isDelete()) suppress(*i);
    else i++;
  }

}


/// Affichage par peintre : init récursivité.
void ObjectBSP::drawBSP(const Vector3 &eye) {
  drawClear();
  glDepthFunc(GL_ALWAYS);
  p3d::materialBlueGreen();
  drawBSP(_bsp,eye);
  drawArrayFill();
}

/** **************************************************************************** */





ObjectBSP::ObjectBSP() {
  _allFace.clear();
  _allVertex.clear();
  _bsp=NULL;
}

FaceBSP *ObjectBSP::createFace() {
  FaceBSP *res;
  res=new FaceBSP(this);
  return res;
}

VertexBSP *ObjectBSP::createVertex(const Vector3 &p) {
  VertexBSP *res;
  res=new VertexBSP(this);
  res->point(p);
  return res;
}



ObjectBSP::~ObjectBSP() {
  for(vector<FaceBSP *>::iterator i=_allFace.begin(); i<_allFace.end(); i++) {
    delete *i;
  }
  for(vector<VertexBSP *>::iterator i=_allVertex.begin(); i<_allVertex.end(); i++) {
    delete *i;
  }
  delete _bsp;
}


void ObjectBSP::read(const string &resourceName) {
//  QFileInfo name=resourceFile(resourceName);
  Mesh *o=new Mesh();
  o->read(resourceName);
  o->scaleInBoxMin(-1.2,0.8,-1,1,-1,1);
  o->computeNormal();

  for(unsigned int i=0; i<o->nbPosition(); i++) {
    VertexBSP *v=createVertex(o->position(i));
    v->normal(o->normal(i)); // only for normal average
  }
  for(unsigned int i=0; i<o->nbFace(); i++) {
    FaceBSP *f=createFace();
    f->normal(o->normalFace(i));
    for(unsigned int j=0; j<o->nbVertex(i); j++) {
      f->add(_allVertex[o->indexPosition(i,j)]);
    }
  }
  delete o;
}

void ObjectBSP::drawNormal() {
    vector<Vector3> pts;
    for(unsigned int i=0;i<nbVertex();++i) {
        pts.push_back(_allVertex[i]->point());
        pts.push_back(_allVertex[i]->point()+_allVertex[i]->normal());
    }
    p3d::ambientColor=Vector4(1,0,0,1);
    p3d::shaderVertexAmbient();
    p3d::drawLines(pts);

}

void ObjectBSP::suppress(FaceBSP *f) {
  vector<FaceBSP *>::iterator a_sup=find(_allFace.begin(),_allFace.end(),f);
  if (a_sup!=_allFace.end()) {
    delete *a_sup;
    _allFace.erase(a_sup);
  }
  else throw Error("suppress a Face ???",__LINE__,__FILE__);
}

void ObjectBSP::add(FaceBSP *f) {
  _allFace.push_back(f);
}

void ObjectBSP::add(VertexBSP *v) {
  _allVertex.push_back(v);
}

void ObjectBSP::drawDepth() {
  p3d::materialFrontBack();
  glDepthFunc(GL_LESS);
  p3d::shaderLightPhong();
  drawGrid();

}


void ObjectBSP::drawClear() {
  _drawNormalFill.clear();
  _drawPtsFill.clear();
  _drawPtsWire.clear();
}

void ObjectBSP::drawArrayGrid() {
  drawArrayFill();
  p3d::fragOffset=0.01;
  p3d::shaderVertexAmbient();
  p3d::uniformAmbient(Vector4(0,0,0,1));
  glLineWidth(2);
  p3d::drawLines(_drawPtsWire);
  p3d::fragOffset=0;


}

void ObjectBSP::drawArrayFill() {
  p3d::materialFrontBack();
  p3d::ambientColor=Vector4(0.1,0.1,0.1,0.5);
  p3d::shaderLightPhong();
  p3d::draw(GL_TRIANGLES,_drawPtsFill,"position",_drawNormalFill,"normal");
}



void ObjectBSP::drawGrid() {
  drawClear();
  for(unsigned int i=0; i<_allFace.size();++i) {
    _allFace[i]->addDraw();
  }
  drawArrayGrid();
}


void ObjectBSP::drawBrut() {
  p3d::materialFrontBack();
  glDepthFunc(GL_ALWAYS);

  drawGrid();
}





