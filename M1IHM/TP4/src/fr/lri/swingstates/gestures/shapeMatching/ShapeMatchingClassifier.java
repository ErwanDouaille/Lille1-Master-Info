/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.gestures.shapeMatching;

import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Vector;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.gestures.AbstractClassifier;
import fr.lri.swingstates.gestures.Gesture;
import fr.lri.swingstates.gestures.GestureUtils;
import fr.lri.swingstates.gestures.Score;

/**
 * A very simple recognizer that performs simple shape matching based on a single example per class (one template):
 * <ol>
 * <li> Resample the gesture to classify so it contains the number of uniformly spaced points as the gesture templates contained in this classifier. </li>
 * <li> For each template, scale the input gesture so its bounding box matches the bounding box of the template
 * and compute the sum of distances point to point between the template points and the input gesture points. </li>
 * <li> Returns the name of the class for the template that minimizes this sum of distances. </li>
 * </ol>
 * 
 * @author Caroline Appert
 *
 */
public class ShapeMatchingClassifier extends AbstractClassifier {
	
	private double theta = Math.PI / 8;
	private double deltaTheta = Math.PI / 90;

	private int nbPoints = 100;

	private double currentDistance = -1;
	private double maximumDistance = 30;
	private double sizeScaleToSquare = 100;
	protected int  minimumStrokeLength = 20;

	/**
	 * {@inheritDoc}
	 */
	public String classify(Gesture g) {
		if(GestureUtils.pathLength(g.getPoints()) < minimumStrokeLength) return null;
		
		double minScore = Double.MAX_VALUE;
		double currentScore;
		
		Vector<Point2D> inputPointsResampled = new Vector<Point2D>();
		GestureUtils.resample(g.getPoints(), nbPoints, inputPointsResampled);
		GestureUtils.scaleToSquare(inputPointsResampled, sizeScaleToSquare, inputPointsResampled);
		GestureUtils.translateToOrigin(inputPointsResampled, inputPointsResampled);

		int match = 0;
		int cpt = 0;
		for (Iterator<Vector<Point2D>> templatesIterator = templates.iterator(); templatesIterator.hasNext();) {
			Vector<Point2D> next = templatesIterator.next();
			currentScore = GestureUtils.pathDistance(inputPointsResampled, next);
			if (currentScore < minScore) {
				minScore = currentScore;
				match = cpt;
			}
			cpt++;
		}
		currentDistance = minScore;
		if (currentDistance > maximumDistance)
			return null;
		return classesNames.get(match);
	}
	
	/**
	 * Classifies a gesture and return the collection of resampled points for the input gesture.
	 * @param g The input gesture.
	 * @return a NamedGesture that contains the name of the recognized class and the set of resampled points.
	 */
	public NamedGesture classifyAndResample(Gesture g) {
		if(GestureUtils.pathLength(g.getPoints()) < minimumStrokeLength) return null;
		
		double minScore = Double.MAX_VALUE;
		double currentScore;
		
		Vector<Point2D> inputPointsResampled = new Vector<Point2D>();
		GestureUtils.resample(g.getPoints(), nbPoints, inputPointsResampled);
		GestureUtils.scaleToSquare(inputPointsResampled, sizeScaleToSquare, inputPointsResampled);
		Vector<Point2D> inputPointsResampledCopy = new Vector<Point2D>();
		for (Iterator<Point2D> iterator = inputPointsResampled.iterator(); iterator.hasNext(); )
			inputPointsResampledCopy.add((Point2D)iterator.next().clone());
		GestureUtils.translateToOrigin(inputPointsResampled, inputPointsResampled);

		int match = 0;
		int cpt = 0;
		Vector<Point2D> bestTemplate = null;
		for (Iterator<Vector<Point2D>> templatesIterator = templates.iterator(); templatesIterator.hasNext();) {
			Vector<Point2D> next = templatesIterator.next();
			currentScore = GestureUtils.pathDistance(inputPointsResampled, next);
			if (currentScore < minScore) {
				minScore = currentScore;
				match = cpt;
				bestTemplate = next;
			}
			cpt++;
		}
		
		
		
		currentDistance = minScore;
		if (currentDistance > maximumDistance) {
			return null;
		}
		Vector<Point2D> bestTemplateCopy = new Vector<Point2D>();
		for (Iterator<Point2D> iterator = bestTemplate.iterator(); iterator.hasNext();) {
			Point2D next = iterator.next();
			bestTemplateCopy.add(new Point2D.Double(next.getX(), next.getY()));
		}
		return new NamedGesture(classesNames.get(match), inputPointsResampledCopy, bestTemplateCopy);
	}

