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
@XmlType(name = "RetrieveRecordsPayload", propOrder = {
    "experimentID",
    "essURL"
})
public class RetrieveRecordsPayload extends Payload {

    protected long experimentID;
    protected String essURL;

    public long getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(long experimentID) {
        this.experimentID = experimentID;
    }

    public String getEssURL() {
        return essURL;
    }

    public void setEssURL(String essURL) {
        this.essURL = essURL;
    }

    public RetrieveRecordsPayload() {
        this(0, null);
    }

    public RetrieveRecordsPayload(long experimentID, String essURL) {
        super(TicketTypes.RetrieveRecords.toString());

        this.experimentID = experimentID;
        this.essURL = essURL;
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
            JAXBElement<RetrieveRecordsPayload> jaxbElement = (new ObjectFactory()).createRetrieveRecordsPayload(this);
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
     * @return RetrieveRecordsPayload
     */
    public static RetrieveRecordsPayload ToObject(String xmlString) {
        RetrieveRecordsPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(RetrieveRecordsPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<RetrieveRecordsPayload> jaxbElementPayload = (JAXBElement<RetrieveRecordsPayload>) unmarshaller.unmarshal(streamSource, RetrieveRecordsPayload.class);
            object = jaxbElementPayload.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
