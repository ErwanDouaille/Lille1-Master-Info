package Request;

import java.io.File;
import java.io.IOException;

import myFTP.FtpRequest;
import Exception.FTPRequestAccessException;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestCwd implements Request {

	private RequestCwd() {
	}

	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestCwd instance = new RequestCwd();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestCwd getInstance()
	{
		return SingletonHolder.instance;
	}
	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("CWD")){
			this.succesor(request, ftp);
			return;
		}

		if(!ftp.getClient().canRead()){
			ftp.getWriter().println(550 + " access denied");
    		throw new FTPRequestAccessException();
		}
		
		
		if(request[1].startsWith("/"))
			ftp.setPathname(request[1]);	
		else{	
			if(ftp.getPathname().compareTo("/") == 0)
				ftp.setPathname(ftp.getPathname() + request[1]);
			else
				ftp.setPathname(ftp.getPathname() + "/" + request[1]);
		}
		if(ftp.getPathname().startsWith(ftp.getRacine())){
			if(new File(ftp.getPathname()).exists())
				ftp.getWriter().println(250 + " CWD ok");
			else{
				ftp.setPathname(ftp.getRacine());
				ftp.getWriter().println(550 + " directory not exist");
			}
		}
		else{
			ftp.setPathname(ftp.getRacine());
			ftp.getWriter().println(550 + " access denied");
		}
	}


	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestType.getInstance().processRequest(request, ftp);	
	}
}
