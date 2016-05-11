/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
 */
package fr.lri.swingstates.sm;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import fr.lri.swingstates.events.Picker;
import fr.lri.swingstates.events.PickerEvent;
import fr.lri.swingstates.events.Utils;
import fr.lri.swingstates.sm.transitions.EventOnPosition;

/**
 * A state machine to monitor events with one or more Component.
 * 
 * <p> The complete list of event types, i.e. classes of transitions, of a JStateMachine is:
 * <ul>
 * <li> <code>Press, PressOnComponent, PressOnTag</code>: pressing a mouse button (anywhere / on a Component / on a Component with a given tag);
 * <li> <code>Release, ReleaseOnComponent, ReleaseOnTag</code>: releasing a mouse button (anywhere / on a Component / on a Component with a given tag);
 * <li> <code>Click, ClickOnComponent, ClickOnTag</code>: clicking (pressing and releasing in quick succession) a mouse button (anywhere / on a Component / on a Component with a given tag);
 * <li> <code>Move, MoveOnComponent, MoveOnTag</code>: moving the mouse with no button pressed (anywhere / on a Component / on a Component with a given tag);
 * <li> <code>Drag, DragOnComponent, DragOnTag</code>: moving the mouse with a button pressed (anywhere / on a Component / on a Component with a given tag);
 * <li> <code>EnterOnComponent, EnterOnTag</code>: cursor enters a Component / a Component with a given tag;
 * <li> <code>LeaveOnComponent, LeaveOnTag</code>: cursor leaves a Component / a Component with a given tag;
 * <li> <code>KeyPress, KeyRelease, KeyType</code>: typing a key (pressing, releasing, press then release in quick succession);
 * <li> <code>TimeOut</code>: delay specified by armTimer expired.
 * </ul>
 * </p>
 *  
 * @see fr.lri.swingstates.sm.BasicInputStateMachine
 * @author Caroline Appert
 *
 */
public class JStateMachine extends BasicInputStateMachine implements MouseListener, MouseMotionListener, KeyListener {

	/**
	 * The picker for this state machine.
	 */
	private class JPicker implements Picker {

		private Component  lastPicked = null;
		private Component  picked     = null;

		/**
		 * Builds a <code>JPicker</code>.
		 */
		public JPicker() { }

		public Component pick(JStateMachine machine, Component component, int x, int y) {
			lastPicked = picked;
//			With a JStateMachine, the components that are under the glasspane stays reachable 
//			by input mouse events. That's why we consider the content pane and not the rootpane for picking
			Container parent = null;
			if(component instanceof Container) {
				parent = (Container)component;
				while(parent != null) {
					if(parent instanceof JComponent) {
						parent = ((JComponent)parent).getTopLevelAncestor();
						if(parent instanceof RootPaneContainer) {
							parent = ((RootPaneContainer)parent).getContentPane();
						}
						break;
					} else {
						if(parent.getParent() != null) parent = parent.getParent();
						else break;
					}
				}

			}
			Point point = SwingUtilities.convertPoint(
					component,
					new Point(x, y),
					parent);
			picked = parent.findComponentAt(point);
			return picked;
		}

		/**
		 * @return The previously picked component.
		 */
		public Component getLastPicked() {
			return lastPicked;
		}

		/**
		 * @return The current picked component.
		 */
		public Component getPicked() {
			return picked;
		}

		public Point2D getLocation() { 
			return null; 
		}

		public void move(Point2D location) { }

	}

	private LinkedList<Component> components;

	private JPicker picker = new JPicker();

	/**
	 * Builds a JStateMachine.
	 */
	public JStateMachine() {
		super();
	}

	/**
	 * Attaches a component to this state machine. 
	 * Events coming on this component and its children are catched by this state machine.
	 * If this component has a glasspane, events will still reach the underlying widgets in a <code>JStateMachine</code>
	 * (the glasspane must be set before calling this <code>attachTo</code> method).
	 * @param c The component to attach.
	 * @return This state machine.
	 */
	public StateMachine attachTo(Component c) {
		if(c == null) return this;
		if(components == null)
			components = new LinkedList<Component>();
		if(components.contains(c)) return this;
		if(c instanceof RootPaneContainer) {
			RootPaneContainer rootPane = (RootPaneContainer)c;
			if(rootPane.getContentPane() != null) register(rootPane.getContentPane());
			if(rootPane.getGlassPane() == null) return this;
			rootPane.getGlassPane().addMouseListener(this);
			rootPane.getGlassPane().addMouseWheelListener(this);
			rootPane.getGlassPane().addMouseMotionListener(this);
			rootPane.getGlassPane().addKeyListener(this);
		} else {
			register(c);
		}
		return this;
	}

	/**
	 * Removes a component from the control of this state machine.
	 * @param c The component to detach.
	 * @return This state machine.
	 */
	public StateMachine detachFrom(Component c) {
		if(c instanceof RootPaneContainer) {
			RootPaneContainer rootPane = (RootPaneContainer)c;
			unregister(rootPane.getContentPane());
			if(rootPane.getGlassPane() == null) return this;
			rootPane.getGlassPane().removeMouseListener(this);
			rootPane.getGlassPane().removeMouseWheelListener(this);
			rootPane.getGlassPane().removeMouseMotionListener(this);
			rootPane.getGlassPane().removeKeyListener(this);
		} else {
			unregister(c);
		}
		return this;
	}


	/**
	 * @return The <code>Component</code>s monitored by this state machine as a linked list.
	 */
	public LinkedList getControlledObjects() {
		return components;
	}

	/**
	 * Adds this state machine as a listener of <code>c</code>
	 * and all its children.
	 * @param c The component to register
	 */
	private void register(Component c) {
		if(! components.contains(c)) {
			components.add(c);
			c.addMouseListener(this);
			c.addMouseMotionListener(this);
			c.addMouseWheelListener(this);
			c.addKeyListener(this);
		}
		if(c instanceof Container) {
			int children = ((Container)c).getComponentCount();
			for(int i = 0; i < children; i++)
				register(((Container)c).getComponent(i));
		}
	}

	/**
	 * Removes this state machine as a listener of <code>c</code>
	 * and all its children.
	 * @param c The component to unregister
	 */
	private void unregister(Component c) {
		c.removeMouseListener(this);
		c.removeMouseMotionListener(this);
		c.removeMouseWheelListener(this);
		c.removeKeyListener(this);
		if(components != null) components.remove(c);
		if(c instanceof Container) {
			int children = ((Container)c).getComponentCount();
			for(int i = 0; i < children; i++)
				unregister(((Container)c).getComponent(i));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(MouseEvent e) {
		if(!(e instanceof EventFromGlasspane)) doProcessEvent(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent e) {
		if(!(e instanceof EventFromGlasspane)) doProcessEvent(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent e) {
		if(!(e instanceof EventFromGlasspane)) doProcessEvent(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(MouseEvent e) {
		if(!(e instanceof EventFromGlasspane)) processEvent(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent e) {
		if(!(e instanceof EventFromGlasspane)) processEvent(e);
	}

	private Component getGlassPane(Component component) {
		Container parent = null;
		if(component instanceof Container) {
			parent = (Container)component;
			while(parent != null) {
				if(parent instanceof JComponent) {
					parent = ((JComponent)parent).getTopLevelAncestor();
					if(parent instanceof RootPaneContainer) {
						return ((RootPaneContainer)parent).getGlassPane();
					}
					break;
				} else {
					if(parent.getParent() != null) parent = parent.getParent();
					else break;
				}
			}
		}
		return null;
	}

	/**
	 * A mouse event occuring on the glasspane.
	 * 
	 * @author Caroline Appert
	 *
	 */
	private class EventFromGlasspane extends MouseWheelEvent {
		EventFromGlasspane(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int scrollType, int scrollAmount, int wheelRotation) {
			super(source, id, when, modifiers, x, y, clickCount, popupTrigger, scrollType,
					scrollAmount, wheelRotation);
		}
		EventFromGlasspane(Component source, int id, long when, 
				int modifiers, int x, int y, 
				int clickCount, boolean popupTrigger) {
			super(source, id, when, modifiers, x, y, clickCount, popupTrigger, -1, -1, -1);
		}
		EventFromGlasspane(MouseEvent e) {
			this((Component) e.getSource(), e.getID(), e.getWhen(), 
					e.getModifiers(), e.getX(), e.getY(),
					e.getClickCount(), e.isPopupTrigger());
		}
	}

	private void doProcessEvent(MouseEvent e) {
		Component picked = picker.pick(this, e.getComponent(), e.getX(), e.getY());
		// In awt, the source of a drag is the component where the first mouse pressed occurs
		// In a JStateMachine, if it is a drag event, the source is always the component that is under the cursor
		if(picked != null) {
			Point eventLocation = getPointRelativeToTopLevel(e);
			PickerEvent pickerEvent = e.getID() == MouseEvent.MOUSE_WHEEL 
			? new PickerEvent(picked, picker,
					e.getID(), e.getWhen(), e.getModifiers(),
					(int)eventLocation.getX(), (int)eventLocation.getY(),
					e.getClickCount(), e.isPopupTrigger(),
					((MouseWheelEvent)e).getScrollType(),
					((MouseWheelEvent)e).getScrollAmount(),
					((MouseWheelEvent)e).getWheelRotation())
			: new PickerEvent(picked, picker,
					e.getID(), e.getWhen(), e.getModifiers(),
					(int)eventLocation.getX(), (int)eventLocation.getY(),
					e.getClickCount(), e.isPopupTrigger());
			processEvent(pickerEvent);
		}
		// Redispatch and perform picking for mouse events incoming from glasspane
		// so the widgets covered by the glasspane still receive events.
		// (In Swing, a glasspane intercepts all the events)
		// Class of redispatched events is EventFromGlasspane
		// so we filter this type of event in JStateMachine mouse listeners.
		if(picked != null && picked != e.getComponent() && getGlassPane(e.getComponent()) == e.getComponent()) {
			// process enter/leave
			if (picker.lastPicked != picked) {
				if (picker.lastPicked != null) {
					Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), picker.lastPicked);
					picker.lastPicked.dispatchEvent(new PickerEvent(picker.lastPicked, picker, MouseEvent.MOUSE_EXITED, 
							System.currentTimeMillis(), 0, 
							pt.x, pt.y, 
							0, false));
				}
				if (picked != null) {
					Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), picked);
					picked.dispatchEvent(new PickerEvent(picked, picker, MouseEvent.MOUSE_ENTERED, 
							System.currentTimeMillis(), 0, 
							pt.x, pt.y, 
							0, false));	
				}
			}
			// redispatch to deepest component
			Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), picker.lastPicked);
			picked.dispatchEvent(
					e.getID() == MouseEvent.MOUSE_WHEEL 
					? new EventFromGlasspane(picked,
							e.getID(), e.getWhen(), e.getModifiers(),
							(int)pt.getX(), (int)pt.getY(),
							e.getClickCount(), e.isPopupTrigger(),
							((MouseWheelEvent)e).getScrollType(),
							((MouseWheelEvent)e).getScrollAmount(),
							((MouseWheelEvent)e).getWheelRotation())
					: new EventFromGlasspane(picked,
							e.getID(), e.getWhen(), e.getModifiers(),
							(int)pt.getX(), (int)pt.getY(),
							e.getClickCount(), e.isPopupTrigger()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseDragged(MouseEvent e) {
		if(!(e instanceof EventFromGlasspane)) {
			doProcessEvent(e);		
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseMoved(MouseEvent e) {
		if(!(e instanceof EventFromGlasspane))  doProcessEvent(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent e) {
		processEvent(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent e) {
		processEvent(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent e) {
		processEvent(e);
	}

	Point getPointRelativeToTopLevel(MouseEvent e) {
		Container parent = getContentPane(e.getComponent());
		return SwingUtilities.convertPoint(
				e.getComponent(),
				e.getPoint(),
				parent);
	}
	
	Container getContentPane(Component c) {
		Container parent = null;
		if(c instanceof Container) {
			parent = (Container)c;
			while(parent != null) {
				if(parent instanceof RootPaneContainer) {
					parent = ((RootPaneContainer)parent).getContentPane();
					break;
				}
				if(parent.getParent() != null) parent = parent.getParent();
				else break;
			}
		}
		return parent;
	}

	/**
	 * A transition triggered on a JComponent. 
	 * OnComponent transitions allow developpers to retrieve the component where this transition has been fired:
	 *  <pre>
	 * 	Transition tcomponent = new EventOnComponent("anEvent") {
	 * 		public void action() {
	 * 			// colors in red the component on which the transition has been fired
	 * 			getComponent().setBackground(Color.RED);
	 * 		}
	 * 	}
	 * </pre>
	 * 	
	 * @author Caroline Appert
	 */
	public abstract class EventOnComponent extends EventOnPosition {

//		Component component;

		/**
		 * Builds a transition on a JComponent with no modifier that loops on the current state.
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 */
		public EventOnComponent(String keyEvent) {
			super(keyEvent);
		}

		/**
		 * Builds a transition on a component with no modifier.
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 * @param outState The name of the output state
		 */
		public EventOnComponent(String keyEvent, String outState) {
			super(keyEvent, outState);
		}

		/**
		 * Builds a transition on a JComponent with no modifier that loops on the current state.
		 * This transition can be triggered by any virtual events
		 * whose type is a subclass of <code>eventClass</code>.
		 * @param eventClass The class of events
		 */
		public EventOnComponent(Class eventClass) {
			super(eventClass);
		}

		/**
		 * Builds a transition on a component with no modifier.
		 * This transition can be triggered by any virtual events
		 * whose type is a subclass of <code>eventClass</code>.
		 * @param eventClass The class of events
		 * @param outState The name of the output state
		 */
		public EventOnComponent(Class eventClass, String outState) {
			super(eventClass, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			if(classEvent != null) return "EventOnComponent("+classEvent.getSimpleName()+".class)";
			else return "EventOnComponent("+event+")";
		}

		/**
		 * Returns the component on which the mouse event firing this transition has occured.
		 * @return the JComponent on which the mouse event firing this transition has occured.
		 */
		public Component getComponent() {
			return ((MouseEvent)triggeringEvent).getComponent();
		}

		/**
		 * Returns the location at which this transition 
		 * has occured in the coordinate system of a given component.
		 * @param c the component defining the coordinate system.
		 * @return the location at which the mouse event firing this transition has occured.
		 */
		public Point2D getPointInComponent(Component c) {
			Point point = SwingUtilities.convertPoint(
					getContentPane(getComponent()),
					new Point((int)getPoint().getX(), (int)getPoint().getY()),
					c);
			return point;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			if(eventObject instanceof MouseEvent) {
				MouseEvent me = (MouseEvent)eventObject;
				return (!(me.getComponent() == null || !getControlledObjects().contains(me.getComponent())));
			}
			return false;
		}


	}


	/**
	 * 
	 * A Transition triggered when the mouse enters in a JComponent.
	 * 
	 * @author Caroline Appert
	 *
	 */
	public class EnterOnComponent extends MouseOnComponent {

		/**
		 * Builds an Enter transition.
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public EnterOnComponent(int modifier, String outState) {
			super(NOBUTTON, modifier, outState);
		}

		/**
		 * Builds an Enter transition that loops on the current state.
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnComponent(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds an Enter transition with no modifier.
		 * @param outState The name of the output state
		 */
		public EnterOnComponent(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds an Enter transition with no modifier that loops on the current state.
		 */
		public EnterOnComponent() {
			super(NOBUTTON, ANYMODIFIER);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			boolean b = matchesIgnoreButtons(eventObject, MouseEvent.MOUSE_ENTERED);
			return b;
		}

	}


	/**
	 * A transition triggered by a mouse button released on a component.
	 * @author Caroline Appert
	 */
	public class ReleaseOnComponent extends MouseOnComponent {

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ReleaseOnComponent(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnComponent(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ReleaseOnComponent(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ReleaseOnComponent(int button, int modifier, String outState) {
			super(button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_RELEASED);
		}

	}

	/**
	 * A transition triggered by a mouse button pressed on a component.
	 * @author Caroline Appert
	 */
	public class PressOnComponent extends MouseOnComponent {

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public PressOnComponent(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnComponent(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public PressOnComponent(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public PressOnComponent(int button, int modifier, String outState) {
			super(button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_PRESSED);
		}
	}

	/**
	 * A transition triggered by a mouse move event on a component with no mouse button down.
	 * @author Caroline Appert
	 */
	public class MoveOnComponent extends MouseOnComponent {

		/**
		 * Builds a transition triggered by a mouse moved event with mo modifier down on a component that loops on the current state.
		 */
		public MoveOnComponent() {
			super(NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a component that loops on the current state.
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnComponent(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a component.
		 * @param outState The name of the output state
		 */
		public MoveOnComponent(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a component.
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public MoveOnComponent(int modifier, String outState) {
			super(NOBUTTON, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_MOVED);
		}

	}

	/**
	 * A transition triggered by a mouse wheel event on a component with no mouse button down.
	 * @author Caroline Appert
	 */
	public class WheelOnComponent extends MouseOnComponent {

		/**
		 * Builds a transition triggered by a mouse wheel event with mo modifier down on a component that loops on the current state.
		 */
		public WheelOnComponent() {
			super(NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a component that loops on the current state.
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnComponent(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any modifier on a component.
		 * @param outState The name of the output state
		 */
		public WheelOnComponent(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a component.
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public WheelOnComponent(int modifier, String outState) {
			super(NOBUTTON, modifier, outState);
		}

		/**
		 * @return the number of units that should be scrolled in response to this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollAmount()
		 */
		public int getScrollAmount() {
			return ((MouseWheelEvent)triggeringEvent).getScrollAmount();
		}

		/**
		 * @return the type of scrolling that should take place in response to this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollType()
		 */
		public int getScrollType() {
			return ((MouseWheelEvent)triggeringEvent).getScrollType();
		}

		/**
		 * @return This is a convenience method to aid in the implementation of the common-case MouseWheelListener 
		 * - to scroll a ScrollPane or JScrollPane by an amount which conforms to the platform settings.
		 * @see java.awt.event.MouseWheelEvent#getUnitsToScroll()
		 */
		public int getUnitsToScroll() {
			return ((MouseWheelEvent)triggeringEvent).getUnitsToScroll();
		}

		/**
		 * @return the number of "clicks" the mouse wheel was rotated.
		 * @see java.awt.event.MouseWheelEvent#getWheelRotation()
		 */
		public int getWheelRotation() {
			return ((MouseWheelEvent)triggeringEvent).getWheelRotation();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_WHEEL);
		}
	}


	/**
	 * A transition triggered by a mouse event on a component.
	 * The transition is specified by a button and modifiers.
	 * The position of the mouse when the transition fired can be retrieved.
	 * @author Caroline Appert
	 */
	public class MouseOnComponent extends EventOnComponent {

		/**
		 * The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3.
		 */
		int button;

		/**
		 * The modifier: CONTROL, SHIFT, ALT, CONTROL_SHIFT, ALT_SHIFT, ALT_CONTROL, ALT_CONTROL_SHIFT or NOMODIFIER.
		 */
		int modifier = ANYMODIFIER;

		/**
		 * Builds a mouse transition.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnComponent(int button, int modifier, String outState) {
			super((String)null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnComponent(int button, int modifier) {
			super((String)null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with any modifier.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public MouseOnComponent(int button, String outState) {
			super((String)null, outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with any modifier that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public MouseOnComponent(int button) {
			super((String)null);
			this.button = button;
		}

		/**
		 * Returns the button of the mouse event that fires this transition.
		 * @return the button of the mouse event that fires this transition (NOBUTTON, BUTTON1, BUTTON2 or BUTTON3).
		 */
		public int getButton(){
			return button;
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			return getClass().getSuperclass().getSimpleName()+"("+Utils.getButtonAsText(button)+","+Utils.getModifiersAsText(modifier)+")";
		}

		/**
		 * @return the input event that fires this transition.
		 */
		public InputEvent getInputEvent() {
			return (InputEvent)triggeringEvent;
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
		public Component getComponent() {
			return ((MouseEvent)triggeringEvent).getComponent();
		}
		
		protected boolean matchesIgnoreButtons(EventObject eventObject, int typeEvent) {
			if(!(eventObject instanceof MouseEvent)) return false;
			MouseEvent me = (MouseEvent)eventObject;
			if(me.getComponent() == null || !getControlledObjects().contains(me.getComponent())) return false;
			return (me.getID() == typeEvent)
			&& (modifier == Utils.modifiers(me) || modifier == ANYMODIFIER);
		}

		protected boolean matches(EventObject eventObject, int typeEvent) {
			if(!(eventObject instanceof MouseEvent)) return false;
			MouseEvent me = (MouseEvent)eventObject;
			if(me.getComponent() == null || !getControlledObjects().contains(me.getComponent())) return false;
			return (me.getID() == typeEvent)
			&& (modifier == Utils.modifiers(me) || modifier == ANYMODIFIER)
			&& (button == ANYBUTTON || button == Utils.button(me));
		}
	}

	/**
	 * A transition triggered with no modifier on a component.
	 * @author Caroline Appert
	 */
	public class LeaveOnComponent extends MouseOnComponent {

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a component.
		 */
		public LeaveOnComponent() {
			super(NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a component that loops on the current state.
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnComponent(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a component. 
		 * @param outState The name of the output state
		 */
		public LeaveOnComponent(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_EXITED);
		}

	}


	/**
	 * A transition triggered by a mouse move event on a component with a mouse button down.
	 * @author Caroline Appert
	 */
	public class DragOnComponent extends MouseOnComponent {

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public DragOnComponent(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnComponent(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public DragOnComponent(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public DragOnComponent(int button, int modifier, String outState) {
			super(button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_DRAGGED);
		}
	}

	/**
	 * A transition triggered by a mouse button clicked on a JComponent.
	 * A click is defined as a quick succession of mouse press and mouse release, without significant motion in between.
	 * Note that the mouse press and mouse release events are always sent, even when a mouse click event is sent.
	 * @author Caroline Appert
	 */
	public class ClickOnComponent extends MouseOnComponent {

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ClickOnComponent(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a component that loops on the current state.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnComponent(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ClickOnComponent(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a component.
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ClickOnComponent(int button, int modifier, String outState) {
			super(button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_CLICKED);
		}

	}


	/**
	 * A transition triggered on a tagged component.
	 * OnTag transitions allow developpers to retrieve the component and the tag where this transition has been fired:
	 *  <pre>
	 *  Class ColorTag extends JNamedTag {
	 *  		Color color;
	 *  		public ColorTag(String tagName, Color c) {
	 *  			super(n);
	 *  		}
	 *  }
	 *  ...
	 *  JButton button1 = new JButton("button1");
	 *  JButton button2 = new JButton("button2");
	 *  JCheckbox cb = new JCheckbox("checkbox");
	 *  ColorTag red = new ColorTag("red", Color.RED);
	 *  red.addTo(button1).addTo(cb);
	 *  ColorTag green = new ColorTag("green", Color.GREEN);
	 *  green.addTo(button2);
	 *  </pre>
	 *  
	 *  <ul>
	 *  <li> Transitions can be marked by a tag:
	 *  <pre>
	 *  // The transition is triggered when an event occurs on button1 or on cb.
	 * 	Transition t = new EventOnTag (red, BUTTON1) {
	 * 		public void action() {
	 * 			...
	 * 			// set the background color of button1 and cb.
	 * 			getTag().setBackground(Color.RED);
	 * 			// print the text of the tooltip of the component on which this transition has been triggered (button1 OR cb)
	 * 			System.out.println(getComponent().getToolTipText());
	 * 		}
	 * 	}
	 * </pre>
	 *  <li> Transitions can be marked by a class of tag:
	 *  <pre>
	 *  // The transition is triggered when an event occurs on button1, button2 or on cb (any component tagged by an instance of ColorTag).
	 * 	Transition t = new EventOnTag (ColorTag.class, BUTTON1) {
	 * 		public void action() {
	 * 			...
	 * 			getComponent().setBackground(((ColorTag)getTag()).color));
	 * 			...
	 * 		}
	 * 	}
	 * </pre>
	 *  <li> Transitions can be marked by the name of the tag (if the tag is an instance of a subclass of a JNamedTag):
	 *  <pre>
	 *  // The transition is triggered when an event occurs on ellipse2 (the only component tagged by the instance of ColorTag having the name "green").
	 * 	Transition t = new EventOnTag ("green", BUTTON1) {
	 * 		public void action() {
	 * 			...
	 * 			System.out.println("This component is tagged by the color "+((ColoredTag)getTag()).color);
	 * 			...
	 * 		}
	 * 	}
	 * </pre>
	 * </ul>
	 * 
	 * <p> Note that each component is tagged by the name of the class of the component. For instance,
	 * one can retrieve any press on any JButton:
	 * <pre>
	 *   Transition pressOnButton = new PressOnTag(BUTTON1, "javax.swing.JButton") { ... };
	 * </pre>
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public abstract class EventOnTag extends EventOnComponent {

		/**
		 * The tag object.
		 */
		JTag tagObject = null;

		/**
		 * The name of the tag.
		 */
		String tagName = null;

		/**
		 * The class of the tag.
		 */
		Class tagClass = null;

		/**
		 * If the tag is mentioned by its name or not. 
		 */
		boolean isNamed = false;

		boolean isDesignedByClass = false;

		/**
		 * Builds a transition with any modifier on a tagged component that loops on the current state.
		 * @param tag The tag
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 */
		public EventOnTag(JTag tag, String keyEvent){
			super(keyEvent);
			tagObject = tag;
		}

		/**
		 * Builds a transition with any modifier on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 */
		public EventOnTag(Class tagClass, String keyEvent){
			super(keyEvent);
			this.tagClass = tagClass;
			isDesignedByClass = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 */
		public EventOnTag(String tagName, String keyEvent) {
			super(keyEvent);
			this.tagName = tagName;
			isNamed = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged component.
		 * @param tagName The name of the tag
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 * @param outState The name of the output state
		 */
		public EventOnTag(String tagName, String keyEvent, String outState) {
			super(keyEvent, outState);
			this.tagName = tagName;
			isNamed = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged component.
		 * @param tagClass The class of the tag
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 * @param outState The name of the output state
		 */
		public EventOnTag(Class tagClass, String keyEvent, String outState) {
			super(keyEvent, outState);
			this.tagClass = tagClass;
			isDesignedByClass = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged component.
		 * @param tag The tag
		 * @param keyEvent The string describing the events for which this transition must be triggered
		 * @param outState The name of the output state
		 */
		public EventOnTag(JTag tag, String keyEvent, String outState){
			super(keyEvent, outState);
			tagObject = tag;
		}

		/**
		 * Returns the name of the tag attached to the component on which the mouse event firing this transition has occured.
		 * @return name of the tag.
		 */
		public String getTagName() {
			return tagName;
		}

		/**
		 * Returns the tag instance attached to the JComponent on which the mouse event firing this transition has occured.
		 * @return the tag instance.
		 */
		public JTag getTag() {
			return tagObject;
		}

		void setTagObject(JTag tag) {
			tagObject = tag;
		}

		void setTagClass(Class tagClass) {
			this.tagClass = tagClass;
		}

		void setTagName(String tagName) {
			this.tagName = tagName;
		}

		protected boolean matchesTag(MouseEvent eventObject) {
			JTag tg = getTag();
			boolean hasTested = false;
			boolean hasTag = false;
			if(isDesignedByClass && JTag.allJComponentTags != null) {
				for(Iterator it = JTag.allJComponentTags.iterator(); it.hasNext(); ) {
					JTag o = (JTag)it.next();
					if(tagClass.equals(o.getClass())) {
						o.reset();
						while(o.hasNext()) {
							if(o.nextComponent() == eventObject.getComponent()) {
								hasTag = true;
								break;
							}
						}
						if(hasTag) {
							hasTested = true;
							setTagObject(o);
							if(o instanceof JNamedTag)
								setTagName(((JNamedTag)o).getName());
							break;
						}
					}
				}
			} else {
				if(isNamed) {
					if(tg == null) {
						tg = JNamedTag.getTag(tagName);
						if(tg == null)
							tg = new JNamedTag(tagName);
					}

					Class cls;
					try {
						cls = Class.forName(tagName);
						if(cls != null && cls.isAssignableFrom(eventObject.getComponent().getClass())) {
							((JNamedTag)tg).addTo(eventObject.getComponent());
							hasTested = true;
							hasTag = true;
						}
					} catch (ClassNotFoundException e) {
						;
					}

					setTagObject(tg);
					setTagClass(tg.getClass());
				}
			}
			if(!hasTested && tg!=null) {
				tg.reset();
				while(tg.hasNext()) {
					if(tg.nextComponent() == eventObject.getComponent()) {
						hasTag = true;
						break;
					}
				}
			}
			if(hasTag) {	
				return true; 
			}
			return false;	
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			String evt = classEvent != null ? classEvent.getSimpleName()+".class" : event;
			if(isDesignedByClass) {
				return "EventOnTag("+tagClass.getSimpleName()+".class, "+evt+")";
			} else {
				if(isNamed) return "EventOnTag(\""+tagName+"\", "+evt+")";
				else return "EventOnTag("+tagObject+", "+evt+")";
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return super.matches(eventObject) && matchesTag((MouseEvent)eventObject);
		}

	}	

	/**
	 * A transition triggered by a mouse event on a tagged component.
	 * The transition is specified by a button and modifiers.
	 * The position of the mouse when the transition fired can be retrieved.
	 * @author Caroline Appert
	 */
	public class MouseOnTag extends EventOnTag {

		/**
		 * The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3.
		 */
		int button;

		/**
		 * The modifier: CONTROL, SHIFT, ALT, CONTROL_SHIFT, ALT_SHIFT, ALT_CONTROL, ALT_CONTROL_SHIFT or NOMODIFIER.
		 */
		int modifier = ANYMODIFIER;

		/**
		 * Builds a mouse transition on tagged component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(JTag tag, int button, int modifier, String outState) {
			super(tag, (String)null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(JTag tag, int button, int modifier) {
			super(tag, (String)null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public MouseOnTag(JTag tag, int button, String outState) {
			super(tag, (String)null, outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public MouseOnTag(JTag tag, int button) {
			super(tag, (String)null);
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName, (String)null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(String tagName, int button, int modifier) {
			super(tagName, (String)null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public MouseOnTag(String tagName, int button, String outState) {
			super(tagName, (String)null, outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public MouseOnTag(String tagName, int button) {
			super(tagName, (String)null);
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass, (String)null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(Class tagClass, int button, int modifier) {
			super(tagClass, (String)null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public MouseOnTag(Class tagClass, int button, String outState) {
			super(tagClass, (String)null, outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public MouseOnTag(Class tagClass, int button) {
			super(tagClass, (String)null);
			this.button = button;
		}


		/**
		 * Returns the button of the mouse event that fires this transition.
		 * @return the button of the mouse event that fires this transition (NOBUTTON, BUTTON1, BUTTON2 or BUTTON3).
		 */
		public int getButton(){
			return button;
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			String evt = classEvent != null ? classEvent.getSimpleName()+".class" : event;
			if(isDesignedByClass) {
				return getClass().getSuperclass().getSimpleName()+"("+tagClass.getSimpleName()+".class, "+Utils.getButtonAsText(button)+","+Utils.getModifiersAsText(modifier)+","+evt+")";
			} else {
				if(isNamed) return getClass().getSuperclass().getSimpleName()+"(\""+tagName+"\", "+Utils.getButtonAsText(button)+","+Utils.getModifiersAsText(modifier)+","+evt+")";
				else return getClass().getSuperclass().getSimpleName()+"("+tagObject+", "+Utils.getButtonAsText(button)+","+Utils.getModifiersAsText(modifier)+","+evt+")";
			}
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
		public Component getComponent() {
			return ((MouseEvent)triggeringEvent).getComponent();
		}

		protected boolean matchesIgnoreButtons(EventObject eventObject, int typeEvent) {
			if(!(eventObject instanceof MouseEvent)) return false;
			MouseEvent me = (MouseEvent)eventObject;
			if(me.getComponent() == null || !getControlledObjects().contains(me.getComponent())) return false;
			return (me.getID() == typeEvent)
			&& (modifier == Utils.modifiers(me) || modifier == ANYMODIFIER)
			&& matchesTag(me);
		}

		protected boolean matches(EventObject eventObject, int typeEvent) {
			if(!(eventObject instanceof MouseEvent)) return false;
			MouseEvent me = (MouseEvent)eventObject;
			if(me.getComponent() == null || !getControlledObjects().contains(me.getComponent())) return false;
			return (me.getID() == typeEvent)
			&& (modifier == Utils.modifiers(me) || modifier == ANYMODIFIER)
			&& (button == ANYBUTTON || button == Utils.button(me))
			&& matchesTag(me);
		}
	}

	/**
	 * A transition triggered when the cursor enters in a component with a given tag.
	 * @author Caroline Appert
	 */
	public class EnterOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered when the cursor enters with any modifier in a tagged component. This transition loops on the current state.
		 * @param tagName The name of the tag 
		 */
		public EnterOnTag(String tagName) {
			super(tagName,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any modifier in a tagged component. This transition loops on the current state.
		 * @param tagClass The class of the tag 
		 */
		public EnterOnTag(Class tagClass) {
			super(tagClass,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any modifier in a tagged component. This transition loops on the current state.
		 * @param tag The tag 
		 */
		public EnterOnTag(JTag tag) {
			super(tag,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged component. This transition loops on the current state.
		 * @param tagName The name of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnTag(String tagName, int modifier) {
			super(tagName,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any modifier in a tagged component. This transition loops on the current state.
		 * @param tagClass The class of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnTag(Class tagClass, int modifier) {
			super(tagClass,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged component. This transition loops on the current state.
		 * @param tag The tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnTag(JTag tag, int modifier) {
			super(tag,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any modifier in a tagged component.
		 * @param tagName The name of the tag 
		 * @param outState The name of the output state
		 */
		public EnterOnTag(String tagName, String outState) {
			super(tagName,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any modifier in a tagged component.
		 * @param tagClass The class of the tag 
		 * @param outState The name of the output state
		 */
		public EnterOnTag(Class tagClass, String outState) {
			super(tagClass,  NOBUTTON, ANYMODIFIER, outState);
		}


		/**
		 * Builds a transition triggered when the cursor enters with any modifier in a tagged component.
		 * @param tag The tag 
		 * @param outState The name of the output state
		 */
		public EnterOnTag(JTag tag, String outState) {
			super(tag,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged component.
		 * @param tagName The name of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public EnterOnTag(String tagName, int modifier, String outState) {
			super(tagName,  NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged component.
		 * @param tagClass The class of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public EnterOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass,  NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged component.
		 * @param tag The tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public EnterOnTag(JTag tag, int modifier, String outState) {
			super(tag,  NOBUTTON, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matchesIgnoreButtons(eventObject, MouseEvent.MOUSE_ENTERED);
		}

	}

	/**
	 * A transition triggered when the cursor leaves a component with a given tag.
	 * @author Caroline Appert
	 */
	public class LeaveOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a tagged component. This transition loops on the current state.
		 * @param tagName The name of the tag 
		 */
		public LeaveOnTag(String tagName) {
			super(tagName,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a tagged component. This transition loops on the current state.
		 * @param tagClass The class of the tag 
		 */
		public LeaveOnTag(Class tagClass) {
			super(tagClass,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a tagged component. This transition loops on the current state.
		 * @param tag The tag 
		 */
		public LeaveOnTag(JTag tag) {
			super(tag,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged component. This transition loops on the current state.
		 * @param tagName The name of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnTag(String tagName, int modifier) {
			super(tagName,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged component. This transition loops on the current state.
		 * @param tagClass The class of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnTag(Class tagClass, int modifier) {
			super(tagClass,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged component. This transition loops on the current state.
		 * @param tag The tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnTag(JTag tag, int modifier) {
			super(tag,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a tagged component.
		 * @param tagName The name of the tag 
		 * @param outState The name of the output state
		 */
		public LeaveOnTag(String tagName, String outState) {
			super(tagName,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a tagged component.
		 * @param tagClass The class of the tag 
		 * @param outState The name of the output state
		 */
		public LeaveOnTag(Class tagClass, String outState) {
			super(tagClass,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any modifier a tagged component.
		 * @param tag The tag 
		 * @param outState The name of the output state
		 */
		public LeaveOnTag(JTag tag, String outState) {
			super(tag,  NOBUTTON, ANYMODIFIER, outState);
		}


		/**
		 * Builds a transition triggered when the cursor leaves a tagged component.
		 * @param tagName The name of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public LeaveOnTag(String tagName, int modifier, String outState) {
			super(tagName,  NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged component.
		 * @param tagClass The class of the tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public LeaveOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged component.
		 * @param tag The tag 
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public LeaveOnTag(JTag tag, int modifier, String outState) {
			super(tag, NOBUTTON, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matchesIgnoreButtons(eventObject, MouseEvent.MOUSE_EXITED);
		}
	}


	/**
	 * A transition triggered by a mouse pressed event on a component with a given tag.
	 * @author Caroline Appert
	 */
	public class PressOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a tagged component that loops on the current state.
		 * @param tagName The name of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public PressOnTag(String tagName, int button) {
			super(tagName,  button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public PressOnTag(Class tagClass, int button) {
			super(tagClass,  button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a tagged component that loops on the current state.
		 * @param tag The tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public PressOnTag(JTag tag, int button) {
			super(tag,  button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged component that loops on the current state.
		 * @param tagName The name of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnTag(String tagName, int button, int modifier) {
			super(tagName,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnTag(Class tagClass, int button, int modifier) {
			super(tagClass,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged component that loops on the current state.
		 * @param tag The tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnTag(JTag tag, int button, int modifier) {
			super(tag,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a tagged component.
		 * @param tagName The name of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public PressOnTag(String tagName, int button, String outState) {
			super(tagName,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a tagged component.
		 * @param tagClass The class of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public PressOnTag(Class tagClass, int button, String outState) {
			super(tagClass,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any modifier on a tagged component.
		 * @param tag The tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public PressOnTag(JTag tag, int button, String outState) {
			super(tag,  button, ANYMODIFIER, outState);
		}


		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged component.
		 * @param tagName The name of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public PressOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged component.
		 * @param tagClass The class of the tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public PressOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged component.
		 * @param tag The tag 
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public PressOnTag(JTag tag, int button, int modifier, String outState) {
			super(tag,  button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_PRESSED);
		}
	}

	/**
	 * A transition triggered by a mouse released event on a component with a given tag.
	 * @author Caroline Appert
	 */
	public class ReleaseOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ReleaseOnTag(String tagName, int button) {
			super(tagName,  button);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ReleaseOnTag(Class tagClass, int button) {
			super(tagClass,  button);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a tagged component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ReleaseOnTag(JTag tag, int button) {
			super(tag,  button);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnTag(String tagName, int button, int modifier) {
			super(tagName,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnTag(Class tagClass, int button, int modifier) {
			super(tagClass,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnTag(JTag tag, int button, int modifier) {
			super(tag,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a tagged component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ReleaseOnTag(String tagName, int button, String outState) {
			super(tagName,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a tagged component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ReleaseOnTag(Class tagClass, int button, String outState) {
			super(tagClass,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any modifier on a tagged component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ReleaseOnTag(JTag tag, int button, String outState) {
			super(tag,  button, ANYMODIFIER, outState);
		}


		/**
		 * Builds a transition triggered by a mouse released event on a tagged component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ReleaseOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName,  button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ReleaseOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass,  button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ReleaseOnTag(JTag tag, int button, int modifier, String outState) {
			super(tag,  button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_RELEASED);
		}
	}

	/**
	 * A transition triggered by a mouse clicked event on a component with a given tag.
	 * A click is defined as a quick succession of mouse press and mouse release, without significant motion in between.
	 * Note that the mouse press and mouse release events are always sent, even when a mouse click event is sent.
	 * @author Caroline Appert
	 */
	public class ClickOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ClickOnTag(String tagName, int button) {
			super(tagName,  button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ClickOnTag(Class tagClass, int button) {
			super(tagClass,  button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a tagged component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public ClickOnTag(JTag tag, int button) {
			super(tag,  button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnTag(String tagName, int button, int modifier) {
			super(tagName,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnTag(Class tagClass, int button, int modifier) {
			super(tagClass,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnTag(JTag tag, int button, int modifier) {
			super(tag,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a tagged component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ClickOnTag(String tagName, int button, String outState) {
			super(tagName,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a tagged component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ClickOnTag(Class tagClass, int button, String outState) {
			super(tagClass,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any modifier on a tagged component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public ClickOnTag(JTag tag, int button, String outState) {
			super(tag,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ClickOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName,  button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ClickOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass,  button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public ClickOnTag(JTag tag, int button, int modifier, String outState) {
			super(tag,  button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_CLICKED);
		}

	}

	/**
	 * A transition triggered by a mouse moved event with a button pressed on a component with a given tag.
	 * @author Caroline Appert
	 */
	public class DragOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public DragOnTag(String tagName, int button) {
			super(tagName,  button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public DragOnTag(Class tagClass, int button) {
			super(tagClass,  button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 */
		public DragOnTag(JTag tag, int button) {
			super(tag,  button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnTag(String tagName, int button, int modifier) {
			super(tagName,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnTag(Class tagClass, int button, int modifier) {
			super(tagClass,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a component that loops on the current state.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnTag(JTag tag, int button, int modifier) {
			super(tag,  button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public DragOnTag(String tagName, int button, String outState) {
			super(tagName,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public DragOnTag(Class tagClass, int button, String outState) {
			super(tagClass,  button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any modifier on a component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param outState The name of the output state
		 */
		public DragOnTag(JTag tag, int button, String outState) {
			super(tag,  button, ANYMODIFIER, outState);
		}


		/**
		 * Builds a transition triggered by a mouse dragged event on a component.
		 * @param tagName The name of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public DragOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName,  button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a component.
		 * @param tagClass The class of the tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public DragOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass,  button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a component.
		 * @param tag The tag
		 * @param button The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public DragOnTag(JTag tag, int button, int modifier, String outState) {
			super(tag,  button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_DRAGGED);
		}
	}

	/**
	 * A transition triggered by a mouse moved event with no button pressed on a component with a given tag.
	 * @author Caroline Appert
	 */
	public class MoveOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 */
		public MoveOnTag(String tagName) {
			super(tagName,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 */
		public MoveOnTag(Class tagClass) {
			super(tagClass,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a tagged component that loops on the current state.
		 * @param tag The tag
		 */
		public MoveOnTag(JTag tag) {
			super(tag,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnTag(String tagName, int modifier) {
			super(tagName,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnTag(Class tagClass, int modifier) {
			super(tagClass,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a tagged component that loops on the current state.
		 * @param tag The tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnTag(JTag tag, int modifier) {
			super(tag,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a tagged component.
		 * @param tagName The name of the tag
		 * @param outState The name of the output state
		 */
		public MoveOnTag(String tagName, String outState) {
			super(tagName,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a tagged component.
		 * @param tagClass The class of the tag
		 * @param outState The name of the output state
		 */
		public MoveOnTag(Class tagClass, String outState) {
			super(tagClass,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a tagged component.
		 * @param tag The tag
		 * @param outState The name of the output state
		 */
		public MoveOnTag(JTag tag, String outState) {
			super(tag,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a tagged component.
		 * @param tagName The name of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public MoveOnTag(String tagName, int modifier, String outState) {
			super(tagName,  NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a tagged component.
		 * @param tagClass The class of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public MoveOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass,  NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse motion event on a tagged component.
		 * @param tag The tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public MoveOnTag(JTag tag, int modifier, String outState) {
			super(tag,  NOBUTTON, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_MOVED);
		}

	}

	/**
	 * A transition triggered by a mouse wheel event with no button pressed on a component with a given tag.
	 * @author Caroline Appert
	 */
	public class WheelOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse motion event with any modifier on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 */
		public WheelOnTag(String tagName) {
			super(tagName,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any modifier on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 */
		public WheelOnTag(Class tagClass) {
			super(tagClass,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any modifier on a tagged component that loops on the current state.
		 * @param tag The tag
		 */
		public WheelOnTag(JTag tag) {
			super(tag,  NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged component that loops on the current state.
		 * @param tagName The name of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnTag(String tagName, int modifier) {
			super(tagName,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged component that loops on the current state.
		 * @param tagClass The class of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnTag(Class tagClass, int modifier) {
			super(tagClass,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged component that loops on the current state.
		 * @param tag The tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnTag(JTag tag, int modifier) {
			super(tag,  NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any modifier on a tagged component.
		 * @param tagName The name of the tag
		 * @param outState The name of the output state
		 */
		public WheelOnTag(String tagName, String outState) {
			super(tagName,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any modifier on a tagged component.
		 * @param tagClass The class of the tag
		 * @param outState The name of the output state
		 */
		public WheelOnTag(Class tagClass, String outState) {
			super(tagClass,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any modifier on a tagged component.
		 * @param tag The tag
		 * @param outState The name of the output state
		 */
		public WheelOnTag(JTag tag, String outState) {
			super(tag,  NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged component.
		 * @param tagName The name of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public WheelOnTag(String tagName, int modifier, String outState) {
			super(tagName,  NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged component.
		 * @param tagClass The class of the tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public WheelOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass,  NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged component.
		 * @param tag The tag
		 * @param modifier The modifier: NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState The name of the output state
		 */
		public WheelOnTag(JTag tag, int modifier, String outState) {
			super(tag,  NOBUTTON, modifier, outState);
		}

		/**
		 * @return the number of units that should be scrolled in response to this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollAmount()
		 */
		public int getScrollAmount() {
			return ((MouseWheelEvent)triggeringEvent).getScrollAmount();
		}

		/**
		 * @return the type of scrolling that should take place in response to this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollType()
		 */
		public int getScrollType() {
			return ((MouseWheelEvent)triggeringEvent).getScrollType();
		}

		/**
		 * @return This is a convenience method to aid in the implementation of the common-case MouseWheelListener 
		 * - to scroll a ScrollPane or JScrollPane by an amount which conforms to the platform settings.
		 * @see java.awt.event.MouseWheelEvent#getUnitsToScroll()
		 */
		public int getUnitsToScroll() {
			return ((MouseWheelEvent)triggeringEvent).getUnitsToScroll();
		}

		/**
		 * @return the number of "clicks" the mouse wheel was rotated.
		 * @see java.awt.event.MouseWheelEvent#getWheelRotation()
		 */
		public int getWheelRotation() {
			return ((MouseWheelEvent)triggeringEvent).getWheelRotation();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_WHEEL);
		}
	}

}
