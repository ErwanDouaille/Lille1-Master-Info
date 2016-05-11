/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.applets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.events.VirtualEvent;
import fr.lri.swingstates.sm.JNamedTag;
import fr.lri.swingstates.sm.JStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Event;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;



/**
 * A pie menu to change the background color of swing widgets.
 * Invoke it by a left button press and release the left button in an item to select a darken.
 * 
 * @author Caroline Appert
 *
 */
public class PieMenuApplet extends JApplet {
	
	JButton button;
	JCheckBox checkbox;
	JTextField textfield;
	Canvas canvas;
	CShape menu;
	
	// The state machine to handle picking of widgets and color events sent by smMenu machine.
	JStateMachine smWidgets;
	
	// The state machine to hide/show pie menu and select a color item in it.
	// Once a colored item is selected, it sends it to smWidgets machine.
	CStateMachine smMenu;
	
	public void init() {
	    try {
	        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	                createGUI();
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("createGUI didn't successfully complete");
	    }
	}
	
	/**
	 * Opens the pie menu at a given location.
	 * @param pt the location of the pie menu center
	 */
	public void showMenu(Point2D pt) {
		menu.translateTo(pt.getX(), pt.getY());
		canvas.getTag("menu").setDrawable(true).setPickable(true);
	}
	
	/**
	 * Closes the pie menu.
	 */
	public void hideMenu() {
		canvas.getTag("menu").setDrawable(false).setPickable(false);
	}
	
	/**
	 * Draws a pie shape.
	 * @param canvas The canvas in which this pie must be displayed.
	 * @param color The darken of the pie.
	 * @param x The x-coordinate of the pie's point.
	 * @param y The y-coordinate of the pie's point.
	 * @param start the starting angle of the pie (in radians)
	 * @param extent The extent of the pie (in radians)
	 * @param radius The radius of the pie.
	 * @return The pie shape.
	 */
	public static CPolyLine pie(Canvas canvas, Color color, double x, double y, double start, double extent, double radius) {
		CPolyLine itemBackground = canvas.newPolyLine(x, y);
		itemBackground.lineTo(x+radius, y).arcTo(0, extent, radius, radius).lineTo(x, y);
		itemBackground.setReferencePoint(0, 1).rotateBy(-start).setFillPaint(color).setTransparencyFill(0.4f).setAntialiased(true).setDrawable(false);
		// We attach an instance of ColorTag to each menu item.
		// CNamedTag is a predefined SwingStates tag that 
		// associates a string with any Canvas widget shape
		itemBackground.addTag(new ColorTag(color)).addTag("menu");
		return itemBackground;
	}
	
	public static CShape pieMenu(int radius, Canvas c) {
		CEllipse parent = (CEllipse) c.newEllipse(-2, -2, 4, 4).addTag("menu");
		pie(c, new Color(205, 150, 205), 0, 0, 0, Math.PI/2, radius).setParent(parent);
		pie(c, Color.YELLOW, 0, 0, Math.PI/2, Math.PI/2, radius).setParent(parent);
		pie(c, new Color(60, 179, 113), 0, 0, Math.PI, Math.PI/2, radius).setParent(parent);
		pie(c, new Color(100, 149, 237), 0, 0, 3*Math.PI/2, Math.PI/2, radius).setParent(parent);
		parent.aboveAll();
		return parent;
	}

	/**
	 * Content pane layout.
	 */
	void createGUI() {
		setSize(300, 150);
		canvas = new Canvas(300, 150);
		canvas.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		canvas.setOpaque(false);
		menu = pieMenu(30, canvas);
		JPanel contentPane;
		contentPane = (JPanel)getContentPane();
		contentPane.setLayout(null);
		contentPane.setSize(new Dimension(300, 150));
		contentPane.setBackground(Color.WHITE);
		
		JNamedTag colorable = new JNamedTag("colorable");
		
		button = new JButton("button");
		button.setSize(new Dimension(50, 30));
		button.setBackground(new Color(200, 200, 200));
		Dimension size = button.getPreferredSize();
		contentPane.add(button);
		button.setBounds(20, 30,
				size.width, size.height);
		colorable.addTo(button);
		
		checkbox = new JCheckBox("checkbox");
		checkbox.setSize(new Dimension(50, 30));
		checkbox.setBackground(new Color(200, 200, 200));
		size = checkbox.getPreferredSize();
		contentPane.add(checkbox);
		checkbox.setBounds(120, 60,
				size.width, size.height);
		colorable.addTo(checkbox);
		
		textfield = new JTextField("textfield");
		textfield.setSize(new Dimension(50, 30));
		textfield.setBackground(Color.WHITE);
		size = textfield.getPreferredSize();
		contentPane.add(textfield);
		textfield.setBounds(200, 20,
				size.width, size.height);
		colorable.addTo(textfield);
		
		// Add a canvas to the swing glasspane to draw the pie menu.
		setGlassPane(canvas);
		getGlassPane().setVisible(true);
		canvas.setOpaque(false);
		
		stateMachines();
		// Attach pie menu machine to this canvas
		smMenu.attachTo(canvas);
		// Attach smWidgets to the whole applet window
		// NOTE: We do it after having set glasspane otherwise glasspane 
		// would note be taken into account by smWidgets JStateMachine 
		// and it would have intercept all events.
		smWidgets.attachTo(this);
		// smMenu listens events fired by smMenu
		smMenu.addStateMachineListener(smWidgets);
	}
	
	void stateMachines() {
		smWidgets = new JStateMachine() {
			
			Component selected = null;
			
			public State start = new State() {
				Transition select = new PressOnTag("colorable", BUTTON3, ">> componentSelected") {
					public void action() {
						selected = getComponent();
					}
				};
			};
			
			public State componentSelected = new State() {
				
				Transition select = new Event("cancel", ">> start") { };
				
				Transition color = new Event(ColorEvent.class, ">> start") {
					public void action() {
						selected.setBackground(((ColorEvent)getEvent()).color);
					}
				};
				
			};
		};
		
		smMenu =  new CStateMachine() {
			
			public State start = new State("start") {
				Transition menu = new Press(BUTTON3, ">> menuOn") {
					public void action() {
						showMenu(getPoint());
					}
				};
			};
			
			public State menuOn = new State() {
				
				Color color = null;
				
				Transition enterMenuItem = new EnterOnTag(ColorTag.class) {
					public void action() {
						color = ((ColorTag)getTag()).color;
					}
				};
				
				// Enter on menu center
				Transition test = new EnterOnShape() {
					public void action() {
						color = null;
					}
				};
				
				Transition select = new Release(BUTTON3, ">> start") {
					public void action() {
						if(color != null) fireEvent(new ColorEvent(smMenu, color));
						else fireEvent(new VirtualEvent("cancel"));
					}
				};
				
				public void leave() {
					hideMenu();
				}
			};
			
		};
		
	}
	
}

