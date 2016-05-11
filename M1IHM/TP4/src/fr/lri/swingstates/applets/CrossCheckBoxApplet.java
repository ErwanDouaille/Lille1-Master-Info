/*
 * Authors: Caroline Appert (caroline.appert@lri.fr) Copyright (c) Universite
 * Paris-Sud XI, 2007. All Rights Reserved Licensed under the GNU LGPL. For full
 * terms see the file COPYING.
 */
package fr.lri.swingstates.applets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.geom.Point2D;

import javax.swing.JApplet;
import javax.swing.JCheckBox;

import fr.lri.swingstates.canvas.CSegment;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

public class CrossCheckBoxApplet extends JApplet {

	private Canvas canvasForInking;
	
	public void init() {
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) {
			System.err.println("createGUI didn't successfully complete");
		}
	}

	// Install a canvas to draw ink left by cursor when he is in crossing mode.
	public void enableInk() {
		new CStateMachine(canvasForInking) {
			Point2D ptInit = new Point2D.Double(0, 0);
			CSegment ink;

			// Point2D lastPoint;
			public void doReset() {
				ink = (CSegment) canvasForInking.newSegment(0, 0, 1, 1).setDrawable(false)
						.setFilled(false).setStroke(
								new BasicStroke(1.3f, BasicStroke.CAP_ROUND,
										BasicStroke.JOIN_ROUND))
						.setAntialiased(true);
			}

			State off = new State() {
				Transition press = new Press(BUTTON1, ">> on") {
					public void action() {
						ptInit.setLocation(getPoint());
						ink.setPoints(ptInit, getPoint());
						ink.setDrawable(true);
						// lastPoint = getPoint();
					}
				};
			};
			State on = new State() {
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						ink.setPoints(ptInit, getPoint());
						// lastPoint = getPoint();
					}
				};
				Transition release = new Release(BUTTON1, ">> off") {
					public void action() {
						ink.setDrawable(false);
					}
				};
				// Transition crossed = new Event("crossed") {
				// public void action() {
				// ptInit = lastPoint;
				// ink.setPoints(lastPoint, lastPoint);
				// }
				// };
			};
		};
		canvasForInking.setTransparencyFill(0.5f);
		canvasForInking.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		canvasForInking.setOpaque(false);
		rootPane.setGlassPane(canvasForInking);
		rootPane.getGlassPane().setVisible(true);
	}

	private void createGUI() {
		setSize(150, 300);
		setLayout(null);
		// String[] name = {"state machines", "Java", "Swing", "SwingStates"};
		String[] name = { "UIST", "2006", "Montreux", "Suisse", "SwingStates",
				"TechNote" };
		JCheckBox[] tabCheckBoxes = new JCheckBox[name.length];
		for (int i = 0; i < name.length; i++) {
			tabCheckBoxes[i] = new JCheckBox(name[i]);
			tabCheckBoxes[i].setBounds(20, 40 + 40 * i, (int) tabCheckBoxes[i]
					.getPreferredSize().getWidth(), (int) tabCheckBoxes[i]
					.getPreferredSize().getHeight());
			add(tabCheckBoxes[i]);
		}
		//
		// String[] namebuttons = {"apply", "ok"};
		// JButton[] tabButtons = new JButton[namebuttons.length];
		// for(int i = 0; i < namebuttons.length; i++) {
		// tabButtons[i] = new JButton(namebuttons[i]);
		// tabButtons[i].setBounds(200, 100 + 40*i, 75, 30);
		// add(tabButtons[i]);
		// }

		canvasForInking = new Canvas(getContentPane().getWidth(),
				getContentPane().getHeight());
		enableInk();

		new CrossingInteraction(this, JCheckBox.class) {
			void invoke(Component c) {
				((JCheckBox) c).setSelected(!((JCheckBox) c).isSelected());
				// ink.processEvent("crossed");
			}
		};
		//
		// new CrossingInteraction(this, JButton.class) {
		// void invoke(Component c) {
		// ((JButton)c).doClick();
		// // ink.processEvent("crossed");
		// }
		// };

	}

}
