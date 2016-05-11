Implémentation d’un serveur FTP en Java
Douaille Erwan & JérémyOpsommer
20/02/2014

*** 0/ README


Éxécuter le projet:
        java -jar FTPServeur.jar


*** 1/ Introduction


Ce programme crée un serveur FTP utilisant le port 2121 pour communiquer avec des clients, le serveur gère les requête USER, PASS, QUIT, LIST RETR, STOR,MKD, RMD, PASV,  PWD, CWD, PORT et CDUP. Le serveur choisit le répertoire de travail de la machine comme racine, il est impossible d’accèder aux fichier situé avant le répertoire de travail pour des raison de sécurité.


*** 2/ Architecture


Packages:


        myFTP
                FtpRequest
                Serveur
        Request
                Request
                ResquestCdup
                RequestCwd
                RequestList
                RequestMkd
                RequestPass
                RequestPasv
                RequestPort
                RequestPwd
                RequestQuit
                RequestRetr
                RequestRmd
                RequestStor
                RequestType
                RequestUnimplemented
                RequestUser
        User
                Access
                User
        Exception
                FTPConnectionException
                FTPDisconnectionException
                FTPRequestAccessException
                FTPResquestPasswordException
                FTPRequestRetrException
                FTPRequestStoreException
                FTPRequestUserException
                RequestException
        Main
                Main
        TestUtils
                FTPClient
        RequestTest
                FTPRequestCwdTest
                FTPRequestPassTest
                FTPRequestTest
                FTPRequestUnimplementedTest
                FTPRequestUserTest




Design patterns:
        Singleton
        Chaine de responsabilitee
        Strategy




Polymorphisme:
        Classe abstraite RequestException représentant les Exceptions de notre FTP.
        Classe FTPRequestTest représentant le comportant minimal de nos tests de Request.
        Interface Request représentant les méthodes à implémenter par nos Request.


Catch:



catch (IOException e) : 
FtpRequest(Socket socket)
void run() un catch vide car apres le process on fait un close du socket pas besoin de levé d’exception
void initialization(int port) si erreur de connection
FTPClient(int port)




catch (RequestException e) : void processRequest(), 









Throw:
        throws RequestException : void processRequest(String[] request, FtpRequest ftp) (sur la Classe Request et donc toute celle qui hérite de cette dernière, permet de récupérer les Exceptions dûe au traitement des commandes)





*** 3/ Parcours du code (code samples)


PACKAGE Request


Interface pour les Request, cette interface définie les méthodes à implémenter pour les Request commandes. processRequest contient le code traitant la commande si c’est bien elle, autrement elle fait appel au successeur. La méthode succesor fait un appel à la commande suivante pour transmettre la commande. Mettre tout le code des RequestX n’aurait aucun sens donc voici l’interface.


package Request;
import java.io.IOException;
import myFTP.FtpRequest;
import Exception.RequestException;


public interface Request {


public abstract void processRequest(String request[], FtpRequest ftp) throws RequestException, IOException;
public abstract void succesor(String request[], FtpRequest ftp) throws RequestException, IOException;


}


Un exemple de Request, la RequestUser


package Request;
import java.io.IOException;
import myFTP.FtpRequest;
import Exception.FTPRequestUserException;
import Exception.RequestException;


