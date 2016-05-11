/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.awt.Component;
import java.util.LinkedList;

/**
 * <p>A <code>JExtensionalTag</code> tag is a tag that can be explicitely attached to and detached from a JComponent, 
 * i.e. this JComponent can be added to or removed to the tag's collection.
 * 
 * @see fr.lri.swingstates.sm.JTag
 * @author Caroline Appert
 */
public abstract class JExtensionalTag extends JTag {

	/**
	 * Builds a SMExtensionalTag that can tag any object. This tag can be explicitely removed to any object.
	 */
	public JExtensionalTag(){
		super();
		collection = new LinkedList<Object>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void reset() {
		iterator = collection.iterator();
	}

	/**
	 * Method called when this tag is added to an JComponent. By default, this method does nothing.
	 * Redefine it in a subclass to specify side effects, such as changing the color of the attached JComponent.
	 * @param c The Component
	 */
	public void added(Component c) { }

	/**
	 * Method called when this tag is removed from an JComponent. By default, this method does nothing.
	 * Redefine it in a subclass to specify side effects, such as changing the color of the attached JComponent.
	 * @param c The JComponent
	 */
	public void removed(Component c){ }
	
	/**
	 * Adds the JComponent c to the collection containing all the components having this tag.
	 * This method calls <code>begin(JComponent)</code>, which can be redefined in subclasses
	 * to specify side effects, such as changing the color of the attached object when the tag is added.
	 * @param c The JComponent
	 * @return this tag
	 * @see JExtensionalTag#added(Component)
	 */
	public final JExtensionalTag addTo(Component c){
		if(collection.contains(c)) return this;
		collection.add(c);
		added(c);
		return this;
	}
	
	/**
	 * Removes the component c from the collection containing all the components having this tag.
	 * This method calls <code>end(JComponent)</code>, which can be redefined in subclasses
	 * to specify side effects, such as changing the color of the attached object when the tag is removed.
	 * @param c The JComponent
	 * @return this tag
	 * @see JExtensionalTag#removed(Component)
	 */
	public final JExtensionalTag removeFrom(Component c){
		removed(c);
		collection.remove(c);	
		return this;
	}
	
	
}
