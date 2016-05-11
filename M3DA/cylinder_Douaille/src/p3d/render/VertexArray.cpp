#include "VertexArray.h"
#include "Vector4.h"
#include "Vector3.h"
#include "Vector2.h"
#include "Tools.h"

#include <string>

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


/** VERTEX ATTRIB **/
VertexArray::~VertexArray() {
  reset();
}


VertexArray &VertexArray::operator=(const VertexArray &) {
  throw ErrorD("do not copy VertexAttrib");
}

VertexArray::VertexArray(const VertexArray &) {
  throw ErrorD("do not copy constructor VertexAttrib");
}

VertexArray::VertexArray(GLenum mode) {
  _mode=mode;
  init();
}

void VertexArray::reset() {
  for(auto b:_buffer) {
    delete b;
  }
  delete _element;
  delete[] _first;
  delete[] _count;
  glDeleteVertexArrays(1,&_vaoId);
  init();
}

void VertexArray::init() {
  _nbElement=-1;
  _nbVertex=-1;
  _buffer.clear();_index.clear();_enable.clear();
  _bufferIndex.clear();
  _bufferName.clear();
  _element=0;
  _restart=-1;
  _first=NULL;_count=NULL;
  _vaoId=0;
}


void VertexArray::add(int nbVertex,const std::string &name,GLint index) {
  if (_vaoId==0) {
    glGenVertexArrays(1,&_vaoId);
  }
  if (index==-1) {
    index=_buffer.size()-1;
  }
  if (_nbVertex==-1) {
    _nbVertex=nbVertex;
  }
  else if (_nbVertex!=nbVertex) throw ErrorD("inconsistent nbVertex");
  if (_nbElement==-1) {
    _nbElement=nbVertex;
  }
  if (_bufferIndex.find(index)!=_bufferIndex.end()) {
    throw ErrorD("attribute index is already mapped");
  }
  if (_bufferName.find(name)!=_bufferName.end()) {
    throw ErrorD(string("the name \"")+name+"\" already exists in VertexAttrib (use update ?)");
  }
  _bufferName[name]=_buffer.size()-1;
  _bufferIndex[index]=_buffer.size()-1;
  _index.push_back(index);
  _enable.push_back(true);

  glBindVertexArray(_vaoId);
  int i=_buffer.size()-1;
  glEnableVertexAttribArray(_index[i]);
  glBindBuffer(GL_ARRAY_BUFFER,_buffer[i]->_id);
  glVertexAttribPointer(_index[i],_buffer[i]->_nbComponent,_buffer[i]->_type,GL_FALSE,0,0);
  glBindVertexArray(0);
}

void VertexArray::add(const std::string &name,const std::vector<Vector2> &data,GLint index) {
  _buffer.push_back(new ArrayBuffer(data,GL_ARRAY_BUFFER));
  add(data.size(),name,index);
}

void VertexArray::add(const std::string &name,const std::vector<Vector3> &data,GLint index) {
  _buffer.push_back(new ArrayBuffer(data,GL_ARRAY_BUFFER));
  add(data.size(),name,index);
}

void VertexArray::add(const std::string &name,const std::vector<Vector4> &data,GLint index) {
  _buffer.push_back(new ArrayBuffer(data,GL_ARRAY_BUFFER));
  add(data.size(),name,index);
}


void VertexArray::add(const std::string &name,const std::vector<float> &data,int nbComponent,GLint index) {
  _buffer.push_back(new ArrayBuffer(data,GL_ARRAY_BUFFER,nbComponent));
  add(data.size()/nbComponent,name,index);
}

void VertexArray::update(const std::string &name,const std::vector<Vector2> &data) {
  if (_bufferName.find(name)==_bufferName.end()) {
    throw ErrorD(string("cant update the buffer \"")+name+"\" : not in VertexAttrib (use add before ?)");
  }
  ArrayBuffer *b=_buffer[_bufferName[name]];
  b->update(data);
}

void VertexArray::update(const std::string &name,const std::vector<Vector3> &data) {
  if (_bufferName.find(name)==_bufferName.end()) {
    throw ErrorD(string("cant update the buffer \"")+name+"\" : not in VertexAttrib (use add before ?)");
  }
  ArrayBuffer *b=_buffer[_bufferName[name]];
  b->update(data);
}

