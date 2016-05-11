/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.lri.swingstates.canvas.CElement;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CTag;
import fr.lri.swingstates.canvas.Canvas;

/**
 * An animation that scales a <code>CElement</code> to a given factor by scaling smoothly this <code>CElement</code> every 40 milliseconds.
 * 
 * @author Caroline Appert
 *
 */
public class AnimationScaleTo extends Animation {
	
	double sxEnd, syEnd;
	CShape[] animatedShapes;
	double[][] scales;
	
	/**
	 * Builds a "scaleTo" animation.
	 * @param sxTarget The final scale factor along the x-axis.
	 * @param syTarget The final scale factor along the y-axis.
	 */
	public AnimationScaleTo(double sxTarget, double syTarget) {
		super();
		sxEnd = sxTarget;
		syEnd = syTarget;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
			for(int i = 0; i < animatedShapes.length; i++) {
				animatedShapes[i].scaleTo(scales[i][0]+(sxEnd-scales[i][0])*t, scales[i][1]+(syEnd-scales[i][1])*t);
			}

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doStart() {
		for(int i = 0; i < animatedShapes.length; i++) {
			scales[i][0] = animatedShapes[i].getScaleX();
			scales[i][1] = animatedShapes[i].getScaleY();
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
			scales = new double[nb][2];
			int cpt = 0;
			synchronized(l) {
				for(Iterator i = l.iterator(); i.hasNext();) {
					CShape next = (CShape) i.next();
					animatedShapes[cpt] = next;
					scales[cpt][0] = next.getScaleX();
					scales[cpt][1] = next.getScaleY();
					cpt++;
				}
			}
		} else {
			if(CTag.class.isAssignableFrom(ce.getClass())) {
				CTag l = (CTag)ce;
				int nb = ((CTag)ce).size();
				animatedShapes = new CShape[nb];
				scales = new double[nb][2];
				int cpt = 0;
				for(l.reset(); l.hasNext();) {
					CShape next = l.nextShape();
					animatedShapes[cpt] = next;
					scales[cpt][0] = next.getScaleX();
					scales[cpt][1] = next.getScaleY();
					cpt++;
				}
			} else {
				if(CShape.class.isAssignableFrom(ce.getClass())) {
					animatedShapes = new CShape[1];
					scales = new double[1][2];
					animatedShapes[0] = (CShape)ce;
					scales[0][0] = ((CShape)ce).getScaleX();
					scales[0][1] = ((CShape)ce).getScaleY();
				}
			}
		}
		super.setAnimatedElement(ce);
		return this;
	}

	/**
	 * @return the final scale factor along the x-axis.
	 */
	public double getScaleXTarget() {
		return sxEnd;
	}

	/**
	 * @return the final scale factor along the y-axis.
	 */
	public double getScaleYTarget() {
		return syEnd;
	}
	
	/**
	 * Sets the final scale factors along the x-axis and the y-axis.
	 * @param sxTarget The final scale factor along the x-axis.
	 * @param syTarget The final scale factor along the y-axis.
	 * @return this animation.
	 */
	public AnimationScaleTo setScaleTarget(double sxTarget, double syTarget) {
		sxEnd = sxTarget;
		syEnd = syTarget;
		return this;
	}

	
	

}