/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * <p>A <code>JNamedTag</code> tag is a <code>JExtensionalTag</code>.
 * A <code>JNamedTag</code> tag is a set of components having the same label.
 * 
 * <p>One can create a CNamedTag named "colorable":
 * <pre>
 * CNamedTag tag = new CNamedTag("colorable");
 * </pre>
 * and use transitions in state machine like:
 * <pre>
 * Transition onAnyColor = new Press("colorable", BUTTON1) {
 * 		public void action() {
 * 			...
 * 		}
 * };
 * </pre>
 * </p>
 * 
 * @see fr.lri.swingstates.sm.JExtensionalTag
 * @author Caroline Appert
 */
public class JNamedTag extends JExtensionalTag {

	static Hashtable<String, JNamedTag> namedTags = null;
	String name;
	
	/**
	 * Builds a <code>JNamedTag</code> tag.
	 * Note that if a tag with the same name already exists, this new tag is not registered and the results are undefined.
	 * This constructor is left public and does not throw an exception in order to make it possible to create anonymous classes derived from <code>JNamedTag</code>.
	 * @param n The name of the tag
	 */
	public JNamedTag(String n) {
		super();
		name = n;
		if(namedTags == null)
			namedTags = new Hashtable<String, JNamedTag>();
		if(namedTags.get(name) == null) {
			namedTags.put(n, this);
			iterator = collection.iterator();
		}
	}
	
	/**
	 * Clear all registered tags.
	 */
	public static void clear() {
		if(namedTags != null) namedTags.clear();
	}
	
	/**
	 * Returns the list of components that have a given tag.
	 * @param t The name of the tag
	 * @return The list of shapes.
	 */
	public static LinkedList getTaggedComponents(String t) {
		if(namedTags == null) return null;
		JNamedTag tg = (JNamedTag)(namedTags.get(t));
		if(tg != null) return (LinkedList)(tg.collection);
		return null;
	}
	
	/**
	 * Returns the tag object given its name.
	 * @param t The name of the tag
	 * @return The tag, or null if no such tag exists.
	 */
	public static JNamedTag getTag(String t) {
		if(namedTags == null) return null;
		JNamedTag res = (JNamedTag)(namedTags.get(t));
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
