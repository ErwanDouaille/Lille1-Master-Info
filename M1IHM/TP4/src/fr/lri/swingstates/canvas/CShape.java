/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.lri.swingstates.animations.Animation;

/**
 * A shape to be added to a <code>Canvas</code>. The shape is defined by a
 * <i>geometry</i> and some <i>grahical attributes</i>, and it can have
 * <i>tags</i>. A shape can be marked as drawable or not, and pickable or not.
 * When a shape is not drawable, it is not displayed by the enclosing canvas.
 * When a shape is not pickable, it is ignored by the enclosing canvas for pick
 * operations.
 * 
 * <p>
 * The <i>geometry</i> of a shape is defined by an arbitrary Java2D
 * <code>Shape</code>. Subclasses of <code>CShape</code> define specific
 * geometries, namely rectangle, ellipse, polyline, text, and image. The
 * geometry of a shape is transformed by an <i>affine transformation</i> that
 * combines a scale, a rotation and a translation. The default transformation is
 * the identity. The rotation, scale and translateTo operations occur around the
 * shape's <i>reference point</i>, which by default is at the center of the
 * shape's bounding box. The reference point can be changed. It is defined
 * relatively to the shape's bounding box, i.e (0, 0) means the top left corner
 * of the shape, (1,1) the bottom right corner, (0.5, 0.5) the center, etc.
 * 
 * <p>
 * By default, the geometry of the shape is relative to the enclosing canvas's
 * coordinate system. However a shape can have a <i>parent</i>, which is
 * another shape in the canvas. In this case the shape's coordinates are
 * relative to the transformation of its parent. This means that changing the
 * transform of the parent shape will affect its children. A shape can be the
 * parent of an arbitrary number of shapes, and can itself have a parent.
 * 
 * <p>
 * The last element that affects the shape's geometry is the <i>clipping shape</i>.
 * A shape can be clipped by another shape of the canvas. The clipped shape is
 * only visible within the interior of the clipping shape.
 * 
 * <p>
 * The <i>graphical attributes</i> of a shape consists of a fill paint, an
 * outline paint and a stroke style. The fill paint is used if the shape is
 * filled to paint the interior of the shape. The outline paint and stroke style
 * are used if the shape is outlined to paint the border of the shape. Paints
 * and stroke styles can be any Java2D objects that implement the
 * <code>Paint</code> and <code>Stroke</code> interfaces respectively.
 * Graphical attributes affect picking. For example, if a shape is not filled,
 * only the border stroke is taken into account for picking.
 * 
 * <p>
 * <i>Tags</i> can be attached to a shape. See <code>Tag</code> for details.
 * It is important to remember that tags cannot be added to a shape until the
 * shape is added to a canvas. This is because each canvas manages its own set
 * of tags.
 * 
 * @see fr.lri.swingstates.canvas.Canvas
 * @see fr.lri.swingstates.canvas.CTag
 * @author Caroline Appert
 */

public class CShape implements Cloneable, CElement {

	protected static final CShape DEFAULT_CLIP = new CRectangle();
	private static final Shape PLACEBO_SHAPE = new Line2D.Double();
	private static final BasicStroke DEFAULT_STROKE = new BasicStroke();

	protected Shape shape;
	protected boolean pickable = true;
	protected boolean drawable = true;
	protected Canvas canvas = null;

	// attributes
	protected boolean filled = true;
	protected Paint fillPaint = Color.LIGHT_GRAY;
	protected boolean outlined = true;
	protected Paint outlinePaint = Color.BLACK;
	protected Stroke stroke = DEFAULT_STROKE;
	protected AlphaComposite transparencyFill = null;
	protected AlphaComposite transparencyOutline = null;

	// protected boolean antialiased = false;

	protected RenderingHints renderingHints = null;

	protected CShape clip = null;

	// transformation
	protected double rx = 0.5, ry = 0.5; // the reference point relative, to
	// the bounding box. Default =
	// center of bounding box
	protected double tx = 0, ty = 0; // translation
	protected double theta = 0; // angle of rotation
	protected double sx = 1, sy = 1; // scale
	protected AffineTransform transform = new AffineTransform();
	protected AffineTransform absTransform = new AffineTransform();

	// hierarchy
	protected CShape parent = null;
	protected LinkedList<CShape> children = null;

	// ghost
	protected CShape ghost = null;

	private Rectangle pickingRectangle = new Rectangle();

	// ____________________________________ CONSTRUCTORS
	// _________________________________________________

	/**
	 * Builds an empty <code>CShape</code>.
	 */
	public CShape() {
		shape = PLACEBO_SHAPE;
		changedTransform();
	}

	/**
	 * Builds a <code>CShape</code> from a Java2D shape.
	 * 
	 * @param s
	 *            The java2D shape
	 */
	public CShape(Shape s) {
		if (s == null)
			shape = PLACEBO_SHAPE;
		else
			shape = s;
		changedTransform();
	}

	// ____________________________________ GENERAL FEATURES
	// _________________________________________________

	/**
	 * Return the canvas this shape belongs to, if any.
	 * 
	 * @return the canvas.
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * @param smc
	 *            The smcanvas to set.
	 */
	void setCanvas(Canvas smc) {
		canvas = smc;
	}

	/**
	 * Returns the Java2D shape of reference, i.e. the shape without any style and geometric transformation applied to it.
	 * 
	 * @return the shape.
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Sets the shape.
	 * 
	 * @param s
	 *            The new Java2D shape.
	 * @return this shape.
	 */
	public CElement setShape(Shape s) {
		shape = s;
		// changedShape();
		return this;
	}

	/**
	 * Adds this shape to a canvas. Note that a number of operations do nothing
	 * until the shape is added to a canvas, e.g. adding tags, setting the
	 * parent, adding children, setting a clip.
	 * 
	 * @param c
	 *            The canvas.
	 * @return this shape.
	 */
	public CShape addTo(Canvas c) {
		c.addShape(this);
		return this;
	}

	/**
	 * Removes this shape from its canvas. Note that a number of operations do
	 * nothing when the shape is not in a canvas, e.g. adding tags, setting the
	 * parent, adding children, setting a clip.
	 * 
	 * @return this shape.
	 */
	public CShape remove() {
		if (canvas != null)
			canvas.removeShape(this);
		return this;
	}

	// remove parent and children before the shape is removed from its canvas
	protected void prepareToRemove() {
		// remove from hierarchy
		setParent(null);
		if (children != null) {
			while (children.size() > 0)
				((CShape) children.getFirst()).setParent(null);
		}

		// remove tags
		List<CTag> canvasTags = canvas.allCanvasTags;
		synchronized(canvasTags) {
			for (Iterator<CTag> i = canvasTags.iterator(); i.hasNext();) {
				CTag next = i.next();
				if (next instanceof CExtensionalTag) {
					CExtensionalTag extTag = (CExtensionalTag) next;
					if (extTag.tagsShape(this))
						this.removeTag(extTag);
				}
			}
		}

		// detach from clip
		setClip(null);
		// remove ghost
		removeGhost();
		setCanvas(null);
	}

