/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.processagent;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
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
     * String constants for logfile messages
     */
    private static final String STRLOG_ServiceUrl_arg = "ServiceUrl: '%s'";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "serviceUrl";
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
            if (webServiceClient == null) {
                throw new NullPointerException(ProcessAgentProxy.class.getSimpleName());
            }
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
            success = this.processAgentProxy.cancelTicket(this.ConvertType(coupon), type, redeemer);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
            calendar = this.ConvertType(xmlGregorianCalendar);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
            statusReport = this.ConvertType(proxyStatusReport);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
                    this.ConvertType(agent), this.ConvertType(inCoupon), this.ConvertType(outCoupon));
            processAgent = this.ConvertType(proxyProcessAgent);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
    public int ModifyDomainCredentials(String originalGuid, ProcessAgent agent, String extra, Coupon inCoupon, Coupon outCoupon) {
        final String methodName = "ModifyDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetInitAuthHeader();
            retval = this.processAgentProxy.modifyDomainCredentials(originalGuid, this.ConvertType(agent), extra, this.ConvertType(inCoupon), this.ConvertType(outCoupon));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
            retval = this.processAgentProxy.modifyProcessAgent(originalGuid, this.ConvertType(agent), extra);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
            this.processAgentProxy.register(registerGuid, (ArrayOfServiceDescription) Arrays.asList(serviceDescriptions));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
            this.processAgentProxy.statusNotification(this.ConvertType(report));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
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
        agentAuthHeader.setCoupon(this.ConvertType(this.authHeaderCoupon));

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
     * @param coupon
     * @return uq.ilabs.processagent.Coupon
     */
    private uq.ilabs.processagent.Coupon ConvertType(Coupon coupon) {
        uq.ilabs.processagent.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new uq.ilabs.processagent.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }

    /**
     *
     * @param proxyCoupon
     * @return Coupon
     */
    private Coupon ConvertType(uq.ilabs.processagent.Coupon proxyCoupon) {
        Coupon coupon = null;

        if (proxyCoupon != null) {
            coupon = new Coupon();
            coupon.setCouponId(proxyCoupon.getCouponId());
            coupon.setIssuerGuid(proxyCoupon.getIssuerGuid());
            coupon.setPasskey(proxyCoupon.getPasskey());
        }

        return coupon;
    }

    /**
     *
     * @param calendar
     * @return XMLGregorianCalendar
     */
    private XMLGregorianCalendar ConvertType(Calendar calendar) {
        XMLGregorianCalendar xmlGregorianCalendar = null;

        if (calendar != null) {
            try {
                DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(calendar.getTime());
                xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException ex) {
            }
        }

        return xmlGregorianCalendar;
    }

    /**
     *
     * @param xmlGregorianCalendar
     * @return Calendar
     */
    private Calendar ConvertType(XMLGregorianCalendar xmlGregorianCalendar) {
        Calendar calendar = null;

        if (xmlGregorianCalendar != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(xmlGregorianCalendar.toGregorianCalendar().getTime());
        }

        return calendar;
    }

    /**
     *
     * @param processAgent
     * @return uq.ilabs.processagent.ProcessAgent
     */
    private uq.ilabs.processagent.ProcessAgent ConvertType(ProcessAgent processAgent) {
        uq.ilabs.processagent.ProcessAgent proxyProcessAgent = null;

        if (processAgent != null) {
            proxyProcessAgent = new uq.ilabs.processagent.ProcessAgent();
            proxyProcessAgent.setAgentGuid(processAgent.getAgentGuid());
            proxyProcessAgent.setAgentName(processAgent.getAgentName());
            proxyProcessAgent.setType(ProcessAgentTypes.ToProcessAgentTypeName(processAgent.getAgentType()));
            proxyProcessAgent.setWebServiceUrl(processAgent.getServiceUrl());
            proxyProcessAgent.setCodeBaseUrl(processAgent.getClientUrl());
            proxyProcessAgent.setDomainGuid(processAgent.getDomainGuid());
        }

        return proxyProcessAgent;
    }

    /**
     *
     * @param proxyProcessAgent
     * @return ProcessAgent
     */
    private ProcessAgent ConvertType(uq.ilabs.processagent.ProcessAgent proxyProcessAgent) {
        ProcessAgent processAgent = null;

        if (proxyProcessAgent != null) {
            processAgent = new ProcessAgent();
            processAgent.setAgentGuid(proxyProcessAgent.getAgentGuid());
            processAgent.setAgentName(proxyProcessAgent.getAgentName());
            processAgent.setAgentType(ProcessAgentTypes.ToProcessAgentType(proxyProcessAgent.getType()));
            processAgent.setServiceUrl(proxyProcessAgent.getWebServiceUrl());
            processAgent.setClientUrl(proxyProcessAgent.getCodeBaseUrl());
            processAgent.setDomainGuid(proxyProcessAgent.getDomainGuid());
        }

        return processAgent;
    }

    /**
     *
     * @param statusNotificationReport
     * @return uq.ilabs.processagent.StatusNotificationReport
     */
    private uq.ilabs.processagent.StatusNotificationReport ConvertType(StatusNotificationReport statusNotificationReport) {
        uq.ilabs.processagent.StatusNotificationReport proxyStatusNotificationReport = null;

        if (statusNotificationReport != null) {
            proxyStatusNotificationReport = new uq.ilabs.processagent.StatusNotificationReport();
            proxyStatusNotificationReport.setAlertCode(statusNotificationReport.getAlertCode());
            proxyStatusNotificationReport.setServiceGuid(statusNotificationReport.getServiceGuid());
            proxyStatusNotificationReport.setTime(this.ConvertType(statusNotificationReport.getTimestamp()));
            proxyStatusNotificationReport.setPayload(statusNotificationReport.getPayload());
        }

        return proxyStatusNotificationReport;
    }

    /**
     *
     * @param proxyStatusReport
     * @return StatusReport
     */
    private StatusReport ConvertType(uq.ilabs.processagent.StatusReport proxyStatusReport) {
        StatusReport statusReport = null;

        if (proxyStatusReport != null) {
            statusReport = new StatusReport();
            statusReport.setOnline(proxyStatusReport.isOnline());
            statusReport.setServiceGuid(proxyStatusReport.getServiceGuid());
            statusReport.setPayload(proxyStatusReport.getPayload());
        }

        return statusReport;
    }
}
