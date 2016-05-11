package Main;

import myFTP.Serveur;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */


/**
 * Main class éxécute le tp FTP 
 */
public class Main {
	
	/** Creation, initialisation and démarrage du server **/
	public static void main(String[] args) {
		Serveur serveur = new Serveur();
		serveur.initialization(2121);
		serveur.start();
	}

}
