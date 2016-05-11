/*
 * Authors: Caroline Appert (caroline.appert@lri.fr) Copyright (c) Universite
 * Paris-Sud XI, 2007. All Rights Reserved Licensed under the GNU LGPL. For full
 * terms see the file COPYING.
 */
package fr.lri.swingstates.applets;

import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.sm.transitions.TimeOut;

public class MarkingMenu extends Menu {
	private Point2D pInit;
	private CShape shapeUnderMenu;
	private boolean modeCommunicationMachine = false;

	public MarkingMenu(boolean modeCommunicationMachine, GraphicalEditor canvas, String[] items) {
		this(canvas, items);
		this.modeCommunicationMachine = modeCommunicationMachine;
	}

	public MarkingMenu(GraphicalEditor canvas, String[] items) {
		super(canvas);

		menuLayout(items);
		hideMenu();

		interaction = new CStateMachine() {
			public State menuOff = new State() {
				Transition invokeOnShape = new PressOnShape(BUTTON1, ">> menuOn") {
					public void action() {
						pInit = getPoint();
						shapeUnderMenu = getShape();
						showMenu(pInit);
					}
				};
				Transition invoke = new Press(BUTTON1, ">> menuOn") {
					public void action() {
						pInit = getPoint();
						showMenu(pInit);
					}
				};

				public void leave() {
					// menu is interactive but not visible
					tagWhole.setPickable(true);
					tagLabels.setPickable(false);
					armTimer(300, false);
				}
			};

			public State menuOn = new State() {
				String lastItemVisited;
				Transition select = new Release(BUTTON1, ">> menuOff") {
					public void action() {
						disarmTimer();
						if (lastItemVisited != null) {
							if (modeCommunicationMachine)
								MarkingMenu.this.canvas.processEvent(lastItemVisited, pInit);
							else
								MarkingMenu.this.canvas.command(lastItemVisited, pInit, shapeUnderMenu);
						}
					}
				};
				Transition changeItem = new EnterOnTag(MenuItem.class) {
					public void action() {
						lastItemVisited = ((MenuItem) getTag()).getName();
					}
				};
				// menu center
				Transition cancel = new EnterOnShape() {
					public void action() {
						lastItemVisited = null;
					}
				};
				Transition showMenu = new TimeOut() {
					public void action() {
						tagWhole.setDrawable(true);
					}
				};

				public void leave() {
					hideMenu();
				}
			};
		};
		interaction.attachTo(canvas);
	}

	@Override
	void showMenu(Point2D pt) {
		parent.translateTo(pt.getX(), pt.getY()).setDrawable(true);
		tagWhole.setPickable(true);
		tagLabels.setPickable(false);
		tagWhole.aboveAll();
		tagLabels.aboveAll();
	}

	void menuLayout(String[] items) {
		parent = canvas.newEllipse(-5, -5, 10, 10);

		CShape clipCircle = (new CEllipse(-50, -50, 100, 100)).getSubtraction(new CEllipse(-30, -30, 60, 60));
		clipCircle.setFilled(false).setOutlined(false);
		clipCircle.addTag(tagWhole).setParent(parent);

		CPolyLine bgItem;
		CText labelItem;
		double angleStep = 2 * Math.PI / items.length;
		for (int i = 0; i < items.length; i++) {
			bgItem = canvas.newPolyLine(0, 0).lineTo(50, 0).arcTo(0, -angleStep, 50, 50).close();
			bgItem.addTag(tagWhole).addTag(new MenuItem(items[i])).setReferencePoint(0, 0).rotateBy(i * angleStep).setFillPaint(Menu.BG_COLOR).setOutlinePaint(Menu.BORDER_COLOR).setClip(clipCircle);

			labelItem = (CText) canvas.newText(0, 0, items[i], Menu.FONT).setPickable(false).addTag(tagWhole).addTag(tagLabels).setReferencePoint(0.5, 0.5).translateTo(
					Math.cos((i * angleStep) + angleStep / 2) * 40, Math.sin((i * angleStep) + angleStep / 2) * 40);

			parent.addChild(labelItem).addChild(bgItem);

		}

		parent.addTag(tagWhole);
		tagLabels.aboveAll();
		parent.aboveAll();
	}

}
