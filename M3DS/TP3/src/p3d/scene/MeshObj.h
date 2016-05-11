#ifndef MESHOBJ_H
#define MESHOBJ_H

#include <vector>
#include <string>
#include "Object3D.h"
#include "Vector2.h"
#include "Vector3.h"


/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

namespace p3d {
class Vector2;
class MeshObjGL;


class VertexIndexAttrib {
public:
  int _vertex,_normal,_texCoord;
  VertexIndexAttrib() : _vertex(-1),_normal(-1),_texCoord(-1) {}
};

typedef std::vector<int> FaceIndex;

static inline int cycle(int i,int nb) {
    return (i%nb+nb)%nb;
}


class MeshObj {
public:
    MeshObj();
    virtual ~MeshObj();

    int addVertexIndexAttrib(const VertexIndexAttrib &v);
    void read(const std::string &filename);
    void readInit(const std::string &resourceName);
    void scaleInBox(double left, double right, double top, double bottom,double znear,double zfar);
    void scaleInBoxMin(double left, double right, double bottom, double top, double znear, double zfar);

    void check();
    void computeNormal();
    void computeTexCoord();
    void computeNormalFace(unsigned int i);
    void triangulate();

    inline const Vector3 &vertex(unsigned int i) const {return _vertexOBJ[_vertexIndexAttrib[i]._vertex];}
    inline const Vector3 &vertex(unsigned int i,unsigned int j) const {return vertex(_faceIndex[i][j]);}
    inline const Vector3 &normal(unsigned int i) const {return _normalOBJ[_vertexIndexAttrib[i]._normal];}
    inline const Vector2 &texCoord(unsigned int i) const {return _texCoordOBJ[_vertexIndexAttrib[i]._texCoord];}
    inline const Vector3 &normalFace(unsigned int i) const {return _normalFace[i];}
    inline int indexVertex(unsigned int i,unsigned int j) const {return _faceIndex[i][j];}
    /*
    inline const Vector3 &vertexFace(unsigned int i,unsigned int j) const {return _vertex[indexVertex(i,j)];}

    inline const Vector3 &vertexCycle(unsigned int i) {return _vertex[cycle(i,nbVertex())];}
    inline const Vector3 &normalVertexCycle(unsigned int i) {return _normalVertex[cycle(i,nbVertex())];}
    inline const Vector3 &normalFaceCycle(unsigned int i) {return _normalFace[cycle(i,nbFace())];}
    inline int indexVertexCycle(unsigned int i,unsigned int j) {return _face[cycle(i,nbFace())][cycle(j,nbVertexFace(i))];}
    inline const Vector3 &vertexFaceCycle(unsigned int i,unsigned int j) {return _vertex[indexVertexCycle(i,j)];}
    */

    inline unsigned int nbVertex() const {return _vertexIndexAttrib.size();}
    inline unsigned int nbVertex(unsigned int i) const {return _faceIndex[i].size();}
    inline unsigned int nbVertexFace(unsigned int i) const {return _faceIndex[i].size();}
    inline unsigned int nbFace() const {return _faceIndex.size();}

    void draw();
    void initDraw();

    MeshObjGL *render() {return _render;}

    void rotateY(double angle);
protected: // protected only for easier coding of the answers (more visibility of the internal structure)
    std::vector<p3d::Vector3> _vertexOBJ; //! x,y,z of a vertex
    std::vector<p3d::Vector3> _normalOBJ; //! x,y,z of a normal (one normal per vertex)
    std::vector<p3d::Vector2> _texCoordOBJ; //! s,t of a texCoord (one texCoord per vertex)
//    std::vector<VertexIndexAttrib> _attribOBJ; //! each vertex contains indexes to _vertexOBJ,_normalOBJ,_texCoordOBJ
//    std::vector<FaceIndex> _faceIndexOBJ; //! _faceIndex[i][j] returns the index (relative to the array _attribOBJ) of the j-ieme vertex of the i-ieme face


    std::vector<VertexIndexAttrib> _vertexIndexAttrib; //! each vertex contains indexes to _vertexOBJ,_normalOBJ,_texCoordOBJ
    std::vector<p3d::Vector3> _normalFace; //! x,y,z of a normal (one normal per face)
    std::vector<FaceIndex> _faceIndex; //! _faceIndex[i][j] returns the index (relative to the array _vertexIndexAttrib) of the j-ieme vertex of the i-ieme face

    std::vector<std::vector<int> > _duplicateVertex; //! [i][j] gives the index (for _vertexIndexAttrib) of the j-th duplicated attrib vertex of the i-th vertex (overhead but useful)


    MeshObjGL *_render;
};
}

#endif // MESHOBJ_H

