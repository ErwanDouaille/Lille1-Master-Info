import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas ;
import fr.lri.swingstates.canvas.CShape ;
import fr.lri.swingstates.canvas.CRectangle ;
import fr.lri.swingstates.canvas.CSegment ;
import fr.lri.swingstates.canvas.CExtensionalTag ;
import fr.lri.swingstates.canvas.transitions.* ;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.*;

import java.awt.Color ;
import java.awt.geom.Point2D ;
import javax.swing.JFrame ;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
public class MagneticGuides extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Canvas canvas ;
	private CExtensionalTag oTag ;

	public MagneticGuides(String title, int width, int height) {
		super(title) ;
		canvas = new Canvas(width, height) ;
		canvas.setAntialiased(true) ;
		getContentPane().add(canvas) ;

		oTag = new CExtensionalTag(canvas) {};

		CStateMachine sm = new CStateMachine() {

			private Point2D p ;
			private CShape draggedShape ;

			@SuppressWarnings("unused")
			public State start = new State() {
				Transition pressOnObject = new PressOnTag(oTag, BUTTON1, ">> oDrag") {
					public void action() {
						p = getPoint() ;
						draggedShape = getShape() ;
					}
				} ;
				Transition createHorizontal = new Press( BUTTON1) {
					public void action() {
						MagneticGuide m = new MagneticGuide(canvas){};
						m.createHorizontalSegment(getPoint(), oTag);
					}
				} ;
				Transition deleteSegment = new PressOnShape(BUTTON2) {
					public void action() {
						if(CSegment.class.isInstance(getShape())){
							for (CExtensionalTag tag : getShape().getTags()) {
								if(MagneticGuide.class.isInstance(tag))
									((MagneticGuide) tag).removeSegment();							
							} 
						}
					}
				};
				Transition createVertical = new Press( BUTTON3) {
					public void action() {
						MagneticGuide m = new MagneticGuide(canvas){};
						m.createVerticalSegment(getPoint(), oTag);
					}
				} ;
				Transition hideAllSegment = new Press( BUTTON2) {
					public void action() {
						for (CShape element : canvas.getDisplayList()) {
							if(CSegment.class.isInstance(element))
								element.setDrawable(!element.isDrawable());
						} 
					}
				} ;
			} ;

			@SuppressWarnings("unused")
			public State oDrag = new State() {
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						Point2D q = getPoint() ;
						if(CSegment.class.isInstance(draggedShape)){
							for (CShape rectangle : canvas.getDisplayList()) {
								if(CRectangle.class.isInstance(rectangle))
									for (CExtensionalTag tag : draggedShape.getTags()) {
										if(MagneticGuide.class.isInstance(tag))
											if(rectangle.hasTag(tag))
												if(((MagneticGuide) tag).isHorizontal())
													rectangle.translateBy(0, q.getY() - p.getY()) ;	
												else
													rectangle.translateBy(q.getX() - p.getX(), 0) ;	
									}
							}			
						} else {
							for (CShape line : canvas.getDisplayList()) {
								if(CSegment.class.isInstance(line))
									if((line.getCenterY()+2>=draggedShape.getCenterY() && line.getCenterY()-2<=draggedShape.getCenterY()) || 
											(line.getCenterX()+2>=draggedShape.getCenterX() && line.getCenterX()-2<=draggedShape.getCenterX())){
										for (CExtensionalTag tag : line.getTags()) {
											if(MagneticGuide.class.isInstance(tag)){
												draggedShape.addTag(tag);			
											}
										}
									}
							}
						}
						draggedShape.translateBy(q.getX() - p.getX(), q.getY() - p.getY()) ;
						p = q ;
					}
				} ;
				Transition release = new Release(BUTTON1, ">> start") {} ;
			} ;
		} ;
		sm.attachTo(canvas);

		pack() ;
		setVisible(true) ;
		canvas.requestFocusInWindow() ;
	}

	public void populate() {
		int width = canvas.getWidth() ;
		int height = canvas.getHeight() ;

		double s = (Math.random()/2.0+0.5)*30.0 ;
		double x = s + Math.random()*(width-2*s) ;
		double y = s + Math.random()*(height-2*s) ;

		int red = (int)((0.8+Math.random()*0.2)*255) ;
		int green = (int)((0.8+Math.random()*0.2)*255) ;
		int blue = (int)((0.8+Math.random()*0.2)*255) ;

		CRectangle r = canvas.newRectangle(x,y,s,s) ;
		r.setFillPaint(new Color(red, green, blue)) ;
		r.addTag(oTag) ;
	}

	public static void main(String[] args) {
		MagneticGuides guides = new MagneticGuides("Magnetic guides",600,600) ;
		for (int i=0; i<20; ++i) guides.populate() ;
		guides.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
	}

}