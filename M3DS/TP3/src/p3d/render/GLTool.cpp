#include "GLTool.h"
#include "GLRender.h"
#include "Shader.h"
#include "Matrix4.h"
#include "Vector3.h"
#include "Vector4.h"
#include "VertexArray.h"
#include "Camera.h"

#include <vector>

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


void initGLTool() {
  initGLRender();
  initGLPrimitive();
}

void apply(const p3d::Camera &c) {
  p3d::projectionMatrix=c.projectionMatrix();
  p3d::modelviewMatrix=c.cameraWorld();
  glViewport(c.viewX(),c.viewY(),c.viewWidth(),c.viewHeight());
}

void drawGround(const p3d::Texture &texture,bool visible) {
  texture.bind(0);
  Vector4 saveLight=p3d::lightPosition[0];
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix.rotate(-90,Vector3(1,0,0));
  p3d::lightPosition[0]=p3d::modelviewMatrix.transform(Vector4(0,0,2));
  p3d::modelviewMatrix.scale(Vector3(50,50,1));
  if (visible)
    p3d::shaderTextureLight();
  else
    p3d::shaderTextureAmbient();

  p3d::drawSquare();

  p3d::modelviewMatrix.pop();
  p3d::lightPosition[0]=saveLight;
}

void drawTexture(const p3d::Texture &texture,double x,double y,double w,double h,bool depthTexture) {
  texture.bind(0);
  p3d::modelviewMatrix.push();
  p3d::projectionMatrix.push();
  p3d::projectionMatrix=Matrix4::fromOrtho(0,1,0,1,0,1);
  // square is drawn in [-1,1]
  p3d::modelviewMatrix.setIdentity();
  p3d::modelviewMatrix.translate(x,y,-0.00001);
  p3d::modelviewMatrix.scale(w/2.0,h/2.0,1);
  p3d::modelviewMatrix.translate(1,1,0);
  if (depthTexture)
    p3d::shaderTextureDepth();
  else
    p3d::shaderTextureAmbient();

  p3d::drawSquare();
  p3d::projectionMatrix.pop();
  p3d::modelviewMatrix.pop();
}

void drawArrow(const p3d::Vector3 &a,const p3d::Vector3 &u,double radius,const std::string &s1,const std::string &s2,double arrowRatio) {
  p3d::modelviewMatrix.push();

  /*
  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix.translate(a);
  p3d::shaderLightPhong();
  p3d::drawSphere();


  p3d::modelviewMatrix.translate(u);
  p3d::shaderLightPhong();
  p3d::drawSphere();
  p3d::modelviewMatrix.pop();
*/

  Quaternion q;
  q.setRotation(u,Vector3(0,0,1));
  double aqq;Vector3 uqq;
  q.toAngleAxis(&aqq,&uqq);
  p3d::modelviewMatrix.translate(a);
  p3d::modelviewMatrix.rotate(q);

  p3d::modelviewMatrix.push();
  p3d::modelviewMatrix.scale(radius,radius,u.length()*(1-arrowRatio));
  p3d::shaderLightPhong();
  p3d::drawCylinder();
  p3d::modelviewMatrix.pop();



  p3d::modelviewMatrix.translate(0,0,u.length()*1);
  p3d::modelviewMatrix.rotate(180,1,0,0);
  p3d::modelviewMatrix.scale(radius*2,radius*2,u.length()*arrowRatio);
  p3d::shaderLightPhong();
  p3d::drawCone();
  p3d::modelviewMatrix.pop();

  p3d::modelviewMatrix.push();

  if (s1!="")
  p3d::drawText(s1,a.x(),a.y(),a.z(),nullptr,(p3d::projectionMatrix*p3d::modelviewMatrix).fv());
  if (s2!="") {
    Vector3 end=a+u;
    p3d::drawText(s2,end.x(),end.y(),end.z(),nullptr,(p3d::projectionMatrix*p3d::modelviewMatrix).fv());
  }


  p3d::modelviewMatrix.pop();


}

void draw(const std::string &s,const Vector3 &pos) {
  p3d::drawText(s,pos.x(),pos.y(),pos.z(),(p3d::projectionMatrix*p3d::modelviewMatrix).fv(),nullptr);
}

void draw(const std::string &s,const Vector2 &pos) {
  p3d::drawText(s,pos.x(),pos.y(),0,nullptr,nullptr);
}

void draw(int v,const Vector3 &pos) {draw(toString(v),pos);}


void drawThickLineStrip(const std::vector<Vector3> &p) {
    Vector3 tp,oldtp;
    Vector3 dir;
    Matrix4 t=projectionMatrix*modelviewMatrix;
    vector<Vector3> pts;
    for(unsigned int i=0;i<p.size();++i) {
        tp=t.transformPoint(p[i]);
        if (i==0) {
            dir=Vector3(0,1,0);
        }
        else {
            dir=tp-oldtp;
            dir=Vector3(-dir.y(),dir.x(),0);
        }
        dir.normalize();
        dir*=0.01;
        pts.push_back(tp+dir);
        pts.push_back(tp-dir);
        oldtp=tp;

    }
    modelviewMatrix.push();
    projectionMatrix.push();
    modelviewMatrix.setIdentity();
    projectionMatrix.setIdentity();
    ambientColor=Vector4(1,0,0,1);
    shaderVertexAmbient();
    p3d::draw(GL_TRIANGLE_STRIP,pts);
    projectionMatrix.pop();
    modelviewMatrix.pop();
}


} // namespace p3d



