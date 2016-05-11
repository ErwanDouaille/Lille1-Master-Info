/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author erwan
 */
@MessageDriven(mappedName = "NewMessage", activationConfig = {
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "NewMessage"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class NewMessage implements MessageListener {

    @Resource
    private MessageDrivenContext mdc;

    @PersistenceContext(unitName = "CarJ2EE-ejbPU")
    private EntityManager entityManager;

    public NewMessage() {
    }

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessageRecu;
        try {
            if (message instanceof ObjectMessage) {
                objectMessageRecu = (ObjectMessage) message;
                if (objectMessageRecu.propertyExists("type")) {
                    String type = objectMessageRecu.getStringProperty("type");

                    if (type != null) {
                        System.out.println(type);
                        if (type.equals("book")) {
                            Book book = (Book) objectMessageRecu.getObject();
                            save(book);
                            System.out.println("Book has been added");
                        }
                        if (type.equals("user")) {
                            Users user = (Users) objectMessageRecu.getObject();
                            save(user);
                            System.out.println("User has been added");
                        }
                    }
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
            mdc.setRollbackOnly();
        } catch (Throwable te) {
            te.printStackTrace();
        }
    }

    public void save(Object object) {
        entityManager.persist(object);
    }
}
