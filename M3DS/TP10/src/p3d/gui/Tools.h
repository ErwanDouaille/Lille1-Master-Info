#ifndef TOOLS_H
#define TOOLS_H

#include "glsupport.h"
#include <string>
#include <exception>
#include <QFileInfo>
#include <iomanip>
#include <sstream>

/**
@author F. Aubert

Usage : throw ErrorD(message)
*/


namespace p3d {

// exception
class Error:public std::exception {
    std::string message;
    public:
    Error(std::string msg,int line,std::string fichier) throw();
    virtual ~Error() throw();

    virtual const char * what() const throw();

    void show() throw();

};

std::string errorToString(GLenum error);
void checkError(const std::string &mesg,int line,const std::string &file);


std::string mediaPath();
void checkFile(const QFileInfo &filename);
QFileInfo resourceFile(const std::string &resourceName);
std::string readTextFile(const std::string &filename);
void mediaPath(const std::string &path);
void mediaPathRelativeFile(const std::string &filename,const std::string &dir);
void captureImage(int x, int y, int w, int h);
void startTime();
double elapsedTime();
double elapsedStartTime();

}

template<typename T> std::string convertToString(const T &a,int nbDigit=0) {
  std::ostringstream os;
  std::string s;
  if (nbDigit!=0) os << std::setw(nbDigit) << std::setfill('0');
  os <<  a;
  return os.str();
}

template<typename T> T convertToNumber(const std::string &s) {
  T res;
  std::stringstream ss;
  ss << s;
  ss >> res;
  return res;
}


#define ErrorD(s) (p3d::Error(s,__LINE__,__FILE__))
#define checkGLErrorD(s) (p3d::checkError(s,__LINE__,__FILE__))

#endif // TOOLS_H

