/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.ticketissuer;

import edu.mit.ilab.ilabs.ticketissuer.proxy.AgentAuthHeader;
import edu.mit.ilab.ilabs.ticketissuer.proxy.ObjectFactory;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
public class QnameFactory {

    private static ObjectFactory objectFactory;
    private static QName agentAuthHeaderQName;

    /**
     *
     * @return edu.mit.ilab.ilabs.ticketissuer.proxy.ObjectFactory
     */
    public static ObjectFactory getObjectFactory() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
        return objectFactory;
    }

    /**
     *
     * @return QName
     */
    public static QName getAgentAuthHeaderQName() {
        if (agentAuthHeaderQName == null) {
            JAXBElement<AgentAuthHeader> jaxbElement = getObjectFactory().createAgentAuthHeader(new AgentAuthHeader());
            agentAuthHeaderQName = jaxbElement.getName();
        }
        return agentAuthHeaderQName;
    }
}