	/**
	 * Builds a new classifier by loading its definition in a file.
	 * 
	 * @param file
	 *            The name of the file containing the definition of the
	 *            classifier.
	 * @return The newly created classifier.
	 */
	public static ShapeMatchingClassifier newClassifier(String file) {
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
	public static ShapeMatchingClassifier newClassifier(File filename) {
		ShapeMatchingClassifier c = new ShapeMatchingClassifier();
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
	public static ShapeMatchingClassifier newClassifier(URL url) {
		ShapeMatchingClassifier c = new ShapeMatchingClassifier();
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
	 * Adds a class and set the template for this class.
	 * @param className The name of the class to add
	 * @param template The template for the class <code>className</code>
	 */
	public void addClass(String className, Vector<Point2D> template) {
		int index = addClass(className);
		Vector<Point2D> newPoints = new Vector<Point2D>();
		GestureUtils.resample(template, getNbPoints(), newPoints);
		GestureUtils.scaleToSquare(newPoints, getSizeScaleToSquare(), newPoints);
		GestureUtils.translateToOrigin(newPoints, newPoints);
		templates.set(index, newPoints);
	}

	protected Object read(DataInputStream in) throws IOException {
		int nClasses = in.readInt();
		for (int i = 0; i < nClasses; i++) {
			classesNames.add(in.readUTF());
			int nbPoints = in.readInt();
			Vector<Point2D> points = new Vector<Point2D>();
			for (int j = 0; j < nbPoints; j++) {
				points.add(new Point2D.Double(in.readDouble(), in.readDouble()));
			}
			templates.add(points);
		}
		try {
			maximumDistance = in.readDouble();
			minimumStrokeLength = in.readInt();
		} catch(IOException ioe) {
			maximumDistance = 30;
			minimumStrokeLength = 20;
		}
		return this;
	}

	protected void write(DataOutputStream out) throws IOException {
		out.writeInt(classesNames.size());
		for (int i = 0; i < classesNames.size(); i++) {
			out.writeUTF(classesNames.get(i));
			out.writeInt(templates.get(i).size());
			for (Iterator<Point2D> iterator = templates.get(i).iterator(); iterator.hasNext();) {
				Point2D next = iterator.next();
				out.writeDouble(next.getX());
				out.writeDouble(next.getY());
			}
		}
		out.writeDouble(maximumDistance);
		out.writeInt(minimumStrokeLength);
	}

	/**
	 * {@inheritDoc}
	 */
	public Vector<Score> sortedClasses(Gesture g) {
		Vector<Score> sortedScores = new Vector<Score>();

		Vector<Point2D> inputPointsResampled = new Vector<Point2D>();
		GestureUtils.resample(g.getPoints(), nbPoints, inputPointsResampled);
		GestureUtils.scaleToSquare(inputPointsResampled, sizeScaleToSquare, inputPointsResampled);
		GestureUtils.translateToOrigin(inputPointsResampled, inputPointsResampled);

		double score;
		double minClassScore = 0;
		for (int nc = 0; nc < classesNames.size(); nc++) {
			minClassScore = Integer.MAX_VALUE;
			Vector<Point2D> gesturePoints = templates.get(nc);
			score = GestureUtils.pathDistance(inputPointsResampled, gesturePoints);
			if (score < minClassScore)
				minClassScore = score;
			if (nc == 0) {
				sortedScores.add(new Score(classesNames.get(nc), minClassScore));
			} else {
				int i = 0;
				while (i < sortedScores.size() && sortedScores.get(i).getScore() < minClassScore)
					i++;
				sortedScores.add(i, new Score(classesNames.get(nc), minClassScore));
			}
		}

		return sortedScores;
	}

	public int getNbPoints() {
		return nbPoints;
	}

	public double getSizeScaleToSquare() {
		return sizeScaleToSquare;
	}

	/**
	 * Saves the definition of this classifier in a file.
	 * 
	 * @param filename
	 *            The name of the file where to write the definition of the
	 *            classifier.
	 */
	public void save(File filename) {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(filename));
			write(dos);
			dos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public CPolyLine getRepresentative(String className) {
		Vector<Point2D> template = getTemplate(className);
		CPolyLine polyline = new CPolyLine(template.firstElement());
		Iterator<Point2D> iterator = template.iterator();
		iterator.next();
		while(iterator.hasNext())
			polyline.lineTo(iterator.next());
		return polyline;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeExample(Gesture gesture)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Removing an example from a ShapeMatchingClassifier does not make sense.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void addExample(String className, Gesture example)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Adding an example to a class in a ShapeMatchingClassifier does not make sense.");
	}

	/**
	 * {@inheritDoc}
	 */
	public Vector<Gesture> getExamples(String className)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("A class in a ShapeMatchingClassifier contains only one template which can be obtained using the method getTemplate(String).");
	}
	
	/**
	 * The side size of the bounding box to which the gesture is scaled after having being resampled.
	 * @param size The side size of the bounding box
	 */
	public void setSizeScaleToSquare(int size) {
		this.sizeScaleToSquare = size;
	}

	/**
	 * Sets the template gesture for a given existing class of gestures in this classifier.
	 * @param className the name of the class of gestures.
	 * @param template the template for the class className.
	 */
	public void setTemplate(String className, Vector<Point2D> template) {
		int index = classesNames.indexOf(className);
		if(index == -1) return;
		templates.remove(index);
		Vector<Point2D> newPoints = new Vector<Point2D>();
		GestureUtils.resample(template, getNbPoints(), newPoints);
		GestureUtils.scaleToSquare(newPoints, getSizeScaleToSquare(), newPoints);
		GestureUtils.translateToOrigin(newPoints, newPoints);
		templates.add(index, newPoints);
	}

	public double getThreshold() {
		return maximumDistance;
	}
	
	public void setThreshold(double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}
	
	public int getMinimumStrokeLength() {
		return minimumStrokeLength;
	}

	public void setMinimumStrokeLength(int minimumStrokeLength) {
		this.minimumStrokeLength = minimumStrokeLength;
	}
	
}
