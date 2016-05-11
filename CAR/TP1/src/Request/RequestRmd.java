package Request;

import java.io.File;
import java.io.IOException;

import myFTP.FtpRequest;
import Exception.FTPRequestAccessException;
import Exception.RequestException;


public class RequestRmd implements Request {

	private RequestRmd() {
	}
	
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestRmd instance = new RequestRmd();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestRmd getInstance()
	{
		return SingletonHolder.instance;
	}

	public void processRequest(String[] request, FtpRequest ftp)
			throws RequestException, IOException {
		
		if(!request[0].equals("RMD")){
			this.succesor(request, ftp);
			return;
		}

		if(!ftp.getClient().canWrite()){
			ftp.getWriter().println(550 + " access denied");
    		throw new FTPRequestAccessException();
		}
		
		String path;
		if(request[1].startsWith(ftp.getPathname()))
			path = ftp.getPathname();
		else
			path = ftp.getPathname() + "/" + request[1];
		File dir = new File(path);
		if(dir.exists() && dir.isDirectory()){
			if(dir.delete())
				ftp.getWriter().println(250 + " Directory delete sucessfully");
			else
				ftp.getWriter().println(550 + " RMD Failed");
		}else
			ftp.getWriter().println(550 + " RMD Failed");

	}

	public void succesor(String[] request, FtpRequest ftp)
			throws RequestException, IOException {
		RequestPasv.getInstance().processRequest(request, ftp);

	}

}
