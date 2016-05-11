#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;

in vec4 texCoord;
out vec4 fTexCoord;

uniform mat4 mvp;
uniform mat4 texTransform;

void main(void) {


  fTexCoord=texTransform*vec4(position,1);
  gl_Position=mvp*vec4(position,1);
}
