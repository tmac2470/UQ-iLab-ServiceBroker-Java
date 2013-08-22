/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.service;

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
import uq.ilabs.library.datatypes.batch.ClientSubmissionReport;
import uq.ilabs.library.datatypes.batch.LabExperimentStatus;
import uq.ilabs.library.datatypes.batch.LabStatus;
import uq.ilabs.library.datatypes.batch.ResultReport;
import uq.ilabs.library.datatypes.batch.ValidationReport;
import uq.ilabs.library.datatypes.batch.WaitEstimate;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.StatusNotificationReport;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.service.SbAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.servicebroker.IlabServiceBrokerAppBean;

/**
 *
 * @author uqlpayne
 */
@WebService(serviceName = "iLabServiceBrokerService",
        portName = "iLabServiceBrokerServiceSoap",
        endpointInterface = "edu.mit.ilab.ilabs.services.ILabServiceBrokerServiceSoap",
        targetNamespace = "http://ilab.mit.edu/iLabs/Services",
        wsdlLocation = "WEB-INF/wsdl/IlabServiceBrokerService/IiLabServiceBrokerService.asmx.wsdl")
@HandlerChain(file = "IlabServiceBrokerService_handler.xml")
public class IlabServiceBrokerService {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    @Resource
    private WebServiceContext wsContext;
    @EJB
    private IlabServiceBrokerAppBean ilabServiceBrokerAppBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BatchServiceBrokerHandler">
    /**
     *
     * @param experimentID
     * @return boolean
     */
    public boolean cancel(int experimentID) {
        boolean value = false;

        try {
            SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().cancel(sbAuthHeader, experimentID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerID
     * @param priorityHint
     * @return edu.mit.ilab.WaitEstimate
     */
    public edu.mit.ilab.WaitEstimate getEffectiveQueueLength(java.lang.String labServerID, int priorityHint) {
        edu.mit.ilab.WaitEstimate proxyWaitEstimate = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            WaitEstimate waitEstimate = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().getEffectiveQueueLength(sbAuthHeader, labServerID, priorityHint);
            proxyWaitEstimate = ConvertTypes.Convert(waitEstimate);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyWaitEstimate;
    }

    /**
     *
     * @param experimentID
     * @return edu.mit.ilab.LabExperimentStatus
     */
    public edu.mit.ilab.LabExperimentStatus getExperimentStatus(int experimentID) {
        edu.mit.ilab.LabExperimentStatus proxyLabExperimentStatus = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            LabExperimentStatus labExperimentStatus = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().getExperimentStatus(sbAuthHeader, experimentID);
            proxyLabExperimentStatus = ConvertTypes.Convert(labExperimentStatus);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyLabExperimentStatus;
    }

    /**
     *
     * @param labServerID
     * @return java.lang.String
     */
    public java.lang.String getLabConfiguration(java.lang.String labServerID) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().getLabConfiguration(sbAuthHeader, labServerID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerID
     * @return java.lang.String
     */
    public java.lang.String getLabInfo(java.lang.String labServerID) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().getLabInfo(sbAuthHeader, labServerID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param labServerID
     * @return edu.mit.ilab.LabStatus
     */
    public edu.mit.ilab.LabStatus getLabStatus(java.lang.String labServerID) {
        edu.mit.ilab.LabStatus proxyLabStatus = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            LabStatus labStatus = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().getLabStatus(sbAuthHeader, labServerID);
            proxyLabStatus = ConvertTypes.Convert(labStatus);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyLabStatus;
    }

    /**
     *
     * @param experimentID
     * @return edu.mit.ilab.ResultReport
     */
    public edu.mit.ilab.ResultReport retrieveResult(int experimentID) {
        edu.mit.ilab.ResultReport proxyResultReport = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            ResultReport resultReport = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().retrieveResult(sbAuthHeader, experimentID);
            proxyResultReport = ConvertTypes.Convert(resultReport);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyResultReport;
    }

    /**
     *
     * @param labServerID
     * @param experimentSpecification
     * @param priorityHint
     * @param emailNotification
     * @return edu.mit.ilab.ClientSubmissionReport
     */
    public edu.mit.ilab.ClientSubmissionReport submit(java.lang.String labServerID, java.lang.String experimentSpecification, int priorityHint, boolean emailNotification) {
        edu.mit.ilab.ClientSubmissionReport proxyClientSubmissionReport = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            ClientSubmissionReport clientSubmissionReport = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().submit(sbAuthHeader, labServerID, experimentSpecification, priorityHint, emailNotification);
            proxyClientSubmissionReport = ConvertTypes.Convert(clientSubmissionReport);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyClientSubmissionReport;
    }

    /**
     *
     * @param labServerID
     * @param experimentSpecification
     * @return edu.mit.ilab.ValidationReport
     */
    public edu.mit.ilab.ValidationReport validate(java.lang.String labServerID, java.lang.String experimentSpecification) {
        edu.mit.ilab.ValidationReport proxyValidationReport = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            ValidationReport validationReport = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().validate(sbAuthHeader, labServerID, experimentSpecification);
            proxyValidationReport = ConvertTypes.Convert(validationReport);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyValidationReport;
    }

    /**
     *
     * @param experimentID
     */
    public void notify(int experimentID) {
        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().notify(sbAuthHeader, experimentID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }

    /**
     *
     * @param name
     * @param itemValue
     */
    public void saveClientItem(java.lang.String name, java.lang.String itemValue) {
        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().saveClientItem(sbAuthHeader, name, itemValue);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }

    /**
     *
     * @param name
     * @return String
     */
    public java.lang.String loadClientItem(java.lang.String name) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().loadClientItem(sbAuthHeader, name);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param name
     */
    public void deleteClientItem(java.lang.String name) {
        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().deleteClientItem(sbAuthHeader, name);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }

    /**
     *
     * @return edu.mit.ilab.ArrayOfString
     */
    public edu.mit.ilab.ArrayOfString listAllClientItems() {
        edu.mit.ilab.ArrayOfString proxyArrayOfString = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            proxyArrayOfString = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().listAllClientItems(sbAuthHeader);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfString;
    }

    /**
     *
     * @param experimentID
     * @return String
     */
    public java.lang.String retrieveSpecification(int experimentID) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().retrieveSpecification(sbAuthHeader, experimentID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentID
     * @return String
     */
    public java.lang.String retrieveExperimentResult(int experimentID) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().retrieveExperimentResult(sbAuthHeader, experimentID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentID
     * @return String
     */
    public java.lang.String retrieveLabConfiguration(int experimentID) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().retrieveLabConfiguration(sbAuthHeader, experimentID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentID
     * @param annotation
     * @return String
     */
    public java.lang.String saveAnnotation(int experimentID, java.lang.String annotation) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().saveAnnotation(sbAuthHeader, experimentID, annotation);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentID
     * @return String
     */
    public java.lang.String retrieveAnnotation(int experimentID) {
        String value = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().retrieveAnnotation(sbAuthHeader, experimentID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentIDs
     * @return edu.mit.ilab.ArrayOfExperimentInformation
     */
    public edu.mit.ilab.ArrayOfExperimentInformation getExperimentInformation(edu.mit.ilab.ArrayOfInt experimentIDs) {
        edu.mit.ilab.ArrayOfExperimentInformation proxyArrayOfExperimentInformation = null;

        SbAuthHeader sbAuthHeader = this.GetSbAuthHeader();

        try {
            proxyArrayOfExperimentInformation = this.ilabServiceBrokerAppBean.getBatchServiceBrokerHandler().getExperimentInformation(sbAuthHeader, experimentIDs);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfExperimentInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IlabServiceBrokerHandler">
    public void saveClientData(java.lang.String name, java.lang.String itemValue) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().saveClientData(operationAuthHeader, name, itemValue);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }

    /**
     *
     * @param name
     * @return String
     */
    public java.lang.String loadClientData(java.lang.String name) {
        String value = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().loadClientData(operationAuthHeader, name);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param name
     */
    public void deleteClientData(java.lang.String name) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().deleteClientData(operationAuthHeader, name);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }

    /**
     *
     * @return edu.mit.ilab.ilabs.services.ArrayOfString
     */
    public edu.mit.ilab.ilabs.services.ArrayOfString listClientDataItems() {
        edu.mit.ilab.ilabs.services.ArrayOfString proxyArrayOfString = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyArrayOfString = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().listClientDataItems(operationAuthHeader);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfString;
    }

    /**
     *
     * @param startTime
     * @param duration
     * @param labServerGuid
     * @param clientGuid
     * @param groupName
     * @param userName
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus createExperiment(javax.xml.datatype.XMLGregorianCalendar startTime, long duration, java.lang.String labServerGuid, java.lang.String clientGuid, java.lang.String groupName, java.lang.String userName) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyStorageStatus = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().createExperiment(operationAuthHeader, startTime, duration, labServerGuid, clientGuid, groupName, userName);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyStorageStatus;
    }

    /**
     *
     * @param experimentId
     * @param duration
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus openExperiment(long experimentId, long duration) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyStorageStatus = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().openExperiment(operationAuthHeader, experimentId, duration);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyStorageStatus;
    }

    /**
     *
     * @param coupon
     * @param experimentId
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus agentCloseExperiment(edu.mit.ilab.ilabs.type.Coupon coupon, long experimentId) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            proxyStorageStatus = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().agentCloseExperiment(agentAuthHeader, coupon, experimentId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyStorageStatus;
    }

    /**
     *
     * @param experimentId
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus clientCloseExperiment(long experimentId) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyStorageStatus = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().clientCloseExperiment(operationAuthHeader, experimentId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyStorageStatus;
    }

    /**
     *
     * @param experimentID
     * @return edu.mit.ilab.ilabs.type.Experiment
     */
    public edu.mit.ilab.ilabs.type.Experiment retrieveExperiment(long experimentID) {
        edu.mit.ilab.ilabs.type.Experiment proxyExperiment = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyExperiment = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().retrieveExperiment(operationAuthHeader, experimentID);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyExperiment;
    }

    /**
     *
     * @param carray
     * @return edu.mit.ilab.ilabs.services.ArrayOfExperimentSummary
     */
    public edu.mit.ilab.ilabs.services.ArrayOfExperimentSummary retrieveExperimentSummary(edu.mit.ilab.ilabs.services.ArrayOfCriterion carray) {
        edu.mit.ilab.ilabs.services.ArrayOfExperimentSummary proxyArrayOfExperimentSummary = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyArrayOfExperimentSummary = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().retrieveExperimentSummary(operationAuthHeader, carray);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfExperimentSummary;
    }

    /**
     *
     * @param experimentID
     * @param carray
     * @return edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord
     */
    public edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord retrieveExperimentRecords(long experimentID, edu.mit.ilab.ilabs.services.ArrayOfCriterion carray) {
        edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord proxyArrayOfExperimentRecord = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyArrayOfExperimentRecord = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().retrieveExperimentRecords(operationAuthHeader, experimentID, carray);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfExperimentRecord;
    }

    /**
     *
     * @param experimentId
     * @return edu.mit.ilab.ilabs.type.Coupon
     */
    public edu.mit.ilab.ilabs.type.Coupon requestExperimentAccess(long experimentId) {
        edu.mit.ilab.ilabs.type.Coupon proxyCoupon = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyCoupon = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().requestExperimentAccess(operationAuthHeader, experimentId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyCoupon;
    }

    /**
     *
     * @param experimentId
     * @param annotation
     * @return String
     */
    public java.lang.String setAnnotation(int experimentId, java.lang.String annotation) {
        String value = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().setAnnotation(operationAuthHeader, experimentId, annotation);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentId
     * @return String
     */
    public java.lang.String getAnnotation(int experimentId) {
        String value = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().getAnnotation(operationAuthHeader, experimentId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
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
     * @param message
     * @return boolean
     */
    public boolean revokeReservation(java.lang.String serviceBrokerGuid, java.lang.String userName, java.lang.String groupName, java.lang.String labServerGuid, java.lang.String labClientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime, java.lang.String message) {
        boolean value = false;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().revokeReservation(agentAuthHeader, serviceBrokerGuid, userName, groupName, labServerGuid, labClientGuid, startTime, endTime, message);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param types
     * @param duration
     * @param userName
     * @param groupName
     * @param serviceGuid
     * @param clientGuid
     * @return edu.mit.ilab.ilabs.type.Coupon
     */
    public edu.mit.ilab.ilabs.type.Coupon requestAuthorization(edu.mit.ilab.ilabs.services.ArrayOfString types, long duration, java.lang.String userName, java.lang.String groupName, java.lang.String serviceGuid, java.lang.String clientGuid) {
        edu.mit.ilab.ilabs.type.Coupon proxyCoupon = null;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            proxyCoupon = this.ilabServiceBrokerAppBean.getIlabServiceBrokerHandler().requestAuthorization(agentAuthHeader, operationAuthHeader, types, duration, userName, groupName, serviceGuid, clientGuid);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyCoupon;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TicketIssuerHandler">
    /**
     *
     * @param coupon
     * @param type
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return edu.mit.ilab.ilabs.type.Ticket
     */
    public edu.mit.ilab.ilabs.type.Ticket addTicket(edu.mit.ilab.ilabs.type.Coupon coupon, java.lang.String type, java.lang.String redeemerGuid, long duration, java.lang.String payload) {
        edu.mit.ilab.ilabs.type.Ticket proxyTicket = null;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            Ticket ticket = this.ilabServiceBrokerAppBean.getTicketIssuerHandler().addTicket(agentAuthHeader,
                    ConvertTypes.ConvertType(coupon), TicketTypes.ToType(type), redeemerGuid, duration, payload);
            proxyTicket = ConvertTypes.ConvertType(ticket);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyTicket;
    }

    /**
     *
     * @param type
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return edu.mit.ilab.ilabs.type.Coupon
     */
    public edu.mit.ilab.ilabs.type.Coupon createTicket(java.lang.String type, java.lang.String redeemerGuid, long duration, java.lang.String payload) {
        edu.mit.ilab.ilabs.type.Coupon proxyCoupon = null;
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            Coupon coupon = this.ilabServiceBrokerAppBean.getTicketIssuerHandler().createTicket(agentAuthHeader, type, redeemerGuid, duration, payload);
            proxyCoupon = ConvertTypes.ConvertType(coupon);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyCoupon;
    }

    /**
     *
     * @param coupon
     * @param type
     * @param redeemerGuid
     * @return edu.mit.ilab.ilabs.type.Ticket
     */
    public edu.mit.ilab.ilabs.type.Ticket redeemTicket(edu.mit.ilab.ilabs.type.Coupon coupon, java.lang.String type, java.lang.String redeemerGuid) {
        edu.mit.ilab.ilabs.type.Ticket proxyTicket = null;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            Ticket ticket = this.ilabServiceBrokerAppBean.getTicketIssuerHandler().redeemTicket(agentAuthHeader,
                    ConvertTypes.ConvertType(coupon), type, redeemerGuid);
            proxyTicket = ConvertTypes.ConvertType(ticket);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyTicket;
    }

    /**
     *
     * @param coupon
     * @param type
     * @param redeemerGuid
     * @return boolean
     */
    public boolean requestTicketCancellation(edu.mit.ilab.ilabs.type.Coupon coupon, java.lang.String type, java.lang.String redeemerGuid) {
        boolean value = false;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            value = this.ilabServiceBrokerAppBean.getTicketIssuerHandler().requestTicketCancellation(agentAuthHeader,
                    ConvertTypes.ConvertType(coupon), type, redeemerGuid);
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
            Calendar calendar = this.ilabServiceBrokerAppBean.getProcessAgentHandler().getServiceTime();
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
            StatusReport statusReport = this.ilabServiceBrokerAppBean.getProcessAgentHandler().getStatus();
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
            StatusNotificationReport statusNotificationReport = ConvertTypes.ConvertType(report);
            this.ilabServiceBrokerAppBean.getProcessAgentHandler().statusNotification(agentAuthHeader,
                    statusNotificationReport);
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
            value = this.ilabServiceBrokerAppBean.getProcessAgentHandler().cancelTicket(agentAuthHeader,
                    ConvertTypes.ConvertType(coupon), type, redeemer);
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
            ProcessAgent processAgent = this.ilabServiceBrokerAppBean.getProcessAgentHandler().installDomainCredentials(initAuthHeader,
                    ConvertTypes.ConvertType(service), ConvertTypes.ConvertType(inIdentCoupon), ConvertTypes.ConvertType(outIdentCoupon));
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
            value = this.ilabServiceBrokerAppBean.getProcessAgentHandler().modifyDomainCredentials(agentAuthHeader,
                    originalGuid, ConvertTypes.ConvertType(agent), extra, ConvertTypes.ConvertType(inCoupon), ConvertTypes.ConvertType(outCoupon));
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
            value = this.ilabServiceBrokerAppBean.getProcessAgentHandler().removeDomainCredentials(agentAuthHeader,
                    domainGuid, serviceGuid);
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
            value = this.ilabServiceBrokerAppBean.getProcessAgentHandler().modifyProcessAgent(agentAuthHeader,
                    originalGuid, ConvertTypes.ConvertType(agent), extra);
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
            value = this.ilabServiceBrokerAppBean.getProcessAgentHandler().retireProcessAgent(agentAuthHeader,
                    domainGuid, serviceGuid, state);
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
            this.ilabServiceBrokerAppBean.getProcessAgentHandler().register(agentAuthHeader,
                    registerGuid, ConvertTypes.ConvertServices(info));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
    }
    //</editor-fold>

    //================================================================================================================//
    /**
     *
     * @return SbAuthHeader
     */
    private SbAuthHeader GetSbAuthHeader() {
        SbAuthHeader sbAuthHeader = null;

        /*
         * Get the authentication header from the message context
         */
        Object object = wsContext.getMessageContext().get(SbAuthHeader.class.getSimpleName());

        /*
         * Check that it is an SbAuthHeader
         */
        if (object != null && object instanceof SbAuthHeader) {
            sbAuthHeader = (SbAuthHeader) object;
        }

        return sbAuthHeader;
    }

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
