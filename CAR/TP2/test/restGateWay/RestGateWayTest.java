/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restGateWay;

import java.io.InputStream;
import javax.ws.rs.core.Response;
import org.apache.commons.net.ftp.FTPFile;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
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
public class RestGateWayTest {
    
    public RestGateWayTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        RestGateWay restGateWay =new RestGateWay();
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
     * Test of postSignIn method, of class RestGateWay.
     */
    @Test
    public void testPostSignIn() {
        System.out.println("postSignIn");
        String user = "";
        String password = "";
        RestGateWay instance = new RestGateWay();
        String expResult = ""; // appel a la methode static pour get le html ;
        String result = instance.postSignIn(user, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefaultSignIn method, of class RestGateWay.
     */
    @Test
    public void testGetDefaultSignIn() {
        System.out.println("getDefaultSignIn");
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.getDefaultSignIn();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisconnection method, of class RestGateWay.
     */
    @Test
    public void testGetDisconnection() {
        System.out.println("getDisconnection");
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.getDisconnection();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileList method, of class RestGateWay.
     */
    @Test
    public void testGetFileList_0args() {
        System.out.println("getFileList");
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.getFileList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileList method, of class RestGateWay.
     */
    @Test
    public void testGetFileList_String() {
        System.out.println("getFileList");
        String dir = "";
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.getFileList(dir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileListJSON method, of class RestGateWay.
     */
    @Test
    public void testGetFileListJSON() {
        System.out.println("getFileListJSON");
        String dir = "";
        RestGateWay instance = new RestGateWay();
        FTPFile[] expResult = null;
        FTPFile[] result = instance.getFileListJSON(dir);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFile method, of class RestGateWay.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        String dir = "";
        String fileName = "";
        RestGateWay instance = new RestGateWay();
        Response expResult = null;
        Response result = instance.getFile(dir, fileName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStoreForm method, of class RestGateWay.
     */
    @Test
    public void testGetStoreForm() {
        System.out.println("getStoreForm");
        String dir = "";
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.getStoreForm(dir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFile method, of class RestGateWay.
     */
    @Test
    public void testDeleteFile_String_String() {
        System.out.println("deleteFile");
        String dir = "";
        String fileName = "";
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.deleteFile(dir, fileName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFile method, of class RestGateWay.
     */
    @Test
    public void testDeleteFile_String() {
        System.out.println("deleteFile");
        String file = "";
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.deleteFile(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of upload method, of class RestGateWay.
     */
    @Test
    public void testUpload() {
        System.out.println("upload");
        InputStream file = null;
        FormDataContentDisposition fileDetail = null;
        String dir = "";
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.upload(file, fileDetail, dir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putUpload method, of class RestGateWay.
     */
    @Test
    public void testPutUpload() {
        System.out.println("putUpload");
        String filePath = "";
        RestGateWay instance = new RestGateWay();
        String expResult = "";
        String result = instance.putUpload(filePath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
