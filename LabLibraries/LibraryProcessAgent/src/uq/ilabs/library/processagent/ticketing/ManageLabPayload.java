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
@XmlType(name = "ManageLabPayload", propOrder = {
    "labServerGuid",
    "labServerName",
    "sbGuid",
    "adminGroup",
    "userTZ"
})
public class ManageLabPayload extends Payload {

    protected String labServerGuid;
    protected String labServerName;
    protected String sbGuid;
    protected String adminGroup;
    protected int userTZ;

    public String getLabServerGuid() {
        return labServerGuid;
    }

    public void setLabServerGuid(String labServerGuid) {
        this.labServerGuid = labServerGuid;
    }

    public String getLabServerName() {
        return labServerName;
    }

    public void setLabServerName(String labServerName) {
        this.labServerName = labServerName;
    }

    public String getSbGuid() {
        return sbGuid;
    }

    public void setSbGuid(String sbGuid) {
        this.sbGuid = sbGuid;
    }

    public String getAdminGroup() {
        return adminGroup;
    }

    public void setAdminGroup(String adminGroup) {
        this.adminGroup = adminGroup;
    }

    public int getUserTZ() {
        return userTZ;
    }

    public void setUserTZ(int userTZ) {
        this.userTZ = userTZ;
    }

    public ManageLabPayload() {
        this(null, null, null, null, 0);
    }

    public ManageLabPayload(String labServerGuid, String labServerName, String sbGuid, String adminGroup, int userTZ) {
        super(TicketTypes.ManageLab.toString());

        this.labServerGuid = labServerGuid;
        this.labServerName = labServerName;
        this.sbGuid = sbGuid;
        this.adminGroup = adminGroup;
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
            JAXBElement<ManageLabPayload> jaxbElement = (new ObjectFactory()).createManageLabPayload(this);
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
     * @return ManageLabPayload
     */
    public static ManageLabPayload ToObject(String xmlString) {
        ManageLabPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(ManageLabPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<ManageLabPayload> jaxbElement = (JAXBElement<ManageLabPayload>) unmarshaller.unmarshal(streamSource, ManageLabPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
