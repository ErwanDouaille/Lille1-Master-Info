#ifndef CSGTREE_H_INCLUDED
#define CSGTREE_H_INCLUDED

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

#include "Line.h"
#include "Matrix4.h"
#include "Material.h"
#include "Ray.h"
#include "Matrix3.h"


/**
@class CsgTree
@brief opérations sur les csg
*/

class Primitive;
class IntersectionArray;
class CsgTree {

public:
  typedef enum {Node_Intersection,Node_Union,Node_Difference,Node_Leaf} ENode;
    /// constructeur par défaut
  CsgTree();
  /// destructeur
  ~CsgTree();

    /// constructeur par champ pour un noeud interne
  CsgTree(const p3d::Matrix4 &trans,CsgTree *left,CsgTree *right,ENode op);
  /// constructeur par champ pour une feuille
    CsgTree(const p3d::Matrix4 &trans,Primitive *p);

    /// Calcul de la normale au point P d'une feuille
    p3d::Vector3 computeNormalLeaf(const p3d::Vector3 &p);


    inline const p3d::Matrix4 &matrix() const {return _parent2Node;}


    void pointNode2Root(p3d::Vector3 *p);
    void directionNode2Root(p3d::Vector3 *p);
    void pointRoot2Node(p3d::Vector3 *p);
    void directionRoot2Node(p3d::Vector3 *p);
    void normalNode2Root(p3d::Vector3 *p);

    Primitive *primitive() const;


    bool isLeaf() const;

    /// prepare le csg (les feuilles contiendront le passage de racine à feuille et inverse
    void cumulateMatrix();
    void composeMatrix(const p3d::Matrix4 &root2Node,const p3d::Matrix4 &node2Root);


    static void cumulateMatrix(CsgTree *t,const p3d::Matrix4 &root2Parent,const p3d::Matrix4 &parent2Root);

    void root2Node(const p3d::Matrix4 &m) {_root2Node=m;}
    void node2Root(const p3d::Matrix4 &m) {_node2Root=m;}
    void normalNode2Root(const p3d::Matrix3 &m) {_normalNode2Root=m;}

    inline const p3d::Matrix4 &parent2Node() const {return _parent2Node;}
    inline const p3d::Matrix4 &node2Parent() const {return _node2Parent;}

    inline const p3d::Matrix4 &root2Node() const {return _root2Node;}
    inline const p3d::Matrix4 &node2Root() const {return _node2Root;}

    CsgTree* createNode(const p3d::Matrix4 &trans,CsgTree *left,CsgTree *right,ENode op);
    CsgTree* createLeaf(const p3d::Matrix4 &trans,Primitive *p);

    inline CsgTree *left() const {return _left;}
    inline CsgTree *right() const {return _right;}
    inline ENode nodeType() const {return _nodeType;}

    void setNode(const p3d::Matrix4 &trans,CsgTree *left,CsgTree *right,ENode op);
    void setLeaf(const p3d::Matrix4 &trans,Primitive *p);

    inline void left(CsgTree *left) {_left=left;}
    inline void right(CsgTree *right) {_right=right;}
    inline void primitive(Primitive *p) {_primitive=p;_nodeType=Node_Leaf;}
    inline void nodeType(ENode op) {_nodeType=op;}
    inline void matrix(const p3d::Matrix4 &f) {_parent2Node=f;_node2Parent.invert(f);}

    void print();


    static void draw(CsgTree *t);
    void drawLocal();
    void drawParent();

    void intersection(const Ray &ray, IntersectionArray *res);
private:
    p3d::Matrix4 _parent2Node;
    p3d::Matrix4 _root2Node;

    p3d::Matrix4 _node2Parent;
    p3d::Matrix4 _node2Root;

    p3d::Matrix3 _normalNode2Root;

    CsgTree *_right,*_left;
    Primitive *_primitive;
    CsgTree::ENode _nodeType;

};

// for dependance compilation
#include "Primitive.h"
#include "IntersectionArray.h"

#endif // CSGTREE_H_INCLUDED

