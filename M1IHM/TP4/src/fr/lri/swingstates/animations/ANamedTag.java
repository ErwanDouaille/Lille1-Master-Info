/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.util.Hashtable;

/**
 * <p>
 * A <code>ANamedTag</code> tag is a <code>AExtensionalTag</code>.
 * A <code>ANamedTag</code> tag an be referenced using its key string.
 * </p>
 * 
 * @see fr.lri.swingstates.animations.AExtensionalTag
 * @see fr.lri.swingstates.animations.ATag
 * @see fr.lri.swingstates.animations.Animation
 * 
 * @author Caroline Appert
 */
public class ANamedTag extends AExtensionalTag {

	static Hashtable<String, ANamedTag> namedTags = null;
	String name;
	
	/**
	 * Builds a <code>ANamedTag</code> tag.
	 * Note that if a tag with the same name already exists, this new tag is not registered, and the results are undefined.
	 * @param n The name of the tag
	 */
	public ANamedTag(String n) {
		super();
		name = n;
		
		if(namedTags == null)
			namedTags = new Hashtable<String, ANamedTag>();
		namedTags.put(n, this);
	}
	
	/**
	 * For internal use. See class <code>SwingStatesApplet</code>.
	 * @see fr.lri.swingstates.sm.SwingStatesApplet#init()
	 */
	public static void initForApplet() {
		namedTags = null;
	}
	
	/**
	 * Returns the tag object given its name.
	 * @param t The name of the tag
	 * @return The tag, or null if no such tag exists.
	 */
	public static ANamedTag getTag(String t) {
		if(namedTags == null) return null;
		ANamedTag res = (ANamedTag)(namedTags.get(t));
		if(res != null)
			res.reset();
		return res;
	}
	
	/**
	 * Returns the name of this tag.
	 * @return the name of this tag.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of this tag.
	 * The name can only be changed if no existing tag by this name already exists.
	 * @param n The name.
	 * @return true if the name could be change, false otherwise.
	 */
	public boolean setName(String n) {
		if(namedTags.get(n) != null)
			return false;
		namedTags.remove(name);
		name = n;
		namedTags.put(name, this);
		return true;
	}

}
