#version 130

uniform sampler2D depthTexture;

in vec4 fTexCoord;

out vec4 fragColor;

void main() {


  vec4 texCoord=fTexCoord;
  texCoord.xy/=texCoord.w;


  texCoord.xyz = (texCoord.xyz +1) /2;
  if( fTexCoord.z >=0)
    fragColor=texture(depthTexture,texCoord.st);

  else {
      fragColor = vec4(0,0,0,0);
  }
}
