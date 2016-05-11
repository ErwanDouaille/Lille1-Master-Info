
/**

  @author F. Aubert
  **/
#include "MainWindow.h"

#include "GLView.h"

#include <string>
#include <sstream>
#include <QtGui>

using namespace std;

MainWindow::MainWindow(QWidget *parent) : QWidget(parent) { // base class initialization

  // create a layout and attach it to the widget
  QGridLayout *gridLayout=new QGridLayout();
  this->setLayout(gridLayout);

  // OpenGL context initializations and creation of the glView (see glView.cpp)
  QGLFormat format=QGLFormat(QGL::DoubleBuffer | QGL::DepthBuffer | QGL::StencilBuffer | QGL::AlphaChannel);
  _glView=new GLView(format,this);
  _glView->resize(512,512);


  // add widget to the layout
  gridLayout->addWidget(_glView);

  // get the focus
  _glView->setFocus();

  // call the glView drawing every 20ms (i.e. 50Hz)
  QTimer *timer1=new QTimer(this);
  connect(timer1,SIGNAL(timeout()),_glView,SLOT(updateData())); // call glView->update() (that will call paintGL() )
  timer1->start(20);




}



MainWindow::~MainWindow() {
  // no need to delete glView : glView will be deleted when its parent will be deleted
}


// default size of the MainWindow
QSize MainWindow::sizeHint() const {
  return QSize(512,512);
}

// key events
void MainWindow::keyPressEvent(QKeyEvent *e) {
    switch(e->key()){
    case Qt::Key_Escape:
      this->close();
      break;
    default:
      QWidget::keyPressEvent(e); // send the event to the parent widget
    }
}


