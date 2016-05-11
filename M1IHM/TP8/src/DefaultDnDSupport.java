/**
 * @author <a href="mailto:gery.casiez@lifl.fr">Gery Casiez</a>
 */

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.tree.DefaultMutableTreeNode;


public class DefaultDnDSupport {

	public DefaultDnDSupport() {
		JFrame fen = new JFrame("DnD et CCP");
		fen.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Panel gauche
		JPanel panelgauche = new JPanel();
		panelgauche.setLayout(new BoxLayout(panelgauche, BoxLayout.Y_AXIS));

		// JTextField
		JTextField textField = new JTextField(10);
		textField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JTextField"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		//textField.setDragEnabled(true);

		// JPasswordField
		JPasswordField passwordField = new JPasswordField(10);
		passwordField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JPasswordField"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		//passwordField.setDragEnabled(true);

		// JFormattedTextField
		JFormattedTextField ftf = new JFormattedTextField("Universite de Lille 1");
		ftf.setFont(new Font("Courier",Font.ITALIC,12));
		ftf.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JFormattedTextField"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		//ftf.setDragEnabled(true);

		// JTextArea
		JTextArea jta = new JTextArea("Master 1 informatique");
		jta.setFont(new Font("Arial",Font.BOLD,12));
		jta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JTextArea"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		//jta.setDragEnabled(true);

		// JEditorPane
		JEditorPane editorPane = createEditorPane();
		//editorPane.setDragEnabled(true);
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JEditorPane"),
				BorderFactory.createEmptyBorder(5,5,5,5)));	

		// JColorChooser
		JColorChooser cc = new JColorChooser();
		//cc.setDragEnabled(true);
		cc.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JColorChooser"),
				BorderFactory.createEmptyBorder(5,5,5,5)));

		// Panel droit
		JPanel paneldroit = new JPanel();
		paneldroit.setLayout(new BoxLayout(paneldroit, BoxLayout.Y_AXIS));

		// JList
		String[] data = {"AAC", "AEV", "ANG", "ASE", "COA", "PJE",
				"CAR", "PJI", "AeA", "BDA", "CALP","FDD", "HECI", "IHM","M3DS","PAC","PPD","RdF","SVL","TI"};
		JList liste = new JList(data);
		JScrollPane jscrollListe = new JScrollPane(liste);
		jscrollListe.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JList"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		liste.setDragEnabled(true);

		// JTree
		DefaultMutableTreeNode m1 =  new DefaultMutableTreeNode("M1");
		DefaultMutableTreeNode s1 =  new DefaultMutableTreeNode("S1");
		m1.add(s1);
		s1.add(new DefaultMutableTreeNode("AAC"));
		s1.add(new DefaultMutableTreeNode("AEV"));
		s1.add(new DefaultMutableTreeNode("ANG"));
		s1.add(new DefaultMutableTreeNode("ASE"));
		s1.add(new DefaultMutableTreeNode("COA"));
		s1.add(new DefaultMutableTreeNode("PJE"));
		DefaultMutableTreeNode s2 =  new DefaultMutableTreeNode("S2");
		m1.add(s2);
		s2.add(new DefaultMutableTreeNode("CAR"));
		s2.add(new DefaultMutableTreeNode("PJI"));
		s2.add(new DefaultMutableTreeNode("AeA"));
		s2.add(new DefaultMutableTreeNode("BDA"));
		s2.add(new DefaultMutableTreeNode("CALP"));
		s2.add(new DefaultMutableTreeNode("FDD"));
		s2.add(new DefaultMutableTreeNode("HECI"));
		s2.add(new DefaultMutableTreeNode("IHM"));
		s2.add(new DefaultMutableTreeNode("M3DS"));
		s2.add(new DefaultMutableTreeNode("PAC"));
		s2.add(new DefaultMutableTreeNode("PPD"));
		s2.add(new DefaultMutableTreeNode("RdF"));
		s2.add(new DefaultMutableTreeNode("SVL"));
		s2.add(new DefaultMutableTreeNode("TI"));
		JTree tree = new JTree(m1);
		tree.setDragEnabled(true);
		JScrollPane jscrollTree = new JScrollPane(tree);
		jscrollTree.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JTree"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		jscrollTree.setPreferredSize(new Dimension(200,200));

		// JTable
		String[] columnNames = {"S1",
		"S2"};
		Object[][] data2 = {
				{"AAC", "CAR"},
				{"AEV", "PJI"},
				{"ANG", "AeA"},
				{"ASE", "BDA"},
				{"COA", "CALP"},
				{"PJE", "FDD"},
				{"","HECI"},
				{"","IHM"},
				{"","M3DS"},
				{"","PAC"},
				{"","PPD"},
				{"","RdF"},
				{"","SVL"},
				{"","TI"}
		};

		JTable table = new JTable(data2, columnNames);
		JScrollPane scrollPaneTable = new JScrollPane(table);
		table.setDragEnabled(true);

		// JFileChooser
		JFileChooser fc = new JFileChooser();
		fc.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("JFileChooser"),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		//fc.setDragEnabled(true);

		panelgauche.add(textField);
		panelgauche.add(passwordField);
		panelgauche.add(ftf);
		panelgauche.add(jta);
		panelgauche.add(cc);
		panelgauche.add(editorScrollPane);
		fen.getContentPane().add(panelgauche);

		paneldroit.add(fc);
		paneldroit.add(jscrollListe);
		paneldroit.add(jscrollTree);
		paneldroit.add(scrollPaneTable);

		fen.getContentPane().add(paneldroit);

		fen.pack();
		fen.setVisible(true);
	}


    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DefaultDnDSupport();
            }
        });
    }

	private static JEditorPane createEditorPane() {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		System.setProperty("http.proxyHost", "cache.univ-lille1.fr");
		System.setProperty("http.proxyPort", "3128");
		editorPane.setPreferredSize(new Dimension (300, 200));

		java.net.URL lille1URL = null;
		try {
			lille1URL = new java.net.URL("http://www.google.com");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (lille1URL != null) {
			try {
				editorPane.setPage(lille1URL);
			} catch (IOException e) {
				System.err.println("Attempted to read a bad URL: " + lille1URL);
			}
		} else {
			System.err.println("Couldn't find file: http://www.google.com");
		}

		return editorPane;
	}

}