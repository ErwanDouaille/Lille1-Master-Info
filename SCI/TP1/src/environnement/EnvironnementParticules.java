package environnement;

import java.util.ArrayList;
import java.util.Random;

import agent.AbstractAgent;
import agent.AgentParticules;

public class EnvironnementParticules extends Environnement {

	public EnvironnementParticules(int height, int width) {
		super(height, width);
		ArrayList<AbstractAgent> list = this.initParticulesList();
		this.addAgentsRandomly(list);
	}

	private ArrayList<AbstractAgent> initParticulesList() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		Random rand = new Random();
		for (int i = 0; i < 20; i++)
			list.add(new AgentParticules(rand.nextInt(3)-1, rand.nextInt(3)-1,this));
		return list;
	}

	public void addDefaultObserver() {
		// no one needed for particules
	}
	
	
	
}
