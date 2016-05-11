/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.animations;

import fr.lri.swingstates.canvas.CElement;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.events.VirtualAnimationEvent;
import fr.lri.swingstates.events.VirtualCElementEvent;

/**
 * <p>
 * An animation to control a continuous change on a <code>CElement</code>: a <code>CShape</code>, a <code>CTag</code> or a <code>Canvas</code>.
 * Animating a <code>CTag</code> or a <code>Canvas</code> means animating every <code>CShape</code> tagged by this tag or displayed in this canvas.
 * </p>
 * 
 * <p>
 * An animation lasts a given number of "laps". A "lap" lasts <code>durationLap</code>
 * milliseconds (by default, 1000). During a "lap", the value of a parameter t starts at 0 and ends at 1 for odd laps 
 * (and starts at 1 and ends at 0 for even laps.
 * Value of parameter t whose value is updated every <code>delay</code> milliseconds (by default, 40).
 * t value follows a pacing funcyion (by default, a linear pacing function).
 * An animation can be started, stopped, suspended and resumed. 
 * </p>
 * 
 * <p>
 * You can also override the methods <code>doStart()</code>, <code>doStop()</code>, <code>doSuspend()</code> and <code>doResume()</code> 
 * to define specific treatments applied when calling respectively the methods <code>start()</code>, <code>stop()</code>, <code>suspend()</code> and <code>resume()</code>.
 * For example, if you want to change the color of the animated elements when you start and stop an animation that rotate and translate:
 * <pre>
 * class AnimationRotateAndTranslateBy extends Animation {
 * 	double rotate;
 * 	double translatex, translatey;
 * 
 * 	public AnimationRotateAndTranslateBy(double r, double tx, double ty) {
 * 		super();
 * 		rotate = r;
 * 		translatex = tx;
 * 		translatey = ty;
 * 	}
 * 
 * 	public void step(double t) {
 * 		getAnimated().rotateBy(rotate).scaleBy(translatex, translatey);
 * 	}
 * 
 * 	public void doStart() {
 * 		getAnimated().setFillPaint(Color.RED);
 * 	}
 * 
 * 	public void doStop() {
 * 		getAnimated().setFillPaint(Color.LIGHT_GRAY);
 * 	}
 * }
 * </pre>
 * </p>
 * 
 * @author Caroline Appert
 *
 */
public abstract class Animation {
	
	/**
	 * The constant to pass to the method <code>setFunction(short)</code> to have a linear pacing function.
	 */
	public static short FUNCTION_LINEAR = 1;
	
	/**
	 * The constant to pass to the method <code>setFunction(short)</code> to have a "sigmoid" pacing function. A sigmoid function corresponds to a slow-in/fast-out effect.
	 */
    public static short FUNCTION_SIGMOID = 2;
    
    /**
	 * The constant to pass to the method <code>setNbLaps(int)</code> to make an animation never stop.
	 */
    public static int INFINITE_NUMBER_OF_LAPS = -1;
    
	private CElement animated;
	
	private double   t = 0;
	private int      delay = 40;
	
	private int      nbLaps = 1;
	private long     lapDuration = 1000;
	
	private short      type = FUNCTION_LINEAR;
	
	private long   startTime = 0;
	private long   endTime = 0;
	private long   pauseDuration = 0;
	private long   lastSuspensionTime = 0;
	private long   elapsed = 0;
	private long   nextStepTime;
	
	private boolean suspended = false;
	private boolean started = false;
	
	private Canvas canvas;
	/**
	 * Builds an animation and registers it to the animation manager.
	 */
	public Animation() { }
	
	/**
	 * For internal use. See class <code>SwingStatesApplet</code>.
	 * @see fr.lri.swingstates.sm.SwingStatesApplet#init()
	 */
	public static void resetAnimationManager() {
		AnimationManager.reset();
	}

	/**
	 * Sets the delay between to successive values of the parameter t. By default, the value of the delay is 40 milliseconds.
	 * @param d The delay in milliseconds.
	 * @return this animation. 
	 */
	public synchronized Animation setDelay(int d) {
		delay = d;
		return this;
	}
	
	/**
	 * @return the duration (in milliseconds) of a lap of this animation.
	 */
	public long getLapDuration() {
		return lapDuration;
	}
	
	/**
	 * Sets the duration of a "lap". By default, the value of the duration is 1000 milliseconds.
	 * @param d The duration in milliseconds.
	 * @return this animation. 
	 */
	public synchronized Animation setLapDuration(long d) {
		if(d <= 0) lapDuration = 0;
		else lapDuration = d;
		return this;
	}
	
