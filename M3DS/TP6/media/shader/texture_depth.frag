#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec2 fTexCoord;

out vec4 fragColor;

uniform sampler2D texture0;
uniform vec4 ambient;

void main(void) {
  vec4 depth=texture(texture0,fTexCoord);
  vec4 color=1.0-clamp(50.0*(1.0-depth),0,1);
  fragColor=color*ambient;
}
