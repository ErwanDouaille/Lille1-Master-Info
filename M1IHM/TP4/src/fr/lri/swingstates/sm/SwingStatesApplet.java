/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import fr.lri.swingstates.animations.ANamedTag;
import fr.lri.swingstates.animations.Animation;

/**
 * Contains a single static method <code>init</code> to build applets which uses SwingStates.
 * 
 * @author Caroline Appert
 *
 */
public class SwingStatesApplet {

	private SwingStatesApplet() { }
	
	/**
	 * Initialization method of a swingstates applet
	 * must start by calling this <code>init</code> method.
	 * It resets all static variables used in SwingStates
	 * and animation manager.
	 */
	public static void init() {
		JTag.allJComponentTags = null;
		JNamedTag.namedTags = null;
		ANamedTag.initForApplet();
		Animation.resetAnimationManager();
	}
	
}
