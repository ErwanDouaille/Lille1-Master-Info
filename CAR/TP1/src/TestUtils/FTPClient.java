package TestUtils;

import java.io.*;
import java.net.*;

public class FTPClient {

	public InetAddress adresse;
	public Socket socket;
	public PrintWriter writer;
	public BufferedReader reader;

	public FTPClient(int port){
		try {
			this.adresse = InetAddress.getByName("localhost"); 
			this.socket = new Socket(this.adresse,port); 
			this.writer = new PrintWriter(socket.getOutputStream(),true);
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public void send(String request) {
		this.writer.println(request);
	}

	public String receive(){
		String line=null;
		try {
			line = this.reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public void close(){
		try {
			this.writer.close();
			this.reader.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
