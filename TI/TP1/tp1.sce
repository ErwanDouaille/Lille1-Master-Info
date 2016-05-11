// Définition des échantillons sur un axe
// Création d'un vecteur auquel on applique une fonction f(x)= (x/100)+5e-3
// sa ressemble a 0.005 0.015 0.025 0.035 ...
axe = [0:99] / 100 + 5e-3;

// Définition des éléments de surface
// Création d'une matrice 100x100 similaire à axe
x = ones (1:100)' * axe;
// Modification de la matrice x, on change sur l'axe y, x-> y et inversement
y = axe' * ones (1:100);

// Position de la source
xs = 0.5;
ys = 0.5;

// Calcul de la distance
surface = 2*%pi;
angle_solide = surface/(xs)^2;
Io = 100/(2*%pi);
h = 0.5;

d = sqrt ((x - xs).^2 + (y - ys).^2);
r = sqrt (d.^2 + h^2);
// Isotrope 
Epi=Io*h./(r.3)
//Lambertienne
Epl=Io*(h^2)./(r.^4)

// Trace de la fonction distance
plot3d (axe,axe, Epi);

// Visualisation sous forme d'image en niveaux de gris
imshow(Ep/max(Ep));
