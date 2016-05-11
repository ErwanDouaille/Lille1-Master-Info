#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;
in vec2 texCoord;

out vec4 fTexCoord;

uniform mat4 mvp;
uniform mat4 textureMatrix;

void main(void) {
  fTexCoord=textureMatrix*vec4(texCoord.st,1,1);
  gl_Position=mvp*vec4(position,1.0);
}
