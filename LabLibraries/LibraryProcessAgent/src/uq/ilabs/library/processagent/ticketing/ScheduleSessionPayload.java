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
@XmlType(name = "ScheduleSessionPayload", propOrder = {
    "userName",
    "groupName",
    "sbGuid",
    "labServerGuid",
    "userName",
    "labClientName",
    "labClientVersion",
    "ussURL",
    "userTZ"
})
public class ScheduleSessionPayload extends Payload {

    protected String userName;
    protected String groupName;
    protected String sbGuid;
    protected String labServerGuid;
    protected String clientGuid;
    protected String labClientName;
    protected String labClientVersion;
    protected String ussURL;
    protected int userTZ;

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

    public String getSbGuid() {
        return sbGuid;
    }

    public void setSbGuid(String sbGuid) {
        this.sbGuid = sbGuid;
    }

    public String getLabServerGuid() {
        return labServerGuid;
    }

    public void setLabServerGuid(String labServerGuid) {
        this.labServerGuid = labServerGuid;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public String getLabClientName() {
        return labClientName;
    }

    public void setLabClientName(String labClientName) {
        this.labClientName = labClientName;
    }

    public String getLabClientVersion() {
        return labClientVersion;
    }

    public void setLabClientVersion(String labClientVersion) {
        this.labClientVersion = labClientVersion;
    }

    public String getUssURL() {
        return ussURL;
    }

    public void setUssURL(String ussURL) {
        this.ussURL = ussURL;
    }

    public int getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(int userTZ) {
        this.userTZ = userTZ;
    }

    public ScheduleSessionPayload() {
        this(null, null, null, null, null, null, null, null, 0);
    }

    public ScheduleSessionPayload(String userName, String groupName, String sbGuid, String labServerGuid, String clientGuid, String labClientName, String labClientVersion, String ussURL, int userTZ) {
        super(TicketTypes.ScheduleSession.toString());

        this.userName = userName;
        this.groupName = groupName;
        this.sbGuid = sbGuid;
        this.labServerGuid = labServerGuid;
        this.clientGuid = clientGuid;
        this.labClientName = labClientName;
        this.labClientVersion = labClientVersion;
        this.ussURL = ussURL;
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
            JAXBElement<ScheduleSessionPayload> jaxbElement = (new ObjectFactory()).createScheduleSessionPayload(this);
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
     * @return ScheduleSessionPayload
     */
    public static ScheduleSessionPayload ToObject(String xmlString) {
        ScheduleSessionPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(ScheduleSessionPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<ScheduleSessionPayload> jaxbElement = (JAXBElement<ScheduleSessionPayload>) unmarshaller.unmarshal(streamSource, ScheduleSessionPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
