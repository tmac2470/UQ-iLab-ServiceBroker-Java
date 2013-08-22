/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker;

import java.util.HashMap;
import java.util.logging.Level;
import uq.ilabs.library.batchlabserver.BatchLabServerAPI;
import uq.ilabs.library.datatypes.batch.ClientSubmissionReport;
import uq.ilabs.library.datatypes.batch.LabExperimentStatus;
import uq.ilabs.library.datatypes.batch.LabStatus;
import uq.ilabs.library.datatypes.batch.ResultReport;
import uq.ilabs.library.datatypes.batch.StatusCodes;
import uq.ilabs.library.datatypes.batch.SubmissionReport;
import uq.ilabs.library.datatypes.batch.ValidationReport;
import uq.ilabs.library.datatypes.batch.WaitEstimate;
import uq.ilabs.library.datatypes.service.SbAuthHeader;
import uq.ilabs.library.datatypes.storage.StorageStatusCodes;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.ticketing.RedeemSessionPayload;
import uq.ilabs.library.servicebroker.database.types.ExperimentInfo;
import uq.ilabs.library.servicebroker.engine.BatchServiceBroker;
import uq.ilabs.library.servicebroker.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
public class BatchServiceBrokerHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = BatchServiceBrokerHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LabServerGuid_arg = "LabServerGuid: %s";
    private static final String STRLOG_ExperimentId_arg = " ExperimentId: %d";
    private static final String STRLOG_Success_arg = " Success: %s";
    private static final String STRLOG_LabStatus_arg2 = "Online: %s  Message: %s";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_LabServerUnknown_arg = "LabServer Unknown: %s";
    private static final String STRERR_AccessDenied_arg = "IlabServiceBrokerService Access Denied: %s";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_Invalid_arg = "%s: Invalid!";
    private static final String STRERR_SbAuthHeader = "SbAuthHeader";
    private static final String STRERR_CouponID = "CouponID";
    private static final String STRERR_CouponPassKey = "CouponPassKey";
    private static final String STRERR_Ticket = "Ticket";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    private BatchServiceBroker batchServiceBroker;
    private HashMap<String, BatchLabServerAPI> mapBatchLabServerAPI;
    //</editor-fold>

    /**
     *
     * @param serviceManagement
     */
    public BatchServiceBrokerHandler(ServiceManagement serviceManagement) {
        final String methodName = "BatchServiceBrokerHandler";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.serviceManagement = serviceManagement;
        this.mapBatchLabServerAPI = new HashMap<>();

        try {
            /*
             * Create an instance of the BatchServiceBroker
             */
            this.batchServiceBroker = new BatchServiceBroker(this.serviceManagement);
            if (this.batchServiceBroker == null) {
                throw new NullPointerException(BatchServiceBroker.class.getSimpleName());
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param sbAuthHeader
     * @param experimentId
     * @return boolean
     */
    public boolean cancel(SbAuthHeader sbAuthHeader, int experimentId) {
        final String STR_MethodName = "cancel";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        boolean success = false;

        this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the LabServer Id for the specified experiment
             */
            ExperimentInfo experimentInfo = this.serviceManagement.getExperimentsDB().Retrieve(experimentId);
            if (experimentInfo != null) {
                /*
                 * Get the BatchLabServerAPI for the specified experiment Id
                 */
                ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getAgentId());
                BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(processAgentInfo.getAgentGuid());

                /*
                 * Pass to LabServer for processing
                 */
                success = batchLabServerAPI.Cancel(experimentId);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName,
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    /**
     *
     * @param sbAuthHeader
     * @param labServerGuid
     * @param priorityHint
     * @return WaitEstimate
     */
    public WaitEstimate getEffectiveQueueLength(SbAuthHeader sbAuthHeader, String labServerGuid, int priorityHint) {
        final String STR_MethodName = "getEffectiveQueueLength";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName);

        WaitEstimate waitEstimate = null;

        RedeemSessionPayload redeemSessionPayload = this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the BatchLabServerAPI for the specified LabServer Guid
             */
            BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(labServerGuid);

            waitEstimate = batchLabServerAPI.GetEffectiveQueueLength(redeemSessionPayload.getGroupName(), priorityHint);

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return waitEstimate;
    }

    /**
     *
     * @param sbAuthHeader
     * @param experimentId
     * @return LabExperimentStatus
     */
    public LabExperimentStatus getExperimentStatus(SbAuthHeader sbAuthHeader, int experimentId) {
        final String STR_MethodName = "getExperimentStatus";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        LabExperimentStatus labExperimentStatus = null;

        this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the LabServer Id for the specified experiment
             */
            ExperimentInfo experimentInfo = this.serviceManagement.getExperimentsDB().Retrieve(experimentId);
            if (experimentInfo != null) {
                /*
                 * Get the BatchLabServerAPI for the specified experiment Id
                 */
                ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getAgentId());
                BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(processAgentInfo.getAgentGuid());

                /*
                 * Pass to LabServer for processing
                 */
                labExperimentStatus = batchLabServerAPI.GetExperimentStatus(experimentId);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return labExperimentStatus;
    }

    /**
     *
     * @param sbAuthHeader
     * @param labServerGuid
     * @return String
     */
    public String getLabConfiguration(SbAuthHeader sbAuthHeader, String labServerGuid) {
        final String STR_MethodName = "getLabConfiguration";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName);

        String labConfiguration = null;

        RedeemSessionPayload redeemSessionPayload = this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the BatchLabServerAPI for the specified LabServer Guid
             */
            BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(labServerGuid);

            /*
             * Pass to LabServer for processing
             */
            labConfiguration = batchLabServerAPI.GetLabConfiguration(redeemSessionPayload.getGroupName());

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return labConfiguration;
    }

    /**
     *
     * @param sbAuthHeader
     * @param labServerGuid
     * @return String
     */
    public String getLabInfo(SbAuthHeader sbAuthHeader, String labServerGuid) {
        final String STR_MethodName = "getLabInfo";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName);

        String labInfo = null;

        this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the BatchLabServerAPI for the specified LabServer Guid
             */
            BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(labServerGuid);

            /*
             * Pass to LabServer for processing
             */
            labInfo = batchLabServerAPI.GetLabInfo();

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return labInfo;
    }

    /**
     *
     * @param sbAuthHeader
     * @param labServerGuid
     * @return LabStatus
     */
    public LabStatus getLabStatus(SbAuthHeader sbAuthHeader, String labServerGuid) {
        final String STR_MethodName = "getLabStatus";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName,
                String.format(STRLOG_LabServerGuid_arg, labServerGuid));

        LabStatus labStatus = null;

        this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the BatchLabServerAPI for the specified LabServer Guid
             */
            BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(labServerGuid);

            /*
             * Pass to LabServer for processing
             */
            labStatus = batchLabServerAPI.GetLabStatus();

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        String message = (labStatus != null)
                ? String.format(STRLOG_LabStatus_arg2, labStatus.isOnline(), labStatus.getLabStatusMessage()) : null;
        Logfile.WriteCompleted(STR_ClassName, STR_MethodName, message);

        return labStatus;
    }

    /**
     *
     * @param sbAuthHeader
     * @param experimentId
     * @return ResultReport
     */
    public ResultReport retrieveResult(SbAuthHeader sbAuthHeader, int experimentId) {
        final String STR_MethodName = "retrieveResult";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        ResultReport resultReport;

        this.Authenticate(sbAuthHeader);

        try {
            resultReport = this.RetrieveResult(experimentId);

        } catch (Exception ex) {
            resultReport = new ResultReport(StatusCodes.Unknown, ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return resultReport;
    }

    /**
     *
     * @param sbAuthHeader
     * @param labServerGuid
     * @param experimentSpecification
     * @param priorityHint
     * @param emailNotification
     * @return ClientSubmissionReport
     */
    public ClientSubmissionReport submit(SbAuthHeader sbAuthHeader, String labServerGuid, String experimentSpecification, int priorityHint, boolean emailNotification) {
        final String STR_MethodName = "submit";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName);

        ClientSubmissionReport clientSubmissionReport = null;

        RedeemSessionPayload redeemSessionPayload = this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the BatchLabServerAPI for the specified LabServer Guid
             */
            BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(labServerGuid);

            /*
             * Get LabConfiguration from LabServer
             */
            ProcessAgentInfo blsProcessAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveByGuid(labServerGuid);
            if (blsProcessAgentInfo == null) {
                throw new NullPointerException(ProcessAgentInfo.class.getSimpleName());
            }
            String labConfiguration = batchLabServerAPI.GetLabConfiguration(redeemSessionPayload.getGroupName());
            long experimentId = this.batchServiceBroker.CreateExperiment(blsProcessAgentInfo, experimentSpecification, labConfiguration, redeemSessionPayload);

            /*
             * Pass to LabServer for processing
             */
            SubmissionReport submissionReport = batchLabServerAPI.Submit((int) experimentId, experimentSpecification, redeemSessionPayload.getGroupName(), priorityHint);

            /*
             * Update experiment with submission results
             */
            if (submissionReport != null) {
                if (submissionReport.getValidationReport() != null) {
                    this.batchServiceBroker.UpdateExperiment(blsProcessAgentInfo, submissionReport);
                }
                clientSubmissionReport = new ClientSubmissionReport(submissionReport);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return clientSubmissionReport;
    }

    /**
     *
     * @param sbAuthHeader
     * @param labServerGuid
     * @param experimentSpecification
     * @return ValidationReport
     */
    public ValidationReport validate(SbAuthHeader sbAuthHeader, String labServerGuid, String experimentSpecification) {
        final String STR_MethodName = "validate";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName);

        ValidationReport validationReport = null;

        this.Authenticate(sbAuthHeader);

        try {
            /*
             * Get the BatchLabServerAPI for the specified LabServer Guid
             */
            BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(labServerGuid);

            /*
             * Pass to LabServer for processing
             */
            validationReport = batchLabServerAPI.Validate(experimentSpecification, labServerGuid);

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return validationReport;
    }

    /**
     *
     * @param sbAuthHeader
     * @param experimentId
     */
    public void notify(SbAuthHeader sbAuthHeader, int experimentId) {
        final String STR_MethodName = "notify";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        try {
            this.RetrieveResult(experimentId);

        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);
    }

    //<editor-fold defaultstate="collapsed" desc="Not implemented yet">
    public void saveClientItem(SbAuthHeader sbAuthHeader, String name, String itemValue) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String loadClientItem(SbAuthHeader sbAuthHeader, String name) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteClientItem(SbAuthHeader sbAuthHeader, String name) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ArrayOfString listAllClientItems(SbAuthHeader sbAuthHeader) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String retrieveSpecification(SbAuthHeader sbAuthHeader, int experimentID) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String retrieveExperimentResult(SbAuthHeader sbAuthHeader, int experimentID) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String retrieveLabConfiguration(SbAuthHeader sbAuthHeader, int experimentID) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String saveAnnotation(SbAuthHeader sbAuthHeader, int experimentID, String annotation) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String retrieveAnnotation(SbAuthHeader sbAuthHeader, int experimentID) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ArrayOfExperimentInformation getExperimentInformation(SbAuthHeader sbAuthHeader, edu.mit.ilab.ArrayOfInt experimentIDs) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    //</editor-fold>

    //================================================================================================================//
    /**
     *
     * @param sbAuthHeader
     * @return RedeemSessionPayload
     */
    private RedeemSessionPayload Authenticate(SbAuthHeader sbAuthHeader) {

        RedeemSessionPayload redeemSessionPayload = null;

        try {
            /*
             * Check that when authenticating the SbAuthHeader is specified
             */
            if (sbAuthHeader == null) {
                throw new NullPointerException(String.format(STRERR_NotSpecified_arg, STRERR_SbAuthHeader));
            }

            /*
             * Check that CouponID is valid
             */
            if (sbAuthHeader.getCouponId() <= 0) {
                throw new IllegalArgumentException(String.format(STRERR_Invalid_arg, STRERR_CouponID));
            }

            /*
             * Check that CouponPassKey is specified
             */
            if (sbAuthHeader.getCouponPasskey() == null) {
                throw new NullPointerException(String.format(STRERR_NotSpecified_arg, STRERR_CouponPassKey));
            }

            /*
             * Get the ticket for the specified coupon Id and check that it is valid
             */
            Ticket ticket = this.serviceManagement.getTicketIssuer().RetrieveTicket(
                    sbAuthHeader.getCouponId(), TicketTypes.RedeemSession, this.serviceManagement.getServiceGuid());
            if (ticket == null) {
                throw new NullPointerException(String.format(STRERR_Invalid_arg, STRERR_Ticket));
            }
            if (ticket.isCancelled() || ticket.isExpired()) {
                throw new RuntimeException(String.format(STRERR_Invalid_arg, STRERR_Ticket));
            }

            /*
             * Get the payload from the ticket
             */
            redeemSessionPayload = RedeemSessionPayload.ToObject(ticket.getPayload());

        } catch (Exception ex) {
            String message = String.format(STRERR_AccessDenied_arg, ex.getMessage());
            Logfile.WriteError(message);
            throw new RuntimeException(message);
        }

        return redeemSessionPayload;
    }

    /**
     *
     * @param labServerGuid
     * @return BatchLabServerAPI
     * @throws Exception
     */
    private synchronized BatchLabServerAPI GetBatchLabServerAPI(String labServerGuid) throws Exception {
        BatchLabServerAPI batchLabServerAPI;

        /*
         * Check if the BatchLabServerAPI for this labServerGuid already exists
         */
        if ((batchLabServerAPI = this.mapBatchLabServerAPI.get(labServerGuid)) == null) {
            /*
             * Get LabServer information
             */
            ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveByGuid(labServerGuid);
            if (processAgentInfo == null) {
                throw new RuntimeException(String.format(STRERR_LabServerUnknown_arg, labServerGuid));
            }

            /*
             * Create an instance of the BatchLabServerAPI for this LabServer
             */
            Coupon coupon = this.serviceManagement.getTicketIssuer().RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId());
            batchLabServerAPI = new BatchLabServerAPI(processAgentInfo.getServiceUrl());
            batchLabServerAPI.setAuthHeaderIdentifier(this.serviceManagement.getServiceGuid());
            batchLabServerAPI.setAuthHeaderPasskey(coupon.getPasskey());

            /*
             * Add the BatchLabServerAPI to the map for next time
             */
            this.mapBatchLabServerAPI.put(labServerGuid, batchLabServerAPI);
        }

        return batchLabServerAPI;
    }

    /**
     *
     * @param experimentId
     * @return ResultReport
     * @throws Exception
     */
    private synchronized ResultReport RetrieveResult(int experimentId) throws Exception {
        final String STR_MethodName = "retrieveResult";
        Logfile.WriteCalled(STR_ClassName, STR_MethodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        ResultReport resultReport;

        /*
         * Get the information for the specified experiment
         */
        ExperimentInfo experimentInfo = this.serviceManagement.getExperimentsDB().Retrieve(experimentId);
        if (experimentInfo == null) {
            throw new NullPointerException();
        }

        /*
         * Check the experiment status
         */
        if (experimentInfo.getStatusCode() == StorageStatusCodes.Closed) {
            /*
             * Retrieve the ResultReport from the ExperimentStorage
             */
            resultReport = this.batchServiceBroker.RetrieveExperiment(experimentId);
        } else {
            /*
             * Get the BatchLabServerAPI for the specified experiment Id
             */
            ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveById(experimentInfo.getAgentId());
            BatchLabServerAPI batchLabServerAPI = this.GetBatchLabServerAPI(processAgentInfo.getAgentGuid());

            /*
             * Get the experiment status
             */
            LabExperimentStatus labExperimentStatus = batchLabServerAPI.GetExperimentStatus(experimentId);
            StatusCodes statusCode = labExperimentStatus.getExperimentStatus().getStatusCode();
            switch (statusCode) {
                case Completed:
                case Failed:
                case Cancelled:
                    /*
                     * Retrieve the ResultReport from the LabServer
                     */
                    resultReport = batchLabServerAPI.RetrieveResult(experimentId);

                    /*
                     * Save the ResultReport to the ExperimentStorage and close the experiment
                     */
                    this.batchServiceBroker.CloseExperiment(experimentId, resultReport);
                    break;

                default:
                    resultReport = new ResultReport(statusCode);
                    break;
            }
        }

        Logfile.WriteCompleted(STR_ClassName, STR_MethodName);

        return resultReport;
    }
}
