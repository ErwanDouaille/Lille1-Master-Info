/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 * An abstract rectangular shape for use with an <code>Canvas</code>.
 * <code>CRectangularShape</code> is the base class for <code>CEllipse</code> and <code>CRectangle</code>,
 * it provides methods to manipulate those shapes by their rectangular bounding box.
 * @author Caroline Appert
 */
public abstract class CRectangularShape extends CShape {

	/**
	 * Builds a rectangular shape.
	 * @param s The shape (Rectangle2D or Ellipse2D)
	 */
	public CRectangularShape(Shape s) {
		super(s);
	}
	
	/**
	 * Builds an empty rectangular shape.
	 */
	public CRectangularShape() {
		super();
	}

	/**
	 * Define this rectangular shape by its bounding box.
	 * @param p1 One corner of the diagonal of the bounding box.
	 * @param p2 The other corner of the diagonal of the bounding box.
	 * @return this rectangular shape.
	*/
	public CRectangularShape setBoundingBox(Point2D p1, Point2D p2) { 
		double minx = Math.min(p1.getX(), p2.getX());
		double miny = Math.min(p1.getY(), p2.getY());
		double width = Math.abs(p1.getX() - p2.getX());
		double height = Math.abs(p1.getY() - p2.getY());
		((RectangularShape)shape).setFrame(minx, miny, width, height);
		repaint();
		return this;
	}
	
	/**
	 * Define this rectangular shape by its bounding box.
	 * @param minx The x coordinate of the upper left corner of the bounding box.
	 * @param miny The y coordinate of the upper left corner of the bounding box.
	 * @param width The width of the bounding box.
	 * @param height The height of the bounding box.
	 * @return this rectangular shape.
	 */
	public CRectangularShape setBoundingBox(double minx, double miny, double width, double height) { 
//		((RectangularShape)shape).setFrame(minx, miny, width, height);
//		changedShape();
//		return this;
		((RectangularShape)shape).setFrameFromDiagonal(minx, miny, minx+width, miny+height);
		repaint();
		return this;
	}
	
	/**
	 * Define this rectangular shape by its diagonal.
	 * @param x1 The x coordinate of one corner of the diagonal.
	 * @param y1 The y coordinate of one corner of the diagonal.
	 * @param x2 The x coordinate of the other corner of the diagonal.
	 * @param y2 The y coordinate of the other corner of the diagonal.
	 * @return this rectangle
	 */
	public CRectangularShape setDiagonal (double x1, double y1, double x2, double y2) {
//		double minx = Math.min(x1, x2);
//		double miny = Math.min(y1, y2);
//		double width = Math.abs(x1 - x2);
//		double height = Math.abs(y1 - y2);
		((RectangularShape)shape).setFrameFromDiagonal(x1, y1, x2, y2);
		repaint();
//		try {
//			AffineTransform inverseTransform = getAbsTransform().createInverse();
//			Point2D p1 = new Point2D.Double();
//			Point2D p2 = new Point2D.Double();
//			inverseTransform.transform(new Point2D.Double(minx, miny), p1);
//			inverseTransform.transform(new Point2D.Double(minx+width, miny+height), p2);
////			((RectangularShape)shape).setFrame(p1.getX(), p1.getY(), p2.getX() - p1.getX(), p2.getY() - p1.getY());
//			changedShape();
//		} catch (NoninvertibleTransformException e) {
//			e.printStackTrace();
//		}
		return this;
	}
	
	/**
	 * Define this rectangular shape by its diagonal.
	 * The coordinates are relative to the untransformed geometry of the shape,
	 * i.e. it modifies the initial bounds of this shape.
	 * @param p1 One corner of the diagonal.
	 * @param p2 The other corner of the diagonal.
	 * @return this ellipse
	 */
	public CRectangularShape setDiagonal (Point2D p1, Point2D p2) {
		((RectangularShape)shape).setFrameFromDiagonal(p1, p2);
		repaint();
		return this;
	}
	
	/**
	 * Sets the width of this <code>CRectangularShape</code>.
	 * @param w The new width
	 * @return this <code>CRectangularShape</code>.
	 */
	public CRectangularShape setWidth(double w) {
		return setDiagonal(getCenterX()-w/2, getMinY(), getCenterX()+w/2, getMaxY());
	}
	
	/**
	 * Sets the height of this <code>CRectangularShape</code>.
	 * @param h The new height
	 * @return this <code>CRectangularShape</code>.
	 */
	public CRectangularShape setHeight(double h) {
		return setDiagonal(getMinX(), getCenterY()-h/2, getMaxX(), getCenterY()+h/2);
	}
}
