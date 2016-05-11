TP3: RMI

17/04/2014

DOUAILLE Erwan
DEMOL David

============
Introduction
============

	Nous avons créer une application répartie qui permet de transférer en RMI des données à un ensemble d'objets organisés.
L'application peut envoyer des données selon deux topologie : Une topologie en arbre et une topologie en graphe.

========
Executer
========

 	Quatre .jar sont créés (CreateGraphLink, CreateTreeLink, Node et SendMessage) pour permettre à quatre script .sh de tester l'application.

	./treeCreation.sh
	./rootTransfer.sh
	./concurentTransfer.sh
	./graphTransfer.sh

	java -jar ./Node.jar -s 1
	java -jar ./Node.jar 2	
	java -jar ./CreateTreeLink.jar 1 2
	java -jar ./SendMessage.jar 1 coucou

============
Architecture
============
Package:
--------

rmiNodeCore

Classes:
--------

-----------------------
SiteImpl
SiteImpl permet de mettre en place la création des noeuds représenté par des objets RMI et également gérer les liens entre les différents noeuds sous la forme de père-fils

Package:
--------

rmiServer

Classes:
--------

-----------------------
Serveur
La classe serveur permet de mettre en place un serveur qui permet à un client d'invoquer des méthodes à distance sur un objet qu'il instancie.

Package:
--------

rmiNodeUtils

Classes:
--------

-----------------------
CreateTreeLink
CreateTreeLink permet de créer un lien entre deux noeuds. Un lien de parenté est mis en place, seul le père peut discuter avec ses enfants.

-----------------------
CreateGraphLink
CreateGraphLink permet de créer un lien entre deux noeuds. Aucun lien de parenté est mis en place, tous les noeuds peuvent discuter entre si ils sont liés.

-----------------------
SendMessage
SendMessage permet l'envoie d'un message depuis l'initiateur.

=======================
Qualité du code: design
=======================

Nous n'avons pas utilisé de méthodes abstraite.
Nous avons utilisé une interface pour la mise en place de SiteImpl.
Nous avons utilisé de l'heritage pour ajouter la classe UnicastRemoteObject à la classe SiteImpl, classe qui contient les différents traitements élémentaires pour un objet distant.

============
Test Junit
============

Les tests sont dans le package rmiNodeCoreTest. Il y a 6 tests qui vérifie le comportement de l'objet SiteImpl. Les tests nous ont permis d'identifier 2 bugs concernant la suppresion d'un fils/pere que nous avons corrigé.
Nous avons toujours un probleme sur 2 tests qui consiste a tester l'envoi de message. Ce qui signifie recuperer la sortie (print) et de la comparer. Le test indique une erreur malgres que les chaines soient similaires.

===============
Gestion d'erreur 
===============

Concernant la gestion d'erreur, la plupart des methodes throw RemoteException pour palier les problemes de communication avec le registre RMI. Nous avons generalement utilisé les try/catch

Exemple avec la creation d'un lien pour le graph:
	Registry reg;
		try {
			reg = LocateRegistry.getRegistry(1099);
			SiteItf father = (SiteItf) reg.lookup(idFather);
			SiteItf son = (SiteItf) reg.lookup(idSon);
			father.addSon(son);
			son.addFather(father);
			System.out.println("Graph link between " + idFather + " and " + idSon + " has been created");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

Les catchs permettent de gerer les Exceptions pouvant etre generés par le getRegistry() et les lookup() qui tentent de recuperer un objet.

============
Code Samples
============

Permet l'envoie de message entre deux noeuds
---------------------------------------------------------------------------------------------------------------------------

public static void main(String[] args) {
	String usage = "Usage: id message";
	String id = null;
	String message = "";
	if(args.length < 2) {
		System.err.println(usage);
		return;
	}
	id = args[0];
	if(id==null){
		System.err.println(usage);
		return;
	}
	for (int i = 1; i < args.length; i++) {
		message += args[i] + " ";
	}
	
	Registry reg;
	try {
		reg = LocateRegistry.getRegistry(1099);
		SiteItf sender = (SiteItf) reg.lookup(id);
		sender.setBlock();
		sender.sendMessage(message);
	} catch (RemoteException e) {
		e.printStackTrace();
	} catch (NotBoundException e) {
		e.printStackTrace();
	}
}

Permet la gestion des envoies de messages, bloque l'envoie sur un noeud ayant déjà eu l'information.
---------------------------------------------------------------------------------------------------------------------------

public void sendMessage(String message) throws RemoteException {
	ExecutorService service = Executors.newCachedThreadPool();
	System.out.println(this.id + " receive: " + message);
	
	for (SiteItf father : this.listOfFather)
			if(!father.isBlockingLoopMessage())
			{
				father.setBlock();
				service.execute(new Propagation(father, message));
			}
	
	for (SiteItf son : this.listOfSon)
		if(!son.isBlockingLoopMessage())
		{
			son.setBlock();
			service.execute(new Propagation(son, message));
		}
	
	service.shutdown();
	try {
		service.awaitTermination(10, TimeUnit.SECONDS);
	} catch (InterruptedException e) {
		e.printStackTrace();
	} finally {
		this.blockingLoopMessage = false;
	}
}

Permet l'envoie des messages de manière parallèle avec l'utilisation de thread
---------------------------------------------------------------------------------------------------------------------------

/**
 * Propagation is a private class responsible to transfer message to an object, using thread.
 * @author Douaille Erwan & Demol David
 *
 */
private class Propagation implements Runnable{
	private SiteItf source;
	private String message;
	/**
	 * Default constructor
	 * @param source object receiver of message
	 * @param message message that have to be send
	 */
	public Propagation(SiteItf source, String message) {
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

Un des tests JUnit, testant la transmission de message
---------------------------------------------------------------------------------------------------------------------------
	@Test
	public void testTreeReceive() throws Exception {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
		SiteItf instance = (SiteItf) new SiteImpl("1");
		SiteItf instance2 = (SiteItf) new SiteImpl("2");

		instance.addSon(instance2);
		
		String messageData[] = new String[2];
		messageData[0] = "1";
		messageData[1] = "toto";
		instance.sendMessage(messageData);

		assertEquals(outContent.toString(), "1 receive: toto");
		assertEquals(outContent.toString(), "2 receive: toto");
	}

Partie de code permettant l'envoi de message à partir d'un initiateur
---------------------------------------------------------------------------------------------------------------------------
		String usage = "Usage: id message";
		String id = null;
		String message = "";
		if(args.length < 2) {
			System.err.println(usage);
			return;
		}
		id = args[0];
		if(id==null){
			System.err.println(usage);
			return;
		}
		for (int i = 1; i < args.length; i++) {
			message += args[i] + " ";
		}
		
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry(1099);
			SiteItf sender = (SiteItf) reg.lookup(id);
			String messageData[] = new String[2];
			messageData[0] = id;
			messageData[1] = message;
			sender.sendMessage(messageData);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
