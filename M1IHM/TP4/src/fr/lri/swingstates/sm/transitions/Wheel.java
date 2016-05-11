package fr.lri.swingstates.sm.transitions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EventObject;

import fr.lri.swingstates.sm.BasicInputStateMachine;

/**
 * A transition triggered by a mouse wheel event with no button pressed.
 * @author Caroline Appert
 */
public class Wheel extends MouseOnPosition {
	
	/**
	 * Builds a transition triggered by a mouse wheel event with any modifier that loops on the current state.
	 */
	public Wheel() {
		super(BasicInputStateMachine.NOBUTTON);
	}
	
	/**
	 * Builds a transition triggered by a mouse wheel event that loops on the current state.
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 */
	public Wheel(int m) {
		super(BasicInputStateMachine.NOBUTTON, m);
	}
	
	/**
	 * Builds a transition triggered by a mouse wheel event with any modifier.
	 * @param outState The name of the output state
	 */
	public Wheel(String outState) {
		super(BasicInputStateMachine.NOBUTTON, BasicInputStateMachine.ANYMODIFIER, outState);
	}
	
	/**
	 * Builds a transition triggered by a mouse wheel event.
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 * @param outState The name of the output state
	 */
	public Wheel(int m, String outState) {
		super(BasicInputStateMachine.NOBUTTON, m, outState);
	}
	
	/**
	 * @return the number of units that should be scrolled in response to this event.
	 * @see java.awt.event.MouseWheelEvent#getScrollAmount()
	 */
	public int getScrollAmount() {
		return ((MouseWheelEvent)triggeringEvent).getWheelRotation();
	}
	
	/**
	 * @return the type of scrolling that should take place in response to this event.
	 * @see java.awt.event.MouseWheelEvent#getScrollType()
	 */
	public int getScrollType() {
		return ((MouseWheelEvent)triggeringEvent).getWheelRotation();
	}
	
	/**
	 * @return This is a convenience method to aid in the implementation of the common-case MouseWheelListener 
	 * - to scroll a ScrollPane or JScrollPane by an amount which conforms to the platform settings.
	 * @see java.awt.event.MouseWheelEvent#getUnitsToScroll()
	 */
	public int getUnitsToScroll() {
		return ((MouseWheelEvent)triggeringEvent).getWheelRotation();
	}
	
	/**
	 * @return the number of "clicks" the mouse wheel was rotated.
	 * @see java.awt.event.MouseWheelEvent#getWheelRotation()
	 */
	public int getWheelRotation() {
		return ((MouseWheelEvent)triggeringEvent).getWheelRotation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		return matches(eventObject, MouseEvent.MOUSE_WHEEL);
	}
}
