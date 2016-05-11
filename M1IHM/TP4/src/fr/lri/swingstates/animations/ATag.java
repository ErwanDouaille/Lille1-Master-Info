/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import java.util.Iterator;

import fr.lri.swingstates.canvas.CElement;
import fr.lri.swingstates.sm.Tag;


/**
 * <p><code>ATag</code>s are labels associated to canvas' animations. <code>ATag</code> class has most of the methods of the <code>Animation</code> class.
 * Calling one of these <code>ATag</code>'s methods calls the method for each tagged <code>Animation</code>.</p>
 * <p>A tag is an object corresponding to a set of <code>Animation</code>s that can be browsed:</p>
 * <pre>
 * aTag.reset();
 * while(aTag.hasNext())
 *    Animation a = aTag.nextAnimation();
 * </pre>
 * 
 * <p>
 * Tags can be used to in Animation* transitions in a <code>CStateMachine</code> to know when an animation
 * has been started, stopped, suspended or resumed.
 * </p>
 * <pre>
 * class MyTag extends ATag { ... }
 * ...
 * Transition anAnimationStopped = new AnimationStopped(MyTag.class) {
 * 	  public void action() {
 *       System.out.println(anAnimationStopped+" has just stopped.");
 *       // start a new animation for example
 *    }
 * };
 * </pre>
 * 
 * <p>
 * A tag can be attached to any number of animations.
 * An animation can have any number of tags attached to it.
 * </p>
 * 
 * <p>
 * Some tags, <code>AExtensionalTag</code> tags, can be explicitely attached to and detached from an animation, 
 * i.e. an animation can be added to or removed from the tag's collection,
 * while others can not. <code>ANamedTag</code> are special extensional tags that can be referenced by their key string.
 * </p> 
 * 
 * <p>
 * Tags are used for grouping animations. 
 * for example, one can decide to start several animations at the same time. 
 * For example, to animate two rectangles that move towards two opposite directions 
 * (we use a <code>ANamedTag</code>):
 * </p>
 * <pre>
 * Animation onePart = new AnimationTranslateBy(-2, 0);
 * onePart.setAnimatedElement(canvas.newRectangle(100, 100, 20, 20).addTag("part"));
 * Animation anotherPart = new AnimationTranslateBy(2, 0);
 * anotherPart.setAnimatedElement(canvas.newRectangle(120, 120, 20, 20).addTag("part"));
 * ...
 * ANamedTag.getTag("part").start();
 * </pre>
 * 
 * @see fr.lri.swingstates.sm.Tag
 * 
 * @author Caroline Appert
 */

public class ATag extends Tag {

	protected Iterator iterator = null;
	
	/**
	 * Builds a <code>ATag</code> that can tag animations.
	 */
	public ATag(){ }
	
	/**
	 * @return the next animation tagged by this tag as an <code>Object</code>.
	 * @see java.util.Iterator#next()
	 */
	public final Object next() {
		return nextAnimation();
	}
	
	/**
	 * Tests if the animation <code>a</code> is tagged by this tag.
	 * @param a The animation
	 * @return true if <code>a</code> is tagged by this tag, false otherwise.
	 */
	public boolean tagsAnimation(Animation a) {
		reset();
		while(hasNext())
			if(nextAnimation() == a) return true;
		return false;
	}
	
	/**
	 * Method called by <code>action()</code>. This method does nothing.
	 * Redefine it in a subclass to specify the effect of <code>ATag#action()</code>
	 * @param a The <code>Animation</code> on which applying specific treatments.
	 */
	public void action(Animation a) { }
	
	/**
	 * Calls <code>action(Animation s)</code> for every <code>Animation</code> <code>a</code> that has this tag.
	 * @return this tag
	 */ 
	public final ATag action(){
		reset(); 
		while(hasNext())
			action(nextAnimation());
		return this;
	}
	
	/**
	 * Calls <code>setDelay(int d)</code> for every <code>Animation</code> that has this tag.
	 * @param d The delay in milliseconds.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#setDelay(int)
	 */
	public ATag setDelay(int d) {
		reset(); 
		while(hasNext())
			nextAnimation().setDelay(d);
		return this;
	}
	
	/**
	 * Calls <code>setDurationLap(long d)</code> for every <code>Animation</code> that has this tag.
	 * @param d The duration in milliseconds.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#setLapDuration(long)
	 */
	public ATag setDurationLap(long d) {
		reset(); 
		while(hasNext())
			nextAnimation().setLapDuration(d);
		return this;
	}
	