	/**
	 * Sets the type of the pacing function, use the static constants <code>Animation.FUNCTION_LINEAR</code> or <code>Animation.FUNCTION_SIGMOID</code>.
	 * By default, the type function is linear.
	 * @param t The type of the pacing function.
	 * @return this animation.
	 */
	public synchronized Animation setFunction(short t) {
		type = t;
		return this;
	}
	
	/**
	 * Sets the number of laps of this animation.
	 * By default, an animation lasts one lap (i.e. the parameter starts at 0 and ends at 1). For example, if one sets the number of laps to 2, the parameter starts at 0 and ends at 0 passing by 1.
	 * @param laps The number of laps.
	 * @return this animation.
	 */
	public synchronized Animation setNbLaps(int laps) {
		nbLaps = laps;
		return this;
	}
	
	/**
	 * Sets the <code>CElement</code> that must be animated by this animation.
	 * If the canvas that must received animation events has not yet been set,
	 * it is set to the canvas to which belongs this <code>CElement</code>.
	 * @param ce The <code>CElement</code> to animate.
	 * @return this animation.
	 */
	public synchronized Animation setAnimatedElement(CElement ce) {
		animated = ce;
		if(canvas == null) canvas = animated.getCanvas();
		return this;
	}
	
	/**
	 * Method called when this animation is started.
	 * This method does nothing. It can be redefined in derived classes.
	 * @see Animation#start() 
	 */
	public void doStart() { }
	
	/**
	 * Starts this animation.
	 * @return This animation.
	 */
	public final synchronized Animation start() {
//		if(animated == null) return this;
		t = 0;
		elapsed = 0;
		pauseDuration = 0;
		lastSuspensionTime = -1;
		startTime = AnimationManager.getInstance().getCurrentTime();
		if(nbLaps==0 || lapDuration==0) return this;
		if(nbLaps<0) endTime=-1;
		else endTime = startTime + nbLaps*lapDuration;
		nextStepTime = startTime;
		AnimationManager.getInstance().addAnim(this);
		suspended = false;
		started = true;
		doStart();
		processAnimationEvent("AnimationStarted");
		return this;
	}
	
	/**
	 * Method called when this animation is stopped.
	 * This method does nothing. It can be redefined in derived classes.
	 * @see Animation#stop() 
	 */
	public void doStop() { }
	
	/**
	 * Stops this animation.
	 * @return This animation.
	 */
	public final synchronized Animation stop() {
		AnimationManager.getInstance().removeAnim(this);
		started = false;
		doStop();
		processAnimationEvent("AnimationStopped");
		return this;
	}
	
	/**
	 * Method called when this animation is suspended.
	 * This method does nothing. It can be redefined in derived classes.
	 * @see Animation#suspend() 
	 */
	public void doSuspend() { }
	
	/**
	 * Suspends this animation. Use the method <code>resume()</code> to resume the animation being suspended.
	 * @return This animation.
	 * @see Animation#resume()
	 */
	public final synchronized Animation suspend() {
		suspended = true;
		lastSuspensionTime = AnimationManager.getInstance().getCurrentTime();
		processAnimationEvent("AnimationSuspended");
		doSuspend();
		return this;
	}
	
	/**
	 * Method called when this animation is resumed.
	 * This method does nothing. It can be redefined in derived classes.
	 * @see Animation#resume() 
	 */
	public void doResume() { }
	
	/**
	 * Resumes this animation that has been resumed using the method <code>resume()</code>.
	 * @return This animation.
	 * @see Animation#suspend()
	 */
	public final synchronized Animation resume() {
		startTime = AnimationManager.getInstance().getCurrentTime();
		if(lastSuspensionTime != -1) pauseDuration += (startTime - lastSuspensionTime);
		lastSuspensionTime = -1;
		nextStepTime = startTime;
		endTime = startTime + (nbLaps*lapDuration-elapsed);
		suspended = false;
		processAnimationEvent("AnimationResumed");
		doResume();
		return this;
	}

	void processAnimationEvent(String event){
		if(canvas != null) {
			canvas.processEvent(new VirtualAnimationEvent(event, this));
		}
	}
	
	/**
	 * Override this abstract to specify the effect of this animation.
	 * @param t The parameter of this animation.
	 */
	public abstract void step(double t);

	/**
     *@param n determines the steepness of the function
     *@param t should be between 0 and 1 for our purpose
     */
    static double computeSigmoid(int n, double t){
        return (Math.atan(n*(2*t-1))/Math.atan(n)+1)/2.0f;   
    } 
	
