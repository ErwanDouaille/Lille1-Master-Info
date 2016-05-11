#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec4 fTexCoord;

out vec4 fragColor;

uniform sampler2D texture0;
uniform vec4 ambient;

void main(void) {
  vec4 color=texture(texture0,fTexCoord.st/fTexCoord.q);
  fragColor=color*ambient;
}
