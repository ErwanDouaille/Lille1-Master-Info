/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.debug;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.animations.AnimationScaleTo;
import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CIntentionalTag;
import fr.lri.swingstates.canvas.CNamedTag;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

/**
 * A <code>GraphicalNode</code> in a <code>StateMachineVisualization</code>.
 * 
 * @author Caroline Appert
 *
 */
class GraphicalNode {
	CEllipse bg;
	CText label;
	State state;
	Canvas canvas;
	
	LinkedList<GraphicalArc> transitions = new LinkedList<GraphicalArc>();
	
	public GraphicalNode(Canvas c, State s, int x, int y) {
		canvas = c;
		state = s;
		String name = s.getName();
		label = (CText)c.newText(0, 0, name, StateMachineVisualization.FONT).setReferencePoint(0.5, 0.5).setOutlined(false).setPickable(false);
		bg = (CEllipse)c.newEllipse(x-10, y-10, label.getWidth()+20, label.getHeight()+20).setFillPaint(Color.WHITE);
		label.above(bg).setParent(bg);
		label.translateTo(bg.getCenterX(), bg.getCenterY());
	}

	void addTransition(GraphicalArc arc) {
		transitions.add(arc);
	}
	
	LinkedList<GraphicalArc> getTransitions() {
		return transitions;
	}
	
	public void hilite() {
		bg.setFillPaint(new Color(255, 204, 255));
		label.setFillPaint(new Color(153, 0, 153));
	}
	
	public void unhilite() {
		bg.setFillPaint(Color.WHITE);
		label.setFillPaint(Color.BLACK);
	}
}

/**
 * A tag for clickable parts of a <code>GraphicalArc</code>s.
 * 
 * @author Caroline Appert
 *
 */
class ClickableArc extends CExtensionalTag {
	public ClickableArc(Canvas c) {
		super(c);
	}
}

/**
 * A tag for clickable handles of a <code>GraphicalArc</code>s.
 *
 * @author Caroline Appert
 *
 */
class TagHandle extends CExtensionalTag {
	
	private GraphicalArc arc;
	
	public TagHandle(Canvas canvas, GraphicalArc arc) {
		super(canvas);
		this.arc = arc;
	}
	
	GraphicalArc getArc() {
		return arc;
	}
	
}

/**
 * A tag for all parts of a <code>GraphicalArc</code>s.
 *
 * @author Caroline Appert
 *
 */
class TagArc extends CExtensionalTag {
	public TagArc(Canvas canvas) {
		super(canvas);
	}
}

/**
 * A <code>GraphicalArc</code> in a <code>StateMachineVisualization</code>.
 * 
 * @author Caroline Appert
 *
 */
class GraphicalArc {
	private CPolyLine polyline;
	private LinkedList<CEllipse> points = new LinkedList<CEllipse>();
	private CExtensionalTag tagWhole, tagHandle, defaultHandle;
	private Transition trans;
	private CPolyLine arrow;
	private CText nameTrans;
	private CRectangle bgName;

	private Canvas canvas;
	private Animation animLastArcPassed = new AnimationScaleTo(1, 1);
	
