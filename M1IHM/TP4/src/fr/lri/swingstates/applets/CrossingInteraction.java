/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Component;

import fr.lri.swingstates.sm.JStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

public abstract class CrossingInteraction {

	public CrossingInteraction(Component panel, final Class widgetClass) {
		JStateMachine crossSM = new JStateMachine() {
			public State crossOff = new State() {
				Transition press = new Press(BUTTON1, ">> out") { };
			};
			
			public State out = new State() {
				Transition enter = new EnterOnTag(widgetClass.getName(), ">> in") { };
				Transition release = new Release(BUTTON1, ">> crossOff") { };
			};
			
			public State in = new State() {
				Transition leave = new LeaveOnTag(widgetClass.getName(), ">> out") {
					public void action() {
						invoke(getComponent());
					}
				};
				Transition release = new Release(BUTTON1, ">> crossOff") { };
			};
			
		};
		crossSM.attachTo(panel);
	}
	
	abstract void invoke(Component c) ;
	
}
