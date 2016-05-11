/**

  @author F. Aubert
  **/


#include "GLView.h"
#include <iostream>
#include <QPushButton>

using namespace std;
using namespace prog3d;


/// ctor
GLView::GLView(QWidget *parent) : QGLWidget(parent) {

    setFocusPolicy(Qt::StrongFocus); // this widget can now catch the keyboard events

    _choice=1;
}


/// dtor
GLView::~GLView() {
}

/**
 initialize GL context (with glew ou without glew) + common set up
 **/
void GLView::initializeGL() {

#ifdef WITH_GLEW
    // initialization of all available OpenGL functionalities
    if (glewInit()!=GLEW_OK) {
        throw ErrorD("cant initialize glew");
    }
#endif

    cout << "GL version :" << glGetString(GL_VERSION) << endl;
    cout << "Shading Language  : " << glGetString(GL_SHADING_LANGUAGE_VERSION) << endl;

    // common OpenGL set up
    glEnable(GL_DEPTH_TEST);  // hidden parts removal with depth buffer algorithm
    glClearColor(1,1,1,1); // color value when glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) is done
    glClearDepth(1.0);       // depth value when glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) is done

    glDisable(GL_STENCIL_TEST);
    glClearStencil(0);

    // polygon drawings set up
    glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

    glPixelStorei(GL_PACK_ALIGNMENT,4);
    glPixelStorei(GL_UNPACK_ALIGNMENT,4);

    ugl::initUtilGL(this);

    initData();

}


/** ***************************************************************************
resize
  **/


/// when the graphics window is sized/resized (including the first time the window is shown)
void GLView::resizeGL(int width, int height) {
    glViewport(0,0,width,height);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(-1,1,-1,1,-1,1);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
}



/** **********************************************************************
  Events
  **/
void GLView::mousePressEvent(QMouseEvent *event) {
    if (event->button()==Qt::LeftButton) {
        cout << "left mouse : " << event->x() << "," << event->y() << endl;
    }
    if (event->button()==Qt::RightButton) {
        cout << "right mouse : " << event->x() << "," << event->y() << endl;
    }
}

void GLView::mouseMoveEvent(QMouseEvent *event) {
    cout << "motion : " << event->x() << "," << event->y() << endl;
}

void GLView::mouseReleaseEvent(QMouseEvent *event) {
    switch(event->button()){
    case Qt::LeftButton:
        cout << "left mouse released" << endl;
        break;
    case Qt::RightButton:
        break;
    default:break;
    }
}

void GLView::wheelEvent(QWheelEvent *event) {
    cout << "mouse wheel :" << event->delta() << endl;
}


void GLView::keyPressEvent(QKeyEvent *event) {
    if (event->isAutoRepeat())
        QGLWidget::keyPressEvent(event);
    else
        switch(event->key()){
        case Qt::Key_Z:
            cout << "Z pressed" << endl;
            break;
        case Qt::Key_S:
            cout << "S pressed" << endl;
            break;
        case Qt::Key_Space:
            cout << "space pressed" << endl;
            break;
        default:
            QGLWidget::keyPressEvent(event); // dispatch the event to the parent
        }
}

void GLView::keyReleaseEvent(QKeyEvent *event) {
    if (event->isAutoRepeat())
        QGLWidget::keyReleaseEvent(event);
    else
        switch(event->key()){
        case Qt::Key_Space:
            cout << "space released" << endl;
        case Qt::Key_Z:
            break;
        case Qt::Key_S:
            break;
        default:
            QGLWidget::keyReleaseEvent(event); // dispatch the event to the parent
        }
}



/** ***************************************************************************
  init/update data
  **/
void GLView::initData() {
    // Nurb initialization
    _nurb = new Nurbs();
    _nurb->initialize(6, 2);

    _nurb->addControlPoint(Vector2(1,0));
    _nurb->addControlPoint(Vector2(2,1));
    _nurb->addControlPoint(Vector2(4,1.5));
    _nurb->addControlPoint(Vector2(5,1.5));
    _nurb->addControlPoint(Vector2(6,0.5));
    _nurb->addControlPoint(Vector2(7,1.5));
}

void GLView::updateData() {
    // update data :


    // update view :
    updateGL();
}


/** ******************************************************************* **/
/**
  Drawings
**/


void GLView::drawChoice0() {
    glColor3f(0,0.5,1);
    ugl::drawText("drawChoice0: "+_choiceText,0,0);
    glPushMatrix();
    glLineWidth(1);
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(-1,20,-1,2,-1,1);

    this->drawNurbs();
    this->drawMultiNurbs();

    glBegin(GL_LINES);
    glColor3f(0.0f, 1.0f, 0.0f);
    glVertex2f(10, -0.5);
    glVertex2f(10, 1.5);
    glEnd();

//    this->drawControlPoints();

    glPopMatrix();
}

void GLView::drawNurbs() {
    _nurb->draw(0,2);
}

void GLView::drawMultiNurbs() {
    int p =2;
    for(int i = 0; i < _nurb->getKnot().size()-p-1;i++) {
        _nurb->drawOnly(i,p);
    }
}

