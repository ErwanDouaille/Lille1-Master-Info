package RequestTest;

import static org.junit.Assert.*;
import org.junit.Test;

public class FTPRequestCwdTest extends FTPRequestTest {

	@Test
	public void test() {
		assertEquals(this.ftpClient.receive(), "220 Service ready, plz login");
		this.ftpClient.send("USER anonymous");
		assertEquals(this.ftpClient.receive(), "331 Login ok");
		this.ftpClient.send("PASS anon@localhost");
		assertEquals(this.ftpClient.receive(), "230 Password ok");
	}

}