	// mark the shape to be repainted
	protected void repaint() {
		if (canvas != null) {
			canvas.repaint();
		}
	}

	// ____________________________________ DRAWING
	// _________________________________________________

	/**
	 * Paints the shape. Normally this method need not be called, since the
	 * canvas paints the display list automatically.
	 * 
	 * @param g
	 *            The graphics with which the shape must be painted.
	 */
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Shape saveClip = g2d.getClip();
		AffineTransform saveTransform = g2d.getTransform();
		RenderingHints saveRenderingHints = g2d.getRenderingHints();
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

		g2d.setStroke(stroke);
		g2d.transform(getAbsTransform());
		if (filled) {
			Composite comp = g2d.getComposite();
			if (transparencyFill != null)
				g2d.setComposite(transparencyFill);
			g2d.setPaint(fillPaint);
			g2d.fill(getShape());
			g2d.setComposite(comp);
		}
		if (outlined) {
			Composite comp = g2d.getComposite();
			if (transparencyOutline != null)
				g2d.setComposite(transparencyOutline);
			g2d.setPaint(outlinePaint);
			g2d.draw(shape);
			g2d.setComposite(comp);
		}
		g2d.setTransform(saveTransform);
		g2d.setClip(saveClip);
		g2d.setRenderingHints(saveRenderingHints);
	}

	/**
	 * Returns true if this shape is to be displayed.
	 * 
	 * @return true if this shape is to be displayed, false otherwise.
	 */
	public boolean isDrawable() {
		return drawable;
	}

	/**
	 * Specifies whether this shape is to be displayed.
	 * 
	 * @param d
	 *            True if the shape is to be displayed, false otherwise.
	 * @return this shape.
	 */
	public CElement setDrawable(boolean d) {
		drawable = d;
		repaint();
		return this;
	}

	/**
	 * Copies style attributes of a given CShape on this CShape.
	 * (filled [yes/no], fill color, transparentFill,
	 * outlined [yes/no], outline color, transparentOutline,
	 * stroke).
	 * 
	 * @param picked
	 *            The <code>CShape</code> on which the style attributes must be picked.
	 * @return this shape.
	 */
	public CElement pickStyle(CShape picked) {
		this.filled = picked.filled;
		this.fillPaint = picked.fillPaint;
		this.outlined = picked.outlined;
		this.outlinePaint = picked.outlinePaint;
		this.stroke = picked.stroke;
		this.transparencyFill = picked.transparencyFill;
		this.transparencyOutline = picked.transparencyOutline;
		repaint();
		return this;
	}
	
	/**
	 * Sets the <code>transparencyFill</code> of this shape, which applies to
	 * the interior.
	 * 
	 * @param transparencyFill
	 *            The <code>AlphaComposite</code> transparency mode.
	 * @return this shape.
	 */
	public CElement setTransparencyFill(AlphaComposite transparencyFill) {
		this.transparencyFill = transparencyFill;
		repaint();
		return this;
	}

	/**
	 * Sets the <code>transparencyOutline</code> of this shape, which applies
	 * to the boundary.
	 * 
	 * @param transparencyOutline
	 *            The <code>AlphaComposite</code> transparency mode.
	 * @return this shape.
	 */
	public CElement setTransparencyOutline(AlphaComposite transparencyOutline) {
		this.transparencyOutline = transparencyOutline;
		repaint();
		return this;
	}

	/**
	 * Sets the <code>transparencyFill</code> of this shape, which applies to
	 * the interior. The transparency composition mode is
	 * <code>AlphaComposite.SRC_OVER</code>, use the
	 * <code>setTransparencyFill(AlphaComposite)</code>.
	 * 
	 * @param alpha
	 *            The transparencyFill level (0.0 = transparent, 1.0 = opaque).
	 * @return this shape.
	 * @see CShape#setTransparencyFill(AlphaComposite)
	 */
	public CElement setTransparencyFill(float alpha) {
		transparencyFill = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		repaint();
		return this;
	}

	/**
	 * Sets the <code>transparencyOutline</code> of this shape, which applies
	 * to the boundary. The transparency composition mode is
	 * <code>AlphaComposite.SRC_OVER</code>, use the
	 * <code>setTransparencyOutline(AlphaComposite)</code>.
	 * 
	 * @param transparencyOutline
	 *            The <code>transparencyOutline</code> level (0.0 =
	 *            transparent, 1.0 = opaque).
	 * @return this shape.
	 * @see CShape#setTransparencyOutline(AlphaComposite)
	 */
	public CElement setTransparencyOutline(float transparencyOutline) {
		this.transparencyOutline = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparencyOutline);
		repaint();
		return this;
	}

	/**
	 * Returns true if this shape is filled. When the shape is filled, the
	 * interior of the shape is displayed.
	 * 
	 * @return true if this shape is filled.
	 */
	public boolean isFilled() {
		return filled;
	}

	/**
	 * Sets the filled property of this shape. When the shape is filled, the
	 * interior of the shape must be displayed.
	 * 
	 * @param isFilled
	 *            True if the shape is filled.
	 * @return this shape.
	 */
	public CElement setFilled(boolean isFilled) {
		filled = isFilled;
		repaint();
		return this;
	}

	/**
	 * @return the fill Paint, which is used to display the interior of the
	 *         shape when it is filled.
	 */
	public Paint getFillPaint() {
		return fillPaint;
	}

	/**
	 * @return the <code>AlphaComposite</code> used to render the interior of
	 *         this shape.
	 */
	public AlphaComposite getTransparencyFill() {
		return transparencyFill;
	}

	/**
	 * @return the alpha value of <code>AlphaComposite</code> used to fill
	 *         this shape.
	 */
	public AlphaComposite getTransparencyOutline() {
		return transparencyOutline;
	}

	/**
	 * Sets the fill Paint, which is used to display the interior of the shape
	 * when it is filled.
	 * 
	 * @param fp
	 *            The fill Paint.
	 * @return this shape.
	 */
	public CElement setFillPaint(Paint fp) {
		fillPaint = fp;
		repaint();
		return this;
	}

	/**
	 * @return true if this shape is outlined. When the shape is outlined, the
	 *         boundary of the shape are displayed.
	 */
	public boolean isOutlined() {
		return outlined;
	}

	/**
	 * Sets the outlined property of this shape. When the shape is outlined, the
	 * boundary of the shape when it is outlined.
	 * 
	 * @param o
	 *            True if the shape is outlined.
	 * @return this shape.
	 */
	public CElement setOutlined(boolean o) {
		outlined = o;
		repaint();
		return this;
	}

	/**
	 * Returns the stroke style, which specifies the shape of the boundary of
	 * the shape when it is outlined (thickness, dash pattern, etc.).
	 * 
	 * @return the stroke style.
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * Sets the stroke style, which specifies the shape of the boundary of the
	 * shape when it is outlined (thickness, dash pattern, etc.).
	 * 
	 * @param str
	 *            The stroke style.
	 * @return this shape.
	 */
	public CElement setStroke(Stroke str) {
		stroke = str;
		repaint();
		return this;
	}

	/**
	 * Returns the outline Paint, which is used to display the boundary of the
	 * shape when it is outlined.
	 * 
	 * @return the outline Paint.
	 */
	public Paint getOutlinePaint() {
		return outlinePaint;
	}

	/**
	 * Sets the outline Paint, which is used to display the boundary of the
	 * shape when it is outlined.
	 * 
	 * @param op
	 *            The outline Paint.
	 * @return this shape.
	 */
	public CElement setOutlinePaint(Paint op) {
		// setOutlined(op != null);
		outlinePaint = op;
		repaint();
		return this;
	}

	/**
	 * Returns the shape that clips this shape, if any. When a clipping shape is
	 * specified, this shape is displayed only within the interior of the
	 * clipping shape.
	 * 
	 * @return the clipping shape, or null if the shape is not clipped.
	 */
	public CShape getClip() {
		return clip;
	}

	/**
	 * Sets the clipping shape of this shape. When a clipping shape is
	 * specified, this shape is displayed only within the interior of the
	 * clipping shape.
	 * 
	 * @param clip
	 *            The clipping shape, or null to reset clipping of this shape.
	 * @return this shape.
	 */
	public CElement setClip(CShape clip) {
		if (clip == null) {
			this.clip = DEFAULT_CLIP;
			repaint();
			return this;
		}
		if (clip == this)
			return this;
		if (this.clip != clip) {
			this.clip = clip;
			repaint();
		}
		return this;
	}

	/**
	 * Returns true if this shape is antialiased. Antialiasing improves the
	 * visual quality of the displayed shapes, at the expense of longer display
	 * times. Note that the canvas has a global antialiasing mode, which is not
	 * tested by this method.
	 * 
	 * @return true if this shape is antialiased.
	 */
	public boolean isAntialiased() {
		if (renderingHints == null)
			return false;
		else
			return renderingHints.get(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON;
	}

	/**
	 * Specifies whether this shape is antialiased. Antialiasing improves the
	 * visual quality of the displayed shapes, at the expense of longer display
	 * times.
	 * 
	 * @param a
	 *            True if this shape is to be antialiased.
	 * @return this shape.
	 */
	public CElement setAntialiased(boolean a) {
		if(a) setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		repaint();
		return this;
	}

	/**
	 * Returns the rendering attribute of this <code>CShape</code> given a
	 * hint key. See <code>java.awt.RenderingHints</code> for available hint
	 * keys and hint values.
	 * 
	 * @param hintKey
	 *            The hint key
	 * @return The rendering hint object.
	 */
	public Object getRenderingHint(Key hintKey) {
		if (renderingHints == null)
			if (canvas.renderingHints == null)
				return null;
			else
				return canvas.renderingHints.get(hintKey);
		else
			return renderingHints.get(hintKey);
	}

	/**
	 * Defines the rendering attributes of this <code>CShape</code>. See
	 * <code>java.awt.RenderingHints</code> for available hint keys and hint
	 * values.
	 * 
	 * @param hintKey
	 *            The hint key
	 * @param hintValue
	 *            The hint value
	 * @return this <code>CShape</code>.
	 */
	public CElement setRenderingHint(Key hintKey, Object hintValue) {
		if (renderingHints == null) {
			renderingHints = new RenderingHints(hintKey, hintValue);
		} else
			renderingHints.put(hintKey, hintValue);
		return this;
	}

	// ____________________________________ HIERARCHY (PARENTS AND CHILDREN)
	// _________________________________________________

	/**
	 * Returns the hierarchy of this shape.
	 * 
	 * @return the hierarchy of this shape.
	 */
	public CHierarchyTag getHierarchy() {
		return new CHierarchyTag(this);
	}

	/**
	 * Returns the parent of this shape, if any. When a parent is specified, the
	 * shapes transformation is relative to the parent's transformation rather
	 * than the canvas. Changing a parent's transformation affects all its
	 * children.
	 * 
	 * @return the parent of this shape, or null if it has no parent.
	 */
	public CShape getParent() {
		return parent;
	}

	/**
	 * Returns the children of this shape as a linked list.
	 * 
	 * @return the children of this shape, or null if it has no parent.
	 */
	public LinkedList<CShape> getChildren() {
		return children;
	}

	/**
	 * Counts the number of children of this shape.
	 * 
	 * @return the number of children of this shape.
	 */
	public int getChildrenCount() {
		if (children == null)
			return 0;
		return children.size();
	}

	/**
	 * Returns the ith child of this shape.
	 * 
	 * @param i
	 *            The index
	 * @return the ith child of this shape.
	 */
	public CShape getChild(int i) {
		if (children == null)
			return null;
		else if (i >= children.size())
			return null;
		else
			return (CShape) children.get(i);
	}

	/**
	 * Returns the index of <code>child</code>.
	 * 
	 * @param child
	 *            The child to find.
	 * @return the index of <code>child</code>, -1 if not found.
	 */
	public int indexOf(CShape child) {
		for (int i = 0; i < getChildrenCount(); i++) {
			if (getChild(i) == child)
				return i;
		}
		return -1;
	}

	/**
	 * Removes the ith child of this shape and returns it.
	 * 
	 * @param i
	 *            The index of the child to remove.
	 * @return the previous ith child of this shape.
	 */
	public CShape removeChild(int i) {
		CShape child = getChild(i);
		if (child != null) {
			removeChild(child);
			return child;
		}
		return null;
	}

	/**
	 * Removes all the children of this shape and returns them.
	 * 
	 * @return the previous children of this shape.
	 */
	public LinkedList<CShape> removeAllChildren() {
		if(children == null) return null;
		LinkedList<CShape> exChildren = new LinkedList<CShape>();
		for (int i = getChildrenCount() - 1; i >= 0; i--) {
			exChildren.add(removeChild(i));
		}
		return exChildren;
	}

	/**
	 * Sets the parent of this shape (only if the parent and this ahape are in
	 * the same canvas). If this shape has a parent, it is detached from this
	 * parent before being attached to the new one.
	 * 
	 * @param p
	 *            The new parent, or null if the shape is to have no parent.
	 * @return this shape.
	 */
	public CElement setParent(CShape p) {

		// detach from our parent if any
		if (parent != null) {
			parent.children.remove(this);
		}

		// attach to new parent
		parent = p;
		if (parent != null) {
			if (parent.children == null)
				parent.children = new LinkedList<CShape>();
			parent.children.add(this);
		}

		// invalidate shape since the reparenting is likely to change the
		// shape's transform
		changedTransform();

		return this;
	}

	/**
	 * Adds a child to this shape. 
	 * 
	 * @param c
	 *            The child shape
	 * @return this shape.
	 */
	public CShape addChild(CShape c) {
		c.setParent(this);
		return this;
	}

	/**
	 * Removes a child from this shape. This sets the child's parent to null.
	 * 
	 * @param s
	 *            The child shape to remove
	 * @throws ImpossibleOperationOnDifferentCanvasException
	 * @throws ImpossibleOperationOnDifferentCanvasException
	 * @return this shape.
	 */
	public CShape removeChild(CShape s) {
		if (s.getParent() == this)
			s.setParent(null);
		return this;
	}

	// ______________ TRANSFORMATIONS __________________
	// _________________________________________________

	/**
	 * Sets the transformation of this shape to identity.
	 * 
	 * @return This shape.
	 */
	public CElement setTransformToIdentity() {
		tx = 0;
		ty = 0;
		sx = 1;
		sy = 1;
		theta = 0;
		changedTransform();
		return this;
	}

	/**
	 * Returns the affine transformation that is applied to the shape. If the
	 * shape has a parent, the transformation is relative to the parent
	 * transform otherwise it is relative to the canvas's coordinate system.
	 * 
	 * @return the shape's affine transform.
	 * @see fr.lri.swingstates.canvas.CShape#getAbsTransform()
	 */
	public AffineTransform getTransform() {
		return transform;
	}

	/**
	 * Returns the absolute affine transformation that is applied to the shape.
	 * The transformation is relative to the canvas's coordinate system.
	 * 
	 * @return the shape's affine transform.
	 */
	public AffineTransform getAbsTransform() {
		return absTransform;
	}

	boolean isVisible() {
		if (canvas == null)
			return false;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D topleft = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		getAbsTransform().transform(topleft, topleft);
		Point2D bottomright = new Point2D.Double(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(bottomright, bottomright);
		double minX = Math.min(topleft.getX(), bottomright.getX());
		double maxX = Math.max(topleft.getX(), bottomright.getX());
		double minY = Math.min(topleft.getY(), bottomright.getY());
		double maxY = Math.max(topleft.getY(), bottomright.getY());
		// top
		boolean out = maxY < 0;
		// bottom
		out = out || (minY > canvas.getSize().height);
		// left
		out = out || (maxX < 0);
		// right
		out = out || (minX > canvas.getSize().width);
		boolean tooSmall = bounds.getWidth() < 1 && bounds.getHeight() < 1;
		return !(out || tooSmall);
	}

	void computeAbsTransform() {
		CShape s = parent;
		absTransform = (AffineTransform) (getTransform().clone());
		while (s != null) {
			absTransform.preConcatenate(s.transform);
			s = s.parent;
		}
	}

	// Computes the current transformation of this shape
	void computeTransform() {
		// dx, dy is the point of the shape that is invariant by rotation and
		// scale
		Rectangle2D bounds = shape.getBounds2D();
		double dx = bounds.getX() + bounds.getWidth() * rx;
		double dy = bounds.getY() + bounds.getHeight() * ry;

		transform.setToIdentity();
		transform.translate(tx + dx, ty + dy);
		transform.rotate(theta);
		transform.scale(sx, sy);
		transform.translate(-dx, -dy);
		computeAbsTransform();
	}

	// set absShape to null in order to signal that both transform and absShape
	// are invalid
	void changedTransform() {
		computeTransform();
		if (children != null)
			for (Iterator<CShape> i = children.iterator(); i.hasNext();)
				((CShape) (i.next())).changedTransform();
		repaint();
	}

	// === reference point

	/**
	 * Returns the x coordinate of this shape's reference point.
	 * 
	 * @return the x coordinate of this shape's reference point.
	 */
	public double getReferenceX() {
		return rx;
	}

	/**
	 * Returns the y coordinate of this shape's reference point.
	 * 
	 * @return the y coordinate of this shape's reference point.
	 */
	public double getReferenceY() {
		return ry;
	}

	/**
	 * Set this shape's reference point. The reference point is relative to the
	 * shape's bounding box. Hence, (0, 0) is the top-left corner, (1, 1) the
	 * bottom right corner, (0.5, 0.5) the center, etc.
	 * 
	 * @param x
	 *            The x coordinate of the new reference point.
	 * @param y
	 *            The y coordinate of the new reference point.
	 * @return this shape.
	 */
	public CElement setReferencePoint(double x, double y) {
		rx = x;
		ry = y;
		changedTransform();
		return this;
	}

	/**
	 * Set this shape's reference point. The reference point is relative to the
	 * shape's bounding box. Hence, (0, 0) is the top-left corner, (1, 1) the
	 * bottom right corner, (0.5, 0.5) the center, etc.
	 * 
	 * @param ptRef
	 *            The new reference point.
	 * @return this shape.
	 */
	public CElement setReferencePoint(Point2D ptRef) {
		return setReferencePoint(ptRef.getX(), ptRef.getY());
	}

	// === translation

	/**
	 * Translates this shape by dx, dy.
	 * 
	 * @param dx
	 *            The x offset
	 * @param dy
	 *            The y offset
	 * @return this shape.
	 */
	public CElement translateBy(double dx, double dy) {
		tx += dx;
		ty += dy;
		changedTransform();
		return this;
	}

	/**
	 * Translates this shape to x, y (the point of reference of this shape will
	 * be exactly at (x, y)).
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @return this shape.
	 */
	public CElement translateTo(double x, double y) {
		Rectangle2D bounds = shape.getBounds2D();
		AffineTransform xform = getAbsTransform();
		Point2D pt = new Point2D.Double();
//		try {
//			xform.inverseTransform(new Point2D.Double(x, y), pt);
//			translateBy(pt.getX() - (bounds.getX() + rx * bounds.getWidth()), pt.getY() - (bounds.getY() + ry * bounds.getHeight()));
			xform.transform(new Point2D.Double(bounds.getX() + rx * bounds.getWidth(), bounds.getY() + ry * bounds.getHeight()), pt);
			translateBy(x - pt.getX(), y - pt.getY());
			changedTransform();
//		} catch (NoninvertibleTransformException e) {
//			return this;
//		}
		return this;
	}

	/**
	 * Returns the x coordinate of this shape.
	 * 
	 * @return the x coordinate of this shape.
	 */
	public double getTranslateX() {
		return tx;
	}

	/**
	 * Returns the y coordinate of this shape.
	 * 
	 * @return the y coordinate of this shape.
	 */
	public double getTranslateY() {
		return ty;
	}

	// === rotation

	/**
	 * Rotates this shape by dt.
	 * 
	 * @param dt
	 *            The angle offset
	 * @return this shape.
	 */
	public CElement rotateBy(double dt) {
		theta += dt;
		changedTransform();
		return this;
	}

	/**
	 * Rotates this shape to angle t.
	 * 
	 * @param t
	 *            The angle
	 * @return this shape.
	 */
	public CElement rotateTo(double t) {
		// theta = t;
		rotateBy(t - theta);
		changedTransform();
		return this;
	}

	/**
	 * Returns this shape's rotation angle.
	 * 
	 * @return this shape's rotation angle
	 */
	public double getRotation() {
		return theta;
	}

	// === scale

	/**
	 * Scales this shape by x horizontally and y vertically.
	 * 
	 * @param x
	 *            The x scale factor
	 * @param y
	 *            The y scale factor
	 * @return this shape.
	 */
	public CElement scaleBy(double x, double y) {
		sx *= x;
		sy *= y;
		changedTransform();
		return this;
	}

	/**
	 * Sets the scale of this shape to x horizontally and y vertically.
	 * 
	 * @param x
	 *            The x scale
	 * @param y
	 *            The y scale
	 * @return this shape.
	 */
	public CElement scaleTo(double x, double y) {
		sx = x;
		sy = y;
		changedTransform();
		return this;
	}

	/**
	 * Scales this shape by s horizontally and vertically.
	 * 
	 * @param ds
	 *            The scale factor
	 * @return this shape.
	 */
	public CElement scaleBy(double ds) {
		sx *= ds;
		sy *= ds;
		changedTransform();
		return this;
	}

	/**
	 * Sets this shape's scale to s horizontally and vertically.
	 * 
	 * @param s
	 *            The scale value
	 * @return this shape.
	 */
	public CElement scaleTo(double s) {
		sx = s;
		sy = s;
		changedTransform();
		return this;
	}

	/**
	 * Returns the x scale value.
	 * 
	 * @return the x scale value.
	 */
	public double getScaleX() {
		return sx;
	}

	/**
	 * Returns the y scale value.
	 * 
	 * @return the y scale value.
	 */
	public double getScaleY() {
		return sy;
	}

	/**
	 * Converts a point relative to the shape into a point relative to the
	 * canvas.
	 * 
	 * @param p -
	 *            The point to be converted
	 * @return the converted point
	 */
	public Point2D shapeToCanvas(Point2D p) {
		AffineTransform xf = getAbsTransform();
		Point2D q = new Point2D.Double();
		xf.transform(p, q);
		return q;
	}

	/**
	 * Converts a point relative to the canvas into a point relative to the
	 * bounding box of this shape.
	 * 
	 * @param p
	 *            The point to be converted
	 * @return the converted point
	 */
	public Point2D canvasToShape(Point2D p) {
		Point2D ptDst = new Point2D.Double();
		try {
			absTransform.inverseTransform(p, ptDst);
		} catch (NoninvertibleTransformException e) {
			return p; // oh well, what can we do?
		}
		ptDst.setLocation(ptDst.getX() - shape.getBounds2D().getMinX(), ptDst.getY() - shape.getBounds2D().getMinY());
		return ptDst;
	}

	// ____________________________________ BOUNDING BOX
	// _________________________________________________

	/**
	 * @return the rectangle that is the bounding box of this shape.
	 */
	public CRectangle getBoundingBox() {
		double minX, maxX, minY, maxY;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		minX = txPt.getX();
		maxX = minX;
		minY = txPt.getY();
		maxY = minY;
		pt.setLocation(bounds.getMinX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		maxX = Math.max(maxX, txPt.getX());
		minY = Math.min(minY, txPt.getY());
		maxY = Math.max(maxY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMinY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		maxX = Math.max(maxX, txPt.getX());
		minY = Math.min(minY, txPt.getY());
		maxY = Math.max(maxY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		maxX = Math.max(maxX, txPt.getX());
		minY = Math.min(minY, txPt.getY());
		maxY = Math.max(maxY, txPt.getY());
		return new CRectangle(minX, minY, Math.abs(maxX - minX), Math.abs(maxY - minY));
	}

	/**
	 * @return the overall height of the bounding box of this shape in double
	 *         precision.
	 */
	public double getHeight() {
		double minY, maxY;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		minY = txPt.getY();
		maxY = minY;
		pt.setLocation(bounds.getMinX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minY = Math.min(minY, txPt.getY());
		maxY = Math.max(maxY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMinY());
		getAbsTransform().transform(pt, txPt);
		minY = Math.min(minY, txPt.getY());
		maxY = Math.max(maxY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minY = Math.min(minY, txPt.getY());
		maxY = Math.max(maxY, txPt.getY());
		return Math.abs(maxY - minY);
	}

	/**
	 * @return the overall width of the bounding box of this shape in double
	 *         precision.
	 */
	public double getWidth() {
		double minX, maxX;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		minX = txPt.getX();
		maxX = minX;
		pt.setLocation(bounds.getMinX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		maxX = Math.max(maxX, txPt.getX());
		pt.setLocation(bounds.getMaxX(), bounds.getMinY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		maxX = Math.max(maxX, txPt.getX());
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		maxX = Math.max(maxX, txPt.getX());
		return Math.abs(maxX - minX);
	}

	/**
	 * @return the X coordinate of the upper left corner of the bounding box of
	 *         this shape in double precision.
	 */
	public double getMinX() {
		double minX;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		minX = txPt.getX();
		pt.setLocation(bounds.getMinX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		pt.setLocation(bounds.getMaxX(), bounds.getMinY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minX = Math.min(minX, txPt.getX());
		return minX;
	}

	/**
	 * @return the Y coordinate of the upper left corner of the bounding box of
	 *         this shape in double precision.
	 */
	public double getMinY() {
		double minY;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		minY = txPt.getY();
		pt.setLocation(bounds.getMinX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minY = Math.min(minY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMinY());
		getAbsTransform().transform(pt, txPt);
		minY = Math.min(minY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		minY = Math.min(minY, txPt.getY());
		return minY;
	}

	/**
	 * @return the X coordinate of the lower right corner of the bounding box of
	 *         this shape in double precision.
	 */
	public double getMaxX() {
		double maxX;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		maxX = txPt.getX();
		pt.setLocation(bounds.getMinX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		maxX = Math.max(maxX, txPt.getX());
		pt.setLocation(bounds.getMaxX(), bounds.getMinY());
		getAbsTransform().transform(pt, txPt);
		maxX = Math.max(maxX, txPt.getX());
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		maxX = Math.max(maxX, txPt.getX());
		return maxX;
	}

	/**
	 * @return the Y coordinate of the lower right corner of the bounding box of
	 *         this shape in double precision.
	 */
	public double getMaxY() {
		double maxY;
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		maxY = txPt.getY();
		pt.setLocation(bounds.getMinX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		maxY = Math.max(maxY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMinY());
		getAbsTransform().transform(pt, txPt);
		maxY = Math.max(maxY, txPt.getY());
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		maxY = Math.max(maxY, txPt.getY());
		return maxY;
	}

	/**
	 * @return the X coordinate of the lower right corner of the bounding box of
	 *         this shape in double precision.
	 */
	public double getCenterX() {
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		double maxX = txPt.getX();
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		return (maxX + txPt.getX()) / 2;
	}

	/**
	 * @return the Y coordinate of the lower right corner of the bounding box of
	 *         this shape in double precision.
	 */
	public double getCenterY() {
		Rectangle2D bounds = getShape().getBounds2D();
		Point2D pt = new Point2D.Double(bounds.getMinX(), bounds.getMinY());
		Point2D txPt = new Point2D.Double();
		getAbsTransform().transform(pt, txPt);
		double maxY = txPt.getY();
		pt.setLocation(bounds.getMaxX(), bounds.getMaxY());
		getAbsTransform().transform(pt, txPt);
		return (maxY + txPt.getY()) / 2;
	}

	// ____________________________________ PICKING
	// _________________________________________________

	/**
	 * Returns true if the shape can be picked. Shapes that are not pickable are
	 * ignored by the canvas picking methods (<code>pick, pickAll</code>)
	 * and by the state machines.
	 * 
	 * @return true if the shape can be picked.
	 */
	public boolean isPickable() {
		return pickable;
	}

	/**
	 * Specifies whether this shape can be picked. Shapes that are not pickable
	 * are ignored by the canvas picking methods (<code>pick, pickAll</code>)
	 * and by the state machines.
	 * 
	 * @param pick
	 *            True if this shape can be picked, false otherwise.
	 * @return this shape.
	 */
	public CElement setPickable(boolean pick) {
		pickable = pick;
		return this;
	}

	/**
	 * Tests whether a point (x, y) is inside this shape.
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @see java.awt.Shape#contains(double, double)
	 * @return this shape if the point (x, y) is inside, null otherwise.
	 */
	public CShape contains(double x, double y) {
		return contains(new Point2D.Double(x, y));
	}

	/**
	 * Tests whether a point p is inside this shape.
	 * 
	 * @param p
	 *            The point
	 * @see java.awt.Shape#contains(Point2D)
	 * @return this shape if the point p is inside, null otherwise.
	 */
	public CShape contains(Point2D p) {
		if (canvas == null || (Graphics2D) canvas.getGraphics() == null)
			return null;
		Point2D ptDst = new Point2D.Double();
		try {
			absTransform.inverseTransform(p, ptDst);
			if ((filled && shape.contains(ptDst)) 
					|| (!filled && outlined && stroke.createStrokedShape(shape).contains(ptDst)))
				if(clip == null)
					return this;
				else
					return clip.contains(p) != null ? this : null;
		} catch (NoninvertibleTransformException e) {
			return null;
		}
		return null;
	}

	/**
	 * Tests whether a point p is inside this shape.
	 * 
	 * @param p
	 *            The point
	 * @see java.awt.Shape#contains(Point2D)
	 * @return this shape if the point p is inside, null otherwise.
	 */
	CShape pick(Point2D p, int tolerance) {
		if (canvas == null || (Graphics2D) canvas.getGraphics() == null)
			return null;
		Point2D ptDst = new Point2D.Double();
		try {
			absTransform.inverseTransform(p, ptDst);
			pickingRectangle.setBounds((int) ptDst.getX() - tolerance / 2, (int) ptDst.getY() - tolerance / 2, tolerance, tolerance);
			if ((filled && ((Graphics2D) canvas.getGraphics()).hit(pickingRectangle, shape, false)) 
					|| (outlined && ((Graphics2D) canvas.getGraphics()).hit(pickingRectangle, shape, true)))
				if(clip == null)
					return this;
				else
					return clip.contains(p) != null ? this : null;
		} catch (NoninvertibleTransformException e) {
			return null;
		}
		return null;
	}

	/**
	 * Tests whether a rectangle R is inside this shape.
	 * 
	 * @param x
	 *            The x coordinate of the upper left corner of R
	 * @param y
	 *            The y coordinate of the upper left corner of R
	 * @param w
	 *            The width of R
	 * @param h
	 *            The height of R
	 * @see java.awt.Shape#contains(double, double, double, double)
	 * @return this shape if the rectangle is inside, null otherwise.
	 */
	public CShape contains(double x, double y, double w, double h) {
		return contains(new Rectangle((int) x, (int) y, (int) w, (int) h));
	}

	/**
	 * Tests whether a rectangle R is inside this shape.
	 * 
	 * @param r
	 *            The rectangle
	 * @see java.awt.Shape#contains(Rectangle2D)
	 * @return this shape if the rectangle is inside this shape, null otherwise.
	 */
	public CShape contains(Rectangle r) {
		if (contains(new Point2D.Double(r.getMinX(), r.getMinY())) != null 
				&& contains(new Point2D.Double(r.getMaxX(), r.getMinY())) != null
				&& contains(new Point2D.Double(r.getMinX(), r.getMaxY())) != null)
			return contains(new Point2D.Double(r.getMaxX(), r.getMaxY()));
		return null;
	}

	/**
	 * Tests whether a point p is on this shape's outline.
	 * 
	 * @param p
	 *            The point
	 * @return this shape if the point p is on the outline, null otherwise.
	 */
	public CShape isOnOutline(Point2D p) {
		Point2D ptDst = new Point2D.Double();
		try {
			absTransform.inverseTransform(p, ptDst);
			pickingRectangle.setBounds((int) ptDst.getX() - 1, (int) ptDst.getY() - 1, 2, 2);
			if (((Graphics2D) canvas.getGraphics()).hit(pickingRectangle, shape, true))
				return this;
		} catch (NoninvertibleTransformException e) {
			return null;
		}
		return null;
	}

	/**
	 * Tests whether a <code>CShape</code> s intersects this shape.
	 * 
	 * @param s
	 *            The shape
	 * @return this shape if s intersects it, null otherwise.
	 */
	public CShape intersects(CShape s) {
		Area areaS = new Area(s.getAbsShape());
		areaS.intersect(new Area(getAbsShape()));
		if (areaS.isEmpty())
			return null;
		return this;
	}

	protected Shape getAbsShape() {
		return getAbsTransform().createTransformedShape(shape);
	}
	
	/**
	 * This method can be useful before changing the parent of a shape while keeping 
	 * its transformation that can be inherited from its current parent.
	 * This method:
	 * <ul>
	 * <li>sets the underlying Java2D shape to its transformed shape,</li>
	 * <li>resets the transformation to identity, i.e.,:</li>
	 * 		<ul>
	 * 		<li>rotation to angle 0, translation to vector (0, 0),</li>
	 * 		<li>scale to vector (1,1),</li>
	 *      <li>reference point to (0.5, 0.5).</li>
	 *      </ul>
	 * <li>removes parent and all children.</li>
	 * </ul>
	 * 
	 * @return this shape.
	 */
	public CShape fixReferenceShapeToCurrent() {
		shape = getAbsTransform().createTransformedShape(shape);
		rx = 0.5;
		ry = 0.5;
		tx = 0;
		ty = 0;
		theta = 0;
		sx = 1;
		sy = 1;
		transform = new AffineTransform();
		absTransform = new AffineTransform();
		if(parent != null)
			parent.removeChild(this);
		removeAllChildren();
		return this;
	}

	/**
	 * Computes the shape corresponding to the intersection of this shape with
	 * another given shape.
	 * 
	 * @param s
	 *            The shape
	 * @return the intersection as a <code>CPolyLine</code> if s intersects
	 *         it, null otherwise.
	 */
	public CPolyLine getIntersection(CShape s) {
		Area areaS = new Area(s.getAbsShape());
		areaS.intersect(new Area(getAbsShape()));
		if (areaS.isEmpty())
			return null;
		GeneralPath gp = new GeneralPath();
		gp.append(areaS.getPathIterator(null), true);
		CPolyLine intersection = new CPolyLine();
		intersection.setShape(gp);
		return intersection;
	}

	/**
	 * Computes the shape corresponding to the subtraction of this shape by
	 * another given shape.
	 * 
	 * @param s
	 *            The shape
	 * @return the subtraction shape as a <code>CPolyLine</code> if s
	 *         intersects it, null otherwise.
	 */
	public CPolyLine getSubtraction(CShape s) {
		Area areaS = new Area(getAbsShape());
		areaS.subtract(new Area(s.getAbsShape()));
		if (areaS.isEmpty())
			return null;
		GeneralPath gp = new GeneralPath();
		gp.append(areaS.getPathIterator(null), true);
		CPolyLine intersection = new CPolyLine();
		intersection.setShape(gp);
		return intersection;
	}

	// ____________________________________ ORDER
	// _________________________________________________

	/**
	 * Tests if this shape is above another shape in the display list.
	 * 
	 * @param before
	 *            The shape.
	 * @return true if this shape is above the shape <code>before</code>
	 *         (false if this shape or the the shape <code>before</code> are
	 *         not attached to a canvas).
	 */
	public boolean isAbove(CShape before) {
		if (canvas == null)
			return false;
		boolean hasSeenMe = false;
		boolean res = false;
		synchronized(canvas.displayOrder) {
			for (Iterator<CShape> i = canvas.displayOrder.iterator(); i.hasNext();) {
				CShape shape = i.next();
				if (shape == before) {
					res = !hasSeenMe;
					break;
				} else {
					if (shape == this)
						hasSeenMe = true;
				}
			}
		}
		return res;
	}

	/**
	 * Moves this shape in the canvas's display list so that it is right above a
	 * the top most shape contained in a given CElement.
	 * 
	 * @param before
	 *            The element that must be right below this shape in the display
	 *            list
	 * @return this shape.
	 */
	public CElement above(CElement before) {
		if (canvas == null)
			return this;
		if(before instanceof Canvas) {
			return aboveAll();
		}
		CShape foregroundShape = null;
		if(before instanceof CShape) {
			foregroundShape = (CShape)before;
		}
		else {
			CTag beforeTag = (CTag)before;
			List<CShape> displaylist = getCanvas().getDisplayList();
			synchronized(displaylist) {
				for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
					CShape element = iter.next();
					if(element.hasTag(beforeTag)) {
						foregroundShape = element;
					}
				}
			}
		}
		canvas.displayOrder.remove(this);
		int index = canvas.displayOrder.indexOf(foregroundShape);
		canvas.displayOrder.add(index+1, this);
		repaint();
		return this;
	}

	/**
	 * Moves this shape in the canvas's display list so that it is above all
	 * other shapes.
	 * 
	 * @return this shape.
	 */
	public CElement aboveAll() {
		if (canvas == null)
			return this;
		canvas.displayOrder.remove(this);
		canvas.displayOrder.add(this);
		repaint();
		return this;
	}

	/**
	 * Tests if this shape is before another shape in the display list.
	 * 
	 * @param after
	 *            The shape.
	 * @return true if this shape is before the shape <code>after</code>
	 *         (false if this shape or the the shape <code>after</code> are
	 *         not attached to a canvas).
	 */
	public boolean isBelow(CShape after) {
		if (canvas == null)
			return false;
		boolean hasSeenMe = false;
		boolean res = false;
		synchronized(canvas.displayOrder) {
			for (Iterator<CShape> i = canvas.displayOrder.iterator(); i.hasNext();) {
				CShape shape = i.next();
				if (shape == after) {
					res = hasSeenMe;
					break;
				} else {
					if (shape == this) {
						hasSeenMe = true;
					}
				}

			}
		}
		return res;
	}

	/**
	 * Moves this shape in the canvas's display list so that it is right below a
	 * the top least shape contained in a given CElement.
	 * 
	 * @param after
	 *            The element that must be right above this shape in the display
	 *            list
	 * @return this shape.
	 */
	public CElement below(CElement after) {
		if (canvas == null)
			return this;
		if(after instanceof Canvas) {
			return belowAll();
		}
		CShape backgroundShape = null;
		if(after instanceof CShape) {
			backgroundShape = (CShape)after;
		}
		else {
			CTag beforeTag = (CTag)after;
			List<CShape> displaylist = getCanvas().getDisplayList();
			synchronized(displaylist) {
				for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
					CShape element = iter.next();
					if(element.hasTag(beforeTag)) {
						backgroundShape = element;
						break;
					}
				}
			}
		}
		canvas.displayOrder.remove(this);
		int index = canvas.displayOrder.indexOf(backgroundShape);
		canvas.displayOrder.add(index, this);
		repaint();
		return this;

	}

	/**
	 * Moves this shape in the canvas's display list so that it is below all
	 * other shapes.
	 * 
	 * @return this shape.
	 */
	public CElement belowAll() {
		if (canvas == null)
			return this;
		canvas.displayOrder.remove(this);
		canvas.displayOrder.add(0, this);
		repaint();
		return this;
	}

	// ____________________________________ TAGS
	// _________________________________________________

	/**
	 * Adds a tag to this shape only if the tag is a
	 * <code>CExtensionalTag</code>, does anything otherwise.
	 * 
	 * @param t
	 *            The tag to be added
	 * @return this shape.
	 */
	public CElement addTag(CTag t) {
		if (t instanceof CExtensionalTag)
			return addTag((CExtensionalTag) t);
		return this;
	}

	/**
	 * Adds a tag to this shape.
	 * 
	 * @param t
	 *            The tag to be added
	 * @return this shape.
	 */
	public CElement addTag(CExtensionalTag t) {
		if (canvas == null || t == null)
			return this;
		t.addTo(this);
		return this;
	}

	/**
	 * Adds a <code>CNamedTag</code> tag to this shape.
	 * 
	 * @param t
	 *            The name of the tag to be added
	 * @return this shape.
	 */
	public CElement addTag(String t) {
		if (canvas == null)
			return this;
		addTag(canvas.newTag(t));
		return this;
	}

	/**
	 * Tests whether this shape has a given tag.
	 * 
	 * @param t
	 *            The tag to be tested
	 * @return true if t tags this shape.
	 */
	public boolean hasTag(CTag t) {
		if (canvas == null || t == null)
			return false;
		t.reset();
		while (t.hasNext())
			if (t.nextShape() == this)
				return true;
		return false;
	}

	/**
	 * Tests whether this shape has a given <code>CNamedTag</code> tag.
	 * 
	 * @param t
	 *            The name of the tag to be tested
	 * @return true if t tags this shape.
	 */
	public boolean hasTag(String t) {
		if (canvas == null)
			return false;
		return hasTag(canvas.getTag(t));
	}

	/**
	 * Removes a tag from this shape only if the tag is a
	 * <code>CExtensionalTag</code>.
	 * 
	 * @param t
	 *            The tag to remove
	 * @return this shape.
	 */
	public CElement removeTag(CExtensionalTag t) {
		if (canvas == null || t == null)
			return this;
		t.removeFrom(this);
		return this;
	}

	/**
	 * Removes a tag from this shape.
	 * 
	 * @param t
	 *            The name of the <code>CNamedTag</code> tag to remove
	 * @return this shape.
	 */
	public CElement removeTag(String t) {
		if (canvas == null)
			return this;
		removeTag(canvas.getTag(t));
		return this;
	}

	// ____________________________________ COPYING AND GHOSTING
	// _________________________________________________

	private static Shape cloneShape(Shape s) {
		// We must do the following heavy code because Polygon does not
		// implement Cloneable...
		if (s instanceof Area)
			return (Shape) ((Area) s).clone();
		else if (s instanceof CubicCurve2D)
			return (Shape) ((CubicCurve2D) s).clone();
		else if (s instanceof GeneralPath)
			return (Shape) ((GeneralPath) s).clone();
		else if (s instanceof Line2D)
			return (Shape) ((Line2D) s).clone();
		else if (s instanceof QuadCurve2D)
			return (Shape) ((QuadCurve2D) s).clone();
		else if (s instanceof Ellipse2D)
			return (Shape) ((Ellipse2D) s).clone();
		else if (s instanceof Rectangle2D)
			return (Shape) ((Rectangle2D) s).clone();
		else if (s instanceof Rectangle)
			return (Shape) ((Rectangle) s).clone();
		else if (s instanceof RectangularShape)
			return (Shape) ((RectangularShape) s).clone();
		else
			return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object clone() throws CloneNotSupportedException {
		if (shape != null && shape instanceof Polygon)
			throw new CloneNotSupportedException("Cannot clone the shape");
		super.clone();
		return duplicate();
	}

	/**
	 * Copies this shape into a destination shape. The copy differs from the
	 * original as follows: If this shape is in a canvas, the copy is added to
	 * that canvas, on top all other objects; The copy has no children, i.e. it
	 * is not the parent of any shape, even if the original has a parent.
	 * 
	 * @param sms
	 *            The destination shape
	 * @return this shape
	 */
	public CShape copyTo(CShape sms) {
		if (canvas != null)
			canvas.addShape(sms);
		else
			sms.canvas = null;
		if (shape != null)
			sms.shape = cloneShape(shape);
		else {
			sms.shape = new Line2D.Double(-1, -1, 1, 1);
		}
		sms.pickable = pickable;
		sms.drawable = drawable;
		sms.filled = filled;
		sms.fillPaint = fillPaint;
		sms.outlined = outlined;
		sms.outlinePaint = outlinePaint;
		sms.stroke = stroke;
		sms.transparencyFill = transparencyFill;
		if (renderingHints != null)
			sms.renderingHints = (RenderingHints) renderingHints.clone();
		sms.clip = clip;
		sms.rx = rx;
		sms.ry = ry;
		sms.tx = tx;
		sms.ty = ty;
		sms.theta = theta;
		sms.sx = sx;
		sms.sy = sy;
		sms.transform = new AffineTransform();
		sms.transform.concatenate(transform);
		if (parent != null)
			sms.setParent(parent);
		else
			sms.parent = null;
		sms.children = null;
		sms.ghost = null;
		sms.changedTransform();
		return this;
	}

	/**
	 * Creates a new copy of this shape and returns it.
	 * The tags of this shape are not added to the copy.
	 * 
	 * @return the copy.
	 * @see CShape#duplicateWithTags()
	 */
	public CShape duplicate() {
		CShape sms = new CShape();
		copyTo(sms);
		return sms;
	}
	
	/**
	 * Creates a new copy of this shape and returns it.
	 * The tags of this shape are added to the copy.
	 * 
	 * @return the copy.
	 * @see CShape#duplicate()
	 */
	public CShape duplicateWithTags() {
		CShape sms = duplicate();
		if(getCanvas() != null) {
			for(Iterator<CTag> iterator = getCanvas().getAllTags().iterator(); iterator.hasNext(); ) {
				CTag next = iterator.next();
				if(next instanceof CExtensionalTag && next.tagsShape(this))
					sms.addTag((CExtensionalTag)next);
			}
		}
		return sms;
	}

	// -------- Narrowing methods --------

	/**
	 * @return this shape if it is a rectangle, null otherwise
	 */
	public CRectangle asRectangle() {
		if (this instanceof CRectangle)
			return (CRectangle) this;
		return null;
	}

	/**
	 * @return this shape if it is an ellipse, null otherwise
	 */
	public CEllipse asEllipse() {
		if (this instanceof CEllipse)
			return (CEllipse) this;
		return null;
	}

	/**
	 * @return this shape if it is a polyline, null otherwise
	 */
	public CPolyLine asPolyLine() {
		if (this instanceof CPolyLine)
			return (CPolyLine) this;
		return null;
	}

	/**
	 * @return this shape if it is a rectangularShape, null otherwise
	 */
	public CRectangularShape asRectangularShape() {
		if (this instanceof CRectangularShape)
			return (CRectangularShape) this;
		return null;
	}

	/**
	 * @return this shape if it is a text, null otherwise
	 */
	public CText asText() {
		if (this instanceof CText)
			return (CText) this;
		return null;
	}

	/**
	 * @return this shape if it is an image, null otherwise
	 */
	public CImage asImage() {
		if (this instanceof CImage)
			return (CImage) this;
		return null;
	}

	// -------- Ghost --------

	/**
	 * Builds the 'ghost' of this shape. The 'ghost' of a shape is a copy of the
	 * shape with a greater transparencyFill.
	 */
	public void addGhost() {
		ghost = duplicate();
		ghost.setDrawable(true);
		ghost.setTransparencyFill(0.15f);
		ghost.changedTransform();
	}

	/**
	 * Builds the 'ghost' of this shape if it does not already exists. The
	 * 'ghost' of a shape is a copy of the shape with a greater
	 * transparencyFill.
	 * 
	 * @return the 'ghost' of this shape.
	 */
	public CShape getGhost() {
		if (ghost == null)
			addGhost();
		return ghost;
	}

	/**
	 * Removes the 'ghost' of this shape from the canvas.
	 */
	public void removeGhost() {
		if (canvas != null)
			canvas.removeShape(ghost);
		ghost = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void attachSM(CStateMachine sm, boolean reset) {
		sm.attachTo(this, reset);
	}

	/**
	 * {@inheritDoc}
	 */
	public void detachSM(CStateMachine sm) {
		sm.detach(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public LinkedList<CShape> getAntialiasedShapes() {
		LinkedList<CShape> res = new LinkedList<CShape>();
		if (isAntialiased())
			res.add(this);
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public LinkedList<CShape> getFilledShapes() {
		LinkedList<CShape> res = new LinkedList<CShape>();
		if (filled)
			res.add(this);
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public LinkedList<CShape> getOutlinedShapes() {
		LinkedList<CShape> res = new LinkedList<CShape>();
		if (outlined)
			res.add(this);
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstHavingTag(CTag t) {
		if (hasTag(t))
			return this;
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape firstShape() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstAntialiasedShape() {
		if (isAntialiased())
			return this;
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstFilledShape() {
		if (filled)
			return this;
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstOutlinedShape() {
		if (outlined)
			return this;
		return null;
	}

	/**
	 * Associates an animation to this shape.
	 * 
	 * @param anim
	 *            The animation to associate to this <code>CShape</code>.
	 * @return this <code>CShape</code> as a <code>CElement</code>
	 */
	public CElement animate(Animation anim) {
		anim.setAnimatedElement(this);
		anim.start();
		return this;
	}

}
