/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.AlphaComposite;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.sm.Tag;


/**
 * <p><code>CTag</code>s are labels associated to canvas' shapes. The <code>CTag</code> class has most of the methods of the <code>CShape</code> class.
 * Calling one of these <code>CTag</code>'s methods calls the <code>CShape</code> method that has the same name on every shape having this tag.</p>
 * <p>A tag is an object corresponding to a set of <code>CShape</code> shapes that can be browsed (i.e. a collection
 * and an iterator on it).
 * One example is a <code>CNamedTag</code> which is a set of shapes having a given label attached to them.
 * Another example is a <code>CHierarchyTag(CShape s)</code> which is a set composed of s and every shape whose s is an ancestor.</p>
 * <p>Some tags, <code>CExtensionalTag</code> tags, can be explicitely attached to and detached from a shape, 
 * i.e. a shape can be added to or removed from the tag's collection,
 * while others can not. For example, a <code>CNamedTag</code> can while a <code>CHierarchyTag</code> can not (it is added by
 * setting the parent of a <code>CShape</code>).</p>
 * <p>A tag can be attached to any number of shapes in the same canvas.
 * A shape in the canvas can have any number of tags attached to it.</p>
 * <p>Tags are used for two main purposes : grouping and interaction.
 * <ul>
 * <li>In order to create a group of objects in the canvas, one creates a new tag and attaches it to each shape in the group.
 * <li> Tags are also used in connection with state machines to describe interaction.
 * <ul> 
 * <li>For example, if some objects in the canvas can be moved but not others, one creates a tag called, say, movable
 * and attaches it to those shapes that can be moved.
 * Then, in the canvas' state machine, one can specify transitions such as <code>ClickOnTag(BUTTON1, movable)</code>
 * that will fire only when the user is clicking on an object with tag "movable".
 * <li>Transitions in state machines can also be fired on any object having a tag of a given class. One
 * can define a ColorTag class (with a color field) and attach many instances of this class to many shapes.
 * 
 * <pre>
 *  class ColorTag extends CTag {
 *  		Color color;
 *  		...
 *  }
 * </pre>
 * Then, in a state machine, one can use transitions like:
 * <pre>
 * 	Transition onAnyColor = new PressOnTag(ColoredTag.class, BUTTON1) {
 * 		public void action() {
 * 			System.out.println("This object has a "+((ColoredTag)getTag()).color+" tag");
 * 		}
 * 	};
 * </pre>
 * </ul>
 * </ul>
 * </p>
 * 
 * @see fr.lri.swingstates.sm.Tag
 * 
 * @author Caroline Appert
 */

public abstract class CTag extends Tag implements CElement {

	protected Iterator iterator = null;
	
	protected Canvas canvas;
	
