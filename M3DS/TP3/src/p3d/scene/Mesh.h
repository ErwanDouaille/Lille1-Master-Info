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


class Mesh :public Object3D {
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
    void computeNormalFace(unsigned int i);
    void triangulate();

    inline const Vector3 &position(unsigned int i) const {return _position[i];}
    inline const Vector3 &normal(unsigned int i) const {return _normal[i];}
    inline const Vector2 &texCoord(unsigned int i) const {return _texCoord[i];}

    inline const Vector3 &normalFace(unsigned int i) const {return _normalFace[i];}

    inline const VertexAttrib &vertex(unsigned int i,unsigned int j) const {return _vertexFace[i][cycle(j,nbVertex(i))];}
    inline int indexPosition(unsigned int i,unsigned int j) const {return vertex(i,j)._position;}
    inline const Vector3 &position(unsigned int i,unsigned int j) const {return position(indexPosition(i,j));}
    inline const Vector3 &normal(unsigned int i,unsigned int j) const {return normal(vertex(i,j)._normal);}
    inline const Vector2 &texCoord(unsigned int i,unsigned int j) const {return texCoord(vertex(i,j)._texCoord);}

    inline unsigned int nbPosition() const {return _position.size();}
    inline unsigned int nbNormal() const {return _normal.size();}
    inline unsigned int nbTexCoord() const {return _texCoord.size();}
    inline unsigned int nbVertex(unsigned int i) const {return _vertexFace[i].size();}
    inline unsigned int nbFace() const {return _vertexFace.size();}

    void draw();
    void initDraw();

    MeshGL *render() {return _render;}

    void rotateY(double angle);
protected: // protected only for easier coding of the answers (more visibility of the internal structure)
    std::vector<p3d::Vector3> _position; //! x,y,z of a vertex
    std::vector<p3d::Vector3> _normal; //! x,y,z of a normal (one normal per vertex)
    std::vector<p3d::Vector3> _normalFace; //! x,y,z of a normal (one normal per face)
    std::vector<p3d::Vector2> _texCoord; //! s,t of a texCoord (one texCoord per vertex)
    std::vector<VertexFace> _vertexFace; //! _vertexFace[i][j] returns the VertexAttrib of the j-ieme vertex of the i-ieme face


    MeshGL *_render;
};
}

#endif // Mesh_H

