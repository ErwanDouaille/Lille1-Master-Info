#ifndef FRAMEBUFFER_H
#define FRAMEBUFFER_H


#include "glsupport.h"
#include "Tools.h"

#include "Texture.h"

/*!
*
* @file
*
* @brief FrameBuffer
* @author F. Aubert
*
*/

namespace p3d {
class FrameBuffer
{
  GLuint _idFrame,_idRenderColor,_idRenderDepth;
  int _width,_height;
public:
  virtual ~FrameBuffer();
  FrameBuffer(const FrameBuffer &) {throw ErrorD("no clone FrameBuffer(FrameBuffer)");}
  FrameBuffer &operator=(const FrameBuffer &) {throw ErrorD("no clone FrameBuffer=FrameBuffer");return *this;}

  FrameBuffer();

  inline int width() {return _width;}
  inline int height() {return _height;}

  void create(int w,int h);
  void attachDepth();
  void attachColor();
  void attachColor(const p3d::Texture &texture);
  void attachDepth(const p3d::Texture &texture);

  void begin();
  void end();

  void rtt(p3d::Texture *colorTexture,p3d::Texture *depthTexture=NULL);

};
} // namespace p3d
#endif // FRAMEBUFFER_H

