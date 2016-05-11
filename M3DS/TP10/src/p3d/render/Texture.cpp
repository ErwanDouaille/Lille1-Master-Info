#include "Texture.h"
#include <iostream>
#include <cstdlib>
#include "Tools.h"
#include "GLRender.h"
#include <QDebug>


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


Texture::~Texture() {
  if (_img) delete _img;
  glDeleteTextures(1,&_texId);
}


Texture::Texture() {
    this->init();
}

void Texture::filename(const std::string &s) {
    _name=s;
}

void Texture::read() {
    read(_name);
}

Texture::Texture(const string &nom) {
  this->init();
  this->read(nom);
}

void Texture::init() {
    _texId=0;
    _generateMipMap=false;
    _img=NULL;
    _wrap=GL_REPEAT;
    _filterMin=GL_NEAREST;
    _filterMag=GL_NEAREST;
    _internalFormat=GL_RGBA;
    _dataFormat=GL_BGRA;
    _dataType=GL_FLOAT;
}


void Texture::filter(GLint filter) {
  filterMin(filter);
  filterMag(filter);
}

void Texture::filterMin(GLint filter) {
  if (filter!=0) _filterMin=filter;
  bind();
  glTexParameterf(_target,GL_TEXTURE_MIN_FILTER,_filterMin);
}

void Texture::filterMag(GLint filter) {
  if (filter!=0) _filterMag=filter;
  bind();
  glTexParameterf(_target,GL_TEXTURE_MAG_FILTER,_filterMag);
}

void Texture::wrap(GLint wrap) {
  if (wrap!=0) _wrap=wrap;
  bind();
  glTexParameterf(_target,GL_TEXTURE_WRAP_S,_wrap);
  glTexParameterf(_target,GL_TEXTURE_WRAP_T,_wrap);
  glTexParameterf(_target,GL_TEXTURE_WRAP_R,_wrap);
}


void Texture::check() {
  cout << "TARGET = ";
  switch(_target) {
    case GL_TEXTURE_1D:cout << "GL_TEXTURE_1D";break;
    case GL_TEXTURE_2D:cout << "GL_TEXTURE_2D";break;
    case GL_TEXTURE_3D:cout << "GL_TEXTURE_3D";break;
    default:cout << "error";
  }
  cout << endl;

  cout << "TEX ID = " << _texId << endl;
  cout << "INTERNAL FORMAT = ";
  switch(_internalFormat) {
    case GL_RED:cout << "GL_RED";break;
    case GL_RGBA:cout << "GL_RGBA";break;
    case GL_DEPTH_COMPONENT:cout << "GL_DEPTH_COMPONENT";break;
    default:cout << "error";
  }
  cout << endl;

  cout << "WIDTH =" << _width << " HEIGHT = " << _height << " DEPTH = " << _depth << endl;

  cout << "DATA FORMAT = ";
  switch(_dataFormat) {
    case GL_RED:cout << "GL_RED";break;
    case GL_BGRA:cout << "GL_BGRA";break;
    case GL_RGBA:cout << "GL_RGBA";break;
    case GL_DEPTH_COMPONENT:cout << "GL_DEPTH_COMPONENT";break;
    default:cout << "error";
  }
  cout << endl;

  cout << "DATA TYPE = ";
  switch(_dataType) {
    case GL_FLOAT:cout << "GL_FLOAT";break;
    case GL_UNSIGNED_BYTE:cout << "GL_UNSIGNED_BYTE";break;
    default:cout << "unknown";
  }
  cout << endl;

  cout << "MIN FILTER = ";
  switch(_filterMin) {
  case GL_NEAREST:cout << "GL_NEAREST";break;
  case GL_LINEAR:cout << "GL_LINEAR";break;
  case GL_LINEAR_MIPMAP_LINEAR:cout << "GL_LINEAR_MIPMAP_LINEAR";break;
  default:cout << "error";
  }
  cout << endl;

  cout << "MAG FILTER = ";
  switch(_filterMag) {
  case GL_NEAREST:cout << "GL_NEAREST";break;
  case GL_LINEAR:cout << "GL_LINEAR";break;
  case GL_LINEAR_MIPMAP_LINEAR:cout << "GL_LINEAR_MIPMAP_LINEAR";break;
  default:cout << "error";
  }
  cout << endl;

  cout << "WRAP = ";
  switch(_wrap) {
  case GL_REPEAT:cout << "GL_REPEAT";break;
  case GL_CLAMP:cout << "GL_CLAMP";break;
  default:cout << "error";
  }
  cout << endl;
  checkGLErrorD("check");

}

