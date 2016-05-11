# -----------------------------------------------------------------------
# Extraction d'attributs de forme,
# Module RdF, reconnaissance de formes
# Copyleft (C) 2014, Universite Lille 1
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
# -----------------------------------------------------------------------

# Chargement des fonctions externes
library ("EBImage")
source ("rdfMoments.R")

# Chargement d'une image d'un seul objet
#nom <- "rdf-rectangle-horizontal.png";
# image <- channel(readImage (nom), 'r');
#image <- 	readImage (nom);
#if (interactive ()) {
#  display (image, nom)
#}


nom <- "rdf-rectangle-horizontal.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Horizontal")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-rectangle-vertical.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Vertical")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)


nom <- "rdf-rectangle-diagonal.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Diagonal")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)


nom <- "rdf-rectangle-diagonal-lisse.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Diagonal lisse")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)


















nom <- "rdf-carre-6.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Carre 6")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-carre-10.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Carre 10")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-carre-10-30deg.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Carre 10 30")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-carre-10-45deg.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Carre 10 45")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-carre-20.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Carre 20")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)



















nom <- "rdf-triangle-10.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Triangle 10")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-triangle-10-15deg.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Triangle 10 deg 15")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-triangle-10-45deg.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Triangle 10 deg 45")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-triangle-10-60deg.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Triangle 10 deg 60")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)

nom <- "rdf-triangle-20.png";
image <- 	rdfReadGreyImage (nom);
surface <- rdfSurface (image)
print("")
print("Triangle 20")
print("Matrice d'inertie")
print(rdfMatriceInertie(image))
eigen <- inertieAxe(image)
print("Valeurs propres")
print(eigen[1])
print("Vecteurs propres")
print(eigen[2])
print("Normalise")
print(rdfMomentCentreNormalise(image,2,2))
rdfMomentInvariant(image)



nom <- "rdf-chiffre-0.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 0")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-1.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 1")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-2.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 2")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-3.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 3")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-4.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 4")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-5.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 5")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-6.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 6")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-7.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 7")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-8.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 8")
rdfMomentInvariant(image)
nom <- "rdf-chiffre-9.png";
image <- 	rdfReadGreyImage (nom);
print("")
print("Chiffre 9")
rdfMomentInvariant(image)
