/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.gestures.shapeMatching;

import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * A named collection of points, i.e. couple &lt;name, points&gt;. 
 * 
 * @author Caroline Appert
 *
 */
public class NamedGesture {

	private String name;
	private Vector<Point2D> points;
	private Vector<Point2D> templatePoints;

	/**
	 * Builds a NamedGesture
	 * @param name The gesture name
	 * @param points The collection of points in this gesture
	 */
	public NamedGesture(String name, Vector<Point2D> points, Vector<Point2D> templatePoints) {
		this.name = name;
		this.points = points;
		this.templatePoints = templatePoints;
	}

	/**
	 * @return The name of this NamedGesture.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The collection of points in this NamedGesture.
	 */
	public Vector<Point2D> getPoints() {
		return points;
	}

	/**
	 * @return The collection of point of the best matching template.
	 */
	public Vector<Point2D> getTemplatePoints() {
		return templatePoints;
	}

}