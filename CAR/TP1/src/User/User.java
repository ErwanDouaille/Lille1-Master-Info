package User;

/**
 * Classe définisant un utilisateur
 * Contient des méthodes pour savoir 
 * si l'utilisateur à des accès en écriture 
 * et lecture
 */
public class User {
	
	private String login;
	private String password;
	private Access access;
	
	/**
	 * Constructeur, prenant un login et un mot de passe
	 */
	public User(String login, String pass){
		this.login=login;
		this.password=pass;
		this.access = new Access();
	}
	
	public void setAccess(Access access){
		this.access = access;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Access getAccess() {
		return access;
	}
	
	/**
	 * Indique si l'utilisateur peut lire
	 */
	public boolean canRead(){
		return this.access.isRead();
	}
	
	/**
	 * Indique si l'utilisateur peut écrire
	 */
	public boolean canWrite(){
		return this.access.isWrite();
	}
	
	/**
	 * Vérification du login
	 */
	public boolean isLogin(String login){
		return this.login.equals(login);
	}
	
	/**
	 * Vérification du password
	 */	
	public boolean isPassword(String pass){
		return this.password.equals(pass);
	}	
	
	public String toString(){
		return this.login.toString();
	}

}
