#include "DebugTool.h"

#include <vector>

using namespace p3d;
using namespace std;

DebugTool::~DebugTool() {

}

DebugTool::DebugTool() {
}

void DebugTool::point(const p3d::Vector3 &p, const std::string &label,const p3d::Vector3 color) {
  _point.push_back(p);
  _labelPoint.push_back(label);
  _colorPoint.push_back(color);

}

void DebugTool::line(const p3d::Line &l, const Vector3 &color) {
  _line.push_back(l);
  _colorLine.push_back(color);
}

void DebugTool::segment(const p3d::Line &l, const std::string &labelA, const std::string &labelB,const p3d::Vector3 &color) {
  _segment.push_back(l);
  _colorSegment.push_back(color);

  point(l.point(0),labelA,color);
  point(l.point(1),labelB,color);
}

void DebugTool::direction(const p3d::Vector3 &,const p3d::Vector3 &,const std::string &,const p3d::Vector3 &) {

}

void DebugTool::frame(const p3d::Matrix4 m, const std::string &label) {
  _frame.push_back(m);

  point(m.column(4).project(),label);
  _point.push_back(Vector4(m.column(4)).project());
  _labelPoint.push_back(label);
}


void DebugTool::drawPoint() {
  p3d::shaderVertexColor();
  p3d::draw(GL_POINTS,_point,"position",_colorPoint,"color");
  for(unsigned int i=0;i<_point.size();++i) {
      Vector3 pos=_point[i];
      p3d::drawText(_labelPoint[i],pos.x(),pos.y(),pos.z(),
                    (p3d::projectionMatrix*p3d::modelviewMatrix).fv(),0,p3d::ambientColor.fv());
  }
}

void DebugTool::drawSegment() {
  vector<Vector3> pts;
  vector<Vector3> color;

  for(unsigned int i=0;i<_segment.size();++i) {
      pts.push_back(_segment[i].point(0));
      pts.push_back(_segment[i].point(1));
      color.push_back(_colorSegment[i]);
      color.push_back(_colorSegment[i]);

  }
  p3d::draw(GL_LINES,pts,"position",color,"color");
}

void DebugTool::draw() {

  drawPoint();
  drawSegment();

}

void DebugTool::clear() {
  _point.clear();
  _labelPoint.clear();
  _colorPoint.clear();


  _line.clear();
  _colorLine.clear();

  _segment.clear();
  _colorSegment.clear();

  _directionPoint.clear();
  _direction.clear();
  _labelDirection.clear();
  _colorDirection.clear();


  _frame.clear();

}


static DebugTool _debugTool;

void p3d::addDebug(const Vector3 &p1,const Vector3 &p2,const std::string &s,const Vector3 &color) {
  _debugTool.segment(Line(p1,p2-p1),s,"",color);
}

void p3d::clearDebug() {
  _debugTool.clear();
}

void p3d::drawDebug() {
  _debugTool.draw();
}


