package Request;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import myFTP.FtpRequest;
import Exception.FTPRequestAccessException;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestCdup implements Request {

	private RequestCdup() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestCdup instance = new RequestCdup();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestCdup getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("CDUP")){
			this.succesor(request, ftp);
			return;
		}
		//on reformate la chaine pathname pour enlever le dernier dossier du chemin


		if(!ftp.getClient().canRead()){
			ftp.getWriter().println(550 + " access denied");
    		throw new FTPRequestAccessException();
		}

		String cdup = "";
		Path p = Paths.get(ftp.getPathname());
		for (int i = 0; i < p.getNameCount()-1; i++) {
			cdup = cdup  + "/"  + p.getName(i);
		}
		System.out.println(cdup);
		if(cdup.startsWith(ftp.getRacine())){
			ftp.setPathname(cdup);
			ftp.getWriter().println(250 + " CDUP ok");	
		}
		else{
			ftp.setPathname(ftp.getRacine());
			ftp.getWriter().println(550 + " access denied");
		}
		
	}

	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestQuit.getInstance().processRequest(request, ftp);	
	}
}
