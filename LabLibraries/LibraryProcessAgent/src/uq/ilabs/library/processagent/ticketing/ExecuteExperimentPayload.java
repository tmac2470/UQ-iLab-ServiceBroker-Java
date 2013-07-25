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
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;

/**
 *
 * @author uqlpayne
 */
@XmlType(name = "ExecuteExperimentPayload", propOrder = {
    "essWebAddress",
    "startExecution",
    "duration",
    "groupName",
    "sbGuid",
    "experimentID",
    "userTZ"
})
public class ExecuteExperimentPayload extends Payload {

    protected String essWebAddress;
    protected String startExecution;
    protected long duration;
    protected String groupName;
    protected String sbGuid;
    protected long experimentID;
    protected int userTZ;

    public String getEssWebAddress() {
        return essWebAddress;
    }

    public void setEssWebAddress(String essWebAddress) {
        this.essWebAddress = essWebAddress;
    }

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

    public String getSbGuid() {
        return sbGuid;
    }

    public void setSbGuid(String sbGuid) {
        this.sbGuid = sbGuid;
    }

    public long getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(long experimentID) {
        this.experimentID = experimentID;
    }

    public int getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(int userTZ) {
        this.userTZ = userTZ;
    }

    public ExecuteExperimentPayload() {
        this(null, null, 0, null, null, 0, 0);
    }

    public ExecuteExperimentPayload(String essWebAddress, Calendar startExecution, long duration, String groupName, String sbGuid, long experimentID, int userTZ) {
        super(TicketTypes.ExecuteExperiment.toString());

        this.essWebAddress = essWebAddress;
        this.startExecution = DateFormat.getDateTimeInstance().format(startExecution.getTime());
        this.duration = duration;
        this.groupName = groupName;
        this.sbGuid = sbGuid;
        this.experimentID = experimentID;
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
            JAXBElement<ExecuteExperimentPayload> jaxbElement = (new ObjectFactory()).createExecuteExperimentPayload(this);
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
     * @return ExecuteExperimentPayload
     */
    public static ExecuteExperimentPayload ToObject(String xmlString) {
        ExecuteExperimentPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(ExecuteExperimentPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<ExecuteExperimentPayload> jaxbElement = (JAXBElement<ExecuteExperimentPayload>) unmarshaller.unmarshal(streamSource, ExecuteExperimentPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
