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
public class Blob {

    /**
     * The unique ID of this BLOB
     */
    private long blobId;
    /**
     * The ID of the associated Experiment. A BLOB must always be associated with an Experiment whether or not it is
     * associated with an ExperimentRecord
     */
    private long experimentId;
    /**
     * The sequence number of the associated Experiment record. A BLOB may be associated with an ExperimentRecord
     */
    private int recordNumber;
    /**
     * A timestamp issued by the ESS when the BLOB is created. It is independent of the time when the actual download of
     * the BLOB data is requested or completed.
     */
    private Calendar dateCreated;
    /**
     * A brief description suitable for listing in a table.
     */
    private String description;
    /**
     * Optional mime type
     */
    private String mimeType;
    /**
     * The length of the actual binary data in bytes.
     */
    private int byteCount;
    /**
     * The string result of the checksum or hashing algorithm.
     */
    private String checksum;
    /**
     * A string designation of a supported checksum algorithm. Implementations must support CRC32 at a minimum, but may
     * also support other hashing or checksum schemes e.g., MD5
     */
    public String checksumAlgorithm;

    public long getBlobId() {
        return blobId;
    }

    public void setBlobId(long blobId) {
        this.blobId = blobId;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getByteCount() {
        return byteCount;
    }

    public void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    public Blob() {
        this.recordNumber = -1;
    }

    public boolean associated() {
        return recordNumber >= 0;
    }
}
