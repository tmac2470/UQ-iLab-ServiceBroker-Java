/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage.engine;

import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.Criterion;
import uq.ilabs.library.datatypes.batch.StatusCodes;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.RecordAttribute;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.datatypes.storage.StorageStatusCodes;
import uq.ilabs.library.experimentstorage.database.ExperimentRecordsDB;
import uq.ilabs.library.experimentstorage.database.ExperimentsDB;
import uq.ilabs.library.experimentstorage.database.types.ExperimentInfo;
import uq.ilabs.library.experimentstorage.database.types.ExperimentRecordInfo;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.Ticketing;

/**
 *
 * @author uqlpayne
 */
public class ExperimentStorage {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentStorage.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ExperimentId_arg = "ExperimentId: %d";
    private static final String STRLOG_ExperimentIdRecords_arg2 = STRLOG_ExperimentId_arg + "  Records: %s";
    private static final String STRLOG_ExperimentIdIssuerGuid_arg2 = STRLOG_ExperimentId_arg + "  IssuerGuid: %s";
    private static final String STRLOG_ExperimentIdIssuerGuidDuration_arg3 = STRLOG_ExperimentIdIssuerGuid_arg2 + "  Duration: %d";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ExperimentsDB experimentsDB;
    private ExperimentRecordsDB experimentRecordsDB;
    private Ticketing ticketing;
    //</editor-fold>

