package Request;

import java.io.IOException;

import myFTP.FtpRequest;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Interface définissant le type Request.
 * Chaque classe implémentant Request aura pour rôle d'éxécuter un 
 * type de requête particullière. Exemple, USER, PASS ...
 */
public interface Request {

	/**
	 * Méthode dans laquelle on procèdera au traitement de la requête
	 * Si la requête ne correspond pas à la requête courante on fait appel 
	 * à la méthode succesor qui va invoquer la requête suivante.
	 * @throws IOException 
	 */
	public abstract void processRequest(String request[], FtpRequest ftp) throws RequestException, IOException;

	/**
	 * Méthode créant le type de requête suivant
	 * Exemple: new RequestQuit(request, ftp);
	 * Chain of responsibility
	 * @throws IOException 
	 */
	public abstract void succesor(String request[], FtpRequest ftp) throws RequestException, IOException;

}
