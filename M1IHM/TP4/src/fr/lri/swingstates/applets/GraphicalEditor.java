package fr.lri.swingstates.applets;

import java.awt.Color;
import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.Canvas;

public class GraphicalEditor extends Canvas {

	private CShape shapeCopied, shapeCut;

	void command(String command, Point2D pt, CShape shape) {
		if(command != null) {
			if(command.compareTo("new") == 0)
				newShape(pt);
			else if(command.compareTo("paste") == 0)
				paste(pt);
			else if(command.compareTo("cut") == 0 && shape != null)
				cut(shape);
			else if(command.compareTo("copy") == 0 && shape != null)
				copy(shape);
		}
	}
	
	void cut(CShape s) {
		if(shapeCut != null)
			shapeCut.setTransparencyFill(1f).setTransparencyOutline(1f);
		shapeCopied = null;
		shapeCut = s;
		shapeCut.setTransparencyFill(0.5f).setTransparencyOutline(0.5f);
	}

	void copy(CShape s) {
		if(shapeCut != null)
			shapeCut.setTransparencyFill(1f).setTransparencyOutline(1f);
		shapeCut = null;
		shapeCopied = s;
	}

	void paste(Point2D pt) {
		if(shapeCut != null) {
			shapeCut.translateTo(pt.getX(), pt.getY())
			.setTransparencyFill(1f).setTransparencyOutline(1f);
			shapeCopied = shapeCut;
			shapeCut = null;
		} else {
			if(shapeCopied != null) {
				shapeCopied = shapeCopied.duplicate();
				shapeCopied.translateTo(pt.getX(), pt.getY()).setDrawable(true);
			}
		}
	}

	void newShape(Point2D pt){
		newRectangle(pt.getX(), pt.getY(), 40, 30)
		.setFillPaint(new Color((int)(Math.random()*254), (int)(Math.random()*254), (int)(Math.random()*254)))
		.translateTo(pt.getX(), pt.getY());
	}

	public GraphicalEditor(int w, int h) {
		super(w, h);
	}

}
