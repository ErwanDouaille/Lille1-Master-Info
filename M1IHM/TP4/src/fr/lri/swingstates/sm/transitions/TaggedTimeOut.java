package fr.lri.swingstates.sm.transitions;

import java.util.EventObject;

import fr.lri.swingstates.events.VirtualTimerEvent;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.TaggedTimer;

/**
 * A transition triggered by a tagged timer.
 * The timer is armed by the <code>armTimer</code> 
 * method in <code>StateMachine</code>.
 * @author Caroline Appert
 * @see StateMachine#armTimer(String, int, boolean)
 */
public class TaggedTimeOut extends TimeOut {
	
	private String tag;
	
	/**
	 * Builds a transition triggered by a time out event originated by a tagged timer.
	 * @param tag the tag of the timer.
	 * @param outState The name of the output state
	 */
	public TaggedTimeOut(String tag, String outState) {
		super(outState);
		this.tag = tag;
	}
	
	/**
	 * Builds a transition triggered by a time out event originated by a tagged timer.
	 * @param tag the tag of the timer.
	 */
	public TaggedTimeOut(String tag) {
		super();
		this.tag = tag;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		if (super.matches(eventObject) && (((VirtualTimerEvent)eventObject).getTimer() instanceof TaggedTimer))
			return ((TaggedTimer)((((VirtualTimerEvent)eventObject).getTimer()))).getTagName().compareTo(tag) == 0;
		return false;
	}
	
}
