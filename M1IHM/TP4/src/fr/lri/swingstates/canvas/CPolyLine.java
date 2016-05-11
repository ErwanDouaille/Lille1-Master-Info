/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

/**
 * An arbitrary shape for use with a <code>Canvas</code>. If filled, the fill
 * paint is used to paint the interior. If outlined, the outline paint and
 * stroke are used to draw the boundary. The shape is build using path operators
 * such as <code>lineTo</code>.
 * 
 * @author Caroline Appert
 */
public class CPolyLine extends CShape {

	private double startX, startY;
	private Point2D lastPoint, currentPoint;

	/**
	 * Builds an empty CPolyLine.
	 */
	public CPolyLine() {
		super();
		shape = new GeneralPath();
		lastPoint = new Point2D.Double();
		currentPoint = new Point2D.Double();
	}

	/**
	 * Builds a CPolyLine with an initial point (x, y).
	 * 
	 * @param x
	 *            The x coordinate of the initial point
	 * @param y
	 *            The y coordinate of the initial point
	 */
	public CPolyLine(double x, double y) {
		super(new GeneralPath());
		lastPoint = new Point2D.Double();
		currentPoint = new Point2D.Double();
		init(x, y);
	}

	/**
	 * Builds a CPolyLine with an initial point pt.
	 * 
	 * @param pt
	 *            The initial point
	 */
	public CPolyLine(Point2D pt) {
		this(pt.getX(), pt.getY());
	}

