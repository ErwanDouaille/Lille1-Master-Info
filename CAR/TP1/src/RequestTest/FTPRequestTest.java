package RequestTest;

import myFTP.Serveur;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import TestUtils.FTPClient;

public class FTPRequestTest {
	
	public static Serveur ftpServer;
	public FTPClient ftpClient;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ftpServer = new Serveur();
		ftpServer.initialization(2121);
		ftpServer.start();
	}

	@SuppressWarnings("deprecation")
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ftpServer.stop();
	}

	@Before
	public void setUp() throws Exception {
		this.ftpClient = new FTPClient(2121);
		System.out.println("Conencted");
	}

	@After
	public void tearDown() throws Exception {
		this.ftpClient.close();
	}

}
