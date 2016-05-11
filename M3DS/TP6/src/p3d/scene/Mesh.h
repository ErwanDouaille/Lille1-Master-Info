#ifndef Mesh_H
#define Mesh_H

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
class MeshGL;


class VertexAttrib {
public:
  int _position,_normal,_texCoord;
  VertexAttrib() : _position(-1),_normal(-1),_texCoord(-1) {}
};

typedef std::vector<VertexAttrib> VertexFace;

static inline int cycle(int i,int nb) {
    return (i%nb+nb)%nb;
}


class Mesh {
public:
    Mesh();
    virtual ~Mesh();

    void read(const std::string &filename);
    void readInit(const std::string &resourceName);
    void scaleInBox(double left, double right, double top, double bottom,double znear,double zfar);
    void scaleInBoxMin(double left, double right, double bottom, double top, double znear, double zfar);

    void check();
    void computeNormal();
    void computeTexCoord();
    void computeNormalFace();
    void computeNormalFace(unsigned int i);
    void triangulate();

    inline const Vector3 &positionVertex(unsigned int i) const {return _positionVertex[i];}
    inline const Vector3 &normalVertex(unsigned int i) const {return _normalVertex[i];}
    inline const Vector2 &texCoordVertex(unsigned int i) const {return _texCoordVertex[i];}

    inline const Vector3 &normalFace(unsigned int i) const {return _normalFace[i];}

    inline const VertexAttrib &vertexIndex(unsigned int i,unsigned int j) const {return _vertexIndexFace[i][cycle(j,nbVertex(i))];}
    inline int positionIndex(unsigned int i,unsigned int j) const {return vertexIndex(i,j)._position;}
    inline const Vector3 &positionVertex(unsigned int i,unsigned int j) const {return positionVertex(positionIndex(i,j));}
    inline const Vector3 &normalVertex(unsigned int i,unsigned int j) const {return normalVertex(vertexIndex(i,j)._normal);}
    inline const Vector2 &texCoordVertex(unsigned int i,unsigned int j) const {return texCoordVertex(vertexIndex(i,j)._texCoord);}

    inline unsigned int nbPosition() const {return _positionVertex.size();}
    inline unsigned int nbNormal() const {return _normalVertex.size();}
    inline unsigned int nbTexCoord() const {return _texCoordVertex.size();}
    inline unsigned int nbVertex(unsigned int i) const {return _vertexIndexFace[i].size();}
    inline unsigned int nbFace() const {return _vertexIndexFace.size();}

    void draw();
    void initDraw();

    MeshGL *render() {return _render;}

    void rotateY(double angle);
protected: // protected only for easier coding of the answers (more visibility of the internal structure)
    std::vector<p3d::Vector3> _positionVertex; //! x,y,z of a vertex
    std::vector<p3d::Vector3> _normalVertex; //! x,y,z of a normal (one normal per vertex)
    std::vector<p3d::Vector3> _normalFace; //! x,y,z of a normal (one normal per face)
    std::vector<p3d::Vector2> _texCoordVertex; //! s,t of a texCoord (one texCoord per vertex)
    std::vector<VertexFace> _vertexIndexFace; //! _vertexFace[i][j] returns the VertexAttrib of the j-ieme vertex of the i-ieme face


    MeshGL *_render;
};
}

#endif // Mesh_H

