#ifndef GLWIDGET_H
#define GLWIDGET_H

#include "glsupport.h"

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

class GLWidget : public QGLWidget {
  Q_OBJECT

  // Interaction
  bool _mouseLeftButton=false; //! to detect when mouse left button is down (true until the button is released).
  bool _mouseRightButton=false;
  bool _mouseLeftPressed=false; //! to detect when mouse left button is pressed (set back to false when mouseLeftPressed() is called)
  bool _mouseRightPressed=false;

  bool _pressAccelerateWheel=false;
  bool _pressDecelerateWheel=false;
  int  _width=0, _height=0;
  int  _mouseX=0;
  int  _mouseY=0;
  int  _oldMouseX=0;
  int  _oldMouseY=0;
  int  _deltaMouseX=0;
  int  _deltaMouseY=0;

  std::map<int,bool> _key;
  std::map<int,bool> _keyPressed;
  int _lastKeyPressed;

  bool _firstElapsed=true;
  time_t _lastElapsed;
  time_t _lastStart;
  bool _snapshot;


protected:

public:
  explicit GLWidget();
  virtual ~GLWidget();

/** interface for child class (typically named GLApplication) **/
  virtual void initialize()=0;
  virtual void resize(int width,int height)=0;
  virtual void update()=0;
  virtual void draw()=0;

/** Interaction helpers **/
  bool mouseLeft();
  bool mouseRight();
  bool mouseLeftPressed();
  bool mouseRightPressed();

  void mouseLeft(bool v);
  void mouseRight(bool v);

  void dimension(int w,int h);
  int width();
  int height();

  void mouse(int x,int y);

  int mouseX();
  int mouseY();
  int deltaMouseX();
  int deltaMouseY();

  bool left();
  bool right();
  bool forward();
  bool backward();
  bool down();
  bool up();
  bool rotateLeft();
  bool rotateRight();

  bool accelerateWheel();
  bool decelerateWheel();
  void accelerateWheel(bool v);
  void decelerateWheel(bool v);

  void key(int k,bool v);
  bool key(int k);
  bool keyPressed(int k);
  int keyPressed();

  void startTime();
  double elapsedStartTime(); ///! since last startTime
  double elapsedTime(); ///! since last elapsedTime

  void resetDeltaMouse();

  void snapshot();
  void requestSnapshot();

  // public members
  QStringList _leftPanelMenu;

  //! default call when a button in left panel is clicked
  virtual void leftPanel(int i,const std::string &s);

  QSize sizeHint() const {return QSize(200,200);}

protected:
  //! initialize data application
  void initData();
  //! initialize the OpenGL context
  void initializeGL();
  //! OpenGL drawing (called when updateGL() )
  void paintGL();
  //! should update projection (called when the widget is resized)
  void resizeGL(int width,int height);

  // events
  //! called when a mouse button is pressed in the widget
  void mousePressEvent(QMouseEvent *event);
  //! called when the mouse is moved in the widget
  void mouseMoveEvent(QMouseEvent *event);
  //! called when a mouse button is released in the widget
  void mouseReleaseEvent(QMouseEvent *event);
  //! called when a key is pressed in the widget
  void keyPressEvent( QKeyEvent *event );
  //! called when a key is released in the widget
  void keyReleaseEvent( QKeyEvent *event );
  //! called when the mouse wheel is turned
  void wheelEvent(QWheelEvent *event);



signals:
  void updateRequest();
public slots:
  //! called every 20ms by the MainWindow
  void updateData();
};

#endif // GLWIDGET_H

