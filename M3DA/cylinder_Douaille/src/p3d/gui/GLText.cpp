#include "glsupport.h"
#include <vector>
#include <QVector2D>

#include <iostream>

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/

using namespace std;

namespace p3d {
static QFont _fontQt("Helvetica",14,QFont::Bold);
static QVector2D _currentTextPos,_initTextPos;

static GLuint _textShaderP=0; // program shader to render text
static GLuint _textShaderV=0; // vertex shader to render text
static GLuint _textShaderF=0; // fragment shader to render text
static GLuint _textVAO;


void endGLText() {
  glDeleteShader(_textShaderV);
  glDeleteShader(_textShaderF);
  glDeleteProgram(_textShaderP);
}


void initGLText() {

}


GLuint toTexture(const std::string &s,int *width,int *height) {
  QString qs=QString::fromStdString(s);
  QFontMetrics metrics(_fontQt);
  QSize sText=metrics.size(Qt::TextSingleLine,qs);
  *width=sText.width();
  *height=sText.height()*1.2; // TODO : ??
  QImage img(*width,*height,QImage::Format_ARGB32);
  img.fill(QColor::fromRgbF(0,0,0,0));
  QPainter p;
  p.begin(&img);
  p.setFont(_fontQt);p.setPen(QColor::fromRgbF(1,1,1,1));
  p.drawText(QPoint(0,*height/1.2),qs);
  p.end();

 // img.save("tchouc.jpg");

  GLuint texId;
  glGenTextures(1,&texId);
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D,texId);

  glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,*width,*height,0,GL_BGRA,GL_UNSIGNED_BYTE,img.bits());
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);

  glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
  glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);

  return texId;
}

void initTextShader() {
  if (!_textShaderP) {
    string shaderv =
        "#version 130\n"
        "uniform mat4 projection;"
        "in vec2 texCoord0;"
        "in vec3 position;"
        "out vec2 fTexCoord0;"
        "void main() {"
        "  fTexCoord0=texCoord0;"
        "  gl_Position=projection*vec4(position,1.0);"
        "}";
    string shaderf =
        "#version 130\n"
        "uniform sampler2D texture0;"
        "uniform vec4 colorText;"
        "in vec2 fTexCoord0;"
        "out vec4 color;"
        "void main() {"
        "  color=colorText*texture(texture0,fTexCoord0);"
        "  if (color.a<0.5) gl_FragDepth=1;"
        "  else gl_FragDepth=gl_FragCoord.z;"
        "}";

    _textShaderP=glCreateProgram();
    _textShaderV=glCreateShader(GL_VERTEX_SHADER);
    _textShaderF=glCreateShader(GL_FRAGMENT_SHADER);
    glAttachShader(_textShaderP,_textShaderV);
    glAttachShader(_textShaderP,_textShaderF);

    const char *buffer;
    buffer=shaderv.data();
    glShaderSource(_textShaderV,1,&buffer,NULL);
    buffer=shaderf.data();
    glShaderSource(_textShaderF,1,&buffer,NULL);

    glCompileShader(_textShaderV);
    glCompileShader(_textShaderF);

    glBindAttribLocation(_textShaderP,0,"position");
    glBindAttribLocation(_textShaderP,1,"texCoord0");
    glLinkProgram(_textShaderP);
  }
}

void drawText(const std::string &s,double x,double y,double z,const float *modelview,const float *projection,const float *color) {
  // init textures and shader (shaders are made the first time only; texture is built each time)
    if (s=="") return;

    GLint saveProgram;
  glGetIntegerv(GL_CURRENT_PROGRAM,&saveProgram);
  int width,height;
  GLuint texId=toTexture(s,&width,&height);

  initTextShader();

  // compute the square vertices
  GLint viewport[4];
  glGetIntegerv(GL_VIEWPORT,viewport);


  float fwidth=2.0*width/viewport[2]; // width in NDC
  float fheight=2.0*height/viewport[3]; // height in NDC
  float posx=x;float posy=y;float posz=z;
  if (modelview) { // transform by modelview
    float posw=modelview[3]*x+modelview[7]*y+modelview[11]*z+modelview[15];
    posx=(modelview[0]*x+modelview[4]*y+modelview[8]*z+modelview[12])/posw;
    posy=(modelview[1]*x+modelview[5]*y+modelview[9]*z+modelview[13])/posw;
    posz=(modelview[2]*x+modelview[6]*y+modelview[10]*z+modelview[14])/posw;
  }
  vector<float> vertex={posx,posy+fheight,posz,0,0,
                        posx,posy,posz,0,1,
                        posx+fwidth,posy+fheight,posz,1,0,
                        posx+fwidth,posy,posz,1,1};
  // vertex to gl : TODO : generate once (then SubBufferData)


  GLuint vbuffer;
  glGenVertexArrays(1,&_textVAO);
  glBindVertexArray(_textVAO);
  glGenBuffers(1,&vbuffer);
  glBindBuffer(GL_ARRAY_BUFFER,vbuffer);
  glBufferData(GL_ARRAY_BUFFER,vertex.size()*sizeof(float),vertex.data(),GL_STREAM_DRAW);


  // draw (uniform setup)
  glUseProgram(_textShaderP);
  glUniform1i(glGetUniformLocation(_textShaderP,"texture0"),0);
  if (projection) {
    glUniformMatrix4fv(glGetUniformLocation(_textShaderP,"projection"),1,GL_FALSE,projection);
  }
  else {
    float identity[]={1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    glUniformMatrix4fv(glGetUniformLocation(_textShaderP,"projection"),1,GL_FALSE,identity);
  }

  if (color) {
    glUniform4fv(glGetUniformLocation(_textShaderP,"colorText"),1,color);
  }
  else {
    float c[4]={1,0,0,1};
    glUniform4fv(glGetUniformLocation(_textShaderP,"colorText"),1,c);
  }

  // draw (attribute setup)
  glEnableVertexAttribArray(0);
  glEnableVertexAttribArray(1);
  glVertexAttribPointer(0,3,GL_FLOAT,GL_FALSE,5*sizeof(GLfloat),NULL);
  glVertexAttribPointer(1,2,GL_FLOAT,GL_FALSE,5*sizeof(GLfloat),(GLvoid *)(3*sizeof(GLfloat)));
  // draw
  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
  glDrawArrays(GL_TRIANGLE_STRIP,0,4);


  glDisable(GL_BLEND);
  glDisableVertexAttribArray(0);
  glDisableVertexAttribArray(1);

  glDeleteBuffers(1,&vbuffer);
  glUseProgram(0);
  glBindVertexArray(0);
  glDeleteTextures(1,&texId);
  glDeleteVertexArrays(1,&_textVAO);
  glUseProgram(saveProgram);
}

} // namespace p3d

