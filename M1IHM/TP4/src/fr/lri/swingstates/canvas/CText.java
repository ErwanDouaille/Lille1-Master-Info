/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * A text shape for use with an <code>Canvas</code>.
 * The text is drawn according to the fill paint and the text font.
 * If the shape is outlined, the bounding box of the text is drawn according to the outline paint and stroke.
 * 
 * @author Caroline Appert
 */
public class CText extends CShape {

	/**
     * The FontRenderContext used to render this text.
     */
    public static FontRenderContext FRC = new FontRenderContext(null, false, false);
	
    private double yOffSet = 0;
	private String text;
	private Font font;
	
	/**
	 * Builds a SMtext.
	 * @param loc The lower left point of this CText.
	 * @param txt The text of this CText.
	 * @param f The font of this CText.
	 */
	public CText(Point2D loc, String txt, Font f) {
		super(null);
		init(loc, txt, f);
		setOutlined(false);
		setFillPaint(Color.BLACK);
	}
	
	// only used by duplicate
	private CText() {
		super();
	}
	
	private void init(Point2D loc, String txt, Font f){
		font = f;
		setText(txt, loc);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		Shape saveClip = g2d.getClip();
		AffineTransform saveTransform = g2d.getTransform();
		RenderingHints saveRenderingHints = g2d.getRenderingHints();
		Composite saveComposite = g2d.getComposite();
		
		if (renderingHints != null) {
			g2d.addRenderingHints(renderingHints);
		}
		if (clip != null && (canvas != null && clip != canvas.clip)) {
			if (clip == DEFAULT_CLIP) {
				g2d.setClip(0, 0, canvas.getWidth(), canvas.getHeight());
			} else {
				g2d.transform(clip.getAbsTransform());
				g2d.setClip(clip.getShape());
				g2d.setTransform(saveTransform);
			}
		}
		
		g2d.setFont(font);
		g2d.setPaint(fillPaint);
		
		if(filled || outlined) {
			CShape s = parent;
			
			AffineTransform xform = new AffineTransform();
			while (s != null) {
				s.getAbsShape();
				xform.preConcatenate (s.transform);
				s = s.parent;
			}
			g2d.transform(xform);
			
			
			Rectangle2D bounds = shape.getBounds2D();
			double dx = bounds.getWidth()*rx;
			double dy = bounds.getHeight()*ry;
			dx += bounds.getX();
			dy += bounds.getY();
			g2d.translate(tx+dx, ty+dy);
			g2d.rotate (theta);
			g2d.scale (sx, sy); 
			g2d.translate (-bounds.getWidth()*rx, -bounds.getHeight()*ry);
			if(filled) {
				if (transparencyFill != null)
					g2d.setComposite(transparencyFill);
				g2d.drawString(text, 0, -(int)yOffSet);
				g2d.setComposite(saveComposite);
			}
			g2d.setTransform(saveTransform);
			if(outlined) {
				g2d.setStroke(stroke);
				g2d.transform(getAbsTransform());
				if (transparencyOutline != null)
					g2d.setComposite(transparencyOutline);
				g2d.setPaint(outlinePaint);
				g2d.draw(shape);
				g2d.setTransform(saveTransform);
				g2d.setComposite(saveComposite);
			}
			g2d.setClip(saveClip);
			g2d.setRenderingHints(saveRenderingHints);
		}

	}
	
