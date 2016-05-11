/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

/**
 * An intentional tag to manage the "hierarchy" of a given <code>CShape</code>.
 * <p>A <code>CHierarchyTag(CShape s)</code> tags s and every shape havind s as an ancestor.</p>
 * 
 * @author Caroline Appert
 */
public class CHierarchyTag extends CIntentionalTag {
	
	CShape topShape;
	
	/**
	 * Builds a <code>CHierarchyTag</code> with the set composed of s and every shape having s as an ancestor.. 
	 * @param s The parent shape of the hierarchy.
	 */
	public CHierarchyTag(CShape s) {
		super(s.getCanvas());
		topShape = s;
		reset();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean criterion(CShape s) {
		if(topShape == s) return true;
		CShape tmp = s;
		while(tmp.getParent() != null) {
			tmp = tmp.getParent();
			if(tmp == topShape) return true;
		}
		return false;
	}
	
}
