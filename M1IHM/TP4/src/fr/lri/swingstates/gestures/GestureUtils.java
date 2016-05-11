/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.gestures;

/*******************************************************************************
 * The algorithm and original C# code are from:
 * http://faculty.washington.edu/wobbrock/proj/dollar/ The algorithm is
 * described in: Wobbrock, J.O., Wilson, A.D., and Li, Y. 2007. Gestures without
 * libraries, toolkits or training: a $1 recognizer for user interface
 * prototypes. In proc.UIST'07.
 ******************************************************************************/

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Vector;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.Canvas;

/**
 * A class containing utility methods for sets of points.
 * 
 * @author Caroline Appert
 *
 */
public class GestureUtils {

	public static double pathLength(Vector<Point2D> points) {
		double d = 0;
		for (int i = 1; i < points.size(); i++) {
			d += points.get(i - 1).distance(points.get(i));
		}
		return d;
	}

	public static void resample(Vector<Point2D> points, int n, Vector<Point2D> newPoints) {
		if (points.isEmpty())
			return;
		Vector<Point2D> dstPts = new Vector<Point2D>(n);

		double segLength = pathLength(points) / (n - 1);
		double currentSegLength = 0;
		Vector<Point2D> srcPts = new Vector<Point2D>(points);
		dstPts.add((Point2D) srcPts.get(0).clone());
		for (int i = 1; i < srcPts.size(); i++) {
			Point2D pt1 = srcPts.get(i - 1);
			Point2D pt2 = srcPts.get(i);
			double d = pt1.distance(pt2);
			if ((currentSegLength + d) >= segLength) {
				double qx = pt1.getX() + ((segLength - currentSegLength) / d) * (pt2.getX() - pt1.getX());
				double qy = pt1.getY() + ((segLength - currentSegLength) / d) * (pt2.getY() - pt1.getY());
				Point2D q = new Point2D.Double(qx, qy);
				dstPts.add(q); // append new point 'q'
				srcPts.add(i, q); // insert 'q' at position i in points s.t.
				// 'q' will be the next i
				currentSegLength = 0.0;
			} else {
				currentSegLength += d;
			}
		}
		// sometimes we fall a rounding-error short of adding the last point, so
		// add it if so
		if (dstPts.size() == (n - 1)) {
			dstPts.add((Point2D) srcPts.get(srcPts.size() - 1).clone());
		}
		newPoints.clear();
		newPoints.addAll(dstPts);
	}

	public static Point2D centroid(Vector<Point2D> points) {
		double sumX = 0;
		double sumY = 0;
		for (Iterator<Point2D> iterator = points.iterator(); iterator.hasNext();) {
			Point2D next = iterator.next();
			sumX += next.getX();
			sumY += next.getY();
		}
		int length = points.size();
		return new Point2D.Double(sumX / length, sumY / length);
	}

	public static void rotateToZero(Vector<Point2D> points, Vector<Point2D> newPoints) {
		Point2D c = centroid(points);
		double theta = Math.atan2(c.getY() - points.get(0).getY(), c.getX() - points.get(0).getX());
		rotateBy(points, -theta, newPoints);
	}

	/**
	 * @param points
	 *            the points to rotate
	 * @param theta
	 *            the angle in radians
	 * @param newPoints
	 *            the points where to store rotated points
	 */
	public static void rotateBy(Vector<Point2D> points, double theta, Vector<Point2D> newPoints) {
		Point2D c = centroid(points);
		Point2D ptSrc, ptDest;
		for (int i = 0; i < points.size(); i++) {
			ptSrc = points.get(i);
			if (newPoints.size() > i) {
				ptDest = newPoints.get(i);
			} else {
				ptDest = new Point2D.Double();
				newPoints.add(i, ptDest);
			}
			ptDest.setLocation((ptSrc.getX() - c.getX()) * Math.cos(theta) - (ptSrc.getY() - c.getY()) * Math.sin(theta) + c.getX(), (ptSrc.getX() - c.getX()) * Math.sin(theta)
					+ (ptSrc.getY() - c.getY()) * Math.cos(theta) + c.getY());
		}
	}

	public static Rectangle2D boundingBox(Vector<Point2D> points) {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		Point2D next;
		for (Iterator<Point2D> iterator = points.iterator(); iterator.hasNext();) {
			next = iterator.next();
			minX = Math.min(minX, next.getX());
			maxX = Math.max(maxX, next.getX());
			minY = Math.min(minY, next.getY());
			maxY = Math.max(maxY, next.getY());
		}
		double w = maxX - minX;
		if (w < 1)
			w = 1;
		double h = maxY - minY;
		if (h < 1)
			h = 1;
		return new Rectangle2D.Double(minX, minY, w, h);
	}

