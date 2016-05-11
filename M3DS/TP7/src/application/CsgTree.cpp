#include "CsgTree.h"
#include <iostream>
#include <math.h>
#include "CsgTree.h"
#include "Intersection.h"
#include "Primitive.h"
#include "Matrix3.h"
#include "GLTool.h"
/**



@author F. Aubert
*/


using namespace std;
using namespace p3d;


/**
effectue l'intersection d'une droite et de l'arbre CSG
CU : ray doit être une droite dans le repère de la scène (i.e. dans le repère du noeud racine du CSG)
*/
void CsgTree::intersection(const Ray &ray,IntersectionArray *res) {
  res->clear(); // liste des intersections
  if (isLeaf()) { // intersection du rayon avec une primitive (= une feuille de l'arbre csg)
    // on exprime le rayon dans le repere de la primitive
    Ray ray_leaf;
    ray_leaf.point(node2Root().transformPoint(ray.point()));
    ray_leaf.direction(node2Root().transformDirection(ray.direction()));
    // on calcule l'intersection du rayon avec la primitive
    primitive()->intersection(ray_leaf,res);
    if (!res->empty()) {
      // on affecte aux intersections trouvées les informations utiles (noeud intersecté, rayon incident, ...)
      for(IntersectionArray::iterator i=res->begin();i!=res->end();i++) {
        (*i)->node(this);
        (*i)->incident(ray);
      }
    }

    } else { // intersection du rayon avec un noeud interne => on doit faire la fusion entre les intersections du sous-arbre gauche et les intersections du sous-arbre droit
        IntersectionArray inter_left;
        IntersectionArray inter_right;
        // appels résursifs pour obtenir la liste des intersections à gauche et à droite
        _left->intersection(ray,&inter_left);
        _right->intersection(ray,&inter_right);
        // faire la fusion des listes d'intersections gauche et droite (la fusion dépendra de l'opération Node_Intersection, Node_Difference, ou Node_Union pour le noeud).
        res->fusion(inter_left,inter_right,nodeType());
    }
}


/** *********************************************************************** */

CsgTree::~CsgTree() {
    delete _right;
    delete _left;
    delete _primitive;
}


CsgTree::CsgTree() {
    _right=NULL;
    _left=NULL;
    _primitive=NULL;
    _nodeType=Node_Leaf;
}

CsgTree::CsgTree(const Matrix4 &m,CsgTree *left,CsgTree *right,ENode op) {
    this->setNode(m,left,right,op);
}

CsgTree::CsgTree(const Matrix4 &m,Primitive *p) {
    this->setLeaf(m,p);
}


Vector3 CsgTree::computeNormalLeaf(const Vector3 &p) {
    if (!isLeaf()) throw ErrorD("only compute normal on a leaf");
    return _primitive->computeNormal(p);
}



void CsgTree::pointNode2Root(Vector3 *p) {
    _root2Node.transformPoint(p);
}

void CsgTree::directionNode2Root(Vector3 *p) {
    _root2Node.transformDirection(p);
}

void CsgTree::pointRoot2Node(Vector3 *p) {
    _node2Root.transformPoint(p);
}

void CsgTree::directionRoot2Node(Vector3 *p) {
    _node2Root.transformDirection(p);
}

void CsgTree::normalNode2Root(Vector3 *p) {
  if (!isLeaf()) throw ErrorD("nomal matrix only computed for leaf");
  _normalNode2Root.transform(p);
}

Primitive *CsgTree::primitive() const {
    if (!isLeaf()) throw ErrorD("get primitive on a non-primitive");
    return _primitive;
}



void CsgTree::draw(CsgTree *t) {
  if (t->isLeaf()) {
    p3d::modelviewMatrix.push();
    p3d::modelviewMatrix*=t->root2Node();
    t->primitive()->drawGL();
    p3d::modelviewMatrix.pop();
  }
  else {
    draw(t->left());
    draw(t->right());
  }
}

void CsgTree::drawLocal() {
    draw(this);
}



void CsgTree::drawParent() {
    p3d::modelviewMatrix.push();
    drawLocal();
    p3d::modelviewMatrix.pop();
}

bool CsgTree::isLeaf() const {
    return _nodeType==Node_Leaf;
}


void CsgTree::setNode(const Matrix4 &m,CsgTree *left,CsgTree *right,ENode op) {
    _parent2Node=m;
    _node2Parent.invert(_parent2Node);
    this->left(left);
    this->right(right);
    this->primitive(NULL);
    this->nodeType(op);
}

void CsgTree::setLeaf(const Matrix4 &m,Primitive *p) {
    _parent2Node=m;
    _node2Parent.invert(_parent2Node);
    this->left(NULL);
    this->right(NULL);
    this->primitive(p);
    this->nodeType(Node_Leaf);
}


void CsgTree::print() {
    if (isLeaf()) {
        cout << "Primitive";
    } else {
        cout << "<";
        if (_left) left()->print(); else cout << "VIDE";
        cout << ",";
        if (_right) right()->print(); else cout << "VIDE";
        cout << ">";
    }
}



void CsgTree::cumulateMatrix() {
  Matrix4 identity;
  identity.setIdentity();
  cumulateMatrix(this,identity,identity);
}

void CsgTree::composeMatrix(const Matrix4 &root2Parent,const Matrix4 &parent2Root) {
    _root2Node.mul(root2Parent,_parent2Node);
    _node2Root.mul(_node2Parent,parent2Root);
}

void CsgTree::cumulateMatrix(CsgTree *t,const Matrix4 &root2Parent,const Matrix4 &parent2Root) {
  if (t!=NULL) {
    t->composeMatrix(root2Parent,parent2Root);
    cumulateMatrix(t->right(),t->root2Node(),t->node2Root());
    cumulateMatrix(t->left(),t->root2Node(),t->node2Root());
    if (t->isLeaf()) {
      t->normalNode2Root(t->root2Node().normalMatrix());
    }
  }
}













