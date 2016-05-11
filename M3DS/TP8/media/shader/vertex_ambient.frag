#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

uniform vec4 ambient;
uniform float offset;

out vec4 fragColor;

void main(void) {
  gl_FragDepth=gl_FragCoord.z-offset*(1-gl_FragCoord.z);
  fragColor=ambient;
}
