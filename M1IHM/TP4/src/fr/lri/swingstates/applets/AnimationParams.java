package fr.lri.swingstates.applets;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.lri.swingstates.animations.Animation;
import fr.lri.swingstates.animations.AnimationScaleTo;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Press;

public class AnimationParams extends BasicApplet {

	CRectangle animatedShape;
	AnimationScaleTo animationDemo;
	JSlider nbLaps, durationLap, delay;
	JCheckBox infinite;
	JRadioButton pacingFunctionLinear, pacingFunctionSigmoid;
	
	JSlider createSlider(boolean title, JPanel panel, String sliderTitle, int min, int max, int value, int majorTick, int minorTick) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);
		slider.setMajorTickSpacing(majorTick);
		slider.setMinorTickSpacing(minorTick);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				animationDemo.suspend();
			}
		});
		panel.add(slider);
		if(title) slider.setBorder(BorderFactory.createTitledBorder(sliderTitle));
		return slider;
	}
	
	JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		BoxLayout layout = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
		controlPanel.setLayout(layout);
		
		JPanel numberOfLaps = new JPanel();
		numberOfLaps.setLayout(new BoxLayout(numberOfLaps, BoxLayout.Y_AXIS));
		numberOfLaps.setBorder(BorderFactory.createTitledBorder("number of laps"));
		nbLaps = createSlider(false, numberOfLaps, "number of laps", 0, 5, 1, 1, 1);
		infinite = new JCheckBox("INFINITE_NUMBER_OF_LAPS");
		numberOfLaps.add(infinite);
		controlPanel.add(numberOfLaps);
		
		infinite.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				animationDemo.suspend();
				nbLaps.setEnabled(e.getStateChange() == ItemEvent.DESELECTED);
			}
		});
		
		durationLap = createSlider(true, controlPanel, "lap duration", 0, 5000, 1000, 1000, 100);
		delay = createSlider(true, controlPanel, "delay", 0, 200, 25, 50, 25);
		
		JPanel pacingPanel = new JPanel();
		pacingPanel.setBorder(BorderFactory.createTitledBorder("pacing function"));
		pacingFunctionLinear = new JRadioButton("FUNCTION_LINEAR");
		pacingFunctionSigmoid = new JRadioButton("FUNCTION_SIGMOID");
		ButtonGroup group = new ButtonGroup();
	    group.add(pacingFunctionLinear);
	    group.add(pacingFunctionSigmoid);
	    
	    pacingFunctionLinear.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animationDemo.suspend();
			}
		});
	    pacingFunctionLinear.setSelected(true);
		pacingFunctionSigmoid.addActionListener(new  ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animationDemo.suspend();
			}
		});
		pacingPanel.add(pacingFunctionLinear);
		pacingPanel.add(pacingFunctionSigmoid);
		controlPanel.add(pacingPanel);
		return controlPanel;
	}
	public void createGUI() {
		Animation.resetAnimationManager();
		JPanel controlPanel = createControlPanel();
		
		
		Canvas canvas = new Canvas(200, (int)controlPanel.getPreferredSize().getHeight());
		animatedShape = canvas.newRectangle(100, controlPanel.getPreferredSize().getHeight()/2, 1, 1);
		animatedShape.setOutlined(false);
		animationDemo = new AnimationScaleTo(200, controlPanel.getPreferredSize().getHeight());
//		animationDemo = new AnimationScaleTo(200, controlPanel.getPreferredSize().getHeight()) {
//			public void step(double t) {
//				super.step(t);
//				getAnimated().setFillPaint(new Color(
//							(int)(Color.YELLOW.getRed()*t + Color.LIGHT_GRAY.getRed()*(1-t)),
//							(int)(Color.YELLOW.getGreen()*t + Color.LIGHT_GRAY.getGreen()*(1-t)),
//							(int)(Color.YELLOW.getBlue()*t + Color.LIGHT_GRAY.getBlue()*(1-t))));
//			}
//		};
		CStateMachine sm = new CStateMachine(canvas) {
			public State start = new State() {
				Transition startAnim = new Press(BUTTON1) {
					public void action() {
						animationDemo.stop();
						animationDemo.setDelay(delay.getValue()).setLapDuration(durationLap.getValue());
						if(infinite.isSelected()) animationDemo.setNbLaps(Animation.INFINITE_NUMBER_OF_LAPS);
						else animationDemo.setNbLaps(nbLaps.getValue());
						if (pacingFunctionSigmoid.isSelected()) animationDemo.setFunction(Animation.FUNCTION_SIGMOID);
						else animationDemo.setFunction(Animation.FUNCTION_LINEAR);
						animatedShape.scaleTo(1, 1);
//						animationDemo.setAnimatedElement(animatedShape).start();
						animatedShape.animate(animationDemo);
					}
				};
			};
		};
		
		setLayout(new FlowLayout());
		add(canvas);
		add(controlPanel);
		setSize(200+(int)controlPanel.getPreferredSize().getWidth()+10, (int)controlPanel.getPreferredSize().getHeight()+10);
	}

}
