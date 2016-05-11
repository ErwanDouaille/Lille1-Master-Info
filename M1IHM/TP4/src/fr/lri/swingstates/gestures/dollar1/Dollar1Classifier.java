/*
 * Authors: Caroline Appert (caroline.appert@lri.fr) Copyright (c) Universite
 * Paris-Sud XI, 2007. All Rights Reserved Licensed under the GNU LGPL. For full
 * terms see the file COPYING.
 */
package fr.lri.swingstates.gestures.dollar1;

/*******************************************************************************
 * The algorithm and original C# code are from:
 * http://faculty.washington.edu/wobbrock/proj/dollar/ The algorithm is
 * described in: Wobbrock, J.O., Wilson, A.D., and Li, Y. 2007. Gestures without
 * libraries, toolkits or training: a $1 recognizer for user interface
 * prototypes. In proc.UIST'07.
 ******************************************************************************/

import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.gestures.AbstractClassifier;
import fr.lri.swingstates.gestures.Gesture;
import fr.lri.swingstates.gestures.GestureClass;
import fr.lri.swingstates.gestures.GestureUtils;
import fr.lri.swingstates.gestures.Score;

/**
 * A classifier that implements $1 algorithm to classify gestures.
 * 
 * @author Caroline Appert
 * 
 */
public class Dollar1Classifier extends AbstractClassifier {

	class Dollar1GestureClass extends GestureClass {

		private Vector<Vector<Point2D>> resampledGestures = new Vector<Vector<Point2D>>();

		Dollar1GestureClass() {
			super();
		}

		Dollar1GestureClass(String n) {
			super(n);
		}

		/**
		 * {@inheritDoc} Each time a gesture is added, a vector of points
		 * corresponding to this gesture as resampled, rotated and scaled is
		 * computed and stored in <code>resampledGestures</code>.
		 */
		public void addExample(Gesture gesture) {
			super.addExample(gesture);
			Vector<Point2D> newPoints = new Vector<Point2D>();
			GestureUtils.resample(gesture.getPoints(), Dollar1Classifier.this.getNbPoints(), newPoints);
			GestureUtils.rotateToZero(newPoints, newPoints);
			GestureUtils.scaleToSquare(newPoints, Dollar1Classifier.this.getSizeScaleToSquare(), newPoints);
			GestureUtils.translateToOrigin(newPoints, newPoints);
			resampledGestures.add(newPoints);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean removeExample(Gesture gesture) {
			if (!gestures.contains(gesture))
				return false;
			int index = gestures.indexOf(gesture);
			if (index != -1)
				resampledGestures.remove(index);
			return super.removeExample(gesture);
		}

		/**
		 * @return The vector of gesture examples as resampled, rotated and scaled.
		 * @see Dollar1GestureClass#addExample(Gesture)
		 */
		public Vector<Vector<Point2D>> getResampledGestures() {
			return resampledGestures;
		}

		/**
		 * @return The average vector of this class. A point#i in this vector is the
		 *         gravity center of points#i of all examples.
		 */
		public Vector<Point2D> getAverage() {
			int nbPoints = Dollar1Classifier.this.getNbPoints();
			Vector<Point2D> average = new Vector<Point2D>(nbPoints);
			double sumX, sumY;
			for (int i = 0; i < nbPoints; i++) {
				sumX = 0;
				sumY = 0;
				for (Iterator<Vector<Point2D>> iterator = resampledGestures.iterator(); iterator.hasNext();) {
					Point2D pt = iterator.next().get(i);
					sumX += pt.getX();
					sumY += pt.getY();
				}
				average.add(new Point2D.Double(sumX / resampledGestures.size(), sumY / resampledGestures.size()));
			}
			return average;
		}
		
	}
	
	protected ArrayList<Dollar1GestureClass>    classes = new ArrayList<Dollar1GestureClass>();

	private double theta = Math.PI / 4;
	private double deltaTheta = Math.PI / 90;

	private double currentDistance = -1;
	private double maximumDistance = 30;
	private double sizeScaleToSquare = 100;

