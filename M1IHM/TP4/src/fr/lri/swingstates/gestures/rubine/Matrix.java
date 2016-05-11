/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.gestures.rubine;

/***********************************************************************
The algorithm and original C code are:
(C) Copyright, 1990 by Dean Rubine, Carnegie Mellon University
 Permission to use this code for noncommercial purposes is hereby granted.
 Permission to copy and distribute this code is hereby granted provided
 this copyright notice is retained.  All other rights reserved.
**********************************************************************/

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Matrix class.
 * 
 * @author Caroline Appert
 *
 */
class Matrix {

	static int PERMBUFSIZE = 200;
	
	double[][] items;
	int nRows, nCols;
	
	Matrix(int r, int c, boolean clear){
		nRows = r;
		nCols = c;
		items = new double[r][c];
		if(clear)
			clear();
	}
	
	Matrix(int r, int c){
		this(r, c, false);
	}
	
	Matrix(Matrix m){
		nRows = m.nRows;
		nCols = m.nCols;
		items = new double[nRows][nCols];
		for(int i = 0; i < nRows; i++)
			for(int j = 0; j < nCols; j++)
				items[i][j] = m.items[i][j];
	}
	
	public Object read(DataInputStream in) throws IOException {
		nRows = in.readInt();
		nCols = in.readInt();
		items = new double[nRows][nCols];
		for(int i=0; i < items.length; i++)
			for(int j=0; j < items[i].length; j++)
				items[i][j] = in.readDouble();
		return this;
	}

	public void write(DataOutputStream out) throws IOException {
		out.writeInt(nRows);
		out.writeInt(nCols);
		for(int i=0; i < items.length; i++)
			for(int j=0; j < items[i].length; j++)
				out.writeDouble(items[i][j]);
	}
	
	public void clear(){
		for(int i = 0; i < nRows; i++)
			for(int j = 0; j < nCols; j++)
				items[i][j] = 0;
	}
	
	public void fill(double d){
		for(int i = 0; i < nRows; i++)
			for(int j = 0; j < nCols; j++)
				items[i][j] = d;
	}
	
	public Matrix copy(Matrix m){
		if (nRows != m.nRows || nCols != m.nCols) {
			nRows = m.nRows;
			nCols = m.nCols;
			items= new double[nRows][nCols];
		}
		for (int i = 0 ; i < nRows ; i++)
			for (int j = 0; j < nCols ; j++)
				items[i][j] = m.items[i][j];
		return this;
	}
	
	public Matrix multiplyByScalar(double d){
		for (int i = 0 ; i < nRows ; i++)
			for (int j = 0; j < nCols ; j++)
				items[i][j] *= d;
		return this;
	}
	
	public Matrix add(Matrix m){
		int r = (nRows < m.nRows) ? nRows : m.nRows;
		int c = (nCols <m.nCols) ? nCols : m.nCols;
		for(int i = 0; i < r; i++)
			for(int j = 0; j < c; j++)
				items[i][j] += items[i][j];
		return this;
	}

	public Matrix mult(Matrix m){
		if(nCols != m.nRows)
			System.err.println("Matrix Multiply: Different sizes\n");
		Matrix res = new Matrix(nRows, m.nCols);
		for (int i = 0; i < nRows; i++)
			for(int j = 0; j < m.nCols; j++) {
				double sum = 0;
				for(int k = 0; k < nCols; k++)
					sum += items[i][k] * m.items[k][j];
				res.items[i][j] = sum;
			}
		return res;
	}	
	
