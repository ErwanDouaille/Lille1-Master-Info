/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CElement;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.StateMachine;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

public class DragAndDrop extends BasicApplet {
	StateMachine sm;
	Canvas canvas;
	
	public void createGUI(){
		
		canvas = new Canvas(300, 300);
		canvas.newEllipse(50, 50, 40, 30).setFillPaint(new GradientPaint(50, 50, Color.CYAN, 90, 80, Color.DARK_GRAY));
		canvas.newSegment(200, 50, 250, 110).setStroke(new BasicStroke(2));
		
		Font font = new Font("verdana", Font.PLAIN, 24);
		canvas.newText(30, 200, "Hello", font).setFillPaint(Color.GREEN);
		canvas.newPolyLine(150, 250).lineTo(190, 240).lineTo(200, 200).lineTo(210, 240).lineTo(250, 250).
		lineTo(210, 260).lineTo(200, 300).lineTo(190, 260).close().setFillPaint(Color.YELLOW).setTransparencyFill(0.5f);
		canvas.newRectangle(150, 150, 40, 30).setFillPaint(Color.BLUE);
		sm = new CStateMachine(canvas) {
			
			CElement toMove = null;
			Point2D lastPoint = null;
			
			public State start = new State() {
				
				Transition moveShape = new PressOnShape(BUTTON1, ">> drag"){
					public void action(){
						toMove = getShape();
						lastPoint = getPoint();
					}
				};
				
				Transition moveAll = new Press(BUTTON1, ">> drag"){
					public void action(){
						toMove = canvas;
						lastPoint = getPoint();
					}
				};
				
			};
			
			public State drag = new State() {

				public void enter() {
					toMove.setOutlinePaint(Color.RED);
				}
				
				Transition stop = new Release(BUTTON1, ">> start"){
					public void action(){
						toMove.translateBy(
								getPoint().getX() - lastPoint.getX(), 
								getPoint().getY() - lastPoint.getY());
					}
				};
				
				Transition move = new Drag(BUTTON1){
					public void action(){
						toMove.translateBy(
								getPoint().getX() - lastPoint.getX(), 
								getPoint().getY() - lastPoint.getY());
						lastPoint = getPoint();
					}
				};
				
				public void leave() {
					toMove.setOutlinePaint(Color.BLACK);
				}
			};
			
		};	
		
		StateMachineVisualization smv = new StateMachineVisualization(sm);
		setLayout(new GridLayout(1, 2));
		add(canvas);
		add(smv);
		setSize(620, 310);
		
	}
	
}	
		
		

