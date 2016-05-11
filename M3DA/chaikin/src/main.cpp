
/**

  @author F. Aubert
  **/

#include <QApplication>

#include "MainWindow.h"
#include <QGLFormat>


int main(int argc, char *argv[])
{
  // initialize the Qt application
  QApplication application(argc, argv);


  // create and show the main window
  MainWindow mainWindow;
  mainWindow.show();

  // run the qt application loop (events dispatcher)
  int finished = application.exec();

  // application is finished => clean up code
  return finished;
}
