package mygeom;

import java.util.ArrayList;
import java.util.Iterator;

public class InertiaMatrix {

	private ArrayList<Point2> lesPoints;
	
	
	public InertiaMatrix(){
		this.lesPoints=new ArrayList<Point2>();
	}
	
	public void addPoints( ArrayList<Point2> liste){
		Iterator<Point2> it=liste.iterator();
		while(it.hasNext()){
			this.lesPoints.add(it.next());
		}
	}
	
	public OBB getOBB(){
		this.resample();
		//a modifier
		return new OBB();
	}
	
	public double pathLength() {
        double sumOfdistance = 0;
        for(int i=1; i<this.lesPoints.size(); i++){
            Point2 p1 = this.lesPoints.get(i-1);
            Point2 p2 = this.lesPoints.get(i);
            sumOfdistance += p1.distanceTo(p2);
        }
        return sumOfdistance;
    }
	
	public ArrayList<Point2> resample() {
    	double I = this.pathLength()/((double)(63));
        double D = 0;
        ArrayList<Point2> newPoints = new ArrayList<Point2>();
        newPoints.add(this.lesPoints.get(0));
        int i = 1;
        while(i < this.lesPoints.size()){
            Point2 p1 = this.lesPoints.get(i-1);
            Point2 p2 = this.lesPoints.get(i);
            double d = p1.distanceTo(p2);
            if((D+d) >= I){
                double qx = p1.getX() + ((I-D)/d) * (p2.getX()-p1.getX());
                double qy = p1.getY() + ((I-D)/d) * (p2.getY()-p1.getY());
                Point2 q = new Point2(qx, qy);
                newPoints.add(q);
                this.lesPoints.add(i, q);
                D = 0.0;
            } else 
                D += d;
            i++;
        }
        if(newPoints.size() == 63){
            newPoints.add(this.lesPoints.get(this.lesPoints.size()-1));
        }
        return newPoints;
    }
	
	public Point2 centroid() {
        double cx = 0;
        double cy = 0;
        double countOfPoints = (double)this.lesPoints.size();
        for(int i=0; i<this.lesPoints.size(); i++){
            cx += this.lesPoints.get(i).getX();
            cy += this.lesPoints.get(i).getY();
        }
        return new Point2(cx/countOfPoints, cy/countOfPoints);
    }
	
	private double calculCoefA(){
		double y,res=0;
		Point2 centroid=this.centroid();
		for(int i=0; i<this.lesPoints.size(); i++){
			y=this.lesPoints.get(i).getY()-centroid.getY();
			res+=y*y;
		}
		return res;
	}
	
	private double calculCoefB(){
		double x,res=0;
		Point2 centroid=this.centroid();
		for(int i=0; i<this.lesPoints.size(); i++){
			x=this.lesPoints.get(i).getX()-centroid.getX();
			res+=x*x;
		}
		return res;
	}
	
	private double calculCoefF(){
		double x,y,res=0;
		Point2 centroid=this.centroid();
		for(int i=0; i<this.lesPoints.size(); i++){
			x=this.lesPoints.get(i).getX()-centroid.getX();
			y=this.lesPoints.get(i).getY()-centroid.getY();
			res+=x*y;
		}
		return res;
	}
	
	private double[] valeursPropres(){
		double[] res=new double[2];
		double A=this.calculCoefA();
		double B=this.calculCoefB();
		double F=this.calculCoefF();
		double sqrt=(A-B)*(A-B)+4*F*F;
		sqrt=Math.sqrt(sqrt);
		res[0]=(A+B+sqrt)/2;
		res[0]=(A+B-sqrt)/2;
		return res;
	}
	
	private Vector2[] vecteursPropres(){
		Vector2[] res=new Vector2[2];
		double[] val=this.valeursPropres();
		double A=this.calculCoefA();
		double F=this.calculCoefF();
		for(int i=0;i<2;i++){
			res[0]=new Vector2(-F, -A+val[0]);
		}
		return res;
	}
	
}
