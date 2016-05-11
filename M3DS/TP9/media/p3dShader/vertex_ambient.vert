#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;

uniform mat4 mvp;

void main(void) {
    gl_Position = mvp*vec4(position,1.0);
}
