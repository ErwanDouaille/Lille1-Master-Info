/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


/**
 * A state machine to handle basic input events (mouse events and keyboard events).
 * 
 * <p> The complete list of event types of a BasicInputStateMachine is:
 * <ul>
 * <li> <code>Press</code>: pressing a mouse button anywhere;
 * <li> <code>Release</code>: releasing a mouse button anywhere;
 * <li> <code>Click</code>: clicking (pressing and releasing in quick succession) a mouse button anywhere;
 * <li> <code>Move</code>: moving the mouse with no button pressed anywhere;
 * <li> <code>Drag</code>: moving the mouse with a button pressed anywhere;
 * <li> <code>KeyPress, KeyRelease, KeyType</code>: typing a key (pressing, releasing, press then release in quick succession);
 * <li> <code>TimeOut</code>: delay specified by armTimer expired.
 * <li> <code>Event</code>: high-level events
 * </ul>
 * An event type in a state machine is the name of the class of a transition:
 * <pre>
 * 	// declare a transition to state s2 when pressing the left mouse button..
 * 	Transition t = new Press (BUTTON1) {
 * 		...
 * 	}
 * </pre>
 * </p>
 * <p>
 * A state machine implements <code>MouseListener</code>, <code>MouseWheelListener</code>, <code>MouseMotionListener</code> and <code>KeyListener</code>, 
 * it can be attached to any awt component to control it:
 * <pre>
 * 	JPanel panel = new JPanel();
 * 	BasicInputStateMachine sm = new BasicInputStateMachine() {
 * 		Point2D pt;
 *		public State start = new State() {
 *			Transition press = new Press(BUTTON1, ">> pressed") {
 *				public void action() {
 *					pt = getPoint();
 *					armTimer(500, false);
 *				}
 *			};
 *		};
 *			
 *		public State pressed = new State() {
 *			Transition timeOut = new TimeOut(">> start") {
 *				public void action() {
 *					System.out.println("long press at: "+pt.getX()+", "+pt.getY());
 *				}
 *			};
 *			Transition release = new Release(BUTTON1, ">> start") { };
 *		};
 *	};
 *		
 *	sm.addAsListenerOf(panel);
 * </pre>
 * </p>
 * @see fr.lri.swingstates.sm.StateMachine
 * @author Caroline Appert
 *
 */
public class BasicInputStateMachine extends StateMachine implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
	
	/**
	 * Specifies that the mouse must have all buttons released.
	 */
	public static final int NOBUTTON = MouseEvent.NOBUTTON;
	/**
	 * Specifies that the mouse must have any or no button pressed.
	 */
	public static final int ANYBUTTON = -1;
	/**
	 * Specifies that the mouse must have the button 1 pressed.
	 */
	public static final int BUTTON1 = MouseEvent.BUTTON1;
	/**
	 * Specify that the mouse must have the button 2 pressed.
	 */
	public static final int BUTTON2 = MouseEvent.BUTTON2;
	/**
	 * Specifies that the mouse must have the button 3 pressed.
	 */
	public static final int BUTTON3 = MouseEvent.BUTTON3;
	
	/**
	 * Specifies that no keyboard modifiers must be pressed.
	 */
	public static final int NOMODIFIER = 0;
	/**
	 * Specifies that the SHIFT keyboard modifier must be pressed.
	 */
	public static final int SHIFT = 1;
	/**
	 * Specifies that the CONTROL keyboard modifier must be pressed.
	 */
	public static final int CONTROL = 2;
	/**
	 * Specifies that the ALT keyboard modifier must be pressed.
	 */
	public static final int ALT = 3;
	/**
	 * Specifies that the CONTROL and SHIFT keyboard modifiers must be pressed.
	 */
	public static final int CONTROL_SHIFT = 4;
	/**
	 * Specifies that the ALT and SHIFT keyboard modifiers must be pressed.
	 */
	public static final int ALT_SHIFT = 5;
	/**
	 * Specifies that the ALT and CONTROL keyboard modifiers must be pressed.
	 */
	public static final int ALT_CONTROL = 6;
	/**
	 * Specifies that the ALT, CONTROL and SHIFT keyboard modifiers must be pressed.
	 */
	public static final int ALT_CONTROL_SHIFT = 7;
	/**
	 * Specifies that any keyboard modifiers can be pressed.
	 */
	public static final int ANYMODIFIER = 8;

	/**
	 * Builds a state machine that handles basic input events. 
	 */
	public BasicInputStateMachine() {
		super();
	}
	
	/**
	 * Installs this <code>BasicInputStateMachine</code> 
	 * as a listener of a given graphical component.
	 * @param c The graphical component
	 * @return This <code>BasicInputStateMachine</code>.
	 */
	public BasicInputStateMachine addAsListenerOf(Component c) {
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addMouseWheelListener(this);
		c.addKeyListener(this);
		return this;
	}
	
	/**
	 * Uninstalls this <code>BasicInputStateMachine</code> 
	 * as a listener of a given graphical component.
	 * @param c The graphical component
	 * @return This <code>BasicInputStateMachine</code>.
	 */
	public BasicInputStateMachine removeAsListenerOf(Component c) {
		c.removeMouseListener(this);
		c.removeMouseMotionListener(this);
		c.removeMouseWheelListener(this);
		c.removeKeyListener(this);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(MouseEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent arg0) { 
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(MouseEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseDragged(MouseEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseMoved(MouseEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent arg0) { 
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent arg0) {
		processEvent(arg0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent arg0) {
		processEvent(arg0);	
	}
	
}
