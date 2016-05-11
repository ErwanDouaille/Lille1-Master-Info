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
 * is an utils class only containing a main method. It has to create link between two node. Graph link means, father knows its son and sons know their fathers 
 * @author Douaille Erwan & Demol David 
 * 
 */
public class CreateGraphLink {

	/**
	 * 
	 * @param args Usage: id message
	 */
	public static void main(String[] args) {
		String usage = "Usage: idFather idSon";
		String idFather = null;
		String idSon = null;
		if(args.length != 2) {
			System.err.println(usage);
			return;
		}
		idFather = args[0];
		idSon = args[1];
		
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry(1099);
			SiteItf father = (SiteItf) reg.lookup(idFather);
			SiteItf son = (SiteItf) reg.lookup(idSon);
			father.addSon(son);
			son.addFather(father);
			System.out.println("Graph link between " + idFather + " and " + idSon + " has been created");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