void VertexArray::update(const std::string &name,const std::vector<Vector4> &data) {
  if (_bufferName.find(name)==_bufferName.end()) {
    throw ErrorD(string("cant update the buffer \"")+name+"\" : not in VertexAttrib (use add before ?)");
  }
  ArrayBuffer *b=_buffer[_bufferName[name]];
  b->update(data);
}


void VertexArray::update(const std::string &name,const std::vector<float> &data) {
  if (_bufferName.find(name)==_bufferName.end()) {
    throw ErrorD(string("cant update the buffer \"")+name+"\" : not in VertexAttrib (use add before ?)");
  }
  ArrayBuffer *b=_buffer[_bufferName[name]];
  b->update(data);
}


void VertexArray::element(const std::vector<int> &element) {
  if (_vaoId==0) {
    glGenVertexArrays(1,&_vaoId);
  }
  _element=new ArrayBuffer(element,GL_ELEMENT_ARRAY_BUFFER);
  _nbElement=element.size();
  glBindVertexArray(_vaoId);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,_element->_id);
  glBindVertexArray(0);
}

void VertexArray::disable(GLint index) {
  _enable[_bufferIndex[index]]=false;
}

void VertexArray::enable(GLint index) {
  _enable[_bufferIndex[index]]=true;
}

void VertexArray::restart(int nbElem) {
  if (_nbElement==-1) {
    throw ErrorD("call restart **after** an attribute or element is set");
  }
  _restart=nbElem;
  intptr_t offset; // offset for restart (different for Arrays (=vertex count) and Elements (=byte offset)
  _primCount=_nbElement/nbElem;
  if (_element) {
    offset=_restart*sizeof(GLuint);
  }
  else {
    offset=(intptr_t)_restart;
  }
  delete[] _first;
  delete[] _count;
  _first=new intptr_t[_primCount];
  _count=new GLsizei[_primCount];
  _first[0]=0;_count[0]=_restart;

  for(int i=1;i<_primCount;i++) {
    _first[i]=_first[i-1]+offset;
    _count[i]=_count[i-1];
  }
}

/// use parameter with care ! (perticularly when restart is set)
void VertexArray::draw(int nbElement) {
  if (_mode==-1) throw ErrorD("draw mode (like GL_TRIANGLES,GL_TRIANGLE_STRIP,etc) not set for VAO");
  if (nbElement!=-1) {
    // if (_restart && nbElement!=_nbElement) throw ErrorD("cant draw nbVertex with a restart set (all elements should be drawn)");
  }
  else nbElement=_nbElement;
  glBindVertexArray(_vaoId);
  if (!_element) { // draw with arrays
    if (_restart==-1) {
      glDrawArrays(_mode,0,nbElement);
    }
    else {
      // TODO : cant use multi with same type for glMultiDrawElements and glMultiDrawArrays (64 bits platform : GLint * not compatible with GLvoid *; use intptr_t)
      for(int i=0;i<_primCount;i++) {
        glDrawArrays(_mode,_first[i],_count[i]);
      }
//      glMultiDrawArrays(_mode,(GLint *)_first,_count,_primCount);
    }
  }
  else {// draw with elements
    if (_restart==-1) {
      glDrawElements(_mode,nbElement,GL_UNSIGNED_INT,0);
    }
    else {
      // TODO : cant use multi with same type for Elements and Array (64 bits : GLuint/GLint not compatible with GLvoid *)

      for(int i=0;i<_primCount;i++) {
        glDrawElements(_mode,_count[i],GL_UNSIGNED_INT,(const GLvoid *)(_first[i]));
      }

 //     glMultiDrawElements(_mode,_count,GL_UNSIGNED_INT,(const GLvoid **)(_first),_primCount);
    }
  }
  glBindVertexArray(0);
}


/** ARRAY BUFFER **/
ArrayBuffer::~ArrayBuffer() {
  if (_id!=0) glDeleteBuffers(1,&_id); // ArrayBuffer must not share gl buffer !
}

ArrayBuffer::ArrayBuffer(const ArrayBuffer &) {
  throw ErrorD("you cant clone ArrayBuffer (copy constructor)");
}

ArrayBuffer &ArrayBuffer::operator=(const ArrayBuffer &) {
  throw ErrorD("you cant clone ArrayBuffer (operator =)");
}

