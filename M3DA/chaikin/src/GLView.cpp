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
    this->_width = width;
    this->_height = height;
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
        this->_courbe->addPoint(Vector2(event->x(), this->_height-event->y()));
    }
    if (event->button()==Qt::RightButton) {
        cout << "right mouse : " << event->x() << "," << event->y() << endl;
        this->_courbe->addPoint(Vector2(event->x(), this->_height-event->y(), true));

    }
}

void GLView::mouseMoveEvent(QMouseEvent *event) {
    cout << "motion : " << event->x() << "," << event->y() << endl;
    this->_courbe->addPoint(Vector2(event->x(), this->_height-event->y()));
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
        case Qt::Key_Plus:
            _subdivisionIteration++;
            break;
        case Qt::Key_Minus:
            _subdivisionIteration--;
            break;
        case Qt::Key_C:
            this->_courbe->closePolygon();
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
    this->_courbe = new Courbe();
    _subdivisionIteration = 0;
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
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(0,this->_width,0,this->_height,-1,1);
    this->_courbe->draw();
    glPopMatrix();
    glFlush();
}

void GLView::drawChoice1() {
    glColor3f(0,0.5,1);
    std::ostringstream oss;
    oss << "drawChoice1: subdivisions " << _subdivisionIteration << ", + increment - decrement";
    ugl::drawText(oss.str() ,0,0);

    if (_subdivisionIteration>14) {
        glColor3f(1,0,0);
        ugl::drawText("Trop d'iteration !!, ca rame car recalcul a chaque update des subdivisions" ,0,0.03);
    }

    glPushMatrix();
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(0,this->_width,0,this->_height,-1,1);
    this->_courbe->drawSubdivisedChaikin(_subdivisionIteration);
    glPopMatrix();
    glFlush();
}

void GLView::drawChoice3() {
    glColor3f(0,0.5,1);
    std::ostringstream oss;
    oss << "drawChoice3: subdivisions 4 points " << _subdivisionIteration << ", + increment - decrement";
    ugl::drawText(oss.str() ,0,0);

    if (_subdivisionIteration>14) {
        glColor3f(1,0,0);
        ugl::drawText("Trop d'iteration !!, ca rame car recalcul a chaque update des subdivisions" ,0,0.03);
    }

    glPushMatrix();
    glColor3f(1,0,0);
    ugl::drawText("vertex ",0.2,0.2); // coordinates for drawText are (0,0) for top-left and (1,1) for bottom-right (not related to glOrtho)
    glOrtho(0,this->_width,0,this->_height,-1,1);
    this->_courbe->drawSubdivised4pointsChaikin(_subdivisionIteration);
    glPopMatrix();
    glFlush();
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
        this->_courbe->reset();
        drawChoice0();
        break;
    case 3:
        /// call the drawing method for the clicked button 1 ...
        drawChoice3();
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
