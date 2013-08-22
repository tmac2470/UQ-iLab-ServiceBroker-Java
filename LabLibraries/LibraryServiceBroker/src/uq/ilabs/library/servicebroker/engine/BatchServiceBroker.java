/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.batch.ResultReport;
import uq.ilabs.library.datatypes.batch.SubmissionReport;
import uq.ilabs.library.datatypes.batch.ValidationReport;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.datatypes.storage.StorageStatusCodes;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.experimentstorage.ExperimentStorageAPI;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.ticketing.AdministerExperimentPayload;
import uq.ilabs.library.processagent.ticketing.RedeemSessionPayload;
import uq.ilabs.library.processagent.ticketing.RetrieveRecordsPayload;
import uq.ilabs.library.processagent.ticketing.StoreRecordsPayload;
import uq.ilabs.library.servicebroker.database.types.ExperimentInfo;
import uq.ilabs.library.servicebroker.database.types.LabClientInfo;
import uq.ilabs.library.servicebroker.ticketissuer.TicketIssuer;

/**
 *
 * @author uqlpayne
 */
public class BatchServiceBroker {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = BatchServiceBroker.class.getName();
    private static final Level logLevel = Level.CONFIG;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    private HashMap<String, ExperimentStorageAPI> mapExperimentStorageAPI;
    //</editor-fold>

