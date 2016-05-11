package fr.lri.swingstates.applets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CTag;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Release;

public class TestTransform extends BasicApplet {

	class SliderListener implements ChangeListener {
		double previousValueTx = Double.MIN_VALUE;
		double previousValueTy = 200;
		
		public void stateChanged (ChangeEvent ev) {
			int tx = txSlider.getValue();
			int ty = tySlider.getValue();
			double angle = angleSlider.getValue() * Math.PI / 180.0;
			double sx = sxSlider.getValue() / 10.0;
			double sy = sySlider.getValue() / 10.0;
			double rx = rxSlider.getValue() / 100.0;
			double ry = rySlider.getValue() / 100.0;
			movable.setReferencePoint(rx, ry);								
			if(previousValueTx == Double.MIN_VALUE) {
				previousValueTx = 0;
				previousValueTy = 0;
			}
			movable.translateBy(tx - previousValueTx, ty - previousValueTy).rotateTo(angle).scaleTo(sx, sy);
			previousValueTx = tx;
			previousValueTy = ty;
		}
	}

	Canvas canvas;
	JSlider txSlider, tySlider, angleSlider, sxSlider, sySlider, rxSlider, rySlider, alphaSlider;
	JCheckBox cbOutlined, cbFilled;
	JButton attachButton, suspendButton, reStartButton;
	JPanel controls;
	JPanel buttons;
	ChangeListener changed = new SliderListener ();
	CExtensionalTag movable;
	
	CStateMachine sm = new CStateMachine() {		
		Point2D pInit = null;
		CTag dragged = null;
		CShape draggedShape = null;
		
		public State start = new State("start") {
			Transition rotate = new PressOnTag("parent", BUTTON1, SHIFT, ">> rotate"){
				public void action(){
					pInit = getPoint();
					dragged = getTag();
				}
			};
			Transition translate = new PressOnTag("parent", BUTTON1, ">> translate"){
				public void action(){
					pInit = getPoint();
					dragged = getTag();
				}
			};
			Transition scale = new PressOnTag("parent", BUTTON3, ">> scale"){
				public void action(){
					pInit = getPoint();
					dragged = getTag();
				}
			};
			
			Transition translateTo = new PressOnTag("movable", BUTTON1, ">> translateTo") {
				public void action() {
					draggedShape = getShape();
				}
			};

			Transition info = new ClickOnShape(BUTTON1) {
				public void action(){
					Point2D pc = getPoint();
					Point2D ps = getShape().canvasToShape(pc);
//					System.out.println("clicked canvas at ("+pc.getX()+", "+pc.getY()+") - clicked shape at ("+ps.getX()+", "+ps.getY()+")");
				}
			};
		};
		
		public State translateTo = new State() {
			Transition drag = new Drag(BUTTON1){
				public void action(){
					draggedShape.translateTo(
							getPoint().getX(), 
							getPoint().getY());
					pInit = getPoint();
				}
			};
			Transition done = new Release(BUTTON1, ">> start");
		};
		
		public State translate = new State() {
			Transition drag = new Drag(BUTTON1){
				public void action(){
					dragged.translateBy(
							getPoint().getX() - pInit.getX(), 
							getPoint().getY() - pInit.getY());
					pInit = getPoint();
				}
			};
			Transition done = new Release(BUTTON1, ">> start");
		};
		
		public State scale = new State() {
			Transition drag = new Drag(BUTTON3){
				public void action(){
					dragged.scaleBy(
							1 + (getPoint().getX() - pInit.getX())/10, 
							1 + (getPoint().getY() - pInit.getY())/10);
					pInit = getPoint();
				}
			};
			Transition done = new Release(BUTTON3, ">> start");
		};
		
		public State rotate = new State() {
			Transition drag = new Drag(BUTTON1, SHIFT){
				public void action(){
					dragged.rotateBy((getPoint().getX() - pInit.getX())/100);
					pInit = getPoint();
				}
			};
			Transition drag2 = new Drag(BUTTON1){
				public void action(){
					dragged.rotateBy((getPoint().getX() - pInit.getX())/100);
					pInit = getPoint();
				}
			};
			Transition done = new Release(BUTTON1, SHIFT, ">> start");
			Transition done2 = new Release(BUTTON1, ">> start");
		};
	};
	
