#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;
in vec2 texCoord;

out vec2 fTexCoord;

uniform mat4 mvp;

void main(void) {
  fTexCoord=texCoord;
  gl_Position=mvp*vec4(position,1.0);
}
