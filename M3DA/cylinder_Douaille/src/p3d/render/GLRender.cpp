#include "GLRender.h"
#include "Shader.h"
#include "Matrix3.h"
#include "Matrix4.h"
#include "Vector3.h"
#include "Tools.h"

#include <iostream>

/**

  @author F. Aubert
  @brief offers a (very) simple gl renderer for common operations (global projection, modelview, material, common shaders)
  **/

using namespace std;

namespace p3d {

static bool _isRenderInit=false;

const unsigned int nbLight=4;
Shader *_shader;
Matrix4 projectionMatrix;
Matrix4 modelviewMatrix;
Matrix4 textureMatrix;
Matrix4 textureTransform;
Vector4 ambientColor;
Vector4 ambientBackColor;
Vector3 diffuseColor; // = front
Vector3 diffuseBackColor;
Vector3 specularColor;
vector<Vector4> lightPosition(nbLight);
vector<float> lightIntensity(nbLight);
float shininess;
float fragOffset;

Shader _shaderLightPhong;
Shader _shaderTextureLight;
Shader _shaderVertexAmbient;
Shader _shaderVertexColor;
Shader _shaderTextureAmbient;
Shader _shaderTextureTransform;
Shader _shaderTexture3DAmbient;
Shader _shaderTexture4DAmbient;
Shader _shaderTextureDepth;
Shader _textureTransform;

void initGLRender() {
  if (!_isRenderInit) {
    for(unsigned int i=0;i<lightPosition.size();++i) {
      lightPosition[i]=Vector4(0,0,1,0);
      lightIntensity[i]=0;
    }
    lightIntensity[0]=1;

    string savePath=p3d::mediaPath();
    p3d::mediaPath(string(RENDER_SHADER_PATH));
    _shaderLightPhong.attribute("position",0);
    _shaderLightPhong.attribute("normal",1);
    _shaderLightPhong.read("light_phong");

    _shaderTextureLight.attribute("position",0);
    _shaderTextureLight.attribute("normal",1);
    _shaderTextureLight.attribute("texCoord",2);
    _shaderTextureLight.read("texture_light");

    _shaderVertexAmbient.attribute("position",0);
    _shaderVertexAmbient.read("vertex_ambient");

    _shaderVertexColor.attribute("position",0);
    _shaderVertexColor.attribute("color",1);
    _shaderVertexColor.read("vertex_color");

    _shaderTextureAmbient.attribute("position",0);
    _shaderTextureAmbient.attribute("texCoord",2);
    _shaderTextureAmbient.read("texture_ambient");

    _shaderTexture3DAmbient.attribute("position",0);
    _shaderTexture3DAmbient.read("texture3D_ambient");

    _shaderTexture4DAmbient.attribute("position",0);
    _shaderTexture4DAmbient.attribute("texCoord",2);
    _shaderTexture4DAmbient.read("texture4D_ambient");

    _shaderTextureDepth.attribute("position",0);
    _shaderTextureDepth.attribute("texCoord",2);
    _shaderTextureDepth.read("texture_depth");

    _shaderTextureTransform.attribute("position",0);
    _shaderTextureTransform.read("texture_transform");


    modelviewMatrix.setIdentity();
    projectionMatrix.setIdentity();
    textureMatrix.setIdentity();
    textureTransform.setIdentity();

    lightPosition[0]=Vector4(0,0,0,1);
    _isRenderInit=true;
    p3d::mediaPath(savePath);
    p3d::fragOffset=0;

    ambientColor=Vector4(0.1,0.1,0.1);
    ambientBackColor=Vector4(0.1,0.1,0.1);
    diffuseColor=Vector3(0.8,0,0);
    diffuseBackColor=Vector3(0.2,0.8,0.0);
    specularColor=Vector3(0.7,0.1,0);
    shininess=100;
  }
}

void shaderLightPhong() {
  shader(&_shaderLightPhong);

  uniformTransformation();
  uniformMaterial();
  uniformLight();
}

void shaderVertexAmbient() {
  shader(&_shaderVertexAmbient);
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("ambientBack",ambientBackColor);
  _shader->uniform("offset",fragOffset);
}

void shaderVertexColor() {
  shader(&_shaderVertexColor);
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
  _shader->uniform("offset",fragOffset);
}

void shaderTextureAmbient() {
  shader(&_shaderTextureAmbient);
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
  _shader->uniform("textureMatrix",textureMatrix);
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("texture0",0);
}

void shaderTextureTransform() {
  shader(&_shaderTextureTransform);
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
  _shader->uniform("textureMatrix",textureMatrix);
  _shader->uniform("textureTransform",textureTransform);
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("texture0",0);
}

void shaderTexture3DAmbient() {
  shader(&_shaderTexture3DAmbient);
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("texture0",0);
}

void shaderTexture4DAmbient() {
  shader(&_shaderTexture4DAmbient);
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("texture0",0);
}


void shaderTextureDepth() {
  shader(&_shaderTextureDepth);
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("texture0",0);
}

void shaderTextureLight() {
  shader(&_shaderTextureLight);
  uniformTransformation();
  uniformLight();
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("textureMatrix",textureMatrix);
  _shader->uniform("texture0",0);
}

void uniformProjection() {
  _shader->uniform("projectionMatrix",projectionMatrix);
}

void uniformModelview() {
  _shader->uniform("modelviewMatrix",modelviewMatrix);
  _shader->uniform("normalMatrix",modelviewMatrix.normalMatrix());
}

void uniformTransformation() {
  uniformProjection();
  uniformModelview();
  uniformMVP();
}

void uniformMVP() {
  _shader->uniform("mvp",projectionMatrix*modelviewMatrix);
}

void uniformAmbient() {
  _shader->uniform("ambient",ambientColor);
}

void uniformAmbient(const Vector4 &c) {
  ambientColor=c;
  uniformAmbient();
}

void uniformAmbient(const Vector3 &c) {
  ambientColor=Vector4(c,1);
  uniformAmbient();
}

void uniformAmbient(double x,double y,double z) {
  ambientColor=Vector4(x,y,z,1);
  uniformAmbient();
}

void uniformMaterial() {
  _shader->uniform("ambient",ambientColor);
  _shader->uniform("materialFrontDiffuse",diffuseColor);
  _shader->uniform("materialBackDiffuse",diffuseBackColor);
  _shader->uniform("materialSpecular",specularColor);
  _shader->uniform("materialShininess",shininess);
}

void uniformLight() {
  for(unsigned int i=0;i<lightPosition.size();++i) {
    QString is=QString::number(i);
    // TODO : template this kind of thing
    _shader->uniform((QString("lightPosition[")+is+"]").toStdString(),lightPosition[i]);
    _shader->uniform((QString("lightIntensity[")+is+"]").toStdString(),lightIntensity[i]);
  }
}

void material(const p3d::Vector4 &ambient,const p3d::Vector3 &diffuse,const p3d::Vector3 &specular,int shininess) {
  p3d::ambientColor=ambient;p3d::diffuseColor=diffuse;p3d::diffuseBackColor=diffuse;p3d::specularColor=specular;p3d::shininess=shininess;
}

void materialBlueGreen() {
  material(Vector4(0,0.1,0.1,1),Vector3(0,0.3,0.7),Vector3(0,0.8,0.2),200);
}

void materialFrontBack() {
  material(Vector4(0.1,0.1,0.1,1),Vector3(0.8,0.1,0.0),Vector3(0.6,0.6,0.6),200);
  diffuseBackColor=Vector3(0,0.8,0);
}


void shader(Shader *s) {
  _shader=s;
  _shader->use();
}

Shader *currentShader() {
  return _shader;
}





} // namespace p3d

