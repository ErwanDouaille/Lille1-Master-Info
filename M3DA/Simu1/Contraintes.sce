getd();

clear all;
// MAILLAGE //
[ noeuds , elements ] = MSHLoader('rectangle1.msh');
numElements = size(elements,2);
numNoeuds = size(noeuds,2);     



// Matrice d'assemblage:
Kass = spzeros(2*numNoeuds,2*numNoeuds);
// materiau: Aluminium
E=69000; //MPa
Poisson=0.33;

Tableau_Cont = zeros(3,6,numElements);
// raideur de chaque element
for i=1:numElements,
  //on récupère les indices des trois noeuds de l'élément
  i1 = elements(1,i);
  i2 = elements(2,i);
  i3 = elements(3,i);
  //on récupère les coordonnées des trois noeuds
  P1 = noeuds(:,i1);
  P2 = noeuds(:,i2);
  P3 = noeuds(:,i3);
  //lignes et colonnes conscernées:
  // par ex = i1 = 3  // i2 = 5 // i3 = 8
  // indices = [5 6 9 10 15 16]
  indices = [2*i1-1 2*i1 2*i2-1 2*i2 2*i3-1 2*i3];
  [Kelement, Cont] = KtriangleSimple(P1,P2,P3,E,Poisson);
  Kass(indices,indices) = Kass(indices,indices) + Kelement;
  Tableau_Cont(:,:,i)=Cont;  
end

// on bloque les points de coordonnée x=5, les autres sont libres
numContraint = 0; 
numLibre = 0;
ddl_contraint=[];
ddl_libre=[];
for n=1:numNoeuds,
  // on récupère la coordonnée en x du noeud
  x = noeuds(1,n);  
  
  // blocage selon x
  if x==5, // A MODIFIER !
    numContraint = numContraint + 1;
    ddl_contraint([numContraint]) = [2*n-1] ;    
  else,
    numLibre = numLibre + 1; 
    ddl_libre([numLibre]) = [2*n-1] ;
  end
  
  // blocage selon y 
  if x==5, // A MODIFIER !
    numContraint = numContraint + 1;
    ddl_contraint([numContraint]) = [ 2*n] ;    
  else,
    numLibre = numLibre + 1; 
    ddl_libre([numLibre]) = [2*n] ;
  end
end


Force = zeros(2*numNoeuds,1);
// force ponctuelle :
// on recupere le noeud de coordonnée x=0 y=1
pos_force=[0;1];
for n=1:numNoeuds,
  x = noeuds(1,n);
  y = noeuds(2,n);
  if abs(x-pos_force(1)) < 0.000001 & abs(y-pos_force(2)) < 0.000001,
    Force([2*n-1 2*n]) = [0 ; -100];
  end
end

//// pression de surface
//numNoeudForce = 0;
//for n=1:numNoeuds,
//  x = noeuds(1,n);
//  y = noeuds(2,n);
//  if x > - 0.000001 & x < 1.0000001 & abs(y-1) < 0.000001,
//    Force([2*n-1 2*n]) = [0 ; -1];
//    numNoeudForce = numNoeudForce+1;
//  end
//end
//Charge = 100;
//Force = Force * Charge / numNoeudForce;

 
// deplacements aux noeuds
U = zeros(2*numNoeuds,1);
U(ddl_libre)= Kass(ddl_libre,ddl_libre)\Force(ddl_libre);
  
// on applique les déplacements aux noeuds:
noeuds_deplaces = noeuds;
for n=1:numNoeuds,
  noeuds_deplaces(1,n) = noeuds(1,n) + U(2*n-1); // deplacement du noeud selon x  
  noeuds_deplaces(2,n) = noeuds(2,n) + U(2*n);   // deplacement du noeud selon y
end
  
 VonMises=[]; 
for i=1:numElements,
  Cont = Tableau_Cont(:,:,i);
  //on récupère les indices des trois noeuds de l'élément
  i1 = elements(1,i);
  i2 = elements(2,i);
  i3 = elements(3,i);
  indices = [2*i1-1 2*i1 2*i2-1 2*i2 2*i3-1 2*i3]; 
  
  // calcul de la contrainte sur l'élément
  // Sigma = [Sigma_xx;
  //          Sigma_yy;
  //          Sigma_xy]
  Sigma = Cont * U(indices);
  Sigma12 = spec([Sigma(1) Sigma(3); Sigma(3) Sigma(2)]);
  VonMises(i)= sqrt(Sigma12(1)*Sigma12(1)+Sigma12(2)*Sigma12(2)-Sigma12(1)*Sigma12(2));
end  
  
//draw_mesh( noeuds_deplaces, elements);  
//draw_mesh2(noeuds, elements, dataNoeud);
draw_mesh3 (noeuds_deplaces, elements, [VonMises  ; 10]);
  
  
  
  

  




