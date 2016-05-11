package Exception;

import java.io.IOException;

import Request.RequestQuit;
import myFTP.FtpRequest;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */


/**
 * Classe spécifique à une exception concernant un problème de login, mot de passe incorrect. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public class FTPRequestPasswordException extends RequestException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique
	 * et déconnecte le client 
	 * @throws IOException 
	 */
	public FTPRequestPasswordException(FtpRequest ftp) throws IOException {
		super("Login failed, connection will be closed");
		ftp.getWriter().println(430 + " Invalid password");
		String[] request = {"QUIT"};
		try {
			RequestQuit.getInstance().processRequest(request, ftp);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
}
