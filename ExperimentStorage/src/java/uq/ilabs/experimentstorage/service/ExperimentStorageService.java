/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.experimentstorage.engine.ConfigProperties;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
@WebService(serviceName = "ExperimentStorageService",
        portName = "ExperimentStorageServiceSoap",
        endpointInterface = "edu.mit.ilab.ilabs.services.ExperimentStorageServiceSoap",
        targetNamespace = "http://ilab.mit.edu/iLabs/Services",
        wsdlLocation = "WEB-INF/wsdl/ExperimentStorageService/IExperimentStorageService.asmx.wsdl")
@HandlerChain(file = "ExperimentStorageService_handler.xml")
public class ExperimentStorageService {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    @Resource
    private WebServiceContext wsContext;
    @EJB
    private ExperimentStorageServiceBean experimentStorageServiceBean;
    @EJB
    private ExperimentStorageBean experimentStorageBean;
    @EJB
    private BlobStorageBean blobStorageBean;
    @EJB
    private ProcessAgentBean processAgentBean;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private static boolean initialised = false;
    private static boolean loggerCreated = false;
    private static ConfigProperties configProperties;
    private static ServiceManagement serviceManagement;

    public static boolean isInitialised() {
        return initialised;
    }

    public static void setInitialised(boolean initialised) {
        ExperimentStorageService.initialised = initialised;
    }

    public static boolean isLoggerCreated() {
        return loggerCreated;
    }

    public static void setLoggerCreated(boolean loggerCreated) {
        ExperimentStorageService.loggerCreated = loggerCreated;
    }

    public static ConfigProperties getConfigProperties() {
        return configProperties;
    }

    public static void setConfigProperties(ConfigProperties configProperties) {
        ExperimentStorageService.configProperties = configProperties;
    }

    public static ServiceManagement getServiceManagement() {
        return serviceManagement;
    }

    public static void setServiceManagement(ServiceManagement serviceManagement) {
        ExperimentStorageService.serviceManagement = serviceManagement;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ExperimentStorageBean">
    public edu.mit.ilab.ilabs.type.StorageStatus closeExperiment(long experimentId) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.experimentStorageBean.closeExperiment(agentAuthHeader, experimentId);
    }

