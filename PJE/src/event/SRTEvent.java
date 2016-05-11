package event;

import java.util.EventObject;

import mygeom.Vector2;
import widget.MTComponent;

public class SRTEvent extends EventObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector2 translation;
	private double angle, scale;
	
	
	
	public SRTEvent(Object source) {
		super(source);
	} 

	public SRTEvent(MTComponent key, Vector2 v, double d, double e) {
		super(key);
		this.translation=v;
		this.angle=d;
		this.scale=e;
	}

	public Vector2 getTranslation() {
		return translation;
	}

	public void setTranslation(Vector2 translation) {
		this.translation = translation;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

}