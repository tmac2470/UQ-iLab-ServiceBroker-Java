/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.service;

import edu.mit.ilab.SbAuthHeader;
import edu.mit.ilab.ilabs.type.AgentAuthHeader;
import edu.mit.ilab.ilabs.type.InitAuthHeader;
import edu.mit.ilab.ilabs.type.ObjectFactory;
import edu.mit.ilab.ilabs.type.OperationAuthHeader;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author uqlpayne
 */
public class QnameFactory {

    private static edu.mit.ilab.ObjectFactory sbObjectFactory;
    private static String sbAuthHeaderLocalPart;
    private static ObjectFactory objectFactory;
    private static String agentAuthHeaderLocalPart;
    private static String initAuthHeaderLocalPart;
    private static String operationAuthHeaderLocalPart;

    /**
     *
     * @return edu.mit.ilab.ObjectFactory
     */
    public static edu.mit.ilab.ObjectFactory getSbObjectFactory() {
        if (sbObjectFactory == null) {
            sbObjectFactory = new edu.mit.ilab.ObjectFactory();
        }
        return sbObjectFactory;
    }

    /**
     *
     * @return String
     */
    public static String getSbAuthHeaderLocalPart() {
        if (sbAuthHeaderLocalPart == null) {
            JAXBElement<SbAuthHeader> jaxbElement = getSbObjectFactory().createSbAuthHeader(new SbAuthHeader());
            sbAuthHeaderLocalPart = jaxbElement.getName().getLocalPart();
        }
        return sbAuthHeaderLocalPart;
    }

    /**
     *
     * @return edu.mit.ilab.ilabs.type.ObjectFactory
     */
    public static ObjectFactory getObjectFactory() {
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
        return objectFactory;
    }

    /**
     *
     * @return String
     */
    public static String getAgentAuthHeaderLocalPart() {
        if (agentAuthHeaderLocalPart == null) {
            JAXBElement<AgentAuthHeader> jaxbElement = getObjectFactory().createAgentAuthHeader(new AgentAuthHeader());
            agentAuthHeaderLocalPart = jaxbElement.getName().getLocalPart();
        }
        return agentAuthHeaderLocalPart;
    }

    /**
     *
     * @return String
     */
    public static String getInitAuthHeaderLocalPart() {
        if (initAuthHeaderLocalPart == null) {
            JAXBElement<InitAuthHeader> jaxbElement = getObjectFactory().createInitAuthHeader(new InitAuthHeader());
            initAuthHeaderLocalPart = jaxbElement.getName().getLocalPart();
        }
        return initAuthHeaderLocalPart;
    }

    /**
     *
     * @return String
     */
    public static String getOperationAuthHeaderLocalPart() {
        if (operationAuthHeaderLocalPart == null) {
            JAXBElement<OperationAuthHeader> jaxbElement = getObjectFactory().createOperationAuthHeader(new OperationAuthHeader());
            operationAuthHeaderLocalPart = jaxbElement.getName().getLocalPart();
        }
        return operationAuthHeaderLocalPart;
    }
}