	public GraphicalArc(Canvas c, GraphicalNode in, GraphicalNode out, Transition t) {
		canvas = c;
		trans = t;
		
		LinkedList<GraphicalArc> allTransitionsIn = in.getTransitions();
		LinkedList<GraphicalArc> allTransitionsOut = out.getTransitions();
		double yState = in.bg.getCenterY();
		int size = Math.max(allTransitionsIn.size(), allTransitionsOut.size());
		
		
//		double xInit, yInit, xOut, yOut, xMiddle, yMiddle;
//		xInit = (in.bg.getCenterX()>out.bg.getCenterX()) || ((in == out) && size%2!=0) ? in.bg.getCenterX() + in.bg.getWidth()/4
//				: in.bg.getCenterX() - in.bg.getWidth()/4;
//		xOut = (in.bg.getCenterX()>out.bg.getCenterX()) || ((in == out) && size%2!=0) ? out.bg.getCenterX() - out.bg.getWidth()/4
//				: out.bg.getCenterX() + out.bg.getWidth()/4;
//		xMiddle = (xOut + xInit) / 2;
		
		
		double xInit, yInit, xOut, yOut, xMiddle, yMiddle;
		xInit = in.bg.getCenterX() - in.bg.getWidth()/4;
		xOut = out.bg.getCenterX() + out.bg.getWidth()/4;
		xMiddle = (xOut + xInit) / 2;
		
		CEllipse pt3;
		if(size%2 == 0) {
			yInit = in.bg.getMinY();
			yOut = out.bg.getMinY();
			yMiddle = yState - (size/2 + 1)*StateMachineVisualization.SPACE_BETWEEN_TRANSITIONS - 20;
			nameTrans = (CText) c.newText(xMiddle, yMiddle-18, t.toString().trim(), StateMachineVisualization.FONT).setPickable(false);
			bgName = (CRectangle)c.newRectangle(xMiddle, nameTrans.getMinY(), nameTrans.getWidth(), nameTrans.getHeight()).belowAll().setOutlined(false).setFillPaint(Color.WHITE);
			pt3 = (CEllipse)c.newEllipse(bgName.getCenterX() - 3, bgName.getMaxY(), 6, 6).setDrawable(false).setPickable(false).setParent(bgName);
		} else {
			yInit = in.bg.getMaxY();
			yOut = out.bg.getMaxY();
			yMiddle = yState + (size/2 + 1)*StateMachineVisualization.SPACE_BETWEEN_TRANSITIONS + 20;
			nameTrans = (CText) c.newText(xMiddle, yMiddle+4, t.toString().trim(), StateMachineVisualization.FONT).setPickable(false);
			bgName = (CRectangle)c.newRectangle(xMiddle, nameTrans.getMinY(), nameTrans.getWidth(), nameTrans.getHeight()).belowAll().setOutlined(false).setFillPaint(Color.WHITE);
			pt3 = (CEllipse)c.newEllipse(bgName.getCenterX() - 3, bgName.getMinY() - 6, 6, 6).setDrawable(false).setPickable(false).setParent(bgName);
			if(in == out) {
				xInit = in.bg.getCenterX() + in.bg.getWidth()/4;
				xOut = out.bg.getCenterX() - out.bg.getWidth()/4;
			}
		}
		
		polyline = (CPolyLine) c.newPolyLine(xInit, yInit).setFilled(false).setStroke(new BasicStroke(2));
		polyline.addTag(new TagArc(c){});
		nameTrans.setParent(bgName);
		CEllipse pt1, pt2, pt4, pt5;
		double delta = 20;
		pt1 = (CEllipse)c.newEllipse(xInit - 3, yInit - 3, 6, 6).setParent(in.bg);
		pt2 = xInit>xOut ? (CEllipse)c.newEllipse(xInit - 3 + delta, yMiddle - 3, 6, 6)
				: (CEllipse)c.newEllipse(xInit - 3 - delta, yMiddle - 3, 6, 6);
		pt4 = xInit>xOut ? (CEllipse)c.newEllipse(xOut - 3 - delta, yMiddle - 3, 6, 6) 
				:(CEllipse)c.newEllipse(xOut - 3 + delta, yMiddle - 3, 6, 6);
		pt5 = (CEllipse)c.newEllipse(xOut - 3, yOut - 3, 6, 6).setParent(out.bg);
		
		bgName.setReferencePoint(0.5, 0.5).translateTo(xMiddle, bgName.getCenterY());
		bgName.addTag("transitionLabel");
		
		points.add(pt1);
		points.add(pt2);
		points.add(pt3);
		points.add(pt4);
		points.add(pt5);

		tagHandle = new TagHandle(c, this);
		defaultHandle = new CExtensionalTag(c) { };
		in.bg.addTag(tagHandle);
		out.bg.addTag(tagHandle);
		bgName.addTag(tagHandle);
		pt2.addTag(defaultHandle);
		pt4.addTag(defaultHandle);
		pt1.addTag(defaultHandle);
		pt5.addTag(defaultHandle);
		defaultHandle.addTag(tagHandle);
		defaultHandle.setFillPaint(new Color(51, 102, 102)).setOutlinePaint(new Color(51, 102, 102));
		
		defaultHandle.setDrawable(false).setPickable(false);
		
		tagWhole = new CExtensionalTag(c) { };
		update();
		in.addTransition(this);
		if(in!=out)out.addTransition(this);
		
		getSelectMachine();
			getMovePointsMachine(c);
	}
	
