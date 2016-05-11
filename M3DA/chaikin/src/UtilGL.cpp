/**

  @author F. Aubert
  **/
#include "glsupport.h"
#include <cmath>
#include "UtilGL.h"
#include <iostream>
#include <string>
#include <sstream>


/**


@author  F. Aubert

*/

using namespace std;

#define MY_PI 3.141592653

namespace prog3d {
  namespace ugl {

// global members (namespace ugl) :
static double _currentTextPosX,_initTextPosX;
static double _currentTextPosY,_initTextPosY;
static QGLWidget *_widgetQt;
static QFont _fontQt=QFont("Helvetica [Cronyx]", 36);


/** ********************** */


void initUtilGL(QGLWidget *widget) {
	_widgetQt=widget;
  _fontQt.setBold(true);
  _fontQt.setPointSize(12);
}


void drawText(const string &s) {
  QFontMetrics metric(_fontQt);
  QString qs;
  qs=QString::fromStdString(s);
  _widgetQt->renderText(_currentTextPosX,_currentTextPosY,qs,_fontQt);
  int width=metric.width(qs);
  _currentTextPosX+=width;
}


void drawText(const string &s,double x,double y) {
  QFontMetrics metric(_fontQt);
  QString qs;
  qs=QString::fromStdString(s);
  int heightT=metric.height();

  _currentTextPosX=x*_widgetQt->width();
  _currentTextPosY=y*_widgetQt->height()+heightT;
  _initTextPosX=_currentTextPosX;
  _initTextPosY=_currentTextPosY;
  _widgetQt->renderText(_currentTextPosX,_currentTextPosY,qs,_fontQt);
  int width=metric.width(qs);
  _currentTextPosX+=width;
}

void drawTextLine(const string &s) {
  QFontMetrics metric(_fontQt);
  QString qs;
  qs=QString::fromStdString(s);
  _widgetQt->renderText(_currentTextPosX,_currentTextPosY,qs,_fontQt);
  int width=metric.height();
  _currentTextPosX=_initTextPosX;
  _currentTextPosY+=width;
}

  }
}

