/* --------------------------------------------------------------------------
Stereovision dense par calcul de correlation
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
#include <getopt.h>
using namespace cv;
#include "glue.hpp"
#include "prenom-nom.hpp"

/* --------------------------------------------------------------------------
Macros
-------------------------------------------------------------------------- */
#define SKIPDELIMS(x) \
while (*(x) && (*(x)==' ' || *(x)=='=' || *(x)==':')) (x)++;
#define ERROR(x) \
{ fprintf(stderr, (x)); return(-1); }
#define ERROR2(x,y) \
{ fprintf(stderr, (x), (y)); return(-1); }

/* --------------------------------------------------------------------------
Variables statiques
-------------------------------------------------------------------------- */
// Options de la ligne de commande
static const char *pcShortOptions = "wm:vdsh";
static const struct option psLongOptions[] =
{
    {"window-half-size", optional_argument, NULL, 'w'},
    {"maximum-disparity", optional_argument, NULL, 'm'},
    {"verbose", no_argument, NULL, 'v'},
    {"display", no_argument, NULL, 'd'},
    {"save", no_argument, NULL, 's'},
    {"help", no_argument, NULL, 'h'},
    {NULL, 0, NULL, 0} // Fin de structure
};

/* --------------------------------------------------------------------------
Programme principal
-------------------------------------------------------------------------- */
int main(int argc, char **argv) {

    /* ------------------------------------------------------------------
    Variables necessitant une initialisation
    ------------------------------------------------------------------ */
    bool bDisplay = false; // Affichage de l'image traitee
    bool bVerbose = false; // Affichage de messages d'information
    bool bSave = false; // Enregistrement des images resultat
    int iMaxDisparity = 32; // Disparite maximale
    int iWindowHalfSize = 2; // Demi-taille de la fenetre de correlation

    /* ------------------------------------------------------------------
    Autres variables
    ------------------------------------------------------------------ */
    int iNextOption; // Lecture d'option sur la ligne de commande
    Mat mLeftGray, mRightGray; // Structures image pour OpenCV
    Mat mLeftDisparity, mRightDisparity; // Disparites G/D
    Mat mDisparity; // Carte finale des disparites
    Mat mValidityMask; // Masque de validite
    double dMin, dMax; // Valeurs maxi et mini d'une image

    // Lire les options et les arguments sur la ligne de commande
    do {
        // Option suivante
        iNextOption = getopt_long(argc, argv, pcShortOptions,
                                  psLongOptions, NULL);
        // Traiter l'option
        switch(iNextOption) {
        case 'w':
            SKIPDELIMS(optarg);
            iWindowHalfSize = atoi(optarg);
            break;
        case 'm':
            SKIPDELIMS(optarg);
            iMaxDisparity = atoi(optarg);
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
    if(optind + 1 >= argc) {
        // Message d'aide et sortie
        gluePrintHelpMessage(*argv, stderr);
        return(-1);
    }

    // Chargement des fichiers
    mLeftGray = imread(argv[optind], CV_LOAD_IMAGE_GRAYSCALE);
    if (mLeftGray.empty())
        ERROR2("Erreur lors de l'ouverture du fichier image gauche '%s'\n",
               argv[optind]);
    mRightGray = imread(argv[optind + 1], CV_LOAD_IMAGE_GRAYSCALE);
    if (mRightGray.empty())
        ERROR2("Erreur lors de l'ouverture du fichier image droite '%s'\n",
               argv[optind + 1]);
    // Creation des images complementaires
    mDisparity = Mat(mLeftGray.size(), CV_8U);
    mValidityMask = Mat(mLeftGray.size(), CV_8U);

    // Calcul de la carte de disparite avec l'image gauche comme reference
    mLeftDisparity = iviLeftDisparityMap(mLeftGray, mRightGray,
                                         iMaxDisparity, iWindowHalfSize);
    // Calcul de la carte de disparite avec l'image droite comme reference
    mRightDisparity = iviRightDisparityMap(mLeftGray, mRightGray,
                                           iMaxDisparity, iWindowHalfSize);
    // Verification gauche droite
    mDisparity = iviLeftRightConsistency(mLeftDisparity, mRightDisparity,
                                          mValidityMask);

    // Affichage ?
    if(bDisplay) {
        // Creer les fenetres d'affichage des images
        namedWindow("Left", CV_WINDOW_AUTOSIZE);
        namedWindow("Right", CV_WINDOW_AUTOSIZE);
        namedWindow("Left disparity", CV_WINDOW_AUTOSIZE);
        namedWindow("Right disparity", CV_WINDOW_AUTOSIZE);
        namedWindow("Disparity", CV_WINDOW_AUTOSIZE);
        namedWindow("Validity mask", CV_WINDOW_AUTOSIZE);
        // Ajuster la dynamique des images de disparite
        minMaxLoc(mLeftDisparity, &dMin, &dMax);
        if (bVerbose) {
            printf("Disparite maximale (ref gauche) = %lf\n", dMax);
        }
        normalize(mLeftDisparity, mLeftDisparity, dMin, dMax, CV_MINMAX);
        minMaxLoc(mRightDisparity, &dMin, &dMax);
        if (bVerbose) {
            printf("Disparite maximale (ref droite) = %lf\n", dMax);
        }
        normalize(mRightDisparity, mRightDisparity, dMin, dMax, CV_MINMAX);
        minMaxLoc(mDisparity, &dMin, &dMax);
        if (bVerbose) {
            printf("Disparite maximale = %lf\n", dMax);
        }
        normalize(mDisparity, mDisparity, dMin, dMax, CV_MINMAX);
        // Et afficher
        imshow("Left", mLeftGray);
        imshow("Right", mRightGray);
        imshow("Left disparity", mLeftDisparity);
        imshow("Right disparity", mRightDisparity);
        imshow("Disparity", mDisparity);
        imshow("Validity mask", mValidityMask);
    }

    // Si affichage, attendre l'appui sur une touche
    if (bDisplay)
        cvWaitKey(0);
    // Sauvegarde des resultats
    if (bSave) {
        imwrite("left-disparity.png", mLeftDisparity);
        imwrite("right-disparity.png", mRightDisparity);
        imwrite("disparity.png", mDisparity);
        imwrite("validity-mask.png", mValidityMask);
    }
    return 0;
}
