package rmiNodeCoreTest;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;

import rmiNodeCore.SiteImpl;
import rmiNodeCore.SiteItf;

public class SiteImplTest {

	@Test
	public void testId() throws Exception {
		SiteItf instance = (SiteItf) new SiteImpl("1");
		assertEquals(instance.getId(), "1");
	}

	@Test
	public void testListOfSonAsTree() throws Exception {
		SiteItf instance = (SiteItf) new SiteImpl("1");
		SiteItf instance2 = (SiteItf) new SiteImpl("2");
		assertEquals(instance.getId(), "1");
		assertEquals(instance2.getId(), "2");
		instance.addSon(instance2);
		assertEquals(instance.getListOfSon().size(), 1);
		assertEquals(instance2.getListOfSon().size(), 0);
	}

	@Test
	public void testListOfSonAsGraph() throws Exception {
		SiteItf instance = (SiteItf) new SiteImpl("1");
		SiteItf instance2 = (SiteItf) new SiteImpl("2");
		assertEquals(instance.getId(), "1");
		assertEquals(instance2.getId(), "2");
		instance.addSon(instance2);
		instance2.addFather(instance);
		assertEquals(instance.getListOfSon().size(), 1);
		assertEquals(instance2.getListOfSon().size(), 0);
		assertEquals(instance.getListOfFather().size(), 0);
		assertEquals(instance2.getListOfFather().size(), 1);
	}

	@Test
	public void testReceive() throws Exception {
		SiteItf instance = (SiteItf) new SiteImpl("1");
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));

		String messageData[] = new String[2];
		messageData[0] = "1";
		messageData[1] = "toto";
		instance.sendMessage(messageData);

		assertEquals(outContent.toString(), "1 receive: toto");
	}

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


	@Test
	public void testDefaultBlockingLoopMessageState() throws Exception {
		SiteItf instance = (SiteItf) new SiteImpl("1");
		assertEquals(instance.getId(), "1");
		assertEquals(instance.isBlockingLoopMessage(), false);
	}

}
