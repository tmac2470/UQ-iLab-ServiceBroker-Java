/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.batchservicebroker;

import edu.mit.ilab.ilabs.batchservicebroker.proxy.BatchServiceBrokerProxy;
import edu.mit.ilab.ilabs.batchservicebroker.proxy.BatchServiceBrokerProxySoap;
import java.util.logging.Level;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Map;
import uq.ilabs.library.datatypes.batch.ClientSubmissionReport;
import uq.ilabs.library.datatypes.batch.LabExperimentStatus;
import uq.ilabs.library.datatypes.batch.LabStatus;
import uq.ilabs.library.datatypes.batch.ResultReport;
import uq.ilabs.library.datatypes.batch.ValidationReport;
import uq.ilabs.library.datatypes.batch.WaitEstimate;
import uq.ilabs.library.datatypes.service.SbAuthHeader;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class BatchServiceBrokerAPI {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = BatchServiceBrokerAPI.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ServiceUrl_arg = "ServiceUrl: '%s'";
    private static final String STRLOG_ExperimentId_arg = "ExperimentId: %d";
    private static final String STRLOG_Success_arg = "Success: %s";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "serviceUrl";
    private static final String STRERR_ServiceBrokerUnaccessible = "ServiceBroker is unaccessible!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private BatchServiceBrokerProxySoap batchServiceBrokerProxy;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private long couponId;
    private String couponPasskey;
    private String labServerId;

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCouponPasskey() {
        return couponPasskey;
    }

    public void setCouponPasskey(String couponPasskey) {
        this.couponPasskey = couponPasskey;
    }

    public String getLabServerId() {
        return labServerId;
    }

    public void setLabServerId(String labServerId) {
        this.labServerId = labServerId;
    }
    //</editor-fold>

    /**
     *
     * @param serviceUrl
     * @throws Exception
     */
    public BatchServiceBrokerAPI(String serviceUrl) throws Exception {
        final String methodName = "BatchServiceBrokerAPI";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ServiceUrl_arg, serviceUrl));

        try {
            /*
             * Check that parameters are valid
             */
            if (serviceUrl == null) {
                throw new NullPointerException(STRERR_ServiceUrl);
            }
            serviceUrl = serviceUrl.trim();
            if (serviceUrl.isEmpty()) {
                throw new IllegalArgumentException(STRERR_ServiceUrl);
            }

            /*
             * Create a proxy for the web service and set the web service URL
             */
            BatchServiceBrokerProxy webServiceClient = new BatchServiceBrokerProxy();
            this.batchServiceBrokerProxy = webServiceClient.getBatchServiceBrokerProxySoap();
            ((BindingProvider) this.batchServiceBrokerProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        } catch (NullPointerException | IllegalArgumentException ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public boolean Cancel(int experimentId) {
        final String methodName = "Cancel";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean cancelled = false;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            cancelled = this.batchServiceBrokerProxy.cancel(experimentId);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return cancelled;
    }

    /**
     *
     * @return
     */
    public WaitEstimate GetEffectiveQueueLength() {
        return this.GetEffectiveQueueLength(0);
    }

    /**
     *
     * @param priorityHint
     * @return
     */
    public WaitEstimate GetEffectiveQueueLength(int priorityHint) {
        final String methodName = "GetEffectiveQueueLength";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        WaitEstimate waitEstimate = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            edu.mit.ilab.ilabs.batchservicebroker.proxy.WaitEstimate proxyWaitEstimate = this.batchServiceBrokerProxy.getEffectiveQueueLength(this.labServerId, priorityHint);
            waitEstimate = ConvertTypes.Convert(proxyWaitEstimate);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return waitEstimate;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public LabExperimentStatus GetExperimentStatus(int experimentId) {
        final String methodName = "GetExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        LabExperimentStatus labExperimentStatus = null;

        try {
            /*
             * Set the authentication information and call the web service
             */
            this.SetSbAuthHeader();
            edu.mit.ilab.ilabs.batchservicebroker.proxy.LabExperimentStatus proxyLabExperimentStatus = this.batchServiceBrokerProxy.getExperimentStatus(experimentId);
            labExperimentStatus = ConvertTypes.Convert(proxyLabExperimentStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return labExperimentStatus;
    }

    /**
     *
     * @return
     */
    public String GetLabConfiguration() {
        final String methodName = "GetLabConfiguration";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String labConfiguration = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            labConfiguration = this.batchServiceBrokerProxy.getLabConfiguration(this.labServerId);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return labConfiguration;
    }

    /**
     *
     * @return
     */
    public String GetLabInfo() {
        final String methodName = "GetLabInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String labInfo = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            labInfo = this.batchServiceBrokerProxy.getLabInfo(this.labServerId);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return labInfo;
    }

    /**
     *
     * @return
     */
    public LabStatus GetLabStatus() {
        final String methodName = "GetLabStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        LabStatus labStatus = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            edu.mit.ilab.ilabs.batchservicebroker.proxy.LabStatus proxyLabStatus = this.batchServiceBrokerProxy.getLabStatus(this.labServerId);
            labStatus = ConvertTypes.Convert(proxyLabStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return labStatus;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public ResultReport RetrieveResult(int experimentId) {
        final String methodName = "RetrieveResult";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ResultReport resultReport = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            edu.mit.ilab.ilabs.batchservicebroker.proxy.ResultReport proxyResultReport = this.batchServiceBrokerProxy.retrieveResult(experimentId);
            resultReport = ConvertTypes.Convert(proxyResultReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return resultReport;
    }

    /**
     *
     * @param experimentSpecification
     * @return ClientSubmissionReport
     */
    public ClientSubmissionReport Submit(String experimentSpecification) {
        return this.Submit(experimentSpecification, 0, false);
    }

    /**
     *
     * @param experimentSpecification
     * @param priorityHint
     * @param emailNotification
     * @return ClientSubmissionReport
     */
    public ClientSubmissionReport Submit(String experimentSpecification, int priorityHint, boolean emailNotification) {
        final String methodName = "Submit";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ClientSubmissionReport clientSubmissionReport = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            edu.mit.ilab.ilabs.batchservicebroker.proxy.ClientSubmissionReport proxyClientSubmissionReport = this.batchServiceBrokerProxy.submit(this.labServerId, experimentSpecification, priorityHint, emailNotification);
            clientSubmissionReport = ConvertTypes.Convert(proxyClientSubmissionReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return clientSubmissionReport;
    }

    /**
     *
     * @param experimentSpecification
     * @return ValidationReport
     */
    public ValidationReport Validate(String experimentSpecification) {
        final String methodName = "Validate";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ValidationReport validationReport = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetSbAuthHeader();
            edu.mit.ilab.ilabs.batchservicebroker.proxy.ValidationReport proxyValidationReport = this.batchServiceBrokerProxy.validate(this.labServerId, experimentSpecification);
            validationReport = ConvertTypes.Convert(proxyValidationReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        } finally {
            this.UnsetSbAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return validationReport;
    }

    /**
     *
     * @param experimentID
     */
    public boolean Notify(int experimentId) {
        final String methodName = "Notify";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        boolean success = false;

        try {
            this.batchServiceBrokerProxy.notify(experimentId);
            success = true;

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ServiceBrokerUnaccessible);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_Success_arg, success));

        return success;
    }

    //================================================================================================================//
    /**
     *
     */
    private void SetSbAuthHeader() {
        /*
         * Create authentication header
         */
        SbAuthHeader sbAuthHeader = new SbAuthHeader();
        sbAuthHeader.setCouponId(this.couponId);
        sbAuthHeader.setCouponPasskey(this.couponPasskey);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        Map<String, Object> requestContext = ((BindingProvider) this.batchServiceBrokerProxy).getRequestContext();
        requestContext.put(SbAuthHeader.class.getSimpleName(), sbAuthHeader);
    }

    /**
     *
     */
    private void UnsetSbAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.batchServiceBrokerProxy).getRequestContext();
        requestContext.remove(SbAuthHeader.class.getSimpleName());
    }
}
