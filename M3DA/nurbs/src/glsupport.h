#ifndef GLSUPPORT_H
#define GLSUPPORT_H

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

#include <iostream>
#ifdef WITH_GLEW
#include <GL/glew.h>
#endif
#include <QGLWidget>
#include "Error.h"


namespace prog3d {
namespace ugl {
/** UTILS **/
inline std::string errorToString(GLuint error) {
  switch(error) {
  case GL_INVALID_ENUM:return "GL_INVALID_ENUM";
  case GL_INVALID_VALUE:return "GL_INVALID_VALUE";
  case GL_INVALID_OPERATION:return "GL_INVALID_OPERATION";
  case GL_INVALID_FRAMEBUFFER_OPERATION:return "GL_INVALID_FRAMEBUFFER_OPERATION";
  case GL_OUT_OF_MEMORY:return "GL_OUT_OF_MEMORY";
  case GL_STACK_UNDERFLOW:return "GL_STACK_UNDERFLOW";
  case GL_STACK_OVERFLOW:return "GL_STACK_OVERFLOW";
  default:return "UNKNOWN ERROR";
  }
}


inline void checkError(const std::string &mesg,int line,const std::string &file) {

  std::cout << "==============================" << std::endl;
  GLuint err=glGetError();
  if (err!=0) {
    std::cout << "GL error : " << errorToString(err) << " from " << file << " at line " << line << " ("+mesg+")" << std::endl;
    throw ErrorD("GL error");
  } else {
    std::cout << "no GL error : " << " from " << file << " at line " << line << " ("+mesg+")" << std::endl;
    std::cout << "==============================" << std::endl;
  }
}
}
}

#define checkGLErrorD(s) (ugl::checkError(s,__LINE__,__FILE__))

#endif // GLSUPPORT_H
