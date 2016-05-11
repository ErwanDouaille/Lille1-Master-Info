/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

/**
 * An animation that never stops, i.e. number of "laps" = -1, and rotates a <code>CElement</code> by a fixed angle every 40 milliseconds.
 * 
 * @author Caroline Appert
 *
 */
public class AnimationRotateBy extends Animation {
	
	private double angle;
	
	/**
	 * Builds a "rotateBy" animation.
	 * @param deltaAngle The fixed angle of rotation (in radians).
	 */
	public AnimationRotateBy(double deltaAngle) {
		super();
		angle = deltaAngle;
		setNbLaps(INFINITE_NUMBER_OF_LAPS);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
		getAnimated().rotateBy(angle);
	}

	/**
	 * @return the fixed angle of rotation.
	 */
	public double getDeltaAngle() {
		return angle;
	}
	
	/**
	 * Sets the fixed angle of rotation.
	 * @param deltaAngle The fixed angle of rotation.
	 * @return this animation.
	 */
	public AnimationRotateBy setDeltaAngle(double deltaAngle) {
		angle = deltaAngle;
		return this;
	}

}