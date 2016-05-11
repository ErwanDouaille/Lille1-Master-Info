/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * <p>
 * A widget shape for use with an <code>Canvas</code>.
 * Like any other canvas shape type, it can be rendered in a smcanvas and interaction 
 * with it can be described in a state machine.
 * It is built on any JComponent and, thus, can have its own listener.
 * </p>
 * 
 * <p>
 * WARNING: The underlying widget is repeatedly refreshed. You should use a <code>CWidget</code>
 * if your widget is a static widget such as a button.
 * </p>
 * 
 **/
public class CDynamicWidget extends CWidget implements Runnable {

	private int delayInMilliseconds;
	private boolean started = false;
	
	private Runnable updateImageThread = new Runnable() {
		public void run() {
			widget.printAll(biContext);
			repaint();
		}
	};
	
	/**
	 * Builds a <code>CDynamicWidget</code> based on a swing component.
	 * @param w The component.
	 * @param x The x coordinate of the upper left point of the bounding box.
	 * @param y The y coordinate of the upper left point of the bounding box.
	 * @param width The width of the bounding box.	
	 * @param height The height of the bounding box.
	 * @param delayInMilliseconds The refreshing rate
	 */
	public CDynamicWidget(JComponent w, double x, double y, double width, double height, int delayInMilliseconds) {
		super(w, x, y, width, height);
		this.delayInMilliseconds = delayInMilliseconds;
	}

	/**
	 * Builds a <code>CDynamicWidget</code> based on a swing component.
	 * @param w The component.
	 * @param x The x coordinate of the upper left point of the bounding box.
	 * @param y The y coordinate of the upper left point of the bounding box.
	 * @param delayInMilliseconds The refreshing rate
	 */
	public CDynamicWidget(JComponent w, double x, double y, int delayInMilliseconds) {
		super(w, x, y);
		this.delayInMilliseconds = delayInMilliseconds;
	}

	/**
	 * Builds a <code>CDynamicWidget</code> based on a swing component.
	 * @param w The component.
	 */
	public CDynamicWidget(JComponent w) {
		super(w);
	}
	
	/**
	 * @return The refreshing delay in milliseconds.
	 */
	public int getDelayInMilliseconds() {
		return delayInMilliseconds;
	}

	/**
	 * Sets the refreshing delay of this <code>CDynamicWidget</code>.
	 * @param delayInMilliseconds The delay in milliseconds
	 */
	public void setDelayInMilliseconds(int delayInMilliseconds) {
		this.delayInMilliseconds = delayInMilliseconds;
	}

	protected void init(JComponent w) {
		widget = w;
		Dimension size = w.getPreferredSize();
		shape = new Rectangle2D.Double(0, 0, size.getWidth(), size.getHeight());
		
		f.getContentPane().add(widget);
		f.pack();
//		widget.setVisible(true);
		
		widget.setBackground((Color)fillPaint);
		bi = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_ARGB );
		biContext = bi.createGraphics();
	}
	
	/**
	 * Requests a refresh of the <code>CWidget</code>.
	 * Actually, a <code>CWidget</code> is repainted only when
	 * an input event ocuurs on it.
	 * @return this <code>CWidget</code>.
	 */
	public CElement widgetChanged() {
		if(started) SwingUtilities.invokeLater(updateImageThread);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if(!started) {
			start();
			started = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		while(true) {
			try {
				Thread.sleep(delayInMilliseconds);
				SwingUtilities.invokeLater(updateImageThread);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Starts the thread that refreshes this <code>CDynamicWidget</code>.
	 */
	void start() {
		Thread th = new Thread(this);
		th.setPriority(Thread.MIN_PRIORITY);
		th.start();
	}
	
}
