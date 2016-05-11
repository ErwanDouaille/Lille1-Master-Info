#include <QMouseEvent>
#include <QKeyEvent>

#include "GLWidget.h"
#include "GLApplication.h"
#include "GLText.h"
#include "Tools.h"

#include <iostream>

#include <QDebug>



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

GLWidget::GLWidget() :
  QGLWidget((QWidget *)0) {

  setFocusPolicy(Qt::StrongFocus);
  grabKeyboard();

  initGLText();

  connect(this,SIGNAL(updateRequest()),this,SLOT(updateData()));

  _firstElapsed=true;
  _snapshot=false;

}


/// dtor
GLWidget::~GLWidget() {
}



/** ***************************************************************************
 initialize GL context + common set up
 **/
void GLWidget::initializeGL() {
#ifdef WITH_GLEW
  glewExperimental=true;
  // initialization of all available OpenGL functionalities
  if (glewInit()!=GLEW_OK) {
    cout << "glew error : unable to initialize glew" << endl;
    exit(EXIT_FAILURE);
  }
  while (glGetError()!=GL_NO_ERROR); // there's a bug in GLEW that generates glError => ignores it
#endif

  cout << "GL version :" << glGetString(GL_VERSION) << endl;
  cout << "Shading Language  : " << glGetString(GL_SHADING_LANGUAGE_VERSION) << endl;



  initData();
}


/** ***************************************************************************
resize
  **/


/// when the graphics window is sized/resized (includes when the window is shown for the first time)
void GLWidget::resizeGL(int width, int height) {
  dimension(width,height);
  resize(width,height);
}



/** **********************************************************************
  Events
  **/
void GLWidget::mousePressEvent(QMouseEvent *event) {
  mouse(event->x(),event->y());
  resetDeltaMouse();
  if (event->button()==Qt::LeftButton) {
    mouseLeft(true);
  }
  if (event->button()==Qt::RightButton) {
    mouseRight(true);
  }
}

void GLWidget::mouseMoveEvent(QMouseEvent *event) {
  mouse(event->x(),event->y());
}

void GLWidget::mouseReleaseEvent(QMouseEvent *event) {
  mouse(event->x(),event->y());
  switch(event->button()){
  case Qt::LeftButton:
      mouseLeft(false);
      break;
  case Qt::RightButton:
      mouseRight(false);
    break;
  default:break;
  }
}

void GLWidget::wheelEvent(QWheelEvent *event) {
  if (event->delta()<0)
    decelerateWheel(true);
  else
    accelerateWheel(true);
}


void GLWidget::keyPressEvent(QKeyEvent *event) {
  QPoint cursor=mapFromGlobal(QCursor::pos());
  mouse(cursor.x(),cursor.y());
  key(event->key(),true);
  QGLWidget::keyPressEvent(event); // dispatch the event to the parent
}

void GLWidget::keyReleaseEvent(QKeyEvent *event) {
  key(event->key(),false);
  QGLWidget::keyReleaseEvent(event); // dispatch the event to the parent
}


  /** ***************************************************************************
  init/update data
  **/
void GLWidget::initData() {
  initialize();
}

void GLWidget::updateData() {
  update();
  updateGL();
}

void GLWidget::paintGL() {
  draw();
  snapshot();
}


/** ***********************************************************************************
 * Interaction helpers
 **/
bool GLWidget::mouseLeft() {return _mouseLeftButton;}
bool GLWidget::mouseRight() {return _mouseRightButton;}
bool GLWidget::mouseLeftPressed() {bool pressed=_mouseLeftPressed;_mouseLeftPressed=false;return pressed;}
bool GLWidget::mouseRightPressed() {bool pressed=_mouseRightPressed;_mouseRightPressed=false;return pressed;}

int GLWidget::mouseX() {return _mouseX;}
int GLWidget::mouseY() {return _mouseY;}
int GLWidget::deltaMouseX() {_deltaMouseX=_mouseX-_oldMouseX;_oldMouseX=_mouseX;return _deltaMouseX;}
int GLWidget::deltaMouseY() {_deltaMouseY=_mouseY-_oldMouseY;_oldMouseY=_mouseY;return _deltaMouseY;}

void GLWidget::mouseLeft(bool v) {_mouseLeftButton=v;_mouseLeftPressed=v;}
void GLWidget::mouseRight(bool v) {_mouseRightButton=v;_mouseRightPressed=v;}

void GLWidget::mouse(int x,int y) {_mouseX=x;_mouseY=_height-y;}
void GLWidget::resetDeltaMouse() {_oldMouseX=_mouseX;_oldMouseY=_mouseY;}

void GLWidget::dimension(int w,int h) {_width=w;_height=h;}
int GLWidget::width() {return _width;}
int GLWidget::height() {return _height;}

bool GLWidget::accelerateWheel() {bool res;res=_pressAccelerateWheel;_pressAccelerateWheel=false;return res;}
bool GLWidget::decelerateWheel() {bool res;res=_pressDecelerateWheel;_pressDecelerateWheel=false;return res;}
void GLWidget::accelerateWheel(bool v) {_pressAccelerateWheel=v;}
void GLWidget::decelerateWheel(bool v) {_pressDecelerateWheel=v;}

bool GLWidget::left() {return _key[Qt::Key_Q] || _key[Qt::Key_Left];}
bool GLWidget::right() {return _key[Qt::Key_D] || _key[Qt::Key_Right];}
bool GLWidget::forward() {return _key[Qt::Key_Z] || _key[Qt::Key_Up];}
bool GLWidget::backward() {return _key[Qt::Key_S] || _key[Qt::Key_Down];}
bool GLWidget::down() {return forward();}
bool GLWidget::up() {return backward();}
bool GLWidget::rotateLeft() {return _key[Qt::Key_A];}
bool GLWidget::rotateRight() {return _key[Qt::Key_E];}

void GLWidget::key(int k,bool v) {_key[k]=v;if (v) {_keyPressed[k]=v;_lastKeyPressed=k;}}
bool GLWidget::key(int k) {return _key[k];}
bool GLWidget::keyPressed(int k) {bool pressed=_keyPressed[k];_keyPressed[k]=false;return pressed;}
int GLWidget::keyPressed() {return ((_lastKeyPressed!=-1 && keyPressed(_lastKeyPressed))?_lastKeyPressed:-1);}





/** i = button number, s = button text
 */
void GLWidget::leftPanel(int i,const std::string &s) {
  cout << "button clicked " << i << " " << s << endl;
}

void GLWidget::requestSnapshot() {
  _snapshot=true;
}

void GLWidget::snapshot() {
  if (_snapshot) {
    p3d::captureImage(0,0,_width,_height);
    _snapshot=false;
  }
}