void Texture::toGL(const GLvoid *data,int level) {
  bind();
  if (_texId==0) throw ErrorD("Texture::init has not been called before Texture::toGL");
  switch(_target) {
    case GL_TEXTURE_1D:glTexImage1D(_target,level,_internalFormat,_width,0,_dataFormat,_dataType,data);
      break;
    case GL_TEXTURE_2D:glTexImage2D(_target,level,_internalFormat,_width,_height,0,_dataFormat,_dataType,data);
      break;
    case GL_TEXTURE_3D:glTexImage3D(_target,level,_internalFormat,_width,_height,_depth,0,_dataFormat,_dataType,data);
      break;
    default:throw ErrorD("_target undefined");
  }
}

void Texture::toGL(GLenum dataFormat,GLenum dataType,const GLvoid *data,int level) {
  _dataFormat=dataFormat;
  _dataType=dataType;
  toGL(data,level);
}


void Texture::init(const QImage &img) {
  if (!_img) _img=new QImage(img.width(),img.height(),QImage::Format_ARGB32);
  (*_img)=img.convertToFormat(QImage::Format_ARGB32);

  if (_img->isNull()) {
    throw ErrorD("QImage error in init(img)");
  }
  _dataType=GL_UNSIGNED_BYTE;
  _dataFormat=GL_BGRA;
  init(_img->width(),_img->height(),0,_img->mirrored().bits(),GL_RGBA);
}

void Texture::toGL(const QImage &img) {
  _dataType=GL_UNSIGNED_BYTE;
  _dataFormat=GL_BGRA;
  init(img.width(),img.height(),0,  img.convertToFormat(QImage::Format_ARGB32).mirrored().bits(),GL_RGBA);
}

void Texture::toSubGL(const QImage &img) {
  _dataType=GL_UNSIGNED_BYTE;
  _dataFormat=GL_BGRA;
  glTexSubImage2D(GL_TEXTURE_2D,0,0,0,img.width(),img.height(),_dataFormat, GL_UNSIGNED_BYTE, img.convertToFormat(QImage::Format_ARGB32).mirrored().bits());
}


void Texture::init(int w,int h,int d,GLenum format) {
  init(w,h,d,0,format);
}

void Texture::init(int w,int h,int d,const GLvoid *data,GLenum format) {
  if (_texId==0) {
    glGenTextures(1,&_texId);
  }
  _internalFormat=format;
  _width=w;_height=h;_depth=d;
  if (_filterMin==0) _filterMin=GL_NEAREST;
  if (_filterMag==0) _filterMag=GL_NEAREST;
  if (_wrap==0) _wrap=GL_REPEAT;
  if (_depth) _target=GL_TEXTURE_3D;
  else if (_height) _target=GL_TEXTURE_2D;
  else _target=GL_TEXTURE_1D;
  ////////////////////////////////////////////////
  toGL(data);
  filter();
  wrap();
  if (_generateMipMap) generateMipmap();
}

void Texture::set(const QImage &img) {
  init(img);
}

void Texture::set(int w,int h,int d,GLenum format,const vector<float> &data) {
  // TODO : check coherence
  const GLvoid *ddata=0;
  if (!data.empty()) ddata=data.data();
  _dataFormat=format;
  _dataType=GL_FLOAT;
  init(w,h,d,ddata,format);
}

void Texture::set(const std::vector<float> &data,int level) {
  // TODO : check coherence
  if (_texId==0) throw ErrorD("texture should be initialized before set direct data : use set(w,h,d,format) or set(QImage) or read(filename) before");
//  _dataFormat=format;
  _dataType=GL_FLOAT;
  toGL(data.data(),level);
}


