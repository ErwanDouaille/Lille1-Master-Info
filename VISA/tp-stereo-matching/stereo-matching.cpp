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
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/calib3d/calib3d.hpp>
#include <getopt.h>
using namespace cv;
#include "glue.hpp"
#include "erwan-douaille.hpp"

/* --------------------------------------------------------------------------
Macros
-------------------------------------------------------------------------- */
#define SKIPDELIMS(x) \
while (*(x) && (*(x)==' ' || *(x)=='=' || *(x)==':')) (x)++;
#define ERROR(x) \
{ fprintf(stderr, (x)); return(-1); }
#define ERROR2(x,y) \
{ fprintf(stderr, (x), (y)); return(-1); }
#define MAX_CORNERS 32

/* --------------------------------------------------------------------------
Variables statiques
-------------------------------------------------------------------------- */
// Options de la ligne de commande
static const char *pcShortOptions = "m:vdsh";
static const struct option psLongOptions[] =
{
    {"maximal-distance", optional_argument, NULL, 'm'},
    {"verbose", no_argument, NULL, 'v'},
    {"display", no_argument, NULL, 'd'},
    {"save", no_argument, NULL, 's'},
    {"help", no_argument, NULL, 'h'},
    {NULL, 0, NULL, 0} // Fin de structure
};
static CvScalar sColor[4] = {
    {{   0,   0, 255,   0 }}, // Rouge
    {{   0, 255,   0,   0 }}, // Vert
    {{ 255,   0,   0,   0 }}, // Bleu
    {{   0, 255, 255,   0 }}  // Jaune
};

/* --------------------------------------------------------------------------
Programme principal
-------------------------------------------------------------------------- */
int main(int argc, char **argv) {

    /* ------------------------------------------------------------------
    Variables necessitant une initialisation
    ------------------------------------------------------------------ */
    bool bDisplay = false; // Affichage de l'image traitee
    bool bVerbose = true; // Affichage de messages d'information
    bool bSave = false; // Sauvegarde des images resultat
    double dMaxDistance = 2.0; // Distance maximale pour une association

    /* ------------------------------------------------------------------
    Autres variables
    ------------------------------------------------------------------ */
    int iNextOption; // Lecture d'option sur la ligne de commande
    Mat mLeftGray, mRightGray; // Structures image pour OpenCV
    Mat mLeftColor, mRightColor; // Structures image pour OpenCV
    Mat mLeftCorners; // Coins dans l'image gauche
    Mat mRightCorners; // Coins dans l'image droite
    Mat mLeftIntrinsic, mRightIntrinsic; // Matrices intrinseques
    Mat mLeftExtrinsic, mRightExtrinsic; // Matrices extrinseques
    Mat mFundamental; // Matrice fondamentale
    Mat mDistances; // Matrice des distances
    Mat mLeftEpipolarLines, mRightEpipolarLines; // Droites epipolaires
    Mat mRightHomologous, mLeftHomologous; // Matrices des associations
    vector<Vec3f> vLines; // Droites epipolaires

    // Lire les options et les arguments sur la ligne de commande
    do {
        // Option suivante
        iNextOption = getopt_long(argc, argv, pcShortOptions,
                                  psLongOptions, NULL);
        // Traiter l'option
        switch(iNextOption) {
        case 'm':
            SKIPDELIMS(optarg);
            dMaxDistance = atof(optarg);
            break;
        case 'v':
            bVerbose = true;
            break;
        case 'd':
            bDisplay = true;
            break;
        case 's':
            bSave = true;
            break;
        case 'h':
        case '?':
            gluePrintHelpMessage(*argv, stderr);
            return (-1);
            break;
        }
    } while(iNextOption != -1);
    // Noms de fichiers disponibles ?
    if(optind + 3 >= argc) {
        // Message d'aide et sortie
        gluePrintHelpMessage(*argv, stderr);
        return(-1);
    }

    // Chargement des fichiers
    mLeftColor = imread(argv[optind], CV_LOAD_IMAGE_COLOR);
    if (mLeftColor.empty())
        ERROR2("Erreur lors de l'ouverture du fichier image gauche '%s'\n",
               argv[optind]);
    mRightColor = imread(argv[optind + 1], CV_LOAD_IMAGE_COLOR);
    if (mRightColor.empty())
        ERROR2("Erreur lors de l'ouverture du fichier image droite '%s'\n",
               argv[optind + 1]);
    // Chargement des matrices intrinseques et extrinseques
    glueLoadMatrices(argv[optind + 2], mLeftIntrinsic, mLeftExtrinsic);
    glueLoadMatrices(argv[optind + 3], mRightIntrinsic, mRightExtrinsic);
    // Creation des images en niveaux de gris
    cvtColor(mLeftColor, mLeftGray, CV_RGB2GRAY);
    cvtColor(mRightColor, mRightGray, CV_RGB2GRAY);

    // Calcule la matrice fondamentale
    mFundamental = iviFundamentalMatrix(mLeftIntrinsic, mLeftExtrinsic,
                                        mRightIntrinsic, mRightExtrinsic);
    if (bVerbose) {
        // Affiche la matrice fondamentale
        printf("Matrice fondamentale\n");
        glueDisplayMatrix(mFundamental);
    }

    // Detection des coins sur l'image gauche
    mLeftCorners = iviDetectCorners(mLeftGray, MAX_CORNERS);
    if (bDisplay) {
        glueDrawCorners(mLeftColor, mLeftCorners, sColor[0]);
    }
    // Calcul des droites epipolaires de ces coins
    mRightEpipolarLines = mFundamental * mLeftCorners;
    if (bDisplay) {
        glueDrawLines(mRightColor, mRightEpipolarLines, sColor[1]);
    }

    // Detection des coins sur l'image droite
    mRightCorners = iviDetectCorners(mRightGray, MAX_CORNERS);
    if (bDisplay) {
        glueDrawCorners(mRightColor, mRightCorners, sColor[0]);
    }
    // Calcul des droites epipolaires de ces coins
    mLeftEpipolarLines = mFundamental.t() * mRightCorners;
    if (bDisplay) {
        glueDrawLines(mLeftColor, mLeftEpipolarLines, sColor[1]);
    }

    // Uniquement s'il y a au moins un point dans chaque image
    if (mLeftCorners.cols * mRightCorners.cols) {
        // Calcule la matrice des distances
        mDistances = iviDistancesMatrix(mLeftCorners, mRightCorners,
                                        mFundamental);
        if (bVerbose) {
            // Affiche la matrice des distances
            printf("Distances\n");
            glueDisplayMatrix(mDistances);
        }
        // Definit les associations de points
        iviMarkAssociations(mDistances, dMaxDistance, mRightHomologous,
                            mLeftHomologous);
    }

    // Affichage ?
    if(bDisplay) {
        // Creer les fenetres d'affichage des images
        namedWindow("Left", CV_WINDOW_AUTOSIZE);
        namedWindow("Right", CV_WINDOW_AUTOSIZE);
        // Et afficher
        imshow("Left", mLeftColor);
        imshow("Right", mRightColor);
    }

    // Si affichage, attendre l'appui sur une touche
    if (bDisplay)
        cvWaitKey(0);
    // Sauvegarde des resultats
    if (bSave) {
        imwrite("left-result.png", mLeftColor);
        imwrite("right-result.png", mRightColor);
    }
    return 0;
}
