# -----------------------------------------------------------------------
# Extraction d'attributs de pixels pour la classification,
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

# Chargement d'une image en niveaux de gris
rdfReadGreyImage <- function (nom) {
  image <- readImage (nom)
  if (length (dim (image)) == 2) {
    image
  } else {
    channel (image, 'red')
  }
}

# Moyennage d'une image
rdfMoyenneImage <- function (image, taille) {
  # cote du masque = 2 *taille + 1
  taille <- 2* taille + 1
  masque <- array (taille ^ -2, c (taille, taille))
  # image filtree
  filter2 (image, masque)
}

# Ecart type normalise des voisinages carres d'une image
rdfTextureEcartType <- function (image, taille) {
  # carre de l'image moins sa moyenne
  carre = (image - rdfMoyenneImage (image, taille)) ^ 2
  # ecart type
  ecart = sqrt (rdfMoyenneImage (carre, taille))
  # normalise pour maximum a 1
  ecart / max (ecart)
}

# Calcul de l'histogramme 2D (log + normalise) de deux images
rdfCalculeHistogramme2D <- function (image1, bins1, image2, bins2) {
  # Bins dans les deux images
  indices1 = findInterval (image1, seq (0, 1, 1 / bins1))
  indices2 = findInterval (image2, seq (0, 1, 1 / bins2))
  # Tableau de contingence
  counts <- table (indices1, indices2)
  # Extension en tableau 2D incluant les valeurs nulles
  liste <- as.data.frame (counts)
  h2d <- array (0, c (bins1, bins2))
  h2d[cbind (liste[,1], liste[,2])] <- liste[,3]
  # Passage en log
  h2d <- log (1 + h2d)
  # normalise pour maximum a 1
  as.Image (h2d / max (h2d))
}