    public ExperimentStorage(ServiceManagement serviceManagement) throws Exception {
        final String methodName = "ExperimentStorage";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check that all parameters are valid
         */
        if (serviceManagement == null) {
            throw new NullPointerException(ServiceManagement.class.getSimpleName());
        }

        /*
         * Create local class instances
         */
        this.experimentsDB = serviceManagement.getExperimentsDB();
        this.experimentRecordsDB = serviceManagement.getExperimentRecordsDB();
        this.ticketing = serviceManagement.getTicketing();

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param experimentId
     * @param issuerGuid
     * @param duration
     * @return long - Id of the experiment in the database
     */
    public long OpenExperiment(long experimentId, String issuerGuid, long duration) {
        final String methodName = "OpenExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdIssuerGuidDuration_arg3, experimentId, issuerGuid, duration));

        long entryId;

        StorageStatus storageStatus = this.GetExperimentStatus(experimentId, issuerGuid);
        if (storageStatus == null) {
            /*
             * Experiment doesn't exist, create the experiment and open
             */
            entryId = this.CreateExperiment(experimentId, issuerGuid, duration, StorageStatusCodes.Open);
        } else {
            /*
             * Experiment already exists, update the experiment scheduled close time and reopen
             */
            Calendar dateScheduledClose = Calendar.getInstance();
            dateScheduledClose.setTimeInMillis(dateScheduledClose.getTimeInMillis() + duration * 1000);
            entryId = this.UpdateExperiment(experimentId, issuerGuid, StorageStatusCodes.Reopened, dateScheduledClose, null);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return entryId;
    }

    /**
     *
     * @param duration
     * @param sbExperimentId
     * @param sbGuid
     * @param status
     * @return
     */
    public long CreateExperiment(long experimentId, String issuerGuid, long duration, StorageStatusCodes statusCode) {
        final String methodName = "CreateExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdIssuerGuidDuration_arg3, experimentId, issuerGuid, duration));

        long entryId = -1;

        ExperimentInfo experimentInfo = new ExperimentInfo();
        experimentInfo.setExperimentId(experimentId);
        experimentInfo.setIssuerGuid(issuerGuid);
        experimentInfo.setStatusCode(statusCode);
        experimentInfo.setBatchStatusCode(StatusCodes.Ready);

        Calendar scheduledClose = Calendar.getInstance();
        scheduledClose.add(Calendar.SECOND, (int) duration);
        experimentInfo.setDateScheduledClose(scheduledClose);

        try {
            entryId = this.experimentsDB.Add(experimentInfo);
        } catch (Exception ex) {
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return entryId;
    }

    /**
     *
     * @param sbExperimentId
     * @param sbGuid
     * @param statusCode
     * @param dateScheduledClose
     * @param dateClosed
     * @return
     */
    public int UpdateExperiment(long experimentId, String issuerGuid, StorageStatusCodes statusCode, Calendar dateScheduledClose, Calendar dateClosed) {
        return 0;
    }

    /**
     *
     * @param experimentId
     * @param guid
     * @return
     */
    public StorageStatus CloseExperiment(long experimentId, String issuerGuid) {
        final String methodName = "GetExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdIssuerGuid_arg2, experimentId, issuerGuid));

        StorageStatus storageStatus = null;

        /*
         * Retrieve ExperimentInfo for the specified experimentId and issuerGuid
         */
        ExperimentInfo experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
        if (experimentInfo != null) {
            /*
             * Set the status code and update ExperimentInfo
             */
            experimentInfo.setStatusCode(StorageStatusCodes.Closed);
            this.experimentsDB.UpdateStatus(experimentInfo);

            /*
             * Retrieve ExperimentInfo for the specified experimentId and issuerGuid
             */
            experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
            if (experimentInfo != null) {
                storageStatus = experimentInfo.GetStorageStatus();
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param experimentId
     * @return boolean
     */
    public boolean DeleteExperiment(long experimentId, String issuerGuid) {
        final String methodName = "DeleteExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        boolean success = false;

        try {
            /*
             * Delete the experiment records
             */
            ExperimentRecordInfo[] experimentRecordInfos;
            experimentRecordInfos = this.experimentRecordsDB.Retrieve(experimentId);
            if (experimentRecordInfos != null) {
                for (ExperimentRecordInfo experimentRecordInfo : experimentRecordInfos) {
                    if (this.experimentRecordsDB.Delete(experimentRecordInfo.getId()) == false) {
                        throw new RuntimeException();
                    }
                }
            }

            /*
             * Retrieve ExperimentInfo for the specified experimentId and issuerGuid
             */
            ExperimentInfo experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
            if (experimentInfo != null) {
                /*
                 * Get the coupon Id for this experiment
                 */
                long couponId = experimentInfo.getCouponId();
                if (couponId > 0) {
                    /*
                     * Delete the coupon tickets and the coupon
                     */
                    this.ticketing.DeleteTickets(couponId);
                    if (this.ticketing.DeleteCoupon(couponId, issuerGuid) == false) {
                        throw new RuntimeException();
                    }
                }

                /*
                 * Delete the experiment
                 */
                if (this.experimentsDB.Delete(experimentId, issuerGuid) == false) {
                    throw new RuntimeException();
                }
            }

            success = true;

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    /**
     *
     * @param experimentId
     * @param issuerGuid
     * @return StorageStatus
     */
    public StorageStatus GetExperimentStatus(long experimentId, String issuerGuid) {
        final String methodName = "GetExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdIssuerGuid_arg2, experimentId, issuerGuid));

        StorageStatus storageStatus = null;

        /*
         * Retrieve ExperimentInfo for the specified experimentId and issuerGuid
         */
        ExperimentInfo experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
        if (experimentInfo != null) {
            storageStatus = experimentInfo.GetStorageStatus();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param experimentId
     * @param issuerGuid
     * @param storageStatusCode
     * @return StorageStatus
     */
    public StorageStatus SetExperimentStatus(long experimentId, String issuerGuid, long couponId, int statusCode) {
        final String methodName = "SetExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdIssuerGuid_arg2, experimentId, issuerGuid));

        StorageStatus storageStatus = null;

        /*
         * Retrieve ExperimentInfo for the specified experimentId and issuerGuid
         */
        ExperimentInfo experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
        if (experimentInfo != null) {
            /*
             * Set batch status code
             */
            experimentInfo.setBatchStatusCode(StatusCodes.ToStatusCode(statusCode));

            /*
             * Check if closing experiment
             */
            StorageStatusCodes storageStatusCode = StorageStatusCodes.ToStorageStatusCode(statusCode);
            switch (storageStatusCode) {

                case Closed:
                case ClosedTimeout:
                case ClosedUserAction:
                case ClosedError:
                    /*
                     * Close experiment if not already closed
                     */
                    if (experimentInfo.getStatusCode() != StorageStatusCodes.Closed) {
                        experimentInfo.setStatusCode(StorageStatusCodes.Closed);
                        this.experimentsDB.UpdateStatus(experimentInfo);
                    }
                    break;

                default:
                    /*
                     * Set the status code and update ExperimentInfo
                     */
                    experimentInfo.setStatusCode(storageStatusCode);
                    this.experimentsDB.Update(experimentInfo);
                    break;
            }

            /*
             * Check if coupon Id has been updated
             */
            if (experimentInfo.getCouponId() <= 0) {
                experimentInfo.setCouponId(couponId);
                this.experimentsDB.UpdateCouponId(experimentInfo);
            }

            /*
             * Retrieve ExperimentInfo for the specified experimentId and issuerGuid
             */
            experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
            if (experimentInfo != null) {
                storageStatus = experimentInfo.GetStorageStatus();
            }
        } else {
            /*
             * Experiment does not exist
             */
            storageStatus = new StorageStatus();
            storageStatus.setExperimentId(experimentId);
            storageStatus.setIssuerGuid(issuerGuid);
            storageStatus.setBatchStatusCode(StatusCodes.Ready);
            storageStatus.setStatusCode(StorageStatusCodes.Unknown);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param experimentId
     * @param submitter
     * @param type
     * @param xmlSearchable
     * @param contents
     * @param recordAttributes
     * @return int Sequence number of the record
     */
    public int AddRecord(long experimentId, String issuerGuid, long couponId, String submitter, String recordType, boolean xmlSearchable, String contents, RecordAttribute[] recordAttributes) {
        final String methodName = "AddRecord";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        int sequenceNum = -1;

        try {
            ExperimentRecord experimentRecord = new ExperimentRecord(recordType, submitter, xmlSearchable, contents);
            ExperimentRecordInfo experimentRecordInfo = new ExperimentRecordInfo(experimentId, experimentRecord);
            long id = this.experimentRecordsDB.Add(experimentRecordInfo);
            if (id > 0) {
                experimentRecordInfo = this.experimentRecordsDB.RetrieveById(id);
                if (experimentRecordInfo != null) {
                    sequenceNum = experimentRecordInfo.getSequenceNum();
                }
            }

            /*
             * Update sequence number in ExperimentInfo
             */
            if (sequenceNum > 0) {
                ExperimentInfo experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
                if (experimentInfo != null) {
                    experimentInfo.setSequenceNo(sequenceNum);
                    this.experimentsDB.Update(experimentInfo);

                    /*
                     * Check if coupon Id has been updated
                     */
                    if (experimentInfo.getCouponId() <= 0) {
                        experimentInfo.setCouponId(couponId);
                        this.experimentsDB.UpdateCouponId(experimentInfo);
                    }
                }
            }
        } catch (Exception ex) {
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return sequenceNum;
    }

    /**
     *
     * @param experimentId
     * @param experimentRecords
     * @return int
     */
    public int AddRecords(long experimentId, String issuerGuid, long couponId, ExperimentRecord[] experimentRecords) {
        final String methodName = "AddRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentIdRecords_arg2, experimentId, experimentRecords.length));

        int sequenceNum = -1;

        try {
            for (ExperimentRecord experimentRecord : experimentRecords) {
                long id = this.experimentRecordsDB.Add(new ExperimentRecordInfo(experimentId, experimentRecord));
                if (id > 0) {
                    ExperimentRecordInfo experimentRecordInfo = this.experimentRecordsDB.RetrieveById(id);
                    if (experimentRecordInfo != null) {
                        sequenceNum = experimentRecordInfo.getSequenceNum();
                    }
                }
            }

            /*
             * Update sequence number in ExperimentInfo
             */
            if (sequenceNum > 0) {
                ExperimentInfo experimentInfo = this.experimentsDB.Retrieve(experimentId, issuerGuid);
                if (experimentInfo != null) {
                    experimentInfo.setSequenceNo(sequenceNum);
                    this.experimentsDB.Update(experimentInfo);

                    /*
                     * Check if coupon Id has been updated
                     */
                    if (experimentInfo.getCouponId() <= 0) {
                        experimentInfo.setCouponId(couponId);
                        this.experimentsDB.UpdateCouponId(experimentInfo);
                    }
                }
            }
        } catch (Exception ex) {
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return sequenceNum;
    }

    /**
     *
     * @param experimentId
     * @param issuerGuid
     * @param criterions
     * @return ExperimentRecord[]
     */
    public ExperimentRecord[] GetRecords(long experimentId, Criterion[] criterions) {
        final String methodName = "GetRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        ExperimentRecord[] experimentRecords;

        experimentRecords = this.experimentRecordsDB.Retrieve(experimentId);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return experimentRecords;
    }
}
