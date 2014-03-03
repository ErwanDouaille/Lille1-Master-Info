import java.util.ArrayList;


public class Clause {

	private ArrayList<Littoral> lesLittoraux;
	
	public Clause(){
		this.lesLittoraux=new ArrayList<Littoral>();
	}
	
	public void addLittoral(Littoral l){
		this.lesLittoraux.add(l);
	}
	
	public ArrayList<Littoral> getLesLittoraux(){
		return this.lesLittoraux;
	}
	
	public String toString(){
		String tmp = "(";
		for (int i = 0; i < this.lesLittoraux.size(); i++) {
			if (i+1==this.lesLittoraux.size()) {
				tmp = tmp + " " + this.lesLittoraux.get(i) + ")";
			} else {
				tmp = tmp + " " + this.lesLittoraux.get(i) + " ||";
			}
		}
		return tmp ;
	}
	
}
