/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package restGateWay;

import javax.ejb.embeddable.EJBContainer;
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
public class NameStorageBeanTest {
    
    public NameStorageBeanTest() {
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
     * Test of getLogin method, of class NameStorageBean.
     */
    @Test
    public void testGetLogin() throws Exception {
        System.out.println("getLogin");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        NameStorageBean instance = (NameStorageBean)container.getContext().lookup("java:global/classes/NameStorageBean");
        String expResult = "";
        String result = instance.getLogin();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPassword method, of class NameStorageBean.
     */
    @Test
    public void testGetPassword() throws Exception {
        System.out.println("getPassword");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        NameStorageBean instance = (NameStorageBean)container.getContext().lookup("java:global/classes/NameStorageBean");
        String expResult = "";
        String result = instance.getPassword();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLogin method, of class NameStorageBean.
     */
    @Test
    public void testSetLogin() throws Exception {
        System.out.println("setLogin");
        String login = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        NameStorageBean instance = (NameStorageBean)container.getContext().lookup("java:global/classes/NameStorageBean");
        instance.setLogin(login);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPassword method, of class NameStorageBean.
     */
    @Test
    public void testSetPassword() throws Exception {
        System.out.println("setPassword");
        String password = "";
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        NameStorageBean instance = (NameStorageBean)container.getContext().lookup("java:global/classes/NameStorageBean");
        instance.setPassword(password);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