	public static void scaleToSquare(Vector<Point2D> points, double size, Vector<Point2D> newPoints) {
		Rectangle2D bb = boundingBox(points);
		double maxSide = Math.max(bb.getWidth(), bb.getHeight());
		Point2D ptSrc, ptDest;
		for (int i = 0; i < points.size(); i++) {
			ptSrc = points.get(i);
			if (newPoints.size() > i) {
				ptDest = newPoints.get(i);
			} else {
				ptDest = new Point2D.Double();
				newPoints.add(i, ptDest);
			}
			ptDest.setLocation(ptSrc.getX() * (size / maxSide), ptSrc.getY() * (size / maxSide));
		}
	}

	public static void translateToOrigin(Vector<Point2D> points, Vector<Point2D> newPoints) {
		Point2D c = centroid(points);
		Point2D ptSrc, ptDest;
		Iterator<Point2D> iteratorNewPoints = newPoints.iterator();
		for (Iterator<Point2D> iterator = points.iterator(); iterator.hasNext();) {
			ptSrc = iterator.next();
			if (iteratorNewPoints.hasNext())
				ptDest = iteratorNewPoints.next();
			else {
				ptDest = new Point2D.Double();
				newPoints.add(ptDest);
			}
			ptDest.setLocation(ptSrc.getX() - c.getX(), ptSrc.getY() - c.getY());
		}
	}

	public static double distanceAtBestAngle(Vector<Point2D> points, Vector<Point2D> gesturePoints, double thetaA, double thetaB, double deltaTheta) {
		double thetaa = thetaA;
		double thetab = thetaB;
		double phi = 0.5 * (-1 + Math.sqrt(5));
		Vector<Point2D> newPoints = new Vector<Point2D>();
		double x1 = phi * thetaa + (1 - phi) * thetab;
		double f1 = distanceAtAngle(points, gesturePoints, x1, newPoints);
		double x2 = (1 - phi) * thetaa + phi * thetab;
		double f2 = distanceAtAngle(points, gesturePoints, x2, newPoints);
		while (Math.abs(thetab - thetaa) > deltaTheta) {
			if (f1 < f2) {
				thetab = x2;
				x2 = x1;
				f2 = f1;
				x1 = phi * thetaa + (1 - phi) * thetab;
				f1 = distanceAtAngle(points, gesturePoints, x1, newPoints);
			} else {
				thetaa = x1;
				x1 = x2;
				f1 = f2;
				x2 = (1 - phi) * thetaa + phi * thetab;
				f2 = distanceAtAngle(points, gesturePoints, x2, newPoints);
			}
		}
		return Math.min(f1, f2);
	}

	private static double distanceAtAngle(Vector<Point2D> points, Vector<Point2D> gesturePoints, double theta, Vector<Point2D> newPoints) {
		rotateBy(points, theta, newPoints);
		double res = pathDistance(newPoints, gesturePoints);
		return res;
	}

	public static double pathDistance(Vector<Point2D> pointsA, Vector<Point2D> pointsB) {
		double d = 0;
		Iterator<Point2D> iteratorB = pointsB.iterator();
		Point2D ptA, ptB;
		for (Iterator<Point2D> iteratorA = pointsA.iterator(); iteratorA.hasNext();) {
			ptA = iteratorA.next();
			ptB = iteratorB.next();
			d += ptA.distance(ptB);
		}
		return d / pointsA.size();
	}

	/**
	 * @param points
	 *            The vector of points.
	 * @return a graphical representation of a vector of points as a
	 *         <code>CPolyLine</code>.
	 */
	public static CPolyLine asPolyLine(Vector<Point2D> points) {
		CPolyLine polyline = new CPolyLine();
		boolean first = true;
		for (Iterator<Point2D> iterator = points.iterator(); iterator.hasNext();) {
			Point2D pt = iterator.next();
			if (first) {
				polyline.moveTo(pt);
				first = false;
			} else {
				polyline.lineTo(pt);
			}
		}
		return polyline;
	}
	
