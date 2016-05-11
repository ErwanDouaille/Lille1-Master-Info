/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.debug;

import java.util.EventListener;

/**
 * <p>The listener interface for receiving "interesting" state machine events:
 * <ul>
 * <li>the state machine is reset,
 * <li>the state machine is resumed,
 * <li>the state machine is suspended,
 * <li>the state machine fires a transition changing the current state,
 * <li>the state machine fires a transition making it loop on the current state.
 * </ul>
 * </p>
 * <p>
 * The class that is interested in processing a state machine event either implements this interface 
 * (and all the methods it contains) or extends the abstract <code>StateMachineEventAdapter</code> class 
 * (overriding only the methods of interest).
 * </p>
 * <p>
 * The listener object created from that class is then registered with a state machine using the state machine's addStateMachineListener method. 
 * When the state machine is resumed, reset, suspended, goes to another state or loops on the current state,
 * the relevant method in the listener object is invoked and the StateMachineEvent is passed to it. </p>
 * @see fr.lri.swingstates.debug.StateMachineEvent
 * @see fr.lri.swingstates.debug.StateMachineEventAdapter
 * @author Caroline Appert
 */
public interface StateMachineEventListener extends EventListener {

	/**
	 * Invoked when the state machine has been suspended.
	 * @param e the event.
	 */
	void smSuspended(StateMachineEvent e);
	/**
	 * Invoked when the state machine has been resumed.
	 * @param e the event.
	 */
	void smResumed(StateMachineEvent e);
	/**
	 * Invoked when the state machine has been reset.
	 * @param e the event.
	 */
	void smReset(StateMachineEvent e);
	/**
	 * Invoked when the state machine has fired a transition changing the current state.
	 * @param e the event.
	 */
	void smStateChanged(StateMachineEvent e);
	/**
	 * Invoked when the state machine has fired a transition making it loop on the current state.
	 * @param e the event.
	 */
	void smStateLooped(StateMachineEvent e);
	
	/**
	 * Invoked when the state machine has been inited.
	 * @param e the event.
	 */
	void smInited(StateMachineEvent e);
	
}