void GLView::drawControlPoints() {
    glPushMatrix();
    glTranslated(10,0,0);
    _nurb->drawControlPoints();
    _nurb->drawBSpline();
    glPopMatrix();
}

void GLView::drawChoice1() {

    _nurb->setDelta(1.0);
    glColor3f(0,0.5,1);
    ugl::drawText("drawChoice1: "+_choiceText,0,0);
    glPushMatrix();
    glLineWidth(1);
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(-1,20,-1,2,-1,1);

    this->drawNurbs();
    this->drawMultiNurbs();

    glBegin(GL_LINES);
    glColor3f(0.0f, 1.0f, 0.0f);
    glVertex2f(10, -0.5);
    glVertex2f(10, 1.5);
    glEnd();

    this->drawControlPoints();

    glPopMatrix();
}

void GLView::drawChoice2() {

    glColor3f(0,0.5,1);
    ugl::drawText("drawChoice2: "+_choiceText,0,0);
    glPushMatrix();
    glLineWidth(1);
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(-1,20,-1,2,-1,1);

    glBegin(GL_LINES);
    glColor3f(0.0f, 1.0f, 0.0f);
    glVertex2f(10, -0.5);
    glVertex2f(10, 1.5);
    glEnd();

    this->drawMultiNurbs();
    this->drawControlPoints();
    if(_nurb->getDelta()>1)
        _nurb->resetDelta();
    _nurb->setDelta(_nurb->getDelta()+0.01);

    glPopMatrix();
}

void GLView::drawChoice3() {

    _nurb->setDelta(1.0);
    glColor3f(0,0.5,1);
    ugl::drawText("drawChoice3: "+_choiceText,0,0);
    glPushMatrix();
    glLineWidth(1);
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(-1,20,-1,2,-1,1);
    _nurb->_knot.clear();
    _nurb->_knot.push_back(1);
    _nurb->_knot.push_back(2);
    _nurb->_knot.push_back(4);
    _nurb->_knot.push_back(5);
    _nurb->_knot.push_back(6);
    _nurb->_knot.push_back(7.5);
    _nurb->_knot.push_back(8);
    _nurb->_knot.push_back(9);
    _nurb->_knot.push_back(10);

    this->drawNurbs();
    this->drawMultiNurbs();

    glBegin(GL_LINES);
    glColor3f(0.0f, 1.0f, 0.0f);
    glVertex2f(10, -0.5);
    glVertex2f(10, 1.5);
    glEnd();



    this->drawControlPoints();

    glPopMatrix();
}

void GLView::drawChoice4() {

    _nurb->setDelta(1.0);
    glColor3f(0,0.5,1);
    ugl::drawText("drawChoice4: "+_choiceText,0,0);
    glPushMatrix();
    glLineWidth(1);
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(-1,20,-1,2,-1,1);

    _nurb->initialize(4, 2);
    _nurb->_knot.clear();
    _nurb->_knot.push_back(0);
    _nurb->_knot.push_back(0);
    _nurb->_knot.push_back(0);
    _nurb->_knot.push_back(1);
    _nurb->_knot.push_back(2);
    _nurb->_knot.push_back(2);
    _nurb->_knot.push_back(2);

    this->drawNurbs();
    this->drawMultiNurbs();

    glBegin(GL_LINES);
    glColor3f(0.0f, 1.0f, 0.0f);
    glVertex2f(10, -0.5);
    glVertex2f(10, 1.5);
    glEnd();

    this->drawControlPoints();

    glPopMatrix();
}

void GLView::drawChoice5() {

    _nurb->setDelta(1.0);
    glColor3f(0,0.5,1);
    ugl::drawText("drawChoice5: "+_choiceText,0,0);
    glPushMatrix();
    glLineWidth(1);
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(-1,20,-1,2,-1,1);

    _nurb->initialize(5, 2);
    _nurb->_controlPoints.clear();
    _nurb->addControlPoint(Vector2(1,0));
    _nurb->addControlPoint(Vector2(2,1));
    _nurb->addControlPoint(Vector2(4,1.5));
    _nurb->addControlPoint(Vector2(6,1));
    _nurb->addControlPoint(Vector2(7,0));


    _nurb->drawControlPoints();
    _nurb->drawBSpline();

    glPopMatrix();
}

void GLView::paintGL() {
    /// clears the window
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    /// choice example
    switch(_choice) {
    case 0:
        /// call the drawing method for the clicked button 0 ...
        drawChoice0();
        break;
    case 1:
        /// call the drawing method for the clicked button 1 ...
        drawChoice1();
        break;
    case 2:
        /// call the drawing method for the clicked button 1 ...
        drawChoice2();
        break;
    case 3:
        /// call the drawing method for the clicked button 1 ...
        drawChoice3();
        break;
    case 4:
        /// call the drawing method for the clicked button 1 ...
        drawChoice4();
        break;
    case 5:
        /// call the drawing method for the clicked button 1 ...
        drawChoice5();
        break;
    }


}

/** ********************************************************************** **/

void GLView::choice(int i,const string &s) {
    // i = button number, s = button text
    cout << "choice " << i << " " << s << endl;
    _choice=i;
    _choiceText=s;
}
