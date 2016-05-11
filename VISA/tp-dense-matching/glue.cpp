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
#include <string>
using namespace std;
#include "glue.hpp"

/* --------------------------------------------------------------------------
Fonctions
-------------------------------------------------------------------------- */

// -----------------------------------------------------------------------
/// \brief Affiche le message d'aide a l'utilisation.
///
/// @param sName: nom du fichier executable
/// @param pfStream: stream dans lequel il faut ecrire le texte
/// @return rien
// -----------------------------------------------------------------------
void gluePrintHelpMessage(const string& sName,
                          FILE *pfStream) {
    // Afficher un message d'aide
    fprintf(pfStream,
            "Utilisation: %s [OPTIONS] left right\n" \
            "Determine la carte dense des disparites.\n\n" \
            "Options courtes, options longues, significations et" \
            " valeurs par defaut:\n" \
            "  -m:valeur, --maximum-disparity:  disparite maximale" \
            " (defaut 32)\n" \
            "  -w:valeur, --window-half-size:  demi-taille de la"
            " fenetre de correlation (defaut 2)" \
            "  -v, --verbose  mode bavard (affichage de messages" \
            " d'information)\n" \
            "  -d, --display  affichage d'une fenetre openCV\n" \
            "  -s, --save     enregistre les images resultat\n" \
            "  -h, --help     affichage de ce message d'aide\n",
            sName.c_str());
}
