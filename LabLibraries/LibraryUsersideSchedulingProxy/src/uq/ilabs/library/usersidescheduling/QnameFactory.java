/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.usersidescheduling;

import edu.mit.ilab.ilabs.usersidescheduling.proxy.AgentAuthHeader;
import edu.mit.ilab.ilabs.usersidescheduling.proxy.ObjectFactory;
import edu.mit.ilab.ilabs.usersidescheduling.proxy.OperationAuthHeader;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
public class QnameFactory {

    private static ObjectFactory objectFactory;
    private static QName agentAuthHeader;
    private static QName operationAuthHeader;

    /**
     *
     * @return edu.mit.ilab.ilabs.usersidescheduling.proxy.ObjectFactory
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
    public static QName getAgentAuthHeader() {
        if (agentAuthHeader == null) {
            JAXBElement<AgentAuthHeader> jaxbElement = getObjectFactory().createAgentAuthHeader(new AgentAuthHeader());
            agentAuthHeader = jaxbElement.getName();
        }
        return agentAuthHeader;
    }

    /**
     *
     * @return QName
     */
    public static QName getOperationAuthHeader() {
        if (operationAuthHeader == null) {
            JAXBElement<OperationAuthHeader> jaxbElement = getObjectFactory().createOperationAuthHeader(new OperationAuthHeader());
            operationAuthHeader = jaxbElement.getName();
        }
        return operationAuthHeader;
    }
}
