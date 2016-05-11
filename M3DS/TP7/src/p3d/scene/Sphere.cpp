#include "Sphere.h"
#include <QGLWidget>

using namespace prog3d;
using namespace std;


/** Class attributes */

Shader* Sphere::_shaderSphere;
int Sphere::_uni_sphereRadius;
int Sphere::_uni_spherePos;
int Sphere::_atr_vertexID;
GLuint Sphere::_program;


/** ********************** */

Sphere::Sphere()
{
}

Sphere::~Sphere(){
}

void Sphere::initSphereShader(string vert, string frag)
{
    _shaderSphere = new Shader(vert, frag);
    _shaderSphere->init();

    _program = _shaderSphere->getProgramID();

    _uni_sphereRadius = glGetUniformLocation(_program,"sphereRadius");
    _uni_spherePos = glGetUniformLocation(_program,"spherePos");
    _atr_vertexID = glGetAttribLocation(_program,"vertexID");
}

void Sphere::removeSphereShader()
{
    delete _shaderSphere;
    _uni_sphereRadius = -1;
    _uni_spherePos = -1;
    _atr_vertexID = -1;
}


void Sphere::drawImposterSphere(Vector3 position, double radius, prog3d::Color color)
{
    glDisable(GL_COLOR_MATERIAL);

    // Set color
    float matColor[4];
    matColor[0] = (float)color.r();
    matColor[1] = (float)color.g();
    matColor[2] = (float)color.b();
    matColor[3] = (float)color.a();
    glMaterialfv(GL_FRONT, GL_DIFFUSE, matColor);

    // Draw Imposter Shader Sphere
    glUseProgram(_program);

    glUniform1f(_uni_sphereRadius,radius);
    glUniform3f(_uni_spherePos,position.x(),position.y(),position.z());

    glBegin(GL_TRIANGLE_STRIP);
        glVertexAttrib1f(_atr_vertexID,0.0);
        glVertexAttrib1f(_atr_vertexID,1.0);
        glVertexAttrib1f(_atr_vertexID,2.0);
        glVertexAttrib1f(_atr_vertexID,3.0);
    glEnd();

    glUseProgram(0);

    glEnable(GL_COLOR_MATERIAL);

}

