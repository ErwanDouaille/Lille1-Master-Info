/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.gestures;

/*******************************************************************************
 * The algorithm and original C code are: (C) Copyright, 1990 by Dean Rubine,
 * Carnegie Mellon University Permission to use this code for noncommercial
 * purposes is hereby granted. Permission to copy and distribute this code is
 * hereby granted provided this copyright notice is retained. All other rights
 * reserved.
 ******************************************************************************/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
 * A basic class of gestures.
 * 
 * @author Caroline Appert
 * 
 */
public class GestureClass {

	protected Vector<Gesture> gestures;
	protected String name;

	/**
	 * Builds a new labeled class of gestures.
	 */
	protected GestureClass() {
		gestures = new Vector<Gesture>();
	}

	/**
	 * Builds a new labeled class of gestures.
	 * 
	 * @param n
	 *            The label of the class of gestures.
	 */
	public GestureClass(String n) {
		this();
		name = n;
	}

	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(name);
		out.writeInt(gestures.size());
		for (Iterator<Gesture> iterator = gestures.iterator(); iterator.hasNext();) {
			iterator.next().write(out);
		}
	}

	/**
	 * Reads the definition of this gesture class from an input stream.
	 * @param in The input stream
	 * @return This gesture class initialized from input stream <code>in</code>.
	 * @throws IOException if a reading error occurs.
	 */
	public Object read(DataInputStream in) throws IOException {
		name = in.readUTF();
		int nExamples = in.readInt();
		gestures = new Vector<Gesture>();
		for (int i = 0; i < nExamples; i++) {
			Gesture g = new Gesture();
			g.read(in);
			addExample(g);
		}
		return this;
	}

	/**
	 * Returns the name of this class of gestures.
	 * 
	 * @return the name of this class of gestures.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Removes an example of this class of gestures.
	 * 
	 * @param gesture
	 *            The gesture to remove from this gesture class.
	 * @return True if this gesture has been found in this class and removed, false if the gesture has not been found in this class.
	 */
	public boolean removeExample(Gesture gesture) {
		return gestures.remove(gesture);
	}

	/**
	 * Adds an example of this class of gestures.
	 * 
	 * @param gesture
	 *            The gesture to add to this gesture class.
	 */
	public void addExample(Gesture gesture) {
		gestures.add(gesture);
	}

	/**
	 * Sets the name of this gesture class.
	 * 
	 * @param name
	 *            the name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number of examples contained in this gesture class.
	 */
	public int getNumberOfExamples() {
		return gestures.size();
	}

	/**
	 * @return the set of gesture examples contained in this gesture class.
	 */
	public Vector<Gesture> getGestures() {
		return gestures;
	}

}
