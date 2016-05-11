// -----------------------------------------------------------------------
// Etude de la projection perspective, matrices extrinseque et intrinseque
// d'une camera. Fonction d'affiche d'un objet apres projection
// Module TI, Traitement d'Images
// Copyleft (C) 2012-2014  Universite Lille 1
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
// -----------------------------------------------------------------------

// -----------------------------------------------------------------------
// Fonction d'affichage d'un objet 2D dans une figure scilab
// nfigure = numero de la figure scilab
// taille = [nblignes, nbpixels] de l'image simulee
// points = matrice 2*N des coordonnees des points
// segments = matrice 2*M des indices des points
// -----------------------------------------------------------------------
function tiAfficheObjet2D (nfigure, taille, points, segments)
    // Creer une nouvelle figure ou activer une figure existante
    hf = scf (nfigure);
    hf.figure_name = "Projection numero %d";
    // Echelle identique sur les deux axes
    ha = gca ();
    ha.isoview = "on";
    // Axe Y vers le bas
    ha.axes_reverse = ["off", "on", "off"];
    ha.x_location = "top";
    ha.box = "on";
    // Tracer les points
    plot2d (points(1,:), points(2,:), style = -4, ...
    rect=[0, 0, taille(2), taille(1)]);
    // Tracer les segments
    sx = [points(1, segments(1,:)); points(1, segments(2,:))];
    sy = [points(2, segments(1,:)); points(2, segments(2,:))];
    xsegs (sx, sy);
endfunction

// -----------------------------------------------------------------------
// Definition d'un cube de cote fixe centre sur l'origine du repere 3D.
// Les points sont definis par des coordonnees homogenes. En plus de la
// matrice des coordonnees des points la fonction retourne une liste des
// segments reliant les points pour former la grille. La liste est une
// matrice de 2 lignes de 12 colonnes, chaque colonne contenant les
// indices de deux points du cube qui sont relies par un segment.
// -----------------------------------------------------------------------
function [points, segments] = tiCube (cote)
    // Definition des coordonnees des points, cube de cote 2
    points = [ ...
    -1  1  1 -1 -1  1  1 -1; ...
    -1 -1  1  1 -1 -1  1  1; ...
    -1 -1 -1 -1  1  1  1  1; ...
    1  1  1  1  1  1  1  1];
    // Changement des cotes -> coefficient cote/2 sur les 3 coordonnees
    points = points .* ([cote/2; cote/2; cote/2; 1] * ones (1, 8));
    // Definition des segments reliant les points
    segments = [1:4, 1:4, 5:8; 2:4, 1, 5:8, 6:8, 5];
endfunction

// -----------------------------------------------------------------------
// Definition d'une grille de points 3D situes dans le plan z=0.
// Les points sont definis par des coordonnees homogenes. La grille est
// constituee de nx x ny carres de cote fixe. La grille est centree sur
// l'origine du repere. En plus de la matrice des coordonnees des points
// la fonction retourne une liste des segments reliant les points pour
// former la grille. La liste est une matrice de 2 lignes de N colonnes,
// chaque colonne contenant les indices de deux points de la grille qui
// sont relies par un segment.
// -----------------------------------------------------------------------
function [points, segments] = tiGrille (nx, ny, cote)
    // Definition des coordonnees X des points de la grille
    x = cote/2 * matrix (((0:nx) - nx/2)' * ones (1, ny+1), 1, -1);
    // Definition des coordonnees Y des points de la grille
    y = cote/2 * matrix (ones (1, nx+1)' * ((0:ny) - ny/2), 1, -1);
    // Definition des coordonnees Z et homogene des points
    zw = [0; 1] * ones (1, (nx+1) * (ny+1));
    // Matrice des points
    points = [x; y; zw];
    // Definition des segments reliant les points selon X
    debutx = find (pmodulo (1:(nx+1)*(ny+1), nx+1) ~= 0);
    finx = debutx + 1;
    // Definition des segments reliant les points selon Y
    debuty = 1:(nx+1)*ny;
    finy = debuty + nx+1;
    // Ensemble des segments
    segments = [debutx, debuty; finx, finy];
endfunction

// Matrice de rotation autour de l'axe x , angles en degrés
function matrice = RotationX(theta)
    c_t = cos(theta);
    s_t = sin(theta);
    
    matrice = [1,0,0,0;
               0,c_t,-s_t,0;
               0,s_t,c_t,0;
               0,0,0,1];
endfunction

// Matrice de rotation autour de l'axe y , angles en degrés
function matrice = RotationY(theta)
    c_t = cos(theta);
    s_t = sin(theta);
    
    matrice = [c_t,0,s_t,0;
               0,1,0,0;
               -s_t,0,c_t,0;
               0,0,0,1];
endfunction

// Matrice de rotation autour de l'axe z , angles en degrés
function matrice = RotationZ(theta)
    c_t = cos(theta);
    s_t = sin(theta);
    
    matrice = [c_t,-s_t,0,0;
               s_t,c_t,0,0;
               0,0,1,0;
               0,0,0,1];
endfunction

// Matrice de translation selon le vecteur v(x,y,z)
function matrice = Translation(x,y,z)
    matrice = [1,0,0,x;
               0,1,0,y;
               0,0,1,z;
               0,0,0,1];
endfunction
 
// Matrice intrinseque 
// f distance focale

function matrice = Intrinseque (f,Sc, Sl, Oc, Ol)
    m1 = [f/Sc, 0, Oc;
          0, f/Sl, Ol;
          0, 0, 1];
    matrice = m1;
endfunction

// f = distance focal
// matExtr = matrice extrinsec
function m = projection(f, matExtr)
proj = [f, 0, 0, 0;
            0, f, 0, 0;
            0, 0, 1, 0]
    m = proj * matExtr
endfunction
