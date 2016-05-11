package widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import javax.imageio.ImageIO;
import event.DiscreteEventListener;
import event.GestureEvent;
import event.GestureEventListener;
import event.SRTEvent;
import event.SRTEventListener;
import mygeom.Vector2;

public class MTPicture extends MTComponent{

	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	
	public MTPicture(String fileName) {
		this.setName(fileName);
		loadImage(fileName);
	}
	
	public void draw(Graphics g){
		if(this.img!=null){
			g.setColor(Color.white);
			g.drawRoundRect(-5, -5, super.getDimension().width+10, super.getDimension().height+10, 10, 10);
			g.fillRoundRect(-5, -5, super.getDimension().width+10, super.getDimension().height+10, 10, 10);
			g.setColor(Color.black);
			g.drawImage(this.img,0,0, super.getDimension().width, super.getDimension().height, this);

		}	
	}
	
	public void loadImage(String fileName){
		try {
			this.img = ImageIO.read(new File(fileName));
			Dimension size=new Dimension(this.img.getWidth(),this.img.getHeight());
			super.setSize(size);
			this.setPosition(new Vector2(0,0), 0, new Dimension(200,200));
		} catch (IOException e) {
			System.out.println(fileName);
			e.printStackTrace();
		}
	}
	
	public void defaultDiscreteEventListener(){
		DiscreteEventListener evt = new DiscreteEventListener() {
			public void gesturePerformed(EventObject e) {
				((MTComponent)e.getSource()).click();
			}
		};		
		this.addDiscreteEventListener(evt);
	}
	
	public void defaultSRTEventListener(){
		SRTEventListener srt = new SRTEventListener() {
			public void gesturePerformed(EventObject e) {
				((MTComponent)e.getSource()).updatePosition(
						((SRTEvent)e).getTranslation(), 
						((SRTEvent)e).getAngle(), 
						((SRTEvent)e).getScale());
			}
		};
		this.addSRTEventListener(srt);
	}
	
	public void defaultGestureEventListener(){
		GestureEventListener gest = new GestureEventListener() {
			public void gesturePerformed(EventObject fooEvent) {
				GestureEvent event = (GestureEvent)fooEvent;
				if(event.getDoigt()==3){
					((MTPicture)event.getSource()).execCmd(event.getTemplate());
				}
			}
		};
		this.addGestureEventListener(gest);
	}
	
	/**
	 * Gesture events
	 */

	public void delete(){
		this.setVisible(!this.isVisible());
	}
	
	public void circle(){
		//Nothing :)
	}
	
	public void rectangle(){
		//Nothing :)
	}
}