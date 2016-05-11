/*
 * Authors: Caroline Appert (caroline.appert@lri.fr) Copyright (c) Universite
 * Paris-Sud XI, 2007. All Rights Reserved Licensed under the GNU LGPL. For full
 * terms see the file COPYING.
 */
package fr.lri.swingstates.gestures;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CIntentionalTag;
import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.gestures.dollar1.Dollar1Classifier;
import fr.lri.swingstates.gestures.rubine.RubineClassifier;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Drag;
import fr.lri.swingstates.sm.transitions.KeyPress;
import fr.lri.swingstates.sm.transitions.Press;
import fr.lri.swingstates.sm.transitions.Release;

/**
 * A graphical application to build a classifier.
 * 
 * @author Caroline Appert
 * 
 */
public class Training {

	/**
	 * Mouse listener for popup menu.
	 */
	private class PopupListener extends MouseAdapter {
		JPopupMenu popup;

		PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	private static int INDEX_RUBINE = 0;
	private static int INDEX_DOLLAR1 = 1;
	private JCheckBox dollar1Enable;
	private JCheckBox rubineEnable;
	private JTextArea dollar1List = new JTextArea();
	private JTextArea rubineList = new JTextArea();
	
	private static DecimalFormat myFormatter = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("en", "US"));
	private static Font currentFont = new Font("Verdana", Font.PLAIN, 12);
	private static int nbWindowsOpened = 0;

	private JTextField name;
	private Canvas trainingCanvas = new Canvas(300, 300);
	private Canvas testingCanvas = new Canvas(300, 300);
	private JDialog infoFrame;
	private JButton addClass, removeClass;
	private JScrollPane jspNamesClass;
	private JFrame frame;
	private DefaultListModel classesList;
	private JList classesListUI;
	private Vector<AbstractClassifier> classifiers = new Vector<AbstractClassifier>();
	private String selectedClass = null;
	private boolean renaming = false;
	private Hashtable<String, GestureExamples> gestureExamples;
	private GestureExamples currentExamples;
//	private JSpinner probability;
	private JSpinner mahalanobis;
	private JSpinner distanceDollar1;
	private JDialog recognitionInfoFrame;
	private JPanel panelRecognitionInfo;
//	private JTextArea recognitionInfoTextArea = new JTextArea();
	private JTextArea textAreaFeaturesAverage;
	private JTextArea textAreaName;
	private JTextArea textAreaNbExamples;
	private Canvas classRepresentative;

	// private Canvas gestureAverage;

