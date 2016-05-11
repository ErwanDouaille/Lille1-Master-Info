package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import environnement.*;

public class MainWindow extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private Environnement env;

	private JMenuBar menuBar = new JMenuBar();
	private JMenu test1 = new JMenu("Programme");
	private JMenu test1_2 = new JMenu("Select environnement");
	private JMenu test2 = new JMenu("Option");
	private JMenuItem item2 = new JMenuItem("Fermer");
	private JMenuItem item3 = new JMenuItem("Timer");
	private JMenuItem item5 = new JMenuItem("Taille grille");
	private JMenuItem item4 = new JMenuItem("A propos");
	private JRadioButtonMenuItem jrmi1 = new JRadioButtonMenuItem("Particules");
	private JRadioButtonMenuItem jrmi2 = new JRadioButtonMenuItem("Wa-Tor");
	private JRadioButtonMenuItem jrmi3 = new JRadioButtonMenuItem("Segregation");
	private JRadioButtonMenuItem jrmi4 = new JRadioButtonMenuItem("Pacman");

	private int gridSize = 30;

	public MainWindow() {		
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panel = new PanelEnvironnement(this);
		this.setContentPane(this.panel);	
		this.menuInitialisation();
		this.setVisible(true);		
	}

	private void menuInitialisation() {
		this.test1_2.addSeparator();
		ButtonGroup bg = new ButtonGroup();
		bg.add(jrmi1);
		bg.add(jrmi2);
		jrmi4.setSelected(true);
		this.test1_2.add(jrmi1);
		this.test1_2.add(jrmi2);
		this.test1_2.add(jrmi3);
		this.test1_2.add(jrmi4);

		jrmi1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				env.removeObserver();
				env = new EnvironnementParticules(gridSize, gridSize);
				setEnv(env);				
			}
		});

		jrmi2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				env.removeObserver();
				env = new EnvironnementWator(gridSize, gridSize);
				setEnv(env);				
			}
		});

		jrmi3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				env.removeObserver();
				env = new EnvironnementSegregation(gridSize, gridSize, 0.7);
				setEnv(env);				
			}
		});

		jrmi4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				env.removeObserver();
				env = new EnvironnementPacMan(gridSize, gridSize);
				setEnv(env);				
			}
		});

		item3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] tim = {"10", "100", "500", "1000", "2000"};
				String timer = (String)JOptionPane.showInputDialog(null, 
						"Veuillez indiquer le fresh rate !",
						"Timer option",
						JOptionPane.QUESTION_MESSAGE,
						null,
						tim,
						tim[2]);
				env.setTimer(Integer.parseInt(timer));

			}
		});

		item4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Made by Erwan Douaille", "A propos", JOptionPane.INFORMATION_MESSAGE);      
			}
		});

		item5.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				JOptionPane jop = new JOptionPane();
				String size = jop.showInputDialog(null, "Taille de grille, x=y", "Taille grille", JOptionPane.QUESTION_MESSAGE);
				gridSize = Integer.parseInt(size);
				System.out.println(gridSize);
				if (env.getClass().equals(EnvironnementWator.class)) {
					env.removeObserver();
					env = new EnvironnementWator(gridSize, gridSize);
					setEnv(env);
				}
				if (env.getClass().equals(EnvironnementParticules.class)) {
					env.removeObserver();
					env = new EnvironnementParticules(gridSize, gridSize);
					setEnv(env);
				}
				if (env.getClass().equals(EnvironnementSegregation.class)){
					env.removeObserver();
					env = new EnvironnementSegregation(gridSize, gridSize, 0.8);
					setEnv(env);
				}
				if (env.getClass().equals(EnvironnementPacMan.class)) {
					env.removeObserver();
					env = new EnvironnementPacMan(gridSize, gridSize);
					setEnv(env);
				}
			}
		});

		this.test1.add(this.test1_2);
		this.test1.addSeparator();
		item2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}        
		});
		this.test1.add(item4);  
		this.test1.add(item2); 
		this.test2.add(item3);  
		this.test2.add(item5);  
		this.menuBar.add(test1);
		this.menuBar.add(test2);
		this.setJMenuBar(menuBar);
	}

	private void updateGraphics() {
		this.panel.repaint();	
	}

	public void update(Observable o, Object arg) {
		if(!arg.equals("env"))
			return;
		this.env = (Environnement)o;
		this.updateGraphics();
	}

	public Environnement getEnv() {
		return env;
	}

	public void setEnv(Environnement env) {
		env.addObserver(this);
		this.env = env;
	}

	public void run() {
		while (true) 
			env.run();
	}

}
