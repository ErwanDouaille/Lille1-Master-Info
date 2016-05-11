/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.debug;

import javax.swing.event.EventListenerList;

import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.Transition;


/**
 * A 'watcher' to track a state machine. It registers listeners for <code>StateMachineEvent</code>s, i.e. 
 * when the state machine:
 * <ul>
 * <li> has just been suspended, 
 * <li> has just been resumed, 
 * <li> has just been reset, 
 * <li> has just been inited,
 * <li> has just changed state or
 * <li> has just looped in the current state.
 * </ul>
 * 
 * @author Caroline Appert 
 */
public class Watcher {
	
	private EventListenerList listeners;
	private StateMachine sm;
	
	/**
	 * Builds a 'watcher' for a state machine.
	 * @param stateMachine The state machine to watch.
	 */
	public Watcher(StateMachine stateMachine) {
		sm = stateMachine;
	}

	/**
	 * Registers a listener for StateMachineEvents.
	 * @param l The listener to register.
	 */
	public synchronized void addSMEventListener(StateMachineEventListener l) {
		if (listeners == null)
			listeners = new EventListenerList();
		listeners.add(StateMachineEventListener.class, l);
	}  
	
	/**
	 * Removes a listener for SMEvents.
	 * @param l The listener to remove.
	 */
	public synchronized void removeSMEventListener(StateMachineEventListener l) {
		if (listeners != null)
			listeners.remove(StateMachineEventListener.class, l);
	}
	
	/**
	 * Fires a StateMachineEvent to all registered listeners notifying them the state machine has been suspended.
	 */
	public void fireSmSuspended() {
		Object[] tabListeners = listeners.getListenerList();
	     for (int i = tabListeners.length-2; i>=0; i-=2)
	         if (tabListeners[i]==StateMachineEventListener.class)
	             ((StateMachineEventListener)tabListeners[i+1]).smSuspended(new StateMachineEvent(sm, null, sm.getCurrentState(), sm.getCurrentState()));
	}
	
	/**
	 * Fires a StateMachineEvent to all registered listeners notifying them the state machine has been resumed.
	 */
	public void fireSmResumed() {
		Object[] tabListeners = listeners.getListenerList();
	     for (int i = tabListeners.length-2; i>=0; i-=2)
	         if (tabListeners[i]==StateMachineEventListener.class)
	             ((StateMachineEventListener)tabListeners[i+1]).smResumed(new StateMachineEvent(sm, null, sm.getCurrentState(), sm.getCurrentState()));
	}
	
	/**
	 * Fires a StateMachineEvent to all registered listeners notifying them the state machine has been restarted.
	 * @param previousState The active state before this machine being reset
	 */
	public void fireSmReset(State previousState) {
		Object[] tabListeners = listeners.getListenerList();
	     for (int i = tabListeners.length-2; i>=0; i-=2)
	         if (tabListeners[i]==StateMachineEventListener.class)
	             ((StateMachineEventListener)tabListeners[i+1]).smReset(new StateMachineEvent(sm, null, previousState, sm.getInitialState()));
	}
	
	/**
	 * Fires a StateMachineEvent to all registered listeners notifying them the state machine has passed in another state.
	 * @param t The transition that has just been fired.
	 * @param previousState The state before the transition.
	 * @param currentState The current state (after the transition).
	 */
	public void fireStateChanged(Transition t, State previousState, State currentState) {
		Object[] tabListeners = listeners.getListenerList();
	     for (int i = tabListeners.length-2; i>=0; i-=2)
	         if (tabListeners[i]==StateMachineEventListener.class)
	             ((StateMachineEventListener)tabListeners[i+1]).smStateChanged(new StateMachineEvent(sm, t, previousState, currentState));
	}
	
	/**
	 * Fires a StateMachineEvent to all registered listeners notifying them the state machine has passed a transition but looped in its current state.
	 * @param t The transition that has just been fired.
	 * @param state The current state.
	 */
	public void fireStateLooped(Transition t, State state) {
		Object[] tabListeners = listeners.getListenerList();
	     for (int i = tabListeners.length-2; i>=0; i-=2)
	         if (tabListeners[i]==StateMachineEventListener.class)
	             ((StateMachineEventListener)tabListeners[i+1]).smStateLooped(new StateMachineEvent(sm, t, state));
	}
	
	/**
	 * Fires a StateMachineEvent to all registered listeners notifying them the state machine has just been inited.
	 */
	public void fireSMInited() {
		Object[] tabListeners = listeners.getListenerList();
	     for (int i = tabListeners.length-2; i>=0; i-=2)
	         if (tabListeners[i]==StateMachineEventListener.class)
	             ((StateMachineEventListener)tabListeners[i+1]).smInited(new StateMachineEvent(sm));
	}
	
}
