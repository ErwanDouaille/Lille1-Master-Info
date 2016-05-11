#ifndef Mesh_H
#define Mesh_H

#include <vector>
#include <string>
#include "Object3D.h"
#include "Vector2.h"
#include "Vector3.h"


/*!
*
* @file Simple OBJ Loader :
* - a face is an array of triplet of indices
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
  int _positionIndex,_normalIndex,_texCoordIndex;
  VertexAttrib() : _positionIndex(-1),_normalIndex(-1),_texCoordIndex(-1) {}
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

    inline const Vector3 &positionOBJ(unsigned int i) const {return _positionOBJ[i];}
    inline const Vector3 &normalOBJ(unsigned int i) const {return _normalOBJ[i];}
    inline const Vector2 &texCoordOBJ(unsigned int i) const {return _texCoordOBJ[i];}

    inline const Vector3 &normalFace(unsigned int i) const {return _normalFace[i];}

    inline const VertexAttrib &vertexIndex(unsigned int i,unsigned int j) const {return _vertexIndexFace[i][cycle(j,nbVertex(i))];}
    inline int positionIndex(unsigned int i,unsigned int j) const {return vertexIndex(i,j)._positionIndex;}
    inline const Vector3 &positionVertex(unsigned int i,unsigned int j) const {return positionOBJ(positionIndex(i,j));}
    inline const Vector3 &normalVertex(unsigned int i,unsigned int j) const {return normalOBJ(vertexIndex(i,j)._normalIndex);}
    inline const Vector2 &texCoordVertex(unsigned int i,unsigned int j) const {return texCoordOBJ(vertexIndex(i,j)._texCoordIndex);}

    inline unsigned int nbPosition() const {return _positionOBJ.size();}
    inline unsigned int nbNormal() const {return _normalOBJ.size();}
    inline unsigned int nbTexCoord() const {return _texCoordOBJ.size();}
    inline unsigned int nbVertex(unsigned int i) const {return _vertexIndexFace[i].size();}
    inline unsigned int nbFace() const {return _vertexIndexFace.size();}

    void draw();
    void drawNormal(double l);
    void initDraw();

    MeshGL *render() {return _render;}

    void rotateY(double angle);
protected: // protected only for easier coding of the answers (more visibility of the internal structure)
    std::vector<p3d::Vector3> _positionOBJ; //! x,y,z of a vertex (read from OBJ)
    std::vector<p3d::Vector3> _normalOBJ; //! x,y,z of a normal  (read from OBJ)
    std::vector<p3d::Vector2> _texCoordOBJ; //! s,t of a texCoord (read from OBJ)
    std::vector<p3d::Vector3> _normalFace; //! x,y,z of a normal (one normal per face)
    std::vector<VertexFace> _vertexIndexFace; //! _vertexIndexFace[i][j] returns the VertexAttrib of the j-ieme vertex of the i-ieme face


    MeshGL *_render;
};
}

#endif // Mesh_H

