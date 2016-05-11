/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * @author Caroline Appert
 *
 * A <code>ClassTag</code> is an intentional tag
 * corresponding to a group of shape tagged by a
 * tag of a given class.
 * 
 */
public class ClassTag extends CIntentionalTag {

	private static Hashtable<String, ClassTag> classTags;
	
	private Class cl;
	
	/**
	 * Builds a ClassTag.
	 * @param c The canvas
	 * @param tagClass The class of the tag
	 */
	private ClassTag(Canvas c, Class tagClass) {
		super(c);
		cl = tagClass;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean criterion(CShape s) {
		for(Iterator i = canvas.allCanvasTags.iterator(); i.hasNext(); ) {
			CTag tg = (CTag)i.next();
			if(tg.getClass() == cl && s.hasTag(tg))
				return true;
		}
		return s.getClass() == cl;
	}
	
	/**
	 * Returns the existing <code>ClassTag</code>
	 * registered on a given canvas or creates it if
	 * it does not exist.
	 * 
	 * @param c The canvas on which must be registered this tag
	 * @param tagClass The class of the tag
	 * @return The <code>tagClass</code> 
	 */
	public static ClassTag getTag(Canvas c, Class tagClass) {
		if(classTags == null) {
			classTags = new Hashtable<String, ClassTag>();
		}
		String key = tagClass.getName() + c.getClass().getName() + "@" + Integer.toHexString(c.hashCode());
		ClassTag existingTag = classTags.get(key);
		if(existingTag == null) {
			existingTag = new ClassTag(c, tagClass);
			classTags.put(key, existingTag);					
		}
		existingTag.reset();
		return existingTag;
	}

}