	private void init(double x, double y) {
		startX = (float) x;
		startY = (float) y;
		lastPoint.setLocation(x, y);
		currentPoint.setLocation(x, y);
		((GeneralPath) shape).moveTo((float) x, (float) y);
		repaint();
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape fixReferenceShapeToCurrent() {
		Point2D ptStart = new Point2D.Double(startX, startY);
		getAbsTransform().transform(ptStart, ptStart);
		startX = ptStart.getX();
		startY = ptStart.getY();
		getAbsTransform().transform(lastPoint, lastPoint);
		getAbsTransform().transform(currentPoint, currentPoint);
		super.fixReferenceShapeToCurrent();
		return this;
	}

	// ______________________________ MANIPULATE SEGMENTS
	// _____________________________

	/**
	 * Resets this polyline to the unique point (x, y).
	 * 
	 * @param x
	 *            The x coordinate of the initial point
	 * @param y
	 *            The y coordinate of the initial point
	 * @return this polyLine
	 */
	public CPolyLine reset(double x, double y) {
		((GeneralPath) shape).reset();
		init(x, y);
		return this;
	}

	/**
	 * Resets this polyline to the unique point pt.
	 * 
	 * @param pt
	 *            The point
	 * @return this polyLine
	 */
	public CPolyLine reset(Point2D pt) {
		((GeneralPath) shape).reset();
		init(pt.getX(), pt.getY());
		return this;
	}

	private void initIfEmpty() {
		if (((GeneralPath) shape).getCurrentPoint() == null) {
			init(0, 0);
		}
	}

	/**
	 * Adds a line segment to this polyline from the current point to point (x,
	 * y).
	 * 
	 * @param x
	 *            The x coordinate of the last point
	 * @param y
	 *            the y coordinate of the last point
	 * @return this polyLine
	 */
	public CPolyLine lineTo(double x, double y) {
		initIfEmpty();
		((GeneralPath) shape).lineTo((float) x, (float) y);
		lastPoint.setLocation(currentPoint);
		currentPoint.setLocation(x, y);
		repaint();
		return this;
	}

	/**
	 * Adds a line segment to this polyline from the current point to point pt.
	 * 
	 * @param pt
	 *            The point
	 * @return this polyLine
	 */
	public CPolyLine lineTo(Point2D pt) {
		return lineTo(pt.getX(), pt.getY());
	}

	/**
	 * Sets the current point of this polyLine to point (x, y).
	 * 
	 * @param x
	 *            The x coordinate of the current point to set
	 * @param y
	 *            the y coordinate of the current point to set
	 * @return this polyLine
	 */
	public CPolyLine moveTo(double x, double y) {
		if (((GeneralPath) shape).getCurrentPoint() == null) {
			startX = x;
			startY = y;
		}
		((GeneralPath) shape).moveTo((float) x, (float) y);
		lastPoint.setLocation(x, y);
		currentPoint.setLocation(x, y);
		return this;
	}

	/**
	 * Sets the current point of this polyLine to point pt.
	 * 
	 * @param pt
	 *            The point
	 * @return this polyLine
	 */
	public CPolyLine moveTo(Point2D pt) {
		return moveTo(pt.getX(), pt.getY());
	}

	/**
	 * Builds an arrow to end the current segment of this polyline. The arrow
	 * has the same graphical and geometrical attributes than this polyline but
	 * it is a NEW <code>CPolyLine</code>. It is not linked to this polyline
	 * and it is not added to the canvas.
	 * 
	 * @param extent
	 *            The extent of the arrow (in radians)
	 * @param sizeArrow
	 *            The length of the arrow.
	 * @return The built arrow, null if distance between last point and current
	 *         point is equal to zero.
	 */
	public CPolyLine getArrow(double extent, double sizeArrow) {
		initIfEmpty();
		double dis = lastPoint.distance(currentPoint);
		double t = sizeArrow / dis;
		if (dis == 0)
			return null;
		Point2D tmp = new Point2D.Double((1 - t) * currentPoint.getX() + t * lastPoint.getX(), (1 - t) * currentPoint.getY() + t * lastPoint.getY());
		AffineTransform rotatePos = AffineTransform.getRotateInstance(extent, currentPoint.getX(), currentPoint.getY());
		AffineTransform rotateNeg = AffineTransform.getRotateInstance(-extent, currentPoint.getX(), currentPoint.getY());
		Point2D tmpTransformed = new Point2D.Double();
		rotatePos.transform(tmp, tmpTransformed);

		CPolyLine res = new CPolyLine((float) tmpTransformed.getX(), (float) tmpTransformed.getY());
		res.lineTo(currentPoint);
		rotateNeg.transform(tmp, tmpTransformed);
		res.lineTo((float) tmpTransformed.getX(), (float) tmpTransformed.getY());

		res.pickable = pickable;
		res.drawable = drawable;
		res.filled = filled;
		res.fillPaint = fillPaint;
		res.outlined = outlined;
		res.outlinePaint = outlinePaint;
		res.stroke = stroke;
		res.transparencyFill = transparencyFill;
		res.transparencyOutline = transparencyOutline;
		if (res.renderingHints != null)
			res.renderingHints = (RenderingHints) renderingHints.clone();
		res.clip = clip;
		res.rx = rx;
		res.ry = ry;
		res.tx = tx;
		res.ty = ty;
		res.theta = theta;
		res.sx = sx;
		res.sy = sy;
		res.transform = new AffineTransform();
		// res.absShape = null;

		res.changedTransform();
		res.repaint();

		return res;
	}

	/**
	 * Adds an arc segment to this polyline. The arc is part of an ellipse of x
	 * radius rx and y radius ry centered at the current point and going from
	 * angle start to angle start+extent. See further explanations <a
	 * href="http://swingstates.sourceforge.net/tutorial/statemachines/arcTo.html">here</a>.
	 * 
	 * @param start
	 *            The starting angle of the ellipse
	 * @param extent
	 *            The extent angle of the arc
	 * @param rx
	 *            The x radius of the ellipse
	 * @param ry
	 *            The y radius of the ellipse
	 * @return this polyLine
	 */
	public CPolyLine arcTo(double start, double extent, double rx, double ry) {
		initIfEmpty();
		// Compute position of current point relative to the ellipse centered on
		// (0, 0) : (-rx, -ry, 2*rx, 2*ry)
		double px = rx * Math.cos(start);
		double py = -ry * Math.sin(start);
		// Translate ellipse to match actual current point coordinates
		double xInitEllipse = (currentPoint.getX() - px) - rx;
		double yInitEllipse = (currentPoint.getY() - py) - ry;
		Arc2D arc = new Arc2D.Double(xInitEllipse, yInitEllipse, 2 * rx, 2 * ry, Math.toDegrees(start), Math.toDegrees(extent), Arc2D.OPEN);
		((GeneralPath) shape).append(arc.getPathIterator(null), true);
		currentPoint = arc.getEndPoint();
		repaint();
		return this;
	}

	/**
	 * @return the coordinates most recently added to the end of the path as a
	 *         Point2D object.
	 */
	public Point2D getCurrentPoint() {
		return ((GeneralPath) shape).getCurrentPoint();
	}

	/**
	 * Adds a quadric curve segment to this polyline. The curved segment starts
	 * at the current point and ends (xEnd, yEnd), and uses point (ctrlx1,
	 * ctrly1) as Bezier control point.
	 * 
	 * @param ctrlx1
	 *            The x coordinate of the first Bezier control point
	 * @param ctrly1
	 *            The y coordinate of the first Bezier control point
	 * @param xEnd
	 *            The x coordinate of the end point
	 * @param yEnd
	 *            The y coordinate of the end point
	 * @return this polyLine
	 */
	public CPolyLine quadTo(double ctrlx1, double ctrly1, double xEnd, double yEnd) {
		initIfEmpty();
		((GeneralPath) shape).quadTo((float) ctrlx1, (float) ctrly1, (float) xEnd, (float) yEnd);
		lastPoint.setLocation((float) ctrlx1, (float) ctrly1);
		currentPoint.setLocation((float) xEnd, (float) yEnd);
		repaint();
		return this;
	}

	/**
	 * Adds a quadric curve segment to this polyline. The curved segment starts
	 * at the current point and ends at ptEnd, and uses point ptCtrl as Bezier
	 * control point.
	 * 
	 * @param ptCtrl
	 *            The first Bezier control point
	 * @param ptEnd
	 *            The end point
	 * @return this polyLine
	 */
	public CPolyLine quadTo(Point2D ptCtrl, Point2D ptEnd) {
		return quadTo(ptCtrl.getX(), ptCtrl.getY(), ptEnd.getX(), ptEnd.getY());
	}

	/**
	 * Adds a cubic curve segment to this polyline. The curved segment starts at
	 * the current point and ends (xEnd, yEnd), and uses points (ctrlx1, ctrly1)
	 * and (ctrlx2, ctrly2) as Bezier control points.
	 * 
	 * @param ctrlx1
	 *            The x coordinate of the first Bezier control point
	 * @param ctrly1
	 *            The y coordinate of the first Bezier control point
	 * @param ctrlx2
	 *            The x coordinate of the second Bezier control point
	 * @param ctrly2
	 *            The y coordinate of the second Bezier control point
	 * @param xEnd
	 *            The x coordinate of the ending point
	 * @param yEnd
	 *            The y coordinate of the ending point
	 * @return this polyLine
	 */
	public CPolyLine curveTo(double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double xEnd, double yEnd) {
		initIfEmpty();
		((GeneralPath) shape).curveTo((float) ctrlx1, (float) ctrly1, (float) ctrlx2, (float) ctrly2, (float) xEnd, (float) yEnd);
		lastPoint.setLocation((float) ctrlx2, (float) ctrly2);
		currentPoint.setLocation((float) xEnd, (float) yEnd);
		repaint();
		return this;
	}

	/**
	 * Adds a cubic curve segment to this polyline. The curved segment starts at
	 * the current point and ends at ptEnd, and uses points ptCtrl1 and ptCtrl2
	 * as Bezier control points.
	 * 
	 * @param ptCtrl1
	 *            The first Bezier control point
	 * @param ptCtrl2
	 *            The second Bezier control point
	 * @param ptEnd
	 *            The ending point
	 * @return this polyLine
	 */
	public CPolyLine curveTo(Point2D ptCtrl1, Point2D ptCtrl2, Point2D ptEnd) {
		initIfEmpty();
		return curveTo(ptCtrl1.getX(), ptCtrl1.getY(), ptCtrl2.getX(), ptCtrl2.getY(), ptEnd.getX(), ptEnd.getY());
	}

	/**
	 * Closes this polyLine by drawing a straight line from the current point to
	 * the starting point.
	 * 
	 * @return this polyLine
	 */
	public CPolyLine close() {
		((GeneralPath) shape).closePath();
		repaint();
		return this;
	}

	/**
	 * Removes the last segment of this polyline, if any.
	 * 
	 * @return this polyline
	 */
	public CPolyLine removeLastSegment() {
		PathIterator iter = ((GeneralPath) shape).getPathIterator(null);
		GeneralPath newShape = new GeneralPath();
		float[] coords = new float[6];
		int segType = iter.currentSegment(coords);
		iter.next();

		int nbSegLeft = 0;

		while (!iter.isDone()) {
			nbSegLeft++;
			switch (segType) {
			case PathIterator.SEG_MOVETO:
				newShape.moveTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_LINETO:
				newShape.lineTo(coords[0], coords[1]);
				break;
			case PathIterator.SEG_QUADTO:
				newShape.quadTo(coords[0], coords[1], coords[2], coords[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				newShape.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
				break;
			case PathIterator.SEG_CLOSE:
				newShape.closePath();
				break;
			default:
				System.out.println("unknown segment type: " + segType);
			}

			segType = iter.currentSegment(coords);
			iter.next();
		}

		if (nbSegLeft == 0)
			newShape.moveTo((float) startX, (float) startY);

		shape = newShape;
		repaint();
		return this;
	}

	// ______________________________ COPY _____________________________

	/**
	 * {@inheritDoc}
	 */
	public CShape copyTo(CShape sms) {
		super.copyTo(sms);
		((CPolyLine) sms).startX = startX;
		((CPolyLine) sms).startY = startY;
		return this;
	}

	/**
	 * Creates a new copy of this shape and returns it.
	 * 
	 * @return the copy.
	 */
	public CShape duplicate() {
		CPolyLine sms = new CPolyLine();
		copyTo(sms);
		sms.startX = startX;
		sms.startY = startY;
		sms.lastPoint = new Point2D.Double(lastPoint.getX(), lastPoint.getY());
		sms.currentPoint = new Point2D.Double(currentPoint.getX(), currentPoint.getY());
		return sms;
	}

	/**
	 * @return the x-coordinate of the starting point.
	 */
	public double getStartX() {
		return startX;
	}

	/**
	 * @return the x-coordinate of the starting point.
	 */
	public double getStartY() {
		return startY;
	}

//	public void write(DataOutputStream out) throws IOException {
//		GeneralPath gp = (GeneralPath) shape;
//		// count number of segments
//		int nbSegments = 0;
//		double[] coords = new double[6];
//		for (PathIterator pi = gp.getPathIterator(null); !pi.isDone(); pi.next())
//			nbSegments++;
//		out.writeInt(nbSegments);
//		for (PathIterator pi = gp.getPathIterator(null); !pi.isDone(); pi.next()) {
//			int seg = pi.currentSegment(coords);
//			out.writeInt(seg);
//			for (int i = 0; i < coords.length; i++)
//				out.writeDouble(coords[i]);
//		}
//	}

//	public Object read(DataInputStream in) throws IOException {
//		int nbSegments = 0;
//		double[] coords = new double[6];
//		nbSegments = in.readInt();
//		for (int j = 0; j < nbSegments; j++) {
//			int segType = in.readInt();
//			for (int k = 0; k < 6; k++)
//				coords[k] = in.readDouble();
//			switch (segType) {
//			case PathIterator.SEG_CLOSE: {
//				close();
//				break;
//			}
//			case PathIterator.SEG_MOVETO: {
//				moveTo(coords[0], coords[1]);
//				break;
//			}
//			case PathIterator.SEG_LINETO: {
//				lineTo(coords[0], coords[1]);
//				break;
//			}
//			case PathIterator.SEG_QUADTO: {
//				quadTo(coords[0], coords[1], coords[2], coords[3]);
//				break;
//			}
//			case PathIterator.SEG_CUBICTO: {
//				curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
//				break;
//			}
//			}
//			;
//		}
//		return this;
//	}
}
