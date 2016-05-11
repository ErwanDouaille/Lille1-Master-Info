package fr.lri.swingstates.sm.transitions;

import java.util.EventObject;

import fr.lri.swingstates.events.VirtualTimerEvent;
import fr.lri.swingstates.sm.StateMachine;

/**
 * A transition triggered by a timer.
 * The timer is armed by the <code>armTimer</code> 
 * method in <code>StateMachine</code>.
 * @author Caroline Appert
 * @see StateMachine#armTimer(int, boolean)
 */
public class TimeOut extends Event {
	
	/**
	 * Builds a transition triggered by a timer event.
	 */
	public TimeOut() {
		super(StateMachine.TIME_OUT);
	}
	
	/**
	 * Builds a transition triggered by a timer event.
	 * @param outState The name of the output state
	 */
	public TimeOut(String outState) {
		super(StateMachine.TIME_OUT, outState);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return StateMachine.TIME_OUT;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		return (eventObject instanceof VirtualTimerEvent) && super.matches(eventObject);
	}
	
}

