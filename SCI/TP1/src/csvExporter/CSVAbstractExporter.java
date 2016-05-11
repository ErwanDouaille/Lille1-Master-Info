package csvExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import environnement.Environnement;

public abstract class CSVAbstractExporter implements Observer {

	private String fileName;

	public CSVAbstractExporter(String filename) {
		this.fileName = filename;
		FileWriter writer;
		try {
			writer = new FileWriter(this.fileName);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(Observable observable, Object arg) {
		if(!arg.equals("env"))
			return;
		Environnement env = (Environnement)observable;
		try
		{
			FileWriter writer = new FileWriter(this.fileName, !this.eraseFile());
			this.exportation(env, writer);
			writer.flush();
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		} 

	}
	
	public abstract void exportation(Environnement env, FileWriter writer);
	
	public abstract boolean eraseFile();

}
