#include<stdio.h>
#include<assert.h>
#include<stdlib.h>
#define MAX 150

static int val[MAX][MAX][MAX][MAX];


int f(int m, int n, int i, int j){
	int iBis,jBis,res,resLocal,ligne,colonne,mBis,nBis,init,iSym,jSym;
	if(i<0 || i>=m || j<0 || j>=n){
		printf("Case de la mort non presente dans la tablette \n");
		printf("m n i j:  %d %d %d %d \n",m,n,i,j);
		assert(0);
	}
	/*Si la tablette contient seulement la case de la mort, on retourne 0*/
	if(m==1 && n==1){
		return 0;
	}
	res=0;
	init=0;
	/*On decoupe sur toute les lignes*/
	for(ligne=1;ligne<m;ligne++){
		/*On calcule la nouvelle position en abscisse de la case de la mort*/		
		if(i<ligne){
			iBis=i;
			mBis=ligne;
		}
		else{
			iBis=i-ligne;
			mBis=m-ligne;
		}
		/*On calcule la valeur de la configuration si cela n'a pas encore été fait*/
		if(val[mBis][n][iBis][j]==0){
			/*on calcule les valeurs de i et j pour la symetrie*/
			iSym=(mBis-1)-iBis;
			jSym=(n-1)-j;
			
			/*calcul de la conf*/
			val[mBis][n][iBis][j]=f(mBis,n,iBis,j);
			
			/*conf symetrique*/
			val[mBis][n][iBis][jSym]=val[mBis][n][iBis][j];
			val[mBis][n][iSym][j]=val[mBis][n][iBis][j];
			val[mBis][n][iSym][jSym]=val[mBis][n][iBis][j];
			val[n][mBis][j][iBis]=val[mBis][n][iBis][j];
			val[n][mBis][j][iSym]=val[mBis][n][iBis][j];
			val[n][mBis][jSym][iBis]=val[mBis][n][iBis][j];
			val[n][mBis][jSym][iSym]=val[mBis][n][iBis][j];
		}
		/*on sauvegarde la valeur dans une variable temporaire*/
		resLocal=val[mBis][n][iBis][j];	
		/*changement de la valeur optimal si necessaire*/
		if(init==1){
			if(((resLocal>0  && res>0) || (resLocal<=0  && res<=0))  && res<resLocal)
				res=resLocal;
			else if(resLocal<=0)
				res=resLocal;
		}
		else{
			init=1;
			res=resLocal;
		}
	}	
	/*On decoupe sur toutes les colonnes*/
	for(colonne=1;colonne<n;colonne++){
	/*On calcule la nouvelle position en ordonne de la case de la mort*/
		if(j<colonne){
			jBis=j;
			nBis=colonne;
		}
		else{
			jBis=j-colonne;
			nBis=n-colonne;
		}
		/*On calcule la valeur de la configuration si cela n'a pas encore été fait*/
		if(val[m][nBis][i][jBis]==0){
			/*on calcule les valeurs de i et j pour la symetrie*/
			iSym=(m-1)-i;
			jSym=(nBis-1)-jBis;

			/*calcul de la conf*/
			val[m][nBis][i][jBis]=f(m,nBis,i,jBis);

			/*conf symetrique*/
			val[m][nBis][i][jSym]=val[m][nBis][i][jBis];
			val[m][nBis][iSym][jBis]=val[m][nBis][i][jBis];
			val[m][nBis][iSym][jSym]=val[m][nBis][i][jBis];
			val[nBis][m][jBis][i]=val[m][nBis][i][jBis];
			val[nBis][m][jBis][iSym]=val[m][nBis][i][jBis];
			val[nBis][m][jSym][i]=val[m][nBis][i][jBis];
			val[nBis][m][jSym][iSym]=val[m][nBis][i][jBis];
		}
		/*on sauvegarde la valeur dans une variable temporaire*/
		resLocal=val[m][nBis][i][jBis];
		/*changement de la valeur optimal si necessaire*/
		if(init==1){
			if(((resLocal>0  && res>0) || (resLocal<=0  && res<=0))  && res<resLocal)
				res=resLocal;
			else if(resLocal<=0)
				res=resLocal;
		}
		else{
			init=1;
			res=resLocal;
		}
	}
	/*Calcul de configuration optimal à partir de la meilleur configuration inferieure*/
	if(res<=0){
		return (res-1)*(-1);}
	else {
		return (res+1)*(-1);
	}
}

void egal127(){
	int i,j,res;
	for(i=0;i<127;i++){
		for(j=0;j<127;j++){
			res=f(127,127,i,j);
			if(res==127){
				printf("conf à 127 pour un tablau 127*127:%d %d \n",i,j);
			}
		}
	}
}

int main (int argc, char *argv[]){
	int res;
 	time_t start;
	if(argc==1){
		egal127();
		return 0;
	}
	if((atoi(argv[1])>=MAX)  || (atoi(argv[2])>=MAX)){
		printf("le tableau est trop grand \n");
		assert(0);
	}	
    	start = time (NULL);	
	res=f(atoi(argv[1]),atoi(argv[2]),atoi(argv[3]),atoi(argv[4]));
	printf("configuration coup %d \n",res);
    	printf ("Duree = %ds\n", (int) (time (NULL) - start));
	return 0;
}
