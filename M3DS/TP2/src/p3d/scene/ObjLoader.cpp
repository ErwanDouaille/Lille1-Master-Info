#include "ObjLoader.h"

#include "Vector2.h"
#include "Vector3.h"
#include "Matrix4.h"
#include "Tools.h"
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

ObjLoader::~ObjLoader() {
}

ObjLoader::ObjLoader() {
  _positionOBJ.clear();
  _normalOBJ.clear();
  _texCoordOBJ.clear();

  _vertexIndexAttrib.clear();

  _faceIndex.clear();

  _normalFace.clear();
}



void ObjLoader::check() {
  cout << "nb vertices OBJ = " << _positionOBJ.size() << " nb vertices attrib = " << _vertexIndexAttrib.size() << endl;
  cout << "nb faces = " << _faceIndex.size() << endl;
  for(auto i=_faceIndex.begin();i!=_faceIndex.end();++i) {
    cout << "(";
    FaceIndex::iterator j=(*i).begin();
    cout << "v" << _vertexIndexAttrib[*j]._vertex << "n" << _vertexIndexAttrib[*j]._normal;
    j++;
    for(;j!=(*i).end();j++) {
      cout << ",v" << _vertexIndexAttrib[*j]._vertex << "n" << _vertexIndexAttrib[*j]._normal;
    }
    cout << ")" << endl;
  }
}


void ObjLoader::readInit(const string &resourceName,const Vector3 &mini,const Vector3 &maxi) {
  read(resourceName);
  triangulate();
  scaleInBoxMin(mini,maxi);
  if (_normalOBJ.empty()) {cout << "no normal in OBJ file => per vertex average is computed" << endl;computeNormal();}
  if (_texCoordOBJ.empty()) {cout << "no texCoord in OBJ file => set to (0,0)" << endl;computeTexCoord();}
}

void ObjLoader::readPrepare(const string &resourceName) {
  read(resourceName);
  triangulate();
  if (_normalOBJ.empty()) {cout << "no normal in OBJ file => per vertex average is computed" << endl;computeNormal();}
  if (_texCoordOBJ.empty()) {cout << "no texCoord in OBJ file => set to (0,0)" << endl;computeTexCoord();}
}



int ObjLoader::addVertexIndexAttrib(const VertexIndexAttrib &v) {
  unsigned int i;
  vector<int> &duplicate=_duplicateVertex[v._vertex];
  for(i=0;i<duplicate.size();++i) {
    VertexIndexAttrib &comp=_vertexIndexAttrib[duplicate[i]];
    if (comp._normal==v._normal && comp._texCoord==v._texCoord && comp._vertex==v._vertex) {
      break;
    }
  }
  if (i==duplicate.size()) {
    duplicate.push_back(_vertexIndexAttrib.size());
    _vertexIndexAttrib.push_back(v);
  }
  return duplicate[i];
}

void ObjLoader::read(const string &resourceName) {
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
  unsigned int indexVertex,indexTexture,indexNormal;
  FaceIndex face;
  VertexIndexAttrib fromObj;

  bool readingFace=false;
  while (!file.eof()) {
    file.clear();
    file >> read;
    if (string(read).compare("v")==0) {
      file >> x >> y >> z;
      _positionOBJ.push_back(Vector3(x,y,z));
      _duplicateVertex.push_back(vector<int>());
      continue;
    }
    if (string(read).compare("vn")==0) {
      file >> x >> y >> z;
      _normalOBJ.push_back(Vector3(x,y,z));
      continue;
    }
    if (string(read).compare("vt")==0) {
      file >> x >> y;
      _texCoordOBJ.push_back(Vector2(x,y));
      continue;
    }

    if (string(read).compare("f")==0) {
      face.clear();
      readingFace=true;
      while (readingFace) {
        file >> indexVertex;
        if (!file.fail()) {
          fromObj._vertex=indexVertex-1; // starts at index 1 in obj file so -1 for internal arrays
          file >> c;
          if (!file.fail()) {
            if (c=='/') {
              file >> indexTexture;
              if (file.fail()) {
                file.clear();
              }
              else fromObj._texCoord=indexTexture-1;
              file >> c;
              if (!file.fail()) {
                if (c=='/') {
                  file >> indexNormal;
                  fromObj._normal=indexNormal-1;
                }
                else file.putback(c);
              }
            }
            else file.putback(c);
          }
          face.push_back(addVertexIndexAttrib(fromObj));
        }
        else {
          readingFace=false;
        }
      }
      _faceIndex.push_back(face);
      continue;
    }
    file.getline(s,200);
  }
  file.close();
}


void ObjLoader::computeNormalFace(unsigned int i) {
  Vector3 s1;
  Vector3 s2;
  Vector3 s3;
  Vector3 n;

  double dist=0;

  FaceIndex::iterator it1=_faceIndex[i].begin();
  FaceIndex::iterator it2=_faceIndex[i].begin()+1;
  FaceIndex::iterator it3=_faceIndex[i].begin()+2;
  bool stop=false;
  bool normalOk=false;
  while (!normalOk && !stop) {
    s1=position(*it1);
    s2=position(*it2);
    s3=position(*it3);
    Vector3 v1(s1,s2);
    Vector3 v2(s2,s3);
    n.setCross(v1,v2);
    dist=n.length();
    if (dist>1e-05) {
      normalOk=true;
    }
    else {
      it3++;
      if (it3==_faceIndex[i].end()) {
        it2++;
        it3=it2+1;
        if (it3==_faceIndex[i].end()) {
          it1++;
          it2=it1+1;
          it3=it2+1;
        }
      }
      if (it3==_faceIndex[i].end()) {
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


void ObjLoader::computeNormal() {
  _normalOBJ.resize(_vertexIndexAttrib.size());
  _normalFace.resize(_faceIndex.size());
  vector<unsigned int> nbFace; //nbFace per vertex
  nbFace.resize(_vertexIndexAttrib.size());
  for(unsigned int i=0;i<_vertexIndexAttrib.size();i++) {
    _normalOBJ[i].set(0,0,0);
    nbFace[i]=0;
  }
  for(unsigned int i=0;i<_faceIndex.size();i++) {
    computeNormalFace(i);
  }
  for(unsigned int i=0;i<_faceIndex.size();i++) {
    for(unsigned int j=0;j<_faceIndex[i].size();j++) {
      _normalOBJ[_faceIndex[i][j]]+=_normalFace[i];
      nbFace[_faceIndex[i][j]]++;
    }
  }
  for(unsigned int i=0;i<_vertexIndexAttrib.size();i++) {
    _normalOBJ[i]/=nbFace[i];
    _vertexIndexAttrib[i]._normal=i;
  }
}

void ObjLoader::computeTexCoord() {
  _texCoordOBJ.push_back(Vector2(0,0));
  for(unsigned int i=0;i<nbVertex();i++) {
    _vertexIndexAttrib[i]._texCoord=0;
  }

}


void ObjLoader::scaleInBoxMin(const p3d::Vector3 &mini,const p3d::Vector3 &maxi) {
  scaleInBoxMin(mini.x(),maxi.x(),mini.y(),maxi.y(),mini.z(),maxi.z());
}

void ObjLoader::scaleInBox(double left,double right,double bottom,double top,double znear,double zfar) {
  Vector3 mini(_positionOBJ[0]);
  Vector3 maxi(_positionOBJ[0]);

  for(unsigned int i=1;i<_positionOBJ.size();i++) {
    mini.setMinCoordinate(_positionOBJ[i]);
    maxi.setMaxCoordinate(_positionOBJ[i]);
  }
  for(unsigned int i=0;i<_positionOBJ.size();i++) {
    _positionOBJ[i].x((_positionOBJ[i].x()-mini.x())/(maxi.x()-mini.x())*(right-left)+left);
    _positionOBJ[i].y((_positionOBJ[i].y()-mini.y())/(maxi.y()-mini.y())*(top-bottom)+bottom);
    _positionOBJ[i].z((_positionOBJ[i].z()-mini.z())/(maxi.z()-mini.z())*(zfar-znear)+znear);
  }
  _aabbMin=Vector3(left,bottom,znear);
  _aabbMax=Vector3(right,top,zfar);

}

void ObjLoader::scaleInBoxMin(double left,double right,double bottom,double top,double znear,double zfar) {
  Vector3 mini(_positionOBJ[0]);
  Vector3 maxi(_positionOBJ[0]);

  for(unsigned int i=1;i<_positionOBJ.size();i++) {
    mini.setMinCoordinate(_positionOBJ[i]);
    maxi.setMaxCoordinate(_positionOBJ[i]);
  }

  Vector3 diag(mini,maxi);
  unsigned int which;
  double scale=diag.max(&which);
  scale=Vector3(right-left,top-bottom,zfar-znear)(which)/scale;


  for(unsigned int i=0;i<_positionOBJ.size();i++) {
    _positionOBJ[i].x((_positionOBJ[i].x()-mini.x())*scale+left+((right-left)-(maxi.x()-mini.x())*scale)/2.0);
    _positionOBJ[i].y((_positionOBJ[i].y()-mini.y())*scale+bottom+((top-bottom)-(maxi.y()-mini.y())*scale)/2.0);
    _positionOBJ[i].z((_positionOBJ[i].z()-mini.z())*scale+znear+((zfar-znear)-(maxi.z()-mini.z())*scale)/2.0);
  }
}

void ObjLoader::triangulate() {
  unsigned int nb=nbFace();
  for(unsigned int i=0;i<nb;i++) {
    if (_faceIndex[i].size()>3) {
      FaceIndex add;
      for(unsigned int j=0;j<_faceIndex[i].size()-3;j++) {
        add.clear();
        add.push_back(_faceIndex[i][0]);
        add.push_back(_faceIndex[i][j+2]);
        add.push_back(_faceIndex[i][j+3]);
        _faceIndex.push_back(add);
      }

      _faceIndex[i].erase(_faceIndex[i].begin()+3,_faceIndex[i].end());
    }
  }
}


// **********************************************************************************************
// only to illustrate an exercice (SubBufferData and animation). Do not use this in general case !
void ObjLoader::rotateX(double angle) {
  Matrix4 trans;
  trans.setIdentity();
  trans.translate((_aabbMax+_aabbMin)/2.0);
  trans.rotate(angle,Vector3(1,0,0));
  trans.translate(-(_aabbMax+_aabbMin)/2.0);
  for(unsigned int i=0;i<_positionOBJ.size();++i) {
    trans.transformPoint(_positionOBJ[i]);
  }

  for(unsigned int i=0;i<_normalOBJ.size();++i) {
    trans.transformDirection(_normalOBJ[i]); // incorrect in general case (eg scale).
  }
}

void ObjLoader::rotateY(double angle) {
  Matrix4 trans;
  trans.setIdentity();
  trans.translate((_aabbMax+_aabbMin)/2.0);
  trans.rotate(angle,Vector3(0,1,0));
  trans.translate(-(_aabbMax+_aabbMin)/2.0);
  for(unsigned int i=0;i<_positionOBJ.size();++i) {
    trans.transformPoint(_positionOBJ[i]);
  }

  for(unsigned int i=0;i<_normalOBJ.size();++i) {
    trans.transformDirection(_normalOBJ[i]); // incorrect in general case (eg scale).
  }
}



void ObjLoader::translate(double x,double y,double z) {
  Matrix4 trans;
  trans.setIdentity();
  trans.translate(x,y,z);
  for(unsigned int i=0;i<_positionOBJ.size();++i) {
    trans.transformPoint(_positionOBJ[i]);
  }
}



