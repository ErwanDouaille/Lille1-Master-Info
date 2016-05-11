package agent;

import java.awt.Color;

import environnement.Environnement;

public class AgentParticules extends AbstractAgent {

	int movX, movY;

	public AgentParticules(int movX, int movY, Environnement environnement) {
		super(environnement);
		this.movX = movX;
		this.movY = movY;
	}

	public void doIt() {
		if(this.posx+this.movX <= 0 || this.posy+this.movY <= 0 || this.posx+this.movX >= this.environnement.getWidth()  || this.posy+this.movY >= this.environnement.getHeight() ) {
			if (this.posy+this.movY >= this.environnement.getHeight()-1 || this.posy+this.movY < 0) 
				this.reverseYMouvement();
			if (this.posx+this.movX >= this.environnement.getWidth()-1 || this.posx+this.movX < 0) 
				this.reverseXMouvement();
			this.move();
		}
		else if (!this.environnement.caseIsEmpty(this.posx+this.movX, this.posy+this.movY) ) {
			AgentParticules ball = (AgentParticules)this.environnement.getAgent(this.posx+this.movX, this.posy+this.movY);
			if (ball.isMoving())
				ball.setMovement(this.movX, this.movY);
			this.changeMouvementCollision();
		} 		
		else {
			this.move();			
		}
	}

	public void move() {
		this.environnement.setEmpty(this.posx, this.posy);
		this.posx = this.posx + this.movX;
		this.posy = this.posy + this.movY;
		this.environnement.setCase(this.posx, this.posy, this);
	}

	private void changeMouvementCollision() {
		this.reverseXMouvement();
		this.reverseYMouvement();
	}

	private void reverseXMouvement() {
		this.movX = -this.movX;
	}

	private void reverseYMouvement() {
		this.movY = -this.movY;
	}

	public void setMovement(int x, int y) {
		this.movX = x;
		this.movY = y;
	}

	public boolean isMoving(){
		return movX == 0 && movY == 0;
	}
	
	public String toString() {
		return "X";
	}

	public int getMovX() {
		return movX;
	}

	public int getMovY() {
		return movY;
	}

	public Color color() {
		return Color.RED;
	}
}
