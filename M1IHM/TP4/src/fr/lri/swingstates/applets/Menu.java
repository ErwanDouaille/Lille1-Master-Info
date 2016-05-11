/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CStateMachine.EnterOnTag;
import fr.lri.swingstates.canvas.CStateMachine.LeaveOnTag;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;

public class Menu {
	
	public static final Color  BG_COLOR       = new Color(251,249,201);
	public static final Color  HILITE_COLOR   = new Color(165,246,154);
	public static final Color  BORDER_COLOR      = new Color(130,195,240);
	public static final Font   FONT              = new Font("verdana", Font.PLAIN, 12);
	
	protected CShape parent;
	protected CExtensionalTag tagWhole, tagLabels;
	
	protected CStateMachine interaction;
	protected GraphicalEditor canvas;
	
	public static CStateMachine getHiliteMachine() {
		return new CStateMachine() {
			public State out = new State() {
				Transition hiliteItem = new EnterOnTag(MenuItem.class, ">> in") {
					public void action() {
						getShape().setFillPaint(Menu.HILITE_COLOR);
					}
				};
			};
			public State in = new State() {
				Transition unhiliteItem = new LeaveOnTag(MenuItem.class, ">> out") {
					public void action() {
						getShape().setFillPaint(Menu.BG_COLOR);
					}
				};
			};
		};
	};
	
	public Menu(GraphicalEditor canvas) {
		this.canvas = canvas;
		tagWhole = new CExtensionalTag(canvas) { };
		tagLabels = new CExtensionalTag(canvas) { };
	}

	void showMenu(Point2D pt) {
		parent.translateTo(pt.getX(), pt.getY()).setDrawable(true);
		tagWhole.setDrawable(true).setPickable(true);
		tagWhole.aboveAll();
		tagLabels.aboveAll();
		tagLabels.setPickable(false);
	}

	void hideMenu() {
		tagWhole.setDrawable(false).setPickable(false);
	}

	public CStateMachine getInteraction() {
		return interaction;
	}
}

