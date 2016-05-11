/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.lri.swingstates.canvas.CElement;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CTag;
import fr.lri.swingstates.canvas.Canvas;

/**
 * An abstract animation that changes the color of a <code>CElement</code> to a given color smoothly by refreshing every 40 milliseconds.
 * 
 * @author Caroline Appert
 *
 */
abstract class AnimationPaint extends Animation {

	private static String ERROR_ONLY_COLOR = "Can only animate java.awt.Color attributes";
	
	protected Color colorEnd;
	protected CShape[] animatedShapes;
	protected Color[] initialColors;
	
	/**
	 * Builds a <code>FillPaint</code> animation.
	 * @param colorTarget The final color.
	 */
	public AnimationPaint(Color colorTarget) {
		super();
		colorEnd = colorTarget;
	}
	
	protected abstract Color getColorValue(CShape shape);
	
	/**
	 * {@inheritDoc}
	 */
	public Animation setAnimatedElement(CElement ce) {
		if(Canvas.class.isAssignableFrom(ce.getClass())) {
			List<CShape> l = ((Canvas)ce).getDisplayList();
			int nb = l.size();
			animatedShapes = new CShape[nb];
			initialColors = new Color[nb];
			int cpt = 0;
			Paint paint;
			synchronized(l) {
				for(Iterator i = l.iterator(); i.hasNext();) {
					CShape next = (CShape) i.next();
					animatedShapes[cpt] = next;
					paint = getColorValue(next);
					if(!(paint instanceof Color)) {
						System.err.println(ERROR_ONLY_COLOR);
					} else {
						initialColors[cpt] = (Color)paint;
					}
					cpt++;
				}
			}
		} else {
			if(CTag.class.isAssignableFrom(ce.getClass())) {
				CTag l = (CTag)ce;
				int nb = ((CTag)ce).size();
				animatedShapes = new CShape[nb];
				initialColors = new Color[nb];
				int cpt = 0;
				Paint paint;
				for(l.reset(); l.hasNext();) {
					CShape next = l.nextShape();
					animatedShapes[cpt] = next;
					paint = getColorValue(next);
					if(!(paint instanceof Color)) {
						System.err.println(ERROR_ONLY_COLOR);
					} else {
						initialColors[cpt] = (Color)paint;
					}
					cpt++;
				}
			} else {
				if(CShape.class.isAssignableFrom(ce.getClass())) {
					animatedShapes = new CShape[1];
					initialColors = new Color[1];
					animatedShapes[0] = (CShape)ce;
					Paint paint = getColorValue((CShape)ce);
					if(!(paint instanceof Color)) {
						System.err.println(ERROR_ONLY_COLOR);
					} else {
						initialColors[0] = (Color)paint;
					}
				}
			}
		}
		super.setAnimatedElement(ce);
		return this;
	}

	/**
	 * @return the final color.
	 */
	public Color getColorTarget() {
		return colorEnd;
	}

	/**
	 * Sets the final color.
	 * @param colorTarget The final color.
	 * @return this animation.
	 */
	public AnimationPaint setColorTarget(Color colorTarget) {
		colorEnd = colorTarget;
		return this;
	}


}
