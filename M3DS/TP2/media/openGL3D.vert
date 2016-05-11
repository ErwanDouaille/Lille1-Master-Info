#version 130

in vec3 position;
in vec4 normal;

out vec4 fColor;

uniform mat4 projection;
uniform mat4 transform;

uniform vec3 lightPosition;
uniform vec3 diffuseColor;

// note pour eclairage de la vache : N=-N; intensity=max(slot(L,N),0.0);
void main() {
  vec3 N = normal.xyz;
  vec4 clipPosition=vec4(position,1);
  vec4 Nclip = transform * vec4(-normal);
  N = Nclip.xyz;
  clipPosition=transform*clipPosition; 
  clipPosition=projection*clipPosition; // transformation par la matrice de projection
  N = normalize(N);
  vec3 L = clipPosition.xyz/clipPosition.w-lightPosition-position;
  L = normalize(L);
  gl_Position=clipPosition;
  float intensity = max(dot(L,N),0.0);
  fColor =  vec4(intensity * diffuseColor,1);
}
