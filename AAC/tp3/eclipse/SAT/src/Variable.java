
public class Variable {

	private boolean valeur;
	private int indice;
	
	public Variable(int indice,boolean valeur){
		this.indice=indice;
		this.valeur=valeur;
	}
	
	public boolean getValeur(){
		return this.valeur;
	}
	
	public int getIndice(){
		return this.indice;
	}

	@Override
	public String toString() {
		return "Variable [valeur=" + valeur + ", indice=" + indice + "]";
	}
	
}
