/*
 * Authors: Caroline Appert (caroline.appert@lri.fr) Copyright (c) Universite
 * Paris-Sud XI, 2007. All Rights Reserved Licensed under the GNU LGPL. For full
 * terms see the file COPYING.
 */
package fr.lri.swingstates.gestures;

/**
 * A score is a couple (String, double).
 * 
 * @author Caroline Appert
 *
 */
public class Score {

	String name;
	double score;
	
	/**
	 * Builds a Score.
	 * @param name The score name.
	 * @param score The score value.
	 */
	public Score(String name, double score) {
		super();
		this.name = name;
		this.score = score;
	}

	/**
	 * @return this score value.
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @return this score name.
	 */
	public String getName() {
		return name;
	}
	
}
