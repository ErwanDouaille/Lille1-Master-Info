/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.BasicInputStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Enter;
import fr.lri.swingstates.sm.transitions.KeyPress;
import fr.lri.swingstates.sm.transitions.Leave;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.sm.transitions.TimeOut;



public class SimpleJoystick extends BasicApplet {

	BasicInputStateMachine interaction = new BasicInputStateMachine(){

		Point2D ptInit = null;
		int increment = 0;
		int currentValue = 0;
		JTextField tf;
		
		public State out = new State() {
			
			Transition enter = new Enter(">> in") {
				public void action() {
					tf = (JTextField)(((MouseEvent)getEvent()).getComponent());
					currentValue = Integer.parseInt(tf.getText());
					tf.setBackground(Color.YELLOW);
				}
			};
			
		};
		
		public State in = new State() {
			
			Transition leave = new Leave(">> out") {
				public void action() {
					tf.setBackground(Color.WHITE);
				}
			};
			
			Transition pressPlus = new KeyPress('+') {
				public void action() {
					tf.setText(""+(currentValue+1));
					currentValue++;
				}
			};
			
			Transition pressMinus = new KeyPress('-') {
				public void action() {
					tf.setText(""+(currentValue-1));
					currentValue--;
				}
			};
			
			Transition press = new Press(BUTTON1, ">> control") {
				public void action() {
					// get the location where this transition occured
					ptInit = getPoint();
					// get the underlying Java mouse event
					MouseEvent event = (MouseEvent)getEvent();
					// get the textfield and its value
					tf = (JTextField)event.getComponent();
					currentValue = Integer.parseInt(tf.getText());
					// arm a timer to update value even if mouse does not move
					armTimer(200, true);
				}
			};
			
		};
		
		public State control = new State() {
			
			Transition drag = new Drag(BUTTON1) {
				public void action() {
					disarmTimer();
					// increment is defined by vector (initial point, current point)
					increment = (int)(ptInit.distance(getPoint())/3);
					if(getPoint().getY() > ptInit.getY()) increment = -increment;
					// add increment to current value
					tf.setText(""+(currentValue+increment));
					currentValue += increment;
					// arm a timer to update value even if mouse does not move
					armTimer(200, true);
				}
			};
			
			Transition timeout = new TimeOut() {
				public void action() {
					tf.setText(""+(currentValue+increment));
					currentValue += increment;
				}
			};
			
			Transition release = new Release(BUTTON1, ">> out") { };
			
			public void leave() {
				tf.setBackground(Color.WHITE);
			}
		};
		
	};
	
	public void createGUI() {
		JTextField text = new JTextField("0");
		text.setText("0");
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 300));
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		text.setEditable(false);
		text.setSize(new Dimension(100, 30));
		Dimension size = text.getSize();
		text.setBounds(150 - size.width/2, 150 - size.height/2, size.width, size.height);
		panel.add(text);
		
		interaction.addAsListenerOf(text);
		
		StateMachineVisualization smv1 = new StateMachineVisualization(interaction);
		
		JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		jsp.add(panel);
		jsp.add(smv1);
		add(jsp);
		setSize(new Dimension(800, 300));
		
		text.requestFocus();
	}
	
}
