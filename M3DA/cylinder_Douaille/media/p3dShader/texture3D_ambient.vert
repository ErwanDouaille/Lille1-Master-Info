#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;

out vec3 fTexCoord;

uniform mat4 mvp;

void main(void) {
  fTexCoord=(position+1.0)/2.0;
  gl_Position=mvp*vec4(position,1.0);
}
