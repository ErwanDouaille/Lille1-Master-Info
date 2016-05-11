package fr.lri.swingstates.sm.transitions;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.EventObject;

import fr.lri.swingstates.events.Utils;
import fr.lri.swingstates.sm.BasicInputStateMachine;

/**
 * A transition triggered by a mouse event on a location.
 * The transition is specified by a button and modifiers.
 * The position of the mouse when the transition fired can be retrieved.
 * @author Caroline Appert
 */
public abstract class MouseOnPosition extends EventOnPosition {
	
	/**
	 * The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3.
	 */
	int button = BasicInputStateMachine.BUTTON1;
	
	/**
	 * The modifier : NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT.
	 */
	int modifier = BasicInputStateMachine.ANYMODIFIER;
	
	/**
	 * Builds a mouse transition with any modifier.
	 * @param outState The name of the output state
	 */
	public MouseOnPosition(String outState){
		this(BasicInputStateMachine.ANYBUTTON, outState);
	}
	
	/**
	 * Builds a mouse transition with any modifier.
	 */
	public MouseOnPosition(){
		this(BasicInputStateMachine.ANYBUTTON);
	}
	
	/**
	 * Builds a mouse transition.
	 * @param b The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 * @param outState The name of the output state
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 */
	public MouseOnPosition(int b, int m, String outState){
		super((String)null, outState);
		modifier = m;
		button = b;
	}
	
	
	/**
	 * Builds a mouse transition with any modifier.
	 * @param b The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 * @param outState The name of the output state
	 */
	public MouseOnPosition(int b, String outState){
		super((String)null, outState);
		button = b;
	}
	
	/**
	 * Builds a mouse transition that loops on the current state.
	 * @param b The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 * @param m The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 */
	public MouseOnPosition(int b, int m){
		super((String)null);
		modifier = m;
		button = b;
	}
	
	/**
	 * Builds a mouse transition with any modifier that loops on the current state.
	 * @param b The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
	 */
	public MouseOnPosition(int b){
		super((String)null);
		button = b;
	}
	
	
	/**
	 * Returns the button of the mouse event that fires this transition.
	 * @return the button of the mouse event that fires this transition (NOBUTTON, BUTTON1, BUTTON2 or BUTTON3).
	 */
	public int getButton(){
		return button;
	}
	
	/**
	 * Returns the modifier of the mouse event that fires this transition.
	 * @return the modifier : NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
	 */
	public int getModifier() {
		return modifier;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getClass().getSuperclass().getSimpleName()+"("+Utils.getButtonAsText(button)+","+Utils.getModifiersAsText(modifier)+")";
	}
	
	/**
	 * @return the awt mouse event that fires this transition.
	 */
	public InputEvent getInputEvent() {
		return (InputEvent)triggeringEvent;
	}
	
	/**
	 * @return the awt mouse event that fires this transition.
	 */
	public MouseEvent getMouseEvent() {
		return (MouseEvent) triggeringEvent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Point2D getPoint() {
		return ((MouseEvent)triggeringEvent).getPoint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		if(eventObject instanceof MouseEvent) {
			return true;
		}
		return false;
	}
	
	protected boolean matches(EventObject eventObject, int typeEvent) {
		if(!(eventObject instanceof MouseEvent)) return false;
		MouseEvent me = (MouseEvent)eventObject;
		if((me.getID() == typeEvent)
			&& (modifier == Utils.modifiers(me) || modifier == BasicInputStateMachine.ANYMODIFIER)
			&& (button == Utils.button(me) || button == BasicInputStateMachine.ANYBUTTON)) {
			return true;
		}
		return false;
	}
	
}
