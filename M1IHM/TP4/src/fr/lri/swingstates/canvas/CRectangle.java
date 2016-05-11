/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * A rectangle shape for use with a <code>Canvas</code>.
 * If filled, the fill paint is used to paint the interior.
 * If outlined, the outline paint and stroke are used to draw the boundary.
 * 
 * @author Caroline Appert
 */
public class CRectangle extends CRectangularShape {
	
	/**
	 * Builds an empty CRectangle.
	 */
	public CRectangle() {
		super(new Rectangle2D.Double(0, 0, 1, 1));
	}
	
	/**
	 * Builds a CRectangle.
	 * @param x The x coordinate of the upper left point of the bounding box
	 * @param y The y coordinate of the upper left point of the bounding box
	 * @param w The width of the bounding box
	 * @param h The height of the bounding box
	 */
	public CRectangle(double x, double y, double w, double h) {
		super(new Rectangle2D.Double(x, y, w, h));
	}
	
	/**
	 * Builds a <code>CRectangle</code> having rounded corners.
	 * @param x The x coordinate of the upper left point of the bounding box
	 * @param y The y coordinate of the upper left point of the bounding box
	 * @param w The width of the bounding box
	 * @param h The height of the bounding box
	 * @param arcw The width of the arc to use to round off the corners of the newly constructed <code>CRectangle</code>
	 * @param arch The height of the arc to use to round off the corners of the newly constructed <code>CRectangle</code>
	 */
	public CRectangle(double x, double y, double w, double h, double arcw, double arch) {
		super(new RoundRectangle2D.Double(x, y, w, h, arcw, arch));
	}
	
	/**
	 * Builds a <code>CRectangle</code>.
	 * @param p The upper left point of the bounding box
	 * @param w The width of the bounding box
	 * @param h The height of the bounding box
	 * @param arcw The width of the arc to use to round off the corners of the newly constructed <code>CRectangle</code>
	 * @param arch The height of the arc to use to round off the corners of the newly constructed <code>CRectangle</code>
	 */
	public CRectangle(Point2D p, double w, double h, double arcw, double arch) {
		this(p.getX(), p.getY(), w, h, arcw, arch);
	}
	
	/**
	 * Builds a CRectangle.
	 * @param p The upper left point of the bounding box
	 * @param w The width of the bounding box
	 * @param h The height of the bounding box
	 */
	public CRectangle(Point2D p, double w, double h) {
		this(p.getX(), p.getY(), w, h);
	}

	/**
	 * Creates a new copy of this shape and returns it.
	 * @return the copy.
	 */
	public CShape duplicate() {
		CRectangle sms = new CRectangle();
		copyTo(sms);
		return sms;
	}
	
	/**
	 * Changes the arc used to round off the corners of this <code>CRectangle</code>. 
	 * @param arcw The width of the arc to use to round off the corners
	 * @param arch The height of the arc to use to round off the corners
	 * @return This <code>CRectangle</code>.
	 */
	public CRectangle roundCorners(double arcw, double arch) {
		if(shape instanceof RoundRectangle2D) {
			if(((RoundRectangle2D)shape).getArcWidth() == arcw 
					&& ((RoundRectangle2D)shape).getArcHeight() == arch)
				return this;
		}
		shape = new RoundRectangle2D.Double(
				shape.getBounds2D().getMinX(),
				shape.getBounds2D().getMinY(),
				shape.getBounds2D().getWidth(),
				shape.getBounds2D().getHeight(),
				arcw, arch);
		repaint();
		return this;
	}

}
