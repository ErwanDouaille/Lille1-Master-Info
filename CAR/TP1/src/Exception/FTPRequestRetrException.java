package Exception;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe spécifique à une exception concernant un problème de téléchargement d'un fichier. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public final class FTPRequestRetrException extends RequestException {
	
	private static final long serialVersionUID = 1L;
	private static volatile FTPRequestRetrException instance = null;

	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique 
	 */
	public final static FTPRequestRetrException getInstance() {
		if (FTPRequestRetrException.instance == null)
			synchronized(FTPRequestRetrException.class) {
				if (FTPRequestRetrException.instance == null) 
					FTPRequestRetrException.instance = new FTPRequestRetrException();
			}
		return FTPRequestRetrException.instance;
	}
	
	public FTPRequestRetrException() {
		super("Error while sending file");
	}
}
