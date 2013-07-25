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
@XmlType(name = "StoreRecordsPayload", propOrder = {
    "blob",
    "experimentID",
    "essURL"
})
public class StoreRecordsPayload extends Payload {

    protected boolean blob;
    protected long experimentID;
    protected String essURL;

    public boolean isBlob() {
        return blob;
    }

    public void setBlob(boolean blob) {
        this.blob = blob;
    }

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

    public StoreRecordsPayload() {
        this(false, 0, null);
    }

    public StoreRecordsPayload(boolean blob, long experimentID, String essURL) {
        super(TicketTypes.StoreRecords.toString());

        this.blob = blob;
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
            JAXBElement<StoreRecordsPayload> jaxbElement = (new ObjectFactory()).createStoreRecordsPayload(this);
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
     * @return StoreRecordsPayload
     */
    public static StoreRecordsPayload ToObject(String xmlString) {
        StoreRecordsPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(StoreRecordsPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<StoreRecordsPayload> jaxbElementPayload = (JAXBElement<StoreRecordsPayload>) unmarshaller.unmarshal(streamSource, StoreRecordsPayload.class);
            object = jaxbElementPayload.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
