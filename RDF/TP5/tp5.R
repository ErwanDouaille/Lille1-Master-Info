
library ("EBImage")
source ("rdfSegmentation.R")


# Extraction de la region

nom <- "rdf-chiffre-0-8bits.png"
image <- rdfReadGreyImage(nom)
seuil1 <- 0.35
seuil2 <- 0.36
seuil3 <- 0.37
binaire1 <- (image - seuil1)>= 0##
binaire2 <- (image - seuil2)>= 0##
binaire3 <- (image - seuil3)>= 0##
#display(binaire1, "binaire 1")
#display(binaire2, "binaire 2")
#display(binaire3, "binaire 3")
nbins <- 256
h <- hist (as.vector (image), breaks = seq (0, 1, 1 / nbins))


#Seuillage automatique - Proba a priori
omega1 <- rdfReadGreyImage("rdf-chiffre-0-8bits_omega1.png")
omega2 <- rdfReadGreyImage("rdf-chiffre-0-8bits_omega2.png")
h1 <- hist (as.vector (omega1), breaks = seq (0, 1, 1 / nbins))
h2 <- hist (as.vector (omega2), breaks = seq (0, 1, 1 / nbins))

N = sum(h$counts) # Nb de pixels total de l'image
N1 = sum(h1$counts) # Nb de pixels de la classe omega 1
N2 = sum(h2$counts) # Nb de pixels de la classe omega 2
Pomega1 = N1/N
Pomega2 = N2/N

# Seuillage automatique - Proba conditionnelle
ndg <- 89
PIconditionnelle <- h$counts[ndg]/N
PO1conditionnelle <- h1$counts[ndg]/N1
PO2conditionnelle <- h2$counts[ndg]/N2

# Seuillage automatique - Proba d'erreur
somme1 = 0:255
somme2 = 0:255
erreur = 0:255
# recherche du minimum
minimum_erreur = 1;
seuil_minimum_erreur = 0;

for (X in 1:255) {
  somme1[X+1]=sum(h1$density[(X+1):256])/sum(h1$density[1:256])
  somme1[X+1]=somme1[X+1]*Pomega1

  somme2[X+1]=sum(h2$density[1:(X+1)])/sum(h2$density[1:256])
  somme2[X+1]=somme2[X+1]*Pomega2
  
  erreur[X+1] = somme1[X+1] + somme2[X+1]
# seuil corrrespondant à l'erreur minimale
  if (erreur[X+1] < minimum_erreur ) 
    seuil_minimum_erreur = X
  if (erreur[X+1] < minimum_erreur ) 
    minimum_erreur = erreur[X+1]
}

#seuillage de l'image
seuil_auto_img <- (image - seuil_minimum_erreur/255)>= 0##
display(seuil_auto_img)



