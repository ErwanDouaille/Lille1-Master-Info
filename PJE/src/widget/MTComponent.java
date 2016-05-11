package widget;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.EventObject;
import javax.swing.JComponent;
import javax.swing.event.EventListenerList;
import oneDollarRecognizer.Template;
import event.DiscreteEvent;
import event.DiscreteEventListener;
import event.GestureEventListener;
import event.SRTEvent;
import event.SRTEventListener;
import mygeom.OBB;
import mygeom.Point2;
import mygeom.Vector2;

public class MTComponent extends JComponent {

	/**
	 * I would like to have MTComponent as an abstract class to define default behavior and selectors
	 * for gesture events. Well ... the code will be a bit crappy :)
	 */
	
	private static final long serialVersionUID = 1L;
	private EventListenerList eventListenerList = new EventListenerList();
	private MTContainer registerContainer;
	private OBB obb;
	
	public void registerContainer(MTContainer container){
		this.registerContainer = container;
	}
	
	public void draw(Graphics g){
	}
	
	public OBB getObb(){
		return this.obb;
	}

	public void setPosition(Vector2 vector, double angle, Dimension dimension) {
		this.obb = new OBB(vector, angle, dimension);
	}
	
	public void updatePosition(Vector2 vector, double angle, double k){
		this.obb.updatePosition(vector, angle, k);
	}

	public Dimension getDimension() {
		return this.obb.getDimension();
	}
	
	public boolean isInside(Point2 p){
		double centreCompX=this.obb.getDimension().getWidth()*Math.cos(-this.obb.getAngle())-this.obb.getDimension().getHeight()*Math.sin(-this.obb.getAngle());
		centreCompX/=2;
		centreCompX+=this.obb.getVector().getX();
		
		double centreCompY=this.obb.getDimension().getWidth()*Math.sin(-this.obb.getAngle())+this.obb.getDimension().getHeight()*Math.cos(-this.obb.getAngle());
		centreCompY/=2;
		centreCompY+=this.obb.getVector().getY();

		
		double x = Math.cos(this.obb.getAngle())*(p.getX()*Toolkit.getDefaultToolkit().getScreenSize().getWidth()-centreCompX ) - Math.sin(this.obb.getAngle())*(p.getY()*Toolkit.getDefaultToolkit().getScreenSize().getHeight()-centreCompY) + centreCompX;
		double y = Math.sin(this.obb.getAngle())*(p.getX()*Toolkit.getDefaultToolkit().getScreenSize().getWidth()- centreCompX) + Math.cos(this.obb.getAngle())*(p.getY()*Toolkit.getDefaultToolkit().getScreenSize().getHeight()-centreCompY) + centreCompY;
        p=new Point2(x, y);
		 
		if( (this.obb.getVector().getX() < p.getX()) && 
				(this.getDimension().getWidth()+this.obb.getVector().getX()> p.getX()) && 
				this.obb.getVector().getY() < p.getY() && 
				this.getDimension().getWidth()+this.obb.getVector().getY()> p.getY())
			return true;
		return false;
		
	}

	public void click(){
		this.registerContainer.select(this);
	}
	
	/**
	 * Add event listeners
	 * @param event
	 */
	
	public void addDiscreteEventListener(DiscreteEventListener event){
		this.eventListenerList.add(DiscreteEventListener.class, event);
	}
	
	public void addSRTEventListener(SRTEventListener event){
		this.eventListenerList.add(SRTEventListener.class, event);
	}
	
	public void addGestureEventListener(GestureEventListener event){
		this.eventListenerList.add(GestureEventListener.class, event);
	}
	
	/**
	 * Remove event listeners
	 * @param event
	 */
	
	public void removeDiscreteEventListener(DiscreteEventListener event){
		this.eventListenerList.remove(DiscreteEventListener.class, event);
	}
	
	public void removeSRTEventListener(SRTEventListener event){
		this.eventListenerList.remove(SRTEventListener.class, event);
	}
	
	public void removeGestureEventListener(GestureEventListener event){
		this.eventListenerList.remove(GestureEventListener.class, event);
	}
	
	
	/**
	 * FIRE events 
	 * @param event
	 */
	
	public void fireDiscretePerformed(EventObject event){
	    Object[] listeners = eventListenerList.getListenerList();
	     for (int i = 0; i<listeners.length; i++) {
	         if (listeners[i]==DiscreteEventListener.class) {	        	 
	        	 ((DiscreteEventListener)listeners[i+1]).gesturePerformed(new DiscreteEvent(this));
	         }
	     }		
	}
	
	public void fireSRTPerformed(EventObject event){
	   Object[] listeners = eventListenerList.getListenerList();
	     for (int i = 0; i<listeners.length; i++) {
	         if (listeners[i]==SRTEventListener.class) {
	        	 ((SRTEventListener)listeners[i+1]).gesturePerformed((SRTEvent)event);
	         }
	     }	
	}
	
	public void fireGesturePerformed(EventObject event){
		Object[] listeners = eventListenerList.getListenerList();
		 for (int i = 0; i<listeners.length; i++) {
		     if (listeners[i]==GestureEventListener.class) {	        	 
		    	 ((GestureEventListener)listeners[i+1]).gesturePerformed(event);
		     }
		 }		
	}
	
	
	/**
	 * Default behavior for gesture events
	 */
	
	public void execCmd(Template template){
		System.out.println(template.getName() + " exec");
		if(template.getName().equals("delete"))
			this.delete();	
		if(template.getName().equals("circle"))
			this.circle();	
		if(template.getName().equals("rectangle"))
			this.rectangle();	
	}
	
	public void delete(){
		System.out.println("super delete");
	}
	
	public void circle(){
		System.out.println("super circle");
	}
	
	public void rectangle(){
		System.out.println("super rectangle");
	}
	
	
}
