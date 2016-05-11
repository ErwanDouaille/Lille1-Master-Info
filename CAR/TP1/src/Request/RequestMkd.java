package Request;

import java.io.File;
import java.io.IOException;

import myFTP.FtpRequest;
import Exception.FTPRequestAccessException;
import Exception.RequestException;

public class RequestMkd implements Request {
	
	private RequestMkd() {
	}
	
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestMkd instance = new RequestMkd();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestMkd getInstance()
	{
		return SingletonHolder.instance;
	}

	public void processRequest(String[] request, FtpRequest ftp)
			throws RequestException, IOException {
		
		if(!request[0].equals("MKD")){
			this.succesor(request, ftp);
			return;
		}

		if(!ftp.getClient().canWrite()){
			ftp.getWriter().println(550 + " access denied");
    		throw new FTPRequestAccessException();
		}
		
		File newDir = new File(ftp.getPathname() + "/" + request[1]);
		if(newDir.mkdir())
			ftp.getWriter().println(250 + " Directory created sucessfully");
		else
			ftp.getWriter().println(550 + " MKD Failed");

	}

	public void succesor(String[] request, FtpRequest ftp)
			throws RequestException, IOException {
		RequestRmd.getInstance().processRequest(request, ftp);

	}

}