ArrayBuffer::ArrayBuffer(const std::vector<float> &data, GLenum target, uint nbComponent) {
  _id=0;
  set(data,target,nbComponent);
}

ArrayBuffer::ArrayBuffer(const std::vector<Vector3> &data, GLenum target) {
  _id=0;
  set(data,target);
}

ArrayBuffer::ArrayBuffer(const std::vector<Vector4> &data, GLenum target) {
  _id=0;
  set(data,target);
}

ArrayBuffer::ArrayBuffer(const std::vector<Vector2> &data, GLenum target) {
  _id=0;
  set(data,target);
}

ArrayBuffer::ArrayBuffer(const std::vector<int> &data, GLenum target) {
  _id=0;
  set(data,target);
}

void ArrayBuffer::update(const std::vector<float> &data) {
  glBindBuffer(_target,_id);
  glBufferData(_target,_size,data.data(),GL_STATIC_DRAW);
}


void ArrayBuffer::update(const std::vector<Vector2> &data) {
  glBindBuffer(_target,_id);
  float *buffer=(float *)glMapBuffer(_target,GL_WRITE_ONLY);
  for(auto &v:data) {
    *(buffer)=v.x();
    *(++buffer)=v.y();
    ++buffer;
  }
  glUnmapBuffer(_target);
}

void ArrayBuffer::update(const std::vector<Vector3> &data) {
  glBindBuffer(_target,_id);
  float *buffer=(float *)glMapBuffer(_target,GL_WRITE_ONLY);
  for(auto &v:data) {
    *(buffer)=v.x();
    *(++buffer)=v.y();
    *(++buffer)=v.z();
    ++buffer;
   }
  glUnmapBuffer(_target);
}

void ArrayBuffer::update(const std::vector<Vector4> &data) {
  glBindBuffer(_target,_id);
  float *buffer=(float *)glMapBuffer(_target,GL_WRITE_ONLY);
  for(auto &v:data) {
    *(buffer)=v.x();
    *(++buffer)=v.y();
    *(++buffer)=v.z();
    *(++buffer)=v.w();
    ++buffer;
   }
  glUnmapBuffer(_target);
}


void ArrayBuffer::set(const std::vector<float> &data,GLenum target,uint nbComponent) {
  _nbComponent=nbComponent;
  _target=target;
  _size=sizeof(float)*data.size();
  _type=GL_FLOAT;
  if (_id==0) glGenBuffers(1,&_id);
  glBindBuffer(_target,_id);
  update(data);
}

void ArrayBuffer::set(const std::vector<Vector4> &data,GLenum target) {
  _nbComponent=4;
  _target=target;
  _size=sizeof(GLfloat)*4*data.size();
  _type=GL_FLOAT;
  if (_id==0)
    glGenBuffers(1,&_id);
  glBindBuffer(_target,_id);
  glBufferData(_target,_size,0,GL_STATIC_DRAW);
  update(data);
}

void ArrayBuffer::set(const std::vector<Vector3> &data,GLenum target) {
  _nbComponent=3;
  _target=target;
  _size=sizeof(GLfloat)*3*data.size();
  _type=GL_FLOAT;
  if (_id==0)
    glGenBuffers(1,&_id);
  glBindBuffer(_target,_id);
  glBufferData(_target,_size,0,GL_STATIC_DRAW);
  update(data);
}




void ArrayBuffer::set(const std::vector<Vector2> &data,GLenum target) {
  _nbComponent=2;
  _target=target;
  _size=sizeof(GLfloat)*2*data.size();
  _type=GL_FLOAT;
  if (_id==0) glGenBuffers(1,&_id);
  glBindBuffer(_target,_id);
  glBufferData(_target,_size,0,GL_STATIC_DRAW);
  update(data);
}


void ArrayBuffer::set(const std::vector<int> &data,GLenum target) {
  _nbComponent=1;
  _target=target;
  _size=sizeof(int)*data.size();
  _type=GL_UNSIGNED_INT;
  if (_id==0) glGenBuffers(1,&_id);
  glBindBuffer(_target,_id);
  glBufferData(_target,_size,data.data(),GL_STATIC_DRAW);
}


void ArrayBuffer::erase() {
  glDeleteBuffers(1,&_id);
  _id=0;
}


