/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.events;

import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CShape;

/**
 * A virtual event originated on a <code>CShape</code>.
 * 
 * @author Caroline Appert
 *
 */
public class VirtualCanvasEvent extends VirtualEvent {

	private CShape cshape;
	private int modifier;
	private Point2D point;
	
	/**
	 * Builds a <code>VirtualShapeEvent</code>.
	 * @param pt The point on which this event occured.
	 */
	public VirtualCanvasEvent(Point2D pt) {
		super(null, pt);
		point = pt;
	}
	
	/**
	 * Builds a <code>VirtualShapeEvent</code>.
	 * @param n The name of the event.
	 * @param pt The point on which this event occured.
	 */
	public VirtualCanvasEvent(String n, Point2D pt) {
		super(n, pt);
		point = pt;
	}
	
	/**
	 * Builds a <code>VirtualShapeEvent</code>.
	 * @param n The name of the event.
	 * @param shape The <code>CShape</code>.
	 * @param pt The point on which this event occurred.
	 */
	public VirtualCanvasEvent(String n, CShape shape, Point2D pt) {
		super(n, pt);
		point = pt;
		cshape = shape;
	}
	
	/**
	 * Builds a <code>VirtualShapeEvent</code>.
	 * @param shape The <code>CShape</code>.
	 * @param pt The point on which this event occurred.
	 */
	public VirtualCanvasEvent(CShape shape, Point2D pt) {
		super(null, pt);
		point = pt;
		cshape = shape;
	}
	
	/**
	 * @return The <code>CShape</code> on which this event occured.
	 */
	public CShape getShape() {
		return cshape;
	}

	/**
	 * @return The point on which this event occured.
	 */
	public Point2D getPoint() {
		return point;
	}

	/**
	 * @return The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT (constants of class <code>BasicInputStateMachine</code>)
	 */
	public int getModifier() {
		return modifier;
	}
	
	/**
	 * Sets the picked shape for this event.
	 * @param shape The picked shape
	 */
	public void setShape(CShape shape) {
		this.cshape = shape;
	}
	
}
