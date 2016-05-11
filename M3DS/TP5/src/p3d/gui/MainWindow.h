#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "GLApplication.h"

#include <QMainWindow>
#include <QPushButton>



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

  std::vector<QPushButton *> _leftPanelButton;
  GLApplication *_glApplication;
public:
  explicit MainWindow();
  

  void keyPressEvent(QKeyEvent *e);

//  QSize sizeHint() const {return QSize(1000,1000);}

signals:
  
public slots:
  void leftPanelSlot();
  void snapshotSlot();
};

#endif // MAINWINDOW_H

