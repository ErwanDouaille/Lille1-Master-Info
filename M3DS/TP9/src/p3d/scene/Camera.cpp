#include "Camera.h"

#include "glsupport.h"

#include "Vector2.h"
#include "Vector4.h"
#include "Matrix4.h"
#include "Tools.h"

#include <iostream>

/*!
*
* @file
*
* @brief
* @author F. Aubert
*
*/



using namespace p3d;
using namespace std;

Camera::Camera() {
  frustum(-1,1,-1,1,0.1,1000);
}

Camera::~Camera() {
}

void Camera::volumeView(double left,double right,double down,double top,double pnear,double pfar) {
  _volumeView[0]=left;
  _volumeView[1]=right;
  _volumeView[2]=down;
  _volumeView[3]=top;
  _volumeView[4]=pnear;
  _volumeView[5]=pfar;
}

void Camera::frustum(double left,double right,double down,double top,double pnear,double pfar) {
  volumeView(left,right,down,top,pnear,pfar);
  _projection=Projection_Frustum;
}

void Camera::ortho(double left,double right,double down,double top,double pnear,double pfar) {
  volumeView(left,right,down,top,pnear,pfar);
  _projection=Projection_Ortho;
}

void Camera::perspective(double fov,double znear,double zfar) {
  double fovr=M_PI*fov/180.0;
  double aspect=double(_viewport[2])/_viewport[3];
  _volumeView[2]=-tan(fovr)*znear;
  _volumeView[3]=tan(fovr)*znear;
  _volumeView[0]=_volumeView[2]*aspect;
  _volumeView[1]=_volumeView[3]*aspect;
  _volumeView[4]=znear;
  _volumeView[5]=zfar;
}

void Camera::viewport(unsigned int x,unsigned int y,unsigned int width,unsigned int height) {
  _viewport[0]=x;
  _viewport[1]=y;
  _viewport[2]=width;
  _viewport[3]=height;
}

Vector2 Camera::windowToNDC(double x, double y) const {
  double xWindow=(double(x-_viewport[0])/_viewport[2])*2-1;
  double yWindow=(double(y-_viewport[1])/_viewport[3])*2-1;
  return Vector2(xWindow,yWindow);
}

bool Camera::windowToCamera(double x, double y, Vector3 *mouseCam) const {
  Vector2 ndc=windowToNDC(x,y);
  if ((ndc.x()<-1) || (ndc.x()>1) || (ndc.y()<-1) || (ndc.y()>1)) {
    return false;
  }
  else {
    mouseCam->x((ndc.x()+1)/2.0*(_volumeView[1]-_volumeView[0])+_volumeView[0]);
    mouseCam->y((ndc.y()+1)/2.0*(_volumeView[3]-_volumeView[2])+_volumeView[2]);
    mouseCam->z(-_volumeView[4]);
    return true;
  }
}

Vector3 Camera::windowToCamera(double x,double y) const {
  Vector3 res;
  windowToCamera(x,y,&res);
  return res;
}

bool Camera::windowToCamera(double x, double y, const Vector3 &pointPlane, Vector3 *pointCam) {
  Vector2 ndc=windowToNDC(x,y);
  if ((ndc.x()<-1) || (ndc.x()>1) || (ndc.y()<-1) || (ndc.y()>1)) {
    return false;
  }
  else {
    Vector3 pCam=pointTo(Coordinate_Local,pointPlane);
    pointCam->x((ndc.x()+1)/2.0*(_volumeView[1]-_volumeView[0])+_volumeView[0]);
    pointCam->y((ndc.y()+1)/2.0*(_volumeView[3]-_volumeView[2])+_volumeView[2]);
    pointCam->z(-_volumeView[4]);
    if (_projection==Projection_Frustum) (*pointCam)*=pCam.z()/pointCam->z();
    else pointCam->z(pCam.z());
    return true;
  }
}

bool Camera::windowToWorld(const Vector2 &pointWindow,const Vector3 &pointPlane,Vector3 *pointCam) {
  if (windowToCamera(pointWindow,pointPlane,pointCam)) {
    pointTo(Coordinate_World,pointCam);
    return true;
  }
  else return false;
}



