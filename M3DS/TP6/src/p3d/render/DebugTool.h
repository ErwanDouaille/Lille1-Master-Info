#ifndef DEBUGTOOL_H
#define DEBUGTOOL_H

#include "GLTool.h"


namespace p3d {

class DebugTool {
public:
  virtual ~DebugTool();
  DebugTool();

  void point(const p3d::Vector3 &p, const std::string &label, const p3d::Vector3 color=p3d::Vector3(0,0,0));
  void line(const p3d::Line &l,const p3d::Vector3 &color);
  void segment(const p3d::Line &l,const std::string &labelA,const std::string &labelB,const p3d::Vector3 &color=p3d::Vector3(0,0,0));
  void frame(const p3d::Matrix4 m,const std::string &label);
  void direction(const p3d::Vector3 &p, const p3d::Vector3 &u, const std::string &label, const p3d::Vector3 &color);

  void drawPoint();
  void drawSegment();
  void draw();
  void clear();


private:
  std::vector<p3d::Vector3> _point;
  std::vector<std::string> _labelPoint;
  std::vector<p3d::Vector3> _colorPoint;

  std::vector<p3d::Line> _line;
  std::vector<p3d::Vector3> _colorLine;

  std::vector<p3d::Line> _segment;
  std::vector<p3d::Vector3> _colorSegment;

  std::vector<p3d::Vector3> _directionPoint;
  std::vector<p3d::Vector3> _direction;
  std::vector<std::string> _labelDirection;
  std::vector<p3d::Vector3> _colorDirection;

  std::vector<p3d::Matrix4> _frame;
};

void addDebug(const Vector3 &p1,const Vector3 &p2,const std::string &s,const Vector3 &color);
void clearDebug();
void drawDebug();
}

#endif // DEBUGTOOL_H

