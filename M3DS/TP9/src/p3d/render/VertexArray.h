#ifndef VERTEXATTRIB_H
#define VERTEXATTRIB_H

#include "glsupport.h"

namespace p3d {
class Vector4;
class Vector3;
class Vector2;

/**
@file
@author F. Aubert
@brief Encapsulates common buffer operations (ArrayBuffer and VertexAttrib)
*/

/** VERTEX ATTRIB (ARRAY BUFFER is defined at the end) **/
class ArrayBuffer;
class VertexArray
{
  GLuint _vaoId; // opengl VAO id
  std::vector<ArrayBuffer *> _buffer; // GL buffer for each attribute (no shared/interleaved buffer for simplicity)
  ArrayBuffer *_element; // for glDrawElements
  std::vector<GLuint> _index; // map each buffer to the index of the generic attribute for the shader (by default _index[i]=i)
  std::vector<bool> _enable; // enable the vertex attribute or not (useless)
  std::map<uint,GLuint> _bufferIndex; // to know which buffer is bind to the given shader index
  std::map<std::string,GLuint> _bufferName; // to know which buffer has the given name
  int _nbVertex; // corresponds to number of vertex in VAO (to check errors)
  int _nbElement; // corresponds to number of element to draw, i.e. count in glDrawArrays, glDrawElements
  int _mode; // GL_TRIANGLES, etc

  int _restart; // for primitive restart : here it is interpreted as a cycle (actually implemented with glMultiDraw* not with the opengl restart for compatibility reason)
  intptr_t *_first; // for primitive restart
  GLsizei *_count; // for primitive restart (actually implemented with glMultiDrawArrays,glMultDrawElements, *not* with restart index from openGL 4)
  GLsizei _primCount; // for primitive restart

  void init();
  void add(int nbVertex, const std::string &name, GLint index);
public:
  virtual ~VertexArray();

  void reset();

  /// do not copy (no semantic due to opengl resources)
  VertexArray(const VertexArray &src);
  /// do not copy (no semantic due to opengl resources)
  VertexArray &operator=(const VertexArray &src);

  VertexArray(GLenum mode=GL_TRIANGLES);

  void mode(GLenum mode) {_mode=mode;}
  inline int nbVertex() {return _nbVertex;}
  void add(const std::string &name,const std::vector<Vector4> &data, GLint index);
  void add(const std::string &name,const std::vector<Vector3> &data, GLint index);
  void add(const std::string &name,const std::vector<Vector2> &data, GLint index);
  void add(const std::string &name,const std::vector<float> &data, int nbComponent,GLint index);

  void update(const std::string &name,const std::vector<Vector4> &data);
  void update(const std::string &name,const std::vector<Vector3> &data);
  void update(const std::string &name,const std::vector<Vector2> &data);
  void update(const std::string &name,const std::vector<float> &data);


  void enable(GLint index);
  void disable(GLint index);
  void draw(int nbElement=-1);
  void element(const std::vector<int> &element);
  void use() {glBindVertexArray(_vaoId);}
  /// attention : if the VAO has elements, make sure that elements are set **before** restart (i.e. with VertexArray::element(indices) )
  void restart(int nbIndex);
};

/** ARRAY BUFFER **/
class ArrayBuffer {
public:
  GLenum _target; // GL_ARRAY_BUFFER, etc
  GLuint _id; // openGL name
  uint _nbComponent;  // corresponds to size of glVertexAttribPointer; must be 1,2,3 or 4
  GLsizeiptr _size; // size in bytes
  GLenum _type; // GL_FLOAT, etc

  virtual ~ArrayBuffer();
  ArrayBuffer(const ArrayBuffer &src);
  ArrayBuffer &operator=(const ArrayBuffer &src);
  ArrayBuffer(const std::vector<float> &data,GLenum target=GL_ARRAY_BUFFER,uint nbComponent=1);
  ArrayBuffer(const std::vector<int> &data,GLenum target=GL_ELEMENT_ARRAY_BUFFER);
  ArrayBuffer(const std::vector<Vector4> &data, GLenum target);
  ArrayBuffer(const std::vector<Vector3> &data, GLenum target);
  ArrayBuffer(const std::vector<Vector2> &data, GLenum target);



  void erase();
  void set(const std::vector<float> &data, GLenum target, uint nbComponent=1);
  void set(const std::vector<int> &data,GLenum target);
  void set(const std::vector<Vector4> &data, GLenum target);
  void set(const std::vector<Vector3> &data, GLenum target);
  void set(const std::vector<Vector2> &data, GLenum target);

  // TODO : templatize all these things
  void update(const std::vector<float> &data);
  void update(const std::vector<Vector4> &data);
  void update(const std::vector<Vector3> &data);
  void update(const std::vector<Vector2> &data);

};




} // namespace p3d

#endif // VERTEXATTRIB_H

