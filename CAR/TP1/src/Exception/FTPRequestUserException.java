package Exception;
import java.io.IOException;

import myFTP.FtpRequest;
import Request.RequestQuit;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe spécifique à une exception concernant un problème de login, user incorrect. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public class FTPRequestUserException extends RequestException {

	private static final long serialVersionUID = 1L;


	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique
	 * et déconnecte le client 
	 * @throws IOException 
	 */
	public FTPRequestUserException(FtpRequest ftp) throws IOException {
		super("Unknow login ... disconnection");
		ftp.getWriter().println(430 + " Invalid login");
		String[] request = {"QUIT"};
		try {
			System.out.println("Je quitte");
			RequestQuit.getInstance().processRequest(request, ftp);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
}
