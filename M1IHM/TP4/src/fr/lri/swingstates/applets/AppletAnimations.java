/*  
	 *   Authors: Caroline Appert (caroline.appert@lri.fr)
	 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
	 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
	 */
package fr.lri.swingstates.applets;

import java.awt.Color;

import fr.lri.swingstates.animations.AExtensionalTag;
import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.animations.AnimationFillPaint;
import fr.lri.swingstates.animations.AnimationTranslateBy;
import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Press;

class ReboundingBall extends AnimationTranslateBy {

	private int widthBound, heightBound;
	
	public ReboundingBall(double dx, double dy, int wb, int wh) {
		super(dx, dy);
		widthBound = wb;
		heightBound = wh;
	}
	
	public void step(double t) {
		super.step(t);
		if(getAnimated().getMinX() < 0) {
			getAnimated().setReferencePoint(0,0.5).translateTo(0, getAnimated().getCenterY());
			setDelta(-getDx(), getDy());
		} else {
			if(getAnimated().getMaxX() > widthBound) {
				getAnimated().setReferencePoint(1,0.5).translateTo(widthBound, getAnimated().getCenterY());
				setDelta(-getDx(), getDy());
			} else {
				if(getAnimated().getMinY() < 0) {
					getAnimated().setReferencePoint(0.5,0).translateTo(getAnimated().getCenterX(), 0);
					setDelta(getDx(), -getDy());
				} else {
					if(getAnimated().getMaxY() > heightBound) {
						getAnimated().setReferencePoint(0.5,1).translateTo(getAnimated().getCenterX(), heightBound);
						setDelta(getDx(), -getDy());
					}
				}
			}
		}
	}
}

class SimpleAnimatedCanvas extends Canvas {

	Animation fillRed;
	
	public SimpleAnimatedCanvas(int w, int h) {
		super(w, h);
		
		AnimationTranslateBy animBall = new ReboundingBall(-10, 10, 400, 300);
		animBall.setAnimatedElement(newEllipse(50, 50, 20, 20));
		
		fillRed = new AnimationFillPaint(Color.RED);
		fillRed.setLapDuration(2000).setNbLaps(1);
		
		CStateMachine smBall = new CStateMachine() {
			
			ReboundingBall animRebound;
			public State idle = new State() {
				Transition newBall = new Press(BUTTON1, ">> prepareBall") {
					public void action() {
						CEllipse newBall = newEllipse(getPoint().getX(), getPoint().getY(), 20, 20);
						fillRed.setAnimatedElement(newBall).start();
					}
				};
//			};
//			
//			public State prepareBall = new State() {
				Transition endCollide = new AnimationStopped(fillRed, ">> idle") {
					public void action() {
						animRebound = new ReboundingBall(0, 10, 400, 300);
						animRebound.setAnimatedElement(getAnimation().getAnimated()).start();
					}
				};
			};
		};

		smBall.attachTo(this);
		
		animBall.start();
	}
	
	

}

class AnimatedCanvas extends Canvas {

	AExtensionalTag animationsFillRed;
	
	public AnimatedCanvas(int w, int h) {
		super(w, h);
		
		animationsFillRed = new AExtensionalTag();
		AnimationTranslateBy animBall = new ReboundingBall(-10, 10, 400, 300);
		animBall.setAnimatedElement(newEllipse(50, 50, 20, 20));
		
		CStateMachine smBall = new CStateMachine() {
			
			ReboundingBall animRebound;
			public State idle = new State() {
				Transition prepareBall = new Press(BUTTON1) {
					public void action() {
						CEllipse newBall = newEllipse(getPoint().getX(), getPoint().getY(), 20, 20);
		                AnimationFillPaint fillRed = new AnimationFillPaint(Color.RED);
		                fillRed.setLapDuration(2000).setNbLaps(1).addTag(animationsFillRed);
		                newBall.animate(fillRed);
					}
				};
				Transition ballReady = new AnimationStopped(animationsFillRed) {
					public void action() {
						animRebound = new ReboundingBall(0, 10, 400, 300);
						animRebound.setAnimatedElement(getAnimation().getAnimated()).start();
					}
				};
			};
		};

		smBall.attachTo(this);
		
		animBall.start();
	}
	
	

}

public class AppletAnimations extends BasicApplet {

	public void createGUI() {
		Canvas canvas = new AnimatedCanvas(400, 300);
		add(canvas);
		setSize(400, 300);
	}

}