	protected Training() {
		Training.nbWindowsOpened++;
		classifiers.add(new RubineClassifier());
		classifiers.add(new Dollar1Classifier());
		frame = new JFrame("Gesture Training");
		infoFrame = new JDialog(frame, "average features vector of the class");
		recognitionInfoFrame = new JDialog(frame, "results");
		panelRecognitionInfo = new JPanel(new BorderLayout());
		JScrollPane jspRecognitionInfo = new JScrollPane(panelRecognitionInfo);
		jspRecognitionInfo.setPreferredSize(new Dimension(400, 300));
		recognitionInfoFrame.getContentPane().add(jspRecognitionInfo);
		dollar1List.setBorder(BorderFactory.createEtchedBorder());
		rubineList.setBorder(BorderFactory.createEtchedBorder());
		frame.setFont(currentFont);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int answer = JOptionPane.showConfirmDialog(frame, "Save changes?", "Quit?", JOptionPane.YES_NO_OPTION);
				Training.nbWindowsOpened--;
				if(answer == JOptionPane.OK_OPTION) 
					saveClassifier();
				if(Training.nbWindowsOpened == 0)
					System.exit(1);
			}
		});
		buildMenu();

		gestureExamples = new Hashtable<String, GestureExamples>();
		currentExamples = new GestureExamples(frame, this);
		buildWidgets();
		buildLayout();
		buildInfoFrame();
		myFormatter.applyPattern("###.###");
		trainingZone();
		testingZone();

		dollar1List.setForeground(Color.DARK_GRAY);
		dollar1List.setFont(currentFont);
		dollar1List.setEditable(false);
		rubineList.setForeground(Color.DARK_GRAY);
		rubineList.setFont(currentFont);
		rubineList.setEditable(false);
		recognitionInfoFrame.getContentPane().add(rubineList, BorderLayout.WEST);
		recognitionInfoFrame.getContentPane().add(dollar1List, BorderLayout.EAST);

		frame.setVisible(true);
	}

	void updateInfos(boolean show) {
		updateInfos(selectedClass, show);
	}

	void updateInfos(String className, boolean show) {
		if (!show && !infoFrame.isVisible())
			return;

		RubineClassifier rubineClassifier = getRubineClassifier();
		textAreaName.setText(className);
		String stringAverageVector = "";
		for (Iterator<Double> it = rubineClassifier.getFeatures(className).iterator(); it.hasNext();) {
			stringAverageVector += myFormatter.format(it.next()) + "\n";
		}
		textAreaFeaturesAverage.setText(stringAverageVector);
		textAreaNbExamples.setText("" + rubineClassifier.getNbGestureExamples(className));
		classRepresentative.removeAllShapes();
		CPolyLine representative = rubineClassifier.getRepresentative(className);
		if (representative != null) {
			GestureUtils.showPreview(classRepresentative, representative, 0, 0, 50, 10, 5);
		}
		// gestureAverage.removeAllShapes();
		// CPolyLine average =
		// getDollar1Classifier().getRepresentative(className);
		// if (representative != null) {
		// GestureExamples.showPreview(gestureAverage, average, 0, 0, 50, 10,
		// 5);
		// }
		infoFrame.pack();
	}

	private void buildInfoFrame() {
		infoFrame.setResizable(true);

		GridBagLayout gridBagLayoutManager = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		infoFrame.getContentPane().setLayout(gridBagLayoutManager);

		buildConstraints(constraints, 0, 0, 1, 1, 100, 15, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		infoFrame.getContentPane().add(new JLabel("Name: "), constraints);
		buildConstraints(constraints, 0, 1, 1, 1, 100, 15, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		textAreaName = new JTextArea();
		textAreaName.setEditable(false);
		infoFrame.getContentPane().add(textAreaName, constraints);

		buildConstraints(constraints, 0, 2, 1, 1, 100, 15, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		infoFrame.getContentPane().add(new JLabel("Average feature vector: "), constraints);
		textAreaFeaturesAverage = new JTextArea();
		textAreaFeaturesAverage.setEditable(false);
		buildConstraints(constraints, 0, 3, 1, 1, 100, 15, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		infoFrame.getContentPane().add(textAreaFeaturesAverage, constraints);

		buildConstraints(constraints, 0, 4, 1, 1, 100, 15, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		infoFrame.getContentPane().add(new JLabel("Number of examples: "), constraints);
		textAreaNbExamples = new JTextArea();
		textAreaNbExamples.setEditable(false);
		buildConstraints(constraints, 0, 5, 1, 1, 100, 15, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
		infoFrame.getContentPane().add(textAreaNbExamples, constraints);

		buildConstraints(constraints, 0, 6, 1, 1, 100, 20, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		infoFrame.getContentPane().add(new JLabel("Representative gesture: "), constraints);
		classRepresentative = new Canvas(50, 50);
		classRepresentative.setMaximumSize(new Dimension(50, 50));
		classRepresentative.setMinimumSize(new Dimension(50, 50));
		buildConstraints(constraints, 0, 7, 1, 1, 100, 40, GridBagConstraints.BOTH, GridBagConstraints.SOUTH);
		infoFrame.getContentPane().add(classRepresentative, constraints);

		// buildConstraints(constraints, 0, 7, 1, 1, 100, 15,
		// GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		// infoFrame.getContentPane().add(new JLabel("Average gesture: "),
		// constraints);
		// gestureAverage = new Canvas(50, 50);
		// gestureAverage.setMaximumSize(new Dimension(50, 50));
		// gestureAverage.setMinimumSize(new Dimension(50, 50));
		// buildConstraints(constraints, 0, 8, 1, 1, 100, 20,
		// GridBagConstraints.BOTH, GridBagConstraints.SOUTH);
		// infoFrame.getContentPane().add(gestureAverage, constraints);

	}

	protected void openFile(File f) {
		RubineClassifier rubineClassifier = RubineClassifier.newClassifier(f);
		Dollar1Classifier dollar1Classifier = Dollar1Classifier.newClassifier(f);
//		probability.setValue(rubineClassifier.getMinimumProbability());
		mahalanobis.setValue(rubineClassifier.getMaximumDistance());
		classifiers.clear();
		classifiers.add(rubineClassifier);
		classifiers.add(dollar1Classifier);
		for (Iterator<String> iterator = rubineClassifier.getClassesNames().iterator(); iterator.hasNext();) {
			addClass(iterator.next(), false, false);
		}
		if (rubineClassifier.getClassesNames().size() > 0)
			classesListUI.setSelectedIndex(0);
//		probability.setValue(rubineClassifier.getMinimumProbability());
		mahalanobis.setValue(rubineClassifier.getMaximumDistance());
	}

	protected void saveClassifier() {
		JFileChooser chooser = new JFileChooser();
		try {
			chooser.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
		} catch (IOException ioe) {
		}
		int returnVal = chooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (classifiers.size() < 1)
				return;
			classifiers.get(INDEX_RUBINE).save(chooser.getSelectedFile());
		}
	}

	private void buildMenu() {
		JMenuBar menuBar;
		JMenu file;
		JMenuItem newItem, open, save, close, quit;
		menuBar = new JMenuBar();
		file = new JMenu("File...");
		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);
		newItem = new JMenuItem("New", KeyEvent.VK_N);
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK));
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Training();
			}
		});
		open = new JMenuItem("Open", KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				try {
					chooser.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
				} catch (IOException ioe) {
				}
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					openFile(chooser.getSelectedFile());
				}
			}
		});
		save = new JMenuItem("Save", KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK));
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveClassifier();
			}
		});
		close = new JMenuItem("Close", KeyEvent.VK_W);
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.META_MASK));
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				recognitionInfoFrame.setVisible(false);
			}
		});
		quit = new JMenuItem("Quit", KeyEvent.VK_Q);
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.META_MASK));
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(newItem);
		file.add(open);
		file.add(save);
		file.addSeparator();
		file.add(close);
		file.add(quit);
		menuBar.add(file);
		frame.setJMenuBar(menuBar);
	}

	protected void addClass(String nameClass, boolean selectNewClass, boolean createNewClass) {
		if (!selectNewClass && !createNewClass) {
			// loading an existing classifier
			if (classifiers.size() < 1)
				System.err.println("no classifier loaded in training application.");

			GestureExamples gex = new GestureExamples(frame, this);
			Vector<Gesture> gestures = classifiers.get(INDEX_RUBINE).getExamples(nameClass);
			for (Iterator<Gesture> j = gestures.iterator(); j.hasNext();)
				gex.addExample(j.next());
			gestureExamples.put(nameClass, gex);
		}
		if (nameClass.length() != 0 && !classesList.contains(nameClass)) {
			classesList.addElement(nameClass);
			if (createNewClass) {
				for (Iterator<AbstractClassifier> iterator = classifiers.iterator(); iterator.hasNext();) {
					iterator.next().addClass(nameClass);
				}
			}
			if (selectNewClass)
				classesListUI.setSelectedValue(nameClass, true);
		}
	}

	protected void removeClass(String nameClass) {
		if (nameClass.length() != 0) {
			int index = classesList.indexOf(nameClass);
			if (index > 0)
				classesListUI.setSelectedIndex(index - 1);
			else if ((index + 1) < classesList.getSize())
				classesListUI.setSelectedIndex(index + 1);
			for (Iterator<AbstractClassifier> iterator = classifiers.iterator(); iterator.hasNext();)
				iterator.next().removeClass(nameClass);
			classesList.remove(index);
			gestureExamples.remove(nameClass);
			frame.getContentPane().remove(currentExamples);
		}
	}

	protected void buildWidgets() {
		name = new JTextField();
		name.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!renaming)
						addClass(name.getText(), true, true);
					else {
						GestureExamples examples = gestureExamples.get(selectedClass);
						gestureExamples.remove(selectedClass);
						for (Iterator<AbstractClassifier> iterator = classifiers.iterator(); iterator.hasNext();) {
							iterator.next().renameClass(selectedClass, name.getText());
						}
						classesList.set(classesList.indexOf(selectedClass), name.getText());
						gestureExamples.put(name.getText(), examples);
					}
					renaming = false;
				}
			}
		});
		addClass = new JButton("add");
		addClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClass(name.getText(), true, true);
				renaming = false;
			}
		});
		removeClass = new JButton("remove");
		removeClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeClass(selectedClass);
			}
		});

		removeClass.setFont(currentFont);
		addClass.setFont(currentFont);

		classesList = new DefaultListModel();
		classesListUI = new JList(classesList);
		classesListUI.setVisibleRowCount(10);
		classesListUI.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					String newSelectedClass = (String) classesListUI.getSelectedValue();
					if (selectedClass != null && newSelectedClass != null)
						if (selectedClass.compareTo(newSelectedClass) == 0)
							return;
					selectedClass = newSelectedClass;
					if (selectedClass == null) {
						return;
					}
					updateInfos(selectedClass, false);
					frame.getContentPane().remove(currentExamples);
					frame.invalidate();
					currentExamples = gestureExamples.get(newSelectedClass);
					if (currentExamples == null) {
						currentExamples = new GestureExamples(frame, Training.this);
						gestureExamples.put(newSelectedClass, currentExamples);
					}
					frame.getContentPane().add(currentExamples, BorderLayout.SOUTH);
					frame.validate();
					frame.repaint();
				}
			}
		});
		JPopupMenu menu = new JPopupMenu();
		JMenuItem itemRename = new JMenuItem("rename");
		JMenuItem itemShowAverage = new JMenuItem("show average");
		menu.add(itemRename);
		menu.add(itemShowAverage);
		// Add listener to the listbox so the popup menu can come up.
		MouseListener popupListener = new PopupListener(menu);
		classesListUI.addMouseListener(popupListener);
		itemRename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renaming = true;
				name.requestFocus();
				name.setText(selectedClass);
			}
		});
		itemShowAverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateInfos(selectedClass, true);
				infoFrame.setVisible(true);
			}
		});

		classesListUI.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
					classesListUI.setSelectedIndex(classesListUI.locationToIndex(e.getPoint()));
			}
		});
		classesListUI.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
					removeClass(selectedClass);
			}
		});
		jspNamesClass = new JScrollPane(classesListUI);

