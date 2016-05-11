/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.util.LinkedList;


/**
 * <p>A <code>CExtensionalTag</code> tag is a tag that can be explicitely attached to and detached from a shape, 
 * i.e. a shape can be added to or removed to the tag's collection.
 * 
 * @see fr.lri.swingstates.canvas.CTag
 * @author Caroline Appert
 */

public abstract class CExtensionalTag extends CTag {

	
	
	/**
	 * Builds a CExtensionalTag that can tag shapes.
	 */
	public CExtensionalTag(){
		super(null);
		collection = new LinkedList<Object>();
	}
	
	/**
	 * Builds a CExtensionalTag that can tag the shapes displayed on the canvas c. This tag can be explicitely removed to any shape displayed on c.
	 * @param c The canvas
	 */
	public CExtensionalTag(Canvas c){
		super(c);
		collection = new LinkedList<Object>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void reset() {
		iterator = collection.iterator();
	}

	/**
	 * Method called when this tag is added to an CShape. By default, this method does nothing.
	 * Redefine it in a subclass to specify side effects, such as changing the color of the attached CShape.
	 * @param s The CShape
	 */
	public void added(CShape s) { }

	/**
	 * Method called when this tag is removed from an CShape. By default, this method does nothing.
	 * Redefine it in a subclass to specify side effects, such as changing the color of the attached CShape.
	 * @param s The CShape
	 */
	public void removed(CShape s){ }
	
	/**
	 * Adds the shape s to the collection containing all the shapes that have this tag.
	 * This method calls <code>begin(CShape)</code>, which can be redefined in subclasses
	 * to specify side effects, such as changing the color of the attached object when the tag is added.
	 * @param s The CShape
	 * @return this tag
	 * @see CExtensionalTag#added(CShape)
	 */
	public final CExtensionalTag addTo(CShape s){
		if(getCanvas() != null && s.getCanvas() != getCanvas()) return this;
		if(collection.contains(s)) return this;
		collection.add(s);
		doAdded(s);
		return this;
	}
	
	private void doAdded(CShape s) {
		if(canvas == null) 
			canvas = s.getCanvas();
		else {
			if(s.getCanvas() != canvas)
				System.err.println("Cannot tag shapes belonging to different canvases with the same instance of tag");
		}
		if(canvas != null) canvas.registerTag(this);
		added(s);
	}

	/**
	 * Removes the shape s from the collection containing all the shapes that have this tag.
	 * This method calls <code>end(CShape)</code>, which can be redefined in subclasses
	 * to specify side effects, such as changing the color of the attached object when the tag is removed.
	 * @param s The CShape
	 * @return this tag
	 * @see CExtensionalTag#removed(CShape)
	 */
	public final CExtensionalTag removeFrom(CShape s){
		if(collection.remove(s))
			removed(s);
		return this;
	}

}
