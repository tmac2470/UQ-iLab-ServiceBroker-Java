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
@XmlType(name = "AdministerLSPayload", propOrder = {
    "userTZ"
})
public class AdministerLSPayload extends Payload {

    protected int userTZ;

    public int getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(int userTZ) {
        this.userTZ = userTZ;
    }

    public AdministerLSPayload() {
        this(0);
    }

    public AdministerLSPayload(int userTZ) {
        super(TicketTypes.AdministerLabServer.toString());

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
            JAXBElement<AdministerLSPayload> jaxbElement = (new ObjectFactory()).createAdministerLSPayload(this);
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
     * @return AdministerLSPayload
     */
    public static AdministerLSPayload ToObject(String xmlString) {
        AdministerLSPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(AdministerLSPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<AdministerLSPayload> jaxbElement = (JAXBElement<AdministerLSPayload>) unmarshaller.unmarshal(streamSource, AdministerLSPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
