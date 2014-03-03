import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Certificat {
	private ArrayList<Variable> lesVariables;
	
	public Certificat(){
		this.lesVariables=new ArrayList<Variable>();
	}
	
	public void addVariable(Variable v){
		this.lesVariables.add(v);
	}
	
	public ArrayList<Variable> getlesVariables(){
		return this.lesVariables;
	}
	
	public Variable getVariable(int indice){
		Iterator<Variable> it=this.lesVariables.iterator();
		Variable v;
		while(it.hasNext()){
			v=it.next();
			if(v.getIndice()==indice)
				return v;
		}
		return null;
	}
	
	public void creerCertificat(){
		System.out.println("Creation d'un certificat");
		int i,nbLittoral,indice;
		boolean valeur;
		Variable v;
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir le nombre de littoral\n");
		nbLittoral=sc.nextInt();
		for(i=0;i<nbLittoral;i++){
			System.out.println("Veuillez saisir l'indice du littoral \n");
			indice=sc.nextInt();
			System.out.println("Veuillez saisir la valeur du littoral\n");
			valeur=sc.nextBoolean();
			v=new Variable(indice, valeur);
			this.lesVariables.add(v);
		}
	}
	
	public void creerCertificatAleatoire(){
		System.out.println("Creation d'un certificat aleatoire");
		int nbLittoral,valeur;
		Variable v;
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir le nombre de littoral\n");
		nbLittoral=sc.nextInt();
		for(int i=0;i<nbLittoral;i++){
			valeur=(int)(Math.random()*2);
			if(valeur==0)
				v=new Variable(i, true);
			else 
				v=new Variable(i, false);
			this.lesVariables.add(v);
		}
	}
	
	public void creerCertificat(String bit){
		this.lesVariables=new ArrayList<Variable>();
		Variable v;
		boolean valeur;
		for(int i=0;i<bit.length();i++){
			if(bit.charAt(i)==0)
				valeur=false;
			else
				valeur=true;
			v=new Variable(i, valeur);
			this.addVariable(v);
		}
	}
	
	public void afficheCertificat(){
		for(int i=0;i<this.lesVariables.size();i++){
			System.out.println("Variable v"+this.lesVariables.get(i).getIndice()+" : "+this.lesVariables.get(i).getValeur());
		}
	}
}
