#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;

out vec4 fTexCoord;

uniform mat4 mvp;
uniform mat4 textureMatrix;
uniform mat4 textureTransform;

void main(void) {
  vec4 texCoord=textureTransform*vec4(position,1.0);
  fTexCoord=textureMatrix*texCoord;
  gl_Position=mvp*vec4(position,1.0);
}
