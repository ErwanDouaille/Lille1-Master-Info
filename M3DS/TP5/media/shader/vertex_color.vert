#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;
in vec3 color;

uniform mat4 mvp;

out vec3 fColor;

void main(void) {
  fColor=color;
  gl_Position = mvp*vec4(position,1.0);
}
