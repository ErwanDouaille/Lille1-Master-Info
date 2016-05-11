package fr.lri.swingstates.sm.transitions;

import java.awt.event.KeyEvent;
import java.util.EventObject;

/**
 * A transition triggered by a key typed event.
 * A key typed event corresponds to the succession of a key press and a key release.
 * Note however that the key press and key release are always sent.
 * Keys can be specified either by the ASCII character they represent or by their keycode.
 * @author Caroline Appert
 */
public class KeyType extends KeyTransition {
	
	/**
	 * Builds a transition triggered by a key typed event with any modifier.
	 * @param outState The name of the output state
	 */
	public KeyType(String outState){
		super(outState);
	}
	
	/**
	 * Builds a transition triggered by a key typed event with any modifier that loops on the current state.
	 */
	public KeyType(){
		super();
	}
	
	/**
	 * Builds a transition triggered by a key typed event with any modifier.
	 * @param character The char corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyType(char character, String outState){
		super(character, outState);
	}
	
	/**
	 * Builds a transition triggered by a key typed event with any modifier.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 * @param outState The name of the output state
	 */
	public KeyType(int keyCode, String outState){
		super(keyCode, outState);
	}
	
	/**
	 * Builds a transition triggered by a key typed event with any modifier that loops on the current state.
	 * @param character The char corresponding to the key event
	 */
	public KeyType(char character){
		super(character);
	}
	
	/**
	 * Builds a transition with any modifier triggered by a key typed event that loops on the current state.
	 * @param keyCode The key code (ASCII) corresponding to the key event
	 */
	public KeyType(int keyCode){
		super(keyCode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean matches(EventObject eventObject) {
		return matches(eventObject, KeyEvent.KEY_TYPED);
	}
}
