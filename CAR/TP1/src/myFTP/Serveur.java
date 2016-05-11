package myFTP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */
public class Serveur extends Thread{

	private ServerSocket serveurSocket;
	private Socket socket = new Socket();
	
	/** Initialisation du Serveur avec le port **/
	public void initialization(int port){
		try {
			this.serveurSocket = new ServerSocket(port);
			System.out.println("Initialization OK on: "+ port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	/** 
	 * Boucle attendant la connexion de nouveaux clients
	 * A chaque connexion, un nouveau processus éxécutant un FtpRequest
	 * est créé.
	 */
	public void run(){
		while(true){
			System.out.println("Waiting client ...");
			try {
				this.socket = this.serveurSocket.accept();
				new Thread(new FtpRequest(this.socket)).start();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public ServerSocket getServeurSocket() {
		return serveurSocket;
	}

	public void setServeurSocket(ServerSocket serveurSocket) {
		this.serveurSocket = serveurSocket;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}	
}
