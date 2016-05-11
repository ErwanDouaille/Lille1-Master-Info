/**
 * Contains java class needed to create RMI node
 */
package rmiNodeCore;


import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * is an interface defining default methods for RMI node
 * @author Douaille Erwan & Demol David
 *
 */
public interface SiteItf extends Remote
{
	/**
	 * return list of sons
	 * @return list of Sons
	 * @throws RemoteException
	 */
	public ArrayList<SiteItf> getListOfSon() throws RemoteException;
	
	/**
	 * return list of fathers
	 * @return list of fathers
	 * @throws RemoteException
	 */
	public ArrayList<SiteItf> getListOfFather() throws RemoteException;
	
	/**
	 * add a son
	 * @param son SiteItf object which will be added
	 */
	public void addSon(SiteItf son)throws RemoteException;

	/**
	 * add a father
	 * @param father SiteItf object which will be added
	 */
	public void addFather(SiteItf father)throws RemoteException;

	/**
	 * remove a son
	 * @param son SiteItf object which will be removed
	 */
	public void removeSon(SiteItf sonId) throws AccessException, RemoteException, NotBoundException;
	
	/**
	 * remove a father
	 * @param father SiteItf object which will be removed
	 */
	public void removeFather(SiteItf fatherId) throws AccessException, RemoteException, NotBoundException;

	/**
	 * is responsible to send message to sons
	 * @param message message[0] contains the original id message sender, message[1] contains a message
	 * @throws RemoteException 
	 */
	public void sendMessage(String[] message) throws RemoteException;

	/**
	 * quit, unreference the current instance from its father and sons
	 */
	public void quit() throws RemoteException;

	/**
	 * return if the current object is blocking an infinite loop Message
	 */
	public boolean isBlockingLoopMessage() throws RemoteException;
	
	/**
	 * 
	 * @param bool setter for blockingLoopMessage
	 * @throws RemoteException
	 */
	public void setBlockingLoopMessage(boolean bool) throws RemoteException;

	/** 
	 * return the id of current instance
	 */
	public String getId() throws RemoteException;


}
