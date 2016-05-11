package mygeom;


public class Tuple2 {
	private double x,y;
	
	public Tuple2() {
		x=0;y=0;
	}

	public Tuple2(double xx,double yy) {
		x=xx;y=yy;
	}
	
	public Tuple2(Tuple2 a) {
		x=a.x;y=a.y;
	}
	
	
	public Tuple2 add(Tuple2 a) {
		x+=a.x;
		y+=a.y;
		return this;
	}

  public Tuple2 sub(Tuple2 a) {
  	x-=a.x;
   	y-=a.y;
   	return this;
  }

  public Tuple2 add(Tuple2 a,Tuple2 b) {
   	x=a.x+b.x;
   	y=a.y+b.y;
   	return this;
   }
    
   public void set(Tuple2 a) {
   	x=a.x;y=a.y;
   }
    
	public void set(Tuple2 a,Tuple2 b) {
		set(b);
		sub(a);
	}

	/// copy
    public void copy(Tuple2 a) {
    	a.x=x;a.y=y;
    }
    
    public double getX() {
    	return x;
    }
    
    public double getY() {
    	return y;
    }
    
    public void set(double xx,double yy) {
    	x=xx;
    	y=yy;
    }
    
    public void setX(double xx) {
    	x=xx;
    }
    
    public void setY(double yy) {
    	y=yy;
    }
    
    public Point2 asPoint2(){
    	return new Point2(this.getX(), this.getY());
    }
    
    /// @brief compatibilit� avec cout (affiche les coordonn�es).
    public String toString() {
    	return "("+x+","+y+")";
    }


}