public class RequestUser implements Request {
        // Chaque Request est un singleton car il n’est pas nécessaire de créer X instances. Imaginons ce serveur tournant à grande échelle 1 million de client serait catastrophique si on n’utilisait pas le singleton.
        private RequestUser() {
        }
        private static class SingletonHolder {                
                private final static RequestUser instance = new RequestUser();
        }
         public static RequestUser getInstance() {
                return SingletonHolder.instance;
        }        
        // Méthode traitant la request USER
        public void processRequest(String[] request, FtpRequest ftp) throws RequestException, IOException {
                // Est-ce la commande USER ? Si non alors je transmet la commande au succeseur par l’appel de la méthode succesor définit par l’interface Request
                if(!request[0].equals("USER")){
                        this.succesor(request, ftp);
                        return;                        
                }
                //Si l’utilisateur est connu dans la BDD alors je confirme que le login est ok et je dis au ftp quel est l’user en cours. Autrement je lève l’exception FTPRequestUserException
                if(ftp.containsUserNamed(request[1])){
                        ftp.getWriter().println(331 + " Login ok");
                        ftp.setClient(ftp.getUserNamed(request[1]));
                }else{
                        throw new FTPRequestUserException(ftp);
                }
        }
        // succesor fesant appel à la commande suivante, ici la commande PASS
        public void succesor(String[] request, FtpRequest ftp) throws RequestException, IOException {
                RequestPass.getInstance().processRequest(request, ftp);
        }
}





PACKAGE Exceptions


La classe RequestException hérite de Exception. Elle sert ici à faire du typage dans notre code. Toute les autres extensions héritent de cette classe, RequestException


package Exception;
/**
 * Classe abstraite définisant le type RequestException. 
 * Hérite de Exception, et transmet le message d'erreur
 *
 */
public abstract class RequestException extends Exception{
        
        /** Constructeur prenant un paramètre le message d'erreur **/
        public RequestException(String errorMessage){
                super(errorMessage);
        }
        private static final long serialVersionUID = 1L;
}


Un exemple d’Exception, FTPRequestUserException est une exception levée si le login de l’utilisateur est incorrect. Dans le cas ou cette exception est levée on éxécute la commande QUIT pour forcer la déconnexion du client. FTPRequestUserException et FTPRequestPassException sont les deux seules exception n’étant pas des singletons. Les autres sont des singletons pour limiter la création d’instance. Imaginons 1 millions de client fesant tous des erreurs, ce serait insupportable.


/**
 * Classe spécifique à une exception concernant un problème de login, user incorrect. 
 * Hérite de RequestExeption, et transmet le message d'erreur
 */
public class FTPRequestUserException extends RequestException {
        private static final long serialVersionUID = 1L;
        /**
         * Constructeur prenant un objet de type FtpRequest
         * Transmet un message d'exception spécifique
         * et déconnecte le client 
         * @throws IOException 
         */
        public FTPRequestUserException(FtpRequest ftp) throws IOException {
                //petit message
                super("Unknow login ... disconnection");
                // transmission au client de l’erreur
                ftp.getWriter().println(430 + " Invalid login");
                //Execution d’une requete QUIT pour quitter
                String[] request = {"QUIT"};
                try {
                        RequestQuit.getInstance().processRequest(request, ftp);
                } catch (RequestException e) {
                        e.printStackTrace();
                }
        }
}




PACKAGE myFTP




La classe FTPRequest est une classe dont chaque instance se verra attribué un client ftp. Elle permet de lire les requetes du client, et de les propager pour qu’elles soient traitées. Cette classe contient toutes les informations nécessaire au bon traitement des requetes, notamment l’identitée du client pour savoir si il possède les droits en lecture/écriture.


/**
 * Classe implémentant Runnable. FtpRequest permet de traiter les requêtes des
 * clients Chaque client se voit affecter une instance de FtpRequest
 */
public class FtpRequest implements Runnable {


        // des attributs


        /**
         * Constructeur prenant un objet de type Socket Initialisation du path
         * working directory, la liste des utilisateurs et des outils de
         * communications
         */
        public FtpRequest(Socket socket) {
                this.socket = socket;
                this.pathname = System.getProperty("user.home");
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


        /** Méthode pour initialiser une liste prédéfinie d'users, pour nos tests **/
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
                        // rien car on passe au try suivant pour la deconnection
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
                // ensuite des getter/setter
        }}


La classe Serveur est lancée par le main. Elle va attendre la connexion des clients et créer des threads FtpRequest pour que chaque client puisse traiter les requetes voulues.


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
        //getter setter}






******************************************************
