package fr.lri.swingstates.sm.transitions;

import java.awt.event.KeyEvent;
import java.util.EventObject;

/**
 * A transition triggered by a key released event.
 * Keys can be specified either by the ASCII character they represent or by their keycode.
 * @author Caroline Appert
 */
public class KeyRelease extends KeyTransition {
	
	/**
	 * Builds a transition triggered by a key released event with any modifier.
	 * @param outState The name of the output state
	 */
	public KeyRelease(String outState){
		super(outState);
	}
	
	/**
	 * Builds a transition triggered by a key released event with any modifier that loops on the current state.
	 */
	public KeyRelease(){
		super();
	}
	
	/**
	 * Builds a transition triggered by a key released event with any modifier.
	 * @param character The char corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyRelease(char character, String outState){
		super(character, outState);
	}
	
	/**
	 * Builds a transition triggered by a key released event with any modifier.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyRelease(int keyCode, String outState){
		super(keyCode, outState);
	}
	
	/**
	 * Builds a transition triggered by a key released event with any modifier that loops on the current state.
	 * @param character The char corresponding to the key event
	 */
	public KeyRelease(char character){
		super(character);
	}
	
	/**
	 * Builds a transition triggered by a key released event with any modifier that loops on the current state.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 */
	public KeyRelease(int keyCode){
		super(keyCode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		return matches(eventObject, KeyEvent.KEY_RELEASED);
	}
	
}
