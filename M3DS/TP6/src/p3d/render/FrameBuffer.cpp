#include "FrameBuffer.h"
#include "glsupport.h"

/*!
*
* @file
*
* @brief FrameBuffer
* @author F. Aubert
*
*/

using namespace std;
using namespace p3d;


FrameBuffer::~FrameBuffer() {
  glDeleteRenderbuffers(1,&_idRenderColor);
  glDeleteRenderbuffers(1,&_idRenderDepth);
  glDeleteFramebuffers(1,&_idFrame);
}

FrameBuffer::FrameBuffer()
{
  _idFrame=0;
  _idRenderColor=0;
  _idRenderDepth=0;
  _width=-1;
  _height=-1;
}


void FrameBuffer::attachDepth() {
  // attach a depth buffer
  if (!_idRenderDepth) glGenRenderbuffers(1,&_idRenderDepth);
  glBindRenderbuffer(GL_RENDERBUFFER,_idRenderDepth);
  glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH_COMPONENT,_width,_height);
  glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER,_idRenderDepth);
}

void FrameBuffer::attachColor() {
  if (!_idRenderColor) glGenRenderbuffers(1,&_idRenderColor);
  glBindRenderbuffer(GL_RENDERBUFFER,_idRenderColor);
  glRenderbufferStorage(GL_RENDERBUFFER,GL_RGBA,_width,_height);
  glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_RENDERBUFFER,_idRenderColor);
  // TODO : bind glReadPixel to the current buffer :
  //glFramebufferRenderbuffer(GL_READ_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_RENDERBUFFER,_idRender);
}

void FrameBuffer::attachColor(const p3d::Texture &texture) {
  // TODO : check texture format to be compatible with color attachment
  // TODO : check texture width and height
  // 0 = level
  glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,texture.id(),0);
}

void FrameBuffer::attachDepth(const p3d::Texture &texture) {
  // TODO : check texture format to be compatible with depth attachment
  // TODO : check texture width and height
  glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_TEXTURE_2D,texture.id(),0);
}



void FrameBuffer::create(int w,int h) {
  _width=w;_height=h;
  // generate fbo
  glGenFramebuffers(1,&_idFrame);
  glBindFramebuffer(GL_FRAMEBUFFER,_idFrame);


  glBindFramebuffer(GL_FRAMEBUFFER,0);

}

void FrameBuffer::begin() {
  glBindFramebuffer(GL_FRAMEBUFFER,_idFrame);
}

void FrameBuffer::end() {
  glBindFramebuffer(GL_FRAMEBUFFER,0);
}


void FrameBuffer::rtt(Texture *colorTexture,Texture *depthTexture) {
  glBindFramebuffer(GL_FRAMEBUFFER,_idFrame);
  if (colorTexture) {
    colorTexture->set(_width,_height,GL_RGBA);
    this->attachColor(*colorTexture);
  }
  else this->attachColor();
  if (depthTexture) {
    depthTexture->set(_width,_height,GL_DEPTH_COMPONENT);
    this->attachDepth(*depthTexture);

  }
  else this->attachDepth();
  glBindFramebuffer(GL_FRAMEBUFFER,0);
}


