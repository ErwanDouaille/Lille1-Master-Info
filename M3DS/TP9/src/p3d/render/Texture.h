#ifndef INC_TEXTURE_H
#define INC_TEXTURE_H

#include "glsupport.h"
#include <string>
#include "Vector2.h"
#include "Matrix4.h"
#include <QImage>

/**
@file
@author F. Aubert
@brief Encapsulates common texture operations
*/


namespace p3d {
/**
@class Texture
@brief Encapsulates common texture operations
*/
class Texture {
  GLuint _width,_height,_depth;
	GLuint _texId;
  GLint _filterMin,_filterMag;
  GLenum _target;
  GLenum _internalFormat;
  GLenum _dataFormat;
  GLenum _dataType;
  GLenum _wrap; /// same wrap in 3 directions
	bool _generateMipMap;
  QImage *_img;
  std::string _name;

	void init();  
  void toGL();
  void toImage();

  /// !caution : do not clone texture (clone _img (RAM) or/and _texId (GL) ? + require resource manager for GL memory)
  Texture(const Texture &) {}
  /// !caution : do not clone texture (clone _img (RAM) or/and _texId (GL) ? + require resource manager for GL memory)
  Texture &operator=(const Texture &) {return *this;}
  /// init from QImage
  void init(const QImage &img);
  /// empty texture of size wxhxd (d=0 => texture 2D)
  void init(int w,int h,int d,GLenum format=GL_RGBA);
  /// init data of the GL texture with size wxhxd (d=0 => texture 2D) (!TODO : may be private...)
  void init(int w,int h,int d,const GLvoid *data,GLenum format=GL_RGBA);
  /// set GL texels with data. The QImage _img is not modified (!! caution : coherence of data with format, width, height, etc is not checked !)
  void toGL(const GLvoid *data,int level=0);
public:
  virtual ~Texture();
  /// ctor
  Texture();
  /// ctor from image file
  Texture(const std::string &filename);
  void check();
  /// sets a filename to texture (useful to load later when opengl context is not valid)
  void filename(const std::string &n);
  /// reads from filename
  void read();
  /// set the texture with img (img is saved in _img property)
  void set(const QImage &img);
  /// set data of the texture !caution : data should be coherent with format, w, h,...
  void set(int w, int h, int d, GLenum format, const std::vector<float> &data=std::vector<float>(0));
  inline void set(int w, int h, GLenum format, const std::vector<float> &data=std::vector<float>(0)) {set(w,h,0,format,data);}
  inline void set(int w, GLenum format, const std::vector<float> &data=std::vector<float>(0)) {set(w,0,0,format,data);}
  /// set direct data to the texture
  void set(const std::vector<float> &data, int level=0);

  /// read and build mip map texture
  void readMipmap(const std::string &filename);
	/// read image file
	void read(const std::string &filename);
	/// read image file for the given mip map level
	void read(const std::string &filename,int level);

	/// get width
  inline unsigned int width() const {return _width;}
	/// get height
  inline unsigned int height() const {return _height;}
  /// get depth (i.e. size of the texture; is not relative to "depth map")
  inline unsigned int depth() const {return _depth;}

  /// this is the current opengl texture for the current texture unit
  void bind(int unit=0) const;
	/// get the texture id
  inline GLuint id() const {return _texId;}
  /// set GL texture parameter
  void parameter(unsigned int para,unsigned int mode);

  void filter(GLint filter=0);
  void filterMin(GLint filter=0);
  void filterMag(GLint filter=0);
  void filterLinear() {filter(GL_LINEAR);}
  void filterMipmap() {filterMin(GL_LINEAR_MIPMAP_LINEAR);filterMag(GL_LINEAR);}
  void filterNearest() {filter(GL_NEAREST);}
  /// set wrap (wrap = GL_CLAMP, GL_REPEAT, etc)
  void wrap(GLint wrap=0);
  void wrapRepeat() {wrap(GL_REPEAT);}
  void wrapClamp() {wrap(GL_CLAMP);}
  void transparency(bool active);
  void setLuminance(bool ok);
  void setAlpha(const p3d::Vector4 &c, double v,double radius);
  void setAlpha(double v);
  void generateMipmap();

  void makeChecker(int w, int h, int nbLine, int nbColumn, const Vector4 &c1, const Vector4 &c2);
  void toGL(const QImage &img);
  void mipmap(bool v);
  void toSubGL(const QImage &img);
  void toGL(GLenum dataFormat, GLenum dataType, const GLvoid *data, int level);
};


}

#endif

