package main;

import java.awt.*;
import java.util.EventObject;
import javax.swing.*;
import mygeom.Vector2;
import event.GestureEvent;
import event.GestureEventListener;
import widget.*;

public class Main {
	
	
	public static void createGui(){
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setVisible(true);
		
		MTSurface surface = new MTSurface();
		surface.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		surface.setBackground(Color.gray);
		surface.setBorder(BorderFactory.createLineBorder(Color.RED));

		MTPicture pic1 = new MTPicture("data/Bird.jpg");//, new Dimension(200, 200), new Point2(50.0, 100.0));
		MTPicture pic2 = new MTPicture("data/Thunder.jpg");//, new Dimension(200, 200), new Point2(150.0, 200.0));
		
		/*********** set Position ********/
		pic1.setPosition(
				new Vector2(0, 0), 	
				0, 
				new Dimension(200, 200));
		pic2.setPosition(
				new Vector2(200, 200), 
				0, 
				new Dimension(200, 200));
		/************************************/
		

		/*********** set DiscrteListener ********/
		pic1.defaultDiscreteEventListener();
		pic2.defaultDiscreteEventListener();
		/************************************/
		
		/*********** set SRTListener ********/
		pic1.defaultSRTEventListener();
		pic2.defaultSRTEventListener();
		/************************************/

		/************* set GestureListener *************/
		pic1.defaultGestureEventListener();
		pic2.defaultGestureEventListener();
		GestureEventListener gest = new GestureEventListener() {
			public void gesturePerformed(EventObject fooEvent) {
				GestureEvent event = (GestureEvent)fooEvent;
				((MTContainer)event.getSource()).execCmd(event.getTemplate());
			}
		};
		surface.getMTContainer().addGestureEventListener(gest);
		/************************************/

		surface.addComponent(pic1);
		surface.addComponent(pic2);
		
		frame.getContentPane().add(surface);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]) {
		System.out.println("Starting");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGui();
			}
		});
	}	
	
}
