package csvExporter;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import agent.AbstractAgent;
import agent.AgentSegregation;
import environnement.Environnement;

public class CSVExporterSatisfaction extends CSVAbstractExporter{

	public CSVExporterSatisfaction(String filename) {
		super(filename);
	}

	public void exportation(Environnement env, FileWriter writer) {

		ArrayList<AbstractAgent> tmp = env.getAgentList();
		int[][] tab = new int [10][2];
		for(int i = 0; i< tab.length; i++)
			for(int j = 0; j< 2; j++)
				tab[i][j] = 0;

		for (AbstractAgent abstractAgent : tmp) {
			if(abstractAgent.getClass().equals(AgentSegregation.class)) {
				AgentSegregation ag = (AgentSegregation)abstractAgent;
				if(this.isBetween(ag.getMySatisfaction(), 0.0, 0.1))
					if(ag.color().equals(Color.GREEN))
						tab[0][0] = tab[0][0]+ 1;
					else
						tab[0][1] = tab[0][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.1, 0.2))
					if(ag.color().equals(Color.GREEN))
						tab[1][0] = tab[1][0]+ 1;
					else
						tab[1][1] = tab[1][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.2, 0.2))
					if(ag.color().equals(Color.GREEN))
						tab[2][0] = tab[2][0]+ 1;
					else
						tab[2][1] = tab[2][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.3, 0.3))
					if(ag.color().equals(Color.GREEN))
						tab[3][0] = tab[3][0]+ 1;
					else
						tab[3][1] = tab[3][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.4, 0.5))
					if(ag.color().equals(Color.GREEN))
						tab[4][0] = tab[4][0]+ 1;
					else
						tab[4][1] = tab[4][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.5, 0.6))
					if(ag.color().equals(Color.GREEN))
						tab[5][0] = tab[5][0]+ 1;
					else
						tab[5][1] = tab[5][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.6, 0.7))
					if(ag.color().equals(Color.GREEN))
						tab[6][0] = tab[6][0]+ 1;
					else
						tab[6][1] = tab[6][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.7, 0.8))
					if(ag.color().equals(Color.GREEN))
						tab[7][0] = tab[7][0]+ 1;
					else
						tab[7][1] = tab[7][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.8, 0.9))
					if(ag.color().equals(Color.GREEN))
						tab[8][0] = tab[8][0]+ 1;
					else
						tab[8][1] = tab[8][1]+ 1;
				if(this.isBetween(ag.getMySatisfaction(), 0.9, 1))
					if(ag.color().equals(Color.GREEN))
						tab[9][0] = tab[9][0]+ 1;
					else
						tab[9][1] = tab[9][1]+ 1;
			}
		} 

		try {
			for(int i = 0; i< tab.length; i++) {
				writer.append("" + i );
				for(int j = 0; j< 2; j++)
					writer.append(", " + tab[i][j]);
				writer.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isBetween(double x, double lower, double upper) {
		return lower <= x && x <= upper;
	}

	public boolean eraseFile() {
		return true;
	}	

}