	CStateMachine getSelectMachine() {
		CStateMachine select = new CStateMachine(polyline) {
			public State start = new State() {
				Transition press = new PressOnShape(BUTTON1) {
					public void action() {
						defaultHandle.setDrawable(!defaultHandle.isDrawable());
						defaultHandle.setPickable(!defaultHandle.isPickable());
					}
				};
			};
		};
		return select;
	}
	
	CStateMachine getMovePointsMachine(Canvas canvas) {
		CStateMachine movePoints = new CStateMachine(canvas) {
			CShape moved = null;
			double lastX, lastY;
			CNamedTag labelMoved = new CNamedTag("labelMoved");
			public State start = new State() {
				Transition moveLabel = new PressOnTag(tagHandle, BUTTON1, ">> moveLabel") {
					public boolean guard() {
						return getShape().hasTag("transitionLabel");
					}
					public void action() {
						((TagHandle)getTag()).getArc().points.get(1).addTag(labelMoved);
						((TagHandle)getTag()).getArc().points.get(3).addTag(labelMoved);
						getShape().addTag(labelMoved);
						lastX = getPoint().getX();
						lastY = getPoint().getY();
					}
				};
				Transition startMove = new PressOnTag(tagHandle, BUTTON1, ">> move") {
					public void action() {
						moved = getShape();
						lastX = getPoint().getX();
						lastY = getPoint().getY();
						getShape().setReferencePoint((lastX - moved.getMinX())/(moved.getMaxX() - moved.getMinX()),
								(lastY - moved.getMinY())/(moved.getMaxY() - moved.getMinY()));
					}
				};
			};
			public State move = new State() {
				Transition move = new Drag(BUTTON1) {
					public void action() {
						moved.translateTo(getPoint().getX(), getPoint().getY());
						update();
					}
				};
				Transition endMove = new Release(BUTTON1, ">> start") {
					public void action() {
						moved.translateTo(getPoint().getX(), getPoint().getY());
						update();
					}
				};
			};
			public State moveLabel = new State() {
				Transition move = new Drag(BUTTON1) {
					public void action() {
						labelMoved.translateBy(getPoint().getX()-lastX, getPoint().getY()-lastY);
						lastX = getPoint().getX();
						lastY = getPoint().getY();
						update();
					}
				};
				Transition endMove = new Release(BUTTON1, ">> start") {
					public void action() {
						labelMoved.translateBy(getPoint().getX()-lastX, getPoint().getY()-lastY);
						update();
					}
				};
			};
		};
		return movePoints;
	}

	void update() {
		polyline.setTransformToIdentity();
		if(arrow != null) arrow.setTransformToIdentity();
		polyline.reset(points.get(0).getCenterX(), points.get(0).getCenterY());
		polyline.quadTo(points.get(1).getCenterX(), points.get(1).getCenterY(), points.get(2).getCenterX(), points.get(2).getCenterY());
		polyline.quadTo(points.get(3).getCenterX(), points.get(3).getCenterY(), points.get(4).getCenterX(), points.get(4).getCenterY());
		CPolyLine newArrow = polyline.getArrow(Math.PI/6, 10);
		if(arrow != null) arrow.setShape(newArrow.getShape());
		else {
			arrow = newArrow;
			canvas.addShape(arrow);
			arrow.setStroke(new BasicStroke(1.5f));
			arrow.belowAll();
		}
		polyline.setAntialiased(true);
		arrow.setAntialiased(true);
		arrow.setParent(polyline);
		polyline.addTag(tagWhole);
		arrow.addTag(tagWhole);
		bgName.aboveAll();
		nameTrans.aboveAll();
	}
	
	
	public void hilite() {
		polyline.setOutlinePaint(new Color(153, 0, 153));
		arrow.setOutlinePaint(new Color(153, 0, 153));
		nameTrans.setFillPaint(new Color(153, 0, 153));
		nameTrans.scaleTo(1.1, 1.1).animate(animLastArcPassed);
		
	}
	
