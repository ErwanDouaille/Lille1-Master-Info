package Request;

import java.io.IOException;

import myFTP.FtpRequest;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestPwd implements Request {

	private RequestPwd() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestPwd instance = new RequestPwd();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestPwd getInstance()
	{
		return SingletonHolder.instance;
	}
	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("PWD")){
			this.succesor(request, ftp);
			return;			
		}
		ftp.getWriter().println(257 + " " + ftp.getPathname().toString());
	}


	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestCwd.getInstance().processRequest(request, ftp);	
	}
}
