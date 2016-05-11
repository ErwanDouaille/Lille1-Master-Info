/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.debug;

/**
 * <p>An abstract adapter class for receiving state machine events. 
 * The methods in this class are empty. This class exists as convenience for creating listener objects. </p>
 * <p> State machine events let you track when a state machine is reset, resumed, suspended or when a state machine passed a transition
 * leading it to a new state or letting it in the current state. </p>
 * <p> Extend this class to create a StateMachineEvent listener and override the methods for the events of interest. 
 * (If you implement the StateMachineEventListener interface, you have to define all of the methods in it. 
 * This abstract class defines null methods for them all, so you can only have to define methods for events you care about.)</p>
 * <p>Create a listener object using the extended class and then register it with a state machine using the state machine's addSMListener method. 
 * When the state machine is attached, detached, resumed, reset, suspended, goes to another state or loops on the current state,
 * the relevant method in the listener object is invoked and the StateMachineEvent is passed to it. </p>
 * @see fr.lri.swingstates.debug.StateMachineEvent
 * @see fr.lri.swingstates.debug.StateMachineEventListener
 * @author Caroline Appert
 */
public class StateMachineEventAdapter implements StateMachineEventListener {

	/**
	 * {@inheritDoc}
	 */
	public void smSuspended(StateMachineEvent e) { }

	/**
	 * {@inheritDoc}
	 */
	public void smResumed(StateMachineEvent e) { }

	/**
	 * {@inheritDoc}
	 */
	public void smReset(StateMachineEvent e) { }

	/**
	 * {@inheritDoc}
	 */
	public void smStateChanged(StateMachineEvent e) {	}

	/**
	 * {@inheritDoc}
	 */
	public void smStateLooped(StateMachineEvent e) { }

	/**
	 * {@inheritDoc}
	 */
	public void smInited(StateMachineEvent e) {
	}

}
