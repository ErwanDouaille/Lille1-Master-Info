package fr.lri.swingstates.sm.transitions;

import java.awt.event.KeyEvent;
import java.util.EventObject;

/**
 * A transition triggered by a key pressed event.
 * Keys can be specified either by the ASCII character they represent or by their keycode.
 * @author Caroline Appert
 */
public class KeyPress extends KeyTransition {
	
	/**
	 * Builds a transition triggered by a key pressed event with any modifier.
	 * @param outState The name of the output state
	 */
	public KeyPress(String outState){
		super(outState);
	}
	
	/**
	 * Builds a transition triggered by a key pressed event with any modifier that loops on the current state.
	 */
	public KeyPress(){
		super();
	}
	
	/**
	 * Builds a transition triggered by a key pressed event with any modifier.
	 * @param character The char corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyPress(char character, String outState){
		super(character, outState);
	}
	
	/**
	 * Builds a transition triggered by a key pressed event with any modifier.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyPress(int keyCode, String outState){
		super(keyCode, outState);
	}
	
	/**
	 * Builds a transition triggered by a key pressed event with any modifier that loops on the current state.
	 * @param character The char corresponding to the key event
	 */
	public KeyPress(char character){
		super(character);
	}
	
	/**
	 * Builds a transition triggered by a key pressed event with any modifier that loops on the current state.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 */
	public KeyPress(int keyCode){
		super(keyCode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		return matches(eventObject, KeyEvent.KEY_PRESSED);
	}
}
