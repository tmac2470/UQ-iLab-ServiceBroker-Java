/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.labsidescheduling;

import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.labsidescheduling.engine.ServiceManagement;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.proxy.ProcessAgentAPI;

/**
 *
 * @author uqlpayne
 */
public class ProcessAgentHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ProcessAgentHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    //</editor-fold>

    /**
     *
     * @param experimentStorageBean
     * @param serviceManagement
     */
    public ProcessAgentHandler(ServiceManagement serviceManagement) {
        this.serviceManagement = serviceManagement;
    }

    /**
     *
     * @return Calendar
     */
    public Calendar getServiceTime() {
        final String methodName = "getServiceTime";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Calendar calendar = null;

        try {
            calendar = Calendar.getInstance();
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return calendar;
    }

    /**
     *
     * @return StatusReport
     */
    public StatusReport getStatus() {
        final String methodName = "getStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        StatusReport statusReport = null;

        try {
            String payload = String.format(ProcessAgentAPI.STR_Version_arg, this.serviceManagement.getVersion());
            statusReport = new StatusReport(true, this.serviceManagement.getServiceGuid(), payload);
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return statusReport;
    }

    /**
     *
     * @param agentAuthHeader
     * @param statusNotificationReport
     */
    public void statusNotification(AgentAuthHeader agentAuthHeader, StatusNotificationReport statusNotificationReport) {
        final String methodName = "statusNotification";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);


        try {
            //
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param agentAuthHeader
     * @param proxyCoupon
     * @param type
     * @param redeemer
     * @return boolean
     */
    public boolean cancelTicket(AgentAuthHeader agentAuthHeader, Coupon proxyCoupon, String type, String redeemer) {
        final String methodName = "cancelTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean value = false;

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return value;
    }

    /**
     *
     * @param initAuthHeader
     * @param processAgent
     * @param inCoupon
     * @param outCoupon
     * @return ProcessAgent
     * @throws Exception
     */
    public ProcessAgent installDomainCredentials(InitAuthHeader initAuthHeader, ProcessAgent processAgent, Coupon inCoupon, Coupon outCoupon) throws Exception {
        final String methodName = "installDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ProcessAgent selfProcessAgent = null;

        try {
            String initPasskey = initAuthHeader.getInitPasskey();
            selfProcessAgent = this.serviceManagement.getProcessAgents().InstallDomainCredentials(initPasskey, processAgent, inCoupon, outCoupon);

        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return selfProcessAgent;
    }

    /**
     *
     * @param agentAuthHeader
     * @param originalGuid
     * @param processAgent
     * @param xmlSystemSupport
     * @param inCoupon
     * @param outCoupon
     * @return int
     */
    public int modifyDomainCredentials(AgentAuthHeader agentAuthHeader, String originalGuid, ProcessAgent processAgent, String xmlSystemSupport, Coupon inCoupon, Coupon outCoupon) {
        final String methodName = "modifyDomainCredentials";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = -1;

        try {
            result = this.serviceManagement.getProcessAgents().ModifyDomainCredentials(originalGuid, processAgent, xmlSystemSupport, inCoupon, outCoupon);
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
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
            result = this.serviceManagement.getProcessAgents().RemoveDomainCredentials(domainGuid, serviceGuid);
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param agentAuthHeader
     * @param originalGuid
     * @param processAgent
     * @param extra
     * @return int
     */
    public int modifyProcessAgent(AgentAuthHeader agentAuthHeader, String originalGuid, ProcessAgent processAgent, String extra) {
        final String methodName = "modifyProcessAgent";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int result = -1;

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
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
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return result;
    }

    /**
     *
     * @param agentAuthHeader
     * @param registerGuid
     * @param ServiceDescriptions
     */
    public void register(AgentAuthHeader agentAuthHeader, String registerGuid, ServiceDescription[] serviceDescriptions) throws Exception {
        final String methodName = "register";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            ServiceDescription[] serviceDescriptionArray = this.serviceManagement.getProcessAgents().Register(registerGuid, serviceDescriptions);
            if (serviceDescriptionArray != null) {
                /*
                 * Get the caller's service url and outgoing coupon to place in the AgentAuthHeader
                 */
                ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgents().RetrieveByGuid(agentAuthHeader.getAgentGuid());
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
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }
}
