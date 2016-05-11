/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JTextField;

/**
 * A text field whose value can be set by a joystick-like interaction. 
 * Press on it and drag: 
 * <ul>
 * <li> The numeric value increases when dragging upwards and decreases when dragging towards.
 * <li> As far as the cursor is from the text field as fast the value is set.
 * </ul>
 * 
 * @author Caroline Appert
 *
 */
public class JoystickTextFieldApplet extends BasicJApplet {
	
	public void createGUI() {
		JTextField text = new JTextField("0");
		text.setText("0");
		setSize(300, 300);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		text.setEditable(false);
		text.setSize(new Dimension(100, 30));
		Dimension size = text.getSize();
		text.setBounds(150 - size.width/2, 150 - size.height/2, size.width, size.height);
		getContentPane().add(text);
		new JoystickTextField(this);
	}
	
}

