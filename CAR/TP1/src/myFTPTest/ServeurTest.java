package myFTPTest;

import static org.junit.Assert.*;


import java.io.IOException;
import java.net.UnknownHostException;
import myFTP.Serveur;
import org.junit.Test;

public class ServeurTest {

	@Test
	public void testRunning() throws UnknownHostException, IOException {
		  Serveur serveur = new Serveur();
		  serveur.initialization(2121);
		  assertNotNull(serveur.getServeurSocket());
		  assertNotNull(serveur.getSocket());
		  assertEquals(serveur.getServeurSocket().getLocalPort(), 2121);
	}
	
	

}
