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
import java.awt.geom.Point2D;
import java.util.LinkedList;

import fr.lri.swingstates.animations.Animation;


/**
 * 
 * A CElement is a graphical object: a <code>Canvas</code>, a <code>Tag</code> or a <code>CShape</code>.
 * It can be linked to a state machine.
 * 
 * @author Caroline Appert
 *
 */
public interface CElement {
	
	/**
	 * Attaches a state machine to this <code>CElement</code> to describe its behavior.
	 * @param sm The State machine that describes the behavior of this <code>CElement</code>. 
	 * @param reset True if the machine must be reset, false otherwise.
	 */
	void attachSM(CStateMachine sm, boolean reset);
	
	/**
	 * Detaches a state machine from this <code>CElement</code>.
	 * @param sm The State machine to detach. 
	 */
	void detachSM(CStateMachine sm);
	
	
	/**
	 * Returns the canvas in which this CElement is displayed.
	 * This method is required because the global handler that dispatches all the events to all the state machines attached
	 * to this CElement is the canvas in which this CElement is displayed.
	 * @return the canvas.
	 */
	Canvas getCanvas();
	
	/**
	 * Calls <code>setShape(Shape sh)</code> for every CShape contained in this <code>CElement</code>.
	 * @param sh The shape
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setShape(java.awt.Shape)
	 */
	CElement setShape(Shape sh);
	
	/**
	 * Calls <code>setParent(CShape parent)</code> for every CShape contained in this <code>CElement</code>.
	 * @param parent The parent shape
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setParent(CShape)
	 */
	CElement setParent(CShape parent);
	
	/**
	 * Calls <code>setStroke(Shape sh)</code> for every CShape contained in this <code>CElement</code>.
	 * @param str The stroke
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setStroke(java.awt.Stroke)
	 */
	CElement setStroke(Stroke str);
	
	/**
	 * Calls <code>setTransparencyFill(AlphaComposite transparencyFill)</code> for every CShape contained in this <code>CElement</code>.
	 * @param transparencyFill The transparency
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setTransparencyFill(AlphaComposite)
	 */
	CElement setTransparencyFill(AlphaComposite transparencyFill);
	
	/**
	 * Calls <code>setTransparencyFill(float alpha)</code> for every CShape contained in this <code>CElement</code>.
	 * @param alpha The transparency
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setTransparencyFill(float)
	 */
	CElement setTransparencyFill(float alpha);
	
	/**
	 * Calls <code>setTransparencyOutline(AlphaComposite transparencyOutline)</code> for every CShape contained in this <code>CElement</code>.
	 * @param transparencyOutline The transparency
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setTransparencyOutline(AlphaComposite)
	 */
	CElement setTransparencyOutline(AlphaComposite transparencyOutline);
	
	/**
	 * Calls <code>setTransparencyOutline(float alpha)</code> for every CShape contained in this <code>CElement</code>.
	 * @param alpha The transparency
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setTransparencyOutline(float)
	 */
	CElement setTransparencyOutline(float alpha);
	
	/**
	 * Calls <code>setFillPaint(Paint fp)</code> for every CShape contained in this <code>CElement</code>.
	 * @param fp The paint value
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setFillPaint(Paint)
	 */
	CElement setFillPaint(Paint fp);
	
	/**
	 * Calls <code>setOutlinePaint(Paint op)</code> for every CShape contained in this <code>CElement</code>.
	 * @param op The paint value
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setOutlinePaint(Paint)
	 */
	CElement setOutlinePaint(Paint op);
	
	
	/**
	 * Calls <code>isAntialiased()</code> for every CShape contained in this <code>CElement</code>.
	 * @return true if every shape contained in this <code>CElement</code> is antialiased.
	 * @see fr.lri.swingstates.canvas.CShape#isAntialiased()
	 */
	boolean isAntialiased();
	
	/**
	 * Returns the list of CShape that have this tag and that are antialiased.
	 * @return the list of CShape that have this tag and that are antialiased.
	 * @see fr.lri.swingstates.canvas.CShape#isAntialiased()
	 */
	LinkedList<CShape> getAntialiasedShapes();
	
	
	/**
	 * Calls <code>setAntialiased(boolean)</code> for every CShape contained in this <code>CElement</code>.
	 * @param a True to activate the antialiasing, false to deactivate it.
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setAntialiased(boolean)
	 */
	CElement setAntialiased(boolean a);
	
	/**
	 * Calls <code>setRenderingHint(hintKey, hintValue)</code> for every CShape contained in this <code>CElement</code>.
	 * See <code>java.awt.RenderingHints</code> for available hint keys and hint values.
	 * @param hintKey The hint key
	 * @param hintValue The hint value
	 * @return this <code>CElement</code>.
	 */
	CElement setRenderingHint(Key hintKey, Object hintValue);
	
