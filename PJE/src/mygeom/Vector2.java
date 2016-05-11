package mygeom;

public class Vector2 extends Tuple2 {
	
	public Vector2() {
		super();
	}
	
	public Vector2(Vector2 a) {
		super(a);
	}
	
	public Vector2(double x,double y) {
		super(x,y);
	}
	
	public Vector2(Segment2 s){
		super(s.end.getX()-s.init.getX(),s.end.getY()-s.init.getY());
	}
	
	public void add(Vector2 v){
		this.setX(v.getX()+this.getX());
		this.setY(v.getY()+this.getY());
	}
	
	public double scalaire(Vector2 v){
		return this.getX()*v.getX()+this.getY()*v.getY();
	}
	
	public double norme(){
		return Math.sqrt(this.scalaire(this));
	}
	
	public void normaliser(){
		double norme=this.norme();
		if(this.getX()>0 & this.getY()<0)
			this.setY(this.getY()*-1);
		else if(this.getY()>0 & this.getX()<0)
			this.setX(this.getX()*-1);
		if(this.getX()!=0)
			this.setX(this.getX()/norme);
		if(this.getY()!=0)
			this.setY(this.getY()/norme);
	}
		
	public double determinant(Vector2 v){
		return this.getX()*v.getY()-v.getX()*this.getY();
	}
	
	public double angle(Vector2 v){
		this.normaliser();
		v.normaliser();
		return Math.acos(scalaire(v));
	}
}
