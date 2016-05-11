package Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import Exception.FTPRequestAccessException;
import Exception.RequestException;
import myFTP.FtpRequest;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class RequestList implements Request {

	private RequestList() {
	}
 
	/** Holder */
	private static class SingletonHolder
	{		
		/** Instance unique non préinitialisée */
		private final static RequestList instance = new RequestList();
	}
 
	/** Point d'accès pour l'instance unique du singleton */
	public static RequestList getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
		if(!request[0].equals("LIST")){
			this.succesor(request, ftp);
			return;
		}

		if(!ftp.getClient().canRead()){
    		ftp.getWriter().println(425 + " Access restriction");
    		throw new FTPRequestAccessException();
		}
		
		
		File base = new File(ftp.getPathname());
		File fileList[] = base.listFiles();
		String currentFile = "";
		ftp.getWriter().println(150 + " Files OK, open Data Connection in ASCII"); 

		try {
			//on ouvre le socket pour les donnes
			System.out.println("a");
			if(ftp.getPassive())
				ftp.setDataSocket(ftp.getPassiveServer().accept());
			else
				ftp.setDataSocket(new Socket(InetAddress.getByName(ftp.getAdress()), ftp.getPort()));
			System.out.println("a");
			ftp.setDataReader(new DataInputStream(ftp.getDataSocket().getInputStream()));
			ftp.setDataWriter(new DataOutputStream(ftp.getDataSocket().getOutputStream()));
			System.out.println("a");
    		//envoie des donnees
    		//dataWriter.writeUTF(result);
			if(fileList.length > 0){
				System.out.println("a");
				//on formate les donnŽes pour les envoyer au format EPLF
				for (int i = 0; i < fileList.length; i++){
					System.out.println("a");
					if(!fileList[i].isHidden()){
						//on recupere les fichiers non caches
						if (fileList[i].isFile())
	                    {
	                        currentFile = "+s" + fileList[i].length()+",m"+fileList[i].lastModified()/1000+",\011"+fileList[i].getName()+"\015\012";
	                    }
	                    else if(fileList[i].isDirectory())
	                    {
	                        currentFile = "+/,m"+fileList[i].lastModified()/1000+",\011"+fileList[i].getName()+"\015\012";
	                    }
						currentFile = currentFile + "\r\n";
						//System.out.println(files);
						ftp.getDataWriter().writeBytes(currentFile);
						ftp.getDataWriter().flush();
					}				
				}			
			}
			//on libere le socket des donnees pour le transfert suivant
			ftp.getDataSocket().close();
			ftp.getWriter().println(226 + " Closing data connection. Requested file action successful");	
		} 
    	catch (IOException e) {
    		ftp.getWriter().println(425 + " Can't open data connection.");
    		throw new RuntimeException(e);
    	}
	}

	public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {		
		RequestPwd.getInstance().processRequest(request, ftp);			
	}
}
