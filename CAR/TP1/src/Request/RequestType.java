package Request;

import java.io.IOException;

import myFTP.FtpRequest;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestType implements Request {

	private RequestType() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestType instance = new RequestType();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestType getInstance()
	{
		return SingletonHolder.instance;
	}
	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("TYPE")){
			this.succesor(request, ftp);
			return;
		}
		ftp.getWriter().println(200 + " TYPE ok");
	}


	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestPort.getInstance().processRequest(request, ftp);	
	}
}
