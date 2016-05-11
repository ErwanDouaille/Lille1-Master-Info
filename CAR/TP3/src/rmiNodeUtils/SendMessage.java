/**
 * Contains java class needed to run and act on RMI nodes
 */
package rmiNodeUtils;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmiNodeCore.SiteItf;

/**
 * is an utils class only containing a main method. It has to run send message from id node parameter
 * @author Douaille Erwan & Demol David
 *
 */
public class SendMessage {

	/**
	 * 
	 * @param args Usage: id message
	 */
	public static void main(String[] args) {
		String usage = "Usage: id message";
		String id = null;
		String message = "";
		if(args.length < 2) {
			System.err.println(usage);
			return;
		}
		id = args[0];
		if(id==null){
			System.err.println(usage);
			return;
		}
		for (int i = 1; i < args.length; i++) {
			message += args[i] + " ";
		}
		
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry(1099);
			SiteItf sender = (SiteItf) reg.lookup(id);
			String messageData[] = new String[2];
			messageData[0] = id;
			messageData[1] = message;
			sender.sendMessage(messageData);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}