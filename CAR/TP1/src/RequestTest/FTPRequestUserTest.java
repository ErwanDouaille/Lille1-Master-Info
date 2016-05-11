package RequestTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class FTPRequestUserTest extends FTPRequestTest {

	@Test
	public void test() {
		assertEquals(this.ftpClient.receive(), "220 Service ready, plz login");
		this.ftpClient.send("USER wawan");
		assertEquals(this.ftpClient.receive(), "331 Login ok");
	}

	@Test
	public void testLoginFail() {
		assertEquals(this.ftpClient.receive(), "220 Service ready, plz login");
		this.ftpClient.send("USER totoFailTest");
		assertEquals(this.ftpClient.receive(), "430 Invalid login");
	}

}
