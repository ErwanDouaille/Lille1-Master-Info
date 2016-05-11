package Exception;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe spécifique à une exception concernant un problème de déconnexion. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public final class FTPDisconnectionException extends RequestException {
	
	private static final long serialVersionUID = 1L;
	private static volatile FTPDisconnectionException instance = null;

	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique
	 */
	public final static FTPDisconnectionException getInstance() {
		if (FTPDisconnectionException.instance == null)
			synchronized(FTPDisconnectionException.class) {
				if (FTPDisconnectionException.instance == null) 
					FTPDisconnectionException.instance = new FTPDisconnectionException();
			}
		return FTPDisconnectionException.instance;
	}
	
	public FTPDisconnectionException() {
		super("An error happened while trying to disconnect");
	}
}