	public void unhilite() {
		animLastArcPassed.stop();
		polyline.setOutlinePaint(Color.BLACK);
		arrow.setOutlinePaint(Color.BLACK);
		nameTrans.setFillPaint(Color.BLACK);
		nameTrans.scaleTo(1, 1);
	}

	public Transition getTransition() {
		return trans;
	}

}

/**
 * A canvas that displays a state machine and shows its activity in real time.
 * At each moment:
 * <ul>
 * <li> The current state is colored in yellow.
 * <li> The last passed transition is colored in red.
 * </ul>
 * 
 * @author Caroline Appert
 *
 */
public class StateMachineVisualization extends Canvas {

	static Font                           	FONT = new Font("verdana", Font.PLAIN, 10);
	static final int 						SPACE_BETWEEN_TRANSITIONS = 30;

	private LinkedList<GraphicalNode>     	states = new LinkedList<GraphicalNode>();
	private LinkedList<GraphicalArc> 		transitions = new LinkedList<GraphicalArc>();
	
	private CIntentionalTag 				allRootShapes;
	
	private GraphicalNode 					hilitedNode = null;
	private GraphicalArc 					hilitedArc = null;

	
	private StateMachine 					smToVisualize;
	private boolean             			first = true;
	
	/**
	 * Builds a canvas that displays a state machine.
	 * @param sm The state machine to display.
	 */
	public StateMachineVisualization(StateMachine sm) {
		super(600, 600);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		smToVisualize = sm;
		init();
	}

