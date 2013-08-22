/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.proxy;

import edu.mit.ilab.ilabs.processagent.proxy.AgentAuthHeader;
import edu.mit.ilab.ilabs.processagent.proxy.InitAuthHeader;
import edu.mit.ilab.ilabs.processagent.proxy.ObjectFactory;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author uqlpayne
 */
public class QnameFactory {

    private static ObjectFactory objectFactory;
    private static QName agentAuthHeaderQName;
    private static QName initAuthHeaderQName;

    public static ObjectFactory getObjectFactory() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
        return objectFactory;
    }

    public static QName getAgentAuthHeaderQName() {
        if (agentAuthHeaderQName == null) {
            JAXBElement<AgentAuthHeader> jaxbElement = getObjectFactory().createAgentAuthHeader(new AgentAuthHeader());
            agentAuthHeaderQName = jaxbElement.getName();
        }
        return agentAuthHeaderQName;
    }

    public static QName getInitAuthHeaderQName() {
        if (initAuthHeaderQName == null) {
            JAXBElement<InitAuthHeader> jaxbElement = getObjectFactory().createInitAuthHeader(new InitAuthHeader());
            initAuthHeaderQName = jaxbElement.getName();
        }
        return initAuthHeaderQName;
    }
}
