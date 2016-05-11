package widget;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import mygeom.Point2;
import mygeom.Vector2;

public class MTContainer extends MTComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<MTComponent> components = new ArrayList<MTComponent>();
	private MTSurface surface;
	
	public MTContainer(MTSurface mtSurface) {
		this.surface=mtSurface;
	}

	public void draw(Graphics2D g){
		for(MTComponent mc : this.components){
			AffineTransform save=g.getTransform();
			g.translate(mc.getObb().getVector().getX(), mc.getObb().getVector().getY());
			g.rotate(mc.getObb().getAngle());
			if(mc.isVisible())
				mc.draw(g);
			g.setTransform(save);
		}
	}

	public MTComponent whichIs(Point2 p){	
		for (int i = this.components.size()-1; i >= 0; i--) {
			if(this.components.get(i).isInside(p))
				return this.components.get(i);	
		}
		return this;
	}
		
	public void add(MTComponent component){
		this.components.add(component);
	}
	
	public void select(MTComponent comp){
		this.components.remove(comp);
		this.components.add(comp);
	}
	

	/**
	 * Gesture events
	 */
	
	public void delete(){
		//Nothing :)
	}
	
	public void circle(){
		for (MTComponent components : this.components) {
			components.setPosition(
					new Vector2(0, 0),
					0, 
					new Dimension(200, 200));
			components.setVisible(true);	
		}
	}
	
	public void rectangle(){
		JFileChooser dialogue = new JFileChooser(new File("."));
		File fichier;
		if (dialogue.showOpenDialog(null)== 
		    JFileChooser.APPROVE_OPTION) {
		    fichier = dialogue.getSelectedFile();
		    try {
			    if(fichier.getPath()!=null){
			    	MTPicture pic = new MTPicture(fichier.getPath());
			    	pic.defaultDiscreteEventListener();
			    	pic.defaultSRTEventListener();
			    	pic.defaultGestureEventListener();
			    	this.surface.addComponent(pic);
			    }
			} catch (Exception e) {
				System.out.println("Cannot open this kind of file :s");
			}
		}
	}
	

}
