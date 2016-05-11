#include "Camera2.h"
#include "Object3D.h"

#include "Matrix4.h"
#include <iostream>

using namespace p3d;
using namespace std;

Camera2::~Camera2() {
}

Camera2::Camera2() {
    _worldCamera.setIdentity();
    _projection.setIdentity();
    _viewX=_viewY=_viewWidth=_viewHeight;
}

void Camera2::frustum(double left, double right, double bottom, double top, double znear, double zfar) {
    _projection.setFrustum(left,right,bottom,top,znear,zfar);
}

void Camera2::viewport(int x, int y, int width, int height) {
    _viewX=x;_viewY=y;_viewWidth=width,_viewHeight=height;
}


const p3d::Matrix4 &Camera2::worldCamera() const {
    return _worldCamera;
}

p3d::Matrix4 Camera2::cameraWorld() const {
    return _worldCamera.inverse();
}

void Camera2::translate(double x,double y, double z,ECoordinate frame) {
    if (frame==Coordinate_Local) {
        _worldCamera*=Matrix4::fromTranslation(x,y,z);
    }
    else {
        _worldCamera=Matrix4::fromTranslation(x,y,z)*_worldCamera;
    }
}

void Camera2::translate(const Vector3 &t, ECoordinate frame) {
    translate(t.x(),t.y(),t.z(),frame);
}

void Camera2::rotate(double angle, double xAxis,double yAxis, double zAxis, ECoordinate frame) {
    if (frame==Coordinate_Local) {
        _worldCamera*=Matrix4::fromAngleAxis(angle,xAxis,yAxis,zAxis);
    }
    else {
        _worldCamera=Matrix4::fromAngleAxis(angle,xAxis,yAxis,zAxis)*_worldCamera;
    }
}

void Camera2::rotate(double angle, const Vector3 &axis, ECoordinate frame) {
    rotate(angle,axis.x(),axis.y(),axis.z(),frame);
}

p3d::Vector3 Camera2::pointTo(ECoordinate frame,const p3d::Vector3 &p) {
    if (frame==Coordinate_World) {
        return _worldCamera.transformPoint(p);
    }
    else {
        return _worldCamera.inverse().transformPoint(p);
    }
}

p3d::Vector3 Camera2::directionTo(ECoordinate frame,const p3d::Vector3 &u) {
    if (frame==Coordinate_World) {
        return _worldCamera.transformDirection(u);
    }
    else {
        return _worldCamera.inverse().transformDirection(u);
    }
}

void Camera2::applyGL() {
    p3d::projectionMatrix=_projection;
    p3d::modelviewMatrix=cameraWorld();
    glViewport(_viewX,_viewY,_viewWidth,_viewHeight);
}

p3d::Vector3 Camera2::windowToNDC(int x,int y) {


    double resX = ((double)x/(_viewWidth/2))-1;
    double resY = ((double)y/(_viewHeight/2))-1;
    double resZ = -1;
    Vector3 res(resX,resY,resZ);
    cout << "window to NDC : " << res << endl;
    return res;
}

p3d::Vector3 Camera2::windowToCamera(int x,int y) {
    Vector3 res(0,0,0);
    res = windowToNDC(x,y);
    res = _projection.inverse().transformPoint(res);
    return res;
}

p3d::Vector3 Camera2::windowToWorld(int x,int y) {
    Vector3 res(0,0,0);
    res = windowToCamera(x,y);
    res = worldCamera().transformPoint(res);
    return res;
}


p3d::Line Camera2::pickingRay(int x, int y) {
    Line res;
    // res.point(a), res.direction(u) to set the line (a and u are Vector3)
    res.point(worldCamera().transformPoint(Vector3(0,0,0)));
    Vector3 mWorld = windowToWorld(x,y);
    res.direction(mWorld-res.point());
    return res;
}



