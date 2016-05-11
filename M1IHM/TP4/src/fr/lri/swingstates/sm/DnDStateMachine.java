/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CTag;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Release;

/**
 * <p>
 * <code>DnDStateMachine</code> is a predefined state machine to
 * implement "press-drag-release" interactions on shapes contained in a
 * <i>SwingStates</i>' canvas.
 * </p>
 * 
 * <p>
 * <code>DnDStateMachine</code> can be used directly by only
 * overriding actions for press, drag and release (methods
 * <code>pressAction</code>, <code>dragAction</code> and <code>releaseAction</code>.
 * </p>
 * 
 * <p>
 * <b>Example</b>: This program makes shapes drawn in 
 * a canvas movable by drag'n drop. 
 * <pre>
 * DnDStateMachine smDragShapes = new DnDStateMachine(BasicInputStateMachine.BUTTON1) {
			public void dragAction(CShape shape, double previousX, double previousY, double currentX, double currentY) {
				shape.translateBy(currentX - previousX, currentY - previousY);
			}
			public void releaseAction(CShape shape, double previousX, double previousY, double currentX, double currentY) {
				shape.translateBy(currentX - previousX, currentY - previousY);
			}
		};
	smDragShapes.attachTo(canvas);
	</pre>
 * 
 * @author Caroline Appert
 *
 */
public abstract class DnDStateMachine extends CStateMachine {
	
	/**
	 * The two states (start and move) of this machine.
	 */
	public State start, move;
	private CTag tag = null;
	private Class classTag = null;
	
	private int button;
	private double lastX, lastY;
	private CShape dragged;
	
	/**
	 * Builds a state machine to manage press-drag-release
	 * interactions that are inited on a <code>CShape</code>.
	 * @param button The mouse button for press and release events (constants BUTTON1, BUTTON2, etc. in class <code>BasicInputStateMachine</code>).
	 * @see BasicInputStateMachine#BUTTON1
	 * @see BasicInputStateMachine#BUTTON2
	 * @see BasicInputStateMachine#BUTTON3
	 */
	public DnDStateMachine(int button) {
		this(button, null, null);
	};
	
	/**
	 * Builds a state machine to manage press-drag-release
	 * interactions that are inited on a <code>CShape</code>
	 * which has a tag of a given class <code>classTag</code>.
	 * @param button The mouse button for press and release events (constants BUTTON1, BUTTON2, etc. in class <code>BasicInputStateMachine</code>).
	 * @param classTag The class of the tag.
	 * @see BasicInputStateMachine#BUTTON1
	 * @see BasicInputStateMachine#BUTTON2
	 * @see BasicInputStateMachine#BUTTON3
	 */
	public DnDStateMachine(int button, Class classTag) {
		this(button, null, classTag);
	};
	
	/**
	 * Builds a state machine to manage press-drag-release
	 * interactions that are inited on a <code>CShape</code>
	 * which has a given tag.
	 * @param button The mouse button for press and release events (constants BUTTON1, BUTTON2, etc. in class <code>BasicInputStateMachine</code>).
	 * @param tag The tag.
	 * @see BasicInputStateMachine#BUTTON1
	 * @see BasicInputStateMachine#BUTTON2
	 * @see BasicInputStateMachine#BUTTON3
	 */
	public DnDStateMachine(int button, CTag tag) {
		this(button, tag, null);
	};
	
	private DnDStateMachine(int button, CTag tag, Class classTag) {
		super();
		this.tag = tag;
		this.classTag = classTag;
		this.button = button;
		start = new State() {
		
			void press(CShape picked, Point2D point) {
				dragged = picked;
				lastX = point.getX();
				lastY = point.getY();
				pressAction(dragged, lastX, lastY);
			}
			
			Transition press = DnDStateMachine.this.tag == null ? 
					(DnDStateMachine.this.classTag == null ? 
							(new PressOnShape(DnDStateMachine.this.button, ">> move") {
								public void action() {
									press(getShape(), getPoint());
								}
							})
							: (new PressOnTag(DnDStateMachine.this.classTag, DnDStateMachine.this.button, ">> move") {
								public void action() {
									press(getShape(), getPoint());
								}
							})) 
							: (new PressOnTag(DnDStateMachine.this.tag, DnDStateMachine.this.button, ">> move") {
								public void action() {
									press(getShape(), getPoint());
								}
							});
		};
		move = new State() {
			Transition drag = new Drag(DnDStateMachine.this.button) {
				public void action() {
					dragAction(dragged, lastX, lastY, getPoint().getX(), getPoint().getY());
					lastX = getPoint().getX();
					lastY = getPoint().getY();
				}
			};
			Transition release = new Release(DnDStateMachine.this.button, ">> start") {
				public void action() {
					releaseAction(dragged, lastX, lastY, getPoint().getX(), getPoint().getY());
				}
			};
		};
	}
	
	/**
	 * The method to override in subclasses to specify
	 * action to do when a press event on a <code>CShape</code>
	 * occurs.
	 * 
	 * @param shape The shape on which occured the press event
	 * @param currentX The mouse x-coordinate
	 * @param currentY The mouse y-coordinate
	 */
	public void pressAction(CShape shape, 
			double currentX, double currentY) { };
	
	/**
	 * The method to override in subclasses to specify
	 * action to do when a drag event occurs. (the considered
	 * drag events are only those that are within a "press-drag-release" sequence
	 * inited by a press on a <code>CShape</code>).
	 * 
	 * @param shape The shape on which occured the press event
	 * @param previousX The last mouse x-coordinate
	 * @param previousY The last mouse y-coordinate
	 * @param currentX The mouse x-coordinate
	 * @param currentY The mouse y-coordinate
	 */
	public void dragAction(CShape shape, 
			double previousX, double previousY, 
			double currentX, double currentY) { };
			
	/**
	 * The method to override in subclasses to specify
	 * action to do when a release event occurs. (the considered
	 * release events are only those that are within a "press-drag-release" sequence
	 * inited by a press on a <code>CShape</code>).
	 * 
	 * @param shape The shape on which occured the press event
	 * @param previousX The last mouse x-coordinate
	 * @param previousY The last mouse y-coordinate
	 * @param currentX The mouse x-coordinate
	 * @param currentY The mouse y-coordinate
	 */
	public void releaseAction(CShape shape, 
			double previousX, double previousY, 
			double currentX, double currentY) { };

}
