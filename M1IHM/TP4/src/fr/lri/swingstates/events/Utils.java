/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.events;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import fr.lri.swingstates.sm.BasicInputStateMachine;

/**
 * A class containing utility methods.
 * @author Caroline Appert
 *
 */
public class Utils {

	private Utils() { }
	
	/**
	 * Returns the string describing this awt key event ("KeyType", "KeyPress" or "KeyRelease").
	 * @param e The awt key event.
	 * @return The string describing this awt key event.
	 */
	public static String getPrefixInputEvent(KeyEvent e) {
		String event = "";
		KeyEvent ke = e;
		switch(ke.getID()){
		case KeyEvent.KEY_TYPED :
			event += "KeyType";
			break;
		case KeyEvent.KEY_PRESSED :
			event += "KeyPress";
			break;
		case KeyEvent.KEY_RELEASED :
			event += "KeyRelease";
			break;
		default : 
			System.out.println("Unknow event type: "+ke.getID());
		}
		return event;
	}
	
	/**
	 * Returns the string describing this awt mouse event 
	 * ("Down", "Move", "Drag", "Release", "Click", "Enter", "Leave" or "Wheel") 
	 * concatenated with the number of the mouse button (0, 1 or 2).
	 * @param me The awt mouse event.
	 * @return The string describing this awt mouse event.
	 */
	public static String getPrefixInputEvent(MouseEvent me) {
		String event = "";
		switch(me.getID()){
		case MouseEvent.MOUSE_PRESSED :
			event += "Down";
			break;
		case MouseEvent.MOUSE_MOVED :
			event += "Move";
			break;
		case MouseEvent.MOUSE_DRAGGED :
			event += "Drag";
			break;
		case MouseEvent.MOUSE_RELEASED :
			event += "Release";
			break;
		case MouseEvent.MOUSE_CLICKED :
			event += "Click";				
			break;
		case MouseEvent.MOUSE_ENTERED :
			event += "Enter";
			break;
		case MouseEvent.MOUSE_EXITED :
			event += "Leave";
			break;
		case MouseEvent.MOUSE_WHEEL :
			event += "Wheel";
		default :
			System.out.println("Unknow event type: "+me.getID());
		}
		event+=button(me);
		return event;
	}
	
	/**
	 * Returns the modifier of an input event :
	 * <ul>
	 * <li> BasicInputStateMachine.NOMODIFIER
	 * <li> BasicInputStateMachine.ALT
	 * <li> BasicInputStateMachine.SHIFT
	 * <li> BasicInputStateMachine.CONTROL
	 * <li> BasicInputStateMachine.CONTROL_SHIFT
	 * <li> BasicInputStateMachine.ALT_CONTROL
	 * <li> BasicInputStateMachine.ALT_SHIFT
	 * <li> BasicInputStateMachine.ALT_CONTROL_SHIFT
	 * </ul>.
	 * @param e The awt input event.
	 * @return The modifier of the event <code>e</code>.
	 */
	public static int modifiers(InputEvent e){
		if(e.isControlDown()) {
			if(e.isShiftDown()) {
				if(e.isAltDown()) return BasicInputStateMachine.ALT_CONTROL_SHIFT;
				return BasicInputStateMachine.CONTROL_SHIFT;
			}
			if(e.isAltDown()) return BasicInputStateMachine.ALT_CONTROL;
			return BasicInputStateMachine.CONTROL;
		}
		if(e.isShiftDown()) {
			if(e.isAltDown()) return BasicInputStateMachine.ALT_SHIFT;
			return BasicInputStateMachine.SHIFT;
		}
		if(e.isAltDown()) return BasicInputStateMachine.ALT;
		return BasicInputStateMachine.NOMODIFIER;
	}

	/**
	 * Returns the button of an input mouse event :
	 * <ul>
	 * <li> BasicInputStateMachine.NOBUTTON
	 * <li> BasicInputStateMachine.BUTTON1
	 * <li> BasicInputStateMachine.BUTTON2
	 * <li> BasicInputStateMachine.BUTTON3
	 * </ul>.
	 * @param e The awt input event.
	 * @return The button of the event <code>e</code>.
	 */
	public static int button(InputEvent e){
		int whichButton=e.getModifiers();
		if ((whichButton & InputEvent.BUTTON1_MASK)==InputEvent.BUTTON1_MASK)
			return BasicInputStateMachine.BUTTON1;
		else
			if ((whichButton & InputEvent.BUTTON2_MASK)==InputEvent.BUTTON2_MASK)
				return BasicInputStateMachine.BUTTON2;
			else 
				if ((whichButton & InputEvent.BUTTON3_MASK)==InputEvent.BUTTON3_MASK)
					return BasicInputStateMachine.BUTTON3;
				else
					return BasicInputStateMachine.NOBUTTON;
	} 
	
	/**
	 * Returns the text corresponding to a button for toString methods in Transition classes.
	 * e.g. 
	 * <pre>
	 * getButtonAsText(BasicInputStateMachine.BUTTON1)
	 * </pre>
	 * returns "BUTTON1".
	 * @param button The button
	 * @return The corresponding text
	 */
	public static String getButtonAsText(int button) {
		switch(button) {
		case BasicInputStateMachine.BUTTON1 : return "BUTTON1";
		case BasicInputStateMachine.BUTTON2 : return "BUTTON2";
		case BasicInputStateMachine.BUTTON3 : return "BUTTON3";
		case BasicInputStateMachine.ANYBUTTON : return "ANYBUTTON";
		default : return "NOBUTTON";
		}
	}
	
	/**
	 * Returns the text corresponding to a modifier for toString methods in Transition classes.
	 * e.g. 
	 * <pre>
	 * getModifiersAsText(BasicInputStateMachine.SHIFT)
	 * </pre>
	 * returns "SHIFT".
	 * @param modifier The modifier
	 * @return The corresponding text
	 */
	public static String getModifiersAsText(int modifier) {
		switch(modifier) {
		case BasicInputStateMachine.ALT_CONTROL_SHIFT : return "ALT_CONTROL_SHIFT";
		case BasicInputStateMachine.CONTROL_SHIFT : return "CONTROL_SHIFT";
		case BasicInputStateMachine.ALT_CONTROL : return "ALT_CONTROL";
		case BasicInputStateMachine.ALT_SHIFT : return "ALT_SHIFT";
		case BasicInputStateMachine.CONTROL : return "CONTROL";
		case BasicInputStateMachine.ALT : return "ALT";
		case BasicInputStateMachine.SHIFT : return "SHIFT";
		default : return "NOMODIFIER";
		}
	}
	
}
