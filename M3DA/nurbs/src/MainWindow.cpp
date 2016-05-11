#include <iostream>

#include "GLView.h"

#include "MainWindow.h"

#include <QGLFormat>
#include <QGridLayout>
#include <QTimer>
#include <QKeyEvent>
#include <QPushButton>

#include <QDebug>

#include "Error.h"

using namespace std;

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/


MainWindow::MainWindow() :
  QMainWindow(NULL) {

  // build a GLWidget
  QGLFormat format=QGLFormat();
  format.setVersion(4,2); // there's a bug somewhere for the compatibility profile on AMD driver : comment this line if there's a problem with the openGL version
  format.setProfile(QGLFormat::CompatibilityProfile);
  format.setSwapInterval(1);
  format.setOption(QGL::DoubleBuffer | QGL::DepthBuffer | QGL::StencilBuffer | QGL::AlphaChannel);
  QGLFormat::setDefaultFormat(format);

  _glView=new GLView(this);

  // application buttons
  _choice.clear();
  _choice.push_back(new QPushButton("draw Nkp Ex1-Q4"));
  _choice.push_back(new QPushButton("draw Bspline Ex1-Q9"));
  _choice.push_back(new QPushButton("animate Ex1-Q10"));
  _choice.push_back(new QPushButton("non uniform Ex1-Q11"));
  _choice.push_back(new QPushButton("open uniform Ex1-Q12"));
  _choice.push_back(new QPushButton("open uniform Ex1-Q13"));


  // set up for the main window : set a central widget (main container), and set a gridLayout to this central widget
  QWidget *central=new QWidget(this);
  this->setCentralWidget(central);
  QGridLayout *layout=new QGridLayout(central);

  // add widgets in layout
  for(unsigned int i=0;i<_choice.size();i++) {
    layout->addWidget(_choice[i],i,0);
    connect(_choice[i],SIGNAL(clicked()),this,SLOT(choice()));
  }
  layout->addWidget(_glView,0,1,20,20);

  // update GLWidget every 20ms
  QTimer *timer=new QTimer(this);
  connect(timer,SIGNAL(timeout()),_glView,SLOT(updateData())); // calls GLView::update() (that will call paintGL() )
  timer->start(20);

  if (!_choice.empty()) _choice[0]->click();

}


void MainWindow::choice() {
  QPushButton *b=dynamic_cast<QPushButton *>(QObject::sender());
  unsigned int i;
  for(i=0;i<_choice.size();++i) if (b==_choice[i]) break;
  _glView->choice(i,b->text().toStdString());
}


// key events
void MainWindow::keyPressEvent(QKeyEvent *e) {
  if (e->key()==Qt::Key_Escape) {
    this->close();
  }
  else {
    QWidget::keyPressEvent(e); // send the event to the parent widget
  }
}

