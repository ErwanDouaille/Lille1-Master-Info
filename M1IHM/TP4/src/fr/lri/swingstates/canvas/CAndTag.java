/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.util.Collection;
import java.util.LinkedList;


/**
 * A <code>CAndTag</code> is a CTag corresponding to the set of shapes tagged 
 * by a <code>CTag t1</code> and a <code>CTag t2</code>.
 * @author Caroline Appert
 */
public class CAndTag extends CTag {

	private CTag tag1;
	private CTag tag2;
	private CShape next;
	
	/**
	 * Builds a <code>CAndTag</code>tag corresponding to the set of shapes tagged by a tag t1 and a tag t2.
	 * @param t1 The first tag.
	 * @param t2 The second tag.
	 */
	public CAndTag(CTag t1, CTag t2){
		super(t1.canvas);
		tag1 = t1;
		tag2 = t2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		tag1.reset();
		tag2.reset();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext(){
		if(!tag1.hasNext()) return false;
		while(tag1.hasNext()) {
			next = tag1.nextShape();
			if(tag2.getCollection().contains(next)) return true;
		}
		return false;
	}
	
	
	/**
	 * Returns a collection containing all the shapes tagged by this tag.
	 * This method makes use of the iterator of the two tags, 
	 * thus it must not been used while browsing t1 or t2.
	 * @return a collection containing all the shapes tagged by this tag.
	 */
	public Collection<Object> getCollection() {
		LinkedList<Object> l = new LinkedList<Object>();
		tag1.reset();
		while(tag1.hasNext()) {
			CShape s = tag1.nextShape();
			if(tag2.getCollection().contains(s)) l.add(s);
		}
		return l;	
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape nextShape() {
		return next;
	}
	
}
