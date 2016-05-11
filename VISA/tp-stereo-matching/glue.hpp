/* --------------------------------------------------------------------------
Mise en correspondance de points d'interet detectes dans deux images
Copyright (C) 2010, 2011  Universite Lille 1

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
-------------------------------------------------------------------------- */

/* --------------------------------------------------------------------------
Prototypes des fonctions
-------------------------------------------------------------------------- */

// -----------------------------------------------------------------------
/// \brief Affiche le message d'aide a l'utilisation.
///
/// @param sName: nom du fichier executable
/// @param pfStream: stream dans lequel il faut ecrire le texte
/// @return rien
// -----------------------------------------------------------------------
void gluePrintHelpMessage(const string& sName,
                          FILE *pfStream);

// -----------------------------------------------------------------------
/// \brief Lit les matrices intrinseque et extrinseque depuis un fichier.
///
/// @param sName: nom du fichier contenant les matrices
/// @param mIntrinsic: pointeur vers la matrice intrinseque
/// @param mExtrinsic: pointeur vers la matrice extrinseque
// -----------------------------------------------------------------------
void glueLoadMatrices(const string& sName,
                      Mat& mIntrinsic,
                      Mat& mExtrinsic);

// -----------------------------------------------------------------------
/// \brief Dessine une liste de points sur une image (petits cercles).
///
/// @param mImage: pointeur vers la structure image openCV
/// @param mPoints: liste des points
/// @param sColor: couleur des cercles traces
/// @return rien
// -----------------------------------------------------------------------
void glueDrawCorners(Mat& mImage,
                     const Mat& mPoints,
                     const CvScalar sColor);

// -----------------------------------------------------------------------
/// \brief Affiche les elements d'une matrice ligne par ligne
///
/// @param m: matrice a afficher
/// @return rien
// -----------------------------------------------------------------------
void glueDisplayMatrix(const Mat& m);

// -----------------------------------------------------------------------
/// \brief Dessine une liste de droites dans une image.
///
/// @param mImage: image dans laquelle la ligne est tracee
/// @param mLines: vecteurs caracteristiques des droites
/// @param sColor: couleur de la ligne tracee
/// @return rien
// -----------------------------------------------------------------------
void glueDrawLines(Mat& mImage,
                   const Mat& mLines,
                   const CvScalar sColor);
