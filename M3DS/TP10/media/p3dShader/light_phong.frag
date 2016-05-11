#version 130

/**
  <c> F.Aubert Univ-Lille1
  **/


uniform vec4 ambient;
uniform vec3 materialSpecular;
uniform float materialShininess;
uniform vec4 lightPosition[4];
uniform float lightIntensity[4];


in vec3 fNormal,fView;
in vec3 fLight[4];
in vec3 frontDiffuse;
in vec3 backDiffuse;
out vec4 fragColor;

void main() {

  vec3 N,L,V;
  vec3 colorDiffuse;
  vec3 colorSpec;

  V=normalize(fView);

  if (gl_FrontFacing) {
    N=fNormal;
    colorDiffuse=frontDiffuse;
  }
  else {
    N=-fNormal;
    colorDiffuse=backDiffuse;
  }
  N=normalize(N);

  vec3 Rv=reflect(-V,N);
  colorSpec=vec3(0,0,0);
  for(int i=0;i<4;++i) {
    L=normalize(fLight[i]);
    colorSpec+=pow(max(dot(Rv,L),0.0),materialShininess)*materialSpecular*lightIntensity[i];
  }

  //colorSpec=min(colorSpec,1);

  fragColor=vec4(ambient.xyz+colorDiffuse+colorSpec,ambient.a);
}
