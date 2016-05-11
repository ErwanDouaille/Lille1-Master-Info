#version 130

/**
  <c> F.Aubert Univ-Lille1
  **/

in vec3 position;
in vec3 normal;

uniform vec3 materialFrontDiffuse;
uniform vec3 materialBackDiffuse;
uniform vec4 lightPosition[4];
uniform float lightIntensity[4];


out vec3 frontDiffuse,backDiffuse;
out vec3 fNormal,fView;
out vec3 fLight[4];


uniform mat4 modelviewMatrix;
uniform mat3 normalMatrix;
uniform mat4 mvp;



void main() {

  fNormal=normalMatrix*normal;
  vec4 positionEye=modelviewMatrix*vec4(position,1.0);
  fView=-positionEye.xyz/positionEye.w;
  int i;
  for(i=0;i<4;++i) {
    fLight[i]=lightPosition[i].xyz+fView;
    fLight[i]=normalize(fLight[i]);
  }
  fNormal=normalize(fNormal);

  frontDiffuse=vec3(0,0,0);
  backDiffuse=vec3(0,0,0);
  for(i=0;i<4;++i) {
    float NdotL=dot(fNormal,fLight[i]);
    //if (dot(fNormal,fView)<0) NdotL=-NdotL;
    frontDiffuse+=max(NdotL,0)*materialFrontDiffuse*lightIntensity[i];
    backDiffuse+=max(-NdotL,0)*materialBackDiffuse*lightIntensity[i];
  }

  frontDiffuse=min(frontDiffuse,1);
  backDiffuse=min(backDiffuse,1);

  gl_Position=mvp*vec4(position,1.0);
}
