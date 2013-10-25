import java.io.*;
import java.lang.String;
import java.util.*;
import java.lang.Class;
import java.lang.System;
import java.lang.Math;

/** Cette classe contient les toutes les methodes dont on souhaite mesurer les performances. Ces methodes s'appliquent sur un objet ArrayList ou tableau. */

public class Methodes{
    
    /** les methodes a mesurer s'appliquent sur L et T de taille size */
    ArrayList L;  
    int T[]; 
    int size;

    /** constructeur */
    public Methodes(int size){

	this.size = size;
	this.L = new ArrayList(size);
	this.T = new int[this.size];
	
	for(int i = 0; i < this.size; i++){
	    this.L.add(i);
	    this.T[i] = i;
	}

    }

    /** place les elements de L dans un ordre aleatoire */
    public void aleaL(){
	Collections.shuffle(this.L);
    }

    /** place les elements de T dans un ordre aleatoire */
    public void aleaT(){
	Random random = new Random(); 
	int place;
	int tmp;
	
	for (int i=0; i < this.size; i++){
	    place = i+random.nextInt(this.size-i);
	    tmp = this.T[i];
	    this.T[i] = this.T[place];
	    this.T[place] = tmp;
	}
    } 

    /** teste la presence de e dans L en utilisant la methode
     * contains */
    public boolean rechercheL(int e){
	
	return this.L.contains(e);
    }

    /** teste la presence de e dans T en utilisant une recherche
     * sequentielle  */
    public boolean rechercheT(int e){
	for(int j=0;j<this.size;j++){
	    if (this.T[j] == e) {
		return true;
	    }
	}
	return false;
    }

    /** recherche de la valeur du minimum de L en utilisant la methode
     * min de la classe Collections */
    public int minimumSimpleL(){
	
	return ((Integer)Collections.min(this.L)).intValue();
    }

    /** recherche de la valeur du minimum de T en utilisant une
     * recherche séquentielle */
    public int minimumSimpleT(){
	int min = 0;
	for(int j=1;j<this.size;j++){
	    if (this.T[min] > this.T[j]) {
		min = j;
	    }
	}
	return this.T[min];
    }
    
    
    /** recherche de la valeur du minimum de L en triant L grace a la
     * methode sort de la classe Collections */
    public int minimumTriL(){

	Collections.sort(this.L);
	return ((Integer)this.L.get(0)).intValue();
   }

    /** recherche de la valeur du minimum de T en triant T grace a la
     * methode sort de la classe Arrays */
    public int minimumTriT(){

	Arrays.sort(this.T);
	return this.T[0];
   }



    /** ??? */
     public void mystereL(){
	Integer min;
	int indexMin;
	
	for ( int i = 0 ; i < this.size; i++){

	    min = (Integer)Collections.min(this.L.subList(i,this.size));
	    indexMin = this.L.indexOf(min);
 
	    Collections.reverse(this.L.subList(indexMin,this.size));
	    Collections.reverse(this.L.subList(i,this.size));
	    
	} 
   }
     /** ??? */
     public void mystereT(){
	int indexMin;
	int tmp;
    
	for ( int i = 0 ; i < this.size; i++){
	    
	    indexMin = i;
	    for(int j=i+1;j<this.size;j++){
		if (this.T[indexMin] > this.T[j]) {
		    indexMin = j;
		}
	    }	   
 
	    for (int k = indexMin; k < (indexMin  + this.size)/2 ; k++){
		tmp = this.T[k];
		this.T[k] = this.T[this.size + indexMin - k - 1];
		this.T[this.size + indexMin - k - 1] = tmp;
	    }

	    for (int p = i; p < (i  + this.size)/2 ; p++){
		tmp = this.T[p];
		this.T[p] = this.T[this.size + i - p - 1];
		this.T[this.size + i - p - 1] = tmp;
	    }
	}
     
	
     }
    
    
}


