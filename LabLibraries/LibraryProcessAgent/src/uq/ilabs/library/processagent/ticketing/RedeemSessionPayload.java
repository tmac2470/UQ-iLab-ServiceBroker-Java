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
@XmlType(name = "RedeemSessionPayload", propOrder = {
    "userID",
    "groupID",
    "clientID",
    "userName",
    "groupName"
})
public class RedeemSessionPayload extends Payload {

    protected int userID;
    protected int groupID;
    protected int clientID;
    protected String userName;
    protected String groupName;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
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

    public RedeemSessionPayload() {
        this(0, 0, 0, null, null);
    }

    public RedeemSessionPayload(int userID, int groupID, int clientID, String userName, String groupName) {
        super(TicketTypes.RedeemSession.toString());

        this.userID = userID;
        this.groupID = groupID;
        this.clientID = clientID;
        this.userName = userName;
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
            JAXBElement<RedeemSessionPayload> jaxbElement = (new ObjectFactory()).createRedeemSessionPayload(this);
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
     * @return RedeemSessionPayload
     */
    public static RedeemSessionPayload ToObject(String xmlString) {
        RedeemSessionPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(RedeemSessionPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<RedeemSessionPayload> jaxbElement = (JAXBElement<RedeemSessionPayload>) unmarshaller.unmarshal(streamSource, RedeemSessionPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
