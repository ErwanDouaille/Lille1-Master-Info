package environnement;

import java.util.ArrayList;

import agent.AbstractAgent;
import agent.AgentEmpty;
import agent.AgentEmptyWithId;
import agent.AgentGhost;
import agent.AgentPacman;

public class EnvironnementPacMan extends Environnement {

	private AgentPacman pacman;

	public EnvironnementPacMan(int height, int width) {
		super(height, width);
		this.initGrille();
		this.pacman = new AgentPacman(this);
		ArrayList<AbstractAgent> ghost = this.initGhost();
		ArrayList<AbstractAgent> obstacle = this.initObstacle();
		ghost.addAll(obstacle);
		ghost.add(this.pacman);
		this.addAgentsRandomly(ghost);
	}

	private void initGrille() {
		this.grille = new AbstractAgent[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++) {
				this.setCase(i, j, new AgentEmptyWithId(this));
			}		
	}

	private ArrayList<AbstractAgent> initGhost() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		for (int i = 0; i < 10; i++)
			list.add(new AgentGhost(this));
		return list;
	}

	private ArrayList<AbstractAgent> initObstacle() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		for (int i = 0; i < (this.getHeight()*this.getWidth()*0.1); i++)
			list.add(new AgentEmpty(this));
		return list;
	}

	public void run() {
		try {
			Thread.sleep(this.timer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.clearDijkstra();
		pacman.doIt();
		this.dijkstraPowa(this.pacman.getPosx(), this.pacman.getPosy());
		ArrayList<AgentGhost> ghostList = this.collectGhost();
		for (AgentGhost agentGhost : ghostList) 
			agentGhost.doIt();
		this.notifyObserver();
	}

	private void clearDijkstra() {
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++) {
				AbstractAgent agent = this.getAgent(i, j);
				if (agent.getClass().equals(AgentEmptyWithId.class) ) {
					((AgentEmptyWithId)agent).setValue(0);
				}
			}
	}

	private void dijkstraPowa(int x, int y) {	
		for (int i = 0; i < this.width; i++) {
			for (int j = -i; j <= i; j++) {
				for (int k = -i; k <= i; k++) {
					if (!this.isOutOfBounds(x+j, y+k)) { 
						this.setDijkstraTo(x+j, y+k, i);
					}
				}
			}
		}
	}

	private void setDijkstraTo(int i, int j, int cpt) {
		AbstractAgent agent = this.getAgent(i, j);
		if (agent.getClass().equals(AgentEmptyWithId.class)) {
			AgentEmptyWithId agentID = (AgentEmptyWithId)agent;
			if (agentID.getValue()==0 ) {
				agentID.setValue(cpt);
			}
		}		
	}

	public ArrayList<AgentGhost> collectGhost() {
		ArrayList<AgentGhost> ghostList = new ArrayList<AgentGhost>();
		ArrayList<AbstractAgent> tmpList = getAgentList();
		for (AbstractAgent abstractAgent : tmpList) 
			if(abstractAgent.getClass().equals(AgentGhost.class))
				ghostList.add((AgentGhost)abstractAgent);
		return ghostList;
	}

	public void addDefaultObserver() {
		// NONE
	}

	public void setEmptyWithId(int posx, int posy, int i) {
		AgentEmptyWithId ag = new AgentEmptyWithId(this);
		ag.setValue(i);
		ag.setGrillePosition(posx, posy);
		this.grille[posx][posy] = ag;		
	}

}