	@Override
	public void createGUI() {
		setSize(new Dimension(800, 600));
		
		// the panel with the canvas in the center and the sliders on the right
		JPanel panel = new JPanel(new BorderLayout());
		add(panel);
		
		canvas = new Canvas(600, 600);
		canvas.attachSM(sm, true);
		canvas.setPreferredSize(new Dimension(600, 600));
		panel.add(canvas, BorderLayout.CENTER);
		
		controls = new JPanel ();
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		panel.add(controls, BorderLayout.WEST);
		
		// the sliders
		txSlider = makeSlider ("tx", -100, 100, 0, 50, 10);
		tySlider = makeSlider ("ty", -100, 100, 0, 50, 10);
		angleSlider = makeSlider ("angle", -180, 180, 0, 90, 15);
		sxSlider = makeSlider ("sx", 0, 100, 10, 25, 5);
		sySlider = makeSlider ("sy", 0, 100, 10, 25, 5);
		rxSlider = makeSlider ("rx", 0, 100, 50, 25, 5);
		rySlider = makeSlider ("ry", 0, 100, 50, 25, 5);
		alphaSlider = makeAlphaSlider ();
		
		cbFilled = new JCheckBox("filled");
		cbFilled.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				movable.setFilled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		cbOutlined = new JCheckBox("outlined");
		cbOutlined.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				movable.setOutlined(e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		controls.add(cbFilled);
		controls.add(cbOutlined);
		
		// canvas contents
		movable = canvas.newTag("movable");
		
		CShape parent = (CShape) canvas.newRectangle (-20, -20, 40, 40).setFillPaint(Color.red).addTag("parent");
		
		CShape s;
		
		// The cast in the CShape is used because each method returns a CElement object
		// (the interface implementing by CShape, CTag and Canvas)
		s = (CShape) canvas.newRectangle (100, 100, 150, 100).setParent(parent);
		s.duplicate().setTransparencyFill(0.2f).addTag(movable);
		
		s = (CShape) canvas.newEllipse (400, 100, 150, 100).setParent(parent);
		s.duplicate().setTransparencyFill(0.2f).addTag(movable);
		
		Font font = new Font("verdana", Font.PLAIN, 24);
		s = (CShape) canvas.newText(100, 300, "Hello", font).setParent(parent);
		s.duplicate().setTransparencyFill(0.2f).addTag(movable);
		
		s = (CShape) canvas.newPolyLine(400, 300).
			lineTo(440, 290).lineTo(450, 250).lineTo(460, 290).lineTo(500, 300).
			lineTo(460, 310).lineTo(450, 350).lineTo(440, 310).close().
			setParent(parent);
		s.duplicate().setTransparencyFill(0.2f).addTag(movable);
		
		s = (CShape) canvas.newPolyLine (400, 500).
			curveTo(400, 425, 450 ,475, 450, 400).quadTo(500, 375, 550, 400).curveTo(550, 475, 500, 425, 500, 500).quadTo(450, 475, 400, 500).
			setParent(parent);
		s.duplicate().setTransparencyFill(0.2f).addTag(movable);
		
		String location =  "images/star.jpg";
		try {
			URL url = new URL(getCodeBase(),location);
			s = (CShape) canvas.newImage (100, 400, url).setParent(parent);
			s.duplicate().setTransparencyFill(0.5f).addTag(movable);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		
		
		
		
		s = (CShape) canvas.newWidget(new JButton("Ok"), 300, 300).setParent(parent);
		s.duplicate().setTransparencyFill(0.5f).addTag(movable);
		
		movable.setFilled(true).setOutlined(true);
		cbFilled.setSelected(true);
		cbOutlined.setSelected(true);
	}

	public JSlider makeSlider (String name, int min, int max, int val, int major, int minor) {
		JLabel l = new JLabel (name);
		JSlider s = new JSlider (JSlider.HORIZONTAL, min, max, val);
		s.setMajorTickSpacing(major);
		s.setMinorTickSpacing(minor);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
		s.addChangeListener(changed);
		controls.add(l);
		controls.add(s);
		return s;
	}
	
	public JSlider makeAlphaSlider () {
		JLabel l = new JLabel ("transparency");
		JSlider s = new JSlider (JSlider.HORIZONTAL, 0, 100, 50);
		s.setMajorTickSpacing(10);
		s.setMinorTickSpacing(1);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
		s.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int alpha = alphaSlider.getValue();
				movable.setTransparencyFill(alpha/100f);
			}
		});
		controls.add(l);
		controls.add(s);
		return s;
	}
	
}
