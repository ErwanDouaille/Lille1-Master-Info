package fr.lri.swingstates.applets;

import java.awt.Font;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.gestures.AbstractClassifier;
import fr.lri.swingstates.gestures.Gesture;
import fr.lri.swingstates.gestures.rubine.RubineClassifier;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.sm.transitions.TimeOut;

class InkMachine extends CStateMachine {
	private Gesture gesture;
	private CPolyLine ink;
	private Point2D pInit;
	private AbstractClassifier classifier;
	private CText recognizedClass;

	public InkMachine(AbstractClassifier classifier, Canvas canvas) {
		super();
		this.classifier = classifier;
		recognizedClass = canvas.newText(10, 10, "", new Font("Verdana", Font.PLAIN, 14));
		ink = (CPolyLine) canvas.newPolyLine().setPickable(false).setFilled(false);
		gesture = new Gesture();
	}

	public State start = new State() {
		
		Transition begin = new Press(BUTTON1, ">> draw") {
			public void action() {
				pInit = getPoint();
				ink.reset(pInit.getX(), pInit.getY()).setDrawable(true).aboveAll();
				gesture.reset();
				gesture.addPoint(pInit.getX(), pInit.getY());
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
					recognizedClass.setText(gc);
				} else {
					recognizedClass.setText("?");
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

public class GestureRecognition extends BasicApplet {

	Canvas canvas;
	
	void recognizedGestures(AbstractClassifier classifier) {
		for(int i = 0; i < classifier.getClassesNames().size(); i++) {
			CPolyLine gest = classifier.getRepresentative(classifier.getClassesNames().get(i));
			gest.addTo(canvas).scaleBy(25 / gest.getHeight(), 25 / gest.getWidth()).translateTo(75 + 50*i, 25);
			canvas.newText(0, 0, classifier.getClassesNames().get(i)).translateTo(gest.getCenterX(), gest.getMaxY() + 12);			
		}
	}
	
	public void createGUI() {
		setSize(300, 300);
		canvas = new Canvas(300, 300);
		RubineClassifier classifier = null;
		try {
			URL url = new URL(this.getCodeBase(), "classifier/classifierNCCC.cl");
			classifier = RubineClassifier.newClassifier(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		recognizedGestures(classifier);
		InkMachine sm = new InkMachine(classifier, canvas);
		sm.attachTo(canvas);
		add(canvas);
	}

}
