/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>Tags are labels associated to objects.
 * A tag is an object corresponding to a set of
 * <code>CShape</code> shapes that can be browsed (i.e. a collection
 * and an iterator on it).
 * </p>
 * @author Caroline Appert
 */
/**
 * <p>Tags are labels associated to objects or animations.
 * <p>A tag is an object corresponding to a set of objects
 * that can be browsed (i.e. a collection and an iterator on it).
 * <p>Some tags are extensional
 * (<code>CExtensionalTag</code> and <code>JExtensionalTag</code>),
 * they can be explicitely attached to and detached from an object,
 * i.e. an object can be added to or removed from the tag's collection.
 * Some other tags are intentional and can not be explicitely added or removed,
 * they tag objects that verify a given property.
 * Thus, the collection of objects tagged by an intentional tag
 * is computed at each use of this tag. 
 * </p>
 * <p>A tag can be attached to any number of objects.
 * An object have any number of tags attached to it.</p>
 * <p>Tags are used for two main purposes : grouping and interaction.
 * <ul>
 * <li>In order to create a group of objects in the canvas, one creates
 * a new tag and attaches it to each object in the group.
 * <li> Tags are also used in connection with state machines
 * to describe interaction.
 * <ul> 
 * <li>For example, if some objects in the canvas can be moved but not others,
 * one creates a tag called, say, movable
 * and attaches it to those objects that can be moved.
 * Then, in the canvas' state machine, one can specify transitions
 * such as <code>ClickOnTag(BUTTON1, movable)</code>
 * that will fire only when the user is clicking on an
 * object with tag "movable".
 * <li>Transitions in state machines can also be fired
 * on any object having a tag of a given class. One
 * can define a ColoredTag class (with a color field)
 * and attach many instances of this class to many shapes.
 * Then, transitions like:
 * <pre>
 * Transition onAnyColor = new Press(ColoredTag.class, BUTTON1) {
 * 		public void action() {
 * 			System.out.println("This object has a "+((ColoredTag)getTag()).color+" tag");
 * 		}
 * };
 *  </pre>
 *  can be used in a state machine.
 * </ul>
 * </ul>
 * </p>
 * @author Caroline Appert
 */
public abstract class Tag implements Iterator {

	protected Collection<Object> collection;
	
	/**
	 * @return shapes tagged by this tag as a collection.
	 */
	public Collection<Object> getCollection() {
		return collection;
	}
	
	/**
	 * Inits the tag's iterator.
	 */
	public abstract void reset();

	/**
	 * Tests if there is other objects tagged by this tag.
	 * @return true if there is other objects tagged by this tag, false otherwise. 
	 * @see java.util.Iterator#hasNext()
	 */
	public abstract boolean hasNext();

	/**
	 * {@inheritDoc}
	 */
	public abstract Object next();

	/**
	 * This methods is not supported by a Tag.
	 * To remove a tag of a shape, take a look at <code>removeFrom</code>
	 * @see fr.lri.swingstates.canvas.CExtensionalTag#removeFrom(fr.lri.swingstates.canvas.CShape) 
     */
    public final void remove() {
        throw new UnsupportedOperationException();
    }

	/**
	 * Counts how many objects are tagged by this tag.
	 * @return the number of objects that are tagged by this tag.
	 */
	public final int size(){
		int i = 0;
		reset();
		while(hasNext()) {
			next(); i++;
		}
		return i;	
	}

}
