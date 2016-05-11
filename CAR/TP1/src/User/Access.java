package User;


/**
 * @author Douaille Erwan (douailleerwan@gmail.com) & Jeremy Opsommer (jeremy.opsommer@gmail.com)
 *
 */

/**
 * Classe d'accès, définit les accès de l'utilisateur
 */
public class Access { 
	
	
	private boolean read, write;
	
	public Access(){
		this.read=true;
		this.write=false;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

}
