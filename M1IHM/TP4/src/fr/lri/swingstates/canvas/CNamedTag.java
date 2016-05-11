/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

/**
 * <p>A <code>CNamedTag</code> tag is a <code>CExtensionalTag</code>.
 * A <code>CNamedTag</code> tag is a set of shapes having the same label.</p>
 * 
 * <p>One can create a CNamedTag named "movable" and use transitions in state machine like:
 * <pre>
 * Transition onAnyColor = new Press("movable", BUTTON1) {
 * 		public void action() {
 * 			...
 * 		}
 * };
 *  </pre>
 * </p>
 * 
 * @see fr.lri.swingstates.canvas.CExtensionalTag
 * @author Caroline Appert
 */
public class CNamedTag extends CExtensionalTag {

	
	String name;

	/**
	 * Builds a CNamedTag.
	 * @param n The name of the tag
	 */
	public CNamedTag(String n) {
		super();
		name = n;
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
		if(canvas.getTag(n) != null)
			return false;
		name = n;
		return true;
	}
	
}
