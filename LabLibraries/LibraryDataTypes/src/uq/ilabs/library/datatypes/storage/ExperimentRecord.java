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

    /**
     * A tag to identify the type of content represented by a record
     */
    private String type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public ExperimentRecord(String type, String submitter, int sequenceNum, Calendar dateCreated, boolean xmlSearchable, String contents) {
        this.type = type;
        this.submitter = submitter;
        this.sequenceNum = sequenceNum;
        this.dateCreated = dateCreated;
        this.xmlSearchable = xmlSearchable;
        this.contents = contents;
    }
}
