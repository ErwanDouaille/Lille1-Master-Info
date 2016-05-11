#ifndef INC_OBJECTBSP_H
#define INC_OBJECTBSP_H

#include "Mesh.h"
#include "TreeBSP.h"
#include "FaceBSP.h"

class ObjectBSP {
private:
  TreeBSP *_bsp;

  std::vector<FaceBSP *> _allFace;
  std::vector<VertexBSP *> _allVertex;


public:
  ObjectBSP();
  virtual ~ObjectBSP();

  void createBSP();
  TreeBSP *consBSP(const std::vector<FaceBSP *> &listeFace);
  void drawBSP(TreeBSP *tree,const p3d::Vector3 &p);

  void drawBSP(const p3d::Vector3 &eye);

  FaceBSP *createFace();
  VertexBSP *createVertex(const p3d::Vector3 &p);

  inline FaceBSP *face(unsigned int i) {return _allFace[i];}

  void add(VertexBSP *v);
  void add(FaceBSP *f);
  void suppress(FaceBSP *f);

  void drawDepth();
  void drawBrut();
  void drawGrid();
  void drawArrayFill();
  void drawArrayGrid();
  void drawNormal();
  void drawClear();

  void read(const std::string &name);

  inline unsigned int nbFace() {return _allFace.size();}
  inline unsigned int nbVertex() {return _allVertex.size();}
  inline TreeBSP *bsp() {return _bsp;}


  std::vector<p3d::Vector3> _drawPtsFill,_drawPtsWire,_drawNormalFill;
};

#endif

