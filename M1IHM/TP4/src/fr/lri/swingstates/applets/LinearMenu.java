/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
 */
package fr.lri.swingstates.applets;

import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

public class LinearMenu extends Menu {
	private Point2D pInit;
	private CShape shapeUnderMenu;
	private boolean modeCommunicationMachine = false;
 
	public LinearMenu(boolean modeCommunicationMachine, GraphicalEditor canvas, String[] items) {
		this(canvas, items);
		this.modeCommunicationMachine = modeCommunicationMachine;
	}
	
	public LinearMenu(GraphicalEditor canvas, String[] items) {
		super(canvas);
		parent = canvas.newRectangle(-1, -1, 2, 2);
		parent.setDrawable(false).setPickable(false);

		CRectangle bgItem;
		CText labelItem;
		for(int i = 0; i < items.length; i++) {
			bgItem = canvas.newRectangle(0, i*20, 50, 20);
			bgItem.addTag(tagWhole).addTag(new MenuItem(items[i]))
			.setFillPaint(Menu.BG_COLOR).setOutlinePaint(Menu.BORDER_COLOR);

			labelItem = (CText) canvas.newText(0, 0, items[i], Menu.FONT).setPickable(false)
			.addTag(tagWhole).addTag(tagLabels)
			.setReferencePoint(0, 0.5).translateTo(3, bgItem.getCenterY());

			parent.addChild(labelItem).addChild(bgItem);

		}

		hideMenu();
		interaction = new CStateMachine() {
			public State menuOff = new State() {
				Transition invokeOnShape = new PressOnShape(BUTTON3, ">> menuOn") {
					public void action() {
						shapeUnderMenu = getShape();
						pInit = getPoint();
						showMenu(pInit);
						parent.setDrawable(false);
					}
				};
				Transition invoke = new Press(BUTTON3, ">> menuOn") {
					public void action() {
						pInit = getPoint();
						showMenu(pInit);
						parent.setDrawable(false);
					}
				};
			};

			public State menuOn = new State() {
				Transition select = new ReleaseOnTag(MenuItem.class, BUTTON3, ">> menuOff") {
					public void action() {
						String item = ((MenuItem)getTag()).getName();
						if(modeCommunicationMachine)
							LinearMenu.this.canvas.processEvent(item, pInit);
						else
							LinearMenu.this.canvas.command(item, pInit, shapeUnderMenu);
					}
				};
				Transition out = new Release(">> menuOff") { };
				public void leave() {
					hideMenu();
				}
			};
		};
		interaction.attachTo(canvas);
	}
	
}
