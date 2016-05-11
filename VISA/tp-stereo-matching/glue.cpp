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
Inclure les fichiers d'entete
-------------------------------------------------------------------------- */
#include <stdio.h>
#include <opencv2/core/core.hpp>
using namespace cv;
#include "glue.hpp"

/* --------------------------------------------------------------------------
Fonctions
-------------------------------------------------------------------------- */

// -----------------------------------------------------------------------
/// \brief Affiche le message d'aide a l'utilisation.
///
/// @param sName: nom du fichier executable
/// @param pfStream: stream dans lequel il faut ecrire le texte
/// @return rien
// -----------------------------------------------------------------------
void gluePrintHelpMessage(const string& sName,
                          FILE *pfStream) {
    // Afficher un message d'aide
    fprintf(pfStream,
            "Utilisation: %s [OPTIONS] lim rim lparm.xml rparm.xml\n" \
            "Determine les points d'interet dans deux images " \
            "et les met en correspondance. " \
            "lim et rim sont deux images correspondant a deux points " \
            "de vue d'un meme objet. lparm.xml et rparm.xml definissent " \
            "les matrices des parametres implicites et explicites des " \
            "deux cameras\n\n" \
            "Options courtes, options longues, significations et" \
            " valeurs par defaut:\n" \
            "  -v, --verbose  mode bavard (affichage de messages" \
            " d'information)\n" \
            "  -d, --display  affichage d'une fenetre openCV\n" \
            "  -s, --save     enregistre les images resultat\n" \
            "  -h, --help     affichage de ce message d'aide\n",
            sName.c_str());
}

// -----------------------------------------------------------------------
/// \brief Lit les matrices intrinseque et extrinseque depuis un fichier.
///
/// @param sName: nom du fichier contenant les matrices
/// @param mIntrinsic: pointeur vers la matrice intrinseque
/// @param mExtrinsic: pointeur vers la matrice extrinseque
// -----------------------------------------------------------------------
void glueLoadMatrices(const string& sName,
                      Mat& mIntrinsic,
                      Mat& mExtrinsic) {
    // Ouverture du fichier en lecture
    FileStorage fs(sName, FileStorage::READ);
    // Lecture des matrices
    fs["Intrinsic"] >> mIntrinsic;
    fs["Extrinsic"] >> mExtrinsic;
    // Changement eventuel de format
    if (mIntrinsic.type() != CV_64F)
        mIntrinsic = Mat_<double>(mIntrinsic);
    if (mExtrinsic.type() != CV_64F)
        mExtrinsic = Mat_<double>(mExtrinsic);
}

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
                     const CvScalar sColor) {
Point2d p;
double w;
    // Si ensemble de vecteurs colonne, coordonnees standard
    if (mPoints.rows == 2) {
        for (int i = 0; i < mPoints.cols; i++) {
            p.x = mPoints.at<double>(0,i);
            p.y = mPoints.at<double>(1,i);
            circle(mImage, p, 2, sColor);
        }
    // Si ensemble de vecteurs colonne, coordonnees homogenes
    } else if (mPoints.rows == 3) {
        for (int i = 0; i < mPoints.cols; i++)
            // Coordonnee w
            if ((w = mPoints.at<double>(2,i)) != 0.) {
                p.x = mPoints.at<double>(0,i) / w;
                p.y = mPoints.at<double>(1,i) / w;
                circle(mImage, p, 2, sColor);
            }
    // Si ensemble de vecteurs ligne, coordonnees standard
    } else if (mPoints.cols == 2) {
        for (int i = 0; i < mPoints.rows; i++) {
            p.x = mPoints.at<double>(i,0);
            p.y = mPoints.at<double>(i,1);
            circle(mImage, p, 2, sColor);
        }
    // Si ensemble de vecteurs ligne, coordonnees homogenes
    } else if (mPoints.cols == 3) {
        for (int i = 0; i < mPoints.rows; i++)
            // Coordonnee w
            if ((w = mPoints.at<double>(i,2)) != 0.) {
                p.x = mPoints.at<double>(i,0) / w;
                p.y = mPoints.at<double>(i,1) / w;
                circle(mImage, p, 2, sColor);
            }
    }
}

// -----------------------------------------------------------------------
/// \brief Affiche les elements d'une matrice ligne par ligne
///
/// @param m: matrice a afficher
/// @return rien
// -----------------------------------------------------------------------
void glueDisplayMatrix(const Mat& m) {
    for (int i = 0; i < m.rows; i++) {
        for (int j = 0; j < m.cols; j++) {
            if (j) printf(", ");
            printf("%12.5le", m.at<double>(i,j));
        }
        printf("\n");
    }
}

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
                   const CvScalar sColor) {
Point2f p[4]; // Points d'intersection valides
Mat mBorderLines[4]; // Quatre bords de l'image
Mat mBorderPoint; // Point d'intersection avec les bords
int iOk; // Nombre de points d'intersection valides
double dMaxX = mImage.cols - 1.; // Limite en X
double dMaxY = mImage.rows - 1.; // Limite en Y

    // Quatre droites limitant l'image
    mBorderLines[0] = (Mat_<double>(3,1) << 0., 1., 0.);
    mBorderLines[1] = (Mat_<double>(3,1) << 0., 1., -dMaxY);
    mBorderLines[2] = (Mat_<double>(3,1) << 1., 0., 0.);
    mBorderLines[3] = (Mat_<double>(3,1) << 1., 0., -dMaxX);
    // Boucler sur toutes les droites
    for (int i = 0; i < mLines.cols; i++) {
        // Nombre de points valides
        iOk = 0;
        // Traitement des quatre points d'intersection
        for (int j = 0; j < 4; j++) {
            // Point d'intersection
            mBorderPoint = mBorderLines[j].cross(mLines.col(i));
            // Si pas a l'infini
            if (fabs(mBorderPoint.at<double>(2,0)) > DBL_EPSILON) {
                // Passage en coordonnees standard
                p[iOk].x = mBorderPoint.at<double>(0,0) /
                    mBorderPoint.at<double>(2,0);
                p[iOk].y = mBorderPoint.at<double>(1,0) /
                    mBorderPoint.at<double>(2,0);
                // Dans l'image ?
                if (p[iOk].x >= 0. && p[iOk].x <= dMaxX &&
                    p[iOk].y >= 0. && p[iOk].y <= dMaxY)
                    // Conserver le point
                    iOk++;
                }
        }
        // Tracer la droite
        if (iOk == 2)
            line(mImage, p[0], p[1], sColor);
    }
}
