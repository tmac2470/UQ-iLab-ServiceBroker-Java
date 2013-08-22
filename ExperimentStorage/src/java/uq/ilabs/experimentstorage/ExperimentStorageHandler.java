/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage;

import java.util.logging.Level;
import uq.ilabs.library.datatypes.Criterion;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.storage.Experiment;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.RecordAttribute;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.experimentstorage.engine.ExperimentStorage;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class ExperimentStorageHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentStorageHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ExperimentId_arg = "ExperimentId: %d";
    private static final String STRLOG_ExperimentIdDuration_arg2 = "ExperimentId: %d  Duration: %d";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ExperimentStorage experimentStorage;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private Authentication authentication;

    public Authentication getAuthentication() {
        return authentication;
    }
    //</editor-fold>

    /**
     *
     * @param experimentStorageBean
     */
    public ExperimentStorageHandler(ServiceManagement serviceManagement) {
        final String methodName = "ExperimentStorageHandler";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            this.experimentStorage = new ExperimentStorage(serviceManagement);
            this.authentication = new Authentication(serviceManagement);

        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param agentAuthHeader
     * @param experimentId
     * @return StorageStatus
     */
    public StorageStatus closeExperiment(AgentAuthHeader agentAuthHeader, long experimentId) {
        final String methodName = "closeExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        StorageStatus storageStatus = null;

        this.authentication.Authenticate(agentAuthHeader);

        try {
            /*
             * Open an experiment and get the storage status
             */
            String issuerGuid = agentAuthHeader.getCoupon().getIssuerGuid();
            storageStatus = this.experimentStorage.CloseExperiment(experimentId, issuerGuid);

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param agentAuthHeader
     * @param experimentId
     * @return boolean
     */
    public boolean deleteExperiment(AgentAuthHeader agentAuthHeader, long experimentId) {
        final String methodName = "deleteExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        boolean success = false;

        this.authentication.Authenticate(agentAuthHeader);

        try {
            Coupon coupon = agentAuthHeader.getCoupon();
            success = this.experimentStorage.DeleteExperiment(experimentId, coupon.getIssuerGuid());

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    /**
     *
     * @param agentAuthHeader
     * @param experimentId
     * @param duration
     * @return StorageStatus
     */
    public StorageStatus openExperiment(AgentAuthHeader agentAuthHeader, long experimentId, long duration) {
        final String methodName = "openExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdDuration_arg2, experimentId, duration));

        StorageStatus storageStatus = null;

        this.authentication.Authenticate(agentAuthHeader);

        try {
            /*
             * Open an experiment and get the storage status
             */
            String issuerGuid = agentAuthHeader.getCoupon().getIssuerGuid();
            if (this.experimentStorage.OpenExperiment(experimentId, issuerGuid, duration) > 0) {
                storageStatus = this.experimentStorage.GetExperimentStatus(experimentId, issuerGuid);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @return StorageStatus
     */
    public StorageStatus getExperimentStatus(OperationAuthHeader operationAuthHeader, long experimentId) {
        final String methodName = "getExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        StorageStatus storageStatus = null;

        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            storageStatus = this.experimentStorage.GetExperimentStatus(experimentId, coupon.getIssuerGuid());
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @param statusCode
     * @return StorageStatus
     */
    public StorageStatus setExperimentStatus(OperationAuthHeader operationAuthHeader, long experimentId, int statusCode) {
        final String methodName = "setExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        StorageStatus storageStatus = null;

        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.AdministerExperiment);

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            storageStatus = this.experimentStorage.SetExperimentStatus(experimentId, coupon.getIssuerGuid(), coupon.getCouponId(), statusCode);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @return int
     */
    public int getIdleTime(OperationAuthHeader operationAuthHeader, long experimentId) {

        int idleTime = -1;

        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        return idleTime;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @param submitter
     * @param type
     * @param xmlSearchable
     * @param contents
     * @param attributes
     * @return int
     */
    public int addRecord(OperationAuthHeader operationAuthHeader, long experimentId, String submitter, String type, boolean xmlSearchable, String contents, RecordAttribute[] recordAttributes) {
        final String methodName = "addRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        int sequenceNum = 0;

        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            sequenceNum = this.experimentStorage.AddRecord(experimentId, coupon.getIssuerGuid(), coupon.getCouponId(), submitter, type, xmlSearchable, contents, recordAttributes);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return sequenceNum;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @param records
     * @return int
     */
    public int addRecords(OperationAuthHeader operationAuthHeader, long experimentId, ExperimentRecord[] experimentRecords) {
        final String methodName = "addRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        int sequenceNum = 0;

        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            sequenceNum = this.experimentStorage.AddRecords(experimentId, coupon.getIssuerGuid(), coupon.getCouponId(), experimentRecords);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return sequenceNum;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @param target
     * @return ExperimentRecord[]
     */
    public ExperimentRecord[] getRecords(OperationAuthHeader operationAuthHeader, long experimentId, Criterion[] target) {
        final String methodName = "getRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        ExperimentRecord[] experimentRecords = null;

        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);

        try {
            Criterion[] criterions = null;
            experimentRecords = this.experimentStorage.GetRecords(experimentId, criterions);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return experimentRecords;
    }

    //<editor-fold defaultstate="collapsed" desc="Not implemented yet">
    public ExperimentRecord getRecord(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int[] getRecordIDs(OperationAuthHeader operationAuthHeader, long experimentId, Criterion[] target) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Experiment getExperiment(OperationAuthHeader operationAuthHeader, long experimentId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public long[] getExperimentIDs(AgentAuthHeader agentAuthHeader, long[] expSet, Criterion[] filter) {
        this.authentication.Authenticate(agentAuthHeader);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int[] addAttributes(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, RecordAttribute[] attributes) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public RecordAttribute[] getRecordAttributes(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, int[] attributeIds) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public RecordAttribute[] getRecordAttributesByName(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, String attributeName) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public RecordAttribute[] deleteRecordAttributes(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, int[] attributeIds) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.AdministerExperiment);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    //</editor-fold>
}
