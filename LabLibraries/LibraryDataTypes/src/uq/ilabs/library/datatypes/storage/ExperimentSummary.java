/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.storage;

import java.util.Calendar;

/**
 *
 * @author uqlpayne
 */
public class ExperimentSummary {

    private long experimentId;
    private long duration;
    private Calendar dateScheduledStart;
    private Calendar dateCreated;
    private Calendar dateClosed;
    private int status;
    private int recordCount;
    private String essGuid;
    private String serviceBrokerGuid;
    private String userName;
    private String groupName;
    private String labServerGuid;
    private String labServerName;
    private String clientName;
    private String clientVersion;
    private String annotation;

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Calendar getDateScheduledStart() {
        return dateScheduledStart;
    }

    public void setDateScheduledStart(Calendar dateScheduledStart) {
        this.dateScheduledStart = dateScheduledStart;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Calendar getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Calendar dateClosed) {
        this.dateClosed = dateClosed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getEssGuid() {
        return essGuid;
    }

    public void setEssGuid(String essGuid) {
        this.essGuid = essGuid;
    }

    public String getServiceBrokerGuid() {
        return serviceBrokerGuid;
    }

    public void setServiceBrokerGuid(String serviceBrokerGuid) {
        this.serviceBrokerGuid = serviceBrokerGuid;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
