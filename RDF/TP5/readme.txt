Q1) Les valeurs de l'histogramme representent le nombre de pixels pour un niveau de gris donne. A la vue de la courbe, on peut distinguer 2 classes omega 1 et omega 2 qui ressortent sur l'histogramme. (je te laisse faire un screenshot de l'histogramme)
En regardant les images binarise, on remarque qu'aucune des 3 valeurs ne permet d'obtenir une image correcte pour classer les pixels. Avec la 1ere valeur, on remarque que la partie du bas est assez propre mais pas celle du bas, et inversement avec la 3e valeur. Ceci est du au fait qu'on utilise la meme valeur de seuil pour les 2 partie et qu'il existe un nombre important de pixels ayant un niveaux de gris entre les 2 principaux pics;

Q2)
La probabilite pour un pixel d'etre dans une classe particuliere sont :
Pour la classe omega1, P = 0.56
Pour la classe omega2, P = 0.44

Q3)
Les probabilites d'avoir le niveau de gris 89 dans chaque image sont :

P(89/I) = 0.0192
P(89/o1) = 0.0289
P(89/o2) = 0.0068

Q4)
On trouves un seuil optimal de 92
Le taux d'erreur de classification est de 0.0471
Image obtenue : image_seuil_auto.png

Q5)
modification, je sais pas, j'ai juste change le nom de fichier et ca donne zero.traite.png
