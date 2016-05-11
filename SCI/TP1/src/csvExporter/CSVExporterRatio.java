package csvExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import agent.AbstractAgent;
import agent.AgentFish;
import agent.AgentShark;

import environnement.Environnement;

public class CSVExporterRatio extends CSVAbstractExporter{

	public CSVExporterRatio(String filename) {
		super(filename);
	}

	public void exportation(Environnement env, FileWriter writer) {

		try {
			ArrayList<AbstractAgent> tmp = env.getAgentList();
			int cptFish=0, cptShark=0;
			for (AbstractAgent abstractAgent : tmp) {
				if(abstractAgent.getClass().equals(AgentFish.class))
					cptFish ++;
				if(abstractAgent.getClass().equals(AgentShark.class))
					cptShark ++;
			} 

			try {
				writer.append(cptFish + ", " + cptShark + "\n");
			} catch (Exception e) {
				writer.append("0\n");
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public boolean eraseFile() {
		return false;
	}	

}
