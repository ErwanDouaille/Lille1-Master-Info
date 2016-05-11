/**
 * Contains the java class initializing rmi registry 
 */
package rmiServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
/**
 * is responsible to initialize a rmi registry
 * @author Douaille Erwan & Demol David
 *
 */
public class Serveur {

	public Serveur() throws Exception {
		try {
			@SuppressWarnings("unused")
			Registry reg=LocateRegistry.createRegistry(1099);
			System.out.println ("Server is ready.");
		} catch (Exception e) {
			System.out.println ("Server failed: " + e);
		}
	}
}
