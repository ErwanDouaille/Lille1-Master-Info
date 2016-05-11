#include "MainWindow.h"

#include <QGridLayout>
#include <QGLFormat>
#include <QTimer>
#include <QKeyEvent>
#include <QPushButton>

#include <QDebug>

#include <iostream>

#include "Tools.h"
#include "GLApplication.h"


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

  // build the GLApplication widget
  QGLFormat format=QGLFormat();
  //format.setVersion(3,0); // there's a bug somewhere for the compatibility profile on AMD driver : comment this line if there's a problem with the openGL version
  format.setProfile(QGLFormat::CompatibilityProfile);
  format.setSwapInterval(1);
  format.setOption(QGL::DoubleBuffer | QGL::DepthBuffer | QGL::StencilBuffer | QGL::AlphaChannel);
  QGLFormat::setDefaultFormat(format);

  _glApplication=new GLApplication();

  // application buttons in left panel
  _leftPanelButton.clear();
  for(QString &s:_glApplication->_leftPanelMenu) {
    _leftPanelButton.push_back(new QPushButton(s));
  }

  // set up for the main window : set a central widget (main container), and set a gridLayout to this central widget
  QWidget *central=new QWidget(this);
  this->setCentralWidget(central);
  QGridLayout *layout=new QGridLayout(central);

  // add widgets in layout
  int row=0;
  for(QPushButton *b:_leftPanelButton) {
    layout->addWidget(b,row,0);
    connect(b,SIGNAL(clicked()),this,SLOT(leftPanelSlot()));
    row++;
  }
  layout->addWidget(_glApplication,0,1,20,20);

  QPushButton *snap=new QPushButton("snapshot");
  layout->addWidget(snap,19,0);
  connect(snap,SIGNAL(clicked()),this,SLOT(snapshotSlot()));

  // update GLWidget every 20ms
  QTimer *timer=new QTimer(this);
  connect(timer,SIGNAL(timeout()),_glApplication,SLOT(updateData())); // calls GLView::update() (that will call paintGL() )
  timer->start(0);

  if (!_leftPanelButton.empty()) _leftPanelButton[0]->click();

}


void MainWindow::leftPanelSlot() {
  QPushButton *b=dynamic_cast<QPushButton *>(QObject::sender());
  unsigned int clicked=0;
  for(auto &bb:_leftPanelButton) if (b==bb) break; else ++clicked;
  _glApplication->leftPanel(clicked,b->text().toStdString());
}

void MainWindow::snapshotSlot() {
  _glApplication->requestSnapshot();
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


