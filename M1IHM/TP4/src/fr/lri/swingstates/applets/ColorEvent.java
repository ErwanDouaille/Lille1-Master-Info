/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Color;
import java.util.EventObject;

import fr.lri.swingstates.sm.StateMachine;

/**
 * Color events.
 * 
 * @author Caroline Appert
 *
 */
class ColorEvent extends EventObject {

	Color color;
	
	public ColorEvent(StateMachine sm, Color c) {
		super(c);
		color = c;
	}
	
	public Color getColor() {
		return color;
	}
	
}