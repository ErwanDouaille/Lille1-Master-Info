package Exception;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe abstraite définisant le type RequestException. 
 * Hérite de Exception, et transmet le message d'erreur
 *
 */
public abstract class RequestException extends Exception{
	
	/** Constructeur prenant un paramètre le message d'erreur **/
	public RequestException(String errorMessage){
		super(errorMessage);
	}

	private static final long serialVersionUID = 1L;

}
