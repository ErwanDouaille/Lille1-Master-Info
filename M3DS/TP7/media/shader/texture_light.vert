#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;
in vec3 normal;
in vec2 texCoord;

out vec3 fNormal;
out vec3 fLight[4];
out vec2 fTexCoord;
out vec3 fEye;

uniform vec4 lightPosition[4];

uniform mat4 modelviewMatrix;
uniform mat4 projectionMatrix;
uniform mat3 normalMatrix;
uniform mat4 textureMatrix;
uniform mat4 mvp;


void main(void) {

  fNormal=normalMatrix*normal;
  vec4 vertexEye=modelviewMatrix*vec4(position,1.0);
  fEye=vertexEye.xyz/vertexEye.w;
  for(int i=0;i<4;++i) {
    fLight[i]=lightPosition[i].xyz-fEye;
  }

  vec4 texCoord4=textureMatrix*vec4(texCoord,0,1);
  fTexCoord=texCoord4.st/texCoord4.w;
  gl_Position=mvp*vec4(position,1.0);
}
