package agent;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import environnement.Environnement;
import environnement.EnvironnementPacMan;

public class AgentGhost extends AbstractAgent {

	private Random rand = new Random();
	
	public AgentGhost(Environnement environnement) {
		super(environnement);
	}

	public void doIt() {
		this.move();
	}

	private void move() {
		Point destination = new Point(-1, -1);
		//DEFAULT VALUE, will never be reach ;)
		int value=1000000;
		ArrayList<Point> listPoint = new ArrayList<Point>();
		for (int i = -1; i < 2; i++) 
			for (int j = -1; j < 2; j++) 
				if (!this.environnement.isOutOfBounds(this.getPosx()+i, this.getPosy()+j)) {
					AbstractAgent agent = this.environnement.getAgent(this.getPosx()+i, this.getPosy()+j);
					if (agent.getClass().equals(AgentEmptyWithId.class)) {
						AgentEmptyWithId agentID = (AgentEmptyWithId)agent;
						if(agentID.getValue()<value){
							value = agentID.getValue();
						}
					}
				}
		for (int i = -1; i < 2; i++) 
			for (int j = -1; j < 2; j++) 
				if (!this.environnement.isOutOfBounds(this.getPosx()+i, this.getPosy()+j)) {
					AbstractAgent agent = this.environnement.getAgent(this.getPosx()+i, this.getPosy()+j);
					if (agent.getClass().equals(AgentEmptyWithId.class)) {
						AgentEmptyWithId agentID = (AgentEmptyWithId)agent;
						if(agentID.getValue()==value){
							listPoint.add(new Point(this.getPosx()+i, this.getPosy()+j));
						}
					}
				}
		if(listPoint.size() != 0) {
			destination = listPoint.get(rand.nextInt(listPoint.size()));
		}
		//BULLSHIT, in case of any adjacent value or lower than the current one
		if (destination.getX()==-1 || destination.getY()==-1)
			return;
		((EnvironnementPacMan)this.environnement).setEmptyWithId(this.posx, this.posy, value);
		this.environnement.setCase((int)destination.getX(), (int)destination.getY(), this);
	}

	public String toString() {
		return "--G--";
	}

	public Color color() {
		return Color.RED;
	}

}
