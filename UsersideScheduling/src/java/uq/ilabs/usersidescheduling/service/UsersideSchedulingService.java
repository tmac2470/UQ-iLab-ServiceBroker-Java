/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.usersidescheduling.service;

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
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.scheduling.Reservation;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.usersidescheduling.UsersideSchedulingAppBean;

/**
 *
 * @author uqlpayne
 */
@WebService(serviceName = "UsersideSchedulingService",
        portName = "UsersideSchedulingServiceSoap",
        endpointInterface = "edu.mit.ilab.ilabs.services.UsersideSchedulingServiceSoap",
        targetNamespace = "http://ilab.mit.edu/iLabs/Services",
        wsdlLocation = "WEB-INF/wsdl/UsersideSchedulingService/IUsersideSchedulingService.asmx.wsdl")
@HandlerChain(file = "UsersideSchedulingService_handler.xml")
public class UsersideSchedulingService {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    @Resource
    private WebServiceContext wsContext;
    @EJB
    private UsersideSchedulingAppBean usersideSchedulingAppBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="UsersideSchedulingHandler">
    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return edu.mit.ilab.ilabs.services.ArrayOfTimePeriod
     */
    public edu.mit.ilab.ilabs.services.ArrayOfTimePeriod retrieveAvailableTimePeriods(java.lang.String serviceBrokerGuid, java.lang.String groupName, java.lang.String labServerGuid, java.lang.String clientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime) {
        edu.mit.ilab.ilabs.services.ArrayOfTimePeriod arrayOfTimePeriod = null;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            TimePeriod[] timePeriods = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    retrieveAvailableTimePeriods(operationAuthHeader, serviceBrokerGuid, groupName, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
            arrayOfTimePeriod = ConvertTypes.ConvertServices(timePeriods);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfTimePeriod;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param userName
     * @param labServerGuid
     * @param labClientGuid
     * @param startTime
     * @param endTime
     * @return edu.mit.ilab.ilabs.services.ArrayOfReservation
     */
    public edu.mit.ilab.ilabs.services.ArrayOfReservation listReservations(java.lang.String serviceBrokerGuid, java.lang.String userName, java.lang.String labServerGuid, java.lang.String labClientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime) {
        edu.mit.ilab.ilabs.services.ArrayOfReservation arrayOfReservation = null;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            Reservation[] reservations = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    listReservations(operationAuthHeader, serviceBrokerGuid, userName, labServerGuid, labClientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
            arrayOfReservation = ConvertTypes.ConvertServices(reservations);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfReservation;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param userName
     * @param groupName
     * @param labServerGuid
     * @param labClientGuid
     * @param startTime
     * @param endTime
     * @return String
     */
    public java.lang.String addReservation(java.lang.String serviceBrokerGuid, java.lang.String userName, java.lang.String groupName, java.lang.String labServerGuid, java.lang.String labClientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime) {
        String value = null;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    addReservation(operationAuthHeader, serviceBrokerGuid, userName, groupName, labServerGuid, labClientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param labServerGuid
     * @param labClientGuid
     * @param startTime
     * @param endTime
     * @param message
     * @return int
     */
    public int revokeReservation(java.lang.String serviceBrokerGuid, java.lang.String groupName, java.lang.String labServerGuid, java.lang.String labClientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime, java.lang.String message) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    revokeReservation(operationAuthHeader, serviceBrokerGuid, groupName, labServerGuid, labClientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime), message);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param userName
     * @param labServerGuid
     * @param clientGuid
     * @return edu.mit.ilab.ilabs.type.Reservation
     */
    public edu.mit.ilab.ilabs.type.Reservation redeemReservation(java.lang.String serviceBrokerGuid, java.lang.String userName, java.lang.String labServerGuid, java.lang.String clientGuid) {
        edu.mit.ilab.ilabs.type.Reservation proxyReservation = null;

        OperationAuthHeader operationAuthHeader = GetOperationAuthHeader();

        try {
            Reservation reservation = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    redeemReservation(operationAuthHeader, serviceBrokerGuid, userName, labServerGuid, clientGuid);
            proxyReservation = ConvertTypes.ConvertType(reservation);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyReservation;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param serviceBrokerName
     * @param groupName
     * @return int
     */
    public int addCredentialSet(java.lang.String serviceBrokerGuid, java.lang.String serviceBrokerName, java.lang.String groupName) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    addCredentialSet(agentAuthHeader, serviceBrokerGuid, serviceBrokerName, groupName);
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
     * @return int
     */
    public int modifyCredentialSet(java.lang.String serviceBrokerGuid, java.lang.String serviceBrokerName, java.lang.String groupName) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    modifyCredentialSet(agentAuthHeader, serviceBrokerGuid, serviceBrokerName, groupName);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @return int
     */
    public int removeCredentialSet(java.lang.String serviceBrokerGuid, java.lang.String groupName) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().removeCredentialSet(agentAuthHeader, serviceBrokerGuid, groupName);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerGuid
     * @param labServerName
     * @param labClientGuid
     * @param labClientName
     * @param labClientVersion
     * @param providerName
     * @param lssGuid
     * @return int
     */
    public int addExperimentInfo(java.lang.String labServerGuid, java.lang.String labServerName, java.lang.String labClientGuid, java.lang.String labClientName, java.lang.String labClientVersion, java.lang.String providerName, java.lang.String lssGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    addExperimentInfo(agentAuthHeader, labServerGuid, labServerName, labClientGuid, labClientName, labClientVersion, providerName, lssGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerGuid
     * @param labServerName
     * @param labClientGuid
     * @param labClientName
     * @param labClientVersion
     * @param providerName
     * @param lssGuid
     * @return int
     */
    public int modifyExperimentInfo(java.lang.String labServerGuid, java.lang.String labServerName, java.lang.String labClientGuid, java.lang.String labClientName, java.lang.String labClientVersion, java.lang.String providerName, java.lang.String lssGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    modifyExperimentInfo(agentAuthHeader, labServerGuid, labServerName, labClientGuid, labClientName, labClientVersion, providerName, lssGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerGuid
     * @param labClientGuid
     * @param lssGuid
     * @return int
     */
    public int removeExperimentInfo(java.lang.String labServerGuid, java.lang.String labClientGuid, java.lang.String lssGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    removeExperimentInfo(agentAuthHeader, labServerGuid, labClientGuid, lssGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param lssGuid
     * @param lssName
     * @param lssUrl
     * @return int
     */
    public int addLSSInfo(java.lang.String lssGuid, java.lang.String lssName, java.lang.String lssUrl) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    addLSSInfo(agentAuthHeader, lssGuid, lssName, lssUrl);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param lssGuid
     * @param lssName
     * @param lssUrl
     * @return int
     */
    public int modifyLSSInfo(java.lang.String lssGuid, java.lang.String lssName, java.lang.String lssUrl) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    modifyLSSInfo(agentAuthHeader, lssGuid, lssName, lssUrl);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param lssGuid
     * @return int
     */
    public int removeLSSInfo(java.lang.String lssGuid) {
        int value = -1;

        AgentAuthHeader agentAuthHeader = GetAgentAuthHeader();

        try {
            value = this.usersideSchedulingAppBean.getUsersideSchedulingHandler().
                    removeLSSInfo(agentAuthHeader, lssGuid);
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
            Calendar calendar = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            StatusReport statusReport = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            value = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            ProcessAgent processAgent = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            value = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            value = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            value = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            value = this.usersideSchedulingAppBean.getProcessAgentHandler().
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
            this.usersideSchedulingAppBean.getProcessAgentHandler().
                    register(agentAuthHeader, registerGuid, ConvertTypes.ConvertServices(info));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }
    //</editor-fold>

    //================================================================================================================//
    /**
     *
     * /**
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
