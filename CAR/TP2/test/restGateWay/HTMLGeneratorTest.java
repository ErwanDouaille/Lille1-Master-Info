/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restGateWay;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Erwan Douaille & Maxime Douylliez
 */
public class HTMLGeneratorTest {
    
    public HTMLGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class HTMLGenerator.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        HTMLGenerator expResult = null;
        HTMLGenerator result = HTMLGenerator.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRemoveConfirmation method, of class HTMLGenerator.
     */
    @Test
    public void testGetRemoveConfirmation() {
        System.out.println("getRemoveConfirmation");
        String dir = "";
        String file = "";
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getRemoveConfirmation(dir, file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUploadConfirmation method, of class HTMLGenerator.
     */
    @Test
    public void testGetUploadConfirmation() {
        System.out.println("getUploadConfirmation");
        String dir = "";
        String file = "";
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getUploadConfirmation(dir, file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUploadContent method, of class HTMLGenerator.
     */
    @Test
    public void testGetUploadContent() {
        System.out.println("getUploadContent");
        String cwd = "";
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getUploadContent(cwd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUploadForm method, of class HTMLGenerator.
     */
    @Test
    public void testGetUploadForm() {
        System.out.println("getUploadForm");
        String cwd = "";
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getUploadForm(cwd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSignInContent method, of class HTMLGenerator.
     */
    @Test
    public void testGetSignInContent() {
        System.out.println("getSignInContent");
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getSignInContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBadConnectionContent method, of class HTMLGenerator.
     */
    @Test
    public void testGetBadConnectionContent() {
        System.out.println("getBadConnectionContent");
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getBadConnectionContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisconnectionContent method, of class HTMLGenerator.
     */
    @Test
    public void testGetDisconnectionContent() {
        System.out.println("getDisconnectionContent");
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getDisconnectionContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisconnectionButton method, of class HTMLGenerator.
     */
    @Test
    public void testGetDisconnectionButton() {
        System.out.println("getDisconnectionButton");
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getDisconnectionButton();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUploadButton method, of class HTMLGenerator.
     */
    @Test
    public void testGetUploadButton() {
        System.out.println("getUploadButton");
        String cwd = "";
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getUploadButton(cwd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSomethingWentWrong method, of class HTMLGenerator.
     */
    @Test
    public void testGetSomethingWentWrong() {
        System.out.println("getSomethingWentWrong");
        String errorCode = "";
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getSomethingWentWrong(errorCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getShouldBeImplemented method, of class HTMLGenerator.
     */
    @Test
    public void testGetShouldBeImplemented() {
        System.out.println("getShouldBeImplemented");
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getShouldBeImplemented();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileListWith method, of class HTMLGenerator.
     */
    @Test
    public void testGetFileListWith() {
        System.out.println("getFileListWith");
        String cwd = "";
        FTPFile[] fileList = null;
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getFileListWith(cwd, fileList);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCssContent method, of class HTMLGenerator.
     */
    @Test
    public void testGetCssContent() {
        System.out.println("getCssContent");
        HTMLGenerator instance = new HTMLGenerator();
        String expResult = "";
        String result = instance.getCssContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
