/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
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
 * WARNING: 
 * <ol>
 * <li>The underlying widget is refreshed each time it receives an input event.</li>
 * <li>When the underlying widget dynamically creates other widgets (such as a <code>JComboBox</code> which
 * brings up a <code>JPopupMenu</code>, the behaviour can not be satisfying.</li>
 * </ol>
 * </p>
 * 
 * This following code makes a button "ok" at location (10, 10) rotated by pi in a smcanvas. 
 * <pre>
 * JButton ok = new JButton("ok");
 * CShape smw = canvas.newWidget(ok, 100, 100, 50, 15).rotateBy(Math.PI/2);
 * </pre>
 * 
 * Now, let's see how to manage interaction with the button <code>ok</code>:
 * <ol>
 * <li>
 * <pre>
 * ok.addActionListener(new ActionListener() {
 * 		public void actionPerformed(ActionEvent e) {
 * 			System.out.println("OK");
 * 		}
 * });
 * </pre>
 * Clicking on the button ok causes the message "OK" written on standard output.
 * <li>
 * <pre>
 * StateMachine sm = new StateMachine() {
 * 		public State colorButton = new State() {
 * 			Transition click = new ClickOnShape(BUTTON1) { 
 * 				public void action(){
 * 					getShape().setFillPaint(Color.RED);
 * 				}
 * 			};
 * 		};
 * };
 * canvas.attach(sm);
 * </pre>
 * Clicking on the button ok fills it in red AND causes the message "OK" written on standard output.
 * <li>
 * <pre>
 * ((CWidget)smw).setBasicListener(false);
 * </pre>
 * Clicking on the button just fills it in red.
 * <li>
 * <pre>
 * smw.setPickable(false);
 * </pre>
 * Clicking on the button does anything.
 * <li>
 * <pre>
 * ((CWidget)smw).setBasicListener(true);
 * </pre>
 * Clicking on the button just causes the message "OK" written on standard output.
 * </ol>
 * 
 * @author Caroline Appert
 */
public class CWidget extends CShape {
	
	protected static JFrame f = new JFrame();
	
	protected volatile BufferedImage bi;
	protected volatile Graphics2D biContext;
	
	protected JComponent widget;
	private boolean basicListener = true;
	
	static {
		f.getContentPane().setLayout(new FlowLayout());
		f.setState(JFrame.ICONIFIED);
	}
	
	private CWidget() {
		super();
	}
	
	/**
	 * Builds a <code>CWidget</code> based on a swing component.
	 * @param w The component.
	 */
	public CWidget(JComponent w) {
		super();
		init(w, 0, 0);
	}
	
	/**
	 * Builds a <code>CWidget</code> based on a swing component.
	 * @param w The component.
	 * @param x The x coordinate of the upper left point of the bounding box.
	 * @param y The y coordinate of the upper left point of the bounding box.
	 */
	public CWidget(JComponent w, double x, double y) {
		super();
		init(w, x, y);
	}
	
	/**
	 * Builds a <code>CWidget</code> based on a swing component.
	 * @param w The component.
	 * @param x The x coordinate of the upper left point of the bounding box.
	 * @param y The y coordinate of the upper left point of the bounding box.
	 * @param width The width of the bounding box.	
	 * @param height The height of the bounding box.
	 */
	public CWidget(JComponent w, double x, double y, double width, double height) {
		super();
		w.setFocusable(false);
		w.setPreferredSize(new Dimension((int)width, (int)height));
		init(w, x, y);
		
	}
	
	protected void init(JComponent w, double x, double y) {
		widget = w;
		Dimension size = w.getPreferredSize();
		shape = new Rectangle2D.Double(x, y, size.getWidth(), size.getHeight());
		f.getContentPane().add(widget);
		f.pack();
//		f.setVisible(true);
		widget.setBackground((Color)fillPaint);
		bi = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_ARGB );
		biContext = bi.createGraphics();
//		absShape = null;
		widgetChanged();
	}
	
	/**
	 * Requests a refresh of the <code>CWidget</code>.
	 * Actually, a <code>CWidget</code> is repainted only when
	 * an input event ocuurs on it.
	 * @return this <code>CWidget</code>.
	 */
	public CElement widgetChanged() {
		widget.printAll(biContext);
		repaint();
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CElement setFillPaint(Paint fp) {
		fillPaint = fp;
		widget.setBackground((Color)fp);
		widgetChanged();
		return this;
	}

	
	CWidget sendEvent(Point2D point2D, MouseEvent evt) {
		if(!widget.isEnabled()) return this;
		try {
			Point2D pt = canvasToShape(point2D);
			Component deepest = SwingUtilities.getDeepestComponentAt(widget, (int)pt.getX(), (int)pt.getY());
			if(deepest == null) {
				MouseEvent newEvent = new MouseEvent(widget, evt.getID(), evt.getWhen(), evt.getModifiers(), (int)pt.getX(), (int)pt.getY(), evt.getClickCount(), evt.isPopupTrigger());
				widget.dispatchEvent(newEvent);
			} else {
				Point pt2 = SwingUtilities.convertPoint(widget, (int)pt.getX(), (int)pt.getY(), deepest);
				MouseEvent newEvent = new MouseEvent(deepest, evt.getID(), evt.getWhen(), evt.getModifiers(), (int)pt2.getX(), (int)pt2.getY(), evt.getClickCount(), evt.isPopupTrigger());
				deepest.dispatchEvent(newEvent);
			}
			widgetChanged();
		} catch (Exception e2) {
			// e2.printStackTrace();
			// Exception: component must be showing on the screen to determine its location...
			// We ignore it.
		}
		return this;
	}
	
	CWidget sendEvent(KeyEvent evt) {
		if(!widget.isEnabled()) return this;
		widget.requestFocus();
		widget.dispatchEvent(evt);
		widgetChanged();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		Shape saveClip = g2d.getClip();
		AffineTransform saveTransform = g2d.getTransform();
		Composite saveComposite = g2d.getComposite();
		RenderingHints saveRenderingHints = g2d.getRenderingHints();
		if (renderingHints != null) {
			g2d.addRenderingHints(renderingHints);
		}
		if (clip != null && (canvas != null && clip != canvas.clip)) {
			if (clip == DEFAULT_CLIP) {
				g2d.setClip(0, 0, canvas.getWidth(), canvas.getHeight());
			} else {
				g2d.transform(clip.getAbsTransform());
				g2d.setClip(clip.getShape());
				g2d.setTransform(saveTransform);
			}
		}
		
		if(filled || outlined) {
			CShape s = parent;
			AffineTransform xform = new AffineTransform();
			while (s != null) {
				s.getAbsShape();
				xform.preConcatenate (s.transform);
				s = s.parent;
			}
			g2d.transform(xform);
			Rectangle2D bounds = shape.getBounds2D();
			double dx = bounds.getWidth()*rx;
			double dy = bounds.getHeight()*ry;
			dx += bounds.getX();
			dy += bounds.getY();
			g2d.translate(tx+dx, ty+dy);
			g2d.rotate (theta);
			g2d.scale (sx, sy); 
			g2d.translate (-bounds.getWidth()*rx, -bounds.getHeight()*ry);
			if(filled) {
				if (transparencyFill != null)
					g2d.setComposite(transparencyFill);
				g2d.drawImage(bi, 0, 0, canvas);
				g2d.setComposite(saveComposite);
			}
			g2d.setTransform(saveTransform);
			if(outlined) {
				g2d.setPaint(outlinePaint);
				g2d.setStroke(stroke);
				g2d.transform(getAbsTransform());
				if(transparencyOutline != null) g2d.setComposite(transparencyOutline);
				g2d.setPaint(outlinePaint);
				g2d.draw(shape);
				g2d.setTransform(saveTransform);
				g2d.setComposite(saveComposite);
			}
			g2d.setClip(saveClip);
			g2d.setRenderingHints(saveRenderingHints);
		}
	}

	/**
	 * Tests if this smwidget's listeners are or are not notified by input events.
	 * @return true if this smwidget's listeners are notified by input events, false, if they are not.
	 */
	public boolean isBasicListener() {
		return basicListener;
	}

	/**
	 * Sets this smwidget's listeners be notified or not by input events. 
	 * @param basicListener True, if this smwidget's listeners must be notified by input events, false, if not.
	 */
	public void setBasicListener(boolean basicListener) {
		widget.setEnabled(basicListener);
		this.basicListener = basicListener;
	}

	/**
	 * Copies this widget into a destination widget.
	 * @param sms The destination shape
	 * @return this shape
	 * @see CShape#copyTo
	 */
	public CShape copyTo (CShape sms) {
		super.copyTo(sms);
		if (sms instanceof CWidget) {
			CWidget smi = (CWidget) sms;
			smi.widget = widget;
			smi.basicListener = basicListener;
			smi.init(widget, shape.getBounds2D().getMinX(), shape.getBounds2D().getMinY());
		}
		return this;
	}

	/**
	 * Creates a new copy of this shape and returns it.
	 * @return the copy.
	 */
	public CShape duplicate() {
		CWidget sms = new CWidget();
		copyTo(sms);
		return sms;
	}

	/**
	 * @return The underlying <code>JComponent</code> widget.
	 */
	public JComponent getWidget() {
		return widget;
	}

}