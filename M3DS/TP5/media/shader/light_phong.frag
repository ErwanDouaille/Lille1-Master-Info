#version 130

/**
  <c> F.Aubert Univ-Lille1
  **/

struct Material {
  vec3 fDiffuse;
  vec3 bDiffuse;
  vec3 specular;
  int shininess;
};


vec3 specular(in vec3 N,in vec3 L,in vec3 V,in int s,in vec3 ks,in float is) {
  vec3 H=(L+V)/length(L+V);
  float intensity;
  intensity=pow(max(dot(H,N),0.0),float(s));
  return intensity*ks*is;
}

uniform Material material;
uniform vec4 ambient;
uniform float lightIntensity[4];

in vec3 fNormal,fView;
in vec3 fLight[4];
in vec3 frontDiffuse;
in vec3 backDiffuse;
out vec4 fragColor;

void main() {

  vec3 N,L,V;
  vec3 colorSpec;

  V=normalize(fView);

  if (gl_FrontFacing) N=fNormal;
  else N=-fNormal;
  N=normalize(N);

  colorSpec=vec3(0,0,0);
  for(int i=0;i<4;++i) {
    L=normalize(fLight[i]);
    colorSpec+=specular(N,L,V,material.shininess,material.specular,lightIntensity[i]);
  }

  vec3 colorDiffuse;
  if (gl_FrontFacing) colorDiffuse=frontDiffuse; else colorDiffuse=backDiffuse;
  fragColor=vec4(colorDiffuse+colorSpec,ambient.a);
}
