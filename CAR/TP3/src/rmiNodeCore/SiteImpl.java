/**
 * Contains java class needed to create RMI node
 */
package rmiNodeCore;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import rmiServer.Serveur;

/**
 * represents a RMI node. You can send message/receive message, manage fathers and sons node
 * @author Douaille Erwan & Demol David
 *
 */
@SuppressWarnings("serial")
public class SiteImpl extends UnicastRemoteObject implements SiteItf {

	private Registry reg;
	private String id;
	private ArrayList<SiteItf> listOfSon = new ArrayList<SiteItf>();
	private ArrayList<SiteItf> listOfFather = new ArrayList<SiteItf>();
	public boolean blockingLoopMessage;


	/**
	 * Default constructor
	 * @param id define the id of the created object
	 * @throws RemoteException
	 */
	public SiteImpl(String id) throws RemoteException {
		super();
		if(reg == null)
			reg = LocateRegistry.getRegistry(1099);
		this.id = id;
		reg.rebind(this.id, this);
		System.out.println("Node " + id + " has been registered");
	}	   


	public ArrayList<SiteItf> getListOfSon() throws RemoteException {
		return listOfSon;
	}
	
	public ArrayList<SiteItf> getListOfFather() throws RemoteException {
		return listOfFather;
	}
	
	public void addFather(SiteItf father) throws RemoteException {
		this.listOfFather.add(father);
	}

	public void addSon(SiteItf son) throws RemoteException {		
		this.listOfSon.add(son);	
	}

	public void removeSon(SiteItf son) throws AccessException, RemoteException, NotBoundException {
		if(!this.listOfSon.remove(son)) {
			System.err.println(son + " non present dans la liste");
			return;
		}
		son.removeFather(this);
		System.out.println("suppresion du fils: " + son.getId());
	}

	public void removeFather(SiteItf father) throws AccessException, RemoteException, NotBoundException {
		if(!this.listOfFather.remove(father)) {
			System.err.println(father + " non present dans la liste");
			return;
		}
		father.removeSon(this);
		System.out.println("suppresion du pere: " + father.getId());
	}


	public void sendMessage(String[] messageData) throws RemoteException {
		if(this.blockingLoopMessage)
			return;
		this.blockingLoopMessage = true;
		System.out.println(this.id + " receive: " + messageData[1]);
		
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (SiteItf father : this.listOfFather){
			if(!father.isBlockingLoopMessage()) {
				Thread thread = new Thread(new Propagation(father, messageData));
				thread.start();
				threads.add(thread);
			}
		}
		for (SiteItf son : this.listOfSon){
			if(!son.isBlockingLoopMessage()) {
				Thread thread = new Thread(new Propagation(son, messageData));
				thread.start();
				threads.add(thread);
			}
		}

		for (int i = 0; i < threads.size(); i++)
			try {
				((Thread) threads.get(i)).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		if(this.getId().equals(messageData[0])) {
			this.blockingLoopMessage = false;
			for (SiteItf son : this.listOfSon){
				son.setBlockingLoopMessage(false);
			}
		}
	}

	public void quit() throws RemoteException {
		try {
			for (SiteItf son : this.listOfSon)
				son.removeFather(this);
			for (SiteItf father : this.listOfFather)
				father.removeSon(this);
			this.reg.unbind(this.id);
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}



	public String getId() {
		return this.id;
	}

	public boolean isBlockingLoopMessage() throws RemoteException {
		return this.blockingLoopMessage;
	}

	public void setBlockingLoopMessage(boolean b) throws RemoteException {
		this.blockingLoopMessage = b;
	}



	/**
	 * 
	 * @param args Usage: [OPTION] [id]\n\nOptions:\n\t-s\t launch server
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			String id=null;
			String usage = "Usage: [OPTION] [id]\n\nOptions:\n\t-s\t launch server";

			if(args.length < 1 || args.length>2) {
				System.err.println(usage);
				return;
			}
			for (String cmd : args) {
				if (cmd.equals("-s"))
					new Serveur();
				else
					id = cmd;
			}
			if(id==null){
				System.err.println(usage);
				return;
			}
			new SiteImpl(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}



	/**
	 * Propagation is an anonymous class responsible to transfer message to an object, using thread.
	 * @author Douaille Erwan & Demol David
	 *
	 */
	private class Propagation implements Runnable{
		private SiteItf source;
		private String[] message;
		/**
		 * Default constructor
		 * @param source object receiver of message
		 * @param message message that have to be send
		 */
		public Propagation(SiteItf source, String[] message) {
			this.source = source;
			this.message = message;
		}		
		/**
		 * run method responsible to send message
		 */
		public void run() {
			if (source==null)
				return;
			try {
				source.sendMessage(message);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

}