	/**
	 * Builds a CTag that can tag the shapes displayed on the canvas c.
	 * @param c The canvas.
	 */
	public CTag(Canvas c){
		canvas = c;
		if(c!=null) c.registerTag(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Canvas getCanvas() {
		return canvas;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext() {
		if(iterator == null) return false;
		return iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final Object next() {
		return nextShape();
	}
	
	/**
	 * @return the next shape tagged by this tag as a <code>CShape</code>.
	 * @see java.util.Iterator#next()
	 */
	public CShape nextShape() {
		return (CShape) iterator.next();
	}
	
	/**
	 * Tests if the shape s is tagged by this tag.
	 * @param s The shape
	 * @return true if s is tagged by this tag, false otherwise.
	 */
	public boolean tagsShape(CShape s) {
		reset();
		while(hasNext())
			if(nextShape() == s) return true;
		return false;
	}
	
	/**
	 * Builds a tag corresponding to the set of shapes tagged by this tag and by t.
	 * @param t The second tag.
	 * @return a new CTag to access shapes tagged by this tag and t.
	 * @see fr.lri.swingstates.canvas.CAndTag
	 */
	public CTag and(CTag t) {
		return new CAndTag(this, t);
	}
	
	/**
	 * Builds a tag corresponding to the set of shapes tagged by this tag and by the <code>SMNamedTag</code> t.
	 * @param t The name of the second tag.
	 * @return a new CTag to access shapes tagged by this tag and the tag named by t.
	 * @see fr.lri.swingstates.canvas.CNamedTag
	 * @see fr.lri.swingstates.canvas.CAndTag
	 */
	public CTag and(String t) {
		CTag namedTag = canvas.getTag(t);
		if(t == null) return this;
		return new CAndTag(this, namedTag);
	}
	
	/**
	 * Builds a tag corresponding to the set of shapes tagged by this tag or by t.
	 * @param t The second tag.
	 * @return a new CTag to access shapes tagged by this tag or t.
	 * @see fr.lri.swingstates.canvas.COrTag
	 */
	public CTag or(CTag t) {
		return new COrTag(this, t);
	}
	
	/**
	 * Builds a tag corresponding to the set of shapes tagged by this tag or by the <code>SMNamedTag</code> t.
	 * @param t The name of the second tag.
	 * @return a new CTag to access shapes tagged by this tag or the tag named by t.
	 * @see fr.lri.swingstates.canvas.CNamedTag
	 * @see fr.lri.swingstates.canvas.COrTag
	 */
	public CTag or(String t) {
		CTag namedTag = canvas.getTag(t);
		if(t == null) return this;
		return new COrTag(this, namedTag);
	}
	
	/**
	 * Method called by <code>action()</code>. This method does nothing.
	 * Redefine it in a subclass to specify the effect of <code>CTag#action()</code>
	 * @param s The CShape on which applying specific treatments.
	 */
	public void action(CShape s) { }
	
	/**
	 * Calls <code>action(CShape s)</code> for every CShape s that has this tag.
	 * @return this tag
	 */ 
	public final CTag action(){
		reset(); 
		while(hasNext())
			action(nextShape());
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setShape(Shape sh){ 
		reset(); 
		while(hasNext())
			nextShape().setShape(sh);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setParent(CShape parent){ 
		reset(); 
		while(hasNext())
			(nextShape()).setParent(parent);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setStroke(Stroke str){ 
		reset(); 
		while(hasNext())
			(nextShape()).setStroke(str);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyFill(AlphaComposite transparencyFill){ 
		reset(); 
		while(hasNext())
			(nextShape()).setTransparencyFill(transparencyFill);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyFill(float alpha){ 
		reset(); 
		while(hasNext())
			(nextShape()).setTransparencyFill(alpha);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyOutline(AlphaComposite transparencyOutline){ 
		reset(); 
		while(hasNext())
			(nextShape()).setTransparencyOutline(transparencyOutline);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setTransparencyOutline(float alpha){ 
		reset(); 
		while(hasNext())
			(nextShape()).setTransparencyOutline(alpha);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setFillPaint(Paint fp){ 
		reset();
		while(hasNext())
			(nextShape()).setFillPaint(fp);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setOutlinePaint(Paint op){ 
		reset(); 
		while(hasNext())
			(nextShape()).setOutlinePaint(op);
		return this;
	}
	
	/**
	 * Calls <code>isAntialiased()</code> for every CShape that has this tag.
	 * @return true if every shape that has this tag is antialiased.
	 * @see fr.lri.swingstates.canvas.CShape#isAntialiased()
	 */
	public boolean isAntialiased(){ 
		reset(); 
		while(hasNext()) 
			if(!(nextShape()).isAntialiased()) return false;
		return true;
	}
	
	/**
	 * Returns the list of CShape that have this tag and that are antialiased.
	 * @return the list of CShape that have this tag and that are antialiased.
	 * @see fr.lri.swingstates.canvas.CShape#isAntialiased()
	 */
	public LinkedList<CShape> getAntialiasedShapes(){ 
		LinkedList<CShape> filledShapes = new LinkedList<CShape>();
		reset(); 
		while(hasNext()) {
			CShape s = nextShape();
			if(s.isAntialiased())
				filledShapes.add(s);
		}
		return filledShapes;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setRenderingHint(Key hintKey, Object hintValue) {
		reset(); 
		while(hasNext())
			(nextShape()).setRenderingHint(hintKey, hintValue);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setAntialiased(boolean a){ 
		reset(); 
		while(hasNext())
			(nextShape()).setAntialiased(a);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isFilled(){ 
		reset(); 
		while(hasNext())
			if(!(nextShape()).isFilled()) return false;
		return true;
	}
	
	/**
	 * Returns the list of CShape that have this tag and that are filled.
	 * @return the list of CShape that have this tag and that are filled.
	 * @see fr.lri.swingstates.canvas.CShape#isFilled()
	 */
	public LinkedList<CShape> getFilledShapes(){ 
		LinkedList<CShape> filledShapes = new LinkedList<CShape>();
		reset(); 
		while(hasNext()) {
			CShape s = nextShape();
			if(s.isFilled())
				filledShapes.add(s);
		}
		return filledShapes;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setFilled(boolean f){ 
		reset(); 
		while(hasNext())
			(nextShape()).setFilled(f);
		return this;
	}
	
	/**
	 * Returns true if every shape that has this tag is outlined.
	 * @return true if every shape that has this tag is outlined.
	 * @see fr.lri.swingstates.canvas.CShape#isOutlined()
	 */
	public boolean isOutlined(){ 
		reset(); 
		while(hasNext())
			if(!(nextShape()).isOutlined()) return false;
		return true;
	}
	
	/**
	 * @return the list of CShape that have this tag and that are outlined.
	 * @see fr.lri.swingstates.canvas.CShape#isOutlined()
	 */
	public LinkedList<CShape> getOutlinedShapes(){ 
		LinkedList<CShape> outlinedShapes = new LinkedList<CShape>();
		reset(); 
		while(hasNext()) {
			CShape s = nextShape();
			if(s.isOutlined())
				outlinedShapes.add(s);
		}
		return outlinedShapes;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setOutlined(boolean f){ 
		reset(); 
		while(hasNext())
			(nextShape()).setOutlined(f);
		return this;
	}

	/**
	 * Returns true if every shape that has this tag is drawable.
	 * @return true if every shape that has this tag is drawable.
	 * @see fr.lri.swingstates.canvas.CShape#isDrawable()
	 */
	public boolean isDrawable(){ 
		reset(); 
		while(hasNext())
			if(!(nextShape()).isDrawable()) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setDrawable(boolean f){ 
		reset(); 
		while(hasNext()) 
			nextShape().setDrawable(f);
		return this;
	}

	/**
	 * Returns true if every shape that has this tag is pickable.
	 * @return true if every shape that has this tag is pickable.
	 * @see fr.lri.swingstates.canvas.CShape#isPickable()
	 */
	public boolean isPickable(){ 
		reset(); 
		while(hasNext())
			if(!(nextShape()).isPickable()) return false;
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setPickable(boolean pick){ 
		reset(); 
		while(hasNext())
			(nextShape()).setPickable(pick);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement setReferencePoint(double x, double y){ 
		reset(); 
		while(hasNext())
			(nextShape()).setReferencePoint(x, y);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setTransformToIdentity() { 
		reset(); 
		while(hasNext())
			(nextShape()).setTransformToIdentity();
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement translateBy(double tx, double ty){ 
		reset(); 
		while(hasNext())
			(nextShape()).translateBy(tx, ty);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement translateTo(double tx, double ty){
		reset(); 
		while(hasNext())
			(nextShape()).translateTo(tx, ty);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement scaleBy(double sx, double sy) {
		reset(); 
		while(hasNext())
			(nextShape()).scaleBy(sx, sy);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement scaleBy(double s) { 
		reset(); 
		while(hasNext())
			(nextShape()).scaleBy(s);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement scaleTo(double sx, double sy) { 
		reset(); 
		while(hasNext())
			(nextShape()).scaleTo(sx, sy);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement scaleTo(double s) { 
		reset(); 
		while(hasNext())
			(nextShape()).scaleTo(s);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement rotateBy(double theta) { 
		reset(); 
		while(hasNext())
			(nextShape()).rotateBy(theta);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement rotateTo(double theta) { 
		reset(); 
		while(hasNext())
			(nextShape()).rotateTo(theta);
		return this;
	}
	
	/**
	 * Returns the topmost CShape containing the point (x, y).
	 * @param x The x-coordinate of the point to test.
	 * @param y The y-coordinate of the point to test.
	 * @return the topmost CShape containing the point (x, y), null if no CShape contains the point (x, y). 
	 * @see fr.lri.swingstates.canvas.CShape#contains(double, double)
	 */
	public CShape contains(double x, double y) { 
	    CShape res = null;
	    reset(); 
	    while(hasNext()) {
			CShape s = nextShape();
			if(s.contains(x, y) != null) {
				if(res == null) res = s;
				else if(s.isAbove(res)) res = s;
			}
		}
		return res;
	}
	
	/**
	 * Returns the topmost CShape containing the point p.
	 * @param p The point to test.
	 * @return the topmost CShape containing the point p, null if no CShape contains the point p. 
	 * @see fr.lri.swingstates.canvas.CShape#contains(Point2D)
	 */
	public CShape contains(Point2D p) { 
	    CShape res = null;
	    reset(); 
	    while(hasNext()) {
			CShape s = nextShape();
			if(s.contains(p) != null) {
				if(res == null) res = s;
				else if(s.isAbove(res)) res = s;
			}
		}
		return res;
	}
	
	/**
	 * Returns the topmost CShape containing a given rectangle.
	 * @param x The x-coordinate of the left corner of the rectangle to test.
	 * @param y The y-coordinate of the left corner of the rectangle to test.
	 * @param w The width of the rectangle to test.
	 * @param h The height of the rectangle to test.
	 * @return the topmost CShape containing the rectangle defined by the upper left corner (x, y), the width w and the height h, null if no CShape contains the rectangle. 
	 * @see fr.lri.swingstates.canvas.CShape#contains(double, double, double, double)
	 */
	public CShape contains(double x, double y, double w, double h) { 
	    CShape res = null;
	    reset(); 
	    while(hasNext()) {
			CShape s = nextShape();
			if(s.contains(x, y, w, h) != null) {
				if(res == null) res = s;
				else if(s.isAbove(res)) res = s;
			}
		}
		return res;
	}
	
	/**
	 * Returns the topmost CShape containing a given rectangle.
	 * @param r The rectangle to test.
	 * @return the topmost CShape containing the rectangle r, null if no CShape contains r. 
	 * @see fr.lri.swingstates.canvas.CShape#contains(Rectangle)
	 */
	public CShape contains(Rectangle r) { 
	    CShape res = null;
	    reset(); 
	    while(hasNext()) {
			CShape s = nextShape();
			if(s.contains(r) != null) {
				if(res == null) res = s;
				else if(s.isAbove(res)) res = s;
			}
		}
		return res;
	}
	
	/**
	 * Returns the topmost CShape whose outline contains the point p.
	 * @param p The point to test.
	 * @return the topmost CShape whose outline contains the point p, null if no CShape's outline contains p. 
	 * @see fr.lri.swingstates.canvas.CShape#isOnOutline(Point2D)
	 */
	public CShape isOnOutline(Point2D p) { 
	    CShape res = null;
	    reset(); 
	    while(hasNext()) {
			CShape s = nextShape();
			if(s.isOnOutline(p) != null) {
				if(res == null) res = s;
				else if(s.isAbove(res)) res = s;
			}
		}
		return res;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CShape intersects(CShape s) {
	    CShape res = null;
	    reset(); 
	    while(hasNext()) {
			CShape shape = nextShape();
			if(shape.intersects(s) != null) {
				if(res == null) res = shape;
				else if(shape.isAbove(res)) res = shape;
			}
		}
		return res;
	}
	
	/**
	 * Tests whether a <code>CShape</code> s intersects this <code>CElement</code>.
	 * @param s The shape
	 * @return the intersection as a <code>CPolyLine</code> if s intersects it, null otherwise.
	 * @see fr.lri.swingstates.canvas.CShape#getIntersection(CShape)
	 */
	public CPolyLine getIntersection(CShape s) {
		Area areaS = new Area(s.getAbsShape());
		Area areaSms = null;
		Area res = new Area();
		reset(); 
	    while(hasNext()) {
			CShape sms = nextShape();
			if(sms.isPickable()) {
				areaSms = new Area(sms.getAbsShape());
				areaSms.intersect(areaS);
				if(!areaSms.isEmpty()) res.add(areaSms);
			}
		}
		if(res.isEmpty()) return null;
		GeneralPath gp = new GeneralPath();
		gp.append(res.getPathIterator(null), false);
		CPolyLine intersection = new CPolyLine();
		intersection.setShape(gp);
		return intersection;
	}
	
	/**
	 * Returns the topmost CShape that has tag t.
	 * @param t The tag to test.
	 * @return the topmost CShape that has tag t, null if no CShape has tag t. 
	 * @see fr.lri.swingstates.canvas.CShape#hasTag(fr.lri.swingstates.canvas.CTag)
	 */
	public CShape getFirstHavingTag(CTag t) { 
		if(t==null) return null;
	    CShape res = null;
	    reset(); 
	    while(hasNext()) {
			CShape s = nextShape();
			if(s.hasTag(t)) {
				if(res == null) res = s;
				else if(res.isBelow(s)) res = s;
			}
		}
		return res;
	}
	
	/**
	 * Returns true if every shape with this tag also has tag t.
	 * @param t The name of the tag to test
	 * @return true if every shape with this tag also has tag t, false otherwise. 
	 * @see fr.lri.swingstates.canvas.CShape#hasTag(String)
	 */
	public boolean hasTag(String t) { 
		if(t==null) return true;
		reset(); 
		while(hasNext()) {
			CShape next = nextShape();
			if(!next.hasTag(t)) return false;
		}
		return true;
	}
	
	/**
	 * Returns true if every shape with this tag also has tag t.
	 * @param t the tag to test.
	 * @return true if every shape with this tag also has tag t, false otherwise. 
	 */
	public boolean hasTag(CTag t) { 
		if(t==null) return true;
		reset(); 
		while(hasNext()) 
			if(!((nextShape()).hasTag(t))) return false;
		return true;
	}

	/**
	 * Changes the display list order so that every CShape tagged by this tag is right above 
	 * the top most shape in another CElement. The relative display order between shapes tagged by this tag is unchanged.
	 * <b>WARNING</b>: If this tag also tags one of the elements contained in <code>before</code>, this method does nothing.
	 * @param before The shape that must be right below every shape contained in this <code>CElement</code> in the display list
	 * @return This <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#below(CElement)
	 */
	public CElement above(CElement before){ 
		if(before instanceof Canvas) {
			return aboveAll();
		}
		Vector<CShape> tagged = new Vector<CShape>();
		List<CShape> displaylist = getCanvas().getDisplayList();
		synchronized(displaylist) {
			for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
				CShape element = iter.next();
				if(element.hasTag(this)) {
					tagged.add(element);
				}
			}
		}
		CShape foregroundShape = null;
		if(before instanceof CShape) {
			foregroundShape = (CShape)before;
			if(foregroundShape.hasTag(this)) return this;
		}
		else {
			CTag beforeTag = (CTag)before;
			foregroundShape = beforeTag.getTopMostShape();
		}
		for (Iterator<CShape> iter = tagged.iterator(); iter.hasNext();) {
			CShape element = iter.next();
			element.above(foregroundShape);
			foregroundShape = element;
		}
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement aboveAll(){ 
		Vector<CShape> tagged = new Vector<CShape>();
		List<CShape> displaylist = getCanvas().getDisplayList();
		synchronized(displaylist) {
			for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
				CShape element = iter.next();
				if(element.hasTag(this)) {
					tagged.add(element);
				}
			}
		}
		for (Iterator<CShape> iter = tagged.iterator(); iter.hasNext();) {
			CShape element = iter.next();
			element.aboveAll();
		}
		return this;
	}
	
	/**
	 * @return 
	 * 		The shape tagged by this tag that has the highest index in the display list,
	 * 		null if this tag is not registered to a canvas.
	 */
	public CShape getTopMostShape() {
		if(getCanvas() == null) return null; 
		List<CShape> displaylist = getCanvas().getDisplayList();
		CShape topMost = null;
		synchronized(displaylist) {
			for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
				CShape element = iter.next();
				if(element.hasTag(this)) {
					topMost = element;
				}
			}
		}
		return topMost;
	}
	
	/**
	 * @return 
	 * 		The shape tagged by this tag that has the lowest index in the display list,
	 * 		null if this tag is not registered to a canvas.
	 */
	public CShape getTopLeastShape() {
		if(getCanvas() == null) return null; 
		List<CShape> displaylist = getCanvas().getDisplayList();
		CShape topMost = null;
		synchronized(displaylist) {
			for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
				CShape element = iter.next();
				if(element.hasTag(this)) {
					topMost = element;
					break;
				}
			}
		}
		return topMost;
	}
	
	/**
	 * Changes the display list order so that every CShape tagged by this tag is right below 
	 * the top least shape in another CElement. The relative display order between shapes tagged by this tag is unchanged.
	 * <b>WARNING</b>: If this tag also tags one of the elements contained in <code>after</code>, this method does nothing.
	 * @param after The shape that must be right above every shape contained in this <code>CElement</code> in the display list
	 * @return This tag
	 * @see fr.lri.swingstates.canvas.CShape#below(CElement)
	 */
	public CElement below(CElement after){ 
		if(after instanceof Canvas) {
			return belowAll();
		}
		Vector<CShape> tagged = new Vector<CShape>();
		List<CShape> displaylist = getCanvas().getDisplayList();
		synchronized(displaylist) {
			for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
				CShape element = iter.next();
				if(element.hasTag(this)) {
					tagged.add(element);
				}
			}
		}
		CShape backgroundShape = null;
		if(after instanceof CShape) {
			backgroundShape = (CShape)after;
			if(backgroundShape.hasTag(this)) return this;
		}
		else {
			CTag beforeTag = (CTag)after;
			backgroundShape = beforeTag.getTopLeastShape();
		}
		for (Iterator<CShape> iter = tagged.iterator(); iter.hasNext();) {
			CShape next = iter.next();
			next.below(backgroundShape);
		}
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement belowAll(){ 
		Vector<CShape> tagged = new Vector<CShape>();
		List<CShape> displaylist = getCanvas().getDisplayList();
		for (Iterator<CShape> iter = displaylist.iterator(); iter.hasNext();) {
			CShape element = iter.next();
			if(element.hasTag(this)) {
				tagged.add(element);
			}
		}
		for (int i = (tagged.size() - 1); i >= 0; i--) {
			CShape element = tagged.get(i);
			element.belowAll();
		}
	    return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setClip(CShape clip){ 
		reset(); 
		while(hasNext())
			(nextShape()).setClip(clip);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addGhost(){ 
		CShape s = null;
		reset(); 
		while(hasNext()) {
			s = nextShape();
			s.addGhost();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeGhost(){ 
		reset(); 
		while(hasNext())
			(nextShape()).removeGhost();
	}

	/**
	 * @return the rectangle that is the bounding box of the group of SMShapes that has this tag.
	 */
	public CRectangle getBoundingBox() {
		return new CRectangle(getMinX(), getMinY(), Math.abs(getMaxX()-getMinX()), Math.abs(getMaxY()-getMinY()));
	}
	
	/**
	 * @return the minimum x-coordinate of the rectangle that is the bounding box of the group of CShapes that has this tag.
	 */
	public double getMinX() {
		double minX = Double.MAX_VALUE;
		reset(); 
		while(hasNext())
			minX = Math.min(nextShape().getMinX(), minX);
		return minX;
	}
	
	/**
	 * @return the maximum x-coordinate of the rectangle that is the bounding box of the group of CShapes that has this tag.
	 */
	public double getMaxX() {
		double maxX = Double.MIN_VALUE;
		reset(); 
		while(hasNext())
			maxX = Math.max(nextShape().getMaxX(), maxX);
		return maxX;
	}
	
	/**
	 * @return center x-coordinate of the rectangle that is the bounding box of the group of CShapes that has this tag.
	 */
	public double getCenterX() {
		return (getMinX()+getMaxX())/2;
	}
	
	/**
	 * @return the minimum y-coordinate of the rectangle that is the bounding box of the group of CShapes that has this tag.
	 */
	public double getMinY() {
		double minY = Double.MAX_VALUE;
		reset(); 
		while(hasNext())
			minY = Math.min(nextShape().getMinY(), minY);
		return minY;
	}
	
	/**
	 * @return the maximum y-coordinate of the rectangle that is the bounding box of the group of CShapes that has this tag.
	 */
	public double getMaxY() {
		double maxY = Double.MIN_VALUE;
		reset(); 
		while(hasNext())
			maxY = Math.max(nextShape().getMaxY(), maxY);
		return maxY;
	}
	
	/**
	 * @return center y-coordinate of the rectangle that is the bounding box of the group of CShapes that has this tag.
	 */
	public double getCenterY() {
		return (getMinY()+getMaxY())/2;
	}
	
	/**
	 * Returns the first shape that has this tag.
	 * @return the first shape that has this tag, null if no shape has this tag.
	 */
	public CShape firstShape() {
		reset(); 
		if (hasNext()) return nextShape();
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstAntialiasedShape(){ 
		reset();
		CShape s = null;
		while(hasNext()) {
			s = nextShape();
			if(s.isAntialiased()) return s;
		}
		return s;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CShape getFirstFilledShape(){ 
		reset();
		CShape s = null;
		while(hasNext()) {
			s = nextShape();
			if(s.isFilled()) return s;
		}
		return s;
	}

	/**
	 * {@inheritDoc}
	 */
	public final CShape getFirstOutlinedShape(){ 
		reset();
		CShape s = null;
		while(hasNext()) {
			s = nextShape();
			if(s.isOutlined()) return s;
		}
		return s;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement addTag(CExtensionalTag t){ 
		if(t == null || t == this)
			return this;
		reset(); 
		while(hasNext())
			(nextShape()).addTag(t);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement addTag(String t){ 
		if(t == null)
			return this;
		reset(); 
		while(hasNext())
			(nextShape()).addTag(t);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement removeTag(CExtensionalTag t) { 
		if(t == null) return this;
		if(t == this) {
			reset(); 
			while(hasNext()) {
				t.removed(nextShape());
			}
			collection.clear();
		} else {
			reset(); 
			while(hasNext())
				(nextShape()).removeTag(t);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public CElement removeTag(String t) { 
		removeTag(canvas.getTag(t));
		return this;
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
	 * Calls <code>animate(Animation anim)</code> for every CShape tagged by this <code>CTag</code>.
	 * @param anim The animation to associate to each shape tagged by this <code>CTag</code>.
	 * @return this <code>CTag</code> as a <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#animate(Animation)
	 */
	public CElement animate(Animation anim) {
		reset();
		anim.setAnimatedElement(this);
		anim.start();
		return this;
	}

}
