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

class BitVector {

	long direct;
	int size;
	
	BitVector() {
		direct = 0L;
		size = 32;
	}
	
	BitVector(int s) {
		direct = 0L;
		size = s;
	}
	
	void zero() {
		direct = 0L;
	}
	
	void fill() {
		direct = -1L;
	}
	
	void set(int b) {
		direct |= 1L << b;
	}
	
	void clear(int b) {
		direct &= ~(1L << b);
	}
	
	int size() {
		return size;
	}
	
	boolean isSet(int b) {
		return ((direct >> b) & 1L) > 0;
	}
	
	boolean isVoid() {
		return direct == 0L;
	}
	
	// operator |=
	BitVector orEq (BitVector v) { direct |= v.direct; return this; } 
	// operator &=
	BitVector andEq (BitVector v)  { direct &= v.direct ; return this; }

	
	int bitCount(int max){
		int count = 0;
		for (int i = 0; i < max && i < size; ++i)
			if (isSet (i))
				++count;
		return count;
	}
	
	public String toString(){
		String res = "";
		for (int i = 0; i < size; ++i)
			if (isSet (i))
				res+="1";
			else
				res+="0";
		return res;
	}

}