	public String toString(){
		String res = "M "+nRows+" "+nCols+"\n";
		for(int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols ; j++)
				res += " "+items[i][j];
			res += "\n";
		}
		res+="\n";
		return res;
	}
	
	// We can't modify an argument of type double in java
	// So, we modify an array containing only one element of type double
	public Matrix invert(double[] rdet){
		int i, j, k;
		double recipBiga, hold;
		int[] l = new int[PERMBUFSIZE];
		int[] m = new int[PERMBUFSIZE];

		if (nRows != nCols)
			System.err.println("InvertMatrix: not square");
			
		Matrix rm = this;

	    /* Allocate permutation vectors for l and m, with the same origin
	       as the matrix. */

		if (nRows >= PERMBUFSIZE)
			System.err.println("InvertMatrix: PERMBUFSIZE");

		double det = 1.0;
		for (k = 0; k < nRows;  k++) {
			l[k] = k;  
			m[k] = k;
			double biga = rm.items[k][k];

			/* Find the biggest element in the submatrix */
			for (i = k;  i < nRows;  i++)
				for (j = k; j < nRows; j++)
					if (Math.abs(rm.items[i][j]) > Math.abs(biga)) {
						biga = rm.items[i][j];
						l[k] = i;
						m[k] = j;
					}

			/* Interchange rows */
			i = l[k];
			if (i > k)
				for (j = 0; j < nRows; j++) {
					hold = -rm.items[k][j];
					rm.items[k][j] = rm.items[i][j];
					rm.items[i][j] = hold;
				}

			/* Interchange columns */
			j = m[k];
			if (j > k)
				for (i = 0; i < nRows; i++) {
					hold = -rm.items[i][k];
					rm.items[i][k] = rm.items[i][j];
					rm.items[i][j] = hold;
				}

			/* Divide column by minus pivot
			    (value of pivot element is contained in biga). */
			if (biga == 0.0) {
				rdet[0] = 0.;
				return rm;
			}

			recipBiga = 1/biga;
			for (i = 0; i < nRows; i++)
				if (i != k)
					rm.items[i][k] *= -recipBiga;

			/* Reduce matrix */
			for (i = 0; i < nRows; i++)
				if (i != k) {
					hold = rm.items[i][k];
					for (j = 0; j < nRows; j++)
						if (j != k)
							rm.items[i][j] += hold * rm.items[k][j];
				}

			/* Divide row by pivot */
			for (j = 0; j < nRows; j++)
				if (j != k)
					rm.items[k][j] *= recipBiga;

			det *= biga;	/* Product of pivots */
			
			rm.items[k][k] = recipBiga;

		}	/* K loop */

		/* Final row & column interchanges */
		for (k = nRows - 1; k >= 0; k--) {
			i = l[k];
			if (i > k)
				for (j = 0; j < nRows; j++) {
					hold = rm.items[j][k];
					rm.items[j][k] = -rm.items[j][i];
					rm.items[j][i] = hold;
				}
			j = m[k];
			if (j > k)
				for (i = 0; i < nRows; i++) {
					hold = rm.items[k][i];
					rm.items[k][i] = -rm.items[j][i];
					rm.items[j][i] = hold;
				}
		}

		rdet[0] = det;
		return rm;

	}
	
	public Matrix slice(BitVector rowmask, BitVector colmask){
		Matrix r = new Matrix(rowmask.bitCount (nRows), colmask.bitCount (nCols));
		int ri = 0;
		for (int i = 0; i < nRows; i++)
			if (rowmask.isSet (i) ) {
				int rj = 0;
				for (int j = 0; j < nCols; j++)
					if(colmask.isSet (j))
						r.items[ri][rj++] = items[i][j];
				ri++;
			}
		return r;
	}
	
	public Matrix deSlice (double fill, int nrow, int ncol, BitVector rowmask, BitVector colmask){
		// *** not optimal : result is copied back
		Matrix r = new Matrix(nrow, ncol);
		r.fill (fill);
		int ri = 0;
		for (int i = 0; i < nrow; i++) {
			if (rowmask.isSet (i) )  {
				int rj = 0;
				for (int j = 0; j < ncol; j++)
					if (colmask.isSet (j))
						r.items[i][j] = items[ri][rj++];
				ri++;
			}
		}
		return r;
	}

}
