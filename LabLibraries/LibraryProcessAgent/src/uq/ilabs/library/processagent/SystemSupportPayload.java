/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

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
import uq.ilabs.library.processagent.database.types.SystemSupportInfo;

/**
 *
 * @author uqlpayne
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "systemSupport", propOrder = {
    "agentGuid",
    "contactName",
    "contactEmail",
    "bugEmail",
    "location",
    "infoUrl",
    "description"
})
public class SystemSupportPayload {

    protected String agentGuid;
    protected String contactName;
    protected String contactEmail;
    protected String bugEmail;
    protected String location;
    protected String infoUrl;
    protected String description;

    public String getAgentGuid() {
        return agentGuid;
    }

    public void setAgentGuid(String agentGuid) {
        this.agentGuid = agentGuid;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getBugEmail() {
        return bugEmail;
    }

    public void setBugEmail(String bugEmail) {
        this.bugEmail = bugEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SystemSupportPayload() {
    }

    public SystemSupportPayload(SystemSupportInfo systemSupportInfo) {
        this.agentGuid = systemSupportInfo.getAgentGuid();
        this.contactName = systemSupportInfo.getContactName();
        this.contactEmail = systemSupportInfo.getContactEmail();
        this.bugEmail = systemSupportInfo.getBugReportEmail();
        this.location = systemSupportInfo.getLocation();
        this.infoUrl = systemSupportInfo.getInfoUrl();
        this.description = systemSupportInfo.getDescription();
    }

    /**
     *
     * @return SystemSupportInfo
     */
    public SystemSupportInfo ToSystemSupportInfo() {
        return new SystemSupportInfo(
                this.agentGuid, this.contactName, this.contactEmail, this.bugEmail, this.location, this.infoUrl, this.description);
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
            JAXBElement<SystemSupportPayload> jaxbElement = (new ObjectFactory()).createSystemSupportPayload(this);
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
     * @return SystemSupportPayload
     */
    public static SystemSupportPayload ToObject(String xmlString) {
        SystemSupportPayload object = null;

        try {
            Unmarshaller unmarshaller = JAXBContext.newInstance(SystemSupportPayload.class).createUnmarshaller();
            StreamSource streamSource = new StreamSource(new StringReader(xmlString));
            JAXBElement<SystemSupportPayload> jaxbElement = (JAXBElement<SystemSupportPayload>) unmarshaller.unmarshal(streamSource, SystemSupportPayload.class);
            object = jaxbElement.getValue();
        } catch (JAXBException ex) {
            System.out.println(ex.toString());
        }

        return object;
    }
}
