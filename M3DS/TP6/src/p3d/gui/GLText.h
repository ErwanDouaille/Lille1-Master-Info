#ifndef GLUTILTEXT_H
#define GLUTILTEXT_H

#include "glsupport.h"

#include <sstream>
#include <iomanip>

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/



namespace p3d {
  void initGLText();
  // render text in OpenGL >= 3.2 : slow cause the texture is computed each call
  void drawText(const std::string &s,double x,double y,double z=0,const float *modelview=NULL,const float *projection=NULL,const float *color=NULL);
  template <class T> std::string toString(const T &v) {std::stringstream s;s<< std::setprecision(2) <<v;return s.str();}
}


#endif // GLUTILTEXT_H

