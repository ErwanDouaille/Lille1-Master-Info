/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
 */
package fr.lri.swingstates.canvas;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import fr.lri.swingstates.animations.ATag;
import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.events.PickerCEvent;
import fr.lri.swingstates.events.Utils;
import fr.lri.swingstates.events.VirtualAnimationEvent;
import fr.lri.swingstates.events.VirtualCElementEvent;
import fr.lri.swingstates.events.VirtualCanvasEvent;
import fr.lri.swingstates.events.VirtualEvent;
import fr.lri.swingstates.sm.BasicInputStateMachine;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Event;
import fr.lri.swingstates.sm.transitions.EventOnPosition;

/**
 * <p>
 * A state machine to handle events with one or more <code>CElement</code> (<code>Canvas</code>,
 * <code>CTag</code> or <code>CShape</code>).
 * </p>
 * 
 * <p>
 * A <code>CStateMachine</code> handles events handled by a
 * <code>BasicInputStateMachine</code> plus the following list:
 * <ul>
 * <li> <code>PressOnShape</code>, <code>PressOnTag</code>: pressing a
 * mouse button on a shape / on a shape with a given tag;
 * <li> <code>ReleaseOnShape</code>, <code>ReleaseOnTag</code>: releasing
 * a mouse button on a shape / on a shape with a given tag;
 * <li> <code>ClickOnShape</code>, <code>ClickOnTag</code>: clicking
 * (pressing and releasing in quick succession) a mouse button on a shape / on a
 * shape with a given tag;
 * <li> <code>MoveOnShape</code>, <code>MoveOnTag</code>: moving the mouse
 * with no button pressed on a shape / on a shape with a given tag;
 * <li> <code>DragOnShape</code>, <code>DragOnTag</code>: moving the mouse
 * with a button pressed on a shape / on a shape with a given tag;
 * <li> <code>WheelOnShape</code>, <code>WheelOnTag</code>: rotating the
 * mouse wheel on a shape / on a shape with a given tag;
 * <li> <code>EnterOnShape</code>, <code>EnterOnTag</code>: cursor enters
 * a shape / a shape with a given tag;
 * <li> <code>LeaveOnShape</code>, <code>LeaveOnTag</code>: cursor leaves
 * a shape / a shape with a given tag;
 * <li> <code>AnimationStarted</code>, <code>AnimationStopped</code>,
 * <code>AnimationSuspended</code> and <code>AnimationResumed</code>: a
 * given animation has started, stopped, been suspended or been resumed.
 * <li> <code>CElementEvent</code>: a given graphical element has changed.
 * </ul>
 * </p>
 * 
 * @see fr.lri.swingstates.sm.BasicInputStateMachine
 * @author Caroline Appert
 * 
 */
public abstract class CStateMachine extends BasicInputStateMachine {

	LinkedList<CElement> controlledObjects = new LinkedList<CElement>();

	/**
	 * The key string of events that triggered <code>AnimationStopped</code> transitions.
	 */
	public static String ANIMATION_STOPPED   = "AnimationStopped";
	/**
	 * The key string of events that triggered <code>AnimationStarted</code> transitions.
	 */
	public static String ANIMATION_STARTED   = "AnimationStarted";
	/**
	 * The key string of events that triggered <code>AnimationSuspended</code> transitions.
	 */
	public static String ANIMATION_SUSPENDED = "AnimationSuspended";
	/**
	 * The key string of events that triggered <code>AnimationResumed</code> transitions.
	 */
	public static String ANIMATION_RESUMED   = "AnimationResumed";

	/**
	 * Builds a CStateMachine.
	 */
	public CStateMachine() {
		super();
	}

	/**
	 * Builds a CStateMachine.
	 * 
	 * @param ce
	 *            The canvas element whose events must be handled by this state
	 *            machine.
	 */
	public CStateMachine(CElement ce) {
		super();
		attachTo(ce, true);
	}

	/**
	 * Attaches a state machine to a given <code>CElement</code>. Attaching a CStateMachine to 
	 * a CElement (a shape, a tag or a canvas) causes the transitions *OnShape and *OnTag
	 * (e.g. PressOnShape, PressOnTag, ReleaseOnShape...)
	 * to be fired only on shapes that are parts of the attached element. Note that 
	 * all other transitions that can occur anywhere (e.g. Press, Release...) remains <i>firable</i>.
	 * 
	 * <p>
	 * For example, the transition <code>t1</code> will be fired only if a mouse press occurs on
	 * a shape that holds the tag <code>movable</code> while the transition <code>t2</code> will still be
	 * fired by any mouse press event on the canvas:
	 * </p>
	 * <pre>
	 * CStateMachine machine = new CStateMachine() {
	 *    State start = new State() {
	 *       Transition t1 = new PressOnShape(BUTTON1, ">> moveShape") {
	 *       	...
	 *       };
	 *       Transition t2 = new Press(BUTTON1, ">> panView") {
	 *       	...
	 *       };
	 *    };
	 *    State moveShape = new State() {
	 *       ...
	 *    };
	 *    State panView = new State() {
	 *       ...
	 *    };
	 *    ...
	 * };
	 * ...
	 * CTag movable = ...;
	 * ...
	 * machine.attachTo(movable);
	 * </pre>
	 * 
	 * @param ce
	 *            The <code>CElement</code> to which this state machine
	 *            must be attached.
	 * @param reset
	 *            Whether or not the state machine must be reset.
	 *            
	 */
	public void attachTo(CElement ce, boolean reset) {
		if (controlledObjects == null)
			controlledObjects = new LinkedList<CElement>();
		if (!controlledObjects.contains(ce)) {
			ce.getCanvas().attachListeners();
			controlledObjects.add(ce);
			ce.getCanvas().registerMachine(this);
		}
		if (reset)
			reset();
	}

	/**
	 * Attaches a state machine to a given <code>CElement</code> and
	 * resets it.
	 * 
	 * @param ce
	 *            The <code>CElement</code> to which this state machine
	 *            must be attached.
	 * 
	 * @see CStateMachine#attachTo(CElement, boolean)
	 */
	public void attachTo(CElement ce) {
		attachTo(ce, true);
	}

	/**
	 * Tests if this state machine is attached to a given <code>CElement</code>
	 * object.
	 * 
	 * @param ce
	 *            The <code>CElement</code> object to test
	 * @return true if this state machine is attached to <code>ce</code>.
	 */
	public boolean isAttachedTo(CElement ce) {
		return controlledObjects.contains(ce);
	}

	/**
	 * Returns the linked list of <code>CElement</code> objects to which this
	 * state machine is attached, or null is the machine is not attached.
	 * 
	 * @return the linked list of <code>CElement</code> objects to which this
	 *         state machine is attached.
	 */
	public LinkedList<CElement> getControlledObjects() {
		if (controlledObjects == null)
			return null;
		if (controlledObjects.size() == 0)
			return null;
		return controlledObjects;
	}

	/**
	 * Tests whether or not one this state machine has a transition of a given
	 * class <code>cl</code>.
	 * 
	 * @param cl
	 *            The class of transitions
	 * @return true if the current state of this state machine contains a
	 *         transition of class <code>cl</code>.
	 */
	boolean hasTransitionOfClass(Class<?> cl) {
		for (Iterator<Transition> i = currentState.getTransitions().iterator(); i.hasNext();) {
			Transition nextTrans = i.next();
			if (cl.isAssignableFrom(nextTrans.getClass()))
				return true;
		}
		return false;
	}

	/**
	 * Detaches a state machine from a <code>CElement</code> object. Does
	 * nothing if it was not attached.
	 * 
	 * @param ce
	 *            The <code>CElement</code> object from which this state
	 *            machine must be detached
	 */
	public void detach(CElement ce) {
		controlledObjects.remove(ce);
		if (controlledObjects.size() == 0) {
			ce.getCanvas().detachListeners();
		}
	}

