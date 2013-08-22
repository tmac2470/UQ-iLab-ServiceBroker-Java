/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.storage;

import java.util.Calendar;
import uq.ilabs.library.datatypes.batch.StatusCodes;

/**
 *
 * @author uqlpayne
 */
public class StorageStatus {

    protected long experimentId;
    protected StorageStatusCodes statusCode;
    protected StatusCodes batchStatusCode;
    protected int recordCount;
    protected String issuerGuid;
    protected Calendar dateCreated;
    protected Calendar dateModified;
    protected Calendar dateClosed;

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public StorageStatusCodes getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StorageStatusCodes statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCodes getBatchStatusCode() {
        return batchStatusCode;
    }

    public void setBatchStatusCode(StatusCodes batchStatusCode) {
        this.batchStatusCode = batchStatusCode;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getIssuerGuid() {
        return issuerGuid;
    }

    public void setIssuerGuid(String issuerGuid) {
        this.issuerGuid = issuerGuid;
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

    public StorageStatus() {
        this.experimentId = -1;
        this.statusCode = StorageStatusCodes.Unknown;
        this.batchStatusCode = StatusCodes.Unknown;
    }
}
