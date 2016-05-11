package agent;

import java.awt.Color;

import environnement.Environnement;
 
public abstract class AbstractAgent {
	
	protected int posx,posy;
	protected Environnement environnement;
	protected boolean shouldDisapear;
	
	public AbstractAgent(Environnement environnement) {
		this.environnement = environnement;
		this.shouldDisapear = false;
	}

	public void setGrillePosition(int x, int y) {
		this.posx = x;
		this.posy = y;
	}

	public abstract void doIt();
	public abstract String toString();

	public int getPosx() {
		return posx;
	}

	public int getPosy() {
		return posy;
	}

	public boolean isEmpty() {
		return false;
	}
	
	public Color color() {
		return Color.WHITE;
	}

}
