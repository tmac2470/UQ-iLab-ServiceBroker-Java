/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

import java.util.Calendar;
import java.util.logging.Level;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.ProcessAgentAPI;
import uq.ilabs.library.processagent.ProcessAgents;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;

/**
 *
 * @author uqlpayne
 */
@Singleton
public class ProcessAgentBean {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ProcessAgentBean.class.getName();
    private static final Level logLevel = Level.INFO;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    private ProcessAgents processAgents;
    //</editor-fold>

    /**
     * Constructor - Seems that this gets called when the project is deployed which is unexpected. To get around this,
     * check to see if the service has been initialized and this class has been initialized. Can't do logging until the
     * service has been initialized and the logger created.
     */
    public ProcessAgentBean() throws Exception {
        final String methodName = "ProcessAgentBean";

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
                 * Create an instance of the ProcessAgents
                 */
                this.processAgents = new ProcessAgents(this.serviceManagement.getDbConnection());
                if (this.processAgents == null) {
                    throw new NullPointerException(ProcessAgents.class.getSimpleName());
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
     * @return
     */
    public javax.xml.datatype.XMLGregorianCalendar getServiceTime() {
        final String methodName = "getServiceTime";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        javax.xml.datatype.XMLGregorianCalendar xmlGregorianCalendar = null;

        try {
            Calendar calendar = Calendar.getInstance();
            xmlGregorianCalendar = ConvertTypes.Convert(calendar);
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return xmlGregorianCalendar;
    }

    /**
     *
     * @return
     */
    public edu.mit.ilab.ilabs.type.StatusReport getStatus() {
        final String methodName = "getStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        edu.mit.ilab.ilabs.type.StatusReport proxyStatusReport = null;

        try {
            String payload = STR_ClassName;
            StatusReport statusReport = new StatusReport(true, this.serviceManagement.getServiceGuid(), payload);
            proxyStatusReport = ConvertTypes.Convert(statusReport);
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return proxyStatusReport;
    }

    /**
     *
     * @param agentAuthHeader
     * @param proxyStatusNotificationReport
     */
    public void statusNotification(AgentAuthHeader agentAuthHeader, edu.mit.ilab.ilabs.type.StatusNotificationReport proxyStatusNotificationReport) {
        final String methodName = "statusNotification";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        StatusNotificationReport statusNotificationReport = ConvertTypes.Convert(proxyStatusNotificationReport);

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param agentAuthHeader
     * @param proxyCoupon
     * @param type
     * @param redeemer
     * @return
     */
    public boolean cancelTicket(AgentAuthHeader agentAuthHeader, edu.mit.ilab.ilabs.type.Coupon proxyCoupon, String type, String redeemer) {
        final String methodName = "cancelTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        Coupon coupon = ConvertTypes.Convert(proxyCoupon);

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    /**
     *
     * @param initAuthHeader
     * @param proxyProcessAgent
     * @param proxyInCoupon
     * @param proxyOutCoupon
     * @return
     */
    public edu.mit.ilab.ilabs.type.ProcessAgent installDomainCredentials(InitAuthHeader initAuthHeader, edu.mit.ilab.ilabs.type.ProcessAgent proxyProcessAgent,
            edu.mit.ilab.ilabs.type.Coupon proxyInCoupon, edu.mit.ilab.ilabs.type.Coupon proxyOutCoupon) {
        final String methodName = "installDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String initPasskey = initAuthHeader.getInitPasskey();
        ProcessAgent processAgent = ConvertTypes.Convert(proxyProcessAgent);
        Coupon inCoupon = ConvertTypes.Convert(proxyInCoupon);
        Coupon outCoupon = ConvertTypes.Convert(proxyOutCoupon);

        try {
            ProcessAgent selfProcessAgent = this.processAgents.InstallDomainCredentials(initPasskey, processAgent, inCoupon, outCoupon);
            proxyProcessAgent = ConvertTypes.Convert(selfProcessAgent);

        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return proxyProcessAgent;
    }

    /**
     *
     * @param agentAuthHeader
     * @param originalGuid
     * @param proxyProcessAgent
     * @param extra
     * @param proxyInCoupon
     * @param proxyOutCoupon
     * @return
     */
    public int modifyDomainCredentials(AgentAuthHeader agentAuthHeader, String originalGuid, edu.mit.ilab.ilabs.type.ProcessAgent proxyProcessAgent,
            String xmlSystemSupport, edu.mit.ilab.ilabs.type.Coupon proxyInCoupon, edu.mit.ilab.ilabs.type.Coupon proxyOutCoupon) {
        final String methodName = "modifyDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = -1;

        ProcessAgent processAgent = ConvertTypes.Convert(proxyProcessAgent);
        Coupon inCoupon = ConvertTypes.Convert(proxyInCoupon);
        Coupon outCoupon = ConvertTypes.Convert(proxyOutCoupon);

        try {
            result = this.processAgents.ModifyDomainCredentials(originalGuid, processAgent, xmlSystemSupport, inCoupon, outCoupon);
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param agentAuthHeader
     * @param domainGuid
     * @param serviceGuid
     * @return
     */
    public int removeDomainCredentials(AgentAuthHeader agentAuthHeader, String domainGuid, String serviceGuid) {
        final String methodName = "removeDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = 0;

        try {
            result = this.processAgents.RemoveDomainCredentials(domainGuid, serviceGuid);
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param agentAuthHeader
     * @param originalGuid
     * @param proxyProcessAgent
     * @param extra
     * @return
     */
    public int modifyProcessAgent(AgentAuthHeader agentAuthHeader, String originalGuid, edu.mit.ilab.ilabs.type.ProcessAgent proxyProcessAgent, String extra) {
        final String methodName = "modifyProcessAgent";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = -1;

        ProcessAgent processAgent = ConvertTypes.Convert(proxyProcessAgent);

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param agentAuthHeader
     * @param domainGuid
     * @param serviceGuid
     * @param state
     * @return
     */
    public int retireProcessAgent(AgentAuthHeader agentAuthHeader, String domainGuid, String serviceGuid, boolean state) {
        final String methodName = "retireProcessAgent";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = -1;

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param agentAuthHeader
     * @param registerGuid
     * @param arrayOfServiceDescription
     */
    public void register(AgentAuthHeader agentAuthHeader, String registerGuid, edu.mit.ilab.ilabs.services.ArrayOfServiceDescription arrayOfServiceDescription) {
        final String methodName = "register";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ServiceDescription[] serviceDescriptions = ConvertTypes.Convert(arrayOfServiceDescription);

        try {
            ServiceDescription[] serviceDescriptionArray = this.processAgents.Register(registerGuid, serviceDescriptions);
            if (serviceDescriptionArray != null) {
                /*
                 * Get the caller's service url and outgoing coupon to place in the AgentAuthHeader
                 */
                ProcessAgentInfo processAgentInfo = this.processAgents.RetrieveByGuid(agentAuthHeader.getAgentGuid());
                if (processAgentInfo != null) {
                    /*
                     * Register SystemSupportInfo for self to callers
                     */
                    for (ServiceDescription serviceDescription : serviceDescriptionArray) {
                        ProcessAgentAPI processAgentAPI = new ProcessAgentAPI(processAgentInfo.getServiceUrl());
                        processAgentAPI.setAuthHeaderAgentGuid(this.serviceManagement.getServiceGuid());
                        processAgentAPI.setAuthHeaderCoupon(processAgentInfo.getOutCoupon());
                        processAgentAPI.Register(registerGuid, new ServiceDescription[]{new ServiceDescription(serviceDescription.getServiceProviderInfo(), null, null)});
                    }
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            this.ThrowSoapFault(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    //================================================================================================================//
    /**
     *
     * @param message
     */
    private void ThrowSoapFault(String message) {
        /*
         * Create a SOAPFaultException to be thrown back to the caller
         */
        try {
            SOAPFault fault = SOAPFactory.newInstance().createFault();
            fault.setFaultString(message);
            throw new SOAPFaultException(fault);
        } catch (SOAPException e) {
            Logfile.WriteError(e.getMessage());
        }
    }

    /**
     *
     */
    @PreDestroy
    private void preDestroy() {
        final String methodName = "preDestroy";
        Logfile.WriteCalled(Level.INFO, STR_ClassName, methodName);

        /*
         * Close the ticketing thread
         */
        this.processAgents.Close();

        Logfile.WriteCompleted(Level.INFO, STR_ClassName, methodName);
    }
}
