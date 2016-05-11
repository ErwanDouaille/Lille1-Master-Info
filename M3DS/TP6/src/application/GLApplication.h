#ifndef GLAPPLICATION_H
#define GLAPPLICATION_H


#include "GLWidget.h"
#include "Camera.h"
#include "MeshObject3D.h"
#include "Texture.h"
#include "Shader.h"
#include "FrameBuffer.h"

#include <string>


class GLApplication : public GLWidget {
  Q_OBJECT
public:


  virtual ~GLApplication();
  GLApplication();
  /** ***** **/
  /** GLApplication must implement these methods : */
  virtual void initialize();
  virtual void update();
  virtual void draw();
  virtual void resize(int width,int height);
  /** ***** **/
  virtual void leftPanel(int i, const std::string &s);
  QSize sizeHint() const {return QSize(512,512);}
  /** ***** **/

  void updateCamera();

  void drawLight();
  void drawGround();
  void drawEarth();
  void drawObject();

  void lightPosition();


  void useCurrentShader();
  void useAmbientTextureShader();

  void renderToTexture();

  void drawIncrustation();

private:
  p3d::Camera _camera;
  double _frustum;
  bool _orbitalCamera;

  bool _animate;

  p3d::Vector4 _materialAmbient;
  p3d::Vector3 _materialDiffuse,_materialSpecular;

  p3d::Texture _groundTexture,_earthTexture,_univLille1;
  p3d::Texture _depthTexture;

  p3d::FrameBuffer _rtt;

  p3d::Shader _perPixelLighting,_perVertexLighting,_shadowMap,_textureTransform,_ambientTexture;

  p3d::Shader *_currentShader,*_firstPassShader,*_secondPassShader;

  p3d::MeshObject3D _airplane,_al,_car;

  p3d::Matrix4 _lightMatrix,_earthMatrix,_textureEyeMatrix;
  p3d::Vector3 _lightPositionEye;

  double _moveAngle,_angleStep,_moveZ,_zStep,_planeAngle;
  bool _drawDepthTexture;


};

#endif // GLAPPLICATION_H



