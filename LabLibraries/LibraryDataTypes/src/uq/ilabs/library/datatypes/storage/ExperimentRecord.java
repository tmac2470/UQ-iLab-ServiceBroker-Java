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
public class ExperimentRecord {

    public static final String STR_TypeExperimentSpecification = "Experiment Specification";
    public static final String STR_TypeLabConfiguration = "Lab Configuration";
    public static final String STR_TypeExperimentResult = "Result";
    public static final String STR_TypeBlobExtension = "Blob Extension";
    public static final String STR_TypeResultExtension = "Result Extension";
    public static final String STR_TypeValidationWarningMessage = "Validation Warning Message";
    public static final String STR_TypeValidationErrorMessage = "Validation Error Message";
    public static final String STR_TypeExecutionWarningMessage = "Execution Warning Message";
    public static final String STR_TypeExecutionErrorMessage = "Execution Error Message";
    /**
     * A tag to identify the type of content represented by a record
     */
    private String recordType;
    /**
     * optional source of the record
     */
    private String submitter;
    /**
     * The ordinal number of this record (starting with 0)
     */
    private int sequenceNum;
    /**
     * A timestamp issued by the ESS when the record is added
     */
    private Calendar dateCreated;
    /**
     * True if the record's contents field is XML encoded and the contained attributes may be searched
     */
    private boolean xmlSearchable;
    /**
     * The payload of this record
     */
    private String contents;

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isXmlSearchable() {
        return xmlSearchable;
    }

    public void setXmlSearchable(boolean xmlSearchable) {
        this.xmlSearchable = xmlSearchable;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public ExperimentRecord() {
    }

    public ExperimentRecord(String recordType, String submitter, boolean xmlSearchable, String contents) {
        this.recordType = recordType;
        this.submitter = submitter;
        this.xmlSearchable = xmlSearchable;
        this.contents = contents;
    }
}
