package Request;

import java.io.IOException;
import myFTP.FtpRequest;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestQuit implements Request {

	private RequestQuit() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestQuit instance = new RequestQuit();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestQuit getInstance()
	{
		return SingletonHolder.instance;
	}
	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("QUIT")){
			this.succesor(request, ftp);
			return;
		}
		ftp.getWriter().println(231 + " logout");
		try {
			ftp.getSocket().close();
			System.out.println(ftp.getClient() + " disconnected");
		} catch (IOException e) {
		}
	}


	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestRetr.getInstance().processRequest(request, ftp);	
	}
}
