/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

import java.util.logging.Level;
import javax.ejb.Singleton;
import uq.ilabs.library.datatypes.Criterion;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.RecordAttribute;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.experimentstorage.engine.ExperimentStorage;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
@Singleton
public class ExperimentStorageBean {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentStorageBean.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ExperimentId_arg = "ExperimentId: %d";
    private static final String STRLOG_ExperimentIdDuration_arg2 = "ExperimentId: %d  Duration: %d";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    private ExperimentStorage experimentStorage;
    //</editor-fold>

    /**
     * Constructor - Seems that this gets called when the project is deployed which is unexpected. To get around this,
     * check to see if the service has been initialized and this class has been initialized. Can't do logging until the
     * service has been initialized and the logger created.
     */
    public ExperimentStorageBean() throws Exception {
        final String methodName = "ExperimentStorageBean";

        /*
         * Check if initialisation needs to be done
         */
        if (ExperimentStorageService.isInitialised() == true && this.serviceManagement == null) {
            Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

            try {
                /*
                 * Save to local variables
                 */
                this.serviceManagement = ExperimentStorageService.getServiceManagement();

                /*
                 * Create an instance of ExperimentStorage
                 */
                this.experimentStorage = new ExperimentStorage(this.serviceManagement);
                if (this.experimentStorage == null) {
                    throw new NullPointerException(ExperimentStorage.class.getSimpleName());
                }
            } catch (Exception ex) {
                Logfile.WriteError(ex.toString());
                throw ex;
            }

            Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
        }
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
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus openExperiment(AgentAuthHeader agentAuthHeader, long experimentId, long duration) {
        final String methodName = "openExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdDuration_arg2, experimentId, duration));

        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        try {
            /*
             * Open an experiment and get the storage status
             */
            String issuerGuid = agentAuthHeader.getCoupon().getIssuerGuid();
            if (this.experimentStorage.OpenExperiment(experimentId, issuerGuid, duration) > 0) {
                StorageStatus storageStatus = this.experimentStorage.GetExperimentStatus(experimentId, issuerGuid);
                proxyStorageStatus = ConvertTypes.Convert(storageStatus);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return proxyStorageStatus;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @return
     */
    public edu.mit.ilab.ilabs.type.StorageStatus getExperimentStatus(OperationAuthHeader operationAuthHeader, long experimentId) {
        final String methodName = "getExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            StorageStatus storageStatus = this.experimentStorage.GetExperimentStatus(experimentId, coupon.getIssuerGuid());
            proxyStorageStatus = ConvertTypes.Convert(storageStatus);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return proxyStorageStatus;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @param statusCode
     * @return
     */
    public edu.mit.ilab.ilabs.type.StorageStatus setExperimentStatus(OperationAuthHeader operationAuthHeader, long experimentId, int statusCode) {
        final String methodName = "setExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            StorageStatus storageStatus = this.experimentStorage.SetExperimentStatus(experimentId, coupon.getIssuerGuid(), coupon.getCouponId(), statusCode);
            proxyStorageStatus = ConvertTypes.Convert(storageStatus);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return proxyStorageStatus;
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @return int
     */
    public int getIdleTime(OperationAuthHeader operationAuthHeader, long experimentId) {

        int idleTime = -1;

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
     * @return
     */
    public int addRecord(OperationAuthHeader operationAuthHeader, long experimentId, String submitter, String type, boolean xmlSearchable, String contents,
            edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute attributes) {
        final String methodName = "addRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        int sequenceNum = 0;

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            RecordAttribute[] recordAttributes = ConvertTypes.Convert(attributes);
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
     * @return
     */
    public int addRecords(OperationAuthHeader operationAuthHeader, long experimentId, edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord records) {
        final String methodName = "addRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        int sequenceNum = 0;

        try {
            Coupon coupon = operationAuthHeader.getCoupon();
            ExperimentRecord[] experimentRecords = ConvertTypes.Convert(records);
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
     * @return
     */
    public edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord getRecords(OperationAuthHeader operationAuthHeader, long experimentId, edu.mit.ilab.ilabs.services.ArrayOfCriterion target) {
        final String methodName = "getRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord proxyExperimentRecords = null;

        try {
            Criterion[] criterions = null;
            ExperimentRecord[] experimentRecords = this.experimentStorage.GetRecords(experimentId, criterions);
            proxyExperimentRecords = ConvertTypes.Convert(experimentRecords);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return proxyExperimentRecords;
    }

    //<editor-fold defaultstate="collapsed" desc="Not implemented yet">
    /**
     *
     * @param agentAuthHeader
     * @param experimentId
     * @return
     */
    public edu.mit.ilab.ilabs.type.StorageStatus closeExperiment(AgentAuthHeader agentAuthHeader, long experimentId) {
        final String methodName = "closeExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.ExperimentRecord getRecord(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfInt getRecordIDs(OperationAuthHeader operationAuthHeader, long experimentId, edu.mit.ilab.ilabs.services.ArrayOfCriterion target) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.Experiment getExperiment(OperationAuthHeader operationAuthHeader, long experimentId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfLong getExperimentIDs(AgentAuthHeader agentAuthHeader, edu.mit.ilab.ilabs.services.ArrayOfLong expSet, edu.mit.ilab.ilabs.services.ArrayOfCriterion filter) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfInt addAttributes(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute attributes) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute getRecordAttributes(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfInt attributeIds) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute getRecordAttributesByName(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, String attributeName) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute deleteRecordAttributes(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfInt attributeIds) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    //</editor-fold>
}
