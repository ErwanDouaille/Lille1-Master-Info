/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A segment for use with an <code>Canvas</code>.
 * @author Caroline Appert
 */

public class CSegment extends CRectangularShape {
	
	/**
	 * Builds an empty CSegment.
	 */
	public CSegment() {
		super();
		outlinePaint = Color.BLACK;
	}
	
	/**
	 * Builds a CSegment.
	 * @param x1 The x-coordinate of the first point of the segment.
	 * @param y1 The y-coordinate of the first point of the segment.
	 * @param x2 The x-coordinate of the second point of the segment.
	 * @param y2 The y-coordinate of the second point of the segment.
	 */
	public CSegment(double x1, double y1, double x2, double y2) {
		super(new Line2D.Double(x1, y1, x2, y2));
	}
	
	/**
	 * Builds a CSegment.
	 * @param p1 The first point of the segment.
	 * @param p2 The second point of the segment.
	 */
	public CSegment(Point2D p1, Point2D p2) {
		super(new Line2D.Double(p1, p2));
	}
	

	/**
	 * Sets the ending points of this segment.
	 * @param p1 One point.
	 * @param p2 The other point.
	 * @return this segment
	 */
	public CSegment setPoints (Point2D p1, Point2D p2) {
		((Line2D)shape).setLine(p1, p2);
		repaint();
		return this;
	}
	
	/**
	 * Creates a new copy of this shape and returns it.
	 * @return the copy.
	 */
	public CShape duplicate() {
		CSegment sms = new CSegment();
		copyTo(sms);
		return sms;
	}
	
	/**
	 * Define this rectangular shape by its bounding box.
	 * @param p1 One corner of the diagonal of the bounding box.
	 * @param p2 The other corner of the diagonal of the bounding box.
	 * @return this rectangular shape.
	*/
	public CRectangularShape setBoundingBox(Point2D p1, Point2D p2) {
		return setDiagonal (p1.getX(), p1.getY(), p2.getX(), p2.getY());
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
		return setDiagonal (minx, miny, minx+width, miny+height);
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
		double minx = Math.min(x1, x2);
		double miny = Math.min(y1, y2);
		double maxx = Math.max(x1, x2);
		double maxy = Math.max(y1, y2);
		
		Point2D point1 = ((Line2D)shape).getP1();
		Point2D point2 = ((Line2D)shape).getP2();
		if(point1.getY() < point2.getY())
			if(point1.getX() < point2.getX()) { 
				point1.setLocation(minx, miny);
				point2.setLocation(maxx, maxy);
			}
			else { 
				point1.setLocation(maxx, miny);
				point2.setLocation(minx, maxy);
			}
		else
			if(point1.getX() < point2.getX()) { 
				point1.setLocation(minx, maxy);
				point2.setLocation(maxx, miny);
			}
			else { 
				point1.setLocation(maxx, maxy);
				point2.setLocation(minx, miny);
			}
		((Line2D)shape).setLine(point1, point2);
		repaint();
		return this;
	}
	
	/**
	 * Define this rectangular shape by its diagonal.
	 * @param p1 One corner of the diagonal.
	 * @param p2 The other corner of the diagonal.
	 * @return this ellipse
	 */
	public CRectangularShape setDiagonal (Point2D p1, Point2D p2) {
		return setBoundingBox(p1, p2);
	}

	
	/**
	 * @return the length of this untransformed <code>CSegment</code>.
	 */
	public double getLength() {
		return (((Line2D)shape).getP1()).distance(((Line2D)shape).getP2());
	}
}
