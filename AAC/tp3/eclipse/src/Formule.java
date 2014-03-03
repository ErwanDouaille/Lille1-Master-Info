import java.util.ArrayList;
import java.util.Scanner;


public class Formule {

	private ArrayList<Clause> lesClauses;
	private int nbVariable;
	private int nbClause;
	
	public Formule(){
		this.lesClauses=new ArrayList<Clause>();
	}
	
	public void CreerFormule(){
		System.out.println("Creation de la formule");
		int nbClause,i,j,indice,nbLittoraux;
		boolean negation;
		Clause c;
		Littoral l;
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir le nombre de clause\n");
		nbClause=sc.nextInt();
		this.nbClause=nbClause;
		for(i=0;i<nbClause;i++){
			c=new Clause();
			System.out.println("Clause "+i+" \n");
			System.out.println("Veuillez saisir le nombre de littoraux dans la clause \n");
			nbLittoraux=sc.nextInt();
			for(j=0;j<nbLittoraux;j++){
				System.out.println("Veuillez saisir l'indice du littoral "+j+" de la clause "+i +"\n");
				indice=sc.nextInt();
				System.out.println("Veuillez saisir la negation du littoral "+j+" de la clause "+i +" \n");
				negation=sc.nextBoolean();
				l=new Littoral(indice, negation);
				c.addLittoral(l);
			}
			this.addClause(c);
		}
	}
	
	public void addClause(Clause c){
		this.lesClauses.add(c);
	}
	
	public boolean verificationFormule(Certificat c){	
		int i,j;
		Variable v;
		boolean valeurLittoral,valeurClause;
		for(i=0;i<this.lesClauses.size();i++){
			valeurClause=false;
			for(j=1;j<this.lesClauses.get(i).getLesLittoraux().size()+1;j++){
				int indice=this.lesClauses.get(i).getLesLittoraux().get(j).getIndice();
				v=c.getVariable(indice);
				System.out.println(v);
				valeurLittoral=v.getValeur();
				if(this.lesClauses.get(i).getLesLittoraux().get(j).getNegation())
					valeurClause=valeurClause || (!valeurLittoral);
				else
					valeurClause=valeurClause || (valeurLittoral);
			}
			if(valeurClause==false){
			//	System.out.println("Le certificat ne resout pas la formule\n");
				return false;
			}
		}
		//System.out.println("Certificat correct\n");
		return true;
	}
	
	public void afficheVerif(boolean b){
		if(!b)
			System.out.println("Le certificat ne resout pas la formule\n");
		else
			System.out.println("Certificat correct\n");
	}
	
	public void afficheFormule(){
		System.out.println(this.nbClause + "  " + this.nbVariable);
		System.out.println(this.lesClauses.get(0) + " && ");
		for(int i=1;i<this.lesClauses.size();i++){
			if (i+1==this.lesClauses.size()) {
				System.out.println(this.lesClauses.get(i));
			} else {
				System.out.println(this.lesClauses.get(i) + " && ");
			}
		}
	}
	
	public boolean verificationFormule(){
		String var= Integer.toBinaryString(this.nbVariable-1);
		Certificat c=new Certificat();
		boolean res;
		for(int i=0;i<Math.pow(2, this.nbVariable);i++){
			var=Integer.toBinaryString(i);
			int diffTaille=this.nbVariable-var.length();
			for(int j=0;j<diffTaille;j++)
				var="0"+var;
			System.out.println(var);
			c.creerCertificat(var);
			res=this.verificationFormule(c);
			if(res)
				return res;
		}
		return false;
	}

	public ArrayList<Clause> getLesClauses() {
		return lesClauses;
	}

	public void setLesClauses(ArrayList<Clause> lesClauses) {
		this.lesClauses = lesClauses;
	}

	public int getNbVariable() {
		return nbVariable;
	}

	public void setNbVariable(int nbVariable) {
		this.nbVariable = nbVariable;
	}

	public int getNbClause() {
		return nbClause;
	}

	public void setNbClause(int nbClause) {
		this.nbClause = nbClause;
	}
	
	/*public static void main(String[] args){
		Formule f=new Formule();
		//Certificat c=new Certificat();
		f.CreerFormule();
		//c.creerCertificat();
		//c.creerCertificatAleatoire();
		f.afficheFormule();
		//c.afficheCertificat();
		boolean b=f.verificationFormule();
		f.afficheVerif(b);
	}*/
	
}
	