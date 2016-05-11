package fr.lri.swingstates.sm.transitions;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import fr.lri.swingstates.sm.BasicInputStateMachine;

/**
 * A transition triggered by a mouse moved event with no button pressed.
 * @author Caroline Appert
 */
public class Move extends MouseOnPosition {
	
	/**
	 * Builds a transition triggered by a mouse moved event with any modifier that loops on the current state.
	 */
	public Move() {
		super(BasicInputStateMachine.NOBUTTON);
	}
	
	/**
	 * Builds a transition triggered by a mouse moved event that loops on the current state.
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 */
	public Move(int m) {
		super(BasicInputStateMachine.NOBUTTON, m);
	}
	
	/**
	 * Builds a transition triggered by a mouse moved event with any modifier.
	 * @param outState The name of the output state
	 */
	public Move(String outState) {
		super(BasicInputStateMachine.NOBUTTON, BasicInputStateMachine.ANYMODIFIER, outState);
	}
	
	/**
	 * Builds a transition triggered by a mouse moved event.
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 * @param outState The name of the output state
	 */
	public Move(int m, String outState) {
		super(BasicInputStateMachine.NOBUTTON, m, outState);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		return matches(eventObject, MouseEvent.MOUSE_MOVED);
	}
}