import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.TransferHandler;


public class Part2 {
	
	public Part2(){
		JFrame win = new JFrame("Drag police");
		
		JLabel label = new JLabel();
		label.setText("chimpokomon");
		label.setTransferHandler(new MyTransferHandler());
//		label.setTransferHandler(new TransferHandler("text"));
		label.addMouseMotionListener(new MouseMotionListener() {
						
			public void mouseMoved(MouseEvent e) {				
			}
			
			public void mouseDragged(MouseEvent e) {
				JLabel label = (JLabel) e.getSource();
				label.getTransferHandler().exportAsDrag(label, e, TransferHandler.COPY);
				e.consume();				
			}
		});
		
		JFormattedTextField format1 = new JFormattedTextField("coucou les gens");
		format1.setDragEnabled(true);
		format1.setFont(new Font("Courier",Font.BOLD,16));
		//format1.setTransferHandler(new TransferHandler("font"));
		
		JFormattedTextField format2 = new JFormattedTextField("chimpokomon !!!!!");
		format2.setDragEnabled(true);
		format2.setFont(new Font("Courier",Font.ITALIC,12));
		//format2.setTransferHandler(new TransferHandler("font"));
		
		win.setLayout(new GridLayout(3, 1));
		win.add(label);
		win.add(format1);
		win.add(format2);
		
		win.setSize(300, 300);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
	}
	
	public static void main(String[] args) {
		Part2 p = new Part2();
		
	}

}
