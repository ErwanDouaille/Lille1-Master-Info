package Request;

import myFTP.FtpRequest;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestUnimplemented implements Request {

	private RequestUnimplemented() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestUnimplemented instance = new RequestUnimplemented();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestUnimplemented getInstance()
	{
		return SingletonHolder.instance;
	}	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException {
		ftp.getWriter().println(202 + " Command not implemented");	
	}

	public void succesor(String[] request, FtpRequest ftp) throws RequestException {
	}
}
