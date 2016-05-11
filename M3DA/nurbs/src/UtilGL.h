#ifndef UTILGL_H_INCLUDED
#define UTILGL_H_INCLUDED

#include "glsupport.h"
#include <string>
#include <ctime>
#include <sstream>
#include <iomanip>

/**
@file
@author F. Aubert

@class UtilGL
@brief Text drawing in 2D
*/

#define MY_PI 3.141592653

namespace prog3d {
  namespace ugl {

    void initUtilGL(QGLWidget *widget);
    void kill();

    /// draws text at position (x,y) : (0,0) = top-left corner of the window, (1,1) = bottom-right corner
    void drawText(const std::string &s, double x,double y);
    /// draws text at current position
    void drawText(const std::string &s);
    //// draws text at current position then updates current position at the next line
    void drawTextLine(const std::string &s);
    template <class T> std::string toString(const T &v) {std::stringstream s;s<< std::setprecision(2) <<v;return s.str();}

  }
}



#endif // UTILGL_H_INCLUDED
