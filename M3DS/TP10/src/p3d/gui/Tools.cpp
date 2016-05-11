#include "Tools.h"

#include <stdlib.h>
#include <iostream>
#include <string>
#include <iostream>
#include <sstream>
#include <QFile>
#include <QTextStream>
#include <QDir>
#include <QDebug>

#include <ctime>

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/
using namespace std;

using namespace p3d;

static QDir _mediaPath(QString(MEDIA_PATH));


static bool _firstElapsed;
long int _lastStart;
long int _lastElapsed;

Error::Error(string mesg, int line,string fichier) throw() {
        std::ostringstream oss;

            string base;
    unsigned int index;

#ifdef _WIN32
#define SEARCH_CHAR "\\"
#else
#define SEARCH_CHAR "/"
#endif
    index=fichier.rfind(SEARCH_CHAR);
    index=fichier.rfind(SEARCH_CHAR,index-1);
    base=fichier.substr(index+1,fichier.length()-index-1);


        oss << "Error in "<< base << " line " << line << " : " << mesg;
        this->message = oss.str();
}

const char * Error::what() const throw() {
     return this->message.c_str();
}


void Error::show() throw() {
    cout << message << endl;
}

Error::~Error() throw() {}

/** UTILS **/
std::string p3d::errorToString(GLenum error) {
  switch(error) {
    case GL_INVALID_ENUM:return "GL_INVALID_ENUM";
    case GL_INVALID_VALUE:return "GL_INVALID_VALUE";
    case GL_INVALID_OPERATION:return "GL_INVALID_OPERATION";
    case GL_INVALID_FRAMEBUFFER_OPERATION:return "GL_INVALID_FRAMEBUFFER_OPERATION";
    case GL_OUT_OF_MEMORY:return "GL_OUT_OF_MEMORY";
    case GL_STACK_UNDERFLOW:return "GL_STACK_UNDERFLOW";
    case GL_STACK_OVERFLOW:return "GL_STACK_OVERFLOW";
    default:return "UNKNOWN ERROR";
  }
}


void p3d::checkError(const std::string &mesg,int line,const std::string &file) {

  std::cout << "==============================" << std::endl;
  GLenum err=glGetError();
  if (err!=GL_NO_ERROR) {
    std::cout << "GL error : " << errorToString(err) << " from " << file << " at line " << line << " ("+mesg+")" << std::endl;
    throw ErrorD("GL error");
  } else {
    std::cout << "no GL error : " << " from " << file << " at line " << line << " ("+mesg+")" << std::endl;
    std::cout << "==============================" << std::endl;
    }
}


std::string p3d::mediaPath() {
  return _mediaPath.absolutePath().toStdString();
}

void p3d::checkFile(const QFileInfo &file) {
  if (!file.exists())
    throw ErrorD("cant locate file "+file.absoluteFilePath().toStdString()+" (media path = "+_mediaPath.absolutePath().toStdString()+")");
}

QFileInfo p3d::resourceFile(const std::string &filename) {
  QFileInfo res;
  res.setFile(_mediaPath,QString::fromStdString(filename));
  checkFile(res);
  return res;
}

void p3d::mediaPath(const std::string &dir) {
  _mediaPath.setPath(QString::fromStdString(dir));
}

void mediaDirectoryRelativeFile(const std::string &filename,const std::string &dir) {
  _mediaPath.setPath(QFileInfo(QString::fromStdString(filename)).absolutePath()+"/"+QString::fromStdString(dir));
}

void p3d::captureImage(int x,int y,int w,int h) {
  QString name("capture_%1.jpg");
  QString fullname;
  int index=-1;
  QFileInfo file;
  do {
    index++;
    fullname=name.arg(index,4,10,QChar('0'));
    file.setFile(_mediaPath,"snapshot/"+fullname);
  }
  while(file.exists());
  unsigned char *pixels=new unsigned char[w*h*4];
  glReadPixels(x,y,w,h,GL_BGRA,GL_UNSIGNED_BYTE,pixels);
  QImage img=QImage(pixels,w,h,QImage::Format_ARGB32);
  img=img.mirrored(false,true);
  img.save(file.absoluteFilePath(),"JPG");
  cout << file.absoluteFilePath().toStdString() << " saved" << endl;
  delete[] pixels;
}

std::string p3d::readTextFile(const std::string &filename) {
  QFileInfo finfo=resourceFile(filename);
  QFile f(finfo.absoluteFilePath());
  f.open(QIODevice::ReadOnly);
  QTextStream t(&f);
  QString s=t.readAll();
  return s.toStdString();
}


void p3d::startTime() {_lastStart=clock();}
double p3d::elapsedStartTime() {return double(clock()-_lastStart)/CLOCKS_PER_SEC;}
double p3d::elapsedTime() {
  double res;
  time_t t=clock();
  if (_firstElapsed) {
    _lastElapsed=t;
    _firstElapsed=false;
  }
  res=t-_lastElapsed;
  _lastElapsed=t;
  return res/double(CLOCKS_PER_SEC);
}




