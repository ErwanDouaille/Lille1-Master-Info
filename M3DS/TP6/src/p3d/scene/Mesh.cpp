#include "Mesh.h"

#include "MeshGL.h"
#include "Vector2.h"
#include "Vector3.h"
#include "Tools.h"
#include "Matrix4.h"
#include <fstream>

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

using namespace std;
using namespace p3d;

Mesh::~Mesh() {
  delete _render;
}

Mesh::Mesh() {
  _positionVertex.clear();
  _normalVertex.clear();
  _texCoordVertex.clear();

  _vertexIndexFace.clear();

  _normalFace.clear();
  _render=new MeshGL(this);
}



void Mesh::initDraw() {
  _render->initBuffer();
}

void Mesh::draw() {
  _render->draw();
}

void Mesh::check() {
  cout << "nb position = " << _positionVertex.size() << endl;
  cout << "nb faces = " << _vertexIndexFace.size() << endl;
  for(auto i=_vertexIndexFace.begin();i!=_vertexIndexFace.end();i++) {
    cout << "(";
    VertexFace::iterator j=(*i).begin();
    cout << "v " << j->_position << "n " << j->_normal;
    j++;
    cout << ")" << endl;
  }
}

void Mesh::readInit(const string &resourceName) {
  read(resourceName);
  triangulate();
  scaleInBoxMin(-1,1,-1,1,-1,1);
  if (_normalVertex.empty()) {cout << "no normal in OBJ file => per vertex average is computed" << endl;computeNormal();}
  if (_texCoordVertex.empty()) {cout << "no texCoord in OBJ file => set to (0,0)" << endl;computeTexCoord();}
}


void Mesh::read(const string &resourceName) {
  QFileInfo resource=p3d::resourceFile(resourceName);
  fstream file;
  file.open(resource.filePath().toStdString().c_str(),ios::in);
  file.width(20);
  if (!file.is_open()) {
    throw Error("cant load file "+resource.filePath().toStdString(),__LINE__,__FILE__);
  }

  char s[200]; // dummy to read non-interpreted line
  char read[30];
  char c;
  double x,y,z;
  unsigned int indexPosition,indexTexture,indexNormal;
  VertexFace face;
  VertexAttrib vertexAttrib;

  bool readingFace=false;
  while (!file.eof()) {
    file.clear();
    file >> read;
    if (string(read).compare("v")==0) {
      file >> x >> y >> z;
      _positionVertex.push_back(Vector3(x,y,z));
      continue;
    }
    if (string(read).compare("vn")==0) {
      file >> x >> y >> z;
      _normalVertex.push_back(Vector3(x,y,z));
      continue;
    }
    if (string(read).compare("vt")==0) {
      file >> x >> y;
      _texCoordVertex.push_back(Vector2(x,y));
      continue;
    }

    if (string(read).compare("f")==0) {
      face.clear();
      readingFace=true;
      while (readingFace) {
        file >> indexPosition;
        if (!file.fail()) {
          vertexAttrib._position=indexPosition-1; // starts at index 1 in obj file so -1 for internal arrays
          file >> c;
          if (!file.fail()) {
            if (c=='/') {
              file >> indexTexture;
              if (file.fail()) {
                file.clear();
              }
              else vertexAttrib._texCoord=indexTexture-1;
              file >> c;
              if (!file.fail()) {
                if (c=='/') {
                  file >> indexNormal;
                  vertexAttrib._normal=indexNormal-1;
                }
                else file.putback(c);
              }
            }
            else file.putback(c);
          }
          face.push_back(vertexAttrib);
        }
        else {
          readingFace=false;
        }
      }
      _vertexIndexFace.push_back(face);
      continue;
    }
    file.getline(s,200);
  }
  file.close();
}


void Mesh::computeNormalFace(unsigned int i) {
  Vector3 s1;
  Vector3 s2;
  Vector3 s3;
  Vector3 n;

  double dist=0;

  unsigned int v1=0;
  unsigned int v2=1;
  unsigned int v3=2;
  bool stop=false;
  bool normalOk=false;
  while (!normalOk && !stop) {
    s1=positionVertex(i,v1);
    s2=positionVertex(i,v2);
    s3=positionVertex(i,v3);
    Vector3 u1(s1,s2);
    Vector3 u2(s2,s3);
    n.setCross(u1,u2);
    dist=n.length();
    if (dist>1e-05) {
      normalOk=true;
    }
    else {
      v3++;
      if (v3==nbVertex(i)) {
        v2++;
        v3=v2+1;
        if (v3==nbVertex(i)) {
          v1++;
          v2=v1+1;
          v3=v2+1;
        }
      }
      if (v3==nbVertex(i)) {
        stop=true;
//        throw Error("Normal problem",__LINE__,__FILE__);
      }
    }
  }

  if (stop) {
    n.set(0.0,0.0,0.0);
  }
  else  n.scale(1.0/dist);
  _normalFace[i]=n;
}

