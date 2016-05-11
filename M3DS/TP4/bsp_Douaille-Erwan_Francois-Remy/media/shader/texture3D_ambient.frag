#version 130

/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 fTexCoord;

out vec4 fragColor;

uniform sampler3D texture0;
uniform vec4 ambient;

void main(void) {
  vec4 color=texture(texture0,fTexCoord);
  fragColor=color*ambient;
}
