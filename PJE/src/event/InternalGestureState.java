package event;

import java.awt.Toolkit;

import oneDollarRecognizer.OneDollarRecognizer;
import widget.MTComponent;
import mygeom.OBB;
import mygeom.Vector2;

public class InternalGestureState {

	private OBB oldOBB, currentOBB;
	private Vector2 oldPos, currentPos, oldPosPrim, currentPosPrim;
	public OneDollarRecognizer oneDollarRecognizer = new OneDollarRecognizer();
	
	public InternalGestureState(MTComponent c){
		this.oldPos = new Vector2();
		this.currentPos = new Vector2();
		this.oldPosPrim = new Vector2();
		this.currentPosPrim = new Vector2();
		this.oldOBB = new OBB();
		this.currentOBB = c.getObb();
	}

	public void motionTranslateBegin(Vector2 cursor){
		this.currentPos=cursor;
	}
	
	public void motionTranslateUpdate(Vector2 cursor){
		this.oldPos=this.currentPos;
		this.currentPos=cursor;
	}
	
	public void motionTRS(Vector2 cursor,Vector2 cursorPrim){
		this.currentPos=cursor;
		this.currentPosPrim=cursorPrim;
	}
	
	public void motionTRSUpdate(Vector2 cursor,Vector2 cursorPrim){
		this.currentPosPrim.copy(this.oldPosPrim);
		cursorPrim.copy(this.currentPosPrim);
		this.currentPos.copy(this.oldPos);
		cursor.copy(this.currentPos);
	}
		
	public OBB getOldOBB() {
		return oldOBB;
	}

	public void setOldOBB(OBB oldOBB) {
		this.oldOBB = oldOBB;
	}

	public OBB getCurrentOBB() {
		return currentOBB;
	}

	public void setCurrentOBB(OBB currentOBB) {
		this.currentOBB = currentOBB;
	}

	public Vector2 getOldPos() {
		return oldPos;
	}

	public void setOldPos(Vector2 oldPos) {
		this.oldPos = oldPos;
	}

	public Vector2 getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(Vector2 currentPos) {
		this.currentPos = currentPos;
	}
	
	public Vector2 computeTranslation(){
		return new Vector2(
				(this.currentPos.getX() - this.oldPos.getX())*Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(this.currentPos.getY() - this.oldPos.getY())*Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	}
	
	public double computeTRSScale(){
		Vector2 ratioCurrent = new Vector2();
		Vector2 ratioOld = new Vector2();
		if(this.currentPos.getX()> this.currentPosPrim.getX())
			ratioCurrent.setX(this.currentPos.getX()/this.currentPosPrim.getX());
		else
			ratioCurrent.setX(this.currentPosPrim.getX()/this.currentPos.getX());
		if(this.currentPos.getY()> this.currentPosPrim.getY())
			ratioCurrent.setY(this.currentPos.getY()/this.currentPosPrim.getY());
		else
			ratioCurrent.setY(this.currentPosPrim.getY()/this.currentPos.getY());
		if(this.oldPos.getX()> this.oldPosPrim.getX())
			ratioOld.setX(this.oldPos.getX()/this.oldPosPrim.getX());
		else
			ratioOld.setX(this.oldPosPrim.getX()/this.oldPos.getX());
		if(this.oldPos.getY()> this.oldPosPrim.getY())
			ratioOld.setY(this.oldPos.getY()/this.oldPosPrim.getY());
		else
			ratioOld.setY(this.oldPosPrim.getY()/this.oldPos.getY());
		
		if(ratioCurrent.getX()>ratioOld.getX() || ratioCurrent.getY()> ratioCurrent.getY())
			return 1.03;
		return 0.97;
	}
	
	public double computeTRSRotation(){
		Vector2 u=new Vector2(
				(this.currentPos.getX() - this.oldPos.getX()),
				(this.currentPos.getY() - this.oldPos.getY()));
		Vector2 v=new Vector2(
				(this.currentPosPrim.getX() - this.oldPosPrim.getX()),
				(this.currentPosPrim.getY() - this.oldPosPrim.getY()));
		return u.determinant(v)*u.angle(v);
	}
	

	
	public Vector2 computeTRSTranslation(){
		return new Vector2(
				(this.currentPos.getX() - this.oldPos.getX()),
				(this.currentPos.getY() - this.oldPos.getY()));
	}

	public OneDollarRecognizer getOneDollarRecognizer() {
		return oneDollarRecognizer;
	}

	public void setOneDollarRecognizer(OneDollarRecognizer oneDollarRecognizer) {
		this.oneDollarRecognizer = oneDollarRecognizer;
	}
	
	
	
}
