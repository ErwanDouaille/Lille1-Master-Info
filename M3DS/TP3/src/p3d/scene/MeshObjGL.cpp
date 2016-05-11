#include "MeshObjGL.h"
#include "GLTool.h"
#include "Tools.h"

#include "glsupport.h"
#include "Vector2.h"


#include <iostream>

using namespace std;


/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

using namespace p3d;
using namespace std;

MeshObjGL::~MeshObjGL() {
  glDeleteBuffers(1,&_vertexBuffer);
  glDeleteBuffers(1,&_normalBuffer);
  glDeleteBuffers(1,&_texCoordBuffer);
  glDeleteBuffers(1,&_indexBuffer);
}

MeshObjGL::MeshObjGL(MeshObj *mesh) {
  _mesh=mesh;
  _indexBuffer=_vertexBuffer=_normalBuffer=_texCoordBuffer=0;
  _vao=0;
}


void MeshObjGL::draw() {
  drawBuffer();
}

void MeshObjGL::initBuffer() {
//  glDeleteVertexArrays(1,&_vao);
  glGenVertexArrays(1,&_vao);
  float *pts;
  pts=new float[_mesh->nbVertex()*4];
  float *normal;
  normal=new float[_mesh->nbVertex()*3];
  float *texCoord;
  texCoord=new float[_mesh->nbVertex()*2];
  unsigned int *indice;
  indice=new unsigned int[_mesh->nbFace()*3];

  unsigned int i;
  for(i=0;i<_mesh->nbVertex();++i) {
    pts[i*4]=_mesh->vertex(i).x();
    pts[i*4+1]=_mesh->vertex(i).y();
    pts[i*4+2]=_mesh->vertex(i).z();
    pts[i*4+3]=1.0;
    normal[i*3]=_mesh->normal(i).x();
    normal[i*3+1]=_mesh->normal(i).y();
    normal[i*3+2]=_mesh->normal(i).z();
    texCoord[i*2+0]=_mesh->texCoord(i).x();
    texCoord[i*2+1]=_mesh->texCoord(i).y();
  }


  for(unsigned int i=0;i<_mesh->nbFace();i++) {
    indice[i*3]=_mesh->indexVertex(i,0);
    indice[i*3+1]=_mesh->indexVertex(i,1);
    indice[i*3+2]=_mesh->indexVertex(i,2);
  }

  glDeleteBuffers(1,&_vertexBuffer);
  glDeleteBuffers(1,&_normalBuffer);
  glDeleteBuffers(1,&_texCoordBuffer);
  glDeleteBuffers(1,&_indexBuffer);
  glBindVertexArray(_vao);
  glEnableVertexAttribArray(0); // should be vertex in shader
  glEnableVertexAttribArray(1); // should be normal in shader
  glEnableVertexAttribArray(2); // should be texCoord in shader

  glGenBuffers(1,&_vertexBuffer);
  glBindBuffer(GL_ARRAY_BUFFER,_vertexBuffer);
  glBufferData(GL_ARRAY_BUFFER,_mesh->nbVertex()*sizeof(float)*4,pts,GL_STATIC_DRAW);
  glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,4*sizeof(float),0); // 0 = vertex

  glGenBuffers(1,&_normalBuffer);
  glBindBuffer(GL_ARRAY_BUFFER,_normalBuffer);
  glBufferData(GL_ARRAY_BUFFER,_mesh->nbVertex()*sizeof(float)*3,normal,GL_STATIC_DRAW);
  glVertexAttribPointer(1,3,GL_FLOAT,GL_FALSE,0,0); // 1 = normal

  glGenBuffers(1,&_texCoordBuffer);
  glBindBuffer(GL_ARRAY_BUFFER,_texCoordBuffer);
  glBufferData(GL_ARRAY_BUFFER,_mesh->nbVertex()*sizeof(float)*2,texCoord,GL_STATIC_DRAW);
  glVertexAttribPointer(2,2,GL_FLOAT,GL_FALSE,0,0); // 2 = texCoord



  glGenBuffers(1,&_indexBuffer);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,_indexBuffer);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER,_mesh->nbFace()*sizeof(unsigned int)*3,indice,GL_STATIC_DRAW);

  glBindVertexArray(0);
  //
  delete[] pts;
  delete[] normal;
  delete[] texCoord;
  delete[] indice;
}


void MeshObjGL::drawBuffer() {
  glBindVertexArray(_vao);

  glDrawElements(GL_TRIANGLES,_mesh->nbFace()*3,GL_UNSIGNED_INT,0);

  glBindVertexArray(0);
}


