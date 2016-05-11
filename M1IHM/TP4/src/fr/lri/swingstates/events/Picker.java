/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.events;

import java.awt.geom.Point2D;

/**
 * The interface for all types of <code>Picker</code>s used in the SwingStates toolkit. 
 * 
 * @author Caroline Appert
 *
 */
public interface Picker {
	
	/**
	 * @return The current location of this picker.
	 */
	Point2D getLocation();
	
	/**
	 * Moves this <code>Picker</code>.
	 * @param location The new location.
	 */
	void move(Point2D location);
	
}
