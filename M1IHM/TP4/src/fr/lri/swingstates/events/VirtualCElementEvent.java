/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.events;

import fr.lri.swingstates.canvas.CElement;


/**
 * An event originated by an <code>VirtualCElementEvent</code>.
 * Typically used to create a collision event.
 * 
 * @author Caroline Appert
 *
 */
public class VirtualCElementEvent extends VirtualEvent {

	CElement cElement;
	
	/**
	 * Builds an event originated by a <code>CElement</code>.
	 * @param ce The <code>CElement</code>.
	 */
	public VirtualCElementEvent(CElement ce) {
		super("CElement", ce);
		cElement = ce;
	}

	/**
	 * @return The <code>CElement</code>.
	 */
	public CElement getCElement() {
		return cElement;
	}

}
