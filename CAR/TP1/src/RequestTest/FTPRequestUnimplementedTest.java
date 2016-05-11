package RequestTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class FTPRequestUnimplementedTest extends FTPRequestTest {

	@Test
	public void test() {
		assertEquals(this.ftpClient.receive(), "220 Service ready, plz login");
		this.ftpClient.send("FAILURETESTOK");
		assertEquals(this.ftpClient.receive(), "502 Command not implemented");
	}

}
