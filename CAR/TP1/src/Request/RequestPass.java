package Request;

import java.io.IOException;

import myFTP.FtpRequest;
import Exception.FTPRequestPasswordException;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestPass implements Request {

	private RequestPass() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestPass instance = new RequestPass();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestPass getInstance()
	{
		return SingletonHolder.instance;
	}	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("PASS")){
			this.succesor(request, ftp);
			return;
		}
		try {
			if(ftp.getClient().isPassword(request[1])){
				ftp.getWriter().println(230 + " Password ok");
				System.out.println(ftp.getClient() + " is now connected");
			}else{
				throw new FTPRequestPasswordException(ftp);
			}			
		} catch (Exception e) {
			throw new FTPRequestPasswordException(ftp);
		}
	}

	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestList.getInstance().processRequest(request, ftp);	
	}
}
