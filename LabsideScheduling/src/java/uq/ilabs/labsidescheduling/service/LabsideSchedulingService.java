/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.labsidescheduling.service;

import java.util.Calendar;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.labsidescheduling.LabsideSchedulingAppBean;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
@WebService(serviceName = "LabsideSchedulingService",
        portName = "LabsideSchedulingServiceSoap",
        endpointInterface = "edu.mit.ilab.ilabs.services.LabsideSchedulingServiceSoap",
        targetNamespace = "http://ilab.mit.edu/iLabs/Services",
        wsdlLocation = "WEB-INF/wsdl/LabsideSchedulingService/ILabsideSchedulingService.asmx.wsdl")
@HandlerChain(file = "LabsideSchedulingService_handler.xml")
public class LabsideSchedulingService {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    @Resource
    private WebServiceContext wsContext;
    @EJB
    private LabsideSchedulingAppBean labsideSchedulingAppBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LabsideSchedulingServiceHandler">
    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return edu.mit.ilab.ilabs.services.ArrayOfTimePeriod
     */
    public edu.mit.ilab.ilabs.services.ArrayOfTimePeriod retrieveAvailableTimePeriods(java.lang.String serviceBrokerGuid, java.lang.String groupName, java.lang.String ussGuid, java.lang.String labServerGuid, java.lang.String clientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime) {
        edu.mit.ilab.ilabs.services.ArrayOfTimePeriod arrayOfTimePeriod = null;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            TimePeriod[] timePeriods = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    retrieveAvailableTimePeriods(operationAuthHeader, serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
            arrayOfTimePeriod = ConvertTypes.ConvertServices(timePeriods);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfTimePeriod;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return java.lang.String
     */
    public java.lang.String confirmReservation(java.lang.String serviceBrokerGuid, java.lang.String groupName, java.lang.String ussGuid, java.lang.String labServerGuid, java.lang.String clientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime) {
        String value = null;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    confirmReservation(operationAuthHeader, serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return int
     */
    public int redeemReservation(java.lang.String serviceBrokerGuid, java.lang.String groupName, java.lang.String ussGuid, java.lang.String labServerGuid, java.lang.String clientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    redeemReservation(operationAuthHeader, serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return int
     */
    public int removeReservation(java.lang.String serviceBrokerGuid, java.lang.String groupName, java.lang.String ussGuid, java.lang.String labServerGuid, java.lang.String clientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    removeReservation(operationAuthHeader, serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param ussGuid
     * @param ussName
     * @param ussUrl
     * @param coupon
     * @return int
     */
    public int addUSSInfo(java.lang.String ussGuid, java.lang.String ussName, java.lang.String ussUrl, edu.mit.ilab.ilabs.type.Coupon coupon) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    addUSSInfo(agentAuthHeader, ussGuid, ussName, ussUrl, ConvertTypes.ConvertType(coupon));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param ussGuid
     * @param ussName
     * @param ussUrl
     * @param coupon
     * @return int
     */
    public int modifyUSSInfo(java.lang.String ussGuid, java.lang.String ussName, java.lang.String ussUrl, edu.mit.ilab.ilabs.type.Coupon coupon) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    modifyUSSInfo(agentAuthHeader, ussGuid, ussName, ussUrl, ConvertTypes.ConvertType(coupon));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param ussGuid
     * @return int
     */
    public int removeUSSInfo(java.lang.String ussGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    removeUSSInfo(agentAuthHeader, ussGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param serviceBrokerName
     * @param groupName
     * @param ussGuid
     * @return int
     */
    public int addCredentialSet(java.lang.String serviceBrokerGuid, java.lang.String serviceBrokerName, java.lang.String groupName, java.lang.String ussGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    addCredentialSet(agentAuthHeader, serviceBrokerGuid, serviceBrokerName, groupName, ussGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param serviceBrokerName
     * @param groupName
     * @param ussGuid
     * @return int
     */
    public int modifyCredentialSet(java.lang.String serviceBrokerGuid, java.lang.String serviceBrokerName, java.lang.String groupName, java.lang.String ussGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    modifyCredentialSet(agentAuthHeader, serviceBrokerGuid, serviceBrokerName, groupName, ussGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param ussGuid
     * @return int
     */
    public int removeCredentialSet(java.lang.String serviceBrokerGuid, java.lang.String groupName, java.lang.String ussGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    removeCredentialSet(agentAuthHeader, serviceBrokerGuid, groupName, ussGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerGuid
     * @param labServerName
     * @param clientGuid
     * @param clientName
     * @param clientVersion
     * @param providerName
     * @return int
     */
    public int addExperimentInfo(java.lang.String labServerGuid, java.lang.String labServerName, java.lang.String clientGuid, java.lang.String clientName, java.lang.String clientVersion, java.lang.String providerName) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    addExperimentInfo(agentAuthHeader, labServerGuid, labServerName, clientGuid, clientName, clientVersion, providerName);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerGuid
     * @param labServerName
     * @param clientGuid
     * @param clientName
     * @param clientVersion
     * @param providerName
     * @return int
     */
    public int modifyExperimentInfo(java.lang.String labServerGuid, java.lang.String labServerName, java.lang.String clientGuid, java.lang.String clientName, java.lang.String clientVersion, java.lang.String providerName) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    modifyExperimentInfo(agentAuthHeader, labServerGuid, labServerName, clientGuid, clientName, clientVersion, providerName);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerGuid
     * @param clientGuid
     * @return int
     */
    public int removeExperimentInfo(java.lang.String labServerGuid, java.lang.String clientGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getLabsideSchedulingHandler().
                    removeExperimentInfo(agentAuthHeader, labServerGuid, clientGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ProcessAgentHandler">
    /**
     *
     * @return javax.xml.datatype.XMLGregorianCalendar
     */
    public javax.xml.datatype.XMLGregorianCalendar getServiceTime() {
        javax.xml.datatype.XMLGregorianCalendar xmlGregorianCalendar = null;

        try {
            Calendar calendar = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    getServiceTime();
            xmlGregorianCalendar = ConvertTypes.Convert(calendar);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return xmlGregorianCalendar;
    }

    /**
     *
     * @return edu.mit.ilab.ilabs.type.StatusReport
     */
    public edu.mit.ilab.ilabs.type.StatusReport getStatus() {
        edu.mit.ilab.ilabs.type.StatusReport proxyStatusReport = null;

        try {
            StatusReport statusReport = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    getStatus();
            proxyStatusReport = ConvertTypes.ConvertType(statusReport);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyStatusReport;
    }

    /**
     *
     * @param report
     */
    public void statusNotification(edu.mit.ilab.ilabs.type.StatusNotificationReport report) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            this.labsideSchedulingAppBean.getProcessAgentHandler().
                    statusNotification(agentAuthHeader, ConvertTypes.ConvertType(report));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }

    /**
     *
     * @param coupon
     * @param type
     * @param redeemer
     * @return boolean
     */
    public boolean cancelTicket(edu.mit.ilab.ilabs.type.Coupon coupon, java.lang.String type, java.lang.String redeemer) {
        boolean value = false;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    cancelTicket(agentAuthHeader, ConvertTypes.ConvertType(coupon), type, redeemer);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param service
     * @param inIdentCoupon
     * @param outIdentCoupon
     * @return edu.mit.ilab.ilabs.type.ProcessAgent
     */
    public edu.mit.ilab.ilabs.type.ProcessAgent installDomainCredentials(edu.mit.ilab.ilabs.type.ProcessAgent service, edu.mit.ilab.ilabs.type.Coupon inIdentCoupon, edu.mit.ilab.ilabs.type.Coupon outIdentCoupon) {
        edu.mit.ilab.ilabs.type.ProcessAgent proxyProcessAgent = null;

        InitAuthHeader initAuthHeader = this.GetInitAuthHeader();

        try {
            ProcessAgent processAgent = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    installDomainCredentials(initAuthHeader, ConvertTypes.ConvertType(service), ConvertTypes.ConvertType(inIdentCoupon), ConvertTypes.ConvertType(outIdentCoupon));
            proxyProcessAgent = ConvertTypes.ConvertType(processAgent);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyProcessAgent;
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
    public int modifyDomainCredentials(java.lang.String originalGuid, edu.mit.ilab.ilabs.type.ProcessAgent agent, java.lang.String extra, edu.mit.ilab.ilabs.type.Coupon inCoupon, edu.mit.ilab.ilabs.type.Coupon outCoupon) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    modifyDomainCredentials(agentAuthHeader, originalGuid, ConvertTypes.ConvertType(agent), extra, ConvertTypes.ConvertType(inCoupon), ConvertTypes.ConvertType(outCoupon));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param domainGuid
     * @param serviceGuid
     * @return int
     */
    public int removeDomainCredentials(java.lang.String domainGuid, java.lang.String serviceGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    removeDomainCredentials(agentAuthHeader, domainGuid, serviceGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param originalGuid
     * @param agent
     * @param extra
     * @return int
     */
    public int modifyProcessAgent(java.lang.String originalGuid, edu.mit.ilab.ilabs.type.ProcessAgent agent, java.lang.String extra) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    modifyProcessAgent(agentAuthHeader, originalGuid, ConvertTypes.ConvertType(agent), extra);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param domainGuid
     * @param serviceGuid
     * @param state
     * @return int
     */
    public int retireProcessAgent(java.lang.String domainGuid, java.lang.String serviceGuid, boolean state) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            value = this.labsideSchedulingAppBean.getProcessAgentHandler().
                    retireProcessAgent(agentAuthHeader, domainGuid, serviceGuid, state);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param registerGuid
     * @param info
     */
    public void register(java.lang.String registerGuid, edu.mit.ilab.ilabs.services.ArrayOfServiceDescription info) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            this.labsideSchedulingAppBean.getProcessAgentHandler().
                    register(agentAuthHeader, registerGuid, ConvertTypes.ConvertServices(info));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }
    //</editor-fold>

    //================================================================================================================//
    /**
     *
     * @return AgentAuthHeader
     */
    private AgentAuthHeader GetAgentAuthHeader() {
        AgentAuthHeader agentAuthHeader = null;

        /*
         * Get the authentication header from the message context
         */
        Object object = wsContext.getMessageContext().get(AgentAuthHeader.class.getSimpleName());

        /*
         * Check that it is an AgentAuthHeader
         */
        if (object != null && object instanceof AgentAuthHeader) {
            agentAuthHeader = (AgentAuthHeader) object;
        }

        return agentAuthHeader;
    }

    /**
     *
     * @return InitAuthHeader
     */
    private InitAuthHeader GetInitAuthHeader() {
        InitAuthHeader initAuthHeader = null;

        /*
         * Get the authentication header from the message context
         */
        Object object = wsContext.getMessageContext().get(InitAuthHeader.class.getSimpleName());

        /*
         * Check that it is an InitAuthHeader
         */
        if (object != null && object instanceof InitAuthHeader) {
            initAuthHeader = (InitAuthHeader) object;
        }

        return initAuthHeader;
    }

    /**
     *
     * @return OperationAuthHeader
     */
    private OperationAuthHeader GetOperationAuthHeader() {
        OperationAuthHeader operationAuthHeader = null;

        /*
         * Get the authentication header from the message context
         */
        Object object = wsContext.getMessageContext().get(OperationAuthHeader.class.getSimpleName());

        /*
         * Check that it is an OperationAuthHeader
         */
        if (object != null && object instanceof OperationAuthHeader) {
            operationAuthHeader = (OperationAuthHeader) object;
        }

        return operationAuthHeader;
    }

    /**
     *
     * @param message
     */
    private void ThrowSOAPFault(String message) {
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
}
