/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

/**
 * An animation that never stops, i.e. number of "laps" = -1, and scales a <code>CElement</code> by fixed scale factors along the x-axis and the y-axis every 40 milliseconds.
 * 
 * @author Caroline Appert
 *
 */
public class AnimationScaleBy extends Animation {
	
	double sdx, sdy;
	
	/**
	 * Builds a "scaleBy" animation.
	 * @param dxScale The fixed scale factor along the x-axis.
	 * @param dyScale The fixed scale factor along the y-axis.
	 */
	public AnimationScaleBy(double dxScale, double dyScale) {
		super();
		sdx = dxScale;
		sdy = dyScale;
		setNbLaps(INFINITE_NUMBER_OF_LAPS);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
		getAnimated().scaleBy(sdx, sdy);
	}

	/**
	 * @return the fixed scale factor along the x-axis.
	 */
	public double getDeltaScaleX() {
		return sdx;
	}
	
	/**
	 * @return the fixed scale factor along the y-axis.
	 */
	public double getDeltaScaleY() {
		return sdy;
	}

	/**
	 * Sets the fixed scale factors along the x-axis and y-axis.
	 * @param dx The fixed scale factor along the x-axis.
	 * @param dy The fixed scale factor along the y-axis.
	 * @return this animation.
	 */
	public AnimationScaleBy setDelta(double dx, double dy) {
		sdx = dx;
		sdy = dy;
		return this;
	}

}