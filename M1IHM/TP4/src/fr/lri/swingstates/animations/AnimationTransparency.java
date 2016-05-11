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
 * An animation that continuously changes the transparency (interior and/or outline) of a <code>CElement</code> to a given transparency level.
 * By default, The pacing function is linear.
 * 
 * @author Caroline Appert
 *
 */
public class AnimationTransparency extends Animation {

	private float transparencyFillEnd;
	private float transparencyOutlineEnd;
	private CShape[] animatedShapes;
	private float[] initialTransparencyOutline;
	private float[] initialTransparencyFill;
	
	/**
	 * Builds a <code>AnimationTransparency</code> that changes the transparency of both interior and outline of a <code>CElement</code>.
	 * @param t The target transparency level.
	 */
	public AnimationTransparency(float t) {
		transparencyFillEnd = t;
		transparencyOutlineEnd = t;
	}
	
	/**
	 * Builds a <code>AnimationTransparency</code> that changes the transparency of a <code>CElement</code>.
	 * @param tFill The target transparency level for interior.
	 * @param tOutline The target transparency level for interior.
	 */
	public AnimationTransparency(float tFill, float tOutline) {
		transparencyFillEnd = tFill;
		transparencyOutlineEnd = tOutline;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doStart() {
		for(int i = 0; i < animatedShapes.length; i++) {
			initialTransparencyFill[i] = animatedShapes[i].getTransparencyFill().getAlpha();
			initialTransparencyOutline[i] = animatedShapes[i].getTransparencyOutline().getAlpha();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void step(double t) {
		for(int i = 0; i < animatedShapes.length; i++) {
			animatedShapes[i].setTransparencyFill((float)(initialTransparencyFill[i]*(1-t) + transparencyFillEnd*t));
			animatedShapes[i].setTransparencyOutline((float)(initialTransparencyOutline[i]*(1-t) + transparencyOutlineEnd*t));
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
			initialTransparencyOutline = new float[nb];
			initialTransparencyFill = new float[nb];
			int cpt = 0;
			synchronized(l) {
				for(Iterator i = l.iterator(); i.hasNext();) {
					CShape next = (CShape) i.next();
					animatedShapes[cpt] = next;
					initialTransparencyOutline[cpt] = next.getTransparencyOutline().getAlpha();
					initialTransparencyFill[cpt] = next.getTransparencyFill().getAlpha();
					cpt++;
				}
			}
		} else {
			if(CTag.class.isAssignableFrom(ce.getClass())) {
				CTag l = (CTag)ce;
				int nb = ((CTag)ce).size();
				animatedShapes = new CShape[nb];
				initialTransparencyOutline = new float[nb];
				initialTransparencyFill = new float[nb];
				int cpt = 0;
				for(l.reset(); l.hasNext();) {
					CShape next = l.nextShape();
					animatedShapes[cpt] = next;
					initialTransparencyOutline[cpt] = next.getTransparencyOutline().getAlpha();
					initialTransparencyFill[cpt] = next.getTransparencyFill().getAlpha();
					cpt++;
				}
			} else {
				if(CShape.class.isAssignableFrom(ce.getClass())) {
					animatedShapes = new CShape[1];
					initialTransparencyOutline = new float[1];
					initialTransparencyFill = new float[1];
					animatedShapes[0] = (CShape)ce;
					initialTransparencyOutline[0] = ((CShape)ce).getTransparencyOutline().getAlpha();
					initialTransparencyFill[0] = ((CShape)ce).getTransparencyFill().getAlpha();
				}
			}
		}
		super.setAnimatedElement(ce);
		return this;
	}
	
	/**
	 * @return the final transparency value for interior.
	 */
	public float getTransparencyFillTarget() {
		return transparencyFillEnd;
	}

	/**
	 * Sets the final transparency value for interior.
	 * @param t The final angle of rotation.
	 * @return this animation.
	 */
	public AnimationTransparency setTransparencyFillTarget(float t) {
		transparencyFillEnd = t;
		return this;
	}
	
	/**
	 * @return the final transparency value for outline.
	 */
	public float getTransparencyOutlineTarget() {
		return transparencyOutlineEnd;
	}

	/**
	 * Sets the final transparency value for outline.
	 * @param t The final angle of rotation.
	 * @return this animation.
	 */
	public AnimationTransparency setTransparencyOutlineTarget(float t) {
		transparencyOutlineEnd = t;
		return this;
	}
	
}
