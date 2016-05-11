/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * <p>
 * Intentional tags label graphical objects, i.e. CShapes, that verify a given property,
 * such as all objects of a given color. The collection of tagged objects
 * is recomputed each time the tag is used. You don't have to add them explicitely to
 * <code>CShape</code>s.
 * </p>
 * 
 * <p>
 * Extends this <code>CIntentionalTag</code> class
 * and overrides the method <code>criterion</code> to specify the property. 
 * Here is an example of a tag shared by all red shapes. 
 * 
 * <pre>
 * class RedTag extends CIntentionalTag {
 * 		
 * 		public RedTag(Canvas canvas) {
 * 			super(canvas);
 * 			reset();
 * 		}
 * 		
 * 		public boolean criterion(CShape s) {
 * 			return s.getFillPaint() == Color.RED;
 * 		}
 * 		
 * 	}
 * </pre>
 * </p>
 * 
 * @author Caroline Appert
 *
 */
public abstract class CIntentionalTag extends CTag {

	
	/**
	 * Builds a <code>CIntentionalTag</code>.
	 * @param c The canvas in which the shapes must be tagged.
	 */
	public CIntentionalTag(Canvas c) {
		super(c);
	}

	/**
	 * The method to override to specify the property a shape must check to be tagged by this tag.
	 * @param s The shape.
	 * @return true if <code>s</code> checks the property, false otherwise.
	 */
	public abstract boolean criterion(CShape s);
	
	/**
	 * {@inheritDoc}
	 */
	public final void reset() {
		collection = new LinkedList<Object>();
		synchronized(canvas.getDisplayList()) {
			for(Iterator i = canvas.getDisplayList().iterator(); i.hasNext(); ) {
				CShape next = (CShape)i.next();
				if(criterion(next)) collection.add(next);
			}
		}
		iterator = collection.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Object> getCollection() {
		reset();
		return collection;
	}
}
