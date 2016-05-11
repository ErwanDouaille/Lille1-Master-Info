package Request;

import java.io.IOException;

import myFTP.FtpRequest;
import Exception.FTPRequestUserException;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestUser implements Request {

	private RequestUser() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestUser instance = new RequestUser();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestUser getInstance()
	{
		return SingletonHolder.instance;
	}
	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("USER")){
			this.succesor(request, ftp);
			return;			
		}
		if(ftp.containsUserNamed(request[1])){
			ftp.getWriter().println(331 + " Login ok");
			ftp.setClient(ftp.getUserNamed(request[1]));
		}else{
			throw new FTPRequestUserException(ftp);
		}
	}

	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestPass.getInstance().processRequest(request, ftp);
	}
}
