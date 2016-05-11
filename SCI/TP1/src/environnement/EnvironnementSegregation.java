package environnement;

import java.awt.Color;
import java.util.ArrayList;

import csvExporter.CSVExporterSatisfaction;

import agent.AbstractAgent;
import agent.AgentSegregation;

public class EnvironnementSegregation extends Environnement{

	private double satisfaction = 0;
	
	public EnvironnementSegregation(int height, int width, double satisfaction) {
		super(height, width);
		this.satisfaction = satisfaction;
		ArrayList<AbstractAgent> red = this.initRedList();
		ArrayList<AbstractAgent> green = this.initGreenList();
		red.addAll(green);
		this.addAgentsRandomly(red);
	}

	private ArrayList<AbstractAgent> initRedList() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		for (int i = 0; i < (this.getHeight()*this.getWidth()*0.4); i++) {
			AgentSegregation agent = new AgentSegregation(this, this.satisfaction);
			agent.setColor(Color.RED);
			list.add(agent);
		}
		return list;
	}
	private ArrayList<AbstractAgent> initGreenList() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		for (int i = 0; i < (this.getHeight()*this.getWidth()*0.4); i++) {
			AgentSegregation agent = new AgentSegregation(this, this.satisfaction);
			agent.setColor(Color.GREEN);
			list.add(agent);
		}
		return list;
	}
	
	public void addDefaultObserver() {
		this.addObserver(new CSVExporterSatisfaction("satisfaction"));
	}
}