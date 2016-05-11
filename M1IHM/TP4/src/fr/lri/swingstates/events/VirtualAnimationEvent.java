/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.events;

import fr.lri.swingstates.animations.Animation;


/**
 * An event originated by an animation.
 * 
 * @author Caroline Appert
 *
 */
public class VirtualAnimationEvent extends VirtualEvent {

	Animation animation;
	
	/**
	 * Builds an animation event.
	 * @param nameEvent The name of the event: "AnimationStarted", "AnimationStopped", "AnimationSuspended" or "AnimationResumed".
	 * @param anim The animation.
	 */
	public VirtualAnimationEvent(String nameEvent, Animation anim) {
		super(nameEvent, anim);
		animation = anim;
	}
	
	/**
	 * @return The animation.
	 */
	public Animation getAnimation() {
		return animation;
	}
	
}
