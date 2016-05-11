#ifndef OBJLOADER_H
#define OBJLOADER_H

#include <vector>
#include <string>

#include "Vector3.h"
#include "Vector2.h"
#include "Tools.h"


/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

namespace p3d {


class VertexIndexAttrib {
public:
  int _vertex,_normal,_texCoord;
  VertexIndexAttrib() : _vertex(-1),_normal(-1),_texCoord(-1) {}
};

typedef std::vector<int> FaceIndex;

static inline int cycle(int i,int nb) {
    return i-nb*(i/nb-(i<0?1:0));
}


class ObjLoader {
public:
    ObjLoader();
    virtual ~ObjLoader();

    int addVertexIndexAttrib(const VertexIndexAttrib &v);
    void read(const std::string &filename);
    void readInit(const std::string &resourceName, const p3d::Vector3 &mini=p3d::Vector3(-1,-1,-1), const p3d::Vector3 &maxi=p3d::Vector3(1,1,1));
    void scaleInBox(double left, double right, double top, double bottom,double znear,double zfar);
    void scaleInBoxMin(double left, double right, double bottom, double top, double znear, double zfar);


    void check();
    void computeNormal();
    void computeTexCoord();
    void computeNormalFace(unsigned int i);
    void triangulate();

    inline const Vector3 &position(unsigned int i) const {return _positionOBJ[_vertexIndexAttrib[i]._vertex];}
    inline const Vector3 &position(unsigned int i,unsigned int j) const {return position(_faceIndex[i][j]);}
    inline const Vector3 &normal(unsigned int i) const {return _normalOBJ[_vertexIndexAttrib[i]._normal];}
    inline const Vector2 &texCoord(unsigned int i) const {return _texCoordOBJ[_vertexIndexAttrib[i]._texCoord];}
    inline const Vector3 &normalFace(unsigned int i) const {return _normalFace[i];}
    inline int indexVertex(unsigned int i,unsigned int j) const {return _faceIndex[i][j];}


    inline unsigned int nbVertex() const {return _vertexIndexAttrib.size();}
    inline unsigned int nbVertex(unsigned int i) const {return _faceIndex[i].size();}
    inline unsigned int nbVertexFace(unsigned int i) const {return _faceIndex[i].size();}
    inline unsigned int nbFace() const {return _faceIndex.size();}

    void translate(double x, double y, double z); // modifies data
    void rotateX(double angle); // modifies data
    void rotateY(double angle); // modifies data
    void readPrepare(const std::string &resourceName);
    void scaleInBoxMin(const p3d::Vector3 &mini, const p3d::Vector3 &maxi);
protected: // protected only for easier coding of the answers (more visibility of the internal structure)
    std::vector<p3d::Vector3> _positionOBJ; //! x,y,z of a vertex
    std::vector<p3d::Vector3> _normalOBJ; //! x,y,z of a normal (one normal per vertex)
    std::vector<p3d::Vector2> _texCoordOBJ; //! s,t of a texCoord (one texCoord per vertex)
    std::vector<VertexIndexAttrib> _vertexIndexAttrib; //! each vertex contains indexes to _vertexOBJ,_normalOBJ,_texCoordOBJ
    std::vector<p3d::Vector3> _normalFace; //! x,y,z of a normal (one normal per face)
    std::vector<FaceIndex> _faceIndex; //! _face[i][j] returns the index (relative to the array _vertex) of the j-ieme vertex of the i-ieme face

    std::vector<std::vector<int> > _duplicateVertex; //! [i][j] gives the index (for _vertexIndexAttrib) of the j-th duplicated attrib vertex of the i-th vertex (overhead but useful)

    p3d::Vector3 _aabbMin,_aabbMax;
};
}

#endif // OBJLOADER_H