void Texture::bind(int unit) const {
  if (unit>20 || unit<0) throw ErrorD("Texture::bind(i) : i should be in [0,20]");
  GLenum unitTarget=GL_TEXTURE0+unit;
  glActiveTexture(unitTarget);
  glBindTexture(_target,_texId);
}

void Texture::read(const string &resourceName) {
  QFileInfo resource=p3d::resourceFile(resourceName);
  if (_img==0) {
    _img=new QImage();
  }

  QImage load;
  load.load(resource.filePath());
  if (load.isNull()) throw ErrorD("cant load the format image : " +resource.absoluteFilePath().toStdString());
  set(load);
}

void Texture::readMipmap(const string &resourceName) {
  read(resourceName);
  generateMipmap();
}

void Texture::read(const string &resourceName,int level) {
  QFileInfo resource=p3d::resourceFile(resourceName);
  if (_texId==0) {
    throw Error("read with mipmap level only with an already created mipmap texture",__LINE__,__FILE__);
  }
  QImage img;
  img.load(resource.filePath());

  if (img.isNull()) {
    throw ErrorD("problem with QImage in read(\""+resource.absoluteFilePath().toStdString()+"\")");
  }
  _dataType=GL_UNSIGNED_BYTE;
  _dataFormat=GL_BGRA;
  // TODO : check coherence with requested level
  //init(_img->width(),_img->height(),0,_img->bits(),GL_RGBA);
  toGL(_img->mirrored().bits(),level);
}


void Texture::toImage() {
  // TODO
}


void Texture::mipmap(bool v) {
  _generateMipMap=v;
}

void Texture::generateMipmap() {
  glGenerateMipmap(GL_TEXTURE_2D); // TODO : check target and see if data=0 is not a problem
  filterMipmap();
}

void Texture::parameter(unsigned int para,unsigned int mode) {
  glBindTexture(_target,_texId);
  glTexParameteri(_target,para,mode);
}

void Texture::transparency(bool active) {
  if (active) {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
  }
  else
    glDisable(GL_BLEND);
}

void Texture::setAlpha(const Vector4 &c, double v, double radius) {
  if (_img!=NULL) {
    for(int i=0;i<_img->width();i++) {
      for(int j=0;j<_img->height();j++) {
        QColor cq=_img->pixel(i,j);
        double dist=Vector3(cq.redF()-c.r(),cq.greenF()-c.g(),cq.blueF()-c.b()).length();
        if (dist<radius)
          cq.setAlphaF(v);//1.0-(radius-dist)/radius);
        _img->setPixel(i,j,cq.rgba());
      }
    }
    init(*_img);
  }
}

void Texture::setAlpha(double v) {
  if (_img!=NULL) {
    for(int i=0;i<_img->width();i++) {
      for(int j=0;j<_img->height();j++) {
        QColor cq=QColor::fromRgba(_img->pixel(i,j));
        cq.setAlphaF(v);
        _img->setPixel(i,j,cq.rgba());
      }
    }
    init(*_img);
  }
}

void Texture::makeChecker(int w, int h, int nbLine, int nbColumn, const Vector4 &c1, const Vector4 &c2) {
  double stepLine=double(h)/nbLine;
  double stepColumn=double(w)/nbColumn;
  double x=0;
  double y=0;
  bool squareC1=false;
  QBrush b1=QBrush(QColor::fromRgbF(c1.r(),c1.g(),c1.b(),c1.a()),Qt::SolidPattern);
  QBrush b2=QBrush(QColor::fromRgbF(c2.r(),c2.g(),c2.b(),c2.a()),Qt::SolidPattern);


  QPainter p;
  QImage img(w,h,QImage::Format_ARGB32);
  img.fill(Qt::red);
  p.begin(&img);

  for(int i=0;i<nbColumn;++i) {
    squareC1=(i%2==0);
    y=0;
    for(int j=0;j<nbLine;++j) {
      if (squareC1) {
        p.setBrush(b1);
      }
      else {
        p.setBrush(b2);
      }
      p.setPen(p.brush().color());
      p.drawRect(x,y,stepColumn,stepLine);
      squareC1=!squareC1;
      y+=stepLine;
    }
    x+=stepColumn;
  }

  p.end();
  set(img);
}


