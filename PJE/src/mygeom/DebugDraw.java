package mygeom;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;


class DebugInfo<T> {
	public int width=1;
	public Color color=Color.black;
	public T element=null;
	public boolean init=false;
	public Color colorInit=Color.red;
	
	public DebugInfo(T e) {
		element=e;
	}
	
	public String toString() {
		return "Width="+width;
	}
}

class DebugList<T> extends ArrayList<DebugInfo<T>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}

public class DebugDraw {

	static DebugList<Point2> p;
	static DebugList<Segment2> s;
	public static boolean enabled = true;
	
	static public void init() {
		p=new DebugList<Point2>();
		s=new DebugList<Segment2>();
	}
	
	static public void toggleDisplay() {
		enabled = !enabled;
	}
	
	static public void draw(Graphics2D g) {
		if (enabled) {
			Stroke saveStroke=g.getStroke();
			for(DebugInfo<Point2> pp:p) {
				g.setStroke(new BasicStroke(pp.width));
				g.setColor(pp.color);
				g.fill(new Ellipse2D.Double(pp.element.getX()-5,pp.element.getY()-5,10,10));
			}
		
			for(DebugInfo<Segment2> ss:s) { 
				g.setStroke(new BasicStroke(ss.width));
				g.setColor(ss.color);
				g.draw(new Line2D.Double(ss.element.init().getX(),ss.element.init().getY(),ss.element.end().getX(),ss.element.end().getY()));
				if (ss.init) {
					g.setColor(ss.colorInit);
					g.fill(new Ellipse2D.Double(ss.element.init().getX()-5,ss.element.init().getY()-5,10,10));					
				}
			}
			
			g.setStroke(saveStroke);
		}
	}
	
	static public <T> void add(T t) {
		DebugInfo<T> info=new DebugInfo<T>(t);
		if (t.getClass()==Segment2.class) s.add((DebugInfo<Segment2>)info); else
		if (t.getClass()==Point2.class) p.add((DebugInfo<Point2>)info); 
		
	}
	
	static public <T> void add(T t,int width) {
		DebugInfo<T> info=new DebugInfo<T>(t);
		info.width=width;
		if (t.getClass()==Segment2.class) s.add((DebugInfo<Segment2>)info); else
		if (t.getClass()==Point2.class) p.add((DebugInfo<Point2>)info); 
		
	}

	static public <T> void add(T t,int width,Color c) {
		DebugInfo<T> info=new DebugInfo<T>(t);
		info.width=width;
		info.color=c;
		if (t.getClass()==Segment2.class) s.add((DebugInfo<Segment2>)info); else
		if (t.getClass()==Point2.class) p.add((DebugInfo<Point2>)info); 
	}
	
	static public <T> void add(T t,int width,Color c,Color cInit) {
		DebugInfo<T> info=new DebugInfo<T>(t);
		info.width=width;
		info.color=c;
		info.init=true;
		info.colorInit=cInit;
		if (t.getClass()==Segment2.class) s.add((DebugInfo<Segment2>)info); else
		if (t.getClass()==Point2.class) p.add((DebugInfo<Point2>)info); 
	}

	
	static public void clear() {
		p.clear();
		s.clear();
	}
	
	
}