/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

import java.util.Calendar;
import java.util.logging.Level;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.processagent.AgentAuthHeader;
import uq.ilabs.processagent.ArrayOfServiceDescription;
import uq.ilabs.processagent.InitAuthHeader;
import uq.ilabs.processagent.ObjectFactory;
import uq.ilabs.processagent.ProcessAgentProxy;
import uq.ilabs.processagent.ProcessAgentProxySoap;

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
    public static final String STR_ProcessAgentStatus_arg2 = "ProcessAgent Status: %s - %s";
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
    private QName qnameAgentAuthHeader;
    private QName qnameInitAuthHeader;
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

            /*
             * Get authentication header QName
             */
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBElement<AgentAuthHeader> jaxbElementAgentAuthHeader = objectFactory.createAgentAuthHeader(new AgentAuthHeader());
            this.qnameAgentAuthHeader = jaxbElementAgentAuthHeader.getName();
            JAXBElement<InitAuthHeader> jaxbElementInitAuthHeader = objectFactory.createInitAuthHeader(new InitAuthHeader());
            this.qnameInitAuthHeader = jaxbElementInitAuthHeader.getName();

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
            success = this.processAgentProxy.cancelTicket(ConvertTypes.ConvertType(coupon), type, redeemer);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
            calendar = ConvertTypes.ConvertType(xmlGregorianCalendar);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
            uq.ilabs.processagent.StatusReport proxyStatusReport = this.processAgentProxy.getStatus();
            statusReport = ConvertTypes.ConvertType(proxyStatusReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
            uq.ilabs.processagent.ProcessAgent proxyProcessAgent = this.processAgentProxy.installDomainCredentials(
                    ConvertTypes.ConvertType(agent), ConvertTypes.ConvertType(inCoupon), ConvertTypes.ConvertType(outCoupon));
            processAgent = ConvertTypes.ConvertType(proxyProcessAgent);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
                    originalGuid, ConvertTypes.ConvertType(agent), xmlSystemSupport, ConvertTypes.ConvertType(inCoupon), ConvertTypes.ConvertType(outCoupon));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
            retval = this.processAgentProxy.modifyProcessAgent(originalGuid, ConvertTypes.ConvertType(agent), extra);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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

        ArrayOfServiceDescription proxyServiceDescriptions = ConvertTypes.ConvertType(serviceDescriptions);

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            this.processAgentProxy.register(registerGuid, proxyServiceDescriptions);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
            this.processAgentProxy.statusNotification(ConvertTypes.ConvertType(report));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new ProtocolException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new ProtocolException(STRERR_ProcessAgentUnaccessible);
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
        agentAuthHeader.setCoupon(ConvertTypes.ConvertType(this.authHeaderCoupon));

        /*
         * Pass the authentication header to the message handler through the message context
         */
        ((BindingProvider) this.processAgentProxy).getRequestContext().put(this.qnameAgentAuthHeader.getLocalPart(), agentAuthHeader);
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
        ((BindingProvider) this.processAgentProxy).getRequestContext().put(this.qnameInitAuthHeader.getLocalPart(), initAuthHeader);
    }

    /**
     *
     */
    private void UnsetAgentAuthHeader() {
        ((BindingProvider) this.processAgentProxy).getRequestContext().remove(this.qnameAgentAuthHeader.getLocalPart());
    }

    /**
     *
     */
    private void UnsetInitAuthHeader() {
        ((BindingProvider) this.processAgentProxy).getRequestContext().remove(this.qnameInitAuthHeader.getLocalPart());
    }
}
