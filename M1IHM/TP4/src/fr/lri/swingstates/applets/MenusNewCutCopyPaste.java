/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.debug.StateMachineVisualization;

public class MenusNewCutCopyPaste extends BasicApplet {

	MarkingMenu markingMenuInteraction;
	LinearMenu linearMenuInteraction;
	
	public void createGUI() {
		GraphicalEditor editor = new GraphicalEditor(400, 600);
		String[] format = {
				"new",
				"cut",
				"copy",
				"paste",
		};
		
		markingMenuInteraction = new MarkingMenu(editor, format);
		linearMenuInteraction = new LinearMenu(editor, format);
		
		CStateMachine hilite = Menu.getHiliteMachine();
		hilite.attachTo(editor);
		StateMachineVisualization smHilite = new StateMachineVisualization(hilite);
		StateMachineVisualization smMarkingMenu = new StateMachineVisualization(markingMenuInteraction.getInteraction());
		StateMachineVisualization smLinearMenu = new StateMachineVisualization(linearMenuInteraction.getInteraction());
		
		GridBagLayout gridBagLayoutManager = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(gridBagLayoutManager);
		buildConstraints(constraints, 0, 1, 1, 1, 50, 33, GridBagConstraints.BOTH, GridBagConstraints.NORTH);
		add(smLinearMenu, constraints);
		buildConstraints(constraints, 1, 1, 1, 1, 50, 33, GridBagConstraints.BOTH, GridBagConstraints.NORTH);
		add(smMarkingMenu, constraints);
		buildConstraints(constraints, 0, 2, 2, 1, 100, 33, GridBagConstraints.BOTH, GridBagConstraints.NORTH);
		add(smHilite, constraints);
		
		buildConstraints(constraints, 0, 0, 2, 1, 100, 34, GridBagConstraints.BOTH, GridBagConstraints.NORTH);
		add(editor, constraints);

		setSize(600, 800);
		editor.requestFocus();
	}
	
	static void buildConstraints(GridBagConstraints constraints, int x, int y, int w, int h, double columnExpand, double rowExpand, int componentFill, int componentAnchor) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.weightx = columnExpand;
		constraints.weighty = rowExpand;
		constraints.fill = componentFill;
		constraints.anchor = componentAnchor;
	}

}

