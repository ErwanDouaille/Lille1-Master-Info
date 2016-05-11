package agent;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import environnement.Environnement;

public class AgentShark extends AbstractAgent {

	private int life, starvingValue, starvingValueMax, reproductionCycle;
	private Random rand = new Random();

	public AgentShark(Environnement environnement) {
		super(environnement);
		this.life = 1; 	
		this.starvingValue = 0;
		this.starvingValueMax = 3;
		this.reproductionCycle = 10;
	}

	public void doIt() {
		if (starvingValue>starvingValueMax) {
			this.environnement.setEmpty(this.posx, this.posy);
		} else {
			this.move();
			this.starvingValue = this.starvingValue +1;
			this.life = this.life + 1;
		}
	}

	private void move() {
		ArrayList<Point> possibilitiesWithEmpty = this.checkPossibleDirection();
		ArrayList<Point> possibilities = this.checkFishDirection();
		if (possibilities.size() != 0 || possibilitiesWithEmpty.size() != 0 ) {
			Point direction = possibilitiesWithEmpty.get(rand.nextInt(possibilitiesWithEmpty.size()));
			if (possibilities.size() != 0) 
				direction = possibilities.get(rand.nextInt(possibilities.size()));
			AbstractAgent agent = (AbstractAgent)this.environnement.getAgent(this.posx+(int)direction.getX(), this.posy+(int)direction.getY());
			this.eatFish(agent);
			this.environnement.setEmpty(this.posx, this.posy);
			this.reproduction();		
			this.environnement.setCase((int) (this.posx + direction.getX()), (int) (this.posy + direction.getY()), this);		
		}
	}

	private ArrayList<Point> checkFishDirection() {
		ArrayList<Point> possibilities = new ArrayList<Point>();
		for (int i = -1; i < 2; i++) 
			for (int j = -1; j < 2; j++) {
				if (!this.environnement.isOutOfBounds(this.posx+i, this.posy+j)) {
					AbstractAgent agent = this.environnement.getAgent(this.posx+i, this.posy+j);
					if (agent.getClass().equals(AgentFish.class))
						possibilities.add(new Point(i, j));
				}
			}
		return possibilities;
	}

	private void reproduction() {
		if ((this.life%(this.reproductionCycle+1))==0) {
			AgentShark reproduction = new AgentShark(this.environnement);
			this.environnement.setCase(this.posx, this.posy, reproduction);
		}	
	}

	private void eatFish(AbstractAgent agent) {
		if (agent.getClass().equals(AgentFish.class)) {
			AgentFish fish = (AgentFish)agent;
			this.environnement.setEmpty(fish.getPosx(), fish.getPosy());
			fish.setDie();
			this.starvingValue = -1;
		}
	}

	private ArrayList<Point> checkPossibleDirection() {
		ArrayList<Point> possibilities = new ArrayList<Point>();
		for (int i = -1; i < 2; i++) 
			for (int j = -1; j < 2; j++) {
				if (!this.environnement.isOutOfBounds(this.posx+i, this.posy+j)) {
					AbstractAgent agent = this.environnement.getAgent(this.posx+i, this.posy+j);
					if (agent.getClass().equals(AgentEmpty.class)) 
						possibilities.add(new Point(i, j));
					if (agent.getClass().equals(AgentFish.class))
						possibilities.add(new Point(i, j));
				}
			}
		return possibilities;
	}

	public String toString() {
		return "S";
	}

	public Color color() {
		return Color.GREEN;
	}
}
