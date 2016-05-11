
/**

  @author F. Aubert
  **/

#include <QtGui/QApplication>

#include "MainWindow.h"

int main(int argc, char *argv[])
{
  // initialize the Qt application
  QApplication application(argc, argv);

  // create and show the main window
  MainWindow *mainWindow=new MainWindow(NULL);
  mainWindow->show();

  // run the qt application loop (events dispatcher)
  int finished = application.exec();

  // application is finished => clean up code
  delete mainWindow;
  return finished;
}

