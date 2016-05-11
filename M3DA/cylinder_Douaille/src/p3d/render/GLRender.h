#ifndef GLRENDER_H
#define GLRENDER_H

#include "glsupport.h"

#include <vector>
#include <string>

#include "Vector4.h"
#include "Vector3.h"
#include "Matrix4.h"
#include "Shader.h"

/**
@file
@author F. Aubert
@brief Simple GL renderer
*/

namespace p3d {

void initGLRender();

extern Matrix4 projectionMatrix;
extern Matrix4 modelviewMatrix;
extern Matrix4 textureMatrix;
extern Matrix4 textureTransform;

extern Vector4 ambientColor;
extern Vector4 ambientBackColor;
extern Vector3 diffuseColor;
extern Vector3 diffuseBackColor;
extern Vector3 specularColor;
extern std::vector<Vector4> lightPosition;
extern std::vector<float> lightIntensity;
extern float shininess;
extern Vector4 color;
extern float fragOffset;
extern const unsigned int nbLight;

void uniformTransformation(); // shader has : modelviewMatrix, projectionMatrix, normalMatrix *and* mvp. normalMatrix is computed from modelviewMatrix; TODO : projectionMatrix should be useless
void uniformMVP(); // shader has : mvp. mvp is computed from projectionMatrix*modelviewMatrix
void uniformModelview(); // shader has : modelviewMatrix and normalMatrix
void uniformProjection(); // shader has : projectionMatrix
void uniformLight(); // shader has : lightPosition
void uniformMaterial(); // shader has : material.ambient, material.diffuse, material.specular, material.shininess
void uniformAmbient();
void uniformAmbient(const Vector4 &c);
void uniformAmbient(const Vector3 &c);
void uniformAmbient(double x,double y,double z);

void shaderLightPhong();  // enable and init uniform
void shaderTextureLight();
void shaderVertexAmbient();
void shaderVertexColor();
void shaderTextureAmbient();
void shaderTextureTransform();
void shaderTexture3DAmbient();
void shaderTexture4DAmbient();
void shaderTextureDepth();

void material(const p3d::Vector4 &ambientColor,const p3d::Vector3 &diffuseColor,const p3d::Vector3 &specularColor,int shininess);
void materialBlueGreen();
void materialFrontBack();
void shader(p3d::Shader *s);
p3d::Shader *currentShader();

}




#endif // GLUTILRENDER_H

