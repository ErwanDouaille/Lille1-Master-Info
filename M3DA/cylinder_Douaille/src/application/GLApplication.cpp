#include "GLApplication.h"
#include "GLTool.h"

#include "Vector3.h"
#include "Vector2.h"


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
using namespace p3d;

GLApplication::~GLApplication() {
}

GLApplication::GLApplication() {
    //
    _leftPanelMenu << "Draw square" << "Cross-section = circle" << "Draw cylinder (path as strip line)" << "Mouse Input Path" << "Draw cylinder (path as spline)" << "Mouse Input Section" << "Draw revolution";
    _activeMenu=0;

    _inputPath.clear();
    _inputCrossSection.clear();

    _camera2D.ortho(-2,2,-2,2,0,1);
    _camera3D.frustum(-_frustum,_frustum,-_frustum,_frustum,0.03,1000);
    _camera3D.position(0,0,10);
    _camera3D.lookAt(Vector3(0,0,0));

}


/** ********************************************************************** **/
void GLApplication::initialize() {
    // appelée 1 seule fois à l'initialisation du contexte
    // => initialisations OpenGL
    glClearColor(1,1,1,1);

    glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glClearDepth(1);

    p3d::initGLTool();
}

void GLApplication::resize(int width,int height) {
    // appelée à chaque dimensionnement du widget OpenGL
    // (inclus l'ouverture de la fenêtre)
    // => réglages liés à la taille de la fenêtre
    _camera2D.viewport(0,0,width,height);
    _camera3D.viewport(0,0,width,height);
    // ...
}

void GLApplication::update() {
    // appelée toutes les 20ms (60Hz)
    // => mettre à jour les données de l'application
    // avant l'affichage de la prochaine image (animation)
    // ...

    if (_activeMenu==3) {
        if (mouseLeftPressed()) {
            _inputPath.push_back(_camera2D.windowToWorld(mouseX(),mouseY()));
        }
    }
    if (_activeMenu==5) {
        if (mouseLeft()) {
            _inputCrossSection.push_back(_camera2D.windowToWorld(mouseX(),mouseY()).xy());
        }
    }
    if (_activeMenu==2 || _activeMenu==4 || _activeMenu==6) {
        updateCamera();
    }
}

/* ************************************************************ */

void GLApplication::updateCamera() {
    if (mouseLeft()) {
        Vector3 center=_camera3D.pointTo(Coordinate_Local,Vector3(0,0,0));
        Vector3 vertical=Vector3(0,1,0);
        _camera3D.translate(center,Coordinate_Local);
        _camera3D.rotate(-deltaMouseX()/2.0,vertical,Coordinate_Local);
        _camera3D.rotate(deltaMouseY()/2.0,Vector3(1,0,0),Coordinate_Local);
        _camera3D.translate(-center,Coordinate_Local);
    }
    if (left()) _camera3D.left(0.3);
    if (right()) _camera3D.right(0.3);
    if (forward()) _camera3D.forward(0.3);
    if (backward()) _camera3D.backward(0.3);
    if (accelerateWheel()) {
        _frustum*=1.05;
        _camera3D.frustum(-_frustum,_frustum,-_frustum,_frustum,0.03,1000);
    }
    if (decelerateWheel()) {
        _frustum/=1.05;
        _camera3D.frustum(-_frustum,_frustum,-_frustum,_frustum,0.03,1000);
    }
}


void GLApplication::draw() {
    // appelée après chaque update
    // => tracer toute l'image
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    if (_activeMenu==2 || _activeMenu==4 || _activeMenu==6)
        p3d::apply(_camera3D);
    else
        p3d::apply(_camera2D);

    switch(_activeMenu) {
    case 0:drawSquare();break;
    case 1:drawCrossSection();break;
    case 2:case 4:case 6:drawCylinder();break;

    case 3:drawSpline();break;

    case 5:drawCrossSection();break;
    default:break;
    }
}

/** ********************************************************************** **/
/** i = button number, s = button text
 */
