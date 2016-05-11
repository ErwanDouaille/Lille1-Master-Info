#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec4 fTexCoord;

out vec4 fragColor;

uniform sampler2D image0;
uniform vec4 materialAmbient;

void main(void) {
  vec4 color=texture(image0,fTexCoord.xy/fTexCoord.w);
  fragColor=color*materialAmbient.a;
}
