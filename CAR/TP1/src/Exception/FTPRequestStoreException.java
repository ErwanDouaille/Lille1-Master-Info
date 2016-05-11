package Exception;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe spécifique à une exception concernant un problème d'envois de fichier. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public final class FTPRequestStoreException extends RequestException {
	
	private static final long serialVersionUID = 1L;
	private static volatile FTPRequestStoreException instance = null;

	/**
	 * Constructeur prenant un objet de type FtpRequest
	 * Transmet un message d'exception spécifique
	 */
	public final static FTPRequestStoreException getInstance() {
		if (FTPRequestStoreException.instance == null)
			synchronized(FTPRequestStoreException.class) {
				if (FTPRequestStoreException.instance == null) 
					FTPRequestStoreException.instance = new FTPRequestStoreException();
			}
		return FTPRequestStoreException.instance;
	}
	
	public FTPRequestStoreException() {
		super("Error while storing file");
	}
}
