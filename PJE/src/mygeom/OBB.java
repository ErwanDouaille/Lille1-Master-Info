package mygeom;

import java.awt.Dimension;

public class OBB {
	private Vector2 vector;
	private double angle;
	private Dimension dimension;
	
	public OBB (Vector2 vector, double angle, Dimension dimension){
		this.angle=angle;
		this.vector=vector;
		this.dimension=dimension;
	}
 	
	public OBB() {
	}

	public Vector2 getVector() {
		return vector;
	}

	public void setVector(Vector2 vector) {
		this.vector = vector;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	public String toString(){
		return "angle:" + this.angle + " vecteur:" + this.vector + " dimension:" + this.dimension;
	}
	
	public void updatePosition(Vector2 vector, double angle, double k){
		if(this.dimension.getHeight()<60 & this.dimension.getWidth()<60 & k<1)
			System.out.println("Security, too small :P");
		else
			this.dimension = new Dimension(
				(int)(this.dimension.getWidth()*k), 
				(int)(this.dimension.getHeight()*k));
		this.angle += angle;
		this.vector.add(vector);		
	}
		
}