	boolean isSourceControlled(CShape source) {
		if (!isActive())
			return false;
		boolean isSourceControlled = false;
		if (source != null) {
			for (ListIterator<CElement> i = controlledObjects.listIterator(); i.hasNext();) {
				CElement next = i.next();
				if (((next instanceof Canvas) && (source.getCanvas() == next)) || ((next instanceof CShape) && (source == next)) || ((next instanceof CTag) && (source.hasTag((CTag) next)))) {
					isSourceControlled = true;
					break;
				}
			}
		}
		return isSourceControlled;
	}

	/**
	 * Makes this state machine having a lower priority than another state
	 * machine. Assume two machines m1 and m2 have priorities p1 and p2. 
	 * If p1 < p2, m1 receives the event after m2 has received it. 
	 * This state machine is placed just before smGreater 
	 * (priority of smGreater is unchanged).
	 * 
	 * @param smGreater
	 *            The state machine that must have a greater priority than
	 *            smLower.
	 * @param canvas
	 *            The canvas.
	 */
	public void lowerPriorityThan(CStateMachine smGreater, Canvas canvas) {
		synchronized(canvas.stateMachines) {
			if (canvas.stateMachines.remove(this)) {
				int i = canvas.stateMachines.indexOf(smGreater);
				if (i != -1) {
					if ((i + 1) < canvas.stateMachines.size()) {
						canvas.stateMachines.add(i + 1, this);
					} else {
						canvas.stateMachines.add(this);
					}
				}
			}
		}
	}
	
	/**
	 * Makes this state machine have the lowest priority. This state machines
	 * will receive events after every state machine has received it.
	 * 
	 * @param canvas
	 *            The canvas.
	 * @return this state machine.
	 */
	public CStateMachine lowestPriority(Canvas canvas) {
		synchronized(canvas.stateMachines) {
			if (canvas.stateMachines.remove(this))
				canvas.stateMachines.add(this);
		}
		return this;
	}

	/**
	 * Makes this state machine have a greater priority than another state
	 * machine. Assume two machines m1 and m2 have priorities p1 and p2. 
	 * If p1 < p2, m1 receives the event after m2 has received it. 
	 * This state machine is placed just after smLower 
	 * (priority of smLower is unchanged).
	 * 
	 * @param smLower
	 *            The state machine that must have a lower priority than
	 *            smGreater.
	 * @param canvas
	 *            The canvas.
	 * @return this state machine.
	 */
	public CStateMachine greaterPriorityThan(CStateMachine smLower, Canvas canvas) {
		synchronized(canvas.stateMachines) {
			if (canvas.stateMachines.remove(this)) {
				int i = canvas.stateMachines.indexOf(smLower);
				if (i != -1) {
					canvas.stateMachines.add(i, this);
				}
			}
		}
		return this;
	}

	/**
	 * Makes this state machine have the greatest priority. This state machines
	 * will receive events before every state machine has received it.
	 * 
	 * @param canvas
	 *            The canvas.
	 * @return this state machine.
	 */
	public CStateMachine greatestPriority(Canvas canvas) {
		synchronized(canvas.stateMachines) {
			if (canvas.stateMachines.remove(this))
				canvas.stateMachines.add(0, this);
		}
		return this;
	}

	/**
	 * <p>
	 * A transition triggered on a CShape in the canvas. OnShape transitions
	 * allow developpers to retrieve the shape in the canvas where this
	 * transition has been fired:
	 * 
	 * <pre>
	 * 	Transition tshape = new EventOnShape (&quot;anEvent&quot;) {
	 * 		public void action() {
	 * 			// colors in red the shape on which the transition has been fired
	 * 			getShape().setFillPaint(Color.RED);
	 * 		}
	 * 	}
	 * 	
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public abstract class EventOnShape extends EventOnPosition {

		/**
		 * Builds a transition on a CShape with no modifier that loops on the
		 * current state.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 */
		public EventOnShape(String keyEvent) {
			super(keyEvent);
		}

		/**
		 * Builds a transition on a CShape with no modifier.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnShape(String keyEvent, String outState) {
			super(keyEvent, outState);
		}

		/**
		 * Builds a transition on a CShape with no modifier that can be
		 * triggered by any virtual events whose type is a subclass of
		 * <code>eventClass</code>.
		 * 
		 * @param eventClass
		 *            The class of events
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnShape(Class eventClass, String outState) {
			super(eventClass, outState);
		}

		/**
		 * Builds a transition on a CShape with no modifier that can be
		 * triggered by any virtual events whose type is a subclass of
		 * <code>eventClass</code>.
		 * 
		 * @param eventClass
		 *            The class of events
		 */
		public EventOnShape(Class eventClass) {
			super(eventClass);
		}

