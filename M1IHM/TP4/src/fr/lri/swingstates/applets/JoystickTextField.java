/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;

import javax.swing.JApplet;
import javax.swing.JTextField;

import fr.lri.swingstates.canvas.CSegment;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.sm.JStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.sm.transitions.TimeOut;

class JoystickTextField {

	Canvas drawInk;
	Font font = new Font("verdana", Font.PLAIN, 12);
	CText textRate;
	CSegment ink;
	
	public JoystickTextField(JApplet applet) {
		drawInk = new Canvas(applet.getContentPane().getWidth(), applet.getContentPane().getHeight());
		textRate = (CText) drawInk.newText(0, 0, "", font).setReferencePoint(0, 1);
		ink = (CSegment) drawInk.newSegment(0, 0, 1, 1).setAntialiased(true).setStroke(new BasicStroke(
				1f, 
				BasicStroke.CAP_ROUND, 
				BasicStroke.JOIN_ROUND, 
				1f, 
				new float[] {2f}, 
				0f));
		drawInk.setTransparencyFill(0.5f);
		drawInk.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		drawInk.setOpaque(false);
		// Add a canvas to the swing glasspane to draw ink and current rate.
		applet.setGlassPane(drawInk);
		applet.getGlassPane().setVisible(true);
		
		// attachTo on the content pane
		smText.attachTo(applet);
	}
	
	JStateMachine smText = new JStateMachine() {
		
		JTextField activeTextField = null;
		Point2D ptInit = null;
		int rate = 0;
		int currentValue = 0;
		
		public State out = new State() {
			
			Transition enter = new EnterOnTag("javax.swing.JTextField", ">> in") {
				public void action() {
					getComponent().setBackground(Color.YELLOW);
					activeTextField = (JTextField) getComponent();
					currentValue = Integer.parseInt(activeTextField.getText());
				}
			};
		
		};
		
		public State in = new State() {
			
			Transition leave = new LeaveOnTag("javax.swing.JTextField", ">> out") {
				public void action() {
					activeTextField.setBackground(Color.WHITE);
				}
			};
			
			Transition pressPlus = new KeyPress('+') {
				public void action() {
					activeTextField.setText(""+(currentValue+1));
					currentValue++;
				}
			};
			
			Transition pressMinus = new KeyPress('-') {
				public void action() {
					activeTextField.setText(""+(currentValue-1));
					currentValue--;
				}
			};
			
			Transition press = new PressOnTag("javax.swing.JTextField", BUTTON1, ">> control") {
				public void action() {
//					ptInit = getPointInMainContainer();
					ptInit = getPoint();
					textRate.setText("0");
					ink.setPoints(ptInit, ptInit).setDrawable(true);
					textRate.translateTo(10 + ptInit.getX(), 25 + ptInit.getY()).setDrawable(true);
					currentValue = Integer.parseInt(activeTextField.getText());
				}
			};
			
		};
		
		public State control = new State() {
			
			Transition drag = new DragOnComponent(BUTTON1) {
				public void action() {
					disarmTimer();
//					Point2D pt = getPointInMainContainer();
					Point2D pt = getPoint();
					rate = (int)ptInit.distance(pt)/3;
					if(pt.getY() > ptInit.getY())
						rate = -rate;
					activeTextField.setText(""+(Integer.parseInt(activeTextField.getText())+rate));
					textRate.setText(""+rate).translateTo(10 + pt.getX(), 25 + pt.getY());
					ink.setPoints(ptInit, pt);
					armTimer(200, true);
				}
			};
			
			Transition timeout = new TimeOut() {
				public void action() {
					activeTextField.setText(""+(currentValue+rate));
					currentValue += rate;
				}
			};
			
			Transition releaseIn = new ReleaseOnTag("javax.swing.JTextField", BUTTON1, ">> in") { };
			
			Transition releaseOut = new Release(BUTTON1, ">> out") {
				public void action() {
					activeTextField.setBackground(Color.WHITE);
				}
			};
			
			public void leave() {
				ink.setDrawable(false);
				textRate.setDrawable(false);
				disarmTimer();
			}
			
		};
	};
	
}

