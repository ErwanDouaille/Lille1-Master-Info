package mygeom;

import java.util.ArrayList;

public class BoundingBox {

        public Point2 min;
        public Point2 max;
        public double width;
        public double height;
        
        public BoundingBox(ArrayList<Point2> points){
                min = new Point2(250, 250);
                max = new Point2(0,0);
                compute(points);
        }
        
        public void compute(ArrayList<Point2> points){
	        for (Point2 point : points) {
	            if(point.getX() < min.getX()) 
	            	min.setX(point.getX());
	            if(point.getY() < min.getY()) 
	            	min.setY(point.getY());
	            if(point.getX() > max.getX()) 
	            	max.setX(point.getX());
	            if(point.getY() > max.getY()) 
	            	max.setY(point.getY()); 
	        }
	        width = max.getX()-min.getX();
	        height = max.getY()-min.getY();
        }
}