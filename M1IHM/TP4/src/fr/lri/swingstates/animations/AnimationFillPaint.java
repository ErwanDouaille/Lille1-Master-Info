/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.awt.Color;

import fr.lri.swingstates.canvas.CShape;

/**
 * An animation that changes the fill color of a <code>CElement</code> to a given color smoothly by refreshing every 40 milliseconds.
 * 
 * @author Caroline Appert
 */
public class AnimationFillPaint extends AnimationPaint {
	
	/**
	 * Builds a "fillPaint" animation.
	 * @param colorTarget The final color.
	 */
	public AnimationFillPaint(Color colorTarget) {
		super(colorTarget);
		colorEnd = colorTarget;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
		for(int i = 0; i < animatedShapes.length; i++) {
			animatedShapes[i].setFillPaint(new Color(
					(int)(initialColors[i].getRed()*(1-t) + colorEnd.getRed()*t),
					(int)(initialColors[i].getGreen()*(1-t) + colorEnd.getGreen()*t),
					(int)(initialColors[i].getBlue()*(1-t) + colorEnd.getBlue()*t)));
		}
	}

	protected Color getColorValue(CShape shape) {
		return (Color)shape.getFillPaint();
	}
	
}