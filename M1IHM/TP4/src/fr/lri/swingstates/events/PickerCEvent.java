/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr) and Michel Beaudouin-Lafon
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.events;

import java.awt.Component;

import fr.lri.swingstates.canvas.CShape;

/**
 * A mouse event (<code>java.awt.MouseEvent</code>) originated by
 * a given <code>Picker</code> and that can carries a CShape.
 * 
 * @author Caroline Appert
 *
 */
public class PickerCEvent extends PickerEvent<CShape> {

	/**
	 * Builds a <code>PickerCEvent</code>.
	 * @param source The source of the event
	 * @param picked The shape on which this event occurs
	 * @param picker The picker that originates this event
	 * @param id The id of this event
	 * @param when The time at which this event occurs
	 * @param modifiers The modifiers of this event
	 * @param x The x-location of this event
	 * @param y The y-location of this event
	 * @param clickCount The number of clicks of this event
	 * @param popupTrigger If this event triggers a popup
	 * @param scrollType The type of scrolling which should take place in response to this event; valid values are <code>WHEEL_UNIT_SCROLL</code> and <code>WHEEL_BLOCK_SCROLL</code> (in class java.awt.MouseWheelEvent) 
	 * @param scrollAmount for scrollType <code>WHEEL_UNIT_SCROLL</code>, the number of units to be scrolled
	 * @param wheelRotation The amount that the mouse wheel was rotated (the number of "clicks")
	 */
	public PickerCEvent(Component source, CShape picked, Picker picker, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int scrollType, int scrollAmount, int wheelRotation) {
		super(source, picked, picker, id, when, modifiers, x, y, clickCount,
				popupTrigger, scrollType, scrollAmount, wheelRotation);
	}

	/**
	 * Builds a <code>PickerCEvent</code>.
	 * @param source The source of the event
	 * @param picked The shape on which this event occurs
	 * @param picker The picker that originates this event
	 * @param id The id of this event
	 * @param when The time at which this event occurs
	 * @param modifiers The modifiers of this event
	 * @param x The x-location of this event
	 * @param y The y-location of this event
	 * @param clickCount The number of clicks of this event
	 * @param popupTrigger If this event triggers a popup
	 */
	public PickerCEvent(Component source, CShape picked, Picker picker, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger) {
		super(source, picked, picker, id, when, modifiers, x, y, clickCount,
				popupTrigger);
	}

	/**
	 * Builds a <code>PickerCEvent</code>.
	 * @param source The source of the event
	 * @param picker The picker that originates this event
	 * @param id The id of this event
	 * @param when The time at which this event occurs
	 * @param modifiers The modifiers of this event
	 * @param x The x-location of this event
	 * @param y The y-location of this event
	 * @param clickCount The number of clicks of this event
	 * @param popupTrigger If this event triggers a popup
	 * @param scrollType The type of scrolling which should take place in response to this event; valid values are <code>WHEEL_UNIT_SCROLL</code> and <code>WHEEL_BLOCK_SCROLL</code> (in class java.awt.MouseWheelEvent) 
	 * @param scrollAmount for scrollType <code>WHEEL_UNIT_SCROLL</code>, the number of units to be scrolled
	 * @param wheelRotation The amount that the mouse wheel was rotated (the number of "clicks")
	 */
	public PickerCEvent(Component source, Picker picker, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int scrollType, int scrollAmount, int wheelRotation) {
		super(source, picker, id, when, modifiers, x, y, clickCount, popupTrigger,
				scrollType, scrollAmount, wheelRotation);
	}

	/**
	 * Builds a <code>PickerCEvent</code>.
	 * @param source The source of the event
	 * @param picker The picker that originates this event
	 * @param id The id of this event
	 * @param when The time at which this event occurs
	 * @param modifiers The modifiers of this event
	 * @param x The x-location of this event
	 * @param y The y-location of this event
	 * @param clickCount The number of clicks of this event
	 * @param popupTrigger If this event triggers a popup
	 */
	public PickerCEvent(Component source, Picker picker, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger) {
		super(source, picker, id, when, modifiers, x, y, clickCount, popupTrigger);
	}
	
}
