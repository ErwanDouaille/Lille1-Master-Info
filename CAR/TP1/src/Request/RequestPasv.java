package Request;


import java.io.IOException;
import java.net.ServerSocket;

import myFTP.FtpRequest;

import Exception.RequestException;

public class RequestPasv implements Request {
	

	private RequestPasv() {
	}
	
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestPasv instance = new RequestPasv();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestPasv getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void processRequest(String[] request, FtpRequest ftp)
			throws RequestException, IOException {
		
		if(!request[0].equals("PASV")){
			this.succesor(request, ftp);
			return;
		}

		/*if(!ftp.getClient().canWrite()){
			ftp.getWriter().println(550 + " access denied");
    		throw new FTPRequestAccessException();
		}*/
		ftp.setPassiveServer(new ServerSocket(0));
		ftp.setPort(ftp.getPassiveServer().getLocalPort());
		
		String ipServer[] = ftp.getSocket().getLocalAddress().getHostAddress().split("\\.");
		int port1 = ftp.getPort() / 256;
		int port2 = ftp.getPort() % 256;
		
		String args = ipServer[0] + "," + ipServer[1] + "," + ipServer[2] + "," + ipServer[3] + "," + port1 + "," + port2;
		ftp.getWriter().println(227 + " Entering passive mode " + args);
		ftp.setPassive(true);
	}

	public void succesor(String[] request, FtpRequest ftp)
			throws RequestException, IOException {
		RequestSyst.getInstance().processRequest(request, ftp);

	}

}
