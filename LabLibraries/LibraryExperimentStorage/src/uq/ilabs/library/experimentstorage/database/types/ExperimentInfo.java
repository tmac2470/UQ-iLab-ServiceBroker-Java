/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage.database.types;

import java.util.Calendar;
import uq.ilabs.library.datatypes.storage.StorageStatus;

/**
 *
 * @author uqlpayne
 */
public class ExperimentInfo extends StorageStatus {

    private long id;
    private int sequenceNo;
    private long couponId;
    private Calendar dateScheduledClose;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public Calendar getDateScheduledClose() {
        return dateScheduledClose;
    }

    public void setDateScheduledClose(Calendar dateScheduledClose) {
        this.dateScheduledClose = dateScheduledClose;
    }

    /**
     *
     */
    public ExperimentInfo() {
        this.id = -1;
    }

    /**
     *
     * @return StorageStatus
     */
    public StorageStatus GetStorageStatus() {
        StorageStatus storageStatus = new StorageStatus();
        storageStatus.setExperimentId(this.experimentId);
        storageStatus.setIssuerGuid(this.issuerGuid);
        storageStatus.setStatusCode(this.statusCode);
        storageStatus.setBatchStatusCode(this.batchStatusCode);
        storageStatus.setRecordCount(this.sequenceNo);
        storageStatus.setDateCreated(this.dateCreated);
        storageStatus.setDateModified(this.dateModified);
        storageStatus.setDateClosed(this.dateClosed);

        return storageStatus;
    }
}
