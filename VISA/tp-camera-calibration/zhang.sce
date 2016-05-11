// -----------------------------------------------------------------------
// Methode de Zhang pour la calibration d'une camera
// Copyright (C) 2010  Universite Lille 1
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

// Definition des fonctions utilitaires
clear;
exec ('glue.sci', -1);
exec ('erwan-douaille.sci', -1);
// Nombre d'images
ni = 4;

// Lire les coordonnees des points de la mire dans la scene
M = read('points.txt', -1, 2)';
np = size(M, 2);
M = [M; zeros(1, np); ones(1, np)];

sansZ = [1, 2, 4];
// Initialiser la matrice des contraintes
V = [];
// Matrices des homographies
H = zeros(3, 3, ni);
// Coordonnees de tous les points image
m = zeros(3, np, ni);

// Boucler pour toutes les images
for i = 1:ni                                           
  // Lire les points de l'image 2.2
  m(1:2,:,i) = read('points-'+string(i)+'.txt', -1, 2)';
  m(3,:,i) = ones(1, np);
  // Estimer l'homographie entre la mire et l' image 2.3
  H(:,:,i) = ZhangHomography(M(sansZ,:), m(:,:,i));
  // Ajouter deux lignes de contraintes dans V
  V = [V; ZhangConstraints(H(:,:,i))];
end

// Calculer le vecteur b
// Part A singular conslusion
b = SmallestRightSingular(V);

// Estimation de la matrice intrinseque
// Part B conclusion
A = IntrinsicMatrix(b);
iA = inv(A);

// Estimations des matrices extrinseques
// Part C conclusion
E = zeros(3, 4, ni);
for i = 1:ni
  E(:,:,i) = ExtrinsicMatrix(iA, H(:,:,i));
end

