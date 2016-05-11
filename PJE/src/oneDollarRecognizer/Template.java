package oneDollarRecognizer;

/**
 * @author <a href="mailto:gery.casiez@lifl.fr">Gery Casiez</a>
 */

import java.util.ArrayList;
import java.util.Vector;

import mygeom.Point2;
import mygeom.Tuple2;

public class Template
{
	private String name;
	private Vector<Tuple2> points;
	
	public Template( String name, Vector<Tuple2> points)
	{
		this.name = name;
		this.points = points;
	}
	
	public void setPoints(Vector<Tuple2> points) {
		this.points = points;
	}

	public Vector<Tuple2> getPoints() {
		return points;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return this.getName() + " " + this.getPoints().toString();
	}

	public ArrayList<Point2> getArrayOfPoints() {
		ArrayList<Point2> list = new ArrayList<Point2>();
		for(int i = 0; i < this.points.size();i++){
			list.add(this.points.get(i).asPoint2());
		}
		return list;
	}
}
