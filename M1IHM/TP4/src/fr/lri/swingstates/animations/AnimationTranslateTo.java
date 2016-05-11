/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.util.Iterator;
import java.util.List;

import fr.lri.swingstates.canvas.CElement;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CTag;
import fr.lri.swingstates.canvas.Canvas;

/**
 * An animation that translates a <code>CElement</code> to a given location in one second by translating smoothly this <code>CElement</code> every 40 milliseconds.
 * 
 * @author Caroline Appert
 *
 */
public class AnimationTranslateTo extends Animation {
	
	double txEnd, tyEnd;
	CShape[] animatedShapes;
	double[][] initialTranslations;
	
	/**
	 * Builds a "translateTo" animation.
	 * @param xTarget The x of the final position.
	 * @param yTarget The y of the final position.
	 */
	public AnimationTranslateTo(double xTarget, double yTarget) {
		super();
		txEnd = xTarget;
		tyEnd = yTarget;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
		for(int i = 0; i < animatedShapes.length; i++) {
			animatedShapes[i].translateTo(initialTranslations[i][0]*(1-t) + txEnd*t,  initialTranslations[i][1]*(1-t) + tyEnd*t);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doStart() {
		for(int i = 0; i < animatedShapes.length; i++) {
			initialTranslations[i][0] = animatedShapes[i].getCenterX();
			initialTranslations[i][1] = animatedShapes[i].getCenterY();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Animation setAnimatedElement(CElement ce) {
		if(Canvas.class.isAssignableFrom(ce.getClass())) {
			List<CShape> l = ((Canvas)ce).getDisplayList();
			int nb = l.size();
			animatedShapes = new CShape[nb];
			initialTranslations = new double[nb][2];
			int cpt = 0;
			synchronized(l) {
				for(Iterator i = l.iterator(); i.hasNext();) {
					CShape next = (CShape) i.next();
					animatedShapes[cpt] = next;
					initialTranslations[cpt][0] = next.getCenterX();
					initialTranslations[cpt][1] = next.getCenterY();
					cpt++;
				}
			}
		} else {
			if(CTag.class.isAssignableFrom(ce.getClass())) {
				CTag l = (CTag)ce;
				int nb = ((CTag)ce).size();
				animatedShapes = new CShape[nb];
				initialTranslations = new double[nb][2];
				int cpt = 0;
				for(l.reset(); l.hasNext();) {
					CShape next = l.nextShape();
					animatedShapes[cpt] = next;
					initialTranslations[cpt][0] = next.getCenterX();
					initialTranslations[cpt][1] = next.getCenterY();
					cpt++;
				}
			} else {
				if(CShape.class.isAssignableFrom(ce.getClass())) {
					animatedShapes = new CShape[1];
					initialTranslations = new double[1][2];
					animatedShapes[0] = (CShape)ce;
					initialTranslations[0][0] = ((CShape)ce).getCenterX();
					initialTranslations[0][1] = ((CShape)ce).getCenterY();
				}
			}
		}
		super.setAnimatedElement(ce);
		return this;
	}

	/**
	 * @return the x-coordinate of the final location.
	 */
	public double getTranslateXTarget() {
		return txEnd;
	}

	/**
	 * @return the y-coordinate of the final location.
	 */
	public double getTranslateYTarget() {
		return tyEnd;
	}
	
	/**
	 * Sets the final location.
	 * @param xTarget The x-coordinate of the final location.
	 * @param yTarget The y-coordinate of the final location.
	 * @return this animation.
	 */
	public AnimationTranslateTo setTranslateTarget(double xTarget, double yTarget) {
		txEnd = xTarget;
		tyEnd = yTarget;
		return this;
	}

}