package fr.lri.swingstates.applets;

import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.EventOnPosition;

public class GraphicalEditorSM extends GraphicalEditor {

	private CStateMachine smEdition = new CStateMachine() {

		public State idle = new State() {
			
			Transition newShape = new EventOnPosition("new"){
				public void action() {
					newShape(getPoint());
				}
			};
			
			Transition copy = new EventOnShape("copy") {
				public void action() {
					copy(getShape());
				}
			};
			
			Transition cut = new EventOnShape("cut"){
				public void action() {
					cut(getShape());
				}
			};

			Transition paste = new EventOnPosition("paste"){
				public void action() {
					paste(getPoint());
				}
			};
		};
	};		
	
	public GraphicalEditorSM(int w, int h) {
		super(w, h);
		smEdition.attachTo(this);
	}

}
