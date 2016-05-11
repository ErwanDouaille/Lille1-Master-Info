package mygeom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Path {

	private int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	private int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	
	public ArrayList<Point2> liste = new ArrayList<Point2>();

	public ArrayList<Point2> getListe(){
		return this.liste;
	}
	
	public void add(Point2 p){
		this.liste.add(p);		
	}
	
	public void clear(){
		this.liste = new ArrayList<Point2>();
	}
	
	public void draw(Graphics2D g){	
		this.draw(g, "");
	}
	
	public Point2 isobarycentre(){
		@SuppressWarnings("unchecked")
		ArrayList<Point2> liste = ((ArrayList<Point2>) this.liste.clone());
		double x = 0,y=0;
		for (Point2 point : liste) {
			x+=point.getX()*width;
			y+=point.getY()*height;	
		}
		return new Point2(x/this.liste.size(), y/this.liste.size());
	}
	
	public void drawIsobarycentre(Graphics2D g){
		Point2 p = this.isobarycentre();
		g.draw(new Line2D.Double(p.getX(), p.getY(),p.getX(), p.getY()));
	}
	
	public void draw(Graphics2D g, String id){	
		this.draw(g, id, true);
	}

	public void draw(Graphics2D g, String id, boolean bool) {
		g.setColor(Color.green);
		@SuppressWarnings("unchecked")
		ArrayList<Point2> liste = (ArrayList<Point2>) this.liste.clone();
		GeneralPath gp=new GeneralPath();
		if (liste.size()<1) 
			return;
		gp.moveTo(liste.get(0).getX()*width, liste.get(0).getY()*height);
		for (Point2 point : liste)
			gp.lineTo(point.getX()*width, point.getY()*height);
		Graphics2D g2 = (Graphics2D) g;
		Point2 last = liste.get(liste.size()-1);
		g2.drawString(id.toString(),  (int)(last.getX()*width-5), (int)(last.getY()*height-5));
		g.draw(new Ellipse2D.Double(last.getX()*width-5, last.getY()*height-5,10,10));	
		this.drawIsobarycentre(g);
		g.draw(gp);	
		//Well, set it by default
		g.setColor(Color.black);	
	}
	
	
}
