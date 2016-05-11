package widget;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import mygeom.BlobQueue;
import mygeom.Point2;
import java.awt.*;
import java.awt.event.*;
import java.util.Map.Entry;

import tuio.*;

public class MTSurface extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MTedt tuio;
	public ComponentMap componentMap;
	public EventListenerList liste = new EventListenerList();
	public MTContainer container;
	public boolean bool = true;
	
	public MTSurface() {
		super();
		this.tuio = new MTedt(this);
		this.container = new MTContainer(this);
		JButton button = new JButton("Visible");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible();
			}
		});
		this.add(button);
		this.componentMap = new ComponentMap(this.container);
	}
	
	public void setVisible(){
		this.bool = !this.bool;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		this.container.draw(g2);
		for (Entry<MTComponent,BlobQueue> entry : this.componentMap.getCmap().entrySet()){
			entry.getValue().draw(g2, this.bool);
		}
	}
	
	public synchronized void addCursor(int id, Point2 p){
		this.componentMap.addBlob(id, p);
		this.repaint();
	}
	
	public synchronized void updateCursor(int id, Point2 p){
		this.componentMap.updateBlob(id, p);
		this.repaint();
		
	}
	public synchronized void removeCursor(int id, Point2 p){
		this.componentMap.removeBlob(id, p);
		this.repaint();
	}
	
	public void addComponent(MTComponent component) {
		this.container.add(component);
		component.registerContainer(this.container);
	}
	
	public MTContainer getMTContainer(){
		return this.container;
	}
	

}
