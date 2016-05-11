package Exception;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe spécifique à une exception concernant un problème d'envois de fichier. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public final class FTPRequestAccessException extends RequestException {
	
	private static final long serialVersionUID = 1L;
	private static volatile FTPRequestAccessException instance = null;

	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique 
	 */
	public final static FTPRequestAccessException getInstance() {
		if (FTPRequestAccessException.instance == null)
			synchronized(FTPRequestAccessException.class) {
				if (FTPRequestAccessException.instance == null) 
					FTPRequestAccessException.instance = new FTPRequestAccessException();
			}
		return FTPRequestAccessException.instance;
	}

	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique
	 */
	public FTPRequestAccessException() {
		super("Access restriction ...");
	}
}
