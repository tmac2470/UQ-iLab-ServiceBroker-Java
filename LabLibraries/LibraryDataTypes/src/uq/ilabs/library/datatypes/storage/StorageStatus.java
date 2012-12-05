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
public class StorageStatus {

    public long experimentId;
    public String issuerGuid;
    public StorageStatusCodes statusCode;
    public int recordCount;
    public Calendar dateCreated;
    public Calendar dateModified;
    public Calendar dateClosed;

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public String getIssuerGuid() {
        return issuerGuid;
    }

    public void setIssuerGuid(String issuerGuid) {
        this.issuerGuid = issuerGuid;
    }

    public StorageStatusCodes getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StorageStatusCodes statusCode) {
        this.statusCode = statusCode;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
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

    public Calendar getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Calendar dateClosed) {
        this.dateClosed = dateClosed;
    }
}