	/**
	 * Returns the nearest char from the location <code>(x, y)</code>.
	 * @param x The x-coordinate (in the canvas coordinate system)
	 * @param y The y-coordinate (in the canvas coordinate system)
	 * @return The nearest char.
	 */
	public char getChar(double x, double y) {
		Point2D pt = new Point2D.Double();
		char res = 0;
		try {
			getAbsTransform().inverseTransform(new Point2D.Double(x, y), pt);
			int indexMin = 0;
			double minDis = Double.MAX_VALUE;
			Rectangle2D bounds;
			double xOffset = 0;
			for(int i = 0; i < text.length(); i++) {
				bounds = font.getStringBounds(""+text.charAt(i), FRC);
				double d = pt.distance(xOffset+bounds.getCenterX(), bounds.getCenterY());
				xOffset+=bounds.getWidth();
				if(d < minDis) {
					minDis = d;
					indexMin = i;
				}
			}
			return text.charAt(indexMin);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * Returns the nearest char from the point <code>p</code>.
	 * @param p The pont (in the canvas coordinate system)
	 * @return The nearest char.
	 */
	public char getChar(Point2D p) {
		Point2D pt = new Point2D.Double();
		char res = 0;
		try {
			getAbsTransform().inverseTransform(p, pt);
			int indexMin = 0;
			double minDis = Double.MAX_VALUE;
			Rectangle2D bounds;
			double xOffset = 0;
			for(int i = 0; i < text.length(); i++) {
				bounds = font.getStringBounds(""+text.charAt(i), FRC);
				double d = pt.distance(xOffset+bounds.getCenterX(), bounds.getCenterY());
				xOffset+=bounds.getWidth();
				if(d < minDis) {
					minDis = d;
					indexMin = i;
				}
			}
			return text.charAt(indexMin);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * Returns a <code>CRectangle</code> that surrounds this text. 
	 * If the bounding box of the text is a box whose width and height are respectively w and h,
	 * the result rectangle is centered at the center of the text and has a width <code>ratioX</code>*w and a height <code>ratioY</code>*h.
	 * This method does NOT add the built CRectangle to the canvas.
	 * @param ratioX The width ratio.
	 * @param ratioY The height ratio.
	 * @return The surrounding CRectangle.
	 */
	public CRectangle getRelativeRectangle(double ratioX, double ratioY) {
		Rectangle2D bounds = getAbsShape().getBounds2D();
		return (CRectangle) (
				new CRectangle(
						bounds.getCenterX() - ratioX*getWidth()/2, 
						bounds.getCenterY() - ratioY*getHeight()/2, 
						bounds.getWidth() * ratioX, 
						bounds.getHeight() * ratioY))
				.setReferencePoint(0, 1);
	}
	
	/**
	 * Returns a <code>CEllipse</code> that surrounds this text. 
	 * If the bounding box of the text is a box whose width and height are respectively w and h,
	 * the result ellipse is centered at the center of the text and has a width <code>ratioX</code>*w and a height <code>ratioY</code>*h.
	 * This method does NOT add the built CRectangle to the canvas.
	 * @param ratioX The width ratio.
	 * @param ratioY The height ratio.
	 * @return The surrounding CEllipse.
	 */
	public CEllipse getRelativeEllipse(double ratioX, double ratioY) {
		Rectangle2D bounds = super.getAbsShape().getBounds2D();
		return (CEllipse) (
				new CEllipse(
						bounds.getCenterX() - ratioX*getWidth()/2, 
						bounds.getCenterY() - ratioY*getHeight()/2, 
						bounds.getWidth() * ratioX, 
						bounds.getHeight() * ratioY))
				.setReferencePoint(getReferenceX(), getReferenceY());
	}
	
	/**
	 * Returns a <code>CRectangle</code> that surrounds this text. 
	 * The result rectangle is centered at the center of the text and has a width <code>w</code> and a height <code>h</code>.
	 * This method does NOT add the built CRectangle to the canvas.
	 * @param w The width ratio.
	 * @param h The height ratio.
	 * @return The surrounding CRectangle.
	 */
	public CRectangle getAbsoluteRectangle(double w, double h) {
		Rectangle2D bounds = getAbsShape().getBounds2D();
		return (CRectangle) (
				new CRectangle(
						bounds.getMinX(), 
						bounds.getMinY(), 
						bounds.getWidth(), 
						bounds.getHeight()))
				.setReferencePoint(0, 1);
	}
	
	/**
	 * Returns a <code>CEllipse</code> that surrounds this text. 
	 * The result ellipse is centered at the center of the text and has a width <code>w</code> and a height <code>h</code>.
	 * This method does NOT add the built CRectangle to the canvas.
	 * @param w The width ratio.
	 * @param h The height ratio.
	 * @return The surrounding CEllipse.
	 */
	public CEllipse getAbsoluteEllipse(double w, double h) {
		Rectangle2D bounds = getAbsShape().getBounds2D();
		return (CEllipse) (
				new CEllipse(
						bounds.getMinX(), 
						bounds.getMinY(), 
						bounds.getWidth(), 
						bounds.getHeight()))
				.setReferencePoint(0, 1);
	}
	
	/**
	 * Returns the text.
	 * @return the text.
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Sets the reference point of this <code>CText</code>
	 * to its baseline.
	 * 
	 * <p>
	 * By default, the position of a <code>CText</code> specifies
	 * the upper left corner of its bounding box. For example, the
	 * following lines makes the two <code>CText</code> be displayed
	 * below the segment [(10, 20), (50, 20)]: 
	 * <pre>
	 * CText text1 = canvas.newText(10, 20, "ab");
	 * CText text2 = canvas.newText(30, 20, "pc");
	 * canvas.newSegment(10, 20, 50, 20);
	 * </pre>
	 * This can be an issue for setting the position of a <code>CText</code> 
	 * according to its baseline.
	 * Setting the reference point of a <code>CText</code> to its
	 * baseline allows to overcome this problem:
	 * <pre>
	 * text1.setReferencePointToBaseline().translateTo(10, 20);
	 * text2.setReferencePointToBaseline().translateTo(30, 20);
	 * </pre>
	 * text1 and text2 appear as if they were written on 
	 * the same virtual line positioned at y=20.
	 * </p>
	 * 
	 * @return this <code>CText</code>.
	 */
	public CText setReferencePointToBaseline() {
		double y =  - yOffSet / getHeight();
		return (CText)super.setReferencePoint(0, y);
	};

	/**
	 * Sets the text to be displayed.
	 * @param text The text to set.
	 * @return this shape.
	 */
	public CText setText(String text) {
		if(shape == null) {
			setText(text, new Point2D.Double(0, 0));
		} else {
			setText(text, new Point2D.Double(shape.getBounds().getX(), shape.getBounds().getY()));
		}
		return this;
	}

	/**
	 * Sets the text to be displayed.
	 * @param text The text to set.
	 * @return this shape.
	 */
	private CText setText(String text, Point2D location) {
		this.text = text;
		Rectangle2D bounds = font.getStringBounds(text, FRC);
		
		yOffSet = bounds.getMinY();
		shape = new Rectangle2D.Double(location.getX(), location.getY(), bounds.getWidth(), bounds.getHeight());
		
		changedTransform();
		getAbsShape();
		repaint();
		return this;
	}
	
	/**
	 * Returns the text font.
	 * @return the text font.
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Sets the text font.
	 * @param f The font.
	 * @return this shape.
	 */
	public CText setFont (Font f) {
		font = f;
		setText(text);
		return this;
	}
	
	/**
	 * Copies this shape into a destination shape.
	 * @param sms The destination shape
	 * @return this shape
	 * @see CShape#copyTo
	 */
	public CShape copyTo (CShape sms) {
		if (sms instanceof CText) {
			CText smt = (CText) sms;
			smt.font = font;
			smt.setText(text, new Point2D.Double(0, 0));
		}
		super.copyTo(sms);
		return this;
	}
	
	/**
	 * Creates a new copy of this shape and returns it.
	 * @return the copy.
	 */
	public CShape duplicate() {
		CText sms = new CText();
		copyTo(sms);
		return sms;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setAntialiased(boolean a) {
		if(a) setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		else setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		setText(text);
		return this;
	}
	
}
