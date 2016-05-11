function [Kelement, Cont, B]=KtriangleSimple(P1,P2,P3,E,Poisson)

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
Matrice = [ 1  1  1;
            x1 x2 x3;
            y1 y2 y3];

// matrice des fonctions d'interpolation linéaire
Interpol = inv(Matrice);

b1=Interpol(1,2);
c1=Interpol(1,3);
b2=Interpol(2,2);
c2=Interpol(2,3);
b3=Interpol(3,2);
c3=Interpol(3,3);

// matrice donnant la deformation en fonction des déplacements:
B = [b1 0 b2 0 b3 0; 0 c1 0 c2 0 c3; c1 b1 c2 b2 c3 b3 ];

//// loi de hooke:
C =  E/(1-Poisson*Poisson)*[1 Poisson 0; Poisson 1 0; 0 0 (1-Poisson/2)];
//
//// Aire du triangle:
Aire= det(Matrice)/2;


//// Matrice de raideur de l'élément:
Kelement = Aire * (B'*C*B);

//// Matrice de contrainte de l'élément:
Cont = C*B;

endfunction










