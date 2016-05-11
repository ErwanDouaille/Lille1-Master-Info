#ifndef SPHERE_H
#define SPHERE_H

#include "Shader.h"
#include "Color.h"

class Sphere
{
    static Shader* _shaderSphere;
    static int _uni_sphereRadius;
    static int _uni_spherePos;
    static int _atr_vertexID;
    static GLuint _program;

public:
    Sphere();
    virtual ~Sphere();
    static void drawImposterSphere(prog3d::Vector3 position, double radius, prog3d::Color color);
    static void initSphereShader(string vert, string frag);
    static void removeSphereShader();

};

#endif // SPHERE_H

