#ifndef GLTOOL_H
#define GLTOOL_H

#include "GLPrimitive.h"
#include "GLText.h"
#include "Camera.h"
#include "Tools.h"

/**
@file
@author F. Aubert
@brief Useful draw : arrow, ground, texture
*/

namespace p3d {

void initGLTool();

void drawArrow(const p3d::Vector3 &a,const p3d::Vector3 &u,double radius,const std::string &s1="",const std::string &s2="",double arrowRatio=0.1);
void drawGround(const p3d::Texture &texture, bool visible=true);
void drawTexture(const p3d::Texture &texture, double x=0, double y=0, double w=0.5, double h=0.5, bool depthTexture=false);
void draw(const std::string &s,const Vector3 &pos);
void draw(const std::string &s,const Vector2 &pos);
void draw(int v,const Vector3 &pos);
void apply(const p3d::Camera &c); // dont want OpenGL functions in Camera.
void drawThickLineStrip(const std::vector<Vector3> &p);

}

#endif // GLTOOL_H

