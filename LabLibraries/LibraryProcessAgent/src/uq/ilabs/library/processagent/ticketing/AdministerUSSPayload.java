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
@XmlType(name = "AdministerUSSPayload", propOrder = {
    "userTZ"
})
public class AdministerUSSPayload extends Payload {

    protected int userTZ;

    public int getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(int userTZ) {
        this.userTZ = userTZ;
    }

    public AdministerUSSPayload() {
        this(0);
    }

    public AdministerUSSPayload(int userTZ) {
        super(TicketTypes.AdministerUSS.toString());

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
            JAXBElement<AdministerUSSPayload> jaxbElement = (new ObjectFactory()).createAdministerUSSPayload(this);
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
     * @return AdministerUSSPayload
     */
    public static AdministerUSSPayload ToObject(String xmlString) {
        AdministerUSSPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(AdministerUSSPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<AdministerUSSPayload> jaxbElement = (JAXBElement<AdministerUSSPayload>) unmarshaller.unmarshal(streamSource, AdministerUSSPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
