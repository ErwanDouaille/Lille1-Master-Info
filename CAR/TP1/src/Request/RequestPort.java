package Request;

import java.io.IOException;

import myFTP.FtpRequest;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestPort implements Request {

	private RequestPort() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestPort instance = new RequestPort();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestPort getInstance()
	{
		return SingletonHolder.instance;
	}
	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("PORT")){
			this.succesor(request, ftp);
			return;
		}

		String client[] = request[1].split(",");
		ftp.setAdress(client[0] + "." + client[1] + "." + client[2] + "." + client[3]);
		ftp.setPort((Integer.parseInt(client[4]) * 256) + Integer.parseInt(client[5]));
		System.out.println("adresse: " + ftp.getAdress() + " port: " + ftp.getPort());		
		ftp.getWriter().println(200 + " PORT ok");
		ftp.setPassive(false);
	}


	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestCdup.getInstance().processRequest(request, ftp);	
	}
}