    /**
     *
     * @param serviceManagement
     */
    public BatchServiceBroker(ServiceManagement serviceManagement) {
        final String methodName = "BatchServiceBroker";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check that all parameters are valid
         */
        if (serviceManagement == null) {
            throw new NullPointerException(ServiceManagement.class.getSimpleName());
        }

        /*
         * Save to local variables
         */
        this.serviceManagement = serviceManagement;

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param labServerInfo
     * @param experimentSpecification
     * @param labConfiguration
     * @param redeemSessionPayload
     * @return long
     */
    public long CreateExperiment(ProcessAgentInfo blsProcessAgentInfo, String experimentSpecification, String labConfiguration, RedeemSessionPayload redeemSessionPayload) throws Exception {
        final String methodName = "CreateExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long experimentId = -1;

        try {
            /*
             * Get the ESS ProcessAgentInfo for the specified LabClient
             */
            LabClientInfo labClientInfo = this.serviceManagement.getLabClientsDB().RetrieveById(redeemSessionPayload.getClientID());
            int essId = labClientInfo.getEssId();
            ProcessAgentInfo essProcessAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(essId);

            /*
             * Create a coupon for the experiment ticket collection
             */
            TicketIssuer ticketIssuer = this.serviceManagement.getTicketIssuer();
            Coupon issuedCoupon = ticketIssuer.CreateCoupon();

            /*
             * Create an experiment record and get the experiment Id
             */
            ExperimentInfo experimentInfo = new ExperimentInfo(issuedCoupon.getCouponId(), redeemSessionPayload.getUserID(), redeemSessionPayload.getGroupID(),
                    blsProcessAgentInfo.getAgentId(), redeemSessionPayload.getClientID(), essId);
            experimentInfo.setStatusCode(StorageStatusCodes.Initialised);
            experimentId = this.serviceManagement.getExperimentsDB().Add(experimentInfo);

            /*
             * Create a ticket collection for the experiment
             */
            long duration = 24 * 60 * 60;
            String essAgentGuid = essProcessAgentInfo.getAgentGuid();
            String essServiceUrl = essProcessAgentInfo.getServiceUrl();
            String serviceGuid = this.serviceManagement.getServiceGuid();

            /*
             * Add AdministerExperiment ticket
             */
            AdministerExperimentPayload administerExperimentPayload = new AdministerExperimentPayload(experimentId, essServiceUrl);
            ticketIssuer.AddTicket(issuedCoupon, TicketTypes.AdministerExperiment, serviceGuid, essAgentGuid, duration, administerExperimentPayload.ToXmlString());

            /*
             * Add StoreRecords ticket
             */
            StoreRecordsPayload storeRecordsPayload = new StoreRecordsPayload(true, experimentId, essServiceUrl);
            ticketIssuer.AddTicket(issuedCoupon, TicketTypes.StoreRecords, serviceGuid, essAgentGuid, duration, storeRecordsPayload.ToXmlString());

            /*
             * Add RetrieveRecords ticket
             */
            RetrieveRecordsPayload retrieveRecordsPayload = new RetrieveRecordsPayload(experimentId, essServiceUrl);
            ticketIssuer.AddTicket(issuedCoupon, TicketTypes.RetrieveRecords, serviceGuid, essAgentGuid, -1, retrieveRecordsPayload.ToXmlString());

            /*
             * Open experiment on the ESS
             */
            Coupon essOutCoupon = ticketIssuer.RetrieveCoupon(essProcessAgentInfo.getOutCoupon().getCouponId());
            ExperimentStorageAPI experimentStorageAPI = this.GetExperimentStorageAPI(essId);
            experimentStorageAPI.setAgentAuthHeaderAgentGuid(serviceGuid);
            experimentStorageAPI.setAgentAuthHeaderCoupon(essOutCoupon);
            experimentStorageAPI.setOperationAuthHeaderCoupon(issuedCoupon);
            StorageStatus storageStatus = experimentStorageAPI.OpenExperiment(experimentId, duration);
            if (storageStatus != null) {
                this.serviceManagement.getExperimentsDB().UpdateStatus(experimentId, storageStatus.getStatusCode(), storageStatus.getBatchStatusCode());
            }

            /*
             * Add ExperimentSpecification record to the ESS
             */
            int sequenceNumber = experimentStorageAPI.AddRecord(experimentId, serviceGuid,
                    ExperimentRecord.STR_TypeExperimentSpecification, true, experimentSpecification, null);

            /*
             * Add LabConfiguration record to the ESS
             */
            sequenceNumber = experimentStorageAPI.AddRecord(experimentId, blsProcessAgentInfo.getAgentGuid(),
                    ExperimentRecord.STR_TypeLabConfiguration, true, labConfiguration, null);

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return experimentId;
    }

    /**
     *
     * @param blsProcessAgentInfo
     * @param submissionReport
     * @return boolean
     */
    public boolean UpdateExperiment(ProcessAgentInfo blsProcessAgentInfo, SubmissionReport submissionReport) {
        final String methodName = "UpdateExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            ValidationReport validationReport = submissionReport.getValidationReport();
            if (validationReport == null) {
                throw new NullPointerException(ValidationReport.class.getSimpleName());
            }

            /*
             * Get the experiment information for the specified experiment
             */
            ExperimentInfo experimentInfo = this.serviceManagement.getExperimentsDB().Retrieve(submissionReport.getExperimentId());
            if (experimentInfo == null) {
                throw new NullPointerException(ExperimentInfo.class.getSimpleName());
            }
            String blsAgentGuid = blsProcessAgentInfo.getAgentGuid();

            /*
             * Create a list of records to save to the ExperimentStorage
             */
            ArrayList<ExperimentRecord> experimentRecordList = new ArrayList<>();
            String contents = validationReport.getErrorMessage();
            if (contents != null && contents.trim().length() > 0) {
                experimentRecordList.add(new ExperimentRecord(ExperimentRecord.STR_TypeValidationErrorMessage, blsAgentGuid, false, contents));
            }
            String[] warningMessages = validationReport.getWarningMessages();
            if (warningMessages != null && warningMessages.length > 0) {
                for (String message : warningMessages) {
                    if (message != null && message.trim().length() > 0) {
                        experimentRecordList.add(new ExperimentRecord(ExperimentRecord.STR_TypeValidationWarningMessage, blsAgentGuid, false, message));
                    }
                }
            }

            /*
             * Check that there are records to save to the ExperimentStorage
             */
            if (experimentRecordList.size() > 0) {
                /*
                 * Get the ESS ProcessAgentInfo for the specified experiment
                 */
                ProcessAgentInfo essProcessAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getEssId());

                TicketIssuer ticketIssuer = this.serviceManagement.getTicketIssuer();
                Coupon essOutCoupon = ticketIssuer.RetrieveCoupon(essProcessAgentInfo.getOutCoupon().getCouponId());
                Coupon issuedCoupon = ticketIssuer.RetrieveCoupon(experimentInfo.getCouponId());

                /*
                 * Get the ExperimentStorageAPI
                 */
                ExperimentStorageAPI experimentStorageAPI = this.GetExperimentStorageAPI(experimentInfo.getEssId());
                experimentStorageAPI.setAgentAuthHeaderAgentGuid(this.serviceManagement.getServiceGuid());
                experimentStorageAPI.setAgentAuthHeaderCoupon(essOutCoupon);
                experimentStorageAPI.setOperationAuthHeaderCoupon(issuedCoupon);

                /*
                 * Save the records to the ExperimentStorage
                 */
                ExperimentRecord[] experimentRecords = experimentRecordList.toArray(new ExperimentRecord[0]);
                experimentStorageAPI.AddRecords(submissionReport.getExperimentId(), experimentRecords);
            }

            success = true;

        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    /**
     *
     * @param experimentId
     * @return boolean
     */
    public boolean CancelExperiment(long experimentId) {
        final String methodName = "CancelExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            //
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
     * @param resultReport
     * @return boolean
     */
    public boolean CloseExperiment(long experimentId, ResultReport resultReport) {
        final String methodName = "CloseExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Get the experiment information for the specified experiment
             */
            ExperimentInfo experimentInfo = this.serviceManagement.getExperimentsDB().Retrieve(experimentId);
            if (experimentInfo == null) {
                throw new NullPointerException(ExperimentInfo.class.getSimpleName());
            }

            /*
             * Get the BLS ProcessAgentInfo for the specified experiment
             */
            ProcessAgentInfo blsProcessAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getAgentId());
            String blsAgentGuid = blsProcessAgentInfo.getAgentGuid();

            /*
             * Create a list of records to save to the ExperimentStorage
             */
            ArrayList<ExperimentRecord> experimentRecordList = new ArrayList<>();
            String contents = resultReport.getXmlExperimentResults();
            if (contents != null && contents.trim().length() > 0) {
                experimentRecordList.add(new ExperimentRecord(ExperimentRecord.STR_TypeExperimentResult, blsAgentGuid, false, contents));
            }
            contents = resultReport.getXmlResultExtension();
            if (contents != null && contents.trim().length() > 0) {
                experimentRecordList.add(new ExperimentRecord(ExperimentRecord.STR_TypeResultExtension, blsAgentGuid, true, contents));
            }
            contents = resultReport.getXmlBlobExtension();
            if (contents != null && contents.trim().length() > 0) {
                experimentRecordList.add(new ExperimentRecord(ExperimentRecord.STR_TypeBlobExtension, blsAgentGuid, true, contents));
            }
            contents = resultReport.getErrorMessage();
            if (contents != null && contents.trim().length() > 0) {
                experimentRecordList.add(new ExperimentRecord(ExperimentRecord.STR_TypeExecutionErrorMessage, blsAgentGuid, false, contents));
            }
            String[] warningMessages = resultReport.getWarningMessages();
            if (warningMessages != null && warningMessages.length > 0) {
                for (String message : warningMessages) {
                    if (message != null && message.trim().length() > 0) {
                        experimentRecordList.add(new ExperimentRecord(ExperimentRecord.STR_TypeExecutionWarningMessage, blsAgentGuid, false, message));
                    }
                }
            }

            /*
             * Get the ESS ProcessAgentInfo for the specified experiment
             */
            ProcessAgentInfo essProcessAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getEssId());

            TicketIssuer ticketIssuer = this.serviceManagement.getTicketIssuer();
            Coupon essOutCoupon = ticketIssuer.RetrieveCoupon(essProcessAgentInfo.getOutCoupon().getCouponId());
            Coupon issuedCoupon = ticketIssuer.RetrieveCoupon(experimentInfo.getCouponId());

            /*
             * Get the ExperimentStorageAPI
             */
            ExperimentStorageAPI experimentStorageAPI = this.GetExperimentStorageAPI(experimentInfo.getEssId());
            experimentStorageAPI.setAgentAuthHeaderAgentGuid(this.serviceManagement.getServiceGuid());
            experimentStorageAPI.setAgentAuthHeaderCoupon(essOutCoupon);
            experimentStorageAPI.setOperationAuthHeaderCoupon(issuedCoupon);

            /*
             * Check that there are records to save to the ExperimentStorage
             */
            if (experimentRecordList.size() > 0) {
                /*
                 * Save the records to the ExperimentStorage
                 */
                ExperimentRecord[] experimentRecords = experimentRecordList.toArray(new ExperimentRecord[0]);
                experimentStorageAPI.AddRecords(experimentId, experimentRecords);
            }

            /*
             * Close the experiment on the ExperimentStorage
             */
            StorageStatus storageStatus = experimentStorageAPI.CloseExperiment(experimentId);

            /*
             * Update the experiment status
             */
            this.serviceManagement.getExperimentsDB().UpdateStatusClose(experimentId, StorageStatusCodes.Closed, resultReport.getStatusCode(), storageStatus.getRecordCount());

            success = true;

        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    /**
     *
     * @param experimentId
     * @return ResultReport
     */
    public ResultReport RetrieveExperiment(long experimentId) {
        final String methodName = "RetrieveExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ResultReport resultReport = null;

        try {
            ExperimentInfo experimentInfo = this.serviceManagement.getExperimentsDB().Retrieve(experimentId);
            if (experimentInfo == null) {
                throw new RuntimeException();
            }
            resultReport = new ResultReport(experimentInfo.getBatchStatusCode());

            /*
             * Get the ESS ProcessAgentInfo for the specified experiment
             */
            ProcessAgentInfo essProcessAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getEssId());

            TicketIssuer ticketIssuer = this.serviceManagement.getTicketIssuer();
            Coupon essOutCoupon = ticketIssuer.RetrieveCoupon(essProcessAgentInfo.getOutCoupon().getCouponId());
            Coupon issuedCoupon = ticketIssuer.RetrieveCoupon(experimentInfo.getCouponId());

            /*
             * Get the ExperimentStorageAPI
             */
            ExperimentStorageAPI experimentStorageAPI = this.GetExperimentStorageAPI(experimentInfo.getEssId());
            experimentStorageAPI.setAgentAuthHeaderAgentGuid(this.serviceManagement.getServiceGuid());
            experimentStorageAPI.setAgentAuthHeaderCoupon(essOutCoupon);
            experimentStorageAPI.setOperationAuthHeaderCoupon(issuedCoupon);

            /*
             * Retrieve the records from the ExperimentStorage
             */
            ExperimentRecord[] experimentRecords = experimentStorageAPI.GetRecords(experimentId, null);
            if (experimentRecords != null && experimentRecords.length > 0) {
                ArrayList<String> warningMessages = new ArrayList<>();
                for (ExperimentRecord experimentRecord : experimentRecords) {
                    switch (experimentRecord.getRecordType()) {

                        case ExperimentRecord.STR_TypeExperimentResult:
                            resultReport.setXmlExperimentResults(experimentRecord.getContents());
                            break;

                        case ExperimentRecord.STR_TypeResultExtension:
                            resultReport.setXmlResultExtension(experimentRecord.getContents());
                            break;

                        case ExperimentRecord.STR_TypeBlobExtension:
                            resultReport.setXmlBlobExtension(experimentRecord.getContents());
                            break;

                        case ExperimentRecord.STR_TypeExecutionErrorMessage:
                            resultReport.setErrorMessage(experimentRecord.getContents());
                            break;

                        case ExperimentRecord.STR_TypeExecutionWarningMessage:
                            warningMessages.add(experimentRecord.getContents());
                            break;

                        case ExperimentRecord.STR_TypeValidationWarningMessage:
                            warningMessages.add(experimentRecord.getContents());
                            break;
                    }
                }
                if (warningMessages.size() > 0) {
                    resultReport.setWarningMessages(warningMessages.toArray(new String[warningMessages.size()]));
                }
            }

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return resultReport;
    }

    //================================================================================================================//
    /**
     *
     * @param essId
     * @return ExperimentStorageAPI
     * @throws Exception
     */
    private ExperimentStorageAPI GetExperimentStorageAPI(int essId) throws Exception {
        ExperimentStorageAPI experimentStorageAPI;

        /*
         * Check if the ExperimentStorageAPI map has been created
         */
        if (this.mapExperimentStorageAPI == null) {
            this.mapExperimentStorageAPI = new HashMap<>();
        }

        /*
         * Check if the ExperimentStorageAPI already exists
         */
        if ((experimentStorageAPI = this.mapExperimentStorageAPI.get(Integer.toString(essId))) == null) {
            /*
             * Get ProcessAgentInfo information
             */
            ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(essId);
            if (processAgentInfo != null) {
                /*
                 * Create an instance of the ExperimentStorageAPI
                 */
                Coupon coupon = this.serviceManagement.getTicketIssuer().RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId());
                experimentStorageAPI = new ExperimentStorageAPI(processAgentInfo.getServiceUrl());
                experimentStorageAPI.setAgentAuthHeaderAgentGuid(this.serviceManagement.getServiceGuid());
                experimentStorageAPI.setAgentAuthHeaderCoupon(coupon);
            }

            /*
             * Add the ExperimentStorageAPI to the map for next time
             */
            this.mapExperimentStorageAPI.put(Integer.toString(essId), experimentStorageAPI);
        }

        return experimentStorageAPI;
    }
}
