/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * An ellipse shape for use with a <code>Canvas</code>.
 * If filled, the fill paint is used to paint the interior.
 * If outlined, the outline paint and stroke are used to draw the boundary.
 * 
 * @author Caroline Appert
 */
public class CEllipse extends CRectangularShape {
	
	/**
	 * Builds an empty CEllipse.
	 */
	public CEllipse() {
		super(new Ellipse2D.Double(0, 0, 1, 1));
	}
	
	/**
	 * Builds a CEllipse.
	 * @param x The x coordinate of the upper left point of the bounding box
	 * @param y The y coordinate of the upper left point of the bounding box
	 * @param w The width of the bounding box
	 * @param h The height of the bounding box
	 */
	public CEllipse(double x, double y, double w, double h) {
		super(new Ellipse2D.Double(x, y, w, h));
	}
	
	/**
	 * Builds a CEllipse.
	 * @param p The upper left point of the bounding box
	 * @param w The width of the bounding box
	 * @param h The height of the bounding box
	 */
	public CEllipse(Point2D p, double w, double h) {
		super(new Ellipse2D.Double(p.getX(), p.getY(), w, h));
	}

	/**
	 * Creates a new copy of this shape and returns it.
	 * @return the copy.
	 */
	public CShape duplicate() {
		CEllipse sms = new CEllipse();
		copyTo(sms);
		return sms;
	}

}
