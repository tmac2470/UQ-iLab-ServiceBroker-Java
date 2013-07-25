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
@XmlType(name = "CreateExperimentPayload", propOrder = {
    "startExecution",
    "duration",
    "userName",
    "labGuid",
    "groupName",
    "clientGuid"
})
public class CreateExperimentPayload extends Payload {

    protected String startExecution;
    protected long duration;
    protected String userName;
    protected String groupName;
    protected String labGuid;
    protected String clientGuid;

    public String getStartExecution() {
        return startExecution;
    }

    public void setStartExecution(String startExecution) {
        this.startExecution = startExecution;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    public String getLabGuid() {
        return labGuid;
    }

    public void setLabGuid(String labGuid) {
        this.labGuid = labGuid;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public CreateExperimentPayload() {
        this(null, 0, null, null, null, null);
    }

    public CreateExperimentPayload(String startExecution, long duration, String userName, String groupName, String labGuid, String clientGuid) {
        super(TicketTypes.CreateExperiment.toString());

        this.startExecution = startExecution;
        this.duration = duration;
        this.userName = userName;
        this.groupName = groupName;
        this.labGuid = labGuid;
        this.clientGuid = clientGuid;
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
            JAXBElement<CreateExperimentPayload> jaxbElement = (new ObjectFactory()).createCreateExperimentPayload(this);
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
     * @return CreateExperimentPayload
     */
    public static CreateExperimentPayload ToObject(String xmlString) {
        CreateExperimentPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(CreateExperimentPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<CreateExperimentPayload> jaxbElement = (JAXBElement<CreateExperimentPayload>) unmarshaller.unmarshal(streamSource, CreateExperimentPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
