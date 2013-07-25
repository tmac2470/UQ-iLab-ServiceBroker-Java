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
@XmlType(name = "AuthorizeClientPayload", propOrder = {
    "clientGuid",
    "groupName"
})
public class AuthorizeClientPayload extends Payload {

    protected String clientGuid;
    protected String groupName;

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public AuthorizeClientPayload() {
        this(null, null);
    }

    public AuthorizeClientPayload(String clientGuid, String groupName) {
        super(TicketTypes.AuthorizeClient.toString());

        this.clientGuid = clientGuid;
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
            JAXBElement<AuthorizeClientPayload> jaxbElement = (new ObjectFactory()).createAuthorizeClientPayload(this);
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
     * @return AuthorizeClientPayload
     */
    public static AuthorizeClientPayload ToObject(String xmlString) {
        AuthorizeClientPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(AuthorizeClientPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<AuthorizeClientPayload> jaxbElement = (JAXBElement<AuthorizeClientPayload>) unmarshaller.unmarshal(streamSource, AuthorizeClientPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
