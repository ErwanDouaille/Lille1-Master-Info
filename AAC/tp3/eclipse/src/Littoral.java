
public class Littoral {

	private int indice;
	private boolean negation;
	
	public Littoral(int indice,boolean negation){
		this.indice=indice;
		this.negation=negation;
	}
	
	public int getIndice(){
		return this.indice;
	}
	
	public boolean getNegation(){
		return this.negation;
	}
	
	public String toString(){
		String tmp="";
		if (negation) {
			tmp = "!";
		}
		return tmp + "v" + this.indice;
	}
	
}
