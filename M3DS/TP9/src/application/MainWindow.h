/**

  @author F. Aubert
  **/

#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QWidget>
#include "GLView.h"

class MainWindow : public QWidget
{
    Q_OBJECT

public:
  explicit MainWindow(QWidget *parent = 0);
  ~MainWindow();

  void keyPressEvent(QKeyEvent *e);

  QSize sizeHint() const;
private:
  GLView *_glView;

};

#endif // MAINWINDOW_H

