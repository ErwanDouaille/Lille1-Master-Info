/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.gestures.rubine;

import java.util.Vector;

/*******************************************************************************
 * The algorithm and original C code are: (C) Copyright, 1990 by Dean Rubine,
 * Carnegie Mellon University Permission to use this code for noncommercial
 * purposes is hereby granted. Permission to copy and distribute this code is
 * hereby granted provided this copyright notice is retained. All other rights
 * reserved.
 ******************************************************************************/

class VectorUtility {

	public static double scalarProduct(Vector<Double> vector, Vector<Double> v) {
		if (vector.size() != v.size())
			System.err.println("Scalar product: " + vector.size() + "x" + v.size());
		double res = 0;
		for (int i = 0; i < vector.size(); i++)
			res += vector.get(i) * v.get(i);
		return res;
	}

	static Vector<Double> substract(Vector<Double> vector, Vector<Double> v) {
		int dim = (vector.size() < v.size()) ? vector.size() : v.size();
		for (int i = 0; i < dim; i++)
			vector.set(i, vector.get(i) - v.get(i));
		return vector;
	}

	public static Vector<Double> minus(Vector<Double> vector, Vector<Double> v) {
		Vector<Double> res = new Vector<Double>(vector);
		res = substract(res, v);
		return res;
	}

	public static Vector<Double> mult(Vector<Double> vector, Matrix m) {
		Vector<Double> res = new Vector<Double>();
		if (vector.size() != m.nRows) {
			System.err.println("Vector times Matrix\n");
			return res;
		}
		for (int i = 0; i < m.nCols; i++) {
			double resi = 0;
			for (int j = 0; j < vector.size(); j++)
				resi += vector.get(j) * m.items[i][j];
			res.add(resi);
		}
		return res;
	}

	/*
	 * Compute v' * m * v
	 */
	public static double quadraticForm(Vector<Double> vector, Matrix m) {
		double res = 0;
		if (vector.size() != m.nRows || vector.size() != m.nCols)
			System.err.println("QuadraticForm: bad matrix size\n");
		for (int i = 0; i < vector.size(); i++)
			for (int j = 0; j < vector.size(); j++)
				res += m.items[i][j] * vector.get(i) * vector.get(j);
		return res;
	}

	public Vector<Double> slice(Vector<Double> vector, BitVector rowmask) {
		Vector<Double> res = new Vector<Double>();
		int length = rowmask.bitCount(vector.size());
		for (int i = 0; i < length; i++)
			if (rowmask.isSet(i))
				res.add(vector.get(i));
		return res;
	}

}