void Mesh::computeNormalFace() {
  _normalFace.resize(nbFace());
  for(unsigned int i=0;i<nbFace();++i) {
    computeNormalFace(i);
  }
}


void Mesh::computeNormal() {
  computeNormalFace();
  _normalVertex.resize(nbPosition());
  vector<unsigned int> nbFaceVertex; //nbFaceVertex[i] = nbFace around vertex i
  nbFaceVertex.resize(nbPosition());
  for(unsigned int i=0;i<nbPosition();++i) {
    _normalVertex[i].set(0,0,0);
    nbFaceVertex[i]=0;
  }
  for(unsigned int i=0;i<nbFace();++i) {
    for(unsigned int j=0;j<nbVertex(i);++j) {
      _vertexIndexFace[i][j]._normal=_vertexIndexFace[i][j]._position;
    }
  }
  for(unsigned int i=0;i<nbFace();++i) {
    for(unsigned int j=0;j<nbVertex(i);++j) {
//      cout << "normal face = " << _normalFace[i] << endl;
      _normalVertex[_vertexIndexFace[i][j]._normal]+=_normalFace[i];
      nbFaceVertex[_vertexIndexFace[i][j]._normal]++;
    }
  }
  for(unsigned int i=0;i<nbNormal();++i) {
    _normalVertex[i]/=nbFaceVertex[i];
//    cout << "normal[i] = " << _normal[i] << endl;
  }
}

void Mesh::computeTexCoord() {
  _texCoordVertex.push_back(Vector2(0,0));
  for(unsigned int i=0;i<nbFace();++i) {
    for(unsigned int j=0;j<nbVertex(i);++j) {
      _vertexIndexFace[i][j]._texCoord=0;
    }
  }
}

void Mesh::scaleInBox(double left,double right,double bottom,double top,double znear,double zfar) {
  Vector3 mini(_positionVertex[0]);
  Vector3 maxi(_positionVertex[0]);

  for(unsigned int i=1;i<nbPosition();++i) {
    mini.setMinCoordinate(_positionVertex[i]);
    maxi.setMaxCoordinate(_positionVertex[i]);
  }
  for(unsigned int i=0;i<nbPosition();++i) {
    _positionVertex[i].x((_positionVertex[i].x()-mini.x())/(maxi.x()-mini.x())*(right-left)+left);
    _positionVertex[i].y((_positionVertex[i].y()-mini.y())/(maxi.y()-mini.y())*(top-bottom)+bottom);
    _positionVertex[i].z((_positionVertex[i].z()-mini.z())/(maxi.z()-mini.z())*(zfar-znear)+znear);
  }
}

void Mesh::scaleInBoxMin(double left,double right,double bottom,double top,double znear,double zfar) {
  Vector3 mini(_positionVertex[0]);
  Vector3 maxi(_positionVertex[0]);

  for(unsigned int i=1;i<_positionVertex.size();i++) {
    mini.setMinCoordinate(_positionVertex[i]);
    maxi.setMaxCoordinate(_positionVertex[i]);
  }

  Vector3 diag(mini,maxi);
  unsigned int which;
  double scale=diag.max(&which);
  scale=Vector3(right-left,top-bottom,zfar-znear)(which)/scale;


  for(unsigned int i=0;i<nbPosition();i++) {
    _positionVertex[i].x((_positionVertex[i].x()-mini.x())*scale+left+((right-left)-(maxi.x()-mini.x())*scale)/2.0);
    _positionVertex[i].y((_positionVertex[i].y()-mini.y())*scale+bottom+((top-bottom)-(maxi.y()-mini.y())*scale)/2.0);
    _positionVertex[i].z((_positionVertex[i].z()-mini.z())*scale+znear+((zfar-znear)-(maxi.z()-mini.z())*scale)/2.0);
  }
}

void Mesh::triangulate() {
  unsigned int nb=nbFace();
  for(unsigned int i=0;i<nb;i++) {
    if (nbVertex(i)>3) {
      VertexFace add;
      for(unsigned int j=0;j<nbVertex(i)-3;++j) {
        add.clear();
        add.push_back(_vertexIndexFace[i][0]);
        add.push_back(_vertexIndexFace[i][j+2]);
        add.push_back(_vertexIndexFace[i][j+3]);
        _vertexIndexFace.push_back(add);
      }

      _vertexIndexFace[i].erase(_vertexIndexFace[i].begin()+3,_vertexIndexFace[i].end());
    }
  }
}

void Mesh::rotateY(double angle) {
  Matrix4 trans;
  trans.setIdentity();
//  trans.translate((_aabbMax+_aabbMin)/2.0);
  trans.rotate(angle,Vector3(0,1,0));
//  trans.translate(-(_aabbMax+_aabbMin)/2.0);
  for(unsigned int i=0;i<_positionVertex.size();++i) {
    trans.transformPoint(&_positionVertex[i]);
  }

  for(unsigned int i=0;i<nbNormal();++i) {
    trans.transformDirection(_normalVertex[i]); // incorrect in general case (eg scale).
  }
}


