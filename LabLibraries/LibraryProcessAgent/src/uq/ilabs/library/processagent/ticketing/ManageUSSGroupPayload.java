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
@XmlType(name = "ManageUSSGroupPayload", propOrder = {
    "groupName",
    "sbGuid",
    "clientGuid",
    "userTZ"
})
public class ManageUSSGroupPayload extends Payload {

    protected String groupName;
    protected String sbGuid;
    protected String clientGuid;
    protected int userTZ;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSbGuid() {
        return sbGuid;
    }

    public void setSbGuid(String sbGuid) {
        this.sbGuid = sbGuid;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public int getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(int userTZ) {
        this.userTZ = userTZ;
    }

    public ManageUSSGroupPayload() {
        this(null, null, null, 0);
    }

    public ManageUSSGroupPayload(String groupName, String sbGuid, String clientGuid, int userTZ) {
        super(TicketTypes.ManageUSSGroup.toString());

        this.groupName = groupName;
        this.sbGuid = sbGuid;
        this.clientGuid = clientGuid;
        this.userTZ = userTZ;
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
            JAXBElement<ManageUSSGroupPayload> jaxbElement = (new ObjectFactory()).createManageUSSGroupPayload(this);
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
     * @return ManageUSSGroupPayload
     */
    public static ManageUSSGroupPayload ToObject(String xmlString) {
        ManageUSSGroupPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(ManageUSSGroupPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<ManageUSSGroupPayload> jaxbElement = (JAXBElement<ManageUSSGroupPayload>) unmarshaller.unmarshal(streamSource, ManageUSSGroupPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
