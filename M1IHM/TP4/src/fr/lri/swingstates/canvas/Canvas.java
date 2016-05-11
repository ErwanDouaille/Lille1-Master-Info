/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
 */
package fr.lri.swingstates.canvas;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;

import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.canvas.CStateMachine.CElementEvent;
import fr.lri.swingstates.canvas.CStateMachine.ClickOnShape;
import fr.lri.swingstates.canvas.CStateMachine.ClickOnTag;
import fr.lri.swingstates.canvas.CStateMachine.DragOnShape;
import fr.lri.swingstates.canvas.CStateMachine.DragOnTag;
import fr.lri.swingstates.canvas.CStateMachine.EnterOnShape;
import fr.lri.swingstates.canvas.CStateMachine.EnterOnTag;
import fr.lri.swingstates.canvas.CStateMachine.EventOnShape;
import fr.lri.swingstates.canvas.CStateMachine.EventOnTag;
import fr.lri.swingstates.canvas.CStateMachine.LeaveOnShape;
import fr.lri.swingstates.canvas.CStateMachine.LeaveOnTag;
import fr.lri.swingstates.canvas.CStateMachine.MoveOnShape;
import fr.lri.swingstates.canvas.CStateMachine.MoveOnTag;
import fr.lri.swingstates.canvas.CStateMachine.ReleaseOnShape;
import fr.lri.swingstates.canvas.CStateMachine.ReleaseOnTag;
import fr.lri.swingstates.canvas.CStateMachine.WheelOnShape;
import fr.lri.swingstates.canvas.CStateMachine.WheelOnTag;
import fr.lri.swingstates.events.Picker;
import fr.lri.swingstates.events.PickerCEvent;
import fr.lri.swingstates.events.PickerEvent;
import fr.lri.swingstates.events.VirtualCanvasEvent;
import fr.lri.swingstates.events.VirtualEvent;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Enter;

/**
 * <p>
 * A canvas that displays a scene composed of graphical objects. A canvas handles
 * a <i>display list</i> containing <code>CShape</code> objects. It is
 * ordered from back to front, i.e. the frontmost object is the last in the list.
 * Whenever the contents of the display list is modified, a repaint is triggered to update the display.
 * </p>
 * 
 * <p>
 * The <i>display list</i> contains <code>CShape</code> objects. It is
 * ordered from back to front, i.e. the frontmost object is the last in the
 * list. Whenever the contents of the display list is modified, a repaint is
 * triggered to update the display.
 * 
 * <p>
 * <code>CStateMachine</code> is a class that implements state machines dedicated 
 * to program interaction with a canvas. See <code>StateMachine</code> 
 * for a description of the syntax and use of state machines.
 * Once a state machine is attached to a canvas and is active, it receives and handles all input events.
 * The canvas may have several active state machines; they receive and handle the same events. State machines can be attached
 * to a subpart of a canvas, i.e. a shape or a tag, to reduce their scope.
 * </p>
 * 
 * <p>
 * Events handled by state machines are mouse events, the keyboard events,
 * the time out events (triggered by <code>armTimer</code>) and animation events.
 * </p>
 *
 * @see fr.lri.swingstates.canvas.CStateMachine
 * @see fr.lri.swingstates.canvas.CShape
 * @author Caroline Appert
 */
