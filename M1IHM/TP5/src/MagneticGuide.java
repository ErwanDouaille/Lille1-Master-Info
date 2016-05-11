import java.awt.geom.Point2D;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CSegment;
import fr.lri.swingstates.canvas.Canvas;


public class MagneticGuide extends CExtensionalTag{

	public CSegment segment;
	public boolean horizontal;
	
	public MagneticGuide(Canvas c){
		super(c);
	}

	public boolean isHorizontal(){
		return horizontal;
	}
	
	public boolean isVertical(){
		return !horizontal;
	}
	
	public void setHorizontal(){
		this.horizontal = true;
	}
	
	public void setVertical(){
		this.horizontal = false;
	}
	
	public void createHorizontalSegment(Point2D p, CExtensionalTag tag){
		this.segment = canvas.newSegment(
				p.getX()-canvas.getWidth(), 
				p.getY(), 
				p.getX()+canvas.getWidth(), 
				p.getY());			
		this.setHorizontal();
		this.segment.addTag(this);
		this.segment.addTag(tag);
	}
	
	public void createVerticalSegment(Point2D p, CExtensionalTag tag){
		this.segment = canvas.newSegment(
				p.getX(), 
				p.getY()-canvas.getHeight(), 
				p.getX(), 
				p.getY()+canvas.getHeight());	
		this.setVertical();
		this.segment.addTag(this);
		this.segment.addTag(tag);
	}

	public void removeSegment() {
		this.canvas.removeShape(this.segment);
	}
}