	/**
	 * Calls <code>setFunction(int t)</code> for every <code>Animation</code> that has this tag.
	 * @param t The type of the pacing function.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#setFunction(short)
	 */
	public ATag setFunction(short t) {
		reset(); 
		while(hasNext())
			nextAnimation().setFunction(t);
		return this;
	}
	
	/**
	 * Calls <code>setNbLaps(int laps)</code> for every <code>Animation</code> that has this tag.
	 * @param laps The number of laps.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#setNbLaps(int)
	 */
	public ATag setNbLaps(int laps) {
		reset(); 
		while(hasNext())
			nextAnimation().setNbLaps(laps);
		return this;
	}
	
	/**
	 * Calls <code>setAnimatedElement(CElement ce)</code> for every <code>Animation</code> that has this tag.
	 * @param ce The <code>CElement</code> to animate.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#setAnimatedElement(CElement)
	 */
	public ATag setAnimatedElement(CElement ce) {
		reset(); 
		while(hasNext())
			nextAnimation().setAnimatedElement(ce);
		return this;
	}
	
	/**
	 * Calls <code>start()</code> for every <code>Animation</code> that has this tag.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#start()
	 */
	public final ATag start() {
		reset(); 
		while(hasNext())
			nextAnimation().start();
		return this;
	}
	
	/**
	 * Calls <code>stop()</code> for every <code>Animation</code> that has this tag.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#stop()
	 */
	public final ATag stop() {
		reset(); 
		while(hasNext())
			nextAnimation().stop();
		return this;
	}
	
	/**
	 * Calls <code>suspend()</code> for every <code>Animation</code> that has this tag.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#suspend()
	 */
	public final ATag suspend() {
		reset(); 
		while(hasNext())
			nextAnimation().suspend();
		return this;
	}
	
	/**
	 * Calls <code>resume()</code> for every <code>Animation</code> that has this tag.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#resume()
	 */
	public final ATag resume() {
		reset(); 
		while(hasNext())
			nextAnimation().resume();
		return this;
	}

	/**
	 * Calls <code>addTag(animations.AExtensionalTag t)</code> for every animation that has this tag.
	 * @param t The tag to add.
	 * @return this tag.
	 * @see fr.lri.swingstates.animations.Animation#addTag(fr.lri.swingstates.animations.AExtensionalTag)
	 */
	public ATag addTag(AExtensionalTag t){ 
		if(t == null || t == this)
			return this;
		reset(); 
		while(hasNext())
			(nextAnimation()).addTag(t);
		return this;
	}
	
	/**
	 * Calls <code>addTag(String t)</code> for every animation that has this tag.
	 * @param t The name of the tag to add.
	 * @return this tag
	 * @see fr.lri.swingstates.animations.Animation#addTag(fr.lri.swingstates.animations.AExtensionalTag)
	 */
	public ATag addTag(String t){ 
		if(t == null)
			return this;
		reset(); 
		while(hasNext())
			(nextAnimation()).addTag(t);
		return this;
	}
	
	/**
	 * Calls <code>removeTag(animations.ATag t)</code> for every animation that has this tag only if t is a <code>AExtensionalTag</code>, 
	 * does nothing otherwise.
	 * @param t The tag to remove.
	 * @return this tag
	 * @see fr.lri.swingstates.animations.Animation#removeTag(fr.lri.swingstates.animations.AExtensionalTag)
	 */
	public ATag removeTag(AExtensionalTag t) { 
		if(t == null) return this;
		if(t == this) {
			reset(); 
			while(hasNext()) {
				t.removeFrom(nextAnimation());
				reset();
			}
		} else {
			reset(); 
			while(hasNext())
				(nextAnimation()).removeTag(t);
		}
		return this;
	}

	/**
	 * Calls <code>removeTag(String t)</code> for every animation that has this tag.
	 * @param t The name of the tag to remove.
	 * @return this tag
	 * @see fr.lri.swingstates.animations.Animation#removeTag(String)
	 */
	public ATag removeTag(String t) { 
		removeTag(ANamedTag.getTag(t));
		return this;
	}
	
	/**
	 * @return the next animation tagged by this tag as an <code>Animation</code>.
	 * @see java.util.Iterator#next()
	 */
	public Animation nextAnimation() {
		return (Animation)iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		iterator = collection.iterator();
	}
	
}
