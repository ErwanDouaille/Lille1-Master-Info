#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

uniform float offset;

in vec3 fColor;
out vec4 fragColor;

void main(void) {
//  gl_FragDepth=gl_FragCoord.z+offset;
  fragColor=vec4(fColor,1);
}