Matrix4 Camera::cameraWorld() const {
  Matrix4 result;
  result=worldLocal();
  result.invert();
  return result;
}

Matrix4 Camera::worldCamera() const {
  return worldLocal();
}

Matrix4 Camera::projectionMatrix() const {
  switch(_projection) {
    case Projection_Frustum:return Matrix4::fromFrustum(_volumeView[0],_volumeView[1],_volumeView[2],_volumeView[3],_volumeView[4],_volumeView[5]);
    case Projection_Ortho:return Matrix4::fromOrtho(_volumeView[0],_volumeView[1],_volumeView[2],_volumeView[3],_volumeView[4],_volumeView[5]);
    default:throw ErrorD("camera projection undefined");
  }
}


void Camera::forward(double k) {
  translate(Vector3(0,0,-k),Coordinate_Local);
}

void Camera::backward(double k) {
  translate(Vector3(0,0,k),Coordinate_Local);
}

void Camera::left(double k) {
  translate(Vector3(-k,0,0),Coordinate_Local);
}

void Camera::right(double k) {
  translate(Vector3(k,0,0),Coordinate_Local);
}



void Camera::setViewport(int x,int y,int w,int h) {
  _x=x;_y=y;_w=w;_h=h;
}


void Camera::lookAt(const Vector3 &at,const Vector3 &up) {
  Vector3 i,j,k;

  k=position()-at;
  i.setCross(up,k);
  if (i.length()<fabs(1e-4)) i.setCross(Vector3(0,0,-1),k); // TODO : hack

  j.setCross(k,i);

  i.normalize();
  j.normalize();
  k.normalize();

  Quaternion q;
  q.set(i,j,k);
  orientation(q);
}

bool Camera::windowToRayWorld(const Vector2 &mouse,Line *l) {
  bool ok;
  Vector3 mouseCam;
  ok=windowToCamera(mouse,&mouseCam);
  if (!ok) return false;
  else {
    Vector3 a;
    Vector3 u=mouseCam;
    if (_projection==Projection_Frustum) {
      a=Vector3(0,0,0);
      u=mouseCam;
    }
    else {
      a=mouseCam;
      u=Vector3(0,0,1);
    }
    pointTo(Coordinate_World,&a);
    directionTo(Coordinate_World,&u);
    l->set(a,u);
    return true;
  }
}

double Camera::windowToNDC(double r) {
  Vector2 p1=windowToNDC(Vector2(r,r));
  Vector2 p0=windowToNDC(Vector2(0,0));
  return (p1-p0).length2();
}

bool Camera::windowToWorld(const Vector2 &mouse,Vector3 *l) {
  bool ok;
  Vector3 mouseCam;
  ok=windowToCamera(mouse,&mouseCam);
  if (!ok) return false;
  else {
    *l=mouseCam;
    pointTo(Coordinate_World,l);
    return true;
  }
}


int Camera::selectNearest(const Vector2 &mouse,const std::vector<Vector3> &point,double radius) {
  return selectNearest(mouse,point,radius,Matrix4::identity());
}

/*
int Camera::selectNearest(const Vector2 &mouse,const std::vector<Vector4> &point,double radius) {
  vector<Vector3> point3;
  for(auto &p:point) {
    point3.push_back(p.project());
  }
  return selectNearest(mouse,point3,radius,Matrix4::identity());
}
*/

int Camera::selectNearest(const Vector2 &mouse,const std::vector<Vector3> &point,double radius,const Matrix4 &transformPoint) {
  Line ray;
  Matrix4 NDCtoLocal=projectionMatrix()*cameraWorld()*transformPoint;
  int imin=-1;
  Vector2 mouseNDC=windowToNDC(mouse);
  double radiusNDC=windowToNDC(radius);
  double dmin;
  double d;
  Vector3 pNDC;
  for(unsigned int i=0;i<point.size();++i) {
    pNDC=NDCtoLocal.transformPoint(point[i]);
    d=Vector2(pNDC.x(),pNDC.y()).distance2(mouseNDC);
    if (d<radiusNDC && (d<dmin || imin==-1)) {
      dmin=d;imin=i;
    }
  }
  return imin;
}




