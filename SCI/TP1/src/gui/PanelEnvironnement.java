package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import agent.AbstractAgent;
import environnement.Environnement;

public class PanelEnvironnement extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainWindow parent;

	public PanelEnvironnement(MainWindow parent) {
		this.parent = parent;
	}

	public int getWidth() {
		return this.parent.getWidth();
	}

	public int getHeight() {
		return this.parent.getHeight();
	}

	public void paint(Graphics g){    
		super.paint(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Environnement env = this.parent.getEnv();
		for (int i = 0; i < env.getHeight(); i++) {
			for (int j = 0; j < env.getWidth(); j++) {
				AbstractAgent tmpAgent = env.getAgent(i, j);
				g.setColor(tmpAgent.color());
				g.fillOval(parent.getWidth()/env.getWidth()*j, (parent.getHeight()-50)/env.getHeight()*i, parent.getWidth()/env.getWidth(), (parent.getHeight()-50)/env.getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(parent.getWidth()/env.getWidth()*j, (parent.getHeight()-50)/env.getHeight()*i, parent.getWidth()/env.getWidth(), (parent.getHeight()-50)/env.getHeight());
			}
		}
	}

}
