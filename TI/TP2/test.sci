// Effacer la memoire de travail de Scilab
clear;
//// Chargement des fonctions externes
exec ('tiProjection.sci');
//// Definition d'un cube de cote unite, sommets et aretes
[pCube, sCube] = tiCube (1);
//[pCube, sCube] = tiGrille (2,2,1.3);



//E1=Translation(0,0,5);

//// E2
//pCube = RotationZ(45) * pCube;
//pCube = RotationY(55) * pCube;
//
////pCube = RotationY((%pi/2)-acos(sqrt(2)/sqrt(3))) * pCube;
//pCube= Translation(0,0,5) * pCube;
//pCube = RotationZ(-150) * pCube;
//pCube = Intrinseque (5,6.6/600,8.8/800, 400, 300) * pCube;

//E = RotationY(-%pi/4);
//E= RotationX(acos(sqrt(2)/sqrt(3))) * E;
E= Translation(0,0,5);
//
M = Intrinseque (20,6.6/600,8.8/800, 400, 300) * ...
[1 0 0 0; 0 1 0 0; 0 0 1 0] * ...
 E;
//
////// Matrice de projection 3D -> 2D
////M = [-360 0 80 400; 0 -360 60 300; 0 0 0.2 1];
////// Projection des sommets du cube
////p = M * pCube
p = M * pCube;
tiAfficheObjet2D (1, [600, 800], p, sCube);
////// Passage en coordonnees cartesiennes
p = [p(1,:) ./ p(3,:); p(2,:) ./ p(3,:)];
////// Affichage dans la figure 1

