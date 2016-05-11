package Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import myFTP.FtpRequest;
import Exception.FTPRequestAccessException;
import Exception.FTPRequestRetrException;
import Exception.RequestException;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestRetr implements Request {

	private RequestRetr() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestRetr instance = new RequestRetr();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestRetr getInstance()
	{
		return SingletonHolder.instance;
	}
	
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("RETR")){
			this.succesor(request, ftp);
			return;
		}
		if(!ftp.getClient().canRead()){
			ftp.getWriter().println(550 + " access denied");
    		throw new FTPRequestAccessException();
		}
		
		ftp.getWriter().println(150 + " Files OK, open Data Connection in ASCII"); 
		try {
			//on ouvre le socket pour les donnŽes
			if(ftp.getPassive())
				ftp.setDataSocket(ftp.getPassiveServer().accept());
			else
				ftp.setDataSocket(new Socket(InetAddress.getByName(ftp.getAdress()), ftp.getPort()));
			ftp.setDataReader(new DataInputStream(ftp.getDataSocket().getInputStream()));
			ftp.setDataWriter(new DataOutputStream(ftp.getDataSocket().getOutputStream()));
			FileInputStream fis;
			int i;

			//on envoie le fichier
			fis = new FileInputStream( ftp.getPathname() + "/" + request[1] );
			while( (i = fis.read()) != -1){
				ftp.getDataWriter().writeByte(i);
			}

			//on ferme le fichier
			fis.close();

			//on ferme le socket de donnŽes
			ftp.getDataSocket().close();
			ftp.getWriter().println(226 + " Closing data connection. Requested file action successful");
		} catch (IOException e) {
			ftp.getWriter().println(425 + " Can't open data connection.");
    		throw new FTPRequestRetrException();
		}
	}


	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
		RequestStor.getInstance().processRequest(request, ftp);	
	}
}