    public boolean deleteExperiment(long experimentId) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.experimentStorageBean.deleteExperiment(agentAuthHeader, experimentId);
    }

    public edu.mit.ilab.ilabs.type.StorageStatus openExperiment(long experimentId, long duration) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.experimentStorageBean.openExperiment(agentAuthHeader, experimentId, duration);
    }

    public edu.mit.ilab.ilabs.type.StorageStatus getExperimentStatus(long experimentId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getExperimentStatus(operationAuthHeader, experimentId);
    }

    public edu.mit.ilab.ilabs.type.StorageStatus setExperimentStatus(long experimentId, int statusCode) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.AdministerExperiment);
        return this.experimentStorageBean.setExperimentStatus(operationAuthHeader, experimentId, statusCode);
    }

    public int getIdleTime(long experimentId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getIdleTime(operationAuthHeader, experimentId);
    }

    public int addRecord(long experimentId, java.lang.String submitter, java.lang.String type, boolean xmlSearchable, java.lang.String contents, edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute attributes) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        return this.experimentStorageBean.addRecord(operationAuthHeader, experimentId, submitter, type, xmlSearchable, contents, attributes);
    }

    public int addRecords(long experimentId, edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord records) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        return this.experimentStorageBean.addRecords(operationAuthHeader, experimentId, records);
    }

    public edu.mit.ilab.ilabs.type.ExperimentRecord getRecord(long experimentId, int sequenceNum) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getRecord(operationAuthHeader, experimentId, sequenceNum);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord getRecords(long experimentId, edu.mit.ilab.ilabs.services.ArrayOfCriterion target) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getRecords(operationAuthHeader, experimentId, target);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfInt getRecordIDs(long experimentId, edu.mit.ilab.ilabs.services.ArrayOfCriterion target) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getRecordIDs(operationAuthHeader, experimentId, target);
    }

    public edu.mit.ilab.ilabs.type.Experiment getExperiment(long experimentId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getExperiment(operationAuthHeader, experimentId);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfLong getExperimentIDs(edu.mit.ilab.ilabs.services.ArrayOfLong expSet, edu.mit.ilab.ilabs.services.ArrayOfCriterion filter) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.experimentStorageBean.getExperimentIDs(agentAuthHeader, expSet, filter);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfInt addAttributes(long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute attributes) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        return this.experimentStorageBean.addAttributes(operationAuthHeader, experimentId, sequenceNum, attributes);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute getRecordAttributes(long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfInt attributeIds) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getRecordAttributes(operationAuthHeader, experimentId, sequenceNum, attributeIds);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute getRecordAttributesByName(long experimentId, int sequenceNum, java.lang.String attributeName) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.experimentStorageBean.getRecordAttributesByName(operationAuthHeader, experimentId, sequenceNum, attributeName);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute deleteRecordAttributes(long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfInt attributeIds) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.AdministerExperiment);
        return this.experimentStorageBean.deleteRecordAttributes(operationAuthHeader, experimentId, sequenceNum, attributeIds);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BlobStorageBean">
    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedBlobImportProtocols() {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getSupportedBlobImportProtocols(operationAuthHeader);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedBlobExportProtocols() {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getSupportedBlobExportProtocols(operationAuthHeader);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedChecksumAlgorithms() {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getSupportedChecksumAlgorithms(operationAuthHeader);
    }

    public long createBlob(long experimentId, java.lang.String description, int byteCount, java.lang.String checksum, java.lang.String checksumAlgorithm) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        return this.blobStorageBean.createBlob(operationAuthHeader, experimentId, description, byteCount, checksum, checksumAlgorithm);
    }

    public long getBlobExperiment(long blobId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getBlobExperiment(operationAuthHeader, blobId);
    }

    public int getBlobAssociation(long blobId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getBlobAssociation(operationAuthHeader, blobId);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfBlob getBlobs(long experimentId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getBlobs(operationAuthHeader, experimentId);
    }

    public int getBlobStatus(long blobId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getBlobStatus(operationAuthHeader, blobId);
    }

    public boolean requestBlobStorage(long blobId, java.lang.String blobUrl) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        return this.blobStorageBean.requestBlobStorage(operationAuthHeader, blobId, blobUrl);
    }

    public int cancelBlobStorage(long blobId) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        return this.blobStorageBean.cancelBlobStorage(operationAuthHeader, blobId);
    }

    public java.lang.String requestBlobAccess(long blobId, java.lang.String protocol, int duration) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.requestBlobAccess(operationAuthHeader, blobId, protocol, duration);
    }

    public boolean addBlobToRecord(long blobId, long experimentId, int sequenceNum) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        return this.blobStorageBean.addBlobToRecord(operationAuthHeader, blobId, experimentId, sequenceNum);
    }

    public edu.mit.ilab.ilabs.services.ArrayOfBlob getBlobsForRecord(long experimentId, int sequenceNum) {
        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        this.experimentStorageServiceBean.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        return this.blobStorageBean.getBlobsForRecord(operationAuthHeader, experimentId, sequenceNum);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ProcessAgentBean">
    public javax.xml.datatype.XMLGregorianCalendar getServiceTime() {
        this.GetNoHeader();
        return this.processAgentBean.getServiceTime();
    }

    public edu.mit.ilab.ilabs.type.StatusReport getStatus() {
        this.GetNoHeader();
        return this.processAgentBean.getStatus();
    }

    public void statusNotification(edu.mit.ilab.ilabs.type.StatusNotificationReport report) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        this.processAgentBean.statusNotification(agentAuthHeader, report);
    }

    public boolean cancelTicket(edu.mit.ilab.ilabs.type.Coupon coupon, java.lang.String type, java.lang.String redeemer) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.processAgentBean.cancelTicket(agentAuthHeader, coupon, type, redeemer);
    }

    public edu.mit.ilab.ilabs.type.ProcessAgent installDomainCredentials(edu.mit.ilab.ilabs.type.ProcessAgent service, edu.mit.ilab.ilabs.type.Coupon inIdentCoupon, edu.mit.ilab.ilabs.type.Coupon outIdentCoupon) {
        InitAuthHeader initAuthHeader = this.GetInitAuthHeader();
        return this.processAgentBean.installDomainCredentials(initAuthHeader, service, inIdentCoupon, outIdentCoupon);
    }

    public int modifyDomainCredentials(java.lang.String originalGuid, edu.mit.ilab.ilabs.type.ProcessAgent agent, java.lang.String extra, edu.mit.ilab.ilabs.type.Coupon inCoupon, edu.mit.ilab.ilabs.type.Coupon outCoupon) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.processAgentBean.modifyDomainCredentials(agentAuthHeader, originalGuid, agent, extra, inCoupon, outCoupon);
    }

    public int removeDomainCredentials(java.lang.String domainGuid, java.lang.String serviceGuid) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.processAgentBean.removeDomainCredentials(agentAuthHeader, domainGuid, serviceGuid);
    }

    public int modifyProcessAgent(java.lang.String originalGuid, edu.mit.ilab.ilabs.type.ProcessAgent agent, java.lang.String extra) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.processAgentBean.modifyProcessAgent(agentAuthHeader, originalGuid, agent, extra);
    }

    public int retireProcessAgent(java.lang.String domainGuid, java.lang.String serviceGuid, boolean state) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        return this.processAgentBean.retireProcessAgent(agentAuthHeader, domainGuid, serviceGuid, state);
    }

    public void register(java.lang.String registerGuid, edu.mit.ilab.ilabs.services.ArrayOfServiceDescription info) {
        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();
        this.processAgentBean.register(agentAuthHeader, registerGuid, info);
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
        String qname = this.experimentStorageServiceBean.getQnameAgentAuthHeaderLocalPart();
        Object object = wsContext.getMessageContext().get(qname);

        /*
         * Check that it is an AgentAuthHeader
         */
        if (object != null && object instanceof edu.mit.ilab.ilabs.type.AgentAuthHeader) {
            edu.mit.ilab.ilabs.type.AgentAuthHeader proxyAgentAuthHeader = (edu.mit.ilab.ilabs.type.AgentAuthHeader) object;
            agentAuthHeader = new AgentAuthHeader();
            agentAuthHeader.setAgentGuid(proxyAgentAuthHeader.getAgentGuid());
            agentAuthHeader.setCoupon(ConvertTypes.Convert(proxyAgentAuthHeader.getCoupon()));
        }

        /*
         * Check AgentAuthHeader authentication - throws a SOAPFaultException if it fails
         */
        this.experimentStorageServiceBean.Authenticate(agentAuthHeader);

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
        String qname = this.experimentStorageServiceBean.getQnameInitAuthHeaderLocalPart();
        Object object = wsContext.getMessageContext().get(qname);

        /*
         * Check that it is an InitAuthHeader
         */
        if (object != null && object instanceof edu.mit.ilab.ilabs.type.InitAuthHeader) {
            edu.mit.ilab.ilabs.type.InitAuthHeader proxyInitAuthHeader = (edu.mit.ilab.ilabs.type.InitAuthHeader) object;
            initAuthHeader = new InitAuthHeader();
            initAuthHeader.setInitPasskey(proxyInitAuthHeader.getInitPasskey());
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
        String qname = this.experimentStorageServiceBean.getQnameOperationAuthHeaderLocalPart();
        Object object = wsContext.getMessageContext().get(qname);

        /*
         * Check that it is an OperationAuthHeader
         */
        if (object != null && object instanceof edu.mit.ilab.ilabs.type.OperationAuthHeader) {
            edu.mit.ilab.ilabs.type.OperationAuthHeader proxyOperationAuthHeader = (edu.mit.ilab.ilabs.type.OperationAuthHeader) object;
            operationAuthHeader = new OperationAuthHeader();
            operationAuthHeader.setCoupon(ConvertTypes.Convert(proxyOperationAuthHeader.getCoupon()));
        }

        return operationAuthHeader;
    }

    /**
     *
     */
    private void GetNoHeader() {
        /*
         * Get a Qname from ExperimentStorageServiceBean to make sure that it has initialised
         */
        this.experimentStorageServiceBean.getQnameOperationAuthHeaderLocalPart();
    }
}
