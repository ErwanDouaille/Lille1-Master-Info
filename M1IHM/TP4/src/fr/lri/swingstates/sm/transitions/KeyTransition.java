package fr.lri.swingstates.sm.transitions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import fr.lri.swingstates.sm.Transition;

/**
 * A transition triggered by a key event.
 * Keys can be specified either by the ASCII character they represent or by their keycode.
 * @author Caroline Appert
 */
public abstract class KeyTransition extends Transition {
	
	/**
	 * True if this transition must be initiated by any key event.
	 */
	boolean generic = false;
	
	/**
	 * The char corresponding to the key event.
	 */
	char charCode;
	
	/**
	 * The ASCII key code of the key event. 
	 */
	int keyCode;
	
	boolean key = true; 
	
	/**
	 * Builds a transition triggered by a key event with any modifier.
	 * @param outState The name of the output state
	 */
	public KeyTransition(String outState){
		super(outState);
		generic = true;
	}
	
	/**
	 * Builds a transition triggered by a key event with any modifier that loops on the current state.
	 */
	public KeyTransition(){
		super();
		generic = true;
	}
	
	/**
	 * Builds a transition triggered by a key event with any modifier.
	 * @param character The char corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyTransition(char character, String outState){
		super(outState);
		this.charCode = character;
		key = false;
	}
	
	/**
	 * Builds a transition triggered by a key event with any modifier.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyTransition(int keyCode, String outState){
		super(outState);
		this.keyCode = keyCode;
	}
	
	/**
	 * Builds a transition triggered by a key event with any modifier that loops on the current state.
	 * @param character The char corresponding to the key event
	 */
	public KeyTransition(char character){
		super();
		charCode = character;
		key = false;
	}
	
	/**
	 * Builds a transition triggered by a key event with any modifier that loops on the current state.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 */
	public KeyTransition(int keyCode){
		super();
		this.keyCode = keyCode;
	}
	
	// TODO Add modifiers
	
	/**
	 * Sets the character associated with the key event that triggers this transition.
	 * @param character The character
	 */
	public void setChar(char character){
		charCode = character;
	}
	
	/**
	 * Returns the character associated with the key event that triggers this transition.
	 * @return the character associated with the key event that triggers this transition
	 */
	public char getChar(){
		return charCode;
	}
	
	/**
	 * @param keyCode The key code (ASCII) to associate to the key event that initiates this transition
	 */
	public void setKeyCode(int keyCode){
		this.keyCode = keyCode;
	}
	
	/**
	 * @return the key code (ASCII) associating to the key event that initiates this transition
	 */
	public int getKeyCode(){
		return keyCode;
	}
	
	/**
	 * @return the awt mouse event that fires this transition.
	 */
	public InputEvent getInputEvent() {
		return (InputEvent)triggeringEvent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		if(generic) return getClass().getSuperclass().getSimpleName()+"()";
		else {
			if(key) return getClass().getSuperclass().getSimpleName()+"("+keyCode+")";
			else return getClass().getSuperclass().getSimpleName()+"("+charCode+")";
		}
	}
	
	private boolean matchesKeyOrChar(EventObject eventObject) {
		if(!(eventObject instanceof KeyEvent))
			return false;
		KeyEvent ke = (KeyEvent)eventObject;
		if(generic) {
			keyCode = ke.getKeyCode();
			charCode = ke.getKeyChar();
			return true;
		}
		if(key) {
			if(keyCode == ke.getKeyCode()) {
				charCode = ke.getKeyChar();
				return true;
			}
			return false;
		}
		if(charCode == ke.getKeyChar()) {
			keyCode = ke.getKeyCode();
			return true;
		}
		return false;
	}
	
	protected boolean matches(EventObject eventObject, int typeEvent) {
		if(!(eventObject instanceof KeyEvent))
			return false;
		KeyEvent ke = (KeyEvent)eventObject;
		if(ke.getID() == typeEvent)
			return matchesKeyOrChar(eventObject);
		else return false;
	}
	
}
