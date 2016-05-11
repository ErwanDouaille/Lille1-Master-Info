package fr.lri.swingstates.applets;

import java.applet.Applet;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.gestures.Gesture;
import fr.lri.swingstates.gestures.rubine.RubineClassifier;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.sm.transitions.TimeOut;

public class GesturalInteraction extends CStateMachine {
	private GraphicalEditor canvas;
	private Gesture gesture;
	private CPolyLine ink;
	private Point2D pInit;
	private CShape shape;
	private RubineClassifier classifier;
	private boolean modeCommunicationMachine = false;

	public GesturalInteraction(boolean modeCommunicationMachine, Applet applet, GraphicalEditor canvas) {
		this(applet, canvas);
		this.modeCommunicationMachine = modeCommunicationMachine;
	}

	public GesturalInteraction(Applet applet, GraphicalEditor canvas) {
		super(canvas);
		this.canvas = canvas;
		ink = (CPolyLine) canvas.newPolyLine().setPickable(false).setFilled(false);
		URL url;
		try {
			url = new URL(applet.getCodeBase(), "classifier/classifierNCCC.cl");
			classifier = RubineClassifier.newClassifier(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		gesture = new Gesture();
	}

	public void doSuspend() {
		ink.setDrawable(false);
	}

	public State start = new State() {

		void reset(Point2D pt) {
			pInit = pt;
			ink.reset(pInit.getX(), pInit.getY()).setDrawable(true).aboveAll();
			gesture.reset();
			gesture.addPoint(pInit.getX(), pInit.getY());
		}

		Transition beginOnShape = new PressOnShape(BUTTON1, ">> draw") {
			public void action() {
				reset(getPoint());
				shape = getShape();
			}
		};

		Transition begin = new Press(BUTTON1, ">> draw") {
			public void action() {
				reset(getPoint());
			}
		};

	};

	public State draw = new State() {
		Transition drag = new Drag(BUTTON1) {
			public void action() {
				ink.lineTo(getPoint().getX(), getPoint().getY());
				gesture.addPoint(getPoint().getX(), getPoint().getY());
			}
		};

		Transition end = new Release(BUTTON1, ">> feedback") {
			public void action() {
				armTimer(500, false);
				gesture.addPoint(getPoint().getX(), getPoint().getY());
				String gc = classifier.classify(gesture);
				if (gc != null) {
					if (modeCommunicationMachine)
						canvas.processEvent(gc, pInit);
					else
						canvas.command(gc, pInit, shape);
				}
			}
		};

	};

	public State feedback = new State() {
		Transition timeout = new TimeOut(">> start") {
			public void action() {
				ink.setDrawable(false);
			}
		};
	};
}
