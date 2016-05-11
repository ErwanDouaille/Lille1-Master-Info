import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;


public class MyTransferHandler extends TransferHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int getSourceActions(JComponent c){
		return COPY_OR_MOVE;
	}
	
	public Transferable createTransferable(JComponent c){
		return new StringSelection(((JLabel)c).getText());
	}
	
	public void exportDone(JComponent c, Transferable t, int action){
		if (action == MOVE) {
			((JLabel)c).setText("");			
		}
	}
	
	public boolean canImport(TransferHandler.TransferSupport support){
		if(!support.isDataFlavorSupported(DataFlavor.stringFlavor)){
			return false;
		}
		return true;
	}
	
	public boolean importData(TransferHandler.TransferSupport support){
		if(!support.isDrop())
			return false;
		if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			System.out.println(("Pas un string"));
			return false;
		}
		JLabel label = (JLabel) support.getComponent();
		Transferable t = support.getTransferable();
		String data;
		try {
			data = (String) t.getTransferData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			return false;
		}
		label.setText(label.getText() + data);
		return true;
				
	}

}
