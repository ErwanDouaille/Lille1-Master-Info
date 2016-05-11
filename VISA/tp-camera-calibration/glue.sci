// -----------------------------------------------------------------------
/// \brief Fonctions utilitaires pour l'implantation de la methode
/// de Zhang.
///
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

// -----------------------------------------------------------------------
/// \brief Calcule le produit vectoriel de deux vecteurs.
///
/// \param x: premier vecteur (dimension 3)
/// \param y: deuxieme vecteur (dimension 3)
/// \return produit vectoriel des deux vecteurs
// -----------------------------------------------------------------------
function z = CrossProduct(x, y)
  z(1) = x(2) * y(3) - x(3) * y(2);
  z(2) = x(3) * y(1) - x(1) * y(3);
  z(3) = x(1) * y(2) - x(2) * y(1);
endfunction

// -----------------------------------------------------------------------
/// \brief Projection d'une liste de points de la scene 3D vers
/// l'image 2D.
///
/// \param P: matrice 3*4 définissant la projection.
/// \param M: matrice 4*n des coordonnees homogenes des points initiaux.
/// \return matrice 3*n des coordonnees des points transformes.
// -----------------------------------------------------------------------
function m = Projection(P, M)
  // Transformation
  m = P * M;
  // Normalisation
  n = size(M, 2);
  m = [m(1,:) ./ m(3,:); m(2,:) ./ m(3,:); ones(1, n)];
endfunction

// -----------------------------------------------------------------------
/// \brief Transformation d'une liste de points par une homographie 2D.
///
/// \param H: matrice 3*3 définissant l'homographie.
/// \param M: matrice 3*n des coordonnees homogenes des points initiaux.
/// \return matrice 3*n des coordonnees des points transformes.
// -----------------------------------------------------------------------
function m = Homography2D(H, M)
  // Transformation
  m = H * M;
  // Normalisation
  n = size(M, 2);
  m = [m(1,:) ./ m(3,:); m(2,:) ./ m(3,:); ones(1, n)];
endfunction

// -----------------------------------------------------------------------
/// \brief Vecteur singulier droit correspondant a la plus petite valeur
/// singuliere d'une matrice.
///
/// \param Mat: matrice.
/// \return vecteur singulier associe a la plus petite valeur singuliere.
// -----------------------------------------------------------------------
function v = SmallestRightSingular(Mat)
  // Diagonalisation
  [v, a] = spec(Mat' * Mat);
  // Recherche de la plus petite valeur singuliere
  d = diag(real(a));
  pos = find(d == min(d));
  // Retour du vecteur colonne correspondant
  v = v(:, pos);
endfunction

// -----------------------------------------------------------------------
/// \brief Estimation d'une homographie pour la methode de Zhang.
///
/// \param M: matrice 3*n des coordonnees homogenes des points scene.
/// \param m: matrice 3*n des coordonnees homogenes des points image.
/// \return matrice 3*3 de l'homographie.
// -----------------------------------------------------------------------
function H = ZhangHomography(M, m)
  // Normalisation des points de la scene
  n = size(M, 2);
  tM = mean(M, 'c');
  sM = stdev(M, 'c');
  nM = [1/sM(1), 0, -tM(1)/sM(1); 0, 1/sM(2), -tM(2)/sM(2); 0, 0, 1];
  M = nM * M;
  // Normalisation des points de l'image
  tm = mean(m, 'c');
  sm = stdev(m, 'c');
  nm = [1/sm(1), 0, -tm(1)/sm(1); 0, 1/sm(2), -tm(2)/sm(2); 0, 0, 1];
  m = nm * m;
  // Matrice L
  u = m(1,:);
  v = m(2,:);
  L = [M', zeros(n,3), -u'.*M(1,:)', -u'.*M(2,:)', -u'; ...
    zeros(n,3), M', -v'.*M(1,:)', -v'.*M(2,:)', -v'];
  // Calcul de l'homographie
  H = inv(nm) * matrix(SmallestRightSingular(L), 3, 3)' * nM;
  H = H / H(3, 3);
endfunction