public class Canvas extends JPanel implements MouseListener,
MouseMotionListener, MouseWheelListener, KeyListener, CElement {

	private static final long serialVersionUID = 1L;

	private Picker defaultPicker = new Picker() {
		private Point2D location = new Point2D.Double(-10, -10);

		/**
		 * {@inheritDoc}
		 */
		public void move(Point2D pt) {
			location = pt;
		}

		/**
		 * {@inheritDoc}
		 */
		public Point2D getLocation() {
			return location;
		}
	};
	/**
	 * When this picker leaves the bounds of this canvas, this canvas loses
	 * focus.
	 */
	protected Picker masterPicker = defaultPicker;

	protected RenderingHints renderingHints = null;
	protected AlphaComposite transparency = null;
	protected AffineTransform transform = null;

	protected Shape clip;

	protected List<CStateMachine> stateMachines = null;
	protected List<CShape> displayOrder;

	protected List<CTag> allCanvasTags = null;

	/**
	 * The active pickers on this <code>Canvas</code>.
	 */
	protected Vector<Picker> pickers = new Vector<Picker>();

	private boolean listenerAttached = false;

	/**
	 * The last <code>CWidget</code> that has been picked by one of the active
	 * pickers. So keyboard events are dispatched to this widget.
	 */
	private CWidget widgetFocused = null;

	/**
	 * The current picked shape for each active picker.
	 */
	private Vector<CShape> pickedShapes = new Vector<CShape>();

	/**
	 * @return all the tags registered on this <code>Canvas</code>.
	 */
	public List<CTag> getAllTags() {
		return allCanvasTags;
	}

	/**
	 * Builds a Canvas.
	 */
	public Canvas() {
		super();
		displayOrder = Collections.synchronizedList(new LinkedList<CShape>());
		stateMachines = Collections.synchronizedList(new LinkedList<CStateMachine>());
		allCanvasTags = Collections.synchronizedList(new LinkedList<CTag>());
		addPicker(masterPicker);
		setBackground(Color.WHITE);
		repaint();
	}

	/**
	 * Builds a Canvas.
	 * 
	 * @param w
	 *            The width of the canvas
	 * @param h
	 *            The height of the canvas
	 */
	public Canvas(int w, int h) {
		this();
		setPreferredSize(new Dimension(w, h));
	}

	protected void addPicker(Picker picker) {
		pickers.add(picker);
		pickedShapes.add(pick(picker.getLocation()));
	}

	protected void removePicker(Picker picker) {
		int indexRemoved = pickers.indexOf(picker);
		if (indexRemoved != -1) {
			pickers.remove(indexRemoved);
			pickedShapes.remove(indexRemoved);
		}
	}

	/**
	 * Registers <code>tag</code> in this canvas.
	 * 
	 * @param tag
	 *            The tag to register
	 */
	void registerTag(CTag tag) {
		if (!allCanvasTags.contains(tag))
			allCanvasTags.add(tag);
	}

	/**
	 * Returns the tag object given its name.
	 * 
	 * @param tag
	 *            The name of the tag
	 * @return The tag, or null if no such tag exists.
	 */
	public CNamedTag getTag(String tag) {
		synchronized(allCanvasTags) {
			for (Iterator<CTag> i = allCanvasTags.iterator(); i.hasNext();) {
				CTag next = i.next();
				if (next instanceof CNamedTag)
					if (((CNamedTag) next).getName().compareTo(tag) == 0) {
						((CNamedTag) next).reset();
						return (CNamedTag) next;
					}
			}
		}
		return null;
	}

	/**
	 * Returns an intentional tag object grouping shapes having a tag of a given
	 * class.
	 * 
	 * @param tagClass
	 *            The class of the tag
	 * @return The tag, or null if no such tag exists.
	 */
	public ClassTag getTag(Class tagClass) {
		return ClassTag.getTag(this, tagClass);
	}

	/**
	 * Installs listeners on this canvas.
	 */
	void attachListeners() {
		if (listenerAttached)
			return;
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		addMouseWheelListener(this);
		listenerAttached = true;
	}

	/**
	 * Uninstalls listeners on this canvas.
	 */
	void detachListeners() {
		if (!listenerAttached)
			return;
		removeMouseMotionListener(this);
		removeMouseListener(this);
		removeKeyListener(this);
		removeMouseWheelListener(this);
		listenerAttached = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		transparency = (AlphaComposite) g2d.getComposite();
		transform = g2d.getTransform();

		if (renderingHints != null) g2d.addRenderingHints(renderingHints);

		if (clip != null)
			g2d.setClip(clip);
		else
			g2d.setClip(g2d.getClip());

		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = (CShape) (i.next());
				if (sms.isDrawable() && sms.isVisible())
					sms.paint(g);
			}
		}

		// to process enter/leave events if needed
		if (hasTransitionOfClass(EnterOnShape.class)
				|| hasTransitionOfClass(EnterOnTag.class)
				|| hasTransitionOfClass(LeaveOnShape.class)
				|| hasTransitionOfClass(LeaveOnTag.class)) {
			updatePickers(true);
		}

	}

	/**
	 * Processes an event to all the state machines that monitor this canvas, a
	 * shape in this canvas or a tag attached to shapes in this canvas. Performs
	 * picking using the location <code>pt</code>.
	 * 
	 * @param event
	 *            The name of the virtual event to process.
	 * @param pt
	 *            The point on which this event occurred.
	 */
	public void processEvent(String event, Point2D pt) {
		boolean isConsumed = false;
		CShape picked = pick(pt);
		VirtualEvent toProcess = new VirtualCanvasEvent(event, picked, pt);
		toProcess.setSource(this);
		CStateMachine machine;
		synchronized(stateMachines) {
			Iterator<CStateMachine> i = stateMachines.iterator(); // Must be in synchronized block
			while (i.hasNext()) {
				if (isConsumed)
					break;
				machine = i.next();
				machine.consumes(false);
				if (machine.getCurrentState() == null)
					continue;
				machine.processEvent(toProcess);
				isConsumed = machine.hasConsumed();
			}
		}
	}

	/**
	 * Processes an event to all the state machines that monitor this canvas, a
	 * shape in this canvas or a tag attached to shapes in this canvas.
	 * 
	 * If <code>virtualEvent</code> has the type <code>VirtualCanvasEvent</code>,
	 * this method performs picking on the canvas so transitions <code>EventOnShape</code>
	 * and <code>EventOnTag</code> can be triggered.
	 * 
	 * @param virtualEvent
	 *            The virtual event to process.
	 */
	public void processEvent(VirtualEvent virtualEvent) {
		boolean isConsumed = false;
		virtualEvent.setSource(this);
		if(virtualEvent instanceof VirtualCanvasEvent) {
			if (hasTransitionOfClass(EventOnShape.class)
					|| hasTransitionOfClass(EventOnTag.class)) {
				((VirtualCanvasEvent)virtualEvent).setShape(pick(((VirtualCanvasEvent)virtualEvent).getPoint()));
			}
		}
		CStateMachine machine;
		synchronized(stateMachines) {
			Iterator<CStateMachine> i = stateMachines.iterator(); 
			while (i.hasNext()) {
				if (isConsumed)
					break;
				machine = i.next();
				machine.consumes(false);
				if (machine.getCurrentState() == null)
					continue;
				machine.processEvent(virtualEvent);
				isConsumed = machine.hasConsumed();
			}
		}
	}

	/**
	 * Tests if a <code>CElement</code> is a subset of another
	 * <code>CElement</code>.
	 * 
	 * @param container
	 *            The <code>CElement</code> container
	 * @param element
	 *            The <code>CElement</code> contained
	 * @return true if <code>element</code> is a subset of
	 *         <code>container</code>.
	 */
	public static boolean contains(CElement container, CElement element) {
		if (container instanceof Canvas) {
			return element.getCanvas() == container;
		} else {
			if (container instanceof CTag) {
				if (element instanceof Canvas) {
					return false;
				} else {
					if (element instanceof CTag) {
						return container == element;
					} else {
						// element instanceof CShape
						return element.hasTag((CTag) container);
					}
				}
			} else {
				// container instanceof CShape
				return container == element;
			}
		}

	}

	/**
	 * Tests if one of the attached state machines is waiting for changes of
	 * <code>element</code>. (if there is one machine whose current state
	 * contains a <code>CElementEvent(element, ...)</code> transition).
	 * 
	 * @param element
	 *            The element.
	 * @return true if <code>element</code> is tracked by a machine, false
	 *         otherwise.
	 */
	public boolean isTracking(CElement element) {
		CStateMachine machine;
		synchronized(stateMachines) {
			Iterator<CStateMachine> i = stateMachines.iterator(); 
			while (i.hasNext()) {
				machine = i.next();
				machine.consumes(false);
				if (machine.isActive()) {
					// because animation can fire events before the machine is initialized
					if (machine.getCurrentState() == null) {
						machine.initStatesAndTransitions();
						continue;
					}
					for (Iterator<Transition> j = machine.getCurrentState()
							.getTransitions().iterator(); j.hasNext();) {
						Transition nextTrans = j.next();
						if (nextTrans instanceof CElementEvent) {
							CElement elem = ((CElementEvent) nextTrans)
							.getCElement();
							if (contains(elem, element))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Processes an event in all machines attached to this canvas.
	 * 
	 * @param e
	 *            The event to process
	 */
	private void processEvent(InputEvent e) {
		CStateMachine machine;
		boolean isConsumed = false;
		synchronized(stateMachines) {
			Iterator<CStateMachine> i = stateMachines.iterator(); // Must be in synchronized block
			while (i.hasNext()) {
				machine = i.next();
				if (isConsumed)
					break;
				machine.consumes(false);
				if (machine.isActive()) {
					machine.processEvent(e);
					isConsumed = machine.hasConsumed();
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseWheelMoved(MouseWheelEvent event) {
		if (event.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(event.getPoint());
		}
		pickAndProcess(event);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(MouseEvent event) {
		if (event.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(event.getPoint());
		}
		pickAndProcess(event);
		requestFocusInWindow();
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent event) {
		if (event.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(event.getPoint());
		}
		pickAndProcess(event);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(MouseEvent event) {
		if (event.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(event.getPoint());
		}
		if (hasTransitionOfClass(Enter.class)
				|| hasTransitionOfClass(EnterOnShape.class)
				|| hasTransitionOfClass(EnterOnTag.class)) {
			processEvent(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent event) {
		if (event.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(event.getPoint());
		}
		if (hasTransitionOfClass(LeaveOnShape.class)
				|| hasTransitionOfClass(LeaveOnTag.class)) {
			processEvent(event);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseDragged(MouseEvent event) {
		// Make the default picker move
		if (event.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(event.getPoint());
		}
		pickAndProcess(event);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mouseMoved(MouseEvent arg0) {
		// Make the default picker move
		if (arg0.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(arg0.getPoint());
		}
		pickAndProcess(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent event) {
		if (event.getClass().equals(MouseEvent.class) && masterPicker != null) {
			masterPicker.move(event.getPoint());
		}
		pickAndProcess(event);
	}

	/**
	 * Tests whether or not one of the attached state machines has a transition
	 * of a given class <code>cl</code>.
	 * 
	 * @param cl
	 *            The class of transitions
	 * @return true if there is at least one state machine whose current state
	 *         contained a transition of class <code>cl</code>.
	 */
	protected boolean hasTransitionOfClass(Class cl) {
		CStateMachine machine;
		synchronized(stateMachines) {
			Iterator<CStateMachine> i = stateMachines.iterator(); // Must be in synchronized block
			while (i.hasNext()) {
				machine = i.next();
				if (machine.isActive()) {
					if (machine.getCurrentState() == null)
						continue;
					if (machine.hasTransitionOfClass(cl))
						return true;
				}
			}
		}
		return false;
	}

	// events incoming from java.awt (MouseEvent)
	// or from any other GMouse (PickerEvent)
	private void pickAndProcess(MouseEvent event) {
		Picker eventPicker;
		if (event instanceof PickerEvent)
			eventPicker = ((PickerEvent) event).getPicker();
		else {
			if (masterPicker == null)
				return;
			eventPicker = masterPicker;
		}
		switch (event.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			// In this case, we always update the picking because of key events
			// that must reach potential CWidget and CDynamicWidget even if the
			// machine does not contain *OnShape or *OnTag transitions. Underlying
			// widgets must get keyboard focus that is acquired by mouse presses.
			updatePicker(eventPicker);
			processModifiedEvent(eventPicker, event);
			return;
		case MouseEvent.MOUSE_RELEASED:
			if (hasTransitionOfClass(ReleaseOnShape.class)
					|| hasTransitionOfClass(ReleaseOnTag.class)) {
				updatePicker(eventPicker);
			}
			processModifiedEvent(eventPicker, event);
			return;
		case MouseEvent.MOUSE_CLICKED:
			if (hasTransitionOfClass(ClickOnShape.class)
					|| hasTransitionOfClass(ClickOnTag.class)) {
				updatePicker(eventPicker);
			}
			processModifiedEvent(eventPicker, event);
			return;
		case MouseEvent.MOUSE_WHEEL:
			if (hasTransitionOfClass(WheelOnShape.class)
					|| hasTransitionOfClass(WheelOnTag.class)) {
				updatePicker(eventPicker);
			}
			processModifiedEvent(eventPicker, event);
			return;
		case MouseEvent.MOUSE_DRAGGED:
			if (hasTransitionOfClass(EnterOnShape.class)
					|| hasTransitionOfClass(EnterOnTag.class)
					|| hasTransitionOfClass(LeaveOnShape.class)
					|| hasTransitionOfClass(LeaveOnTag.class)
					|| hasTransitionOfClass(DragOnShape.class)
					|| hasTransitionOfClass(DragOnTag.class)) {
				updatePicker(eventPicker);
			}
			processModifiedEvent(eventPicker, event);
			return;
		case MouseEvent.MOUSE_MOVED:
			if (hasTransitionOfClass(EnterOnShape.class)
					|| hasTransitionOfClass(EnterOnTag.class)
					|| hasTransitionOfClass(LeaveOnShape.class)
					|| hasTransitionOfClass(LeaveOnTag.class)
					|| hasTransitionOfClass(MoveOnShape.class)
					|| hasTransitionOfClass(MoveOnTag.class)) {
				updatePicker(eventPicker);
			}
			processModifiedEvent(eventPicker, event);
			return;
		default:
			System.out.println("unknown event type: " + event.getID());
		}
	}

	private void updatePicker(Picker movedPicker) {
		int index = pickers.indexOf(movedPicker);
		CShape newPicked = pick(movedPicker.getLocation());
		CShape previousPicked = pickedShapes.remove(index);
		pickedShapes.add(index, newPicked);
		if (previousPicked != newPicked) {
			long time = System.currentTimeMillis();
			if (previousPicked != null) {
				dispatchEvent(new PickerCEvent(this, previousPicked,
						movedPicker, MouseEvent.MOUSE_EXITED, time, 0, (int) movedPicker
						.getLocation().getX(), (int) movedPicker
						.getLocation().getY(), 0, false));
			}
			if (newPicked != null) {
				dispatchEvent(new PickerCEvent(this, newPicked, movedPicker,
						MouseEvent.MOUSE_ENTERED, time,
						0, (int) movedPicker.getLocation().getX(),
						(int) movedPicker.getLocation().getY(), 0, false));
			}
		}
	}

	private void updatePickers(boolean fire) {
		for (Iterator<Picker> i = pickers.iterator(); i.hasNext();) {
			Picker movedPicker = i.next();
			updatePicker(movedPicker);
		}
	}

	/**
	 * Enhances a mouse event with the picked shape.
	 * 
	 * @param initialEvent
	 *            the initial nouse event.
	 */
	protected void processModifiedEvent(Picker eventPicker,
			MouseEvent initialEvent) {
		int index = pickers.indexOf(eventPicker);
		CShape picked = pickedShapes.get(index);
		if (picked != null) {
			PickerCEvent eventToDispatch = initialEvent.getID() == MouseEvent.MOUSE_WHEEL ? new PickerCEvent(
					(Component) initialEvent.getSource(), picked, eventPicker,
					initialEvent.getID(), initialEvent.getWhen(), initialEvent
					.getModifiers(), (int) initialEvent.getPoint()
					.getX(), (int) initialEvent.getPoint().getY(),
					initialEvent.getClickCount(),
					initialEvent.isPopupTrigger(),
					((MouseWheelEvent) initialEvent).getScrollType(),
					((MouseWheelEvent) initialEvent).getScrollAmount(),
					((MouseWheelEvent) initialEvent).getWheelRotation())
			: new PickerCEvent((Component) initialEvent.getSource(),
					picked, eventPicker, initialEvent.getID(),
					initialEvent.getWhen(),
					initialEvent.getModifiers(), (int) initialEvent
					.getPoint().getX(), (int) initialEvent
					.getPoint().getY(), initialEvent
					.getClickCount(), initialEvent
					.isPopupTrigger());
					processEvent(eventToDispatch);

					if (picked instanceof CWidget) {
						widgetFocused = (CWidget) picked;
						if (widgetFocused.isBasicListener()) {
							widgetFocused.sendEvent(eventPicker.getLocation(),
									initialEvent);
						}
					}
		} else
			processEvent(initialEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyTyped(KeyEvent arg0) {
		processEvent(arg0);
		if (widgetFocused != null && widgetFocused.isBasicListener())
			widgetFocused.sendEvent(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyPressed(KeyEvent arg0) {
		processEvent(arg0);
		if (widgetFocused != null && widgetFocused.isBasicListener())
			widgetFocused.sendEvent(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public void keyReleased(KeyEvent arg0) {
		processEvent(arg0);
		if (widgetFocused != null && widgetFocused.isBasicListener())
			widgetFocused.sendEvent(arg0);
	}

	/**
	 * Returns the topmost shape at a given position.
	 * 
	 * @param p
	 *            The position.
	 * @return the topmost shape under <code>p</code>, or <code>null</code>
	 *         if there is no shape at this postion.
	 */
	public CShape pick(Point2D p) {
		CShape picked = null;
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isPickable())
					if (sms.pick(p, 2) != null)
						picked = sms;
			}
		}
		return picked;
	}

	/**
	 * Returns a list containing all the shapes at a given position.
	 * 
	 * @param p
	 *            The position.
	 * @return the list of shapes, with the topmost shape first.
	 */
	public LinkedList<CShape> pickAll(Point2D p) {
		LinkedList<CShape> pickedShapes = new LinkedList<CShape>();
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = (CShape) (i.next());
				if (sms.isPickable())
					if (sms.pick(p, 2) != null)
						pickedShapes.addFirst(sms);
			}
		}
		return pickedShapes;
	}

	/**
	 * Returns the top most shape having a given tag at a given position.
	 * 
	 * @param p
	 *            The position.
	 * @param tag
	 *            The tag.
	 * @return the picked tagged shape, null if there is no tagged shape at
	 *         <code>p</code>.
	 */
	public CShape pickShapeHavingTag(Point2D p, CTag tag) {
		CShape picked = null;
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isPickable())
					if (sms.pick(p, 2) != null)
						if (sms.hasTag(tag))
							picked = sms;
			}
		}
		return picked;
	}

	/**
	 * Returns the top most shape having a given named tag at a given position.
	 * 
	 * @param p
	 *            The position.
	 * @param tag
	 *            The name of the tag.
	 * @return the picked tagged shape, null if there is no tagged shape at
	 *         <code>p</code>.
	 */
	public CShape pickShapeHavingTag(Point2D p, String tag) {
		CTag t = getTag(tag);
		if (t == null)
			return null;
		return pickShapeHavingTag(p, t);
	}

	/**
	 * Adds a shape to the canvas.
	 * 
	 * @param sms
	 *            the shape to add.
	 * @return this canvas.
	 */
	public Canvas addShape(CShape sms) {
		if (sms == null)
			return this;
		if (sms.getCanvas() == null) {
			sms.setCanvas(this);
			displayOrder.add(sms);
		}
		repaint();
		return this;
	}

	/**
	 * Removes a shape from the canvas. Before being removed, the tags of this
	 * shape are removed, the shape is removed from its hierarchy (its parent
	 * and children are set to null), and its ghost, if any, is removed.<br>
	 * <b>WARNING:</b> Since this method potentially modifies the tags 
	 * registered on this canvas, be sure to not use it while iterating
	 * on any of these canvas' tags.
	 * <br>
	 * <br>
	 * Sometimes, it can be more efficient or more convenient to just "hide"
	 * a shape by using the method <code>setDrawable</code> in class <code>CShape</code>.
	 * 
	 * @param shape
	 *            The shape to remove from this canvas.
	 * @return this canvas.
	 * 
	 * @see CShape#setDrawable(boolean)
	 */
	public Canvas removeShape(CShape shape) {
		if (shape == null)
			return this;
		if (shape.getCanvas() == this) {
			shape.prepareToRemove();
			displayOrder.remove(shape);
			repaint();
		}
		return this;
	}

	/**
	 * Removes a set of tagged shapes from the canvas.
	 * 
	 * @param shapes
	 *            The shapes that have to be deleted.
	 * @return this canvas.
	 */
	public Canvas removeShapes(CTag shapes) {
		if (shapes == null)
			return this;
		LinkedList<Object> taggedShapes = new LinkedList<Object>();
		for (shapes.reset(); shapes.hasNext();)
			taggedShapes.add(shapes.nextShape());
		for (Iterator<Object> i = taggedShapes.iterator(); i.hasNext();)
			((CShape) i.next()).prepareToRemove();
		displayOrder.removeAll(shapes.getCollection());
		repaint();
		return this;
	}

	/**
	 * Removes all shapes from this canvas.
	 * 
	 * @return this canvas.
	 */
	public Canvas removeAllShapes() {
		for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
			i.next().prepareToRemove();
		displayOrder.clear();
		repaint();
		return this;
	}

	/**
	 * Creates a new segment and add it to the canvas.
	 * 
	 * @param p1
	 *            The first point of the segment.
	 * @param p2
	 *            The second point of the segment.
	 * @return the newly created segment.
	 */
	public CSegment newSegment(Point2D p1, Point2D p2) {
		CSegment s = new CSegment(p1, p2);
		addShape(s);
		return s;
	}

	/**
	 * Creates a new segment and add it to the canvas.
	 * 
	 * @param x1
	 *            The x-coordinate of the first point of the segment.
	 * @param y1
	 *            The y-coordinate of the first point of the segment.
	 * @param x2
	 *            The x-coordinate of the second point of the segment.
	 * @param y2
	 *            The y-coordinate of the second point of the segment.
	 * @return the newly created segment.
	 */
	public CSegment newSegment(double x1, double y1, double x2, double y2) {
		CSegment s = new CSegment(x1, y1, x2, y2);
		addShape(s);
		return s;
	}

	/**
	 * Creates a new rectangle and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the upper left point of the bounding box.
	 * @param y
	 *            The y coordinate of the upper left point of the bounding box.
	 * @param w
	 *            The width of the bounding box.
	 * @param h
	 *            The height of the bounding box.
	 * @return the newly created rectangle.
	 */
	public CRectangle newRectangle(double x, double y, double w, double h) {
		CRectangle s = new CRectangle(x, y, w, h);
		addShape(s);
		return s;
	}

	/**
	 * Creates a new rectangle and add it to the canvas.
	 * 
	 * @param p
	 *            The upper left point of the bounding box.
	 * @param w
	 *            The width of the bounding box.
	 * @param h
	 *            The height of the bounding box.
	 * @return the newly created rectangle.
	 */
	public CRectangle newRectangle(Point2D p, double w, double h) {
		CRectangle s = new CRectangle(p, w, h);
		addShape(s);
		return s;
	}

	/**
	 * Creates a new rectangle and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the upper left point of the bounding box.
	 * @param y
	 *            The y coordinate of the upper left point of the bounding box.
	 * @param w
	 *            The width of the bounding box.
	 * @param h
	 *            The height of the bounding box.
	 * @param arcw
	 *            The width of the arc to use to round off the corners of the
	 *            newly constructed <code>CRectangle</code>
	 * @param arch
	 *            The height of the arc to use to round off the corners of the
	 *            newly constructed <code>CRectangle</code>
	 * @return the newly created <code>CRectangle</code>.
	 */
	public CRectangle newRoundRectangle(double x, double y, double w, double h,
			double arcw, double arch) {
		CRectangle s = new CRectangle(x, y, w, h, arcw, arch);
		addShape(s);
		return s;
	}

	/**
	 * Creates a new rectangle and add it to the canvas.
	 * 
	 * @param p
	 *            The upper left point of the bounding box.
	 * @param w
	 *            The width of the bounding box.
	 * @param h
	 *            The height of the bounding box.
	 * @param arcw
	 *            The width of the arc to use to round off the corners of the
	 *            newly constructed <code>CRectangle</code>
	 * @param arch
	 *            The height of the arc to use to round off the corners of the
	 *            newly constructed <code>CRectangle</code>
	 * @return the newly created <code>CRectangle</code>.
	 */
	public CRectangle newRoundRectangle(Point2D p, double w, double h,
			double arcw, double arch) {
		CRectangle s = new CRectangle(p, w, h, arcw, arch);
		addShape(s);
		return s;
	}

	/**
	 * Creates a new ellipse and add it to the canvas.
	 * 
	 * @param p
	 *            The upper left point of the bounding box.
	 * @param w
	 *            The width of the bounding box.
	 * @param h
	 *            The height of the bounding box.
	 * @return the newly created ellipse.
	 */
	public CEllipse newEllipse(Point2D p, double w, double h) {
		CEllipse s = new CEllipse(p, w, h);
		addShape(s);
		return s;
	}

	/**
	 * Creates a new ellipse and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the upper left point of the bounding box.
	 * @param y
	 *            The y coordinate of the upper left point of the bounding box.
	 * @param w
	 *            The width of the bounding box.
	 * @param h
	 *            The height of the bounding box.
	 * @return the newly created ellipse.
	 */
	public CEllipse newEllipse(double x, double y, double w, double h) {
		CEllipse s = new CEllipse(x, y, w, h);
		addShape(s);
		return s;
	}

	/**
	 * Create a new polyline and add it to the canvas.
	 * 
	 * @param p
	 *            The starting point.
	 * @return the newly created polyline.
	 */
	public CPolyLine newPolyLine(Point2D p) {
		CPolyLine s = new CPolyLine(p);
		addShape(s);
		return s;
	}

	/**
	 * Create a new empty polyline and add it to the canvas.
	 * 
	 * @return the newly created polyline.
	 */
	public CPolyLine newPolyLine() {
		CPolyLine s = new CPolyLine();
		addShape(s);
		return s;
	}

	/**
	 * Create a new polyline and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the starting point.
	 * @param y
	 *            The y coordinate of the starting point.
	 * @return the newly created polyline.
	 */
	public CPolyLine newPolyLine(double x, double y) {
		CPolyLine s = new CPolyLine(x, y);
		addShape(s);
		return s;
	}

	/**
	 * Create a new text and add it to the canvas.
	 * 
	 * @param p
	 *            The lower left point.
	 * @param text
	 *            The text to display.
	 * @param font
	 *            The font to use to render this text.
	 * @return the newly created text.
	 */
	public CText newText(Point2D p, String text, Font font) {
		CText s = new CText(p, text, font);
		addShape(s);
		return s;
	}

	/**
	 * Create a new text and add it to the canvas.
	 * 
	 * @param p
	 *            The lower left point.
	 * @param text
	 *            The text to display.
	 * @return the newly created text.
	 */
	public CText newText(Point2D p, String text) {
		CText s = new CText(p, text, new Font("verdana", Font.PLAIN, 12));
		addShape(s);
		return s;
	}

	/**
	 * Create a new text and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the lower left point.
	 * @param y
	 *            The y coordinate of the lower left point.
	 * @param text
	 *            The text to display.
	 * @param font
	 *            The font to use to render this text.
	 * @return the newly created text.
	 */
	public CText newText(double x, double y, String text, Font font) {
		CText s = new CText(new Point2D.Double(x, y), text, font);
		addShape(s);
		return s;
	}

	/**
	 * Create a new text and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the lower left point.
	 * @param y
	 *            The y coordinate of the lower left point.
	 * @param text
	 *            The text to display.
	 * @return the newly created text.
	 */
	public CText newText(double x, double y, String text) {
		CText s = newText(new Point2D.Double(x, y), text);
		addShape(s);
		return s;
	}

	/**
	 * Create a new image and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the lower left point.
	 * @param y
	 *            The y coordinate of the lower left point.
	 * @param imageFile
	 *            The name of the file image (gif, jpeg or png)
	 * @return the newly created image.
	 */
	public CImage newImage(double x, double y, String imageFile) {
		CImage s = new CImage(imageFile, new Point2D.Double(x, y));
		addShape(s);
		return s;
	}

	/**
	 * Create a new image and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the lower left point.
	 * @param y
	 *            The y coordinate of the lower left point.
	 * @param imageURL
	 *            The url of the file image (gif, jpeg or png)
	 * @return the newly created image.
	 */
	public CImage newImage(double x, double y, URL imageURL) {
		CImage s = new CImage(imageURL, new Point2D.Double(x, y));
		addShape(s);
		return s;
	}

	/**
	 * Create a new image and add it to the canvas.
	 * 
	 * @param x
	 *            The x coordinate of the lower left point.
	 * @param y
	 *            The y coordinate of the lower left point.
	 * @param imageStream
	 *            The url of the file image (gif, jpeg or png)
	 * @return the newly created image.
	 */
	public CImage newImage(double x, double y, InputStream imageStream) {
		CImage s = new CImage(imageStream, new Point2D.Double(x, y));
		addShape(s);
		return s;
	}

	/**
	 * Create a new widget and add it to the canvas.
	 * 
	 * @param b
	 *            The swing widget.
	 * @return The newly created widget.
	 */
	public CWidget newWidget(JComponent b) {
		CWidget smw = new CWidget(b);
		addShape(smw);
		return smw;
	}

	/**
	 * Create a new widget and add it to the canvas (i.e. the widget is
	 * refreshed each time it receives an input event).
	 * 
	 * @param b
	 *            The swing widget.
	 * @param x
	 *            The x coordinate of the upper left point.
	 * @param y
	 *            The y coordinate of the upper left point.
	 * @return The newly created widget.
	 */
	public CWidget newWidget(JComponent b, double x, double y) {
		CWidget smw = new CWidget(b, x, y);
		addShape(smw);
		return smw;
	}

	/**
	 * Create a new dynamic widget and add it to the canvas (i.e. a widget that
	 * must be refreshed every <code>delayInMilliseconds</code> milliseconds,
	 * e.g. a video or an animated widget).
	 * 
	 * @param b
	 *            The swing widget.
	 * @param x
	 *            The x coordinate of the upper left point.
	 * @param y
	 *            The y coordinate of the upper left point.
	 * @param delayInMilliseconds
	 *            The refreshing rate
	 * @return The newly created widget.
	 */
	public CDynamicWidget newDynamicWidget(JComponent b, double x, double y,
			int delayInMilliseconds) {
		CDynamicWidget smw = new CDynamicWidget(b, x, y, delayInMilliseconds);
		addShape(smw);
		return smw;
	}

	/**
	 * Create a new widget and add it to the canvas.
	 * 
	 * @param b
	 *            The swing widget.
	 * @param x
	 *            The x coordinate of the upper left point.
	 * @param y
	 *            The y coordinate of the upper left point.
	 * @param w
	 *            The width of the bounding box.
	 * @param h
	 *            The height of the bounding box.
	 * @return The newly created widget.
	 */
	public CWidget newWidget(JComponent b, double x, double y, double w,
			double h) {
		CWidget smw = new CWidget(b, x, y, w, h);
		addShape(smw);
		return smw;
	}

	/**
	 * Returns the state machines attached to this canvas.
	 * 
	 * @return the state machines attached to this canvas.
	 */
	public List<CStateMachine> getSMs() {
		return stateMachines;
	}

	/**
	 * Returns True if the canvas is antialiased.
	 * 
	 * @return the value of the global antialiasing.
	 */
	public boolean isAntialiased() {
		if (renderingHints == null)
			return false;
		else
			return renderingHints.get(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON;
	}

	/**
	 * Sets the rendering hints for every CShape drawn in this canvas (global
	 * rendering hints). NOTE: Calling this method instead of calling
	 * <code>setRenderingHint</code> for each shape in the canvas is more
	 * efficient (the same <code>RenderingHints</code> is referenced by all
	 * shapes).
	 * 
	 * @param hintKey
	 *            The hint key
	 * @param hintValue
	 *            The hint value
	 * @return this canvas
	 */
	public CElement setRenderingHint(Key hintKey, Object hintValue) {
		if (renderingHints == null)
			renderingHints = new RenderingHints(hintKey, hintValue);
		else
			renderingHints.add(new RenderingHints(hintKey, hintValue));
		return this;
	}

	/**
	 * Sets the antialiasing for every CShape drawn in this canvas (global
	 * antialiasing). NOTE: Calling this method instead of calling
	 * <code>setAntialiased</code> for each shape in the canvas is more
	 * efficient (the same <code>RenderingHints</code> is referenced by all
	 * shapes).
	 * 
	 * @param a
	 *            True if the canvas is antialiased.
	 * @return this canvas
	 */
	public CElement setAntialiased(boolean a) {
		if(a) {
			setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		else {
			setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		repaint();
		return this;
	}

	/**
	 * Returns a named tag object given its name. If a tag with this name
	 * already exists, return it, otherwise create a new one.
	 * 
	 * @param t
	 *            The name of the tag
	 * @return The tag.
	 */
	public CNamedTag newTag(String t) {
		CNamedTag tag = getTag(t);
		if (tag == null) {
			tag = new CNamedTag(t);
			registerTag(tag);
		}
		return tag;
	}

	/**
	 * {@inheritDoc}
	 */
	public Canvas getCanvas() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void attachSM(CStateMachine sm, boolean reset) {
		sm.attachTo(this, reset);
	}

	/**
	 * {@inheritDoc}
	 */
	public void detachSM(CStateMachine sm) {
		sm.detach(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setShape(Shape sh) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setShape(sh);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setParent(CShape parent) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setParent(parent);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setStroke(Stroke str) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setStroke(str);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyFill(AlphaComposite transparencyFill) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setTransparencyFill(transparencyFill);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyFill(float alpha) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setTransparencyFill(alpha);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyOutline(AlphaComposite transparencyOutline) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setTransparencyOutline(transparencyOutline);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyOutline(float alpha) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setTransparencyOutline(alpha);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setFillPaint(Paint fp) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setFillPaint(fp);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setOutlinePaint(Paint op) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setOutlinePaint(op);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public LinkedList<CShape> getAntialiasedShapes() {
		LinkedList<CShape> res = new LinkedList<CShape>();
		CShape s;
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				s = i.next();
				if (s.isAntialiased())
					res.add(s);
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFilled() {
		boolean res = true;
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				if (!i.next().isFilled()) {
					res = false; break; 
				}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public LinkedList<CShape> getFilledShapes() {
		LinkedList<CShape> res = new LinkedList<CShape>();
		CShape s;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				s = i.next();
				if (s.isFilled())
					res.add(s);
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setFilled(boolean f) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setFilled(f);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOutlined() {
		boolean res = true;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				if (!i.next().isOutlined()) {
					res = false; break;
				}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public LinkedList<CShape> getOutlinedShapes() {
		LinkedList<CShape> res = new LinkedList<CShape>();
		CShape s;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				s = i.next();
				if (s.isOutlined())
					res.add(s);
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setOutlined(boolean f) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setFilled(f);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDrawable() {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				if (!i.next().isDrawable())
					return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setDrawable(boolean f) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setFilled(f);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isPickable() {
		boolean res = true;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				if (!i.next().isPickable()) {
					res = false; break;
				}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setPickable(boolean pick) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setPickable(pick);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setReferencePoint(double x, double y) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setReferencePoint(x, y);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setTransformToIdentity() {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().setTransformToIdentity();
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement translateBy(double tx, double ty) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().translateBy(tx, ty);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement translateTo(double tx, double ty) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().translateTo(tx, ty);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement scaleBy(double sx, double sy) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().scaleBy(sx, sy);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement scaleBy(double s) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().scaleBy(s);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement scaleTo(double sx, double sy) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().scaleTo(sx, sy);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement scaleTo(double s) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().scaleTo(s);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement rotateBy(double theta) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().rotateTo(theta);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement rotateTo(double theta) {
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().rotateTo(theta);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape contains(double x, double y) {
		return pick(new Point2D.Double(x, y));
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape contains(Point2D p) {
		return pick(p);
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape contains(double x, double y, double w, double h) {
		CShape res = null;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isPickable())
					if (sms.contains(x, y, w, h) != null)
						res = sms;
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape contains(Rectangle r) {
		CShape res = null;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isPickable())
					if (sms.contains(r) != null)
						res = sms;
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape isOnOutline(Point2D p) {
		CShape res = null;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isPickable())
					if (sms.isOnOutline(p) != null)
						res = sms;
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape intersects(CShape s) {
		CShape res = null;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isPickable())
					if (sms.intersects(s) != null)
						res = sms;
			}
		}
		return res;
	}

	/**
	 * Tests whether a <code>CShape</code> s intersects this
	 * <code>CElement</code>.
	 * 
	 * @param s
	 *            The shape
	 * @return the intersection as a <code>CPolyLine</code> if s intersects
	 *         it, null otherwise.
	 * @see fr.lri.swingstates.canvas.CShape#getIntersection(CShape)
	 */
	public CPolyLine getIntersection(CShape s) {
		Area areaS = new Area(s.getAbsShape());
		Area areaSms = null;
		Area res = new Area();
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isPickable()) {
					areaSms = new Area(sms.getAbsShape());
					areaSms.intersect(areaS);
					if (!areaSms.isEmpty()) {
						res.add(areaSms);
					}
				}
			}
		}
		if (res.isEmpty())
			return null;
		GeneralPath gp = new GeneralPath();
		gp.append(res.getPathIterator(null), false);
		CPolyLine intersection = new CPolyLine();
		intersection.setShape(gp);
		return intersection;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstHavingTag(CTag t) {
		CShape res = null;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.hasTag(t)) {
					res = sms;
				}
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasTag(String t) {
		if (t == null)
			return true;
		boolean res = true;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				if (!(i.next().hasTag(t))) {
					res = false; break;
				}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasTag(CTag t) {
		if (t == null)
			return true;
		boolean res = true;
		synchronized (displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				if (!(i.next().hasTag(t))) {
					res = false; break;
				}
		}
		return res;
	}

	/**
	 * Does Anything. {@inheritDoc}
	 */
	public CElement above(CElement before) {
		return this;
	}

	/**
	 * Does Anything. {@inheritDoc}
	 */
	public CElement aboveAll() {
		return this;
	}

	/**
	 * Does Anything. {@inheritDoc}
	 */
	public CElement below(CElement after) {
		return this;
	}

	/**
	 * Does Anything. {@inheritDoc}
	 */
	public CElement belowAll() {
		return this;
	}

	/**
	 * Sets the clip for every CShape drawn in this canvas (global rendering
	 * hints). NOTE: Calling this method instead of calling <code>setClip</code>
	 * for each shape in the canvas is more efficient (the same
	 * <code>CShape</code> clip is referenced by all shapes).
	 * 
	 * @param clip
	 *            The clipping shape
	 * @return this canvas
	 */
	public CElement setClip(CShape clip) {
		this.clip = clip.getAbsShape();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addGhost() {
		List<CShape> displayOrderCopy = new LinkedList<CShape>();
		// To avoid deadlock (addGhost in class CShape calls 
		// addShape in class Canvas which has to access safely to the display list
		synchronized(displayOrder) {
			for (Iterator<CShape> iter = displayOrder.iterator(); iter.hasNext();) {
				displayOrderCopy.add(iter.next());
			}
		}
		for (Iterator<CShape> i = displayOrderCopy.iterator(); i.hasNext();)
			i.next().addGhost();
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeGhost() {
		List<CShape> displayOrderCopy = new LinkedList<CShape>();
		synchronized(displayOrder) {
			for (Iterator<CShape> iter = displayOrder.iterator(); iter.hasNext();) {
				displayOrderCopy.add(iter.next());
			}
		}
		for (Iterator<CShape> i = displayOrderCopy.iterator(); i.hasNext();)
			i.next().removeGhost();
	}

	/**
	 * {@inheritDoc}
	 */
	public CRectangle getBoundingBox() {
		return new CRectangle(getMinX(), getMinY(), Math.abs(getMaxX()
				- getMinX()), Math.abs(getMaxY() - getMinY()));
	}

	/**
	 * {@inheritDoc}
	 */
	public double getMinX() {
		double minX = Double.MAX_VALUE;
		for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
			minX = Math.min(i.next().getMinX(), minX);
		return minX;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getMaxX() {
		double maxX = Double.MIN_VALUE;
		for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
			maxX = Math.max(i.next().getMaxX(), maxX);
		return maxX;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getCenterX() {
		return (getMinX() + getMaxX()) / 2;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getMinY() {
		double minY = Double.MAX_VALUE;
		for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
			minY = Math.min(i.next().getMinY(), minY);
		return minY;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getMaxY() {
		double maxY = Double.MIN_VALUE;
		for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
			maxY = Math.max(i.next().getMaxY(), maxY);
		return maxY;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getCenterY() {
		return (getMinY() + getMaxY()) / 2;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape firstShape() {
		if (displayOrder.size() > 1)
			return displayOrder.get(0);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstAntialiasedShape() {
		CShape res = null;
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.isAntialiased())
					res = sms;
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstFilledShape() {
		CShape res = null;
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.filled)
					res = sms;
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstOutlinedShape() {
		CShape res = null;
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();) {
				CShape sms = i.next();
				if (sms.outlined)
					res = sms;
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement addTag(CExtensionalTag t) {
		synchronized(displayOrder) {
			for (Iterator<CShape>i = displayOrder.iterator(); i.hasNext();)
				i.next().addTag(t);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement addTag(String t) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().addTag(t);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement removeTag(CExtensionalTag t) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().removeTag(t);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement removeTag(String t) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().removeTag(t);
		}
		return this;
	}

	/**
	 * Calls <code>animate(Animation animTagScale)</code> for every CShape
	 * contained in this <code>Canvas</code>.
	 * 
	 * @param anim
	 *            The animation to associate to each shape in this
	 *            <code>Canvas</code>.
	 * @return this <code>Canvas</code> as a <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#animate(Animation)
	 */
	public CElement animate(Animation anim) {
		synchronized(displayOrder) {
			for (Iterator<CShape> i = displayOrder.iterator(); i.hasNext();)
				i.next().animate(anim);
		}
		return this;
	}

	/**
	 * @return The display list of this canvas.
	 */
	public List<CShape> getDisplayList() {
		return displayOrder;
	}

	protected void registerMachine(CStateMachine machine) {
		if (!stateMachines.contains(machine)) {
			stateMachines.add(machine);
			// machine.addStateMachineListener(updatePicker);
		}
	}

}