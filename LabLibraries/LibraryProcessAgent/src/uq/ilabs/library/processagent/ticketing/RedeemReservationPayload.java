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
    "start",
    "end",
    "userName",
    "groupName",
    "clientGuid"
})
public class RedeemReservationPayload extends Payload {

    protected String start;
    protected String end;
    protected String userName;
    protected String groupName;
    protected String clientGuid;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public RedeemReservationPayload() {
        this(null, null, null, null, null);
    }

    public RedeemReservationPayload(Calendar start, Calendar end, String userName, String groupName, String clientGuid) {
        super(TicketTypes.RedeemReservation.toString());

        this.start = DateFormat.getDateTimeInstance().format(start.getTime());
        this.end = DateFormat.getDateTimeInstance().format(end.getTime());
        this.userName = userName;
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
            JAXBElement<RedeemReservationPayload> jaxbElement = (new ObjectFactory()).createRedeemReservationPayload(this);
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
     * @return RedeemReservationPayload
     */
    public static RedeemReservationPayload ToObject(String xmlString) {
        RedeemReservationPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(RedeemReservationPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<RedeemReservationPayload> jaxbElement = (JAXBElement<RedeemReservationPayload>) unmarshaller.unmarshal(streamSource, RedeemReservationPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
