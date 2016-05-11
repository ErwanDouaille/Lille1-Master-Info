/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.util.EventObject;

/**
 * <p>The listener interface for receiving events fired by state machines. (An event is fired
 * by a state machine when its method <code>fireEvent</code> has been called). </p>
 * <p>
 * The class that is interested in processing events fired by a state machine implements this interface 
 * (and the method it contains).
 * </p>
 * <p>
 * The listener object created from that class is then registered with a state machine <code>sm</code> using 
 * the <code>addStateMachineListener</code> method on <code>sm</code>.
 * <p>
 * 
 * <p> The following example is just an illustration of the mechanism and is not very useful. 
 * Note that a state machine is itself a <code>StateMachineListener</code> so a
 * state machine can process events fired by other state machine. To see a more
 * interesting example that uses two state machines, take a look at the pie menu applet example on
 * <a href="http://www.lri.fr/~appert/SwingStates/">SwingStates web site</a>.
 * </p>
 * <pre>
 * CStateMachine sm = new CStateMachine() {
 *    public State start = new State() {
 *       Transition selectColor = new PressOnShape() {
 *       	public void action() {
 *             fireEvent(new VirtualEvent(""+getShape().getFillPaint()));
 *          }
 *       };
 *    };
 * };
 * sm.addStateMachineListener(new StateMachineListener() {
 *    public void eventOccured(EventObject event) {
 *       System.out.println("The pressed shape is filled in "+((VirtualEvent)event).getName());
 *    };
 * });
 * </pre>
 * @see fr.lri.swingstates.debug.StateMachineEvent
 * @see fr.lri.swingstates.debug.StateMachineEventAdapter
 * @author Caroline Appert
 */
public interface StateMachineListener {

	/**
	 * Invoked when the state machine has fired an event.
	 * @param eventObject the event.
	 */
	void eventOccured(EventObject eventObject);
	
}
