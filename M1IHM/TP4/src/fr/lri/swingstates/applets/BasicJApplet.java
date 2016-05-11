package fr.lri.swingstates.applets;

import javax.swing.JApplet;

import fr.lri.swingstates.sm.SwingStatesApplet;

public abstract class BasicJApplet extends JApplet {

	public void init() {
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					SwingStatesApplet.init();
					createGUI();
				}
			});
		} catch (Exception e) {
			System.err.println("createGUI didn't successfully complete");
			e.printStackTrace();
		}
	}

	public abstract void createGUI();

}
