function [Phi1, Phi2, Phi3]=InterpolationTriangle(P1,P2,P3)

P(:,1)=P1;
P(:,2)=P2;
P(:,3)=P3;

// positions en X et Y des noeuds 1, 2 et 3 du triangle
x1 = P1(1);
y1 = P1(2);
x2 = P2(1);
y2 = P2(2);
x3 = P3(1);
y3 = P3(2);

// matrice pour créer les fonctions d'interpolation

B = [1, 1, 1; x1, x2, x3; y1, y2, y3];

A = inv(B);

// matrice des fonctions d'interpolation linéaire

Phi1 = A(1,:);
Phi2 = A(2,:);
Phi3 = A(3,:); 

endfunction
