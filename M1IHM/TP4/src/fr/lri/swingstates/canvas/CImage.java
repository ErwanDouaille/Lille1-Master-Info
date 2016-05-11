/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
*/
package fr.lri.swingstates.canvas;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;


/**
 * An image shape for use with a <code>Canvas</code>.
 * The image is loaded from a file and can be in any format recognized by Java2D.
 * The fill paint is ignored. 
 * If the shape is outlined, the border of the image is drawn according to the outline paint and stroke.
 * 
 * @author Caroline Appert
 */
public class CImage extends CShape {

	private BufferedImage image = null;

	/**
	 * Builds an empty CImage.
	 */
	private CImage() {
		super();
		outlined = false;
		outlinePaint = null;
	}

	/**
	 * Builds a CImage.
	 * @param imageFile The name of the file image (gif, jpeg or png)
	 * @param loc The location of the lower left corner of the image
	 */
	public CImage(String imageFile, Point2D loc) {
		super(null);
		try {
			FileInputStream fis = new FileInputStream(imageFile);
			try {
				image = ImageIO.read(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
			init(loc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Builds a CImage.
	 * @param imageURL The url where to read the image
	 * @param loc The location of the lower left corner of the image
	 */
	public CImage(URL imageURL, Point2D loc) {
		super(null);
		try {
			image = ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		init(loc);
	}
	
	/**
	 * Builds a CImage.
	 * @param imageStream The stream where to read the image
	 * @param loc The location of the lower left corner of the image
	 */
	public CImage(InputStream imageStream, Point2D loc) {
		super(null);
		try {
			image = ImageIO.read(imageStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		init(loc);
	}
	
	private void init(Point2D loc){
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		shape = new Rectangle2D.Double(loc.getX(), loc.getY(), w, h);
		changedTransform();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		Shape saveClip = g2d.getClip();
		AffineTransform saveTransform = g2d.getTransform();
		RenderingHints saveRenderingHints = g2d.getRenderingHints();
		Composite saveComposite = g2d.getComposite();
		
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
				g2d.drawImage(image, 0, 0, canvas);
				g2d.setComposite(saveComposite);
			}
			g2d.setTransform(saveTransform);
			if(outlined) {
				g2d.setStroke(stroke);
				g2d.transform(getAbsTransform());
				if(transparencyOutline != null) g2d.setComposite(transparencyOutline);
				g2d.setPaint(outlinePaint);
				g2d.draw(shape);
				g2d.setTransform(saveTransform);
				g2d.setComposite(saveComposite);
			}
		}
		g2d.setClip(saveClip);
		g2d.setRenderingHints(saveRenderingHints);
	}
	
	/**
	 * Copies this shape into a destination shape.
	 * @param sms The destination shape
	 * @return this shape
	 * @see CShape#copyTo
	 */
	public CShape copyTo (CShape sms) {
		super.copyTo(sms);
		if (sms instanceof CImage) {
			CImage smi = (CImage) sms;
			WritableRaster raster = image.copyData(null);
			smi.image = new BufferedImage(image.getColorModel(), raster, image.isAlphaPremultiplied(), null);
		}
		return this;
	}
	
	/**
	 * Creates a new copy of this shape and returns it.
	 * @return the copy.
	 */
	public CShape duplicate() {
		CImage sms = new CImage();
		copyTo(sms);
		return sms;
	}

}