void GLApplication::leftPanel(int i,const std::string &s) {
    _activeMenu=i;
    cout << _activeMenu << endl;
    switch (_activeMenu) {
    case 1: // button "Define cross section"

        //        crossSectionSquare();
        crossSectionCircle();

        break;
    case 2: // button "Draw cylinder (strip line)"
        pathSegment();
        crossSectionCircle();
        extrudeLineStrip();
        break;
    case 3:
        //        _inputPath.clear(); //
        break;
    case 4: // button "Draw cylinder (spline)"
        crossSectionCircle();
        extrudeSpline();
        break;
    case 6: // button "Draw revolution"
        pathCircle();
        extrudeSpline();
        break;
    default:break;
    }

    /*
  switch (i) {
    case 0:...;break;
    case 1:...;break;
    ...
  }
  */
}

/** ************************************************************************ **/
void GLApplication::drawSquare() {

    // compute points of the square
    vector<Vector2> pts;
    pts.resize(5);
    pts[0]=Vector2(-0.5,-0.5);
    pts[1]=Vector2(0.5,-0.5);
    pts[2]=Vector2(0.5,0.5);
    pts[3]=Vector2(-0.5,0.5);
    pts[4]=pts[0];

    // draw square
    p3d::ambientColor=Vector4(1,0,0,1);
    p3d::shaderVertexAmbient();
    p3d::drawLineStrip(pts,5);

    // draw vertices (points)
    glPointSize(10);
    p3d::ambientColor=Vector4(0,0,1,1);
    p3d::shaderVertexAmbient();
    p3d::drawPoints(pts);

    // draw text
    p3d::ambientColor=Vector4(1,0,1,1);
    p3d::draw("V0",Vector3(pts[0],0));


}


/** ************************************************************************ **/

void GLApplication::crossSectionSquare() {
    _inputCrossSection.clear();
    _inputCrossSection.push_back(Vector2(-1,-1));
    _inputCrossSection.push_back(Vector2(1,-1));
    _inputCrossSection.push_back(Vector2(1,1));
    _inputCrossSection.push_back(Vector2(-1,1));
    _inputCrossSection.push_back(Vector2(-1,-1));
}

void GLApplication::crossSectionCircle() {
    _inputCrossSection.clear();
    int r = 1, iteration = 30;
    for(int i = -1; i < iteration; i++)
    {
        float theta = 2.0f * 3.1415926f * float(i) / float(iteration);
        float x = r * cosf(theta);
        float y = r * sinf(theta);
        _inputCrossSection.push_back(Vector2(x + r/2, y + r/2));
    }
}


/** ************************************************************************ **/

void GLApplication::pathSegment() {

    //    _inputPath.clear();
    //    _inputPath.push_back(Vector3(0,0,-2));
    //    _inputPath.push_back(Vector3(0,0,2));
    _inputPath.clear();
    _inputPath.push_back(Vector3(-2,0,-2));
    _inputPath.push_back(Vector3(0,0,2));
    _inputPath.push_back(Vector3(2,0,-1));

}

void GLApplication::pathCircle() {
    _inputPath.clear();
    int r = 1, iteration = 40;
    for(int i = -1; i < iteration+1; i++)
    {
        float theta = 2.0f * 3.1415926f * float(i) / float(iteration);
        float x = r * cosf(theta);
        float y = r * sinf(theta);
        _inputPath.push_back(Vector3(0,x + r/2, y + r/2));
    }

}

/** ************************************************************************ **/

void GLApplication::drawCrossSection() {
    vector<Vector3> toDraw;
    for(Vector2 i : _inputCrossSection) {
        toDraw.push_back(Vector3(i, 0));
    }
    p3d::drawLineStrip(toDraw,toDraw.size());
}

void GLApplication::drawInputPath() {
    if (_inputPath.size()<1) return;

    p3d::ambientColor=Vector4(0,0,1,1);
    p3d::shaderVertexAmbient();
    p3d::drawPoints(_inputPath);
    p3d::drawLineStrip(_inputPath);
}


void GLApplication::drawCylinder() {
    if (_extrude.size()<4) return;
    int nbSlice=_inputCrossSection.size();

    p3d::ambientColor=Vector4(1,0,0,1);
    p3d::shaderVertexAmbient();


    drawGrid(_extrude,nbSlice);

    /*
   *  uncomment once normals computed
  p3d::lightPosition[0]=Vector4(0,0,10,1);
  p3d::lightIntensity[0]=1.0;
  p3d::material(Vector4(0,0,0.3,1),Vector3(0,0.2,0.8),Vector3(0,0.8,0.3),100);
  p3d::diffuseBackColor=Vector3(0.8,0,0);
  p3d::shaderLightPhong();
  fillGrid(_extrude,_normalExtrude,nbSlice);
  */
    //    drawInputPath();
    drawSpline();
}



