package fr.lri.swingstates.gestures;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CRectangle;

/**
 * A <code>javax.swing.Icon<code> of a gesture.
 * 
 * @author Caroline Appert
 *
 */
public class GestureIcon implements Icon {

	private int width;
	private int height;
	private CPolyLine polyline;
	private CEllipse startPoint;
	private double sideStartingPoint;
	
	public GestureIcon(int width, int height, AbstractClassifier classifier, String className) {
		this.width = width;
		this.height = height;
		polyline = classifier.getRepresentative(className).asPolyLine();
		double sx = (width - 6) /polyline.getWidth();
		double sy = (height - 6) /polyline.getHeight();
		sideStartingPoint = Math.min(width, height) / 5;
		startPoint = new CEllipse(polyline.getStartX() - (sideStartingPoint/2)/sx, polyline.getStartY() - (sideStartingPoint/2)/sy, sideStartingPoint/sx, sideStartingPoint/sy);
		startPoint.setFillPaint(Color.RED).setOutlinePaint(Color.RED).setPickable(false);
		polyline.addChild(startPoint);
		polyline.scaleBy(sx, sy);
		polyline.setFilled(false);
	}
	
	public int getIconHeight() {
		return this.width;
	}

	public int getIconWidth() {
		return this.height;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color color = c.getForeground();
		Color saveColor = g.getColor();
		CRectangle bg = new CRectangle(x, y, width, height);
		bg.setFilled(false);
		bg.paint(g);
		polyline.setReferencePoint(0, 0).translateTo(x + sideStartingPoint/2 + 1, y + sideStartingPoint/2 + 1);
		polyline.setOutlinePaint(color).setAntialiased(true);
		polyline.paint(g);
		startPoint.paint(g);
		g.setColor(saveColor);
	}

}

