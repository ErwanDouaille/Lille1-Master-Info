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
using namespace cv;
#include "glue.hpp"
#include "erwan-douaille.hpp"
#include "iostream"

using namespace std;
// -----------------------------------------------------------------------
/// \brief Detecte les coins.
///
/// @param mImage: pointeur vers la structure image openCV
/// @param iMaxCorners: nombre maximum de coins detectes
/// @return matrice des coins
// -----------------------------------------------------------------------
Mat iviDetectCorners(const Mat& mImage,
                     int iMaxCorners) {
    // A modifier !
    vector<Point2f> vCorners;
    goodFeaturesToTrack(mImage,vCorners,iMaxCorners,0.01,10,Mat(),3,false,0.04);
    Mat mCorners(3,vCorners.size(),CV_64F);
    for(int i=0;i<vCorners.size();i++){
        mCorners.at<double>(0,i)=(double)vCorners[i].x;
        mCorners.at<double>(1,i)=(double)vCorners[i].y;
        mCorners.at<double>(2,i)=1;
    }

    // Retour de la matrice
    return mCorners;
}

// -----------------------------------------------------------------------
/// \brief Initialise une matrice de produit vectoriel.
///
/// @param v: vecteur colonne (3 coordonnees)
/// @return matrice de produit vectoriel
// -----------------------------------------------------------------------
Mat iviVectorProductMatrix(const Mat& tmp) {
    Mat mVectorProduct = (Mat_<double>(3,3) <<
        0.0, -tmp.at<double>(2), tmp.at<double>(1),
        tmp.at<double>(2), 0.0, -tmp.at<double>(0),
        -tmp.at<double>(1), tmp.at<double>(0), 0.0);
    // Retour de la matrice
    return mVectorProduct;
}

// -----------------------------------------------------------------------
/// \brief Initialise et calcule la matrice fondamentale.
///
/// @param mLeftIntrinsic: matrice intrinseque de la camera gauche
/// @param mLeftExtrinsic: matrice extrinseque de la camera gauche
/// @param mRightIntrinsic: matrice intrinseque de la camera droite
/// @param mRightExtrinsic: matrice extrinseque de la camera droite
/// @return matrice fondamentale
// -----------------------------------------------------------------------
Mat iviFundamentalMatrix(const Mat& mLeftIntrinsic,const Mat& mLeftExtrinsic,const Mat& mRightIntrinsic,const Mat& mRightExtrinsic) {
    // Doit utiliser la fonction iviVectorProductMatrix
    Mat p1 = mLeftIntrinsic * Mat::eye(3, 4, CV_64F) * mLeftExtrinsic;
    Mat p2 = mRightIntrinsic * Mat::eye(3, 4, CV_64F) * mRightExtrinsic;
    Mat o1 = mLeftExtrinsic.inv().col(3);
    Mat mFundamental = iviVectorProductMatrix(p2*o1) * p2 * p1.inv(DECOMP_SVD);
    return mFundamental;
}

// -----------------------------------------------------------------------
/// \brief Initialise et calcule la matrice des distances entres les
/// points de paires candidates a la correspondance.
///
/// @param mLeftCorners: liste des points 2D image gauche
/// @param mRightCorners: liste des points 2D image droite
/// @param mFundamental: matrice fondamentale
/// @return matrice des distances entre points des paires
// -----------------------------------------------------------------------
Mat iviDistancesMatrix(const Mat& m2DLeftCorners,
                       const Mat& m2DRightCorners,
                       const Mat& mFundamental) {

    int widthLeft = m2DLeftCorners.size().width;
    int widthRight = m2DRightCorners.size().width;
    Mat mDistances(widthLeft, widthRight,CV_64F);

    for(int i=0;i<widthLeft;i++){
        Mat pL = m2DLeftCorners.col(i);
        Mat epipolaireL = mFundamental * pL;
        for(int j = 0; j < widthRight; j++) {
            Mat pR = m2DRightCorners.col(j);
            Mat epipolaireR = mFundamental.t() * pR;
            mDistances.at<double>(i,j) = ((abs(epipolaireL.at<double>(0) * pR.at<double>(0) + epipolaireL.at<double>(1) * pR.at<double>(1) + epipolaireL.at<double>(2)))
                                          /(sqrt(pow(epipolaireL.at<double>(0),2) + pow(epipolaireL.at<double>(1),2)))) +

                                        ((abs(epipolaireR.at<double>(0)*pL.at<double>(0)+epipolaireR.at<double>(1)*pL.at<double>(1)+epipolaireR.at<double>(2)))
                                        /(sqrt(pow(epipolaireR.at<double>(0),2) + pow(epipolaireR.at<double>(1),2))));
        }
    }
    // A modifier !
    // Retour de la matrice fondamentale
    return mDistances;
}

// -----------------------------------------------------------------------
/// \brief Initialise et calcule les indices des points homologues.
///
/// 0@param mDistances: matrice des distances
/// @param fMaxDistance: distance maximale autorisant une association
/// @param mRightHomologous: liste des correspondants des points gauche
/// @param mLeftHomologous: liste des correspondants des points droite
/// @return rien
// -----------------------------------------------------------------------
void iviMarkAssociations(const Mat& mDistances,
                         double dMaxDistance,
                         Mat& mRightHomologous,
                         Mat& mLeftHomologous) {

    int width =mDistances.size().width, height =mDistances.size().height;

    int match = 0, notFound = 0;
    mRightHomologous = Mat::eye(width, 1, CV_64F);
    mLeftHomologous = Mat::eye(height, 1, CV_64F);

    for(int i= 0; i< width ; i++) {
        int min = dMaxDistance;
        int minIndex = -1;
        for(int j= 0; j< height; j++) {
            if(min>mDistances.at<double>(i,j) && mDistances.at<double>(i,j)<dMaxDistance) {
                min = mDistances.at<double>(i,j);
                minIndex = j;
            }
        }
        mRightHomologous.at<double>(i,0) = minIndex;
    }

    for(int i= 0; i< height ; i++) {
        int min = dMaxDistance;
        int minIndex = -1;
        for(int j= 0; j< width; j++) {
            if(min>mDistances.at<double>(j,i) && mDistances.at<double>(j,i)<dMaxDistance) {
                min = mDistances.at<double>(j,i);
                minIndex = j;
            }
        }
        mLeftHomologous.at<double>(i,0) = minIndex;
    }

    cout << endl <<"Right matrix " << endl;
    for(int i = 0; i<mRightHomologous.size().height; i++) {
        if(mLeftHomologous.at<double>(mRightHomologous.at<double>(i,0),0)==i)
            match++;
        else if(mRightHomologous.at<double>(i,0)==-1)
            notFound++;
        cout << i << ": " << mRightHomologous.at<double>(i,0);
        cout << endl;
    }
    cout << "Match: " << match << endl;
    cout << "Not found: " << notFound << endl;
    cout << "Error: " << mRightHomologous.size().height-(match+notFound) << endl;

    match = 0;
    notFound = 0;
    cout << "Left matrix " << endl;
    for(int i = 0; i<mLeftHomologous.size().height; i++) {
        if(mRightHomologous.at<double>(mLeftHomologous.at<double>(i,0),0)==i)
            match++;
        else if (mLeftHomologous.at<double>(i,0)==-1)
            notFound++;
        cout << i << ": " << mLeftHomologous.at<double>(i,0);
        cout << endl;
    }
    cout << "MaxDistance: " << dMaxDistance << endl;
    cout << "Match: " << match << endl;
    cout << "Not found: " << notFound << endl;
    cout << "Error: " << mLeftHomologous.size().height-(match+notFound) << endl;
}
