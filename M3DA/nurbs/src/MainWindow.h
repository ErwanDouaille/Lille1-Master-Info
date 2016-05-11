#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QPushButton>

#include "GLView.h"


/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

class MainWindow : public QMainWindow {
  Q_OBJECT

  std::vector<QPushButton *> _choice;
  GLView *_glView;
public:
  explicit MainWindow();
  

  void keyPressEvent(QKeyEvent *e);

  QSize sizeHint() const {return QSize(1000,1000);}

signals:
  
public slots:
  void choice();
  
};

#endif // MAINWINDOW_H