	/**
	 * Displays a polyline of a gesture example in a bounding box in a
	 * SwingStates <code>Canvas</code>. The gesture is displayed with a red
	 * circle at its start point and an orange arrow head at its end point.
	 * 
	 * @param canvas
	 *            The SwingStates <code>Canvas</code>.
	 * @param polyline
	 *            The polyline of gesture example.
	 * @param x
	 *            The x-coordinate of the bounding box upper left corner in
	 *            canvas coordinate system.
	 * @param y
	 *            The y-coordinate of the bounding box upper left corner in
	 *            canvas coordinate system.
	 * @param sizeBoundingBox
	 *            The size of the bounding box side of this gesture example.
	 * @param sizeSpan
	 *            The blank space between bounding box and this gesture.
	 * @param sizeStartPoint
	 *            The size of the red starting circle.
	 * 
	 * @return the polyline for this gesture example that has been added to the
	 *         canvas.
	 */
	public static CShape showArrowPreview(Canvas canvas, CPolyLine polyline, int x, int y, int sizeBoundingBox, int sizeSpan, double sizeStartPoint) {
		double maxSide = Math.max(polyline.getHeight(), polyline.getWidth());
		double dscale = (sizeBoundingBox - 2 * sizeSpan) / maxSide;
		CRectangle gestureBB = canvas.newRectangle(x, y, sizeBoundingBox, sizeBoundingBox);
		CPolyLine exampleView = (CPolyLine) polyline.duplicate().setFilled(false);

		Canvas initialCanvas = exampleView.getCanvas();
		if (initialCanvas != null)
			initialCanvas.removeShape(exampleView);
		canvas.addShape(exampleView);
		exampleView.scaleBy(dscale).setReferencePoint(0.5, 0.5).translateBy(x + sizeBoundingBox / 2 - exampleView.getCenterX(), y + sizeBoundingBox / 2 - exampleView.getCenterY());

		gestureBB.setFillPaint(new Color(250, 240, 230));

		exampleView.fixReferenceShapeToCurrent().setPickable(false);

		gestureBB.fixReferenceShapeToCurrent().setOutlined(false);
		CEllipse startPoint = canvas.newEllipse(exampleView.getStartX() - sizeStartPoint / 2, exampleView.getStartY() - sizeStartPoint / 2, sizeStartPoint, sizeStartPoint);
		startPoint.setFillPaint(Color.ORANGE).setOutlinePaint(Color.ORANGE).setPickable(false);

		CPolyLine arrow = exampleView.getArrow(Math.PI / 4, 4);
		arrow.setOutlinePaint(Color.RED);
		canvas.addShape(arrow);
		arrow.setParent(exampleView);

		gestureBB.addChild(exampleView);
		exampleView.addChild(startPoint);

		return gestureBB;
	}
	
	/**
	 * Displays a polyline of a gesture example in a bounding box in a
	 * SwingStates <code>Canvas</code>. The gesture is displayed with a red
	 * circle at its start point.
	 * 
	 * @param canvas
	 *            The SwingStates <code>Canvas</code>.
	 * @param polyline
	 *            The polyline of gesture example.
	 * @param x
	 *            The x-coordinate of the bounding box upper left corner in
	 *            canvas coordinate system.
	 * @param y
	 *            The y-coordinate of the bounding box upper left corner in
	 *            canvas coordinate system.
	 * @param sizeBoundingBox
	 *            The size of the bounding box side of this gesture example.
	 * @param sizeSpan
	 *            The blank space between bounding box and this gesture.
	 * @param sizeStartPoint
	 *            The size of the red starting circle.
	 * 
	 * @return the polyline for this gesture example that has been added to the
	 *         canvas.
	 */
	public static CShape showPreview(Canvas canvas, CPolyLine polyline, int x, int y, int sizeBoundingBox, int sizeSpan, double sizeStartPoint) {
		double maxSide = Math.max(polyline.getHeight(), polyline.getWidth());
		double dscale = (sizeBoundingBox - 2 * sizeSpan) / maxSide;
		CRectangle gestureBB = canvas.newRectangle(x, y, sizeBoundingBox, sizeBoundingBox);
		CPolyLine exampleView = (CPolyLine) polyline.duplicate().setFilled(false);

		Canvas initialCanvas = exampleView.getCanvas();
		if (initialCanvas != null)
			initialCanvas.removeShape(exampleView);
		canvas.addShape(exampleView);
		exampleView.scaleBy(dscale).setReferencePoint(0.5, 0.5).translateBy(x + sizeBoundingBox / 2 - exampleView.getCenterX(), y + sizeBoundingBox / 2 - exampleView.getCenterY());

		gestureBB.setFillPaint(new Color(250, 240, 230));

		exampleView.fixReferenceShapeToCurrent().setPickable(false).setAntialiased(true);
		gestureBB.fixReferenceShapeToCurrent();
		CEllipse startPoint = canvas.newEllipse(exampleView.getStartX() - sizeStartPoint / 2, exampleView.getStartY() - sizeStartPoint / 2, sizeStartPoint, sizeStartPoint);
		startPoint.setFillPaint(Color.RED).setOutlinePaint(Color.RED).setPickable(false).setAntialiased(true);

		gestureBB.addChild(exampleView);
		exampleView.addChild(startPoint);
		
		return gestureBB;
	}

}
