/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage.database.types;

import uq.ilabs.library.datatypes.storage.ExperimentRecord;

/**
 *
 * @author uqlpayne
 */
public class ExperimentRecordInfo extends ExperimentRecord {

    private long id;
    private long experimentId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public ExperimentRecordInfo() {
        this.id = -1;
    }

    public ExperimentRecordInfo(long experimentId, ExperimentRecord experimentRecord) {
        super(experimentRecord.getRecordType(), experimentRecord.getSubmitter(), experimentRecord.isXmlSearchable(), experimentRecord.getContents());
        this.id = -1;
        this.experimentId = experimentId;
    }
}
