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
 * An animation that rotates a <code>CElement</code> to a given angle in one second by rotating smoothly this <code>CElement</code> every 40 milliseconds.
 * 
 * @author Caroline Appert
 *
 */
public class AnimationRotateTo extends Animation {
	
	private double angleEnd;
	private CShape[] animatedShapes;
	private double[] initialRotations;
	
	/**
	 * Builds a "rotateTo" animation.
	 * @param angleTarget The final angle.
	 */
	public AnimationRotateTo(double angleTarget) {
		super();
		angleEnd = angleTarget;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
		for(int i = 0; i < animatedShapes.length; i++) {
			animatedShapes[i].rotateTo(initialRotations[i]*(1-t) + angleEnd*t);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doStart() {
		for(int i = 0; i < animatedShapes.length; i++) {
			initialRotations[i] = animatedShapes[i].getRotation();
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
			initialRotations = new double[nb];
			int cpt = 0;
			synchronized(l) {
				for(Iterator i = l.iterator(); i.hasNext();) {
					CShape next = (CShape) i.next();
					animatedShapes[cpt] = next;
					initialRotations[cpt] = next.getRotation();
					cpt++;
				}
			}
		} else {
			if(CTag.class.isAssignableFrom(ce.getClass())) {
				CTag l = (CTag)ce;
				int nb = ((CTag)ce).size();
				animatedShapes = new CShape[nb];
				initialRotations = new double[nb];
				int cpt = 0;
				for(l.reset(); l.hasNext();) {
					CShape next = l.nextShape();
					animatedShapes[cpt] = next;
					initialRotations[cpt] = next.getRotation();
					cpt++;
				}
			} else {
				if(CShape.class.isAssignableFrom(ce.getClass())) {
					animatedShapes = new CShape[1];
					initialRotations = new double[1];
					animatedShapes[0] = (CShape)ce;
					initialRotations[0] = ((CShape)ce).getRotation();
				}
			}
		}
		super.setAnimatedElement(ce);
		return this;
	}

	/**
	 * @return the final angle.
	 */
	public double getRotateAngleTarget() {
		return angleEnd;
	}

	/**
	 * Sets the final angle of rotation.
	 * @param angle The final angle of rotation.
	 * @return this animation.
	 */
	public AnimationRotateTo setRotateAngleTarget(double angle) {
		angleEnd = angle;
		return this;
	}

}