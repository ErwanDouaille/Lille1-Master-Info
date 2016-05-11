getd();
/// définition de la géométrie
P1=[0;0;0];
P2=[10;0;0];
P3=[5;6.6;0];

/// définition de la topologie
t1 = [1;2;3];

/// tableaux 
noeuds=[P1 P2 P3];
elements=[t1];

/// On suppose qu'on connait le déplacement aux noeuds
U1=[5;1.5;0];
U2=[4.5;-0.3;0];
U3=[5.3;1.2;0];
U_noeuds = [U1 U2 U3];


/// on calcule les fonctions d'interpolation
/// ????

/// on prend un point appartenant au triangle
x=4;
y=3;

/// on interpole son déplacement:
[Phi1, Phi2, Phi3]=InterpolationTriangle(P1,P2,P3);
P = [1; x; y];
u = (Phi1*P)*U1 +(Phi2*P)*U2 + (Phi3*P)*U3; 

/// affichage d'un point en 3D
draw_point(x,y,0,0.5);

/// affichage du point déplacé (interpolé)
draw_point(x+u(1),y+u(2),0,0.5);



/// On affiche les deux triangles
draw_mesh3([noeuds noeuds+U_noeuds], [t1 t1+3], [0 0.1]);

