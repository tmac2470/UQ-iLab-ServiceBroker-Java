/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.ticketing;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;

/**
 *
 * @author uqlpayne
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthenticateAgentPayload", propOrder = {
    "authGuid",
    "clientGuid",
    "userName",
    "groupName"
})
public class AuthenticateAgentPayload extends Payload {

    protected String authGuid;
    protected String clientGuid;
    protected String userName;
    protected String groupName;

    public String getAuthGuid() {
        return authGuid;
    }

    public void setAuthGuid(String authGuid) {
        this.authGuid = authGuid;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public AuthenticateAgentPayload() {
        super(TicketTypes.AuthenticateProcessAgent.toString());
    }

    public AuthenticateAgentPayload(String authGuid, String clientGuid, String userName, String groupName) {
        super(TicketTypes.AuthenticateProcessAgent.toString());

        this.authGuid = authGuid;
        this.clientGuid = clientGuid;
        this.userName = userName;
        this.groupName = groupName;
    }

    /**
     *
     * @return String
     */
    public String ToXmlString() {
        String xmlString = null;

        try {
            Marshaller marshaller = JAXBContext.newInstance(this.getClass()).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            JAXBElement<AuthenticateAgentPayload> jaxbElement = (new ObjectFactory()).createAuthenticateAgentPayload(this);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(jaxbElement, stringWriter);
            xmlString = stringWriter.toString();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return xmlString;
    }

    /**
     *
     * @param xmlString
     * @return AuthenticateAgentPayload
     */
    public static AuthenticateAgentPayload ToObject(String xmlString) {
        AuthenticateAgentPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(AuthenticateAgentPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<AuthenticateAgentPayload> jaxbElementPayload = (JAXBElement<AuthenticateAgentPayload>) unmarshaller.unmarshal(streamSource, AuthenticateAgentPayload.class);
            object = jaxbElementPayload.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
