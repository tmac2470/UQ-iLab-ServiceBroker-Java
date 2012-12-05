/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.storage;

/**
 *
 * @author uqlpayne
 */
public class Experiment {

    /**
     * A unique non-negative long that identifies this experiment on the issuing ServiceBroker.
     */
    private long experimentId;
    /**
     * The issuing ServiceBroker
     */
    private String issuerGuid;
    /**
     * The array of records that represents the experiment log
     */
    private ExperimentRecord[] records;

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

    public ExperimentRecord[] getRecords() {
        return records;
    }

    public void setRecords(ExperimentRecord[] records) {
        this.records = records;
    }
}
