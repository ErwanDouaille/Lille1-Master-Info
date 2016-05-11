package fr.lri.swingstates.sm.transitions;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import fr.lri.swingstates.sm.BasicInputStateMachine;

/**
 * A transition triggered by a mouse clicked event.
 * A click is defined as a quick succession of mouse press and mouse release, without significant motion in between.
 * Note that the mouse press and mouse release events are always sent, even when a mouse click event is sent.
 * @author Caroline Appert
 */
public class Click extends MouseOnPosition {
	
	/**
	 * Builds a transition triggered by a mouse clicked event with any modifier and any button.
	 * @param outState The name of the output state
	 */
	public Click(String outState){
		super(BasicInputStateMachine.ANYBUTTON, outState);
	}
	
	/**
	 * Builds a transition triggered by a mouse clicked event with any modifier and any button that loops on the current state.
	 */
	public Click(){
		super(BasicInputStateMachine.ANYBUTTON);
	}
	
	/**
	 * Builds a transition triggered by a mouse clicked event with any modifier that loops on the current state.
	 * @param b The button of the mouse event: BasicInputStateMachine.NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 */
	public Click(int b) {
		super(b);
	}
	
	/**
	 * Builds a transition triggered by a mouse clicked event that loops on the current state.
	 * @param b The button of the mouse event: BasicInputStateMachine.NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 */
	public Click(int b, int m) {
		super(b, m);
	}
	
	/**
	 * Builds a transition with any modifier triggered by a mouse clicked event.
	 * @param b The button of the mouse event: BasicInputStateMachine.NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 * @param outState The name of the output state
	 */
	public Click(int b, String outState) {
		super(b, BasicInputStateMachine.ANYMODIFIER, outState);
	}
	
	/**
	 * Builds a transition triggered by a mouse clicked event.
	 * @param b The button of the mouse event: BasicInputStateMachine.NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 * @param outState The name of the output state
	 */
	public Click(int b, int m, String outState) {
		super(b, m, outState);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		return matches(eventObject, MouseEvent.MOUSE_CLICKED);
	}
}
