package agent;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import environnement.Environnement;
import environnement.EnvironnementPacMan;

public class AgentPacman extends AbstractAgent {
	
	private Random rand = new Random();

	public AgentPacman(Environnement environnement) {
		super(environnement);
	}

	public void doIt() {
		ArrayList<Point> list = new ArrayList<Point>();
		for (int i = -1; i < 2; i++) 
			for (int j = -1; j < 2; j++) {
				if (!this.environnement.isOutOfBounds(this.posx+i, this.posy+j)) {
					AbstractAgent agent = this.environnement.getAgent(this.posx+i, this.posy+j);
					if (agent.getClass().equals(AgentEmptyWithId.class))
						list.add(new Point(i, j));
				}
			}
		if (list.size() != 0 ) {
			Point direction = list.get(rand.nextInt(list.size()));
			((EnvironnementPacMan)this.environnement).setEmptyWithId(this.posx, this.posy, 0);
			this.environnement.setCase(this.posx+(int)direction.getX(), this.posy+(int)direction.getY(), this);

		}
		
	}

	public String toString() {
		return "--P--";
	}

	public Color color() {
		return Color.YELLOW;
	}
}
