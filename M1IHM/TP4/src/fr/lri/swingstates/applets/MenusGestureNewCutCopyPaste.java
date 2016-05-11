/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;

//public class MenusGestureNewCutCopyPaste extends BasicApplet {
public class MenusGestureNewCutCopyPaste extends BasicJApplet {

	GesturalInteraction gesturalInteraction;
	CStateMachine markingMenuInteraction;
	CText modeInfo;
	
	public void createGUI() {
		final GraphicalEditorSM editor = new GraphicalEditorSM(400, 600);
		String[] format = {
				"new",
				"cut",
				"copy",
				"paste",
		};

		gesturalInteraction = new GesturalInteraction(true, this, editor);
		markingMenuInteraction = (new MarkingMenu(true, editor, format)).getInteraction();
		new LinearMenu(true, editor, format);

		Menu.getHiliteMachine().attachTo(editor);
		
		gesturalInteraction.suspend();
		modeInfo = editor.newText(20, 20, "marking menu mode", new Font("verdana", Font.PLAIN, 14));
		CStateMachine modes = new CStateMachine(editor) {
		    public State markingMenuMode = new State() {
		        Transition gestureMode = new KeyPress(KeyEvent.VK_SHIFT, ">> gestureMode") {
		            public void action() {
		                markingMenuInteraction.suspend();
		                gesturalInteraction.reset().resume();
		                modeInfo.setText("gesture mode");
		            }
		        };
		    };
		    public State gestureMode = new State() {
		        Transition markingMenu = new KeyPress(KeyEvent.VK_SHIFT, ">> markingMenuMode") {
		            public void action() {
		                gesturalInteraction.suspend();
		                markingMenuInteraction.reset().resume();
		                modeInfo.setText("marking menu mode");
		            }
		        };
		    };
		    
		};

		getContentPane().add(editor);

		setSize(600, 300);
		
		
		// Trick to get keyboard events: in a japplet, the key listener
		// must be attached to the japplet itself to work whereas a
		// SwingStates canvas contains its own keylistener that processes
		// events in state machines.
		// In an application, we can use only the following line:
		//    editor.requestFocus();
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				editor.dispatchEvent(e);
			}
			public void keyReleased(KeyEvent e) {
				editor.dispatchEvent(e);
			}
		});
		requestFocus();
		

	}

}
