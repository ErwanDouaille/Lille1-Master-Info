/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.Timer;

/**
 * The type used for timers having a name.
 * @author Caroline Appert
 */
public class TaggedTimer extends Timer {

	private static LinkedList<TaggedTimer> allTags;
	private String tagName;
	
	
	TaggedTimer(String tagName, int delay, ActionListener listener) {
		super(delay, listener);
		this.tagName = tagName;
		if(allTags == null) allTags = new LinkedList<TaggedTimer>();
		allTags.add(this);
	}

	/**
	 * @return The name of this timer.
	 */
	public String getTagName() {
		return tagName;
	}
	
	static TaggedTimer getTimer(String tagName) {
		if(allTags == null) return null;
		for(Iterator<TaggedTimer> iterator = allTags.iterator(); iterator.hasNext(); ) {
			TaggedTimer next = iterator.next();
			if(next.getTagName().compareTo(tagName) == 0)
				return next;
		}
		return null;
	}
}
