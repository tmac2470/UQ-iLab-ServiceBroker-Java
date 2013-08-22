/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database.types;

import java.util.Calendar;
import uq.ilabs.library.servicebroker.engine.types.LabClientTypes;

/**
 *
 * @author uqlpayne
 */
public class LabClientInfo {

    private int id;
    private String name;
    private String guid;
    private LabClientTypes type;
    private String title;
    private String version;
    private String description;
    private String loaderScript;
    private int agentId;
    private int essId;
    private int ussId;
    private boolean reentrant;
    private String contactName;
    private String contactEmail;
    private String documentationUrl;
    private String notes;
    private Calendar dateCreated;
    private Calendar dateModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public LabClientTypes getType() {
        return type;
    }

    public void setType(LabClientTypes type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoaderScript() {
        return loaderScript;
    }

    public void setLoaderScript(String loaderScript) {
        this.loaderScript = loaderScript;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getEssId() {
        return essId;
    }

    public void setEssId(int essId) {
        this.essId = essId;
    }

    public int getUssId() {
        return ussId;
    }

    public void setUssId(int ussId) {
        this.ussId = ussId;
    }

    public boolean isReentrant() {
        return reentrant;
    }

    public void setReentrant(boolean reentrant) {
        this.reentrant = reentrant;
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

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Calendar getDateModified() {
        return dateModified;
    }

    public void setDateModified(Calendar dateModified) {
        this.dateModified = dateModified;
    }

    public LabClientInfo() {
        this.id = -1;
    }

    public LabClientInfo(String name, String guid, LabClientTypes type, String title, String version, String description, String loaderScript) {
        this();
        this.name = name;
        this.guid = guid;
        this.type = type;
        this.title = title;
        this.version = version;
        this.description = description;
        this.loaderScript = loaderScript;
    }
}