	/**
	 * Builds a canvas that displays a state machine
	 * and adds it to en independant <code>JFrame</code>.
	 * @param sm The state machine to display
	 * @return The window
	 */
	public static JFrame windowVisualization(StateMachine sm) {
		JFrame frame = new JFrame("StateMachine "+sm);
		frame.getContentPane().add(new StateMachineVisualization(sm));
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	private void init(){
		// State names are initialized when at least one transition has been fired
		smToVisualize.addStateMachineListener(new StateMachineEventListener(){
			public void smInited(StateMachineEvent e){
				if(first) { first = false; drawSM(); }
			}
			public void smStateChanged(StateMachineEvent e){
				if(first) { first = false; drawSM(); }
				hilite(e.getCurrentState(), e.getTransition());
				repaint();
			}

			public void smStateLooped(StateMachineEvent e) {
				if(first) { first = false; drawSM(); }
				hilite(e.getCurrentState(), e.getTransition());
				repaint();
			}
			
			public void smReset(StateMachineEvent e) { }
			
			public void smResumed(StateMachineEvent e) {
				StateMachineVisualization.this.setTransparencyFill(1);
				StateMachineVisualization.this.setTransparencyOutline(1);
			}
			
			public void smSuspended(StateMachineEvent e) {
				StateMachineVisualization.this.setTransparencyFill(.5f);
				StateMachineVisualization.this.setTransparencyOutline(.5f);
			}

		});
		// to make the state machine start
		smToVisualize.resume();
		hilite(smToVisualize.getInitialState(), null);
		
		new CStateMachine(this) {

			double lastX, lastY;

			public State start = new State() {
				Transition pressOnBg = new Press(BUTTON1, ">> moveCanvas") {
					public boolean guard() {
						return pick(getPoint()) == null;
					}
					public void action() {
						lastX = getPoint().getX();
						lastY = getPoint().getY();
					}
				};
			};

			public State moveCanvas = new State() {
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						allRootShapes.reset();
						allRootShapes.translateBy(getPoint().getX() - lastX, getPoint().getY() - lastY);
						lastX = getPoint().getX();
						lastY = getPoint().getY();
					}
				};
				Transition release = new Release(BUTTON1, ">> start") {
					public void action() {
						allRootShapes.translateBy(getPoint().getX() - lastX, getPoint().getY() - lastY);
						lastX = getPoint().getX();
						lastY = getPoint().getY();
					}
				};
			};

		};
	}

	private void hilite(State s, Transition t) {
		for(Iterator<GraphicalNode> i = states.iterator(); i.hasNext(); ) {
			GraphicalNode node = i.next();
			if(node.state == s) {
				if(node != hilitedNode) {
					if(hilitedNode != null) hilitedNode.unhilite();
					hilitedNode = node;
					hilitedNode.hilite();
					break;
				}
			}
		}
		for(Iterator<GraphicalArc> i = transitions.iterator(); i.hasNext(); ) {
			GraphicalArc transition = i.next();
			if(transition.getTransition() == t) {
				if(hilitedArc != null) hilitedArc.unhilite();
				hilitedArc = transition;
				hilitedArc.hilite();
				break;
			}
		}
	}
	
	private void drawSM(){
		Collection<State> allStates = smToVisualize.getAllStates(); 
		int xStep = 100;
		int x = xStep - xStep/2;
		int middle = 200;
			
		for(Iterator<State> i = allStates.iterator(); i.hasNext(); ){
			State s = i.next();
			drawState(x, middle, s);
			x+=xStep;
		}

		for(Iterator i = allStates.iterator(); i.hasNext(); ){
			drawTransitions((State)(i.next()));
		}
		allRootShapes = new CIntentionalTag(this) {
			public boolean criterion(CShape s) {
				return s.getParent() == null;
			}
		};
		addComponentListener(new ResizeListener(this));
	}
	
	/**
	 * Used to recenter the machine when the state machine visualizer widget is resized.
	 * 
	 * @author Caroline Appert
	 *
	 */
	private class ResizeListener implements ComponentListener {

		StateMachineVisualization smv;
		
		public ResizeListener(StateMachineVisualization smv) {
			this.smv = smv;
		}
		
		void updateComponent() {
			double centerx = getCenterX();
			double centery = getCenterY();
			double centerviewx = smv.getSize().getWidth()/2;
			double centerviewy = smv.getSize().getHeight()/2;
			allRootShapes.translateBy(centerviewx - centerx, centerviewy - centery);
		}
	
		public void componentHidden(ComponentEvent e) {
			updateComponent();
		}

		public void componentMoved(ComponentEvent e) {
			updateComponent();
		}

		public void componentResized(ComponentEvent e) {
			updateComponent();
		}

		public void componentShown(ComponentEvent e) {
			updateComponent();
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Dimension getPreferredSize() {
		return new Dimension((int)(getMaxX()-getMinX()), (int)(getMaxY()-getMinY()));
	}

	private GraphicalNode getGraphicalNode(State s) {
		for(Iterator<GraphicalNode> i = states.iterator(); i.hasNext(); ) {
			GraphicalNode node = i.next();
			if(node.state == s) {
				return node;
			}
		}
		return null;
	}
	
	private void drawTransitions(State s){
		LinkedList<Transition> trans = s.getTransitions();
		for(Iterator<Transition> i = trans.iterator(); i.hasNext(); ) {
			Transition t = i.next();
			GraphicalNode in = getGraphicalNode(t.getInputState());
			GraphicalNode out = getGraphicalNode(t.getOutputState());
			if(out != null)
				transitions.add(
						new GraphicalArc(
								this, 
								in, 
								out, 
								t));

			else
				transitions.add(
						new GraphicalArc(
								this, 
								in,
								in,
								t));
		}
	}

	private void drawState(int x, int y, State s) {
		states.add(new GraphicalNode(this, s, x, y));
	}

}

