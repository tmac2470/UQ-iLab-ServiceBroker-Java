/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.database.types;

/**
 *
 * @author uqlpayne
 */
public class SystemSupportInfo {

    private String agentGuid;
    private String contactName;
    private String contactEmail;
    private String bugReportEmail;
    private String location;
    private String infoUrl;
    private String description;

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

    public String getBugReportEmail() {
        return bugReportEmail;
    }

    public void setBugReportEmail(String bugReportEmail) {
        this.bugReportEmail = bugReportEmail;
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

    public SystemSupportInfo() {
    }

    public SystemSupportInfo(String agentGuid, String contactName, String contactEmail, String bugReportEmail, String location, String infoUrl, String description) {
        this.agentGuid = agentGuid;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.bugReportEmail = bugReportEmail;
        this.location = location;
        this.infoUrl = infoUrl;
        this.description = description;
    }
}
