package mygeom;

public class Point2 extends Tuple2 {

		public Point2() {
			super();
		}
	
		public Point2(Point2 a) {
			super(a);
		}
		
		public Point2(Point2 a,Point2 b) {
			super(b);
			this.sub(a);
		}
		
		public Point2(double x,double y) {
			super(x,y);
		}
		
		public double distanceTo(Point2 b){
            double dx = b.getX() - this.getX();
            double dy = b.getY() - this.getY();
            return Math.sqrt(dx*dx + dy*dy);
        }
		
		public Vector2 asVector2(){
			return new Vector2(this.getX(), this.getY());
		}
				
}