		/**
		 * Returns the CShape on which the event firing this transition has
		 * occured.
		 * 
		 * @return the CShape on which the event firing this transition has
		 *         occured.
		 */
		public CShape getShape() {
			return ((VirtualCanvasEvent)triggeringEvent).getShape();
		}


		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			if (classEvent != null)
				return "EventOnShape(" + classEvent.getSimpleName() + ".class)";
			else
				return "EventOnShape(" + event + ")";
		}

		/**
		 * {@inheritDoc}
		 */
		public Point2D getPoint() {
			return ((VirtualCanvasEvent)triggeringEvent).getPoint();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			if (super.matches(eventObject)) {
				VirtualCanvasEvent vse = (VirtualCanvasEvent)eventObject;
				return vse.getShape() != null;
			}
			return false;
		}
	}

	/**
	 * <p>
	 * A transition triggered by an animation. Animation transitions allow
	 * developpers to retrieve the animation that fired this transition:
	 * 
	 * <pre>
	 * 	Transition tshape = new AnimationEvent(&quot;anEvent&quot;) {
	 * 		public void action() {
	 * 			// starts the transition that fired this transition
	 * 			getAnimation().start();
	 * 		}
	 * 	}
	 * 	
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	abstract class AnimationEvent extends Event {

		protected Animation animation = null;
		protected ATag tagAnimation = null;
		protected boolean genericAnimation = true;

		/**
		 * Builds a transition triggered by any animation that loops on the
		 * current state.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered: "AnimationStarted", "AnimationStopped",
		 *            "AnimationSuspended" or "AnimationResumed".
		 */
		protected AnimationEvent(String keyEvent) {
			super(keyEvent);
		}

		/**
		 * Builds a transition triggered by an animation that loops on the
		 * current state.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered: "AnimationStarted", "AnimationStopped",
		 *            "AnimationSuspended" or "AnimationResumed".
		 * @param anim
		 *            The animation that fires this transition.
		 */
		protected AnimationEvent(String keyEvent, Animation anim) {
			super(keyEvent);
			animation = anim;
			genericAnimation = false;
		}

		/**
		 * Builds a transition triggered by a tagged animation that loops on the
		 * current state.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered: "AnimationStarted", "AnimationStopped",
		 *            "AnimationSuspended" or "AnimationResumed".
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 */
		protected AnimationEvent(String keyEvent, ATag tagAnim) {
			super(keyEvent);
			tagAnimation = tagAnim;
			genericAnimation = false;
		}

		/**
		 * Builds a transition triggered by any animation.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered: "AnimationStarted", "AnimationStopped",
		 *            "AnimationSuspended" or "AnimationResumed".
		 * @param outState
		 *            The name of the output state.
		 */
		protected AnimationEvent(String keyEvent, String outState) {
			super(keyEvent, outState);
		}

		/**
		 * Builds a transition triggered by an animation.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered: "AnimationStarted", "AnimationStopped",
		 *            "AnimationSuspended" or "AnimationResumed".
		 * @param anim
		 *            The animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		protected AnimationEvent(String keyEvent, Animation anim, String outState) {
			super(keyEvent, outState);
			animation = anim;
			genericAnimation = false;
		}

		/**
		 * Builds a transition triggered by a tagged animation.
		 * 
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered: "AnimationStarted", "AnimationStopped",
		 *            "AnimationSuspended" or "AnimationResumed".
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		protected AnimationEvent(String keyEvent, ATag tagAnim, String outState) {
			super(keyEvent, outState);
			tagAnimation = tagAnim;
			genericAnimation = false;
		}

		/**
		 * @return the animation that has just fired this transition.
		 */
		public Animation getAnimation() {
			return animation;
		}

		public VirtualAnimationEvent getAnimationEvent() {
			return (VirtualAnimationEvent) triggeringEvent;
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			return getClass().getSuperclass().getSimpleName() + "(" + animation + ")";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			if (!(eventObject instanceof VirtualAnimationEvent))
				return false;
			if (super.matches(eventObject)) {
				Animation triggeringAnimation = ((VirtualAnimationEvent) eventObject).getAnimation();
				// transition for a given tagged animation
				if (tagAnimation != null) {
					if (tagAnimation.tagsAnimation(triggeringAnimation)) {
						animation = triggeringAnimation;
						return true;
					} else {
						return false;
					}
				} else {
					// generic transition
					if (genericAnimation) {
						animation = triggeringAnimation;
						return true;
						// transition for a given animation
					} else {
						return triggeringAnimation == animation;
					}
				}
			} else
				return false;
		}
	}

	/**
	 * <p>
	 * A transition triggered by an animation that has just "naturally" stopped
	 * or has just been explicitely stopped. For example, one can want to start
	 * an animation <code>animAfter</code> just after an animation
	 * <code>animBefore</code> has stopped.
	 * 
	 * <pre>
	 * 	Transition tshape = new AnimationStopped(animBefore) {
	 * 		public void action() {
	 * 			// starts
	 * <code>
	 * animAfter
	 * </code>
	 *  animation.
	 * 			animAfter.start();
	 * 		}
	 * 	}
	 * 	
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public class AnimationStopped extends AnimationEvent {

		/**
		 * Builds a transition that loops on the current state triggered when
		 * any animation has just stopped.
		 */
		public AnimationStopped() {
			super(ANIMATION_STOPPED);
		}

		/**
		 * Builds a transition that loops on the current state triggered when an
		 * animation has just stopped.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 */
		public AnimationStopped(Animation anim) {
			super(ANIMATION_STOPPED, anim);
		}

		/**
		 * Builds a transition that loops on the current state triggered when a
		 * tagged animation has just stopped.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 */
		public AnimationStopped(ATag tagAnim) {
			super(ANIMATION_STOPPED, tagAnim);
		}

		/**
		 * Builds a transition triggered when any animation has just stopped.
		 * 
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationStopped(String outState) {
			super(ANIMATION_STOPPED, outState);
		}

		/**
		 * Builds a transition triggered when an animation has just stopped.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationStopped(Animation anim, String outState) {
			super(ANIMATION_STOPPED, anim, outState);
		}

		/**
		 * Builds a transition triggered when a tagged animation has just
		 * stopped.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationStopped(ATag tagAnim, String outState) {
			super(ANIMATION_STOPPED, tagAnim, outState);
		}

	}

	/**
	 * <p>
	 * A transition triggered by an animation that has just started. For
	 * example, one can want to stop an animation <code>animBefore</code> when
	 * an animation <code>animAfter</code> starts.
	 * 
	 * <pre>
	 * 	Transition tshape = new AnimationStarted(animAfter) {
	 * 		public void action() {
	 * 			// stops
	 * <code>
	 * animBefore
	 * </code>
	 *  animation.
	 * 			animBefore.stop();
	 * 		}
	 * 	}
	 * 	
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public class AnimationStarted extends AnimationEvent {

		/**
		 * Builds a transition that loops on the current state triggered when
		 * any animation has just started.
		 */
		public AnimationStarted() {
			super(ANIMATION_STARTED);
		}

		/**
		 * Builds a transition that loops on the current state triggered when an
		 * animation has just started.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 */
		public AnimationStarted(Animation anim) {
			super(ANIMATION_STARTED, anim);
		}

		/**
		 * Builds a transition that loops on the current state triggered when a
		 * tagged animation has just started.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 */
		public AnimationStarted(ATag tagAnim) {
			super(ANIMATION_STARTED, tagAnim);
		}

		/**
		 * Builds a transition triggered when any animation has just started.
		 * 
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationStarted(String outState) {
			super(ANIMATION_STARTED, outState);
		}

		/**
		 * Builds a transition triggered when an animation has just started.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationStarted(Animation anim, String outState) {
			super(ANIMATION_STARTED, anim, outState);
		}

		/**
		 * Builds a transition triggered when a tagged animation has just
		 * started.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationStarted(ATag tagAnim, String outState) {
			super(ANIMATION_STARTED, tagAnim);
		}

	}

	/**
	 * <p>
	 * A transition triggered by an animation that has just been suspended. For
	 * example, one can want to resume an animation <code>animSlept</code>
	 * when an animation <code>animAwake</code> is suspended.
	 * 
	 * <pre>
	 * 	Transition tshape = new AnimationSuspended(animAwake) {
	 * 		public void action() {
	 * 			// resumes
	 * <code>
	 * animSlept
	 * </code>
	 *  animation.
	 * 			animSlept.resume();
	 * 		}
	 * 	}
	 * 	
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public class AnimationSuspended extends AnimationEvent {

		/**
		 * Builds a transition that loops on the current state triggered when
		 * any animation has just been suspended.
		 */
		public AnimationSuspended() {
			super(ANIMATION_SUSPENDED);
		}

		/**
		 * Builds a transition that loops on the current state triggered when an
		 * animation has just been suspended.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 */
		public AnimationSuspended(Animation anim) {
			super(ANIMATION_SUSPENDED, anim);
		}

		/**
		 * Builds a transition that loops on the current state triggered when a
		 * tagged animation has just been suspended.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 */
		public AnimationSuspended(ATag tagAnim) {
			super(ANIMATION_SUSPENDED, tagAnim);
		}

		/**
		 * Builds a transition triggered when any animation has just been
		 * supended.
		 * 
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationSuspended(String outState) {
			super(ANIMATION_SUSPENDED, outState);
		}

		/**
		 * Builds a transition triggered when an animation has just been
		 * supended.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationSuspended(Animation anim, String outState) {
			super(ANIMATION_SUSPENDED, anim, outState);
		}

		/**
		 * Builds a transition triggered when a tagged animation has just been
		 * supended.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationSuspended(ATag tagAnim, String outState) {
			super(ANIMATION_SUSPENDED, tagAnim, outState);
		}

	}

	/**
	 * <p>
	 * A transition triggered by an animation that has just been resumed. For
	 * example, one can want to suspend an animation <code>animAwake</code>
	 * when an animation <code>animSlept</code> is resumed.
	 * 
	 * <pre>
	 * 	Transition tshape = new AnimationResumed(animSlept) {
	 * 		public void action() {
	 * 			// suspends
	 * <code>
	 * animAwake
	 * </code>
	 *  animation.
	 * 			animAwake.suspend();
	 * 		}
	 * 	}
	 * 	
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public class AnimationResumed extends AnimationEvent {

		/**
		 * Builds a transition that loops on the current state triggered when
		 * any animation that has just been resumed.
		 */
		public AnimationResumed() {
			super(ANIMATION_RESUMED);
		}

		/**
		 * Builds a transition that loops on the current state triggered when an
		 * animation that has just been resumed.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 */
		public AnimationResumed(Animation anim) {
			super(ANIMATION_RESUMED, anim);
		}

		/**
		 * Builds a transition that loops on the current state triggered when a
		 * tagged animation that has just been resumed.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 */
		public AnimationResumed(ATag tagAnim) {
			super(ANIMATION_RESUMED, tagAnim);
		}

		/**
		 * Builds a transition triggered when any animation that has just been
		 * resumed.
		 * 
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationResumed(String outState) {
			super(ANIMATION_RESUMED, outState);
		}

		/**
		 * Builds a transition triggered when an animation that has just been
		 * resumed.
		 * 
		 * @param anim
		 *            The animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationResumed(Animation anim, String outState) {
			super(ANIMATION_RESUMED, anim, outState);
		}

		/**
		 * Builds a transition triggered when a tagged animation that has just
		 * been resumed.
		 * 
		 * @param tagAnim
		 *            The tag of the animation that fires this transition.
		 * @param outState
		 *            The name of the output state.
		 */
		public AnimationResumed(ATag tagAnim, String outState) {
			super(ANIMATION_RESUMED, tagAnim, outState);
		}
	}

	/**
	 * <p>
	 * A transition triggered by a <code>CElement</code> each time this
	 * <code>CElement</code> is modified. For example, one can want to track
	 * the potential collisions of a given shape <code>ball</code> with the
	 * left edge of the canvas.
	 * 
	 * <pre>
	 * 	Transition tshape = CElementEvent(ball) {
	 * 		public boolean guard() {
	 * 			return ((CShape)getCElement()).getMinX() &lt; 0;
	 * 		}
	 * 		public void action() {
	 * 			... // do something
	 * 		}
	 * 	}
	 * 	
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public class CElementEvent extends Event {

		CElement element;

		/**
		 * Builds a transition that loops on the current state triggered by any
		 * changes on a <code>CElement</code>.
		 * 
		 * @param cElement
		 *            The <code>CElement</code> to track.
		 */
		public CElementEvent(CElement cElement) {
			super("");
			element = cElement;
		}

		/**
		 * Builds a transition triggered by any changes on a
		 * <code>CElement</code>.
		 * 
		 * @param cElement
		 *            The <code>CElement</code> to track.
		 * @param outState
		 *            The name of the output state.
		 */
		public CElementEvent(CElement cElement, String outState) {
			super("", outState);
			element = cElement;
		}

		/**
		 * @return the <code>CElement</code> that has just triggered this
		 *         animation.
		 */
		public CElement getCElement() {
			return element;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			if (!(eventObject instanceof VirtualCElementEvent)) {
				return false;
			}
			VirtualCElementEvent vce = (VirtualCElementEvent) eventObject;
			CElement elementThatTriggered = vce.getCElement();
			if (element instanceof CShape) {
				return element == elementThatTriggered;
			}
			if (element instanceof CTag) {
				return elementThatTriggered.hasTag((CTag) element);
			}
			if (element instanceof Canvas) {
				return elementThatTriggered.getCanvas() == element;
			}
			return false;
		}

		/**
		 * @return the event that has just triggered this transition.
		 */
		public VirtualCElementEvent getCElementEvent() {
			return (VirtualCElementEvent) triggeringEvent;
		}
	}

	/**
	 * A transition triggered by a mouse event on a CShape in the canvas.
	 * <code>MouseOnShape</code> is the super class of
	 * <ul>
	 * <li><code>PressOnShape</code>: mouse button pressed on a shape in the
	 * canvas
	 * <li><code>ReleaseOnShape</code>: mouse button released on a shape in
	 * the canvas
	 * <li><code>MoveOnShape</code>: mouse moved on a shape in the canvas
	 * <li><code>DragOnShape</code>: mouse dragged on a shape in the canvas
	 * (move with a button pressed)
	 * <li><code>EnterOnShape</code>: mouse cursor entered on a shape in the
	 * canvas
	 * <li><code>LeaveOnShape</code>: mouse cursor left a shape in the
	 * canvas
	 * </ul>
	 * <p>
	 * For instance, the following code allows to specify a transition that
	 * fires only on the background of the canvas, where no shape is displayed.
	 * First specify an empty transition that fires on any shape, then a
	 * transition that fires anywhere on the canvas. For example:
	 * 
	 * <pre>
	 * 	public State s = new State () {
	 * 		Transition tshape = new PressOnShape (BUTTON1) { ... };	// captures button presses on shapes
	 * 		Transition tbackground = new Press (BUTTON1) { ... };	// captures button presses on background
	 * 	}
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @author Caroline Appert
	 */
	public class MouseOnShape extends EventOnShape {

		/**
		 * The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3.
		 */
		int button;

		/**
		 * The modifier: CONTROL, SHIFT, ALT, CONTROL_SHIFT, ALT_SHIFT,
		 * ALT_CONTROL, ALT_CONTROL_SHIFT or NOMODIFIER.
		 */
		int modifier = ANYMODIFIER;

		/**
		 * Builds a mouse transition with any modifier.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnShape(String outState) {
			this(ANYBUTTON, outState);
		}

		/**
		 * Builds a mouse transition with any modifier that loops on the current
		 * state.
		 */
		public MouseOnShape() {
			this(ANYBUTTON);
		}

		/**
		 * Builds a mouse transition.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnShape(int button, int modifier, String outState) {
			super((String) null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnShape(int button, int modifier) {
			super((String) null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with any modifier.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnShape(int button, String outState) {
			super((String) null);
			setOutputStateName(outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with any modifier that loops on the current
		 * state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public MouseOnShape(int button) {
			super((String) null);
			this.button = button;
		}

		/**
		 * Returns the button of the mouse event that fires this transition.
		 * 
		 * @return the button of the mouse event that fires this transition
		 *         (NOBUTTON, BUTTON1, BUTTON2 or BUTTON3).
		 */
		public int getButton() {
			return button;
		}

		/**
		 * Returns the modifier of the event that fires this transition.
		 * 
		 * @return the modifier of the event that fires this transition
		 *         (NOMODIFIER, CONTROL, ALT, SHIFT, ALT_CONTROL, CONTROL_SHIFT,
		 *         ALT_SHIFT or ALT_CONTROL_SHIFT).
		 */
		public int getModifier() {
			return modifier;
		}

		/**
		 * @return the input event that has just fired this transition.
		 */
		public InputEvent getInputEvent() {
			return (InputEvent) triggeringEvent;
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
			return ((PickerCEvent)triggeringEvent).getPoint();
		}

		/**
		 * {@inheritDoc}
		 */
		public CShape getShape() {
			return ((PickerCEvent)triggeringEvent).getPicked();
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			return getClass().getSuperclass().getSimpleName() + "(" + Utils.getButtonAsText(button) + "," + Utils.getModifiersAsText(modifier) + ")";
		}

		protected boolean matchesIgnoreButtons(EventObject eventObject, int typeEvent) {
			if (!(eventObject instanceof PickerCEvent))
				return false;
			PickerCEvent me = (PickerCEvent) eventObject;
			if(!(me.getID() == typeEvent))
				return false;
			if (me.getSource() instanceof Canvas) {
				CShape picked = me.getPicked();
				return isSourceControlled(picked);
			}
			return false;
		}

		protected boolean matches(EventObject eventObject, int typeEvent) {
			if (!(eventObject instanceof PickerCEvent))
				return false;
			PickerCEvent me = (PickerCEvent) eventObject;
			if (me.getSource() instanceof Canvas) {
				if(!((me.getID() == typeEvent) 
						&& (modifier == ANYMODIFIER || modifier == Utils.modifiers(me)) 
						&& (button == ANYBUTTON || button == Utils.button((PickerCEvent) eventObject)))) 
					return false;
				CShape picked = me.getPicked();
				if(isSourceControlled(picked)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * A transition triggered by a mouse button released on a CShape.
	 * 
	 * @author Caroline Appert
	 */
	public class ReleaseOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier and any button on a CShape that loops on the current state.
		 */
		public ReleaseOnShape() {
			super();
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier and any button on a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnShape(String outState) {
			super(outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a CShape that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ReleaseOnShape(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a CShape
		 * that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnShape(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a CShape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnShape(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a CShape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnShape(int button, int modifier, String outState) {
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
	 * A transition triggered by a mouse button pressed on a CShape.
	 * 
	 * @author Caroline Appert
	 */
	public class PressOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier and any button on a CShape that loops on the current state.
		 */
		public PressOnShape() {
			super();
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnShape(String outState) {
			super(outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a CShape that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public PressOnShape(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a CShape
		 * that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnShape(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a CShape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnShape(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a CShape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnShape(int button, int modifier, String outState) {
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
	 * A transition triggered by a mouse moved event on a CShape with no mouse
	 * button down.
	 * 
	 * @author Caroline Appert
	 */
	public class MoveOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered by a mouse moved event with mo modifier
		 * down on a CShape that loops on the current state.
		 */
		public MoveOnShape() {
			super(NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a CShape that
		 * loops on the current state.
		 * 
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnShape(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse moved event with any
		 * modifier on a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnShape(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a CShape.
		 * 
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnShape(int modifier, String outState) {
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
	 * A transition triggered by a mouse wheel event on a CShape with no mouse
	 * button down.
	 * 
	 * @author Caroline Appert
	 */
	public class WheelOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered by a mouse wheel event with mo modifier
		 * down on a CShape that loops on the current state.
		 */
		public WheelOnShape() {
			super(NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a CShape that
		 * loops on the current state.
		 * 
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnShape(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any
		 * modifier on a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnShape(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a CShape.
		 * 
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnShape(int modifier, String outState) {
			super(NOBUTTON, modifier, outState);
		}

		/**
		 * @return the number of units that should be scrolled in response to
		 *         this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollAmount()
		 */
		public int getScrollAmount() {
			return ((MouseWheelEvent) triggeringEvent).getScrollAmount();
		}

		/**
		 * @return the type of scrolling that should take place in response to
		 *         this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollType()
		 */
		public int getScrollType() {
			return ((MouseWheelEvent) triggeringEvent).getScrollType();
		}

		/**
		 * @return This is a convenience method to aid in the implementation of
		 *         the common-case MouseWheelListener - to scroll a ScrollPane
		 *         or JScrollPane by an amount which conforms to the platform
		 *         settings.
		 * @see java.awt.event.MouseWheelEvent#getUnitsToScroll()
		 */
		public int getUnitsToScroll() {
			return ((MouseWheelEvent) triggeringEvent).getUnitsToScroll();
		}

		/**
		 * @return the number of "clicks" the mouse wheel was rotated.
		 * @see java.awt.event.MouseWheelEvent#getWheelRotation()
		 */
		public int getWheelRotation() {
			return ((MouseWheelEvent) triggeringEvent).getWheelRotation();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_WHEEL);
		}
	}

	/**
	 * A transition triggered when mouse cursor leaves a CShape.
	 * 
	 * @author Caroline Appert
	 */
	public class LeaveOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a CShape.
		 */
		public LeaveOnShape() {
			super(NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a CShape that
		 * loops on the current state.
		 * 
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnShape(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public LeaveOnShape(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			if (!(eventObject instanceof PickerCEvent))
				return false;
			if (matchesIgnoreButtons(eventObject, MouseEvent.MOUSE_EXITED)) {
				return true;
			}
			return false;
		}

	}

	/**
	 * A transition triggered when mouse cursor enters on a CShape.
	 * 
	 * @author Caroline Appert
	 */
	public class EnterOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier into a CShape.
		 */
		public EnterOnShape() {
			super(NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor enters into a CShape
		 * that loops on the current state.
		 * 
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnShape(int modifier) {
			super(NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier into a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public EnterOnShape(String outState) {
			super(NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			if (!(eventObject instanceof PickerCEvent))
				return false;
			if (matchesIgnoreButtons(eventObject, MouseEvent.MOUSE_ENTERED)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * A transition triggered by a mouse move event on a CShape with a mouse
	 * button down.
	 * 
	 * @author Caroline Appert
	 */
	public class DragOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier and any button on a CShape that loops on the current state.
		 */
		public DragOnShape() {
			super();
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnShape(String outState) {
			super(outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public DragOnShape(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape
		 * that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnShape(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnShape(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnShape(int button, int modifier, String outState) {
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
	 * A transition triggered by a mouse button clicked on a CShape. A click is
	 * defined as a quick succession of mouse press and mouse release, without
	 * significant motion in between. Note that the mouse press and mouse
	 * release events are always sent, even when a mouse click event is sent.
	 * 
	 * @author Caroline Appert
	 */
	public class ClickOnShape extends MouseOnShape {

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier and any button on a CShape that loops on the current state.
		 */
		public ClickOnShape() {
			super();
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a CShape.
		 * 
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnShape(String outState) {
			super(outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a CShape that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ClickOnShape(int button) {
			super(button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a CShape
		 * that loops on the current state.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnShape(int button, int modifier) {
			super(button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a CShape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnShape(int button, String outState) {
			super(button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a CShape.
		 * 
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnShape(int button, int modifier, String outState) {
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
	 * A transition triggered on a tagged shape. OnTag transitions allow
	 * developpers to retrieve the shape and the tag where this transition has
	 * been fired:
	 * 
	 * <pre>
	 *  class ColorTag extends CNamedTag {
	 *  		Color color;
	 *  		public ColorTag(String tagName, Color c) {
	 *  			super(n);
	 *  		}
	 *  }
	 *  ...
	 *  CShape ellipse1 = canvas.newEllipse(100, 100, 20, 30);
	 *  CShape ellipse2 = canvas.newEllipse(200, 200, 20, 30);
	 *  CShape rectangle = canvas.newRectangle(300, 300, 20, 30);
	 *  ColorTag red = new ColorTag(&quot;red&quot;, Color.RED);
	 *  red.addTo(ellipse1).addTo(rectangle);
	 *  ColorTag green = new ColorTag(&quot;green&quot;, Color.GREEN);
	 *  green.addTo(ellipse2);
	 * </pre>
	 * 
	 * <ul>
	 * <li> Transitions can be marked by a tag:
	 * 
	 * <pre>
	 *  // The transition is triggered when an event occurs on ellipse1 or on rectangle.
	 * 	Transition t = new EventOnTag (red, BUTTON1) {
	 * 		public void action() {
	 * 			...
	 * 			// scale ellipse1 and rectangle
	 * 			getTag().scaleBy(1.5);
	 * 			// set the transparencyFill of the shape on which this transition has been triggered (ellipse1 OR rectangle)
	 * 			getShape().setTransparency(0.5f);
	 * 		}
	 * 	}
	 * </pre>
	 * 
	 * <li> Transitions can be marked by a class of tag:
	 * 
	 * <pre>
	 *  // The transition is triggered when an event occurs on ellipse1, ellipse2 or on rectangle (any shape tagged by an instance of ColorTag).
	 * 	Transition t = new EventOnTag (ColorTag.class, BUTTON1) {
	 * 		public void action() {
	 * 			...
	 * 			getShape().setFillPaint(((ColorTag)getTag()).color));
	 * 			...
	 * 		}
	 * 	}
	 * </pre>
	 * 
	 * <li> Transitions can be marked by the name of the tag (if the tag is an
	 * instance of a subclass of a CNamedTag):
	 * 
	 * <pre>
	 *  // The transition is triggered when an event occurs on ellipse2 (the only shape tagged by the instance of ColorTag having the name &quot;green&quot;).
	 * 	Transition t = new EventOnTag (&quot;green&quot;, BUTTON1) {
	 * 		public void action() {
	 * 			...
	 * 			System.out.println(&quot;This shape is tagged by the color &quot;+((ColorTag)getTag()).color);
	 * 			...
	 * 		}
	 * 	}
	 * </pre>
	 * 
	 * </ul>
	 * 
	 * @author Caroline Appert
	 */
	public abstract class EventOnTag extends EventOnShape {

		/**
		 * The tag object.
		 */
		CTag tagObject = null;

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
		 * Builds a transition with any modifier on a tagged shape that loops on
		 * the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 */
		public EventOnTag(CTag tag, String keyEvent) {
			super(keyEvent);
			tagObject = tag;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape that loops on
		 * the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 */
		public EventOnTag(Class tagClass, String keyEvent) {
			super(keyEvent);
			this.tagClass = tagClass;
			isDesignedByClass = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape that loops on
		 * the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 */
		public EventOnTag(String tagName, String keyEvent) {
			super(keyEvent);
			this.tagName = tagName;
			isNamed = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape that loops on
		 * the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param eventClass
		 *            The class of events
		 */
		public EventOnTag(CTag tag, Class eventClass) {
			super("");
			tagObject = tag;
			classEvent = eventClass;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape that loops on
		 * the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param eventClass
		 *            The class of events
		 */
		public EventOnTag(Class tagClass, Class eventClass) {
			super("");
			this.tagClass = tagClass;
			isDesignedByClass = true;
			classEvent = eventClass;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape that loops on
		 * the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param eventClass
		 *            The class of events
		 */
		public EventOnTag(String tagName, Class eventClass) {
			super("");
			this.tagName = tagName;
			isNamed = true;
			classEvent = eventClass;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnTag(String tagName, String keyEvent, String outState) {
			super(keyEvent, outState);
			this.tagName = tagName;
			isNamed = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnTag(Class tagClass, String keyEvent, String outState) {
			super(keyEvent, outState);
			this.tagClass = tagClass;
			isDesignedByClass = true;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param keyEvent
		 *            The string describing the events for which this transition
		 *            must be triggered
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnTag(CTag tag, String keyEvent, String outState) {
			super(keyEvent, outState);
			tagObject = tag;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param eventClass
		 *            The class of events
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnTag(String tagName, Class eventClass, String outState) {
			super("", outState);
			this.tagName = tagName;
			isNamed = true;
			classEvent = eventClass;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape.
		 * 
		 * @param tgClass
		 *            The class of the tag
		 * @param eventClass
		 *            The class of events
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnTag(Class tgClass, Class eventClass, String outState) {
			super("", outState);
			tagClass = tgClass;
			isDesignedByClass = true;
			classEvent = eventClass;
		}

		/**
		 * Builds a transition with any modifier on a tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param eventClass
		 *            The class of events for which that triggers this
		 *            transition.
		 * @param outState
		 *            The name of the output state
		 */
		public EventOnTag(CTag tag, Class eventClass, String outState) {
			super("", outState);
			tagObject = tag;
			classEvent = eventClass;
		}

		/**
		 * Returns the name of the tag hold by the CShape on which the mouse
		 * event firing this transition has occurred.
		 * 
		 * @return name of the tag.
		 */
		public String getTagName() {
			return tagName;
		}

		/**
		 * Returns the tag instance hold by the CShape on which the mouse
		 * event firing this transition has occurred.
		 * 
		 * @return the tag instance.
		 */
		public CTag getTag() {
			return tagObject;
		}

		void setTagObject(CTag tag) {
			tagObject = tag;
		}

		void setTagClass(Class tagClass) {
			this.tagClass = tagClass;
		}

		void setTagName(String tagName) {
			this.tagName = tagName;
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			String evt = classEvent != null ? classEvent.getSimpleName() + ".class" : event;
			if (isDesignedByClass) {
				return "EventOnTag(" + tagClass.getSimpleName() + ".class, " + evt + ")";
			} else {
				if (isNamed)
					return "EventOnTag(\"" + tagName + "\", " + evt + ")";
				else
					return "EventOnTag(" + tagObject + ", " + evt + ")";
			}
		}

		protected boolean matches(CShape source) {
			boolean hasTested = false;
			if (!isSourceControlled(source))
				return false;
			if (isDesignedByClass && source.getCanvas().allCanvasTags != null) {
				for (Iterator<CTag> it = source.getCanvas().allCanvasTags.iterator(); it.hasNext();) {
					CTag o = (CTag) it.next();
					if (tagClass.isAssignableFrom(o.getClass())) {
						if (source.hasTag(o)) {
							hasTested = true;
							tagObject = o;
							if (o instanceof CNamedTag)
								setTagName(((CNamedTag) o).getName());
							break;
						}
					}
				}
			} else {
				if (isNamed) {
					if (tagObject == null) {
						tagObject = source.getCanvas().getTag(tagName);
						if (tagObject == null)
							return false;
						tagClass = tagObject.getClass();
					}
				}
				// else {
				// is denoted by the tag object itself

				// }
			}
			if (hasTested || source.hasTag(tagObject))
				return true;
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			if (super.matches(eventObject)) {
				return matches(((VirtualCanvasEvent)eventObject).getShape());
			}
			return false;
		}
	}

	/**
	 * A transition triggered by a mouse event on a tagged shape in the canvas.
	 * The transition is specialized by a button and modifiers. Constants used
	 * for button and modifier are static fields in <code>BasicInputStateMachine</code>.
	 * 
	 * @author Caroline Appert
	 * 
	 * @see BasicInputStateMachine#BUTTON1
	 * @see BasicInputStateMachine#BUTTON2
	 * @see BasicInputStateMachine#BUTTON3
	 * @see BasicInputStateMachine#NOBUTTON
	 * @see BasicInputStateMachine#ANYBUTTON
	 * 
	 * @see BasicInputStateMachine#CONTROL
	 * @see BasicInputStateMachine#ALT
	 * @see BasicInputStateMachine#SHIFT
	 * @see BasicInputStateMachine#CONTROL_SHIFT
	 * @see BasicInputStateMachine#ALT_CONTROL
	 * @see BasicInputStateMachine#ALT_SHIFT
	 * @see BasicInputStateMachine#ALT_CONTROL_SHIFT
	 * @see BasicInputStateMachine#NOMODIFIER
	 * @see BasicInputStateMachine#ANYMODIFIER
	 */
	public class MouseOnTag extends EventOnTag {

		/**
		 * The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2 or BUTTON3.
		 */
		int button;

		/**
		 * The modifier: CONTROL, SHIFT, ALT, CONTROL_SHIFT, ALT_SHIFT,
		 * ALT_CONTROL, ALT_CONTROL_SHIFT or NOMODIFIER.
		 */
		int modifier = ANYMODIFIER;

		/**
		 * Builds a mouse transition with any modifier and any button on a
		 * tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnTag(CTag tag, String outState) {
			this(tag, ANYBUTTON, outState);
		}

		/**
		 * Builds a mouse transition with any modifier and any button on a
		 * tagged shape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public MouseOnTag(CTag tag) {
			this(tag, ANYBUTTON);
		}

		/**
		 * Builds a mouse transition with any modifier and any button on a
		 * tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnTag(Class tagClass, String outState) {
			this(tagClass, ANYBUTTON, outState);
		}

		/**
		 * Builds a mouse transition with any modifier and any button on a
		 * tagged shape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public MouseOnTag(Class tagClass) {
			this(tagClass, ANYBUTTON);
		}

		/**
		 * Builds a mouse transition on tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(CTag tag, int button, int modifier, String outState) {
			super(tag, (String) null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged shape that loops on the current
		 * state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(CTag tag, int button, int modifier) {
			super(tag, (String) null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnTag(CTag tag, int button, String outState) {
			super(tag, (String) null, outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged shape that loops
		 * on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public MouseOnTag(CTag tag, int button) {
			super(tag, (String) null);
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName, (String) null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged shape that loops on the current
		 * state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(String tagName, int button, int modifier) {
			super(tagName, (String) null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnTag(String tagName, int button, String outState) {
			super(tagName, (String) null, outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on a tagged shape that loops
		 * on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public MouseOnTag(String tagName, int button) {
			super(tagName, (String) null);
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass, (String) null, outState);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition on tagged shape that loops on the current
		 * state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MouseOnTag(Class tagClass, int button, int modifier) {
			super(tagClass, (String) null);
			this.modifier = modifier;
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public MouseOnTag(Class tagClass, int button, String outState) {
			super(tagClass, (String) null, outState);
			this.button = button;
		}

		/**
		 * Builds a mouse transition with no modifier on tagged shape that loops
		 * on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public MouseOnTag(Class tagClass, int button) {
			super(tagClass, (String) null);
			this.button = button;
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			String evt = classEvent != null ? classEvent.getSimpleName() + ".class" : event;
			if (isDesignedByClass) {
				return getClass().getSuperclass().getSimpleName() + "(" + tagClass.getSimpleName() + ".class, " + Utils.getButtonAsText(button) + "," + Utils.getModifiersAsText(modifier) + "," + evt
				+ ")";
			} else {
				if (isNamed)
					return getClass().getSuperclass().getSimpleName() + "(\"" + tagName + "\", " + Utils.getButtonAsText(button) + "," + Utils.getModifiersAsText(modifier) + "," + evt + ")";
				else
					return getClass().getSuperclass().getSimpleName() + "(" + tagObject + ", " + Utils.getButtonAsText(button) + "," + Utils.getModifiersAsText(modifier) + "," + evt + ")";
			}
		}

		/**
		 * Returns the button of the mouse event that fires this transition.
		 * 
		 * @return the button of the mouse event that fires this transition
		 *         (NOBUTTON, BUTTON1, BUTTON2 or BUTTON3).
		 */
		public int getButton() {
			return button;
		}

		/**
		 * @return the awt input event that fires this transition.
		 */
		public InputEvent getInputEvent() {
			return (InputEvent) triggeringEvent;
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
			return ((PickerCEvent)triggeringEvent).getPoint();
		}

		/**
		 * {@inheritDoc}
		 */
		public CShape getShape() {
			return ((PickerCEvent)triggeringEvent).getPicked();
		}

		protected boolean matchesIgnoreButtons(EventObject eventObject, int typeEvent) {
			if (!(eventObject instanceof PickerCEvent))
				return false;
			PickerCEvent me = (PickerCEvent) eventObject;
			if(!(me.getID() == typeEvent)) return false;
			if (me.getSource() instanceof Canvas) {
				CShape picked = me.getPicked();
				if(isSourceControlled(picked) && matches(me.getPicked())) {
					return true;
				}
			}
			return false;
		}

		protected boolean matches(EventObject eventObject, int typeEvent) {
			if (!(eventObject instanceof PickerCEvent))
				return false;
			PickerCEvent me = (PickerCEvent) eventObject;
			if (me.getSource() instanceof Canvas) {
				if(!((me.getID() == typeEvent) 
						&& (modifier == ANYMODIFIER || modifier == Utils.modifiers(me)) 
						&& (button == ANYBUTTON || button == Utils.button((PickerCEvent) eventObject)))) 
					return false;
				CShape picked = me.getPicked();
				return isSourceControlled(picked) && matches(me.getPicked());
			}
			return false;
		}

	}

	/**
	 * A transition triggered when the cursor enters in a CShape 
	 * that holds a given
	 * tag.
	 * 
	 * @author Caroline Appert
	 */
	public class EnterOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier in a tagged shape. This transition loops on the current
		 * state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 */
		public EnterOnTag(String tagName) {
			super(tagName, NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier in a tagged shape. This transition loops on the current
		 * state.
		 * 
		 * @param tagClass
		 *            The clas of the tag
		 */
		public EnterOnTag(Class tagClass) {
			super(tagClass, NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier in a tagged shape. This transition loops on the current
		 * state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public EnterOnTag(CTag tag) {
			super(tag, NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged
		 * shape. This transition loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnTag(String tagName, int modifier) {
			super(tagName, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier in a tagged shape. This transition loops on the current
		 * state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnTag(Class tagClass, int modifier) {
			super(tagClass, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged
		 * shape. This transition loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public EnterOnTag(CTag tag, int modifier) {
			super(tag, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier in a tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public EnterOnTag(String tagName, String outState) {
			super(tagName, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier in a tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public EnterOnTag(Class tagClass, String outState) {
			super(tagClass, ANYBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters with any
		 * modifier in a tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public EnterOnTag(CTag tag, String outState) {
			super(tag, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged
		 * shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public EnterOnTag(String tagName, int modifier, String outState) {
			super(tagName, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged
		 * shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public EnterOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor enters in a tagged
		 * shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public EnterOnTag(CTag tag, int modifier, String outState) {
			super(tag, NOBUTTON, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matchesIgnoreButtons(eventObject, MouseEvent.MOUSE_ENTERED);
		}
	}

	/**
	 * A transition triggered when the cursor leaves a CShape with a given tag.
	 * 
	 * @author Caroline Appert
	 */
	public class LeaveOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a tagged shape. This transition loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 */
		public LeaveOnTag(String tagName) {
			super(tagName, NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a tagged shape. This transition loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public LeaveOnTag(Class tagClass) {
			super(tagClass, NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a tagged shape. This transition loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public LeaveOnTag(CTag tag) {
			super(tag, NOBUTTON);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged shape.
		 * This transition loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnTag(String tagName, int modifier) {
			super(tagName, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged shape.
		 * This transition loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnTag(Class tagClass, int modifier) {
			super(tagClass, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged shape.
		 * This transition loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public LeaveOnTag(CTag tag, int modifier) {
			super(tag, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public LeaveOnTag(String tagName, String outState) {
			super(tagName, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public LeaveOnTag(Class tagClass, String outState) {
			super(tagClass, ANYBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves with any
		 * modifier a tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public LeaveOnTag(CTag tag, String outState) {
			super(tag, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public LeaveOnTag(String tagName, int modifier, String outState) {
			super(tagName, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public LeaveOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered when the cursor leaves a tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public LeaveOnTag(CTag tag, int modifier, String outState) {
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
	 * A transition triggered by a mouse pressed event on a CShape with a given
	 * tag.
	 * 
	 * @author Caroline Appert
	 */
	public class PressOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public PressOnTag(Class tagClass) {
			super(tagClass);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public PressOnTag(CTag tag) {
			super(tag);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(Class tagClass, String outState) {
			super(tagClass, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(CTag tag, String outState) {
			super(tag, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a tagged shape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public PressOnTag(String tagName, int button) {
			super(tagName, button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a tagged shape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public PressOnTag(Class tagClass, int button) {
			super(tagClass, button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a tagged shape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public PressOnTag(CTag tag, int button) {
			super(tag, button);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged
		 * shape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnTag(String tagName, int button, int modifier) {
			super(tagName, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged
		 * shape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnTag(Class tagClass, int button, int modifier) {
			super(tagClass, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged
		 * shape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public PressOnTag(CTag tag, int button, int modifier) {
			super(tag, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a tagged shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(String tagName, int button, String outState) {
			super(tagName, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a tagged shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(Class tagClass, int button, String outState) {
			super(tagClass, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event with any
		 * modifier on a tagged shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(CTag tag, int button, String outState) {
			super(tag, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged
		 * shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged
		 * shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse pressed event on a tagged
		 * shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public PressOnTag(CTag tag, int button, int modifier, String outState) {
			super(tag, button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_PRESSED);
		}

	}

	/**
	 * A transition triggered by a mouse released event on a CShape with a given
	 * tag.
	 * 
	 * @author Caroline Appert
	 */
	public class ReleaseOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public ReleaseOnTag(Class tagClass) {
			super(tagClass);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public ReleaseOnTag(CTag tag) {
			super(tag);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(Class tagClass, String outState) {
			super(tagClass, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(CTag tag, String outState) {
			super(tag, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ReleaseOnTag(String tagName, int button) {
			super(tagName, button);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ReleaseOnTag(Class tagClass, int button) {
			super(tagClass, button);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ReleaseOnTag(CTag tag, int button) {
			super(tag, button);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnTag(String tagName, int button, int modifier) {
			super(tagName, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnTag(Class tagClass, int button, int modifier) {
			super(tagClass, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ReleaseOnTag(CTag tag, int button, int modifier) {
			super(tag, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(String tagName, int button, String outState) {
			super(tagName, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(Class tagClass, int button, String outState) {
			super(tagClass, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(CTag tag, int button, String outState) {
			super(tag, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged
		 * CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged
		 * CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse released event on a tagged
		 * CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ReleaseOnTag(CTag tag, int button, int modifier, String outState) {
			super(tag, button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_RELEASED);
		}

	}

	/**
	 * A transition triggered by a mouse clicked event on a CShape with a given
	 * tag. A click is defined as a quick succession of mouse press and mouse
	 * release, without significant motion in between. Note that the mouse press
	 * and mouse release events are always sent, even when a mouse click event
	 * is sent.
	 * 
	 * @author Caroline Appert
	 */
	public class ClickOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public ClickOnTag(Class tagClass) {
			super(tagClass);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public ClickOnTag(CTag tag) {
			super(tag);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(Class tagClass, String outState) {
			super(tagClass, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(CTag tag, String outState) {
			super(tag, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ClickOnTag(CTag tag, int button) {
			super(tag, button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ClickOnTag(String tagName, int button) {
			super(tagName, button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public ClickOnTag(Class tagClass, int button) {
			super(tagClass, button);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnTag(String tagName, int button, int modifier) {
			super(tagName, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnTag(Class tagClass, int button, int modifier) {
			super(tagClass, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public ClickOnTag(CTag tag, int button, int modifier) {
			super(tag, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(String tagName, int button, String outState) {
			super(tagName, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(Class tagClass, int button, String outState) {
			super(tagClass, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(CTag tag, int button, String outState) {
			super(tag, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged
		 * CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged
		 * CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse clicked event on a tagged
		 * CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public ClickOnTag(CTag tag, int button, int modifier, String outState) {
			super(tag, button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_CLICKED);
		}
	}

	/**
	 * A transition triggered by a mouse moved event with a button pressed on a
	 * CShape with a given tag.
	 * 
	 * @author Caroline Appert
	 */
	public class DragOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public DragOnTag(Class tagClass) {
			super(tagClass);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier and any button on a tagged CShape that loops on the current
		 * state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public DragOnTag(CTag tag) {
			super(tag);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(Class tagClass, String outState) {
			super(tagClass, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier and any button on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(CTag tag, String outState) {
			super(tag, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public DragOnTag(String tagName, int button) {
			super(tagName, button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public DragOnTag(Class tagClass, int button) {
			super(tagClass, button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 */
		public DragOnTag(CTag tag, int button) {
			super(tag, button);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape
		 * that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnTag(String tagName, int button, int modifier) {
			super(tagName, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape
		 * that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnTag(Class tagClass, int button, int modifier) {
			super(tagClass, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape
		 * that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public DragOnTag(CTag tag, int button, int modifier) {
			super(tag, button, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(String tagName, int button, String outState) {
			super(tagName, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape.
		 * 
		 * @param tagClass
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(Class tagClass, int button, String outState) {
			super(tagClass, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event with any
		 * modifier on a shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(CTag tag, int button, String outState) {
			super(tag, button, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(String tagName, int button, int modifier, String outState) {
			super(tagName, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(Class tagClass, int button, int modifier, String outState) {
			super(tagClass, button, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse dragged event on a shape.
		 * 
		 * @param tag
		 *            The tag
		 * @param button
		 *            The button of the mouse event: NOBUTTON, BUTTON1, BUTTON2
		 *            or BUTTON3
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public DragOnTag(CTag tag, int button, int modifier, String outState) {
			super(tag, button, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_DRAGGED);
		}

	}

	/**
	 * A transition triggered by a mouse moved event with no button pressed on a
	 * CShape with a given tag.
	 * 
	 * @author Caroline Appert
	 */
	public class MoveOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse moved event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 */
		public MoveOnTag(String tagName) {
			super(tagName, NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse moved event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public MoveOnTag(Class tagClass) {
			super(tagClass, NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse moved event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public MoveOnTag(CTag tag) {
			super(tag, NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnTag(String tagName, int modifier) {
			super(tagName, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnTag(Class tagClass, int modifier) {
			super(tagClass, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public MoveOnTag(CTag tag, int modifier) {
			super(tag, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse moved event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnTag(String tagName, String outState) {
			super(tagName, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse moved event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnTag(Class tagClass, String outState) {
			super(tagClass, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse moved event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnTag(CTag tag, String outState) {
			super(tag, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a tagged
		 * CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnTag(String tagName, int modifier, String outState) {
			super(tagName, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a tagged
		 * CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse moved event on a tagged
		 * CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public MoveOnTag(CTag tag, int modifier, String outState) {
			super(tag, NOBUTTON, modifier, outState);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_MOVED);
		}
	}

	/**
	 * A transition triggered by a mouse wheel event with no button pressed on a
	 * CShape with a given tag.
	 * 
	 * @author Caroline Appert
	 */
	public class WheelOnTag extends MouseOnTag {

		/**
		 * Builds a transition triggered by a mouse wheel event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 */
		public WheelOnTag(String tagName) {
			super(tagName, NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 */
		public WheelOnTag(Class tagClass) {
			super(tagClass, NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any
		 * modifier on a tagged CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 */
		public WheelOnTag(CTag tag) {
			super(tag, NOBUTTON);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnTag(String tagName, int modifier) {
			super(tagName, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnTag(Class tagClass, int modifier) {
			super(tagClass, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged
		 * CShape that loops on the current state.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 */
		public WheelOnTag(CTag tag, int modifier) {
			super(tag, NOBUTTON, modifier);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnTag(String tagName, String outState) {
			super(tagName, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnTag(Class tagClass, String outState) {
			super(tagClass, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event with any
		 * modifier on a tagged CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnTag(CTag tag, String outState) {
			super(tag, NOBUTTON, ANYMODIFIER, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged
		 * CShape.
		 * 
		 * @param tagName
		 *            The name of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnTag(String tagName, int modifier, String outState) {
			super(tagName, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged
		 * CShape.
		 * 
		 * @param tagClass
		 *            The class of the tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnTag(Class tagClass, int modifier, String outState) {
			super(tagClass, NOBUTTON, modifier, outState);
		}

		/**
		 * Builds a transition triggered by a mouse wheel event on a tagged
		 * CShape.
		 * 
		 * @param tag
		 *            The tag
		 * @param modifier
		 *            The modifier: NOMODIFIER, CONTROL, ALT, SHIFT,
		 *            ALT_CONTROL, CONTROL_SHIFT, ALT_SHIFT or ALT_CONTROL_SHIFT
		 * @param outState
		 *            The name of the output state
		 */
		public WheelOnTag(CTag tag, int modifier, String outState) {
			super(tag, NOBUTTON, modifier, outState);
		}

		/**
		 * @return the number of units that should be scrolled in response to
		 *         this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollAmount()
		 */
		public int getScrollAmount() {
			return ((MouseWheelEvent) triggeringEvent).getWheelRotation();
		}

		/**
		 * @return the type of scrolling that should take place in response to
		 *         this event.
		 * @see java.awt.event.MouseWheelEvent#getScrollType()
		 */
		public int getScrollType() {
			return ((MouseWheelEvent) triggeringEvent).getWheelRotation();
		}

		/**
		 * @return This is a convenience method to aid in the implementation of
		 *         the common-case MouseWheelListener - to scroll a ScrollPane
		 *         or JScrollPane by an amount which conforms to the platform
		 *         settings.
		 * @see java.awt.event.MouseWheelEvent#getUnitsToScroll()
		 */
		public int getUnitsToScroll() {
			return ((MouseWheelEvent) triggeringEvent).getWheelRotation();
		}

		/**
		 * @return the number of "clicks" the mouse wheel was rotated.
		 * @see java.awt.event.MouseWheelEvent#getWheelRotation()
		 */
		public int getWheelRotation() {
			return ((MouseWheelEvent) triggeringEvent).getWheelRotation();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(EventObject eventObject) {
			return matches(eventObject, MouseEvent.MOUSE_WHEEL);
		}
	}

}
