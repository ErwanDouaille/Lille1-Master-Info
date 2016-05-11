package environnement;

import java.util.ArrayList;

import csvExporter.CSVExporterPopulation;
import csvExporter.CSVExporterRatio;

import agent.AbstractAgent;
import agent.AgentFish;
import agent.AgentShark;

public class EnvironnementWator extends Environnement{

	public EnvironnementWator(int height, int width) {
		super(height, width);
		ArrayList<AbstractAgent> fish = this.initFishList();
		ArrayList<AbstractAgent> shark = this.initSharkList();
		fish.addAll(shark);
		this.addAgentsRandomly(fish);
	}

	private ArrayList<AbstractAgent> initSharkList() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		for (int i = 0; i < (this.getHeight()*this.getWidth()*0.005); i++)
			list.add(new AgentShark(this));
		return list;
	}
	private ArrayList<AbstractAgent> initFishList() {
		ArrayList<AbstractAgent> list = new ArrayList<AbstractAgent>();
		for (int i = 0; i < (this.getHeight()*this.getWidth()*0.022); i++)
			list.add(new AgentFish(this));
		return list;
	}

	public void addDefaultObserver() {
		this.addObserver(new CSVExporterPopulation("population"));
		this.addObserver(new CSVExporterRatio("ratio"));
	}
}