	private int nbPoints = 64;

	/**
	 * {@inheritDoc}
	 */
	public String classify(Gesture g) {
		double minScore = Double.MAX_VALUE;
		double currentScore;
		GestureClass recognized = null;

		Vector<Point2D> inputPointsResampled = new Vector<Point2D>();
		GestureUtils.resample(g.getPoints(), nbPoints, inputPointsResampled);
		GestureUtils.rotateToZero(inputPointsResampled, inputPointsResampled);
		GestureUtils.scaleToSquare(inputPointsResampled, sizeScaleToSquare, inputPointsResampled);
		GestureUtils.translateToOrigin(inputPointsResampled, inputPointsResampled);

		for (Iterator<Dollar1GestureClass> classesIterator = classes.iterator(); classesIterator.hasNext();) {
			Dollar1GestureClass nextClass = classesIterator.next();
			for (Iterator<Vector<Point2D>> gesturesIterator = nextClass.getResampledGestures().iterator(); gesturesIterator.hasNext();) {
				Vector<Point2D> gesturePoints = gesturesIterator.next();
				currentScore = GestureUtils.distanceAtBestAngle(inputPointsResampled, gesturePoints, -theta, theta, deltaTheta);
				if (currentScore < minScore) {
					minScore = currentScore;
					recognized = nextClass;
				}
			}
		}
		currentDistance = minScore;
		if (currentDistance > maximumDistance)
			return null;
		return recognized.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public CPolyLine getRepresentative(String className) {
		int i = 0;
		Dollar1GestureClass gestureClass = null;
		for (; i < classes.size(); i++) {
			gestureClass = classes.get(i);
			if (className.compareTo(gestureClass.getName()) == 0)
				break;
		}

		Vector<Point2D> average = gestureClass.getAverage();
		CPolyLine representative = null;
		Vector<Point2D> next = null;
		double minValue = Double.MAX_VALUE;
		for (Iterator<Vector<Point2D>> gesturesIterator = gestureClass.getResampledGestures().iterator(); gesturesIterator.hasNext();) {
			next = gesturesIterator.next();
			double value = GestureUtils.distanceAtBestAngle(next, average, -theta, theta, deltaTheta);
			if (value < minValue) {
				minValue = value;
				representative = GestureUtils.asPolyLine(next);
			}
		}

		if (representative != null)
			representative.setFilled(false);
		return representative;
	}

	/**
	 * Builds a new classifier by loading its definition in a file.
	 * 
	 * @param file
	 *            The name of the file containing the definition of the
	 *            classifier.
	 * @return The newly created classifier.
	 */
	public static Dollar1Classifier newClassifier(String file) {
		return newClassifier(new File(file));
	}

	/**
	 * Builds a new classifier by loading its definition in a file.
	 * 
	 * @param filename
	 *            The name of the file containing the definition of the
	 *            classifier.
	 * @return The newly created classifier.
	 */
	public static Dollar1Classifier newClassifier(File filename) {
		Dollar1Classifier c = new Dollar1Classifier();
		try {
			c.read(new DataInputStream(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * Builds a new classifier by loading its definition in a url.
	 * 
	 * @param url
	 *            The url containing the definition of the classifier.
	 * @return The newly created classifier.
	 */
	public static Dollar1Classifier newClassifier(URL url) {
		Dollar1Classifier c = new Dollar1Classifier();
		try {
			URLConnection urlc = null;
			urlc = url.openConnection();
			c.read(new DataInputStream(urlc.getInputStream()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * {@inheritDoc}
	 */
	public int addClass(String className) {
		int index = super.addClass(className);
		if(index == -1) return -1;
		Dollar1GestureClass gcr = new Dollar1GestureClass(className);
		classes.add(gcr);
		return index;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeClass(String className) {
		int index = classesNames.indexOf(className);
		if(index == -1) return;
		super.removeClass(className);
		classes.remove(index);
	}

	/**
	 * @return The number of points used for resampling a gesture during $1
	 *         recognition process.
	 */
	public int getNbPoints() {
		return nbPoints;
	}

	/**
	 * @return The size of the bounding box side used for rescaling a gesture
	 *         during $1 recognition process.
	 */
	public double getSizeScaleToSquare() {
		return sizeScaleToSquare;
	}

	protected void write(DataOutputStream out) throws IOException {
		out.writeInt(classes.size());
		for (int i = 0; i < classes.size(); i++)
			classes.get(i).write(out);
	}
	
	protected Object read(DataInputStream in) throws IOException {
		int nClasses = in.readInt();
		for (int i = 0; i < nClasses; i++) {
			Dollar1GestureClass c = new Dollar1GestureClass();
			c.read(in);
			classes.add(c);
			classesNames.add(c.getName());
			templates.add(null);
		}
		return this;
	}

	/**
	 * @return The maximum score threshold for recognition.
	 */
	public double getMaximumDistance() {
		return maximumDistance;
	}

	/**
	 * Sets a minimum score threshold for recognition. If the distance is
	 * greater than this maximum distance, the gesture is not recognized (i.e.
	 * method <code>classify</code> returns null.
	 * 
	 * @param maximumDistance
	 *            The minimum score threshold for recognition.
	 */
	public void setMaximumDistance(double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

	/**
	 * @return The distance of the last recognized gesture.
	 */
	public double getCurrentDistance() {
		return currentDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	public Vector<Score> sortedClasses(Gesture g) {
		Vector<Score> sortedClasses = new Vector<Score>();

		Vector<Point2D> inputPointsResampled = new Vector<Point2D>();
		GestureUtils.resample(g.getPoints(), nbPoints, inputPointsResampled);
		GestureUtils.rotateToZero(inputPointsResampled, inputPointsResampled);
		GestureUtils.scaleToSquare(inputPointsResampled, sizeScaleToSquare, inputPointsResampled);
		GestureUtils.translateToOrigin(inputPointsResampled, inputPointsResampled);

		double score;
		double minClassScore = 0;
		for (int nc = 0; nc < classes.size(); nc++) {
			minClassScore = Integer.MAX_VALUE;
			for (Iterator<Vector<Point2D>> gesturesIterator = classes.get(nc).getResampledGestures().iterator(); gesturesIterator.hasNext();) {
				Vector<Point2D> gesturePoints = gesturesIterator.next();
				score = GestureUtils.distanceAtBestAngle(inputPointsResampled, gesturePoints, -theta, theta, deltaTheta);
				if (score < minClassScore)
					minClassScore = score;
			}
			if (nc == 0) {
				sortedClasses.add(new Score(classes.get(nc).getName(), minClassScore));
			} else {
				int i = 0;
				while (i < sortedClasses.size() && sortedClasses.get(i).getScore() < minClassScore)
					i++;
				sortedClasses.add(i, new Score(classes.get(nc).getName(), minClassScore));
			}
		}

		return sortedClasses;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeExample(Gesture gesture) {
		for (Iterator<Dollar1GestureClass> iterator = classes.iterator(); iterator.hasNext();) {
			Dollar1GestureClass next = iterator.next();
			if(next != null) next.removeExample(gesture);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addExample(String className, Gesture example) {
		int index = classesNames.indexOf(className);
		if(index == -1) return;
		Dollar1GestureClass gestureClass = classes.get(index);
		if(gestureClass != null) gestureClass.addExample(example);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void renameClass(String previousClassName, String newClassName) {
		int index = classesNames.indexOf(previousClassName);
		if(index == -1) return;
		Dollar1GestureClass gc = classes.get(index);
		gc.setName(newClassName);
		super.renameClass(previousClassName, newClassName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		super.reset();
		classes.clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Vector<Gesture> getExamples(String className)
			throws UnsupportedOperationException {
		int index = classesNames.indexOf(className);
		if(index == -1) return null;
		Dollar1GestureClass gc = classes.get(index);
		return gc.getGestures();
	}
}
