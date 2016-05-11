/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Color;

import fr.lri.swingstates.canvas.CExtensionalTag;


// A class of tag having a darken field
class ColorTag extends CExtensionalTag {
	
	Color color;
	
	public ColorTag(Color color) {
		this.color = color;
	}
	
	
}