package oneDollarRecognizer;

import java.util.ArrayList;
import java.util.Vector;
import widget.MTComponent;
import event.GestureEvent;
import mygeom.BoundingBox;
import mygeom.Point2;

public class OneDollarRecognizer {
	
	public double pathLength(ArrayList<Point2> points) {
        double sumOfdistance = 0;
        for(int i=1; i<points.size(); i++){
            Point2 p1 = points.get(i-1);
            Point2 p2 = points.get(i);
            sumOfdistance += p1.distanceTo(p2);
        }
        return sumOfdistance;
    }
	
    public ArrayList<Point2> resample(ArrayList<Point2> points, int N) {
    	double I = this.pathLength(points)/((double)(N-1));
        double D = 0;
        ArrayList<Point2> newPoints = new ArrayList<Point2>();
        newPoints.add(points.get(0));
        int i = 1;
        while(i < points.size()){
            Point2 p1 = points.get(i-1);
            Point2 p2 = points.get(i);
            double d = p1.distanceTo(p2);
            if((D+d) >= I){
                double qx = p1.getX() + ((I-D)/d) * (p2.getX()-p1.getX());
                double qy = p1.getY() + ((I-D)/d) * (p2.getY()-p1.getY());
                Point2 q = new Point2(qx, qy);
                newPoints.add(q);
                points.add(i, q);
                D = 0.0;
            } else 
                D += d;
            i++;
        }
        if(newPoints.size() == N-1){
            newPoints.add(points.get(points.size()-1));
        }
        return newPoints;
    }
    
    public Point2 centroid(ArrayList<Point2> points) {
        double cx = 0;
        double cy = 0;
        double countOfPoints = (double)points.size();
        for(int i=0; i<points.size(); i++){
            cx += points.get(i).getX();
            cy += points.get(i).getY();
        }
        return new Point2(cx/countOfPoints, cy/countOfPoints);
    }
    
    public ArrayList<Point2> rotateToZero(ArrayList<Point2> points) {
        Point2 c = this.centroid(points);
        double theta = Math.atan2(c.getY()-points.get(0).getY(), c.getX()-points.get(0).getX());
        
        return this.rotateBy(points, -theta);
    }
    
    public ArrayList<Point2> rotateBy(ArrayList<Point2> points, double d) {
        Point2 c = this.centroid(points);
        ArrayList<Point2> newPoints = new ArrayList<Point2>();
        for(int i=0; i<points.size(); i++){
            double qx = (points.get(i).getX() - c.getX())*Math.cos(d) - (points.get(i).getY() - c.getY())*Math.sin(d) + c.getX();
            double qy = (points.get(i).getX() - c.getX())*Math.sin(d) + (points.get(i).getY() - c.getY())*Math.cos(d) + c.getY();
            newPoints.add(new Point2(qx, qy));
        }
        return newPoints;
    }

    public ArrayList<Point2> scaleToSquare(ArrayList<Point2> points, double squareSize) {
		BoundingBox boundingBox = new BoundingBox(points);
		ArrayList<Point2> newPoints = new ArrayList<Point2>();
		for(int i=0; i<points.size(); i++){
	        double qx = points.get(i).getX()*(squareSize/boundingBox.width);
	        double qy = points.get(i).getY()*(squareSize/boundingBox.height);
	        newPoints.add(new Point2(qx, qy));
		}
		return newPoints;
    }
    
    public ArrayList<Point2> translateToOrigin(ArrayList<Point2> points) {
        Point2 c = this.centroid(points);
        ArrayList<Point2> newPoints = new ArrayList<Point2>();
        for(int i=0; i<points.size(); i++){
            double qx = points.get(i).getX() - c.getX();
            double qy = points.get(i).getY() - c.getY();
            newPoints.add(new Point2(qx, qy));
        }
        return newPoints;
    }
        
    public double distanceAtBestAngle(ArrayList<Point2> points, Template template, double f, double theta, double dTheta) { 
    	double phi = 0.5*(Math.sqrt(5.0) - 1.0);
    	double x1 = phi*f + (1-phi)*theta;
        double f1 = this.distanceAtBestAngle(points, template, x1);
        double x2 = (1-phi)*f + phi*theta;
        double f2 = this.distanceAtBestAngle(points, template, x2);
        while((theta-f) > dTheta){
            if(f1 < f2){
                theta = x2;
                x2 = x1;
                f2 = f1;
                x1 = phi*f + (1-phi)*theta;
                f1 = this.distanceAtBestAngle(points, template, x1);
            } else {
                f = x1;
                x1 = x2;
                f1 = f2;
                x2 = (1-phi)*f + phi*theta;
                f2 = this.distanceAtBestAngle(points, template, x2);
            }
        }
        return Math.min(f1, f2);
    } 

	public double distanceAtBestAngle(ArrayList<Point2> points, Template t, double theta) {
	    return this.pathDistance(initResampleFor(points, theta), initResampleFor(t.getArrayOfPoints(), theta));
	}
	
	public double pathDistance(ArrayList<Point2> listA, ArrayList<Point2> listB) {
        double totalDistance = 0.0;
        for(int i = 0; i < listA.size(); i++){
            Point2 currentPointFromA = listA.get(i);
            Point2 currentPointFromB = listB.get(i);
            totalDistance += currentPointFromA.distanceTo(currentPointFromB);
        }
        return (totalDistance/(double)listB.size());
	}
	
	public GestureEvent recognize(MTComponent key, ArrayList<Point2> points, Vector<Template> templates){
        double b = 99999;
        Template bestOne = null;
        for(int i=0; i < templates.size(); i++){
            double d = this.distanceAtBestAngle(
				points, 
				templates.get(i), 
				-45, 
				45, 
				2);
            if(d < b){
                b = d;
                bestOne = templates.get(i);
            }
        }
        double score = 1-b/0.5*Math.sqrt((Math.pow(250,2)+Math.pow(250,2)));
        return new GestureEvent(key, bestOne, score);
    }
	
	public ArrayList<Point2> initResampleFor(ArrayList<Point2> list, double theta){
		ArrayList<Point2> newPoints = this.resample(list, 64);
		newPoints = this.rotateBy(newPoints, theta);	
		newPoints = this.scaleToSquare(newPoints, 250);
		newPoints = this.translateToOrigin(newPoints);
		return newPoints;
	}

}
