package main;

import environnement.*;
import gui.MainWindow;

public class Main  {

	public static void main(String[] args) {
		EnvironnementPacMan env = new EnvironnementPacMan(50, 50);
		MainWindow win = new MainWindow();
		win.setEnv(env);
		win.run();		
	}
}
