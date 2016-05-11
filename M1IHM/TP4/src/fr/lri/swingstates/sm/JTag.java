/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JComponent;

/**
 * <p>
 * JTags are labels associated to components.
 * </p>
 * <p>
 * A tag is an object corresponding to a set of <code>JComponent</code>
 * objects that can be browsed (i.e. a collection and an iterator on it). One
 * example is a <code>JNamedTag</code> which is a set of components having a
 * given label attached to them.
 * </p>
 * <p>
 * Any component has a default tag of the name of its class. For instance, one
 * can write a state machine that contains a transition that is fired only when
 * occuring on JButton components:
 * 
 * <pre>
 * JStateMachine sm = new JStateMachine() {
 * 	public State s = new State() {
 * 		Transition onlyOnButtons = new Press(&quot;javax.swing.JButton&quot;, BUTTON1) {
 * 			public void action() {
 * 				System.out.println(&quot;press on a the button named &quot; + ((JButton) getSource()).getText());
 * 			}
 * 		};
 * 	};
 * };
 * </pre>
 * 
 * </p>
 * <p>
 * Some tags, <code>JExtensionalTag</code> tags, can be explicitely attached
 * to and detached from a component, i.e. a component can be added to or removed
 * from the tag's collection, while others can not.
 * </p>
 * <p>
 * A tag can be attached to any number of components. A component can have any
 * number of tags attached to it.
 * </p>
 * <p>
 * Tags are used for two main purposes : grouping and interaction.
 * <ul>
 * <li>In order to create a group of objects in the canvas, one creates a new
 * tag and attaches it to each object in the group. The method
 * <code>action()</code> calls the method <code>action(JComponent)</code>
 * for any component having this tag. Redefine the method
 * <code>action(JComponent)</code> to define a specific behavior such as
 * changing the background color of all the tagged components.
 * 
 * <pre>
 * class ColorChangingComponent extends JTag {
 *    ...
 *    void action(JComponent c) {
 *       c.setBackground(Color.RED);
 *    }
 *    ...
 * }
 * </pre>
 * 
 * Calling the method <code>action()</code> on a
 * <code>ColorChangingComponent</code> causes background of every tagged
 * component being red.
 * <li>Tags are also used in connection with state machines to describe
 * interaction. For example, if some objects in the canvas can be moved but not
 * others, one creates a tag called, say, "movable" and attaches it to those
 * objects that can be moved. Then, in the canvas' state machine, one can
 * specify transitions such as <code>ClickOnTag("movable")</code> that will
 * fire only when the user is clicking on a component with tag "movable".
 * </ul>
 * </p>
 * 
 * @see fr.lri.swingstates.sm.Tag
 * @author Caroline Appert
 */
public abstract class JTag extends Tag {

	static ArrayList<JTag> allJComponentTags = null;
	protected Iterator iterator = null;

	/**
	 * Builds a JTag that can tag components.
	 */
	public JTag() {
		super();
		if (allJComponentTags == null)
			allJComponentTags = new ArrayList<JTag>();
		allJComponentTags.add(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object next() {
		return nextComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean hasNext() {
		if(iterator == null) return false;
		return iterator.hasNext();
	}

	/**
	 * @return The next component tagged by this tag as a
	 *         <code>JComponent</code>.
	 * @see java.util.Iterator#next()
	 */
	public final JComponent nextComponent() {
		return (JComponent) iterator.next();
	}

	/**
	 * Tests if the component c is tagged by this tag.
	 * 
	 * @param c
	 *            The component
	 * @return true if c is tagged by this tag, false otherwise.
	 */
	public boolean tagsComponent(JComponent c) {
		reset();
		while (hasNext())
			if (nextComponent() == c)
				return true;
		return false;
	}

	/**
	 * Tests if the component <code>c</code> is tagged by a tag of class
	 * <code>tagClass</code>.
	 * 
	 * @param c The component
	 * @param tagClass The class of tags
	 *            
	 * @return the list of tags of class <code>tagClass</code> that are
	 *         attached to <code>c</code>.
	 */
	public static LinkedList<JTag> tagsComponent(JComponent c, Class tagClass) {
		LinkedList<JTag> res = new LinkedList<JTag>();
		if (allJComponentTags == null)
			return res;
		for (Iterator<JTag> iterator = allJComponentTags.iterator(); iterator.hasNext();) {
			JTag next = iterator.next();
			if (next.getClass().equals(tagClass))
				if (next.tagsComponent(c))
					res.add(next);
		}
		return res;
	}

	/**
	 * Returns the list of components that have a tag of a given class.
	 * 
	 * @param tagClass
	 *            The tag class
	 * @return The list of components.
	 */
	public static LinkedList getTaggedComponents(Class tagClass) {
		LinkedList<Object> res = new LinkedList<Object>();
		if (allJComponentTags == null)
			return res;
		for (Iterator<JTag> iterator = allJComponentTags.iterator(); iterator.hasNext();) {
			JTag next = iterator.next();
			if (next.getClass().equals(tagClass))
				res.addAll(next.getCollection());
		}
		return res;
	}

	/**
	 * Method called by <code>action()</code>. This method does nothing.
	 * Redefine it in a subclass to specify the effect of
	 * <code>JTag#action()</code>
	 * 
	 * @param c
	 *            The JComponent on which applying specific treatments.
	 */
	public void action(JComponent c) {
	}

	/**
	 * Calls <code>action(JComponent c)</code> for every component c that has
	 * this tag.
	 * 
	 * @return this tag
	 */
	public final JTag action() {
		reset();
		while (hasNext())
			action(nextComponent());
		return this;
	}

	/**
	 * Calls <code>c.setBackground(Color color)</code> for every component c
	 * that has this tag.
	 * 
	 * @param color
	 *            The new background color
	 * @return this tag
	 */
	public final JTag setBackground(Color color) {
		reset();
		while (hasNext())
			nextComponent().setBackground(color);
		return this;
	}

}
