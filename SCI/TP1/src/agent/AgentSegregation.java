package agent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import environnement.Environnement;
import environnement.EnvironnementSegregation;

public class AgentSegregation extends AbstractAgent{
	
	private Color color;
	private double satisfaction;
	private double mySatisfaction;
	private Random rand = new Random();

	public AgentSegregation(Environnement environnement) {
		super(environnement);
	}

	public AgentSegregation(EnvironnementSegregation environnement, double satisfaction) {
		super(environnement);
		this.satisfaction = satisfaction;
	}

	public void doIt() {
		if(!this.isSatisfied())
			this.move();
	}

	private void move() {
		ArrayList<AgentEmpty> possibilities = this.environnement.getAgentEmptyList();
		if (possibilities.size() != 0 ) {
			AgentEmpty empty = possibilities.get(rand.nextInt(possibilities.size()));
			this.environnement.setEmpty(this.posx, this.posy);
			this.environnement.setCase(empty.getPosx(), empty.getPosy(), this);		
		}
	}

	private boolean isSatisfied() {
		int nbAgent = 0, ennemies = 0;
		for (int i = -1; i < 2; i++) 
			for (int j = -1; j < 2; j++) {
				if (!this.environnement.isOutOfBounds(this.posx+i, this.posy+j)) {
					AbstractAgent agent = this.environnement.getAgent(this.posx+i, this.posy+j);
					if (!agent.getClass().equals(AgentEmpty.class)) {
						AgentSegregation agentS = (AgentSegregation) agent;
						if(agentS.color().equals(this.color()))
							nbAgent++;
						else {
							nbAgent++;
							ennemies++;
						}								
					}						
				}
			}
		double result = 1.0;
		try {
			result = 1.0 - (double)ennemies/(double)nbAgent;
			this.mySatisfaction = result;
		} catch(Exception e) {			
		}
		return this.satisfaction < result;
	}

	public String toString() {
		return this.color.toString();
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public double getSatisfaction() {
		return this.satisfaction;
	}
	
	public double getMySatisfaction() {
		return this.mySatisfaction;
	}
	
	public Color color() {
		return this.color;
	}
	

}
