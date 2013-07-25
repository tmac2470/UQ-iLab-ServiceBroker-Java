/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.ticketing;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Calendar;
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
@XmlType(name = "RedeemSessionPayload", propOrder = {
    "startExecution",
    "duration",
    "groupName",
    "clientGuid"
})
public class AllowExperimentExecutionPayload extends Payload {

    protected String startExecution;
    protected long duration;
    protected String groupName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public AllowExperimentExecutionPayload() {
        this(null, 0, null, null);
    }

    public AllowExperimentExecutionPayload(Calendar startExecution, long duration, String groupName, String clientGuid) {
        super(TicketTypes.AllowExperimentExecution.toString());

        this.startExecution = DateFormat.getDateTimeInstance().format(startExecution.getTime());
        this.duration = duration;
        this.groupName = groupName;
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
            JAXBElement<AllowExperimentExecutionPayload> jaxbElement = (new ObjectFactory()).createAllowExperimentExecutionPayload(this);
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
     * @return AllowExperimentExecutionPayload
     */
    public static AllowExperimentExecutionPayload ToObject(String xmlString) {
        AllowExperimentExecutionPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(AllowExperimentExecutionPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<AllowExperimentExecutionPayload> jaxbElement = (JAXBElement<AllowExperimentExecutionPayload>) unmarshaller.unmarshal(streamSource, AllowExperimentExecutionPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