//		SpinnerNumberModel modelProbability = new SpinnerNumberModel(new Double(getRubineClassifier().getMinimumProbability()), new Double(0.0), new Double(1.0), new Double(0.01));
//		probability = new JSpinner(modelProbability);
//		probability.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				getRubineClassifier().setMinimumProbability((Double) (((SpinnerNumberModel) probability.getModel()).getNumber()));
//			}
//		});

		SpinnerNumberModel modelMahalanobis = new SpinnerNumberModel(new Integer(getRubineClassifier().getMaximumDistance()), new Integer(0), new Integer(500000), new Integer(1));
		mahalanobis = new JSpinner(modelMahalanobis);
		mahalanobis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				getRubineClassifier().setMaximumDistance((Integer) ((SpinnerNumberModel) mahalanobis.getModel()).getNumber());
			}
		});

		SpinnerNumberModel modelDistanceDollar1 = new SpinnerNumberModel(new Integer((int) getDollar1Classifier().getMaximumDistance()), new Integer(0), new Integer(100), new Integer(1));
		distanceDollar1 = new JSpinner(modelDistanceDollar1);
		distanceDollar1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				getDollar1Classifier().setMaximumDistance((Double) (((SpinnerNumberModel) distanceDollar1.getModel()).getNumber()));
			}
		});
	}

	private Dollar1Classifier getDollar1Classifier() {
		return (Dollar1Classifier) classifiers.get(INDEX_DOLLAR1);
	}

	private RubineClassifier getRubineClassifier() {
		return (RubineClassifier) classifiers.get(INDEX_RUBINE);
	}

	static void buildConstraints(GridBagConstraints constraints, int x, int y, int w, int h, double columnExpand, double rowExpand, int componentFill, int componentAnchor) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.weightx = columnExpand;
		constraints.weighty = rowExpand;
		constraints.fill = componentFill;
		constraints.anchor = componentAnchor;
	}

	protected void buildLayout() {

		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setBorder(BorderFactory.createTitledBorder("Classes"));
		JPanel rightTopPanel = new JPanel();
		rightTopPanel.setBorder(BorderFactory.createTitledBorder("Testing"));
		JPanel middleTopPanel = new JPanel();
		middleTopPanel.setBorder(BorderFactory.createTitledBorder("Training"));
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createTitledBorder("Classifiers"));

		// Left top panel layout
		GridBagLayout gblLeftTopPanel = new GridBagLayout();
		GridBagConstraints constraintsLeftTopPanel = new GridBagConstraints();
		leftTopPanel.setLayout(gblLeftTopPanel);

		buildConstraints(constraintsLeftTopPanel, 0, 0, 2, 1, 100, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH);
		leftTopPanel.add(name, constraintsLeftTopPanel);
		buildConstraints(constraintsLeftTopPanel, 0, 1, 1, 1, 50, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		leftTopPanel.add(addClass, constraintsLeftTopPanel);
		buildConstraints(constraintsLeftTopPanel, 1, 1, 1, 1, 50, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		leftTopPanel.add(removeClass, constraintsLeftTopPanel);
		buildConstraints(constraintsLeftTopPanel, 0, 2, 2, 1, 100, 100, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
		leftTopPanel.add(jspNamesClass, constraintsLeftTopPanel);

		// Middle top panel layout
		GridBagLayout gblMiddleTopPanel = new GridBagLayout();
		GridBagConstraints constraintsMiddleTopPanel = new GridBagConstraints();
		middleTopPanel.setLayout(gblMiddleTopPanel);

		buildConstraints(constraintsMiddleTopPanel, 0, 0, 1, 1, 100, 100, GridBagConstraints.BOTH, GridBagConstraints.NORTH);
		middleTopPanel.add(trainingCanvas, constraintsMiddleTopPanel);

		// Right top panel layout
		GridBagLayout gblRightTopPanel = new GridBagLayout();
		GridBagConstraints constraintsRightTopPanel = new GridBagConstraints();
		rightTopPanel.setLayout(gblRightTopPanel);

		buildConstraints(constraintsRightTopPanel, 0, 0, 1, 1, 100, 100, GridBagConstraints.BOTH, GridBagConstraints.NORTH);
		rightTopPanel.add(testingCanvas, constraintsRightTopPanel);

		// Top panel layout
		topPanel.setLayout(new GridLayout(1, 4));

//		JLabel probaLabel = new JLabel("Minimum probability of non ambiguity (Rubine)");
//		probaLabel.setFont(currentFont);
//		topPanel.add(probaLabel);

//		probability.setPreferredSize(new Dimension(100, probability.getPreferredSize().height));
//		topPanel.add(probability);

		final JPanel rubinePanel = new JPanel(new BorderLayout());
		rubinePanel.setBorder(BorderFactory.createEtchedBorder());
		final JPanel dollar1Panel = new JPanel(new BorderLayout());
		dollar1Panel.setBorder(BorderFactory.createEtchedBorder());
		topPanel.add(rubinePanel);
		topPanel.add(dollar1Panel);
		topPanel.add(new JPanel());
		topPanel.add(new JPanel());
		
		rubineEnable = new JCheckBox("Rubine");
		rubineEnable.setFont(currentFont);
		rubinePanel.add(rubineEnable, BorderLayout.NORTH);
		
		
		final JLabel distLabel = new JLabel("Distance max: ");
		distLabel.setFont(currentFont);
		rubinePanel.add(distLabel, BorderLayout.WEST);

		mahalanobis.setPreferredSize(new Dimension(100, mahalanobis.getPreferredSize().height));
		mahalanobis.setEditor(new JSpinner.NumberEditor(mahalanobis, "#"));
		rubinePanel.add(mahalanobis, BorderLayout.CENTER);
		
		rubineEnable.setSelected(true);
		rubineEnable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					rubinePanel.setForeground(Color.LIGHT_GRAY);
					distLabel.setEnabled(false);
					mahalanobis.setEnabled(false);
				} else {
					rubinePanel.setForeground(Color.BLACK);
					distLabel.setEnabled(true);
					mahalanobis.setEnabled(true);
				}
			}
		});
		
		dollar1Enable = new JCheckBox("$1");
		dollar1Enable.setFont(currentFont);
		dollar1Panel.add(dollar1Enable, BorderLayout.NORTH);
		
		final JLabel distanceDollar1Label = new JLabel("Distance max: ");
		distanceDollar1Label.setFont(currentFont);
		dollar1Panel.add(distanceDollar1Label, BorderLayout.WEST);

		distanceDollar1.setPreferredSize(new Dimension(100, distanceDollar1.getPreferredSize().height));
		dollar1Panel.add(distanceDollar1, BorderLayout.CENTER);

		dollar1Enable.setSelected(true);
		dollar1Enable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.DESELECTED) {
					distanceDollar1Label.setForeground(Color.LIGHT_GRAY);
					distanceDollar1Label.setEnabled(false);
					distanceDollar1.setEnabled(false);
				} else {
					distanceDollar1Label.setForeground(Color.BLACK);
					distanceDollar1Label.setEnabled(true);
					distanceDollar1.setEnabled(true);
				}
			}
		});
		
		// main layout
		frame.getContentPane().setLayout(new BorderLayout());
		JPanel middle = new JPanel();
		middle.setLayout(new GridLayout(1, 3));
		middle.add(leftTopPanel);
		middle.add(middleTopPanel);
		middle.add(rightTopPanel);
		frame.getContentPane().add(middle, BorderLayout.CENTER);
		frame.getContentPane().add(currentExamples, BorderLayout.SOUTH);
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		frame.pack();
	}

	private void trainingZone() {
		new CStateMachine(trainingCanvas) {
			CPolyLine ink;
			Gesture gest;
			public State start = new State("start") {
				Transition press = new Press(BUTTON1, "==> draw") {
					public boolean guard() {
						if (selectedClass == null)
							JOptionPane.showMessageDialog(frame, "Please, select a gesture class to train");
						return selectedClass != null;
					}

					public void action() {
						ink = (CPolyLine) trainingCanvas.newPolyLine(getPoint().getX(), getPoint().getY()).setFilled(false);
						gest = new Gesture();
						gest.addPoint((int) getPoint().getX(), (int) getPoint().getY());
					}
				};
			};

			public State draw = new State("draw") {
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						ink.lineTo((float) getPoint().getX(), (float) getPoint().getY());
						gest.addPoint((int) getPoint().getX(), (int) getPoint().getY());
					}
				};
				Transition release = new Release(BUTTON1, "==> start") {
					public void action() {
						trainingCanvas.removeShape(ink);
						currentExamples.addExample(gest);
						addExample(gest);
						updateInfos(selectedClass, false);
					}
				};
			};
		};
	}

	private void addExample(Gesture gesture) {
		for (Iterator<AbstractClassifier> iterator = classifiers.iterator(); iterator.hasNext();) {
			iterator.next().addExample(selectedClass, gesture);
		}
	}

	private void testingZone() {
		new CStateMachine(testingCanvas) {
			CPolyLine ink = (CPolyLine) testingCanvas.newPolyLine().setFilled(false);
			Gesture gest = new Gesture();
			public State start = new State("start") {
				Transition press = new Press(BUTTON1, "==> draw") {
					public void action() {
						ink.reset(getPoint().getX(), getPoint().getY());
						gest.reset();
						gest.addPoint((int) getPoint().getX(), (int) getPoint().getY());
					}
				};
			};

			public State draw = new State("draw") {
				Transition drag = new Drag(BUTTON1) {
					public void action() {
						ink.lineTo((float) getPoint().getX(), (float) getPoint().getY());
						gest.addPoint((int) getPoint().getX(), (int) getPoint().getY());
					}
				};
				Transition release = new Release(BUTTON1, "==> start") {
					public void action() {
						ink.lineTo((float) getPoint().getX(), (float) getPoint().getY());
						gest.addPoint((int) getPoint().getX(), (int) getPoint().getY());
						classify(gest);
					}
				};
			};
		};
	}

	protected void classify(Gesture gest) {
		if(rubineEnable.isSelected()) {
			String textResult = "Rubine classifier has recognized: \n";
			// best match
			try {
			textResult += "    " + getRubineClassifier().classify(gest);
			textResult += "\n" + "Distance: " + getRubineClassifier().getCurrentDistance();
//			textResult += "\n" + "\tProbability of non ambiguity: " + getRubineClassifier().getCurrentProbability();
			// list match
			String sortedClasses = "\n\nClasses order (closest first): \n";
			Vector<Score> sorted = getRubineClassifier().sortedClasses(gest);
			for (Iterator<Score> iterator = sorted.iterator(); iterator.hasNext();) {
				Score next = iterator.next();
				sortedClasses += "\n    " + next.getName()+" ("+next.getScore()+")";
			}
			textResult += sortedClasses;
			rubineList.setText(textResult);
			} catch(Exception e) {
				textResult = "Rubine classifier requires at least: \n";
				textResult +=  "- two classes \n";
				textResult +=  "- two examples per class.";
				rubineList.setText(textResult);
			}
			if(!panelRecognitionInfo.isAncestorOf(rubineList))
				panelRecognitionInfo.add(rubineList, BorderLayout.WEST);
		} else {
			panelRecognitionInfo.remove(rubineList);
		}
		
		if(dollar1Enable.isSelected()) {
			String textResult = "$1 classifier has recognized: \n";
			textResult += "    " + classifiers.get(INDEX_DOLLAR1).classify(gest);
			textResult += "\n" + "Distance: " + getDollar1Classifier().getCurrentDistance();
			String sortedClasses = "\n\nClasses order (closest first): \n";
			Vector<Score> sorted = getDollar1Classifier().sortedClasses(gest);
			for (Iterator<Score> iterator = sorted.iterator(); iterator.hasNext();) {
				Score next = iterator.next();
				sortedClasses += "\n    " + next.getName()+" ("+next.getScore()+")";
			}
			textResult += sortedClasses;

			dollar1List.setText(textResult);
			if(!panelRecognitionInfo.isAncestorOf(dollar1List))
				panelRecognitionInfo.add(dollar1List, BorderLayout.EAST);
		} else {
			panelRecognitionInfo.remove(dollar1List);
		}
		recognitionInfoFrame.pack();
		recognitionInfoFrame.setVisible(true);
	}

	/**
	 * Launches a GUI to build a new gesture set.
	 * 
	 * @param args
	 *            the program arguments (here, there is no argument)
	 */
	public static void main(String[] args) {
		new Training();
	}

	/**
	 * @return The list of classifiers currently defined by this training
	 *         application.
	 */
	public Vector<AbstractClassifier> getClassifiers() {
		return classifiers;
	}
	
	class GestureExamples extends JScrollPane {

		private int sizePreview = 50;

		private Canvas canvas;
		private JFrame topLevelContainer;
		private int lastX, lastY;
		private Training training;

		/**
		 * Tag assigned to every graphical gesture examples.
		 */
		private class GestureBoundingBox extends CExtensionalTag {
			Gesture containedGesture;

			public GestureBoundingBox(Gesture gesture) {
				super();
				this.containedGesture = gesture;
			}

			public Gesture getContainedGesture() {
				return containedGesture;
			}
		};

		/**
		 * Builds a widget for displaying a set of gestures.
		 * @param topLevelContainer The main frame in which this widget is packed.
		 * @param training The training application.
		 */
		public GestureExamples(JFrame topLevelContainer, Training training) {
			super();
			this.training = training;
			this.topLevelContainer = topLevelContainer;
			canvas = new Canvas(200, 100);
			setViewportView(canvas);
			lastX = 0;
			lastY = 0;

			new CStateMachine(canvas) {
				CShape selected = null;
				GestureBoundingBox tagSelected;
				public State start = new State() {
					Transition selectGesture = new PressOnTag(GestureBoundingBox.class, BUTTON1, ">> selection") {
						public void action() {
							tagSelected = (GestureBoundingBox) getTag();
							canvas.requestFocus();
							selected = getShape();
							selected.setOutlinePaint(Color.RED);
						}
					};
				};
				public State selection = new State() {
					Transition deleteGesture = new KeyPress(KeyEvent.VK_BACK_SPACE, ">> start") {
						public void action() {
							deleteGesture(tagSelected, selected);
						}
					};
					Transition selectAnotherGesture = new PressOnTag(GestureBoundingBox.class, BUTTON1) {
						public void action() {
							tagSelected = (GestureBoundingBox) getTag();
							selected.setOutlinePaint(Color.BLACK);
							canvas.requestFocus();
							selected = getShape();
							selected.setOutlinePaint(Color.RED);
						}
					};
					Transition deselect = new Press(BUTTON1, ">> start") {
						public void action() {
							selected.setOutlinePaint(Color.BLACK);
						}
					};
				};
			};

		}

		private void fillBlanks() {
			CIntentionalTag tag = canvas.getTag(GestureBoundingBox.class);
			tag.reset();
			CShape next;
			int x = 0;
			int y = 0;
			while (tag.hasNext()) {
				next = tag.nextShape();
				next.setReferencePoint(0.5, 0.5).translateTo(x + sizePreview / 2, y + sizePreview / 2);
				x += sizePreview;
				if ((x + sizePreview) > topLevelContainer.getWidth()) {
					x = 0;
					y += sizePreview;
					if ((y + sizePreview) > canvas.getPreferredSize().getHeight()) {
						canvas.setPreferredSize(new Dimension((int) canvas.getPreferredSize().getWidth(), y + sizePreview));
						canvas.revalidate();
					}
				}
			}
			lastX = x;
			lastY = y;
		}

		private void deleteGesture(GestureBoundingBox tagSelected, CShape bbGesture) {
			canvas.removeShapes(bbGesture.getHierarchy());
			fillBlanks();
			for (Iterator<AbstractClassifier> iterator = training.getClassifiers().iterator(); iterator.hasNext();)
				iterator.next().removeExample(tagSelected.getContainedGesture());

			training.updateInfos(false);
		}

		/**
		 * Adds a gesture to this GestureExamples widget.
		 * 
		 * @param example
		 *            The gesture to add.
		 */
		public void addExample(Gesture example) {
			CPolyLine graphicalExample = example.asPolyLine();
			double maxSide = Math.max(graphicalExample.getHeight(), graphicalExample.getWidth());
			double dscale = 40 / maxSide;
			if ((lastX + sizePreview) > topLevelContainer.getWidth()) {
				lastX = 0;
				lastY += sizePreview;
				if ((lastY + sizePreview) > canvas.getPreferredSize().getHeight()) {
					canvas.setPreferredSize(new Dimension((int) canvas.getPreferredSize().getWidth(), lastY + sizePreview));
					canvas.revalidate();
				}
			}
			CRectangle gestureBB = canvas.newRectangle(lastX + 1, lastY + 1, sizePreview - 2, sizePreview - 2);
			graphicalExample.setFilled(false);
			canvas.addShape(graphicalExample);

			gestureBB.addTag(new GestureBoundingBox(example));

			double sizeStartPoint = 5 / dscale;
			CEllipse startPoint = canvas.newEllipse(graphicalExample.getStartX() - sizeStartPoint / 2, graphicalExample.getStartY() - sizeStartPoint / 2, sizeStartPoint, sizeStartPoint);
			startPoint.setFillPaint(Color.RED).setOutlinePaint(Color.RED).setPickable(false);
			gestureBB.setFillPaint(new Color(250, 240, 230));
			gestureBB.addChild(graphicalExample);
			graphicalExample.addChild(startPoint);
			graphicalExample.translateBy(lastX + sizePreview / 2 - graphicalExample.getCenterX(), lastY + sizePreview / 2 - graphicalExample.getCenterY()).scaleBy(dscale).setPickable(false);

			lastX += sizePreview;
		}

	}


}
