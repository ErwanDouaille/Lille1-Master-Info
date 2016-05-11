package fr.lri.swingstates.applets;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.lri.swingstates.sm.JStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;

public class HighlightWidgets extends BasicApplet {

	JStateMachine smWidgets;
	JLabel label1, label2, label3;
	
	public HighlightWidgets() {
		interaction();
		swingLayout();
	}
	
	public void interaction() {
		smWidgets = new JStateMachine() {
			
			Color initColor;
			
			public State out = new State() {
				Transition enter = new EnterOnComponent(">> in") {
					public void action() {
						initColor = getComponent().getBackground();
						getComponent().setBackground(Color.YELLOW);
					}
				};
			};
			
			public State in = new State() {
				
				Transition getClass = new ClickOnComponent(BUTTON1) {
					public void action() {
						label1.setText("This widget is a "+getComponent().getClass().getName());
						label2.setText("Coordinates in top level container: ("
								+getPoint().getX()+", "+getPoint().getY()+")");
						label3.setText("Coordinates in local component    : ("
								+getPointInComponent(getComponent()).getX()+", "+getPointInComponent(getComponent()).getY()+")");
					}
				};
				
				Transition enter = new LeaveOnComponent(">> out") {
					public void action() {
						getComponent().setBackground(initColor);
					}
				};
			};
			
		};
	}
	
	public void swingLayout() {
		setLayout(null);
		setBackground(Color.WHITE);
		
		JButton button = new JButton("button");
		Dimension size = button.getPreferredSize();
		add(button);
		button.setBounds(20, 30,
				size.width, size.height);
		
		JCheckBox checkbox = new JCheckBox("checkbox");
		size = checkbox.getPreferredSize();
		add(checkbox);
		checkbox.setBounds(120, 100,
				size.width, size.height);
		
		JTextField textfield = new JTextField("textfield");
		size = textfield.getPreferredSize();
		add(textfield);
		textfield.setBounds(250, 20,
				size.width, size.height);
		
		label1 = new JLabel("This widget is a <class_widget>");
		label2 = new JLabel("Coordinates in top level container: (0, 0)");
		label3 = new JLabel("Coordinates in local component: (0, 0)");
		size = label2.getPreferredSize();
		label1.setBounds(20, 160, 320, size.height);
		label2.setBounds(20, 180, 320, size.height);
		label3.setBounds(20, 200, 320, size.height);
		add(label1);
		add(label2);
		add(label3);
		label1.setOpaque(true);
		label2.setOpaque(true);
		label3.setOpaque(true);
		label1.setBackground(Color.GREEN);
		label2.setBackground(Color.GREEN);
		label3.setBackground(Color.GREEN);
		
	}
	
	public void createGUI() {
		new HighlightWidgets();
		// when attaching a container to a state machine,
		// all its current children are also attached.
		smWidgets.attachTo(this);
		setSize(360, 230);
	}

}
