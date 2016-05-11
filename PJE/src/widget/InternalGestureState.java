package widget;

import oneDollarRecognizer.OneDollarRecognizer;
import mygeom.OBB;
import mygeom.Vector2;

public class InternalGestureState{
	// position d'une OBB
	OBB oldOBB,currentOBB;
	OneDollarRecognizer odr;
	
	//gestion des états du cursuer pour la translation simple
	Vector2 oldPos,currentPos;
	
	public InternalGestureState(MTComponent c){
		//le currentOBB doit correspondre à la refernce de l'OBB de c
		//i.e. : lorsque currentOBB est modifié, cela modifie l'OBB de c
		oldPos=new Vector2();
		currentPos=new Vector2();
		oldOBB=new OBB(); //pour memoriser la position precedente, si besoin
		currentOBB = c.getObb(); //i.e reference sur l'OBB du composant
	}
}