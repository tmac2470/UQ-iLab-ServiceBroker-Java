/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent.proxy;

import edu.mit.ilab.ilabs.processagent.proxy.ProcessAgentProxy;
import edu.mit.ilab.ilabs.processagent.proxy.ProcessAgentProxySoap;
import java.util.Calendar;
import java.util.logging.Level;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import java.util.Map;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;

/**
 *
 * @author uqlpayne
 */
public class ProcessAgentAPI {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ProcessAgentAPI.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants
     */
    public static final String STR_Online = "Online";
    public static final String STR_Offline = "Offline";
    public static final String STR_ServiceStatus_arg2 = "Service Status: %s - %s";
    public static final String STR_Version_arg = "Version: %s";
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ServiceUrl_arg = "ServiceUrl: '%s'";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "serviceUrl";
    public static final String STRERR_ProcessAgentUnaccessible = "ProcessAgent is unaccessible!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ProcessAgentProxySoap processAgentProxy;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String authHeaderInitPasskey;
    private String authHeaderAgentGuid;
    private Coupon authHeaderCoupon;

    public void setAuthHeaderInitPasskey(String authHeaderInitPasskey) {
        this.authHeaderInitPasskey = authHeaderInitPasskey;
    }

    public void setAuthHeaderAgentGuid(String authHeaderAgentGuid) {
        this.authHeaderAgentGuid = authHeaderAgentGuid;
    }

    public void setAuthHeaderCoupon(Coupon authHeaderCoupon) {
        this.authHeaderCoupon = authHeaderCoupon;
    }
    //</editor-fold>

    /**
     *
     * @param serviceUrl
     * @throws Exception
     */
    public ProcessAgentAPI(String serviceUrl) throws Exception {
        final String methodName = "ProcessAgentAPI";
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
            ProcessAgentProxy webServiceClient = new ProcessAgentProxy();
            this.processAgentProxy = webServiceClient.getProcessAgentProxySoap();
            ((BindingProvider) this.processAgentProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        } catch (NullPointerException | IllegalArgumentException ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param coupon
     * @param type
     * @param redeemer
     * @return boolean
     */
    public boolean CancelTicket(Coupon coupon, String type, String redeemer) {
        final String methodName = "CancelTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            success = this.processAgentProxy.cancelTicket(ConvertTypes.Convert(coupon), type, redeemer);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    /**
     *
     * @return Calendar
     */
    public Calendar GetServiceTime() {
        final String methodName = "GetServiceTime";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Calendar calendar = null;

        try {
            /*
             * Set no authentication information and call the web service
             */
            XMLGregorianCalendar xmlGregorianCalendar = this.processAgentProxy.getServiceTime();
            calendar = ConvertTypes.Convert(xmlGregorianCalendar);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return calendar;
    }

    /**
     *
     * @return StatusReport
     */
    public StatusReport GetStatus() {
        final String methodName = "GetStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        StatusReport statusReport = null;

        try {
            /*
             * Set no authentication information and call the web service
             */
            edu.mit.ilab.ilabs.processagent.proxy.StatusReport proxyStatusReport = this.processAgentProxy.getStatus();
            statusReport = ConvertTypes.Convert(proxyStatusReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return statusReport;
    }

    /**
     *
     * @param serviceProcessAgent
     * @param inCoupon
     * @param outCoupon
     * @return ProcessAgent
     */
    public ProcessAgent InstallDomainCredentials(ProcessAgent agent, Coupon inCoupon, Coupon outCoupon) {
        final String methodName = "InstallDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ProcessAgent processAgent = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetInitAuthHeader();
            edu.mit.ilab.ilabs.processagent.proxy.ProcessAgent proxyProcessAgent = this.processAgentProxy.installDomainCredentials(
                    ConvertTypes.Convert(agent), ConvertTypes.Convert(inCoupon), ConvertTypes.Convert(outCoupon));
            processAgent = ConvertTypes.Convert(proxyProcessAgent);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetInitAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return processAgent;
    }

    /**
     *
     * @param originalGuid
     * @param agent
     * @param extra
     * @param inCoupon
     * @param outCoupon
     * @return int
     */
    public int ModifyDomainCredentials(String originalGuid, ProcessAgent agent, String xmlSystemSupport, Coupon inCoupon, Coupon outCoupon) {
        final String methodName = "ModifyDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.processAgentProxy.modifyDomainCredentials(
                    originalGuid, ConvertTypes.Convert(agent), xmlSystemSupport, ConvertTypes.Convert(inCoupon), ConvertTypes.Convert(outCoupon));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param originalGuid
     * @param processAgent
     * @param extra
     * @return int
     */
    public int ModifyProcessAgent(String originalGuid, ProcessAgent agent, String extra) {
        final String methodName = "ModifyProcessAgent";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = 0;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.processAgentProxy.modifyProcessAgent(originalGuid, ConvertTypes.Convert(agent), extra);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param registerGuid
     * @param serviceDescriptions
     */
    public void Register(String registerGuid, ServiceDescription[] serviceDescriptions) {
        final String methodName = "Register";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            this.processAgentProxy.register(registerGuid, ConvertTypes.Convert(serviceDescriptions));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param domainGuid
     * @param serviceGuid
     * @return int
     */
    public int RemoveDomainCredentials(String domainGuid, String serviceGuid) {
        final String methodName = "RemoveDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.processAgentProxy.removeDomainCredentials(domainGuid, serviceGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param domainGuid
     * @param serviceGuid
     * @param state
     * @return int
     */
    public int RetireProcessAgent(String domainGuid, String serviceGuid, boolean state) {
        final String methodName = "RetireProcessAgent";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.processAgentProxy.retireProcessAgent(domainGuid, serviceGuid, state);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param statusNotificationReport
     */
    public void StatusNotification(StatusNotificationReport report) {
        final String methodName = "StatusNotification";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            this.processAgentProxy.statusNotification(ConvertTypes.Convert(report));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ProcessAgentUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    //================================================================================================================//
    /**
     *
     */
    private void SetAgentAuthHeader() {
        /*
         * Create authentication header
         */
        AgentAuthHeader agentAuthHeader = new AgentAuthHeader();
        agentAuthHeader.setAgentGuid(this.authHeaderAgentGuid);
        agentAuthHeader.setCoupon(this.authHeaderCoupon);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        Map<String, Object> requestContext = ((BindingProvider) this.processAgentProxy).getRequestContext();
        requestContext.put(AgentAuthHeader.class.getSimpleName(), agentAuthHeader);
    }

    /**
     *
     */
    private void SetInitAuthHeader() {
        /*
         * Create authentication header
         */
        InitAuthHeader initAuthHeader = new InitAuthHeader();
        initAuthHeader.setInitPasskey(this.authHeaderInitPasskey);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        Map<String, Object> requestContext = ((BindingProvider) this.processAgentProxy).getRequestContext();
        requestContext.put(InitAuthHeader.class.getSimpleName(), initAuthHeader);
    }

    /**
     *
     */
    private void UnsetAgentAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.processAgentProxy).getRequestContext();
        requestContext.remove(AgentAuthHeader.class.getSimpleName());
    }

    /**
     *
     */
    private void UnsetInitAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.processAgentProxy).getRequestContext();
        requestContext.remove(InitAuthHeader.class.getSimpleName());
    }
}
