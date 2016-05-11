/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.util.LinkedList;



/**
 * <p>A <code>AExtensionalTag</code> tag is a tag that can be explicitely attached to and detached from an animation, 
 * i.e. an animation can be added to or removed to the tag's collection.
 * 
 * @see fr.lri.swingstates.animations.ATag
 * @see fr.lri.swingstates.animations.Animation
 * 
 * @author Caroline Appert
 */

public class AExtensionalTag extends ATag {

	/**
	 * Builds a <code>AExtensionalTag</code> that can tag the animations.
	 * @see fr.lri.swingstates.animations.Animation
	 */
	public AExtensionalTag() {
		super();
		collection = new LinkedList<Object>();
	}
	
	/**
	 * Method called when this tag is added to a <code>Animation</code>. By default, this method does nothing.
	 * Redefine it in a subclass to specify side effects, such as changing the delay of the attached <code>Animation</code>.
	 * @param a The animation
	 */
	public void added(Animation a) { }

	/**
	 * Method called when this tag is removed from an <code>Animation</code>. By default, this method does nothing.
	 * Redefine it in a subclass to specify side effects, such as changing the delay of the attached <code>Animation</code>.
	 * @param a The animation.
	 */
	public void removed(Animation a){ }
	
	/**
	 * Adds this tag to a given <code>Animation</code>.
	 * This method calls <code>added(Animation)</code>, which can be redefined in subclasses
	 * to specify side effects, such as changing the delay of the attached animation when the tag is added.
	 * @param a The animation
	 * @return this tag
	 * @see AExtensionalTag#added(Animation)
	 */
	public AExtensionalTag addTo(Animation a){
		if(collection.contains(a)) return this;
		collection.add(a);
		added(a);
		return this;
	}
	
	/**
	 * Removes this tag from a given <code>Animation</code>.
	 * This method calls <code>removed(Animation)</code>, which can be redefined in subclasses
	 * to specify side effects, such as changing the delay of the attached animation when the tag is removed.
	 * @param a The animation
	 * @return this tag
	 * @see AExtensionalTag#removed(Animation)
	 */
	public AExtensionalTag removeFrom(Animation a){
		removed(a);
		collection.remove(a);	
		return this;
	}


}
