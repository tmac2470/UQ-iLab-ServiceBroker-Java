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
import javax.xml.transform.stream.StreamSource;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;

/**
 *
 * @author uqlpayne
 */
public class AdministerESSPayload extends Payload {

    public AdministerESSPayload() {
        super(TicketTypes.AdministerESS.toString());
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
            JAXBElement<AdministerESSPayload> jaxbElement = (new ObjectFactory()).createAdministerESSPayload(this);
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
     * @return AdministerESSPayload
     */
    public static AdministerESSPayload ToObject(String xmlString) {
        AdministerESSPayload administerESSPayload = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(AdministerESSPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<AdministerESSPayload> jaxbElementPayload = (JAXBElement<AdministerESSPayload>) unmarshaller.unmarshal(streamSource, AdministerESSPayload.class);
            administerESSPayload = jaxbElementPayload.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return administerESSPayload;
    }
}
