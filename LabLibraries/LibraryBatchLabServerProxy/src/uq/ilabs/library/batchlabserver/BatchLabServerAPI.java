/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.batchlabserver;

import java.util.logging.Level;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.batchlabserver.AuthHeader;
import uq.ilabs.batchlabserver.BatchLabServerProxy;
import uq.ilabs.batchlabserver.BatchLabServerProxySoap;
import uq.ilabs.batchlabserver.ObjectFactory;
import uq.ilabs.library.datatypes.batch.ExperimentStatus;
import uq.ilabs.library.datatypes.batch.LabExperimentStatus;
import uq.ilabs.library.datatypes.batch.LabStatus;
import uq.ilabs.library.datatypes.batch.ResultReport;
import uq.ilabs.library.datatypes.batch.StatusCodes;
import uq.ilabs.library.datatypes.batch.SubmissionReport;
import uq.ilabs.library.datatypes.batch.ValidationReport;
import uq.ilabs.library.datatypes.batch.WaitEstimate;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class BatchLabServerAPI {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = BatchLabServerAPI.class.getName();
    private static final Level logLevel = Level.FINER;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ServiceUrl_arg = "ServiceUrl: %s";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "serviceUrl";
    private static final String STRERR_LabServerUnaccessible = "LabServer is unaccessible!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private BatchLabServerProxySoap batchLabServerProxy;
    private QName qnameAuthHeader;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String identifier;
    private String passkey;

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }
    //</editor-fold>

    /**
     *
     * @param serviceUrl
     * @throws Exception
     */
    public BatchLabServerAPI(String serviceUrl) throws Exception {
        final String methodName = "BatchLabServerAPI";
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
             * Create a proxy for the LabServer's web service and set the web service URL
             */
            BatchLabServerProxy webServiceClient = new BatchLabServerProxy();
            this.batchLabServerProxy = webServiceClient.getBatchLabServerProxySoap();
            ((BindingProvider) this.batchLabServerProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

            /*
             * Get authentication header QName
             */
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBElement<AuthHeader> jaxbElementAuthHeader = objectFactory.createAuthHeader(new AuthHeader());
            this.qnameAuthHeader = jaxbElementAuthHeader.getName();

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
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            cancelled = this.batchLabServerProxy.cancel(experimentId);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return cancelled;
    }

    /**
     *
     * @param userGroup
     * @param priorityHint
     * @return
     */
    public WaitEstimate GetEffectiveQueueLength(String userGroup, int priorityHint) {
        final String methodName = "GetEffectiveQueueLength";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        WaitEstimate waitEstimate = null;

        try {
            /*
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            uq.ilabs.batchlabserver.WaitEstimate proxyWaitEstimate = this.batchLabServerProxy.getEffectiveQueueLength(userGroup, priorityHint);
            waitEstimate = this.ConvertType(proxyWaitEstimate);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
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
            this.SetAuthHeader();
            uq.ilabs.batchlabserver.LabExperimentStatus proxyLabExperimentStatus = this.batchLabServerProxy.getExperimentStatus(experimentId);
            labExperimentStatus = this.ConvertType(proxyLabExperimentStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return labExperimentStatus;
    }

    /**
     *
     * @param userGroup
     * @return
     */
    public String GetLabConfiguration(String userGroup) {
        final String methodName = "GetLabConfiguration";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String labConfiguration = null;

        try {
            /*
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            labConfiguration = this.batchLabServerProxy.getLabConfiguration(userGroup);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
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
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            labInfo = this.batchLabServerProxy.getLabInfo();

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
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
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            uq.ilabs.batchlabserver.LabStatus proxyLabStatus = this.batchLabServerProxy.getLabStatus();
            labStatus = this.ConvertType(proxyLabStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
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
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            uq.ilabs.batchlabserver.ResultReport proxyResultReport = this.batchLabServerProxy.retrieveResult(experimentId);
            resultReport = this.ConvertType(proxyResultReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return resultReport;
    }

    /**
     *
     * @param experimentId
     * @param experimentSpecification
     * @param userGroup
     * @param priorityHint
     * @return
     */
    public SubmissionReport Submit(int experimentId, String experimentSpecification, String userGroup, int priorityHint) {
        final String methodName = "Submit";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        SubmissionReport submissionReport = null;

        try {
            /*
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            uq.ilabs.batchlabserver.SubmissionReport proxySubmissionReport = this.batchLabServerProxy.submit(experimentId, experimentSpecification, userGroup, priorityHint);
            submissionReport = this.ConvertType(proxySubmissionReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return submissionReport;
    }

    /**
     *
     * @param experimentSpecification
     * @param userGroup
     * @return
     */
    public ValidationReport Validate(String experimentSpecification, String userGroup) {
        final String methodName = "Validate";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ValidationReport validationReport = null;

        try {
            /*
             * Set the authentication information and call the web service
             */
            this.SetAuthHeader();
            uq.ilabs.batchlabserver.ValidationReport proxyValidationReport = this.batchLabServerProxy.validate(experimentSpecification, userGroup);
            validationReport = this.ConvertType(proxyValidationReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_LabServerUnaccessible);
        } finally {
            this.UnsetAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return validationReport;
    }

    //================================================================================================================//
    /**
     *
     */
    private void SetAuthHeader() {
        /*
         * Create authentication header
         */
        AuthHeader authHeader = new AuthHeader();
        authHeader.setIdentifier(this.identifier);
        authHeader.setPassKey(this.passkey);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        ((BindingProvider) this.batchLabServerProxy).getRequestContext().put(this.qnameAuthHeader.getLocalPart(), authHeader);
    }

    /**
     *
     */
    private void UnsetAuthHeader() {
        ((BindingProvider) this.batchLabServerProxy).getRequestContext().remove(this.qnameAuthHeader.getLocalPart());
    }

    //<editor-fold defaultstate="collapsed" desc="ConvertType">
    /**
     *
     * @param arrayOfString
     * @return String[]
     */
    private String[] ConvertType(uq.ilabs.batchlabserver.ArrayOfString arrayOfString) {
        String[] strings = null;

        if (arrayOfString != null) {
            strings = arrayOfString.getString().toArray(new String[0]);
        }

        return strings;
    }

    /**
     *
     * @param proxySubmissionReport
     * @return SubmissionReport
     */
    private SubmissionReport ConvertType(uq.ilabs.batchlabserver.SubmissionReport proxySubmissionReport) {
        SubmissionReport submissionReport = null;

        if (proxySubmissionReport != null) {
            submissionReport = new SubmissionReport();
            submissionReport.setExperimentId(proxySubmissionReport.getExperimentID());
            submissionReport.setMinTimeToLive(proxySubmissionReport.getMinTimetoLive());
            submissionReport.setValidationReport(this.ConvertType(proxySubmissionReport.getVReport()));
            submissionReport.setWaitEstimate(this.ConvertType(proxySubmissionReport.getWait()));
        }

        return submissionReport;
    }

    /**
     *
     * @param proxyExperimentStatus
     * @return ExperimentStatus
     */
    private ExperimentStatus ConvertType(uq.ilabs.batchlabserver.ExperimentStatus proxyExperimentStatus) {
        ExperimentStatus experimentStatus = null;

        if (proxyExperimentStatus != null) {
            experimentStatus = new ExperimentStatus();
            experimentStatus.setEstRemainingRuntime(proxyExperimentStatus.getEstRemainingRuntime());
            experimentStatus.setEstRuntime(proxyExperimentStatus.getEstRuntime());
            experimentStatus.setStatusCode(StatusCodes.ToStatusCode(proxyExperimentStatus.getStatusCode()));
            experimentStatus.setWaitEstimate(this.ConvertType(proxyExperimentStatus.getWait()));
        }

        return experimentStatus;
    }

    /**
     *
     * @param proxyLabExperimentStatus
     * @return LabExperimentStatus
     */
    private LabExperimentStatus ConvertType(uq.ilabs.batchlabserver.LabExperimentStatus proxyLabExperimentStatus) {
        LabExperimentStatus labExperimentStatus = null;

        if (proxyLabExperimentStatus != null) {
            labExperimentStatus = new LabExperimentStatus();
            labExperimentStatus.setMinTimetoLive(proxyLabExperimentStatus.getMinTimetoLive());
            labExperimentStatus.setExperimentStatus(this.ConvertType(proxyLabExperimentStatus.getStatusReport()));
        }

        return labExperimentStatus;
    }

    /**
     *
     * @param proxyLabStatus
     * @return LabStatus
     */
    private LabStatus ConvertType(uq.ilabs.batchlabserver.LabStatus proxyLabStatus) {
        LabStatus labStatus = null;

        if (proxyLabStatus != null) {
            labStatus = new LabStatus();
            labStatus.setOnline(proxyLabStatus.isOnline());
            labStatus.setLabStatusMessage(proxyLabStatus.getLabStatusMessage());
        }

        return labStatus;
    }

    /**
     *
     * @param proxyResultReport
     * @return ResultReport
     */
    private ResultReport ConvertType(uq.ilabs.batchlabserver.ResultReport proxyResultReport) {
        ResultReport resultReport = null;

        if (proxyResultReport != null) {
            resultReport = new ResultReport();
            resultReport.setErrorMessage(proxyResultReport.getErrorMessage());
            resultReport.setXmlExperimentResults(proxyResultReport.getExperimentResults());
            resultReport.setStatusCode(StatusCodes.ToStatusCode(proxyResultReport.getStatusCode()));
            resultReport.setXmlBlobExtension(proxyResultReport.getXmlBlobExtension());
            resultReport.setXmlResultExtension(proxyResultReport.getXmlResultExtension());
            resultReport.setWarningMessages(this.ConvertType(proxyResultReport.getWarningMessages()));
        }

        return resultReport;
    }

    /**
     *
     * @param proxyValidationReport
     * @return ValidationReport
     */
    private ValidationReport ConvertType(uq.ilabs.batchlabserver.ValidationReport proxyValidationReport) {
        ValidationReport validationReport = null;

        if (proxyValidationReport != null) {
            validationReport = new ValidationReport();
            validationReport.setAccepted(proxyValidationReport.isAccepted());
            validationReport.setErrorMessage(proxyValidationReport.getErrorMessage());
            validationReport.setEstRuntime(proxyValidationReport.getEstRuntime());
            validationReport.setWarningMessages(ConvertType(proxyValidationReport.getWarningMessages()));
        }

        return validationReport;
    }

    /**
     *
     * @param proxyWaitEstimate
     * @return WaitEstimate
     */
    private WaitEstimate ConvertType(uq.ilabs.batchlabserver.WaitEstimate proxyWaitEstimate) {
        WaitEstimate waitEstimate = null;

        if (proxyWaitEstimate != null) {
            waitEstimate = new WaitEstimate();
            waitEstimate.setEffectiveQueueLength(proxyWaitEstimate.getEffectiveQueueLength());
            waitEstimate.setEstWait(proxyWaitEstimate.getEstWait());
        }

        return waitEstimate;
    }
    //</editor-fold>
}