    synchronized double updateTValue() {
    	long currentTime = AnimationManager.getInstance().getCurrentTime();
    	nextStepTime = currentTime+delay;
    	if(nbLaps<0) {
    		elapsed = currentTime - startTime - pauseDuration;
    		long timeDoneInLap = elapsed%lapDuration;
    		long currentLap = (long) (elapsed/lapDuration);
    		if(currentLap%2 == 0) t = (double)timeDoneInLap/lapDuration;
    		else t = 1 - (double)timeDoneInLap/lapDuration;
    	} else {
    		long timeLeft = endTime - currentTime;
        	if(timeLeft<0) {
        		stop();
        		return -1;
        	}
        	long lapsToDo = timeLeft/lapDuration;
        	long timeToDoInLap = timeLeft%lapDuration;
        	long currentLap = nbLaps-lapsToDo-1;
        	if(currentLap%2 == 0) t = 1 - (double)timeToDoInLap/lapDuration;
    		else t = (double)timeToDoInLap/lapDuration;
        	elapsed = currentLap%2==0 ? (long)(currentLap*lapDuration + t*lapDuration)
    				: (long)(currentLap*lapDuration + (1-t)*lapDuration);
    	}
		if(type == FUNCTION_SIGMOID)
			return computeSigmoid(4, t);
		else
			if(type == FUNCTION_LINEAR)
				return t;
		return t;
	}
	
	void update() {
		if(suspended) return;
		if(nextStepTime <= AnimationManager.getInstance().getCurrentTime()) {
			double on = updateTValue();
			if(t <= 0) {
				t = 0;
			} else {
				if(t >= 1) {
					t = 1;
				}
			}
			if(on != -1) step(t);
			if(getAnimated() != null) {
				if(getAnimated().getCanvas().isTracking(getAnimated())) {
					getAnimated().getCanvas().processEvent(new VirtualCElementEvent(getAnimated()));
				}
			}
		}
	}

	/**
	 * @return the <code>CElement</code> animated by this animation.
	 */
	public CElement getAnimated() {
		return animated;
	}
	
	/**
	 * Adds a tag to this animation.
	 * @param t The tag to be added
	 * @return this animation.
	 */
	public Animation addTag(AExtensionalTag t){
		if(t == null)
			return this;
		t.addTo(this);
		return this;
	}
	
	/**
	 * Adds a <code>ANamedTag</code> tag to this animation.
	 * @param t The name of the tag to be added
	 * @return this animation.
	 */
	public Animation addTag(String t){
		ANamedTag tag = ANamedTag.getTag(t);
		if(tag == null)
			tag = new ANamedTag(t);
		addTag(tag);
		return this;
	}
	
	/**
	 * Removes a tag from this animation.
	 * @param t The tag to remove
	 * @return this animation.
	 */
	public Animation removeTag(AExtensionalTag t) {
		if (t == null) return this;
		t.removeFrom(this);
		return this;
	}
	
	/**
	 * Removes a tag from this animation.
	 * @param t The name of the <code>ANamedTag</code> tag to remove
	 * @return this animation.
	 */
	public Animation removeTag(String t) {
		removeTag(ANamedTag.getTag(t));
		return this;
	}
	
	/**
	 * Tests whether this animation has a given tag.
	 * @param t The tag to be tested
	 * @return true if t tags this animation.
	 */
	public boolean hasTag(ATag t){
		if (t == null)
			return false;
		t.reset();
		while(t.hasNext())
			if(t.nextAnimation() == this) return true;
		return false;
	}

	/**
	 * Tests whether this animation has a given <code>ANamedTag</code> tag.
	 * @param t The name of the tag to be tested
	 * @return true if t tags this animation.
	 */
	public boolean hasTag(String t) {
		return hasTag(ANamedTag.getTag(t));
	}

	/**
	 * @return The pacing function for this <code>Animation</code>: <code>Animation.FUNCTION_LINEAR</code> or <code>Animation.FUNCTION_SIGMOID</code>.
	 */
	public short getPacingFunction() {
		return type;
	}
	
	/**
	 * @return The delay in milliseconds between two successive animation steps.
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * @return The number of laps in this animation.
	 */
	public int getNbLaps() {
		return nbLaps;
	}

	/**
	 * @return True if this animation is suspended.
	 */
	public boolean isSuspended() {
		return suspended;
	}
	
	/**
	 * @param canvas Sets the canvas on which will be fired animation events.
	 */
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * @return True if this animation is started.
	 */
	public boolean isStarted() {
		return started;
	}

}