void GLApplication::drawSpline() {
    if (_inputPath.size()>=2) {
        vector<Vector3> toDraw;
        toDraw.clear();
        int nbPts=100;
        double step=1.0/(nbPts-1);
        double t=0;
        for(int i=0;i<nbPts;i++) {
            toDraw.push_back(pointSpline(t));
            t+=step;
        }

        p3d::ambientColor=Vector4(0,0,1,1);
        p3d::shaderVertexAmbient();
        drawLineStrip(toDraw,toDraw.size());
    }
    if (_inputPath.size()>0) {
        p3d::ambientColor=Vector4(0,0,1,1);
        p3d::shaderVertexAmbient();
        p3d::drawPoints(_inputPath);
    }
}

/** ************************************************************************* **/

Vector3 GLApplication::transform(const Vector3 &p,const Vector3 &n) {
    Matrix4 m;
    m.set(Vector3(0,0,0),
          normalize(Vector3(1,0,0).cross(n)),
          normalize(n.cross(normalize(Vector3(1,0,0).cross(n)))),
          (Vector3)normalize(n));
    return m.transformPoint(p);
}

Vector3 GLApplication::pointSpline(double tNormalized) {
    unsigned int p = floor(tNormalized * (double)(_inputPath.size()-1));
    double t = (tNormalized-(p/(double)(_inputPath.size()-1)))* (double)(_inputPath.size()-1);
    Vector3 P0 = _inputPath[p];
    Vector3 P1 = _inputPath[p+1];
    Vector3 T0 = tangentInputPath(p);
    Vector3 T1 = tangentInputPath(p+1);
    return t*t*t*((2*P0)-(2*P1)+T0+T1)+
            t*t*((-3)*P0+(3*P1)-(2*T0)-T1)+
            t*T0+P0;
}

Vector3 GLApplication::tangentSpline(double i) {
    if(i==0 )
        return pointSpline(i+0.01)-pointSpline(i);
    else if(i==1)
        return pointSpline(i)-pointSpline(i-0.01);
    else
        return pointSpline(i+0.01)-pointSpline(i-0.01);
}

Vector3 GLApplication::tangentInputPath(unsigned int i) {
    if(i==0 )
        return _inputPath[i+1]-_inputPath[i];
    else if(i==_inputPath.size()-1)
        return _inputPath[i]-_inputPath[i-1];
    else
        return _inputPath[i+1]-_inputPath[i-1];
}

/** ************************************************************************* **/

void GLApplication::normalCrossSection() {
    _normalCrossSection.clear();
}

void GLApplication::extrudeLineStrip() {
    if (_inputPath.size()<1 || _inputCrossSection.size()<1) return;
    _extrude.clear();

    for(unsigned int j=0; j < _inputPath.size(); j++){
        Vector3 dir= tangentInputPath(j);
        for(unsigned int i=0; i < _inputCrossSection.size(); i++){
            p3d::Vector3 coord;
            Vector3 p=p3d::Vector3(_inputCrossSection[i],0);
            coord = transform(p,dir);
            _extrude.push_back(coord+_inputPath[j]);
        }
    }
    _normalExtrude.clear(); // for lighting (last question)
}

void GLApplication::extrudeSpline() {
    if (_inputPath.size()<1 || _inputCrossSection.size()<1) return;
    _extrude.clear();

    for(double j=0.; j <= 1.; j+=0.01){
        Vector3 dir= tangentSpline(j);
        for(unsigned int i=0; i < _inputCrossSection.size(); i++){
            p3d::Vector3 coord;
            Vector3 p=p3d::Vector3(_inputCrossSection[i],0);
            coord = transform(p,dir);
            _extrude.push_back(coord+pointSpline(j));
        }
    }
    _normalExtrude.clear(); // for lighting (last question)
}


/** ************************************************************************* **/


double GLApplication::scale(double tNormalized) {
    return 1.0;
}
