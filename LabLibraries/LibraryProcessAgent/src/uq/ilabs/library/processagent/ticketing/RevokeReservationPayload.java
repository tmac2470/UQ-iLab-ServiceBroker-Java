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
@XmlType(name = "RevokeReservationPayload", propOrder = {
    "source",
    "userName",
    "groupName",
    "authorityGuid",
    "clientGuid",
    "ussURL"
})
public class RevokeReservationPayload extends Payload {

    protected String source;
    protected String userName;
    protected String groupName;
    protected String authorityGuid;
    protected String clientGuid;
    protected String ussURL;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getAuthorityGuid() {
        return authorityGuid;
    }

    public void setAuthorityGuid(String authorityGuid) {
        this.authorityGuid = authorityGuid;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public String getUssURL() {
        return ussURL;
    }

    public void setUssURL(String ussURL) {
        this.ussURL = ussURL;
    }

    public RevokeReservationPayload() {
        this(null, null, null, null, null, null);
    }

    public RevokeReservationPayload(String source, String userName, String groupName, String authorityGuid, String clientGuid, String ussURL) {
        super(TicketTypes.RevokeReservation.toString());

        this.source = source;
        this.userName = userName;
        this.groupName = groupName;
        this.authorityGuid = authorityGuid;
        this.clientGuid = clientGuid;
        this.ussURL = ussURL;
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
            JAXBElement<RevokeReservationPayload> jaxbElement = (new ObjectFactory()).createRevokeReservationPayload(this);
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
     * @return RevokeReservationPayload
     */
    public static RevokeReservationPayload ToObject(String xmlString) {
        RevokeReservationPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(RevokeReservationPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<RevokeReservationPayload> jaxbElement = (JAXBElement<RevokeReservationPayload>) unmarshaller.unmarshal(streamSource, RevokeReservationPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
