/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.events;

import javax.swing.Timer;

import fr.lri.swingstates.sm.StateMachine;

/**
 * A virtual event originated on a given position.
 * 
 * @author Caroline Appert
 *
 */
public class VirtualTimerEvent extends VirtualEvent {

	private Timer timer;
	
	/**
	 * Builds a <code>VirtualTimerEvent</code>.
	 * @param timer The timer.
	 */
	public VirtualTimerEvent(Timer timer) {
		super(StateMachine.TIME_OUT, timer);
		this.timer = timer;
	}
	
	/**
	 * @return The timer that originated this event.
	 */
	public Timer getTimer() {
		return timer;
	}

}
