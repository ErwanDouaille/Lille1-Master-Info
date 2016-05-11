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
U1=[0;0;0];
U2=[0;0;0];
U3=[0;-2;0];

U = [U1(1); U1(2) ;U2(1); U2(2); U3(1); U3(2)];


/// on calcule la deformation
[K,Cont,B] = KtriangleSimple(P1,P2,P3,100,0.3);

Epsilon = B * U;
Sigma = Cont * U;

/// on prend un point appartenant au triangle
x=4;
y=3;

U_noeuds = [U1 U2 U3];
/// On affiche les deux triangles
draw_mesh3([noeuds noeuds+U_noeuds], [t1 t1+3], [0 abs(Epsilon(1))  0.5]);

