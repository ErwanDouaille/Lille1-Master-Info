
// --------------------------------------------------------------------
// Licence Professionnelle GPI, specialite Vision Industrielle
// Travaux Pratiques UE3: Analyse des Images
// F. Cabestaing (2008)
// --------------------------------------------------------------------
// TP2: Simulation des différents types de cameras
// Fichier principal
// --------------------------------------------------------------------

// --------------------------------------------------------------------
// Definition des couleurs standard
// --------------------------------------------------------------------
global_settings { ambient_light rgb <1,1,1> }
#include "colors.inc"

// --------------------------------------------------------------------
// Source ponctuelle de lumiere
// --------------------------------------------------------------------
light_source {
  <0,-50000,0>, rgb <1,1,1>
}

// --------------------------------------------------------------------
// Inclure la definition de la mire
// --------------------------------------------------------------------
#declare iCornersX = 11; // Nombre de coins sur la grille en X
#declare iCornersY = 7; // Nombre de coins sur la grille en Y
#declare dSensorSizeX = 9.024; // Cote X du capteur
#declare dSensorSizeY = 6.768; // Cote Y du capteur
#declare dFocalLength = 50; // Longueur focale
#declare dGridSize = 100; // Cote d'un carre de la grille
#include "checker.inc"

// --------------------------------------------------------------------
// Camera
// --------------------------------------------------------------------
// Position
camera {
  location <vPositionCameraX,vPositionCameraY,vPositionCameraZ>
  direction dFocalLength * y
  right dSensorSizeX * x
  up dSensorSizeY * z
  rotate dAngleX * x
  rotate dAngleY * y
  rotate dAngleZ * z
}

// --------------------------------------------------------------------
// Mire
// --------------------------------------------------------------------
object { Mire }