	/**
	 * Calls <code>isFilled()</code> for every CShape contained in this <code>CElement</code>.
	 * @return true if every shape contained in this <code>CElement</code> is filled.
	 * @see fr.lri.swingstates.canvas.CShape#isFilled()
	 */
	boolean isFilled();
	
	/**
	 * Returns the list of CShape that have this tag and that are filled.
	 * @return the list of CShape that have this tag and that are filled.
	 * @see fr.lri.swingstates.canvas.CShape#isFilled()
	 */
	LinkedList<CShape> getFilledShapes();
	
	/**
	 * Calls <code>setFilled(boolean)</code> for every CShape contained in this <code>CElement</code>.
	 * @param f True if the shape must be filled, false otherwise.
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setFilled(boolean)
	 */
	CElement setFilled(boolean f);
	
	/**
	 * Returns true if every shape contained in this <code>CElement</code> is outlined.
	 * @return true if every shape contained in this <code>CElement</code> is outlined.
	 * @see fr.lri.swingstates.canvas.CShape#isOutlined()
	 */
	boolean isOutlined();
	
	/**
	 * Returns the list of CShape that have this tag and that are outlined.
	 * @return the list of CShape that have this tag and that are outlined.
	 * @see fr.lri.swingstates.canvas.CShape#isOutlined()
	 */
	LinkedList<CShape> getOutlinedShapes();
	
	/**
	 * Calls <code>setOutlined(boolean)</code> for every CShape contained in this <code>CElement</code>.
	 * @param f True if the shape must be outlined, false otherwise.
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setOutlined(boolean)
	 */
	CElement setOutlined(boolean f);
	
	/**
	 * Returns true if every shape contained in this <code>CElement</code> is drawable.
	 * @return true if every shape contained in this <code>CElement</code> is drawable.
	 * @see fr.lri.swingstates.canvas.CShape#isDrawable()
	 */
	boolean isDrawable();
	
	/**
	 * Calls <code>setDrawable(boolean)</code> for every CShape contained in this <code>CElement</code>.
	 * @param f True if the shape must be drawn, false otherwise.
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setDrawable(boolean)
	 */
	CElement setDrawable(boolean f);
	
	/**
	 * Returns true if every shape contained in this <code>CElement</code> is pickable.
	 * @return true if every shape contained in this <code>CElement</code> is pickable.
	 * @see fr.lri.swingstates.canvas.CShape#isPickable()
	 */
	boolean isPickable();
	
	/**
	 * Calls <code>setPickable(boolean)</code> for every CShape contained in this <code>CElement</code>.
	 * @param pick True if the shape must be pickable, false otherwise.
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setOutlined(boolean)
	 */
	CElement setPickable(boolean pick);
	
	/**
	 * Calls <code>setReferencePoint(double x, double y)</code> for every CShape contained in this <code>CElement</code>.
	 * @param x The x coordinate of the new reference point.
	 * @param y The y coordinate of the new reference point. 
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setReferencePoint(double, double)
	 */
	CElement setReferencePoint(double x, double y);
	
	/**
	 * Calls <code>setTransformToIdentity()</code> for every CShape contained in this <code>CElement</code>.
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setTransformToIdentity()
	 */
	CElement setTransformToIdentity();
	
	/**
	 * Calls <code>translateBy(double tx, double ty)</code> for every CShape contained in this <code>CElement</code>.
	 * @param dx The x offset
	 * @param dy The y offset
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#translateBy(double, double)
	 */
	CElement translateBy(double dx, double dy);
	
	/**
	 * Calls <code>translateTo(double tx, double ty)</code> every CShape contained in this <code>CElement</code>.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#translateTo(double, double)
	 */
	CElement translateTo(double x, double y);
	
	/**
	 * Calls <code>scale(double sx, double sy)</code> for every CShape contained in this <code>CElement</code>.
	 * @param x The x scale factor
	 * @param y The y scale factor
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#scaleBy(double, double)
	 */
	CElement scaleBy(double x, double y);
	
	/**
	 * Calls <code>scaleBy(double s)</code> for every CShape contained in this <code>CElement</code>.
	 * @param ds The scale factor
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#scaleBy(double)
	 */
	CElement scaleBy(double ds);
	
	/**
	 * Calls <code>scaleTo(double sx, double sy)</code> for every CShape contained in this <code>CElement</code>.
	 * @param x The x scale
	 * @param y The y scale
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#scaleTo(double, double)
	 */
	CElement scaleTo(double x, double y);
	
	/**
	 * Calls <code>scaleTo(double s)</code> for every CShape contained in this <code>CElement</code>.
	 * @param s The scale value
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#scaleTo(double)
	 */
	CElement scaleTo(double s);
	
	/**
	 * Calls <code>rotateBy(double theta)</code> for every CShape contained in this <code>CElement</code>.
	 * @param dt The angle offset
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#rotateBy(double)
	 */
	CElement rotateBy(double dt);
	
	/**
	 * Calls <code>rotateTo(double theta)</code> for every CShape contained in this <code>CElement</code>.
	 * @param t The angle
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#rotateTo(double)
	 */
	CElement rotateTo(double t);
	
	/**
	 * Returns the topmost CShape containing the point (x, y).
	 * @param x The x coordinate 
	 * @param y The y coordinate
	 * @return the topmost CShape containing the point (x, y), null if no CShape contains the point (x, y). 
	 * @see fr.lri.swingstates.canvas.CShape#contains(double, double)
	 */
	CShape contains(double x, double y);
	
	/**
	 * Returns the topmost CShape containing the point p.
	 * @param p The point.
	 * @return the topmost CShape containing the point p, null if no CShape contains the point p. 
	 * @see fr.lri.swingstates.canvas.CShape#contains(Point2D)
	 */
	CShape contains(Point2D p);
	
	/**
	 * Returns the topmost CShape containing a given rectangle R.
	 * @param x The x coordinate of the upper left corner of R 
	 * @param y The y coordinate of the upper left corner of R
	 * @param w The width of R
	 * @param h The height of R
	 * @return the topmost CShape containing the rectangle defined by the upper left corner (x, y), the width w and the height h, null if no CShape contains the rectangle. 
	 * @see fr.lri.swingstates.canvas.CShape#contains(double, double, double, double)
	 */
	CShape contains(double x, double y, double w, double h);
	
	/**
	 * Returns the topmost CShape containing a given rectangle.
	 * @param r The rectangle
	 * @return the topmost CShape containing the rectangle r, null if no CShape contains r. 
	 * @see fr.lri.swingstates.canvas.CShape#contains(Rectangle)
	 */
	CShape contains(Rectangle r);
	
	/**
	 * Returns the topmost CShape whose outline contains the point p.
	 * @param p The point
	 * @return the topmost CShape whose outline contains the point p, null if no CShape's outline contains p. 
	 * @see fr.lri.swingstates.canvas.CShape#isOnOutline(Point2D)
	 */
	CShape isOnOutline(Point2D p);
	
	/**
	 * Returns the topmost <code>CShape</code> that intersects the given <code>CShape</code> s.
	 * @param s The shape
	 * @return the topmost <code>CShape</code> that intersects the given <code>CShape</code>, null if no <code>CShape</code> intersects s.
	 * @see fr.lri.swingstates.canvas.CShape#intersects(CShape)
	 */
	CShape intersects(CShape s);
	
	/**
	 * Tests whether a <code>CShape</code> s intersects this <code>CElement</code>.
	 * @param s The shape
	 * @return the intersection as a <code>CPolyLine</code> if s intersects it, null otherwise.
	 * @see fr.lri.swingstates.canvas.CShape#getIntersection(CShape)
	 */
	CPolyLine getIntersection(CShape s);
	
	/**
	 * Returns the topmost CShape that has tag t.
	 * @param t The tag
	 * @return the topmost CShape that has tag t, null if no CShape has tag t. 
	 * @see fr.lri.swingstates.canvas.CShape#hasTag(fr.lri.swingstates.canvas.CTag)
	 */
	CShape getFirstHavingTag(CTag t);
	
	/**
	 * Returns true if every shape with this tag also has tag t.
	 * @param t The name of the tag
	 * @return true if every shape with this tag also has tag t, false otherwise. 
	 * @see fr.lri.swingstates.canvas.CShape#hasTag(String)
	 */
	boolean hasTag(String t);
	
	/**
	 * Returns true if every shape with this tag also has tag t.
	 * @param t The tag
	 * @return true if every shape with this tag also has tag t, false otherwise. 
	 */
	boolean hasTag(CTag t);
	
	/**
	 * Changes the display list order so that every CShape contained in this CElement is right above 
	 * the top most shape in another CElement. The relative display order between shapes contained in this CElement is unchanged.
	 * <b>WARNING</b>: If this CElement also contains one of the elements contained in <code>before</code>, this method does nothing.
	 * @param before The element that must be right below every shape contained in this <code>CElement</code> in the display list
	 * @return This <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#below(CElement)
	 */
	CElement above(CElement before);
	
	/**
	 * Calls <code>aboveAll()</code> for every CShape contained in this <code>CElement</code>.
	 * @return This <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#aboveAll()
	 */
	CElement aboveAll();
	
	/**
	 * Changes the display list order so that every CShape contained in this CElement is right below 
	 * the top least shape in another CElement. The relative display order between shapes contained in this CElement is unchanged.
	 * <b>WARNING</b>: If this CElement also contains one of the elements contained in <code>after</code>, this method does nothing.
	 * @param after The element that must be right above every shape contained in this <code>CElement</code> in the display list
	 * @return This tag
	 * @see fr.lri.swingstates.canvas.CShape#below(CElement)
	 */
	CElement below(CElement after);
	
	/**
	 * Calls <code>belowAll()</code> for every CShape contained in this <code>CElement</code>.
	 * @return This <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#belowAll()
	 */
	CElement belowAll();
	
	/**
	 * Calls <code>setClip(CShape clip)</code> for every CShape contained in this <code>CElement</code>.
	 * @param clip The clipping shape, or null to reset clipping of this shape
	 * @return This <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#setClip(CShape)
	 */
	CElement setClip(CShape clip);
	
	/**
	 * Calls addGhost() on every CShape contained in this <code>CElement</code>.
	 * @see fr.lri.swingstates.canvas.CShape#addGhost()
	 */
	void addGhost();
	
	/**
	 * Calls removeGhost() on every CShape contained in this <code>CElement</code>.
	 * @see fr.lri.swingstates.canvas.CShape#removeGhost()
	 */
	void removeGhost();
	
	/**
	 * @return the rectangle that is the bounding box of the group of SMShapes contained in this <code>CElement</code>.
	 */
	CRectangle getBoundingBox();
	
	/**
	 * @return the minimum x-coordinate of the rectangle that is the bounding box of the group of CShapes contained in this <code>CElement</code>.
	 */
	double getMinX();
	
	/**
	 * @return the maximum x-coordinate of the rectangle that is the bounding box of the group of CShapes contained in this <code>CElement</code>.
	 */
	double getMaxX();
	
	/**
	 * @return center x-coordinate of the rectangle that is the bounding box of the group of CShapes contained in this <code>CElement</code>.
	 */
	double getCenterX();
	
	/**
	 * @return the minimum y-coordinate of the rectangle that is the bounding box of the group of CShapes contained in this <code>CElement</code>.
	 */
	double getMinY();
	
	/**
	 * @return the maximum y-coordinate of the rectangle that is the bounding box of the group of CShapes contained in this <code>CElement</code>.
	 */
	double getMaxY();
	
	/**
	 * @return center y-coordinate of the rectangle that is the bounding box of the group of CShapes contained in this <code>CElement</code>.
	 */
	double getCenterY();
	
	/**
	 * Returns the first shape contained in this <code>CElement</code>.
	 * @return the first shape contained in this <code>CElement</code>, null if no shape is contained.
	 */
	CShape firstShape();
	
	/**
	 * Returns the first antialiased CShape contained in this <code>CElement</code>.
	 * @return the first antialiased CShape contained in this <code>CElement</code>, null if no CShape is antialiased.
	 * @see fr.lri.swingstates.canvas.CShape#isAntialiased()
	 */
	CShape getFirstAntialiasedShape();
	
	/**
	 * Returns the first CShape contained in this <code>CElement</code> and that is filled.
	 * @return the first CShape contained in this <code>CElement</code> and that is filled, null if no CShape is filled.
	 * @see fr.lri.swingstates.canvas.CShape#isFilled()
	 */
	CShape getFirstFilledShape();
	
	
	/**
	 * Returns the first CShape contained in this <code>CElement</code> and that is outlined.
	 * @return the first CShape contained in this <code>CElement</code> and that is outlined, null if no CShape is outlined.
	 * @see fr.lri.swingstates.canvas.CShape#isOutlined()
	 */
	CShape getFirstOutlinedShape();
	
	/**
	 * Calls <code>addTag(Canvas.Tag t)</code> for every CShape contained in this <code>CElement</code>.
	 * @param t The tag
	 * @return this <code>CElement</code>
	 */
	CElement addTag(CExtensionalTag t);
	
	/**
	 * Calls <code>addTag(String t)</code> for every CShape contained in this <code>CElement</code>.
	 * @param t The name of the tag
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#addTag(String)
	 */
	CElement addTag(String t);
	
	/**
	 * Calls <code>removeTag(Tag t)</code> for every CShape contained in this <code>CElement</code> only if t is a <code>SMExtensionalTag</code>, 
	 * does nothing otherwise.
	 * @param t The tag
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#removeTag(CExtensionalTag)
	 */
	CElement removeTag(CExtensionalTag t);
	
	/**
	 * Calls <code>removeTag(String t)</code> for every CShape contained in this <code>CElement</code>.
	 * @param t The name of the tag
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#removeTag(String)
	 */
	CElement removeTag(String t);
	
	/**
	 * Calls <code>animate(Animation animTagScale)</code> for every CShape contained in this <code>CElement</code>.
	 * @param anim The animation to associate to each shape.
	 * @return this <code>CElement</code>
	 * @see fr.lri.swingstates.canvas.CShape#animate(Animation)
	 */
	CElement animate(Animation anim);

}
