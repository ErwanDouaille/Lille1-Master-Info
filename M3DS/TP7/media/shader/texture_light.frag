#version 130
/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 fNormal;
in vec3 fLight[4];
in vec3 fEye;
in vec2 fTexCoord;

out vec4 fragColor;

uniform sampler2D texture0;
uniform float lightIntensity[4];
uniform vec4 ambient;

void main(void) {
  vec3 light;
  float intensity=0;
  vec3 normal;
  if (gl_FrontFacing) normal=fNormal; else normal=-fNormal;
  normal=normalize(normal);
  for(int i=0;i<4;++i) {
    light=normalize(fLight[i]);
    intensity+=max(dot(normal,light),0.0)*lightIntensity[i];
  }
  vec4 color=texture(texture0,fTexCoord);
  fragColor=color*(ambient+vec4(intensity,intensity,intensity,1));
}
