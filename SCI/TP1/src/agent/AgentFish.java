package agent;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import environnement.Environnement;

public class AgentFish extends AbstractAgent {

	private int life, reproductionCycle;
	private Random rand = new Random();
	private boolean die;

	public AgentFish(Environnement environnement) {
		super(environnement);
		this.life = 1;
		this.reproductionCycle = 2;
		this.die = false;
	}

	public void doIt() {
		if (die) {
		} else {
			this.move();
			this.life++;
		}
	}

	private void move() {
		ArrayList<Point> list = new ArrayList<Point>();
		for (int i = -1; i < 2; i++) 
			for (int j = -1; j < 2; j++) {
				if (!this.environnement.isOutOfBounds(this.posx+i, this.posy+j)) {
					AbstractAgent agent = this.environnement.getAgent(this.posx+i, this.posy+j);
					if (agent.getClass().equals(AgentEmpty.class))
						list.add(new Point(i, j));
				}
			}
		if (list.size() != 0 ) {
			Point direction = list.get(rand.nextInt(list.size()));
			this.environnement.setEmpty(this.posx, this.posy);
			this.reproduction();
			this.environnement.setCase(this.posx+(int)direction.getX(), this.posy+(int)direction.getY(), this);

		}
	}

	public void setDie() {
		this.die = true;
	}

	private void reproduction() {
		if ((this.life%(this.reproductionCycle+1))==0) {
			AgentFish reproduction = new AgentFish(this.environnement);
			this.environnement.setCase(this.posx, this.posy, reproduction);
		}				
	}

	public String toString() {
		return "F";
	}

	public Color color() {
		return Color.BLUE;
	}

}
