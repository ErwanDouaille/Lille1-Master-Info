package myFTP;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Exception.*;
import Request.*;
import User.User;

/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe implémentant Runnable. FtpRequest permet de traiter les requêtes des
 * clients Chaque client se voit affecter une instance de FtpRequest
 */
public class FtpRequest implements Runnable {

	private Socket socket;
	private Socket dataSocket;
	private BufferedReader reader;
	private PrintWriter writer;
	private DataInputStream dataReader;
	private DataOutputStream dataWriter;
	private ArrayList<User> userList = new ArrayList<User>();
	private User client;
	private String adress;
	private int port;
	private String pathname;
	private String racine;
	private boolean passive;
	private ServerSocket passiveServer;

	/**
	 * Constructeur prenant un objet de type Socket Initialisation du path
	 * working directory, la liste des utilisateurs et des outils de
	 * communications
	 */
	public FtpRequest(Socket socket) {
		this.socket = socket;
		this.pathname = System.getProperty("user.dir");
		this.setRacine(this.pathname);
		this.initUsers();
		try {
			this.reader = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			this.writer = new PrintWriter(new OutputStreamWriter(
					this.socket.getOutputStream()), true);
			this.writer.println(220 + " Service ready, plz login");
		} catch (IOException e) {
			new FTPConnectionException();
		}
	}

	/** Méthode pour initialiser une liste prédéfinie d'users **/
	public void initUsers() {
		User jerem = new User("jeremy", "test");
		User erwan = new User("wawan", "dadou");
		User anonymous = new User("anonymous", "anon@localhost");
		User anonymous2 = new User("anonymous", "");
		jerem.getAccess().setWrite(true);
		erwan.getAccess().setWrite(true);
		this.userList.add(jerem);
		this.userList.add(erwan);
		this.userList.add(anonymous);
		this.userList.add(anonymous2);
	}

	/**
	 * Méthode run éxécuté à chaque connexion dun client. Lancement du
	 * traitement des requêtes du client
	 */
	public void run() {
		try {
			this.processRequest();
		} catch (IOException e) {
		}
		try {
			System.out.println(this.getClient() + " closing connection");
			this.socket.close();
		} catch (IOException e) {
			new FTPDisconnectionException();
		}
	}

	/**
	 * Lit en continue les requêtes envoyées par le client et les transmets aux
	 * objets de type Request pour trouver et éxécuter la tâche voulue. Appelée
	 * dans run()
	 * 
	 * @throws IOException
	 */
	public void processRequest() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			String request[] = line.split("\\s");
			try {
				RequestUser.getInstance().processRequest(request, this);
			} catch (RequestException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Socket getDataSocket() {
		return this.dataSocket;
	}

	public void setDataSocket(Socket dataSocket) {
		this.dataSocket = dataSocket;
	}

	public BufferedReader getReader() {
		return this.reader;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public PrintWriter getWriter() {
		return this.writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public DataInputStream getDataReader() {
		return this.dataReader;
	}

	public void setDataReader(DataInputStream dataReader) {
		this.dataReader = dataReader;
	}

	public DataOutputStream getDataWriter() {
		return this.dataWriter;
	}

	public void setDataWriter(DataOutputStream dataWriter) {
		this.dataWriter = dataWriter;
	}

	public User getClient() {
		return this.client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	public String getAdress() {
		return this.adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPathname() {
		return this.pathname;
	}

	public void setPathname(String pathname) {
		this.pathname = pathname;
	}

	public String getRacine() {
		return this.racine;
	}

	public void setRacine(String racine) {
		this.racine = racine;
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	public boolean containsUserNamed(String userName) {
		for (User u : this.userList) {
			if (u.isLogin(userName))
				return true;
		}
		return false;
	}

	public User getUserNamed(String userName) {
		for (User u : this.userList) {
			if (u.isLogin(userName))
				return u;
		}
		return null;
	}

	public void setPassive(boolean b) {
		this.passive = b;
		
	}
	
	public boolean getPassive() {
		return this.passive;
		
	}

	public void setPassiveServer(ServerSocket serverSocket) {
		this.passiveServer = serverSocket;
		
	}
	
	public ServerSocket getPassiveServer() {
		return this.passiveServer;
		
	}

}
