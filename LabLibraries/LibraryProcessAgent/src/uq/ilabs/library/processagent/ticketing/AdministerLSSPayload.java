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
public class AdministerLSSPayload extends Payload {

    protected int userTZ;

    public int getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(int userTZ) {
        this.userTZ = userTZ;
    }

    public AdministerLSSPayload() {
        this(0);
    }

    public AdministerLSSPayload(int userTZ) {
        super(TicketTypes.AdministerLSS.toString());

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
            JAXBElement<AdministerLSSPayload> jaxbElement = (new ObjectFactory()).createAdministerLSSPayload(this);
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
     * @return AdministerLSSPayload
     */
    public static AdministerLSSPayload ToObject(String xmlString) {
        AdministerLSSPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(AdministerLSSPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<AdministerLSSPayload> jaxbElement = (JAXBElement<AdministerLSSPayload>) unmarshaller.unmarshal(streamSource, AdministerLSSPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
