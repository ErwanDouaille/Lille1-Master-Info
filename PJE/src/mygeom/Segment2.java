package mygeom;

public class Segment2 {

	Point2 init,end;
	
	public Segment2(Point2 a,Point2 b) {
		init=a;
		end=b;
	}
		
	public Segment2(Point2 a,Vector2 b) {
		init=a;
		end=new Point2(a);
		end.add(b);
	}
	
	public Point2 init() {
		return init;
	}
	
	public Point2 end() {
		return end;
	}
	
	public void init(Point2 a) {
		init=a;
	}
	
	public void end(Point2 a) {
		end=a;
	}
	
	public void copy(Segment2 s) {
		init.set(s.init());
		end.set(s.end());
	}
	
	public static double computeAngle(Segment2 s1,Segment2 s2) {
		
		Vector2 u=new Vector2(s1);
		Vector2 v=new Vector2(s2);
		
		u.normaliser();
		v.normaliser();
		
		double ac=u.scalaire(v);
		if (ac<-1) ac=-1;
		if (ac>1) ac=1;
		double angle=Math.acos(ac);
		
		double det=u.determinant(v);
		if (det<0) angle=-angle;				

		return angle;
		
	}
	
	
	/// calcul du scale suivant les 2 segments
	public  static double computeScale(Segment2 s1,Segment2 s2) {
		Vector2 u=new Vector2(s1);
		Vector2 v=new Vector2(s2);
		double lu=u.norme();
		if (Math.abs(lu)<0.00001) return 1.0;  // TODO : pas terrible, mais correspond � l'identit�.
		else  return v.norme()/u.norme();
	}
	
	public String toString() {
		return "["+init+","+end+"]";
	}

}