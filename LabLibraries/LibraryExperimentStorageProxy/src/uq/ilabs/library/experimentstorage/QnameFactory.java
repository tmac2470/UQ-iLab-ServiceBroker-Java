/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage;

import edu.mit.ilab.ilabs.experimentstorage.proxy.AgentAuthHeader;
import edu.mit.ilab.ilabs.experimentstorage.proxy.ObjectFactory;
import edu.mit.ilab.ilabs.experimentstorage.proxy.OperationAuthHeader;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
public class QnameFactory {

    private static ObjectFactory objectFactory;
    private static QName agentAuthHeaderQName;
    private static QName operationAuthHeaderQName;

    /**
     *
     * @return edu.mit.ilab.ilabs.experimentstorage.proxy.ObjectFactory
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

    /**
     *
     * @return QName
     */
    public static QName getOperationAuthHeaderQName() {
        if (operationAuthHeaderQName == null) {
            JAXBElement<OperationAuthHeader> jaxbElement = getObjectFactory().createOperationAuthHeader(new OperationAuthHeader());
            operationAuthHeaderQName = jaxbElement.getName();
        }
        return operationAuthHeaderQName;
    }
}
