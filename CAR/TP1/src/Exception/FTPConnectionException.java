package Exception;


/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe spécifique à une exception concernant un problème de connexion. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public final class FTPConnectionException extends RequestException {
	
	private static final long serialVersionUID = 1L;
	private static volatile FTPConnectionException instance = null;

	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique
	 */
	public final static FTPConnectionException getInstance() {
		if (FTPConnectionException.instance == null)
			synchronized(FTPConnectionException.class) {
				if (FTPConnectionException.instance == null) 
					FTPConnectionException.instance = new FTPConnectionException();
			}
		return FTPConnectionException.instance;
	}
	
	public FTPConnectionException() {
		super("An error happened while trying to connect");
	}
}
