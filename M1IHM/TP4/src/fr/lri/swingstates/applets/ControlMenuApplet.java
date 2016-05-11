/*
 * Authors: Caroline Appert (caroline.appert@lri.fr) Copyright (c) Universite
 * Paris-Sud XI, 2007. All Rights Reserved Licensed under the GNU LGPL. For full
 * terms see the file COPYING.
 */
package fr.lri.swingstates.applets;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.debug.StateMachineVisualization;

// public class MenusGestureNewCutCopyPaste extends BasicApplet {
public class ControlMenuApplet extends BasicJApplet {

	CStateMachine controlMenuInteraction;
	CText modeInfo;
	GraphicalEditor editor;

	public void createGUI() {
		editor = new GraphicalEditor(400, 600);
		Command[] format = {
				new ContinuousCommand(editor, "scale", 10, 1, 1, 100) {
					public void start(CShape shape, Point2D pt) {
						if (shape == null)
							return;
						Point2D ps = shape.canvasToShape(pt);
						shape.setReferencePoint(ps.getX()
								/ shape.getShape().getBounds2D().getWidth(), ps
								.getY()
								/ shape.getShape().getBounds2D().getHeight());
					}

					public void apply(CShape shape, Point2D pt,
							double valueParam) {
						if (shape == null)
							return;
						super.apply(shape, pt, valueParam);
						shape.scaleTo(getRangeModel().getValue() / 10.0,
								getRangeModel().getValue() / 10.0);
					}
				}, new ContinuousCommand(editor, "new", 1, 1, 1, 400) {
					CRectangle newShape;
					Point2D ptInit;

					public void start(CShape shape, Point2D pt) {
						newShape = getCanvas().newRectangle(pt, 1, 1);
						newShape.setFillPaint(new Color(100, 200, 255));
						ptInit = pt;
					}

					public void apply(CShape shape, Point2D pt,
							double valueParam) {
						super.apply(shape, pt, valueParam);
						newShape.setDiagonal(ptInit, pt);
					}
				}, new ContinuousCommand(editor, "rotate", 0, 1, -180, 180) {
					public void start(CShape shape, Point2D pt) {
						if (shape == null)
							return;
						Point2D ps = shape.canvasToShape(pt);
						shape.setReferencePoint(ps.getX()
								/ shape.getShape().getBounds2D().getWidth(), ps
								.getY()
								/ shape.getShape().getBounds2D().getHeight());
						// System.out.println("setting ref point: "
						// + ps.getX()/shape.getShape().getBounds2D().getWidth()
						// + ", "
						// +
						// ps.getY()/shape.getShape().getBounds2D().getHeight());
					}

					public void apply(CShape shape, Point2D pt,
							double valueParam) {
						if (shape == null)
							return;
						super.apply(shape, pt, valueParam);
						shape.rotateTo(getRangeModel().getValue() / 180.0
								* Math.PI);
					}
				}, new Command(editor, "delete") {
					public void apply(CShape shape, Point2D pt,
							double valueParam) {
						getCanvas().removeShape(shape);
					}
				}, };

		// controlMenuInteraction = (new ControlMenu(editor,
		// format)).getInteraction();
		controlMenuInteraction = (new ControlMenu(editor, format)).machineAnimations;
		Menu.getHiliteMachine().attachTo(editor);

		getContentPane().setLayout(new GridLayout(1, 2));
		getContentPane().add(
				new StateMachineVisualization(controlMenuInteraction));

		getContentPane().add(editor);
		setSize(1200, 300);

	}

}
