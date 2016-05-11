import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class TransferableCouleur implements Transferable{
	
	public static DataFlavor couleurFlavor;
	public Couleur c;

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { couleurFlavor, DataFlavor.stringFlavor};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return (flavor.equals(couleurFlavor) || flavor.equals(DataFlavor.stringFlavor));
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException{
		if(flavor == null)
			throw new IOException();
		if (flavor.equals(couleurFlavor)) 
			return c;
		if (flavor.equals(DataFlavor.stringFlavor)) 
			return c.toString();
		else
			throw new UnsupportedFlavorException(flavor);
	}

}
