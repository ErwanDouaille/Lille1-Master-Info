package RequestTest;

import static org.junit.Assert.*;
import org.junit.Test;

public class FTPRequestPassTest extends FTPRequestTest{

	@Test
	public void testLoginOk() {
		assertEquals(this.ftpClient.receive(), "220 Service ready, plz login");
		this.ftpClient.send("USER wawan");
		assertEquals(this.ftpClient.receive(), "331 Login ok");
		this.ftpClient.send("PASS dadou");
		assertEquals(this.ftpClient.receive(), "230 Password ok");
	}

	@Test
	public void testPassFail() {
		assertEquals(this.ftpClient.receive(), "220 Service ready, plz login");
		this.ftpClient.send("USER wawan");
		assertEquals(this.ftpClient.receive(), "331 Login ok");
		this.ftpClient.send("PASS totoFailTest");
		assertEquals(this.ftpClient.receive(), "430 Invalid password");
	}

}
