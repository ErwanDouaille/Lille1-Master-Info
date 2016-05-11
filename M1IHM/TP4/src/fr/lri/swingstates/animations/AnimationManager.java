/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.Timer;

/**
 * An <code>AnimationManager</code> manages a thread to play
 * a list of <code>Animation</code>s.
 * 
 * @author Caroline Appert
 *
 */
class AnimationManager {

	private static AnimationManager manager;
	
	protected int 				  delay = 40;
	protected long 				  duration = 1000;
	protected long 				  currentTime = 0;
	protected Timer 			  clock = null;
	
	private ConcurrentLinkedQueue<Animation> allAnimations = new ConcurrentLinkedQueue<Animation>();
	
	private AnimationManager() {
		currentTime = System.currentTimeMillis();
		clock = new Timer(40, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentTime+=delay;
				Animation next;
				for(Iterator<Animation> i = allAnimations.iterator(); i.hasNext(); ) {
					next = i.next();
					if(!next.isSuspended()) next.update();
				}
			}
		});
		clock.start();
	}
	
	public static AnimationManager getInstance() {
		if(manager == null) manager = new AnimationManager();
		return manager;
	}
	
	public static void reset() {
		if(manager != null) {
			manager.clock.stop();
			manager.allAnimations.clear();
			ANamedTag.namedTags = null;
		}
		manager = new AnimationManager();
	}
	
	public void addAnim(Animation a) {
		if(!allAnimations.contains(a))
			allAnimations.add(a);
	}
	
	public void removeAnim(Animation a) {
		allAnimations.remove(a);
	}
	
	public long getCurrentTime() {
		return currentTime;
	}
	
	void setCurrentTime(long ct) {
		currentTime = ct;
	}
	
	
	
}


