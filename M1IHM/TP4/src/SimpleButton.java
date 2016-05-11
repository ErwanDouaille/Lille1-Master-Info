import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.Canvas ;
import fr.lri.swingstates.canvas.CShape ;
import fr.lri.swingstates.canvas.CText ;
import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.TimeOut;

import javax.swing.JFrame ;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font ;
import java.awt.Point;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
public class SimpleButton {

	private CText label ;
	private CRectangle rectangle;

	SimpleButton(final Canvas canvas, String text) {
		label = canvas.newText(0, 0, text, new Font("verdana", Font.PLAIN, 12)) ;
		rectangle = canvas.newRectangle(new Point((int)label.getMinX()-10, (int)label.getMinY()-10), label.getWidth()+20, label.getHeight()+20);
		label.above(rectangle);
		rectangle.addChild(label);
		label.setPickable(false);

		final CExtensionalTag selected = new CExtensionalTag(canvas) {
			public void added(CShape s) { 
				s.setOutlined(true).setStroke(new BasicStroke(2));
			}
			public void removed(CShape s) {
				s.setStroke(new BasicStroke(1));
				s.setFillPaint(new Color(192, 192, 192));
			}
		};

		CStateMachine sm = new CStateMachine() {
			@SuppressWarnings("unused")
			public State in = new State() {
				Transition enterShape = new EnterOnShape()  {
					public void action() {
						System.out.println(getShape());
						getShape().addTag(selected);
					}
				};
				Transition leaveShape = new LeaveOnShape() {
					public void action() {
						getShape().removeTag(selected);
					}
				};
				Transition press = new PressOnShape(BUTTON1, ">> click") {
					public void action() {
						getShape().setFillPaint(Color.YELLOW);
						armTimer(2000, true);
					}		            
				};	  
				Transition a = new TimeOut(){
					public void action() {
						System.out.println("b ok");
					}		

				};      		        
			};
			@SuppressWarnings("unused")
			public State click = new State() {
				Transition press = new ReleaseOnShape(BUTTON1, ">> in"){
					public void action() {
						getShape().setFillPaint(new Color(192, 192, 192));
					}		            
				};	   
				Transition pressUntil = new PressOnShape(BUTTON1) {
					public void action() {
						getShape().setFillPaint(Color.YELLOW);
						armTimer(2000, true);
					}		            
				};	    
				Transition leaveShape = new LeaveOnShape(">> in") {
					public void action() {
						getShape().removeTag(selected);
					}
				};   	
				Transition a = new TimeOut(){
					public void action() {
						disarmTimer();
						getShape().setFillPaint(Color.RED);
					}		

				};	        
			};
		};		

		sm.attachTo(rectangle);
		
		StateMachineVisualization mv = new StateMachineVisualization(sm);
		JFrame fenetre = new JFrame();
		fenetre.add(mv);
		fenetre.setTitle("Machine state");
		fenetre.setSize(400,400);
		fenetre.setLocationRelativeTo(null);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
	}



	public void action() {
		System.out.println("ACTION!") ;
	}

	public CShape getShape() {
		return rectangle ;
	}

	static public void main(String[] args) {
		JFrame frame = new JFrame() ;
		Canvas canvas = new Canvas(400,400) ;
		frame.getContentPane().add(canvas) ;
		frame.pack() ;
		frame.setVisible(true) ;

		SimpleButton simple = new SimpleButton(canvas, "simple") ;
		simple.getShape().translateBy(100,100);
	}

}
