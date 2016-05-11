/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.sm;

import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;



public abstract class PressDragReleaseStateMachine extends JStateMachine {
	
	private int button, modifier;
	public State start, move;
	private double lastX, lastY;
	
	public PressDragReleaseStateMachine(int button, int modifier) {
		super();
		this.button = button;
		this.modifier = modifier;
		
		start = new State() {
			Transition press = new Press(PressDragReleaseStateMachine.this.button, PressDragReleaseStateMachine.this.modifier, ">> move") {
				public boolean guard() {
					return PressDragReleaseStateMachine.this.guard(getPoint().getX(), getPoint().getY());
				}
				public void action() {
					lastX = getPoint().getX();
					lastY = getPoint().getY();
					pressAction(getPoint().getX(), getPoint().getY());
				}
			};
		};
		move = new State() {
			Transition drag = new Drag(PressDragReleaseStateMachine.this.button) {
				public void action() {
					dragAction(lastX, lastY, getPoint().getX(), getPoint().getY());
					lastX = getPoint().getX();
					lastY = getPoint().getY();
				}
			};
			Transition release = new Release(PressDragReleaseStateMachine.this.button, ">> start") {
				public void action() {
					releaseAction(lastX, lastY, getPoint().getX(), getPoint().getY());
				}
			};
		};
	};
	
	public void pressAction(double currentX, double currentY) { };
	
	public void dragAction(double previousX, double previousY, 
			double currentX, double currentY) { };

	public void releaseAction(double previousX, double previousY, 
			double currentX, double currentY) { };
			
	public boolean guard(double x, double y) {
		return true;
	}
}

