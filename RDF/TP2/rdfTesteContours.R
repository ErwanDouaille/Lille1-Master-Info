# -----------------------------------------------------------------------
# Extraction d'attributs de contours,
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
source ("rdfContours.R")

# Chargement d'un contour
#nom <- "rdf-carre-80.txt"
#cont <- rdfChargeFichierContour (nom)
#print(cont)
# Afficher le contour
#plot (cont, main = nom, type = "o", asp = 1, col = "black", ylim = rev (range (Im (cont))))

nom <- "rdf-cercle-80.txt"
cont <- rdfChargeFichierContour (nom)

#print(cont)
# Afficher le contour
#plot (cont, main = nom, type = "o", asp = 1, col = "red", ylim = rev (range (Im (cont))))
#lines(cont[seq(0,80, 4)], col="blue")
#lines(cont[seq(0,80, 8)], col="green")


#plot(cont, main = nom, type = "o", asp = 1, col = "red", ylim = rev (range (Im (cont))))
#lines(fft(cont), main = nom, type = "o", asp = 1, col = "purple", ylim = rev (range (Im (cont))))
#lines(fft(fft(cont),inverse=TRUE)/80, main = nom, type = "o", asp = 1, col = "black", ylim = rev (range (Im (cont))))
#lines(rdfAnnuleDescFourrier(cont, 0.8),col="green")
#lines(rdfAlgorithmeCorde(cont, dmax=1), col="blue")
#lines(rdfAlgorithmeCorde(cont, dmax=0.5), col="green")
nom <- "rdf-rectangle-horizontal.png"
img <- rdfReadGreyImage (nom)
cont <- rdfContour (img)

plot(cont, main = nom, type = "o", asp = 1, col = "red",  xlim = c(5,24), ylim = c(0,25))

lines(fft(fft(cont),inverse=TRUE)/length(cont), main = nom, type = "o", pch=22, lty=2,  col = "green")
#lines(rdfAnnuleDescFourrier(cont, 0.8),col="green")
lines(rdfAlgorithmeCorde(cont, dmax=1), col="blue")
#lines(rdfAlgorithmeCorde(cont, dmax=0.5), col="green")
