/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

/**
 * An animation that never stops, i.e. number of "laps" = -1, and translates a <code>CElement</code> by fixed steps along the x-axis and y-axis every 40 milliseconds.
 * 
 * @author Caroline Appert
 *
 */
public class AnimationTranslateBy extends Animation {
	
	double deltax, deltay;
	
	/**
	 * Builds a "translateBy" animation.
	 * @param dx The fixed step of translation along x-axis.
	 * @param dy The fixed step of translation along y-axis.
	 */
	public AnimationTranslateBy(double dx, double dy) {
		super();
		deltax = dx;
		deltay = dy;
		setNbLaps(INFINITE_NUMBER_OF_LAPS);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
		getAnimated().translateBy(deltax, deltay);
	}

	/**
	 * @return the fixed step of translation along x-axis.
	 */
	public double getDx() {
		return deltax;
	}
	
	/**
	 * @return the fixed step of translation along y-axis.
	 */
	public double getDy() {
		return deltay;
	}

	/**
	 * Sets the fixed steps along the x-axis and y-axis.
	 * @param dx The fixed step of translation along x-axis.
	 * @param dy The fixed step of translation along y-axis.
	 * @return this animation.
	 */
	public AnimationTranslateBy setDelta(double dx, double dy) {
		deltax = dx;
		deltay = dy;
		return this;
	}

}