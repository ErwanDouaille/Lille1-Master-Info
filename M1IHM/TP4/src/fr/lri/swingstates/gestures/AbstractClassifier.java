/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.gestures;

import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import fr.lri.swingstates.canvas.CPolyLine;

/**
 * The base class for a gesture classifier.
 * 
 * @author Caroline Appert
 */
public abstract class AbstractClassifier {

	protected ArrayList<String>          classesNames = new ArrayList<String>();
	protected ArrayList<Vector<Point2D>> templates = new ArrayList<Vector<Point2D>>();

	protected abstract Object read(DataInputStream in) throws IOException;

	protected abstract void write(DataOutputStream out) throws IOException;

	/**
	 * Removes a gesture example from this classifier.
	 * 
	 * @param gesture
	 *            the gesture to remove
	 */
	public abstract void removeExample(Gesture gesture) throws UnsupportedOperationException;

	/**
	 * Returns a graphical representation for a given class of gestures. The
	 * graphical representation is the one which minimizes the distance with
	 * vector of features characterizing this gesture class.
	 * 
	 * @param className
	 *            the name of the gesture class.
	 * @return A representative polyline for the gesture class having name
	 *         <code>className</code>.
	 */
	public abstract CPolyLine getRepresentative(String className);

	/**
	 * Adds a class of gestures to this classifier.
	 * 
	 * @param className
	 *            The name of the class of gestures to add.
	 * @return the index of this class in the list of classes (-1 if this class already exists and thus has not been added).
	 */
	public int addClass(String className) {
		classesNames.add(className);
		templates.add(null);
		return classesNames.size() - 1;
	}

	/**
	 * Adds a gesture example to this classifier.
	 * 
	 * @param className
	 *            the gesture example's class
	 * @param example
	 *            the gesture example
	 */
	public abstract void addExample(String className, Gesture example) throws UnsupportedOperationException;
	
	/**
	 * Returns the vector of gesture examples for a given class.
	 * @param className The name of the class
	 * @return The set of examples for the class <code>className</code>.
	 * @throws UnsupportedOperationException
	 */
	public abstract Vector<Gesture> getExamples(String className) throws UnsupportedOperationException;

	/**
	 * Removes a class of gestures from this classifier.
	 * 
	 * @param className
	 *            The name of the class of gestures to remove.
	 */
	public void removeClass(String className) {
		int index = classesNames.indexOf(className);
		if (index == -1)
			System.err.println("no class " + className + " in the classifier");
		classesNames.remove(index);
		templates.remove(index);
	}

	/**
	 * Renames a class of gestures.
	 * 
	 * @param previousClassName
	 *            The current name of this class of gestures
	 * @param newClassName
	 *            The new name of this class of gestures
	 */
	public void renameClass(String previousClassName, String newClassName) {
		int index = classesNames.indexOf(previousClassName);
		classesNames.set(index, newClassName);
	}

	/**
	 * Recognizes a gesture.
	 * 
	 * @param g
	 *            The gesture to recognize
	 * @return The name of the class of gestures that best fit to g.
	 */
	public abstract String classify(Gesture g);

	/**
	 * Computes a sorted list of classes contained in this recognizer from the
	 * best match to the the worst match given a gesture.
	 * 
	 * @param g
	 *            The gesture
	 * @return a vector of scores for all the classes registered in this classifier 
	 * 			sorted from the best match (index 0) to the worst match (index n-1),
	 * 			with n the number of classes.
	 * 			A score is a couple (class_name, distance).
	 */
	public abstract Vector<Score> sortedClasses(Gesture g);

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
	 * Resets this classifier (i.e. removes all the classes of gestures).
	 */
	public void reset() {
		classesNames.clear();
		templates.clear();
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
		templates.add(index, template);
	}

	/**
	 * @param className the name of the class of gestures.
	 * @return the template for the class className if it exists, null otherwise.
	 */
	public Vector<Point2D> getTemplate(String className) {
		int index = classesNames.indexOf(className);
		if (index == -1)
			System.err.println("no class " + className + " in the classifier");
		return templates.get(index);
	}
	
	/**
	 * @return The list of gesture class names in this classifier.
	 */
	public ArrayList<String> getClassesNames() {
		return classesNames;
	}

	/**
	 * @return The list of templates in this classifier (in the order corresponding to the classes names order returned by <code>getClassesNames</code>).
	 */
	public ArrayList<Vector<Point2D>> getTemplates() {
		return templates;
	}
}
