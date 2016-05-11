package environnement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import agent.AbstractAgent;
import agent.AgentEmpty;

public abstract class Environnement extends Observable {

	protected AbstractAgent[][] grille;
	protected int width,height;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	protected long timer =100;


	public Environnement(int height, int width) {
		this.height = height;
		this.width = width;
		this.initGrille();
		this.addDefaultObserver();
	}

	private void initGrille() {
		this.grille = new AbstractAgent[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				this.setCase(i, j, new AgentEmpty(this));				
	}

	public void addAgentsRandomly(ArrayList<AbstractAgent> agentList) {
		int x,y;
		for (AbstractAgent abstractAgent : agentList) {
			x = new Random().nextInt(height);
			y = new Random().nextInt(width);
			this.grille[x][y] = abstractAgent;
			abstractAgent.setGrillePosition(x,y);
		}
	}

	public boolean caseIsEmpty(int x, int y) {
		AbstractAgent agent = this.getAgent(x, y);
		if (agent==null)
			return true;
		return this.grille[x][y].isEmpty();
	}

	public boolean isOutOfBounds( int i, int j){
		return !(i >= 0 && i < this.width && j >= 0 && j < this.height);
	}

	public ArrayList<AgentEmpty> getAgentEmptyList(){
		ArrayList<AgentEmpty> list = new ArrayList<>();
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				if(this.caseIsEmpty(i, j)) 
					list.add((AgentEmpty)this.getAgent(i, j));
		return list;
	}

	public AbstractAgent getAgent(int x, int y) {
		return this.grille[x][y];
	}

	public void setCase(int x, int y, AbstractAgent agent) {
		if(this.caseIsEmpty(x, y)) {
			this.grille[x][y] = agent;
			agent.setGrillePosition(x, y);
		}
	} 

	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	public void notifyObserver() {
		for(Observer obs : listObserver)
			obs.update(this, "env");
	}

	public void removeObserver() {
		listObserver = new ArrayList<Observer>();
	} 

	public String toString() {
		String tab = "--------------------------------------------------\n";
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) { 
				if (this.grille[i][j] == null)
					tab += " ";
				else
					tab += "" + this.grille[i][j] ;
			}
			tab += "\n";
		}
		return tab;
	}

	public void run() {
		try {
			Thread.sleep(this.timer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<AbstractAgent> tmpList = getAgentList();
		Collections.shuffle(tmpList);
		for (AbstractAgent agent : tmpList) {
			if (agent.getClass().equals(AgentEmpty.class)) {

			} else {
				this.runOnce(agent);
			}

		}
		this.notifyObserver();
	}

	public AbstractAgent[][] getGrille() {
		return grille;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void runOnce(AbstractAgent agent) { 	
		agent.doIt();		
	}

	public ArrayList<AbstractAgent> getAgentList() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				list.add(this.grille[i][j]);
			}
		}
		return list;
	}

	public void setEmpty(int i, int j) {
		AgentEmpty ag = new AgentEmpty(this);
		ag.setGrillePosition(i, j);
		this.grille[i][j] = ag;

	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public abstract void addDefaultObserver();

}
