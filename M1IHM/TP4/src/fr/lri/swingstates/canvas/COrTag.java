/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.util.Collection;
import java.util.LinkedList;


/**
 * A <code>COrTag</code> is a tag corresponding to the set of shapes tagged by a tag t1 or a tag t2.
 * @author appert
 */
public class COrTag extends CTag {

	CTag tag1;
	CTag tag2;
	
	/**
	 * Builds a <code>COrTag</code>tag corresponding to the set of shapes tagged by a tag t1 or a tag t2.
	 * @param t1 The first tag.
	 * @param t2 The second tag.
	 */
	public COrTag(CTag t1, CTag t2){
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
		if(!tag1.hasNext()) return tag2.hasNext();
		return true;
	}
	
	/**
	 * Returns a collection containing all the shapes tagged by this tag.
	 * This method uses the iterator of the two tags, thus it mustn't be used while browsing this tag, the tag t1 or the tag t2.
	 * @return a collection containing all the shapes tagged by this tag.
	 */
	public Collection<Object> getCollection() {
		LinkedList<Object> l = new LinkedList<Object>();
		tag1.reset();
		while(tag1.hasNext())
			l.add(tag1.nextShape());
		tag2.reset();
		while(tag2.hasNext())
			l.add(tag2.nextShape());
		return l;	
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape nextShape() {
		if(tag1.hasNext()) return tag1.nextShape();
		return tag2.nextShape();
	}
	
}
