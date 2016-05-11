package agent;

import environnement.Environnement;

public class AgentEmpty extends AbstractAgent{

	public AgentEmpty(Environnement environnement) {
		super(environnement);
	}

	public void doIt() {
	}

	public boolean isEmpty() {
		return true;
	}

	public String toString() {
		return "     ";
	}

}
