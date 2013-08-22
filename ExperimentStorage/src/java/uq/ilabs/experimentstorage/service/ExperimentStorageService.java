/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

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
import uq.ilabs.experimentstorage.ExperimentStorageAppBean;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.InitAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.storage.Blob;
import uq.ilabs.library.datatypes.storage.Experiment;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.RecordAttribute;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.lab.utilities.Logfile;

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
    private ExperimentStorageAppBean experimentStorageAppBean;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ExperimentStorageHandler">
    /**
     *
     * @param experimentId
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus closeExperiment(long experimentId) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            StorageStatus storageStatus = this.experimentStorageAppBean.getExperimentStorageHandler().closeExperiment(agentAuthHeader, experimentId);
            proxyStorageStatus = ConvertTypes.ConvertType(storageStatus);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyStorageStatus;
    }

    /**
     *
     * @param experimentId
     * @return boolean
     */
    public boolean deleteExperiment(long experimentId) {
        boolean success = false;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            success = this.experimentStorageAppBean.getExperimentStorageHandler().deleteExperiment(agentAuthHeader, experimentId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return success;
    }

    /**
     *
     * @param experimentId
     * @param duration
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus openExperiment(long experimentId, long duration) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            StorageStatus storageStatus = this.experimentStorageAppBean.getExperimentStorageHandler().
                    openExperiment(agentAuthHeader, experimentId, duration);
            proxyStorageStatus = ConvertTypes.ConvertType(storageStatus);
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
    public edu.mit.ilab.ilabs.type.StorageStatus getExperimentStatus(long experimentId) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            StorageStatus storageStatus = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getExperimentStatus(operationAuthHeader, experimentId);
            proxyStorageStatus = ConvertTypes.ConvertType(storageStatus);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }
        return proxyStorageStatus;
    }

    /**
     *
     * @param experimentId
     * @param statusCode
     * @return edu.mit.ilab.ilabs.type.StorageStatus
     */
    public edu.mit.ilab.ilabs.type.StorageStatus setExperimentStatus(long experimentId, int statusCode) {
        edu.mit.ilab.ilabs.type.StorageStatus proxyStorageStatus = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            StorageStatus storageStatus = this.experimentStorageAppBean.getExperimentStorageHandler().
                    setExperimentStatus(operationAuthHeader, experimentId, statusCode);
            proxyStorageStatus = ConvertTypes.ConvertType(storageStatus);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyStorageStatus;
    }

    /**
     *
     * @param experimentId
     * @return int
     */
    public int getIdleTime(long experimentId) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getIdleTime(operationAuthHeader, experimentId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentId
     * @param submitter
     * @param type
     * @param xmlSearchable
     * @param contents
     * @param attributes
     * @return int
     */
    public int addRecord(long experimentId, java.lang.String submitter, java.lang.String type, boolean xmlSearchable, java.lang.String contents, edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute attributes) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getExperimentStorageHandler().
                    addRecord(operationAuthHeader, experimentId, submitter, type, xmlSearchable, contents, ConvertTypes.ConvertServices(attributes));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentId
     * @param records
     * @return int
     */
    public int addRecords(long experimentId, edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord records) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getExperimentStorageHandler().
                    addRecords(operationAuthHeader, experimentId, ConvertTypes.ConvertServices(records));
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @return edu.mit.ilab.ilabs.type.ExperimentRecord
     */
    public edu.mit.ilab.ilabs.type.ExperimentRecord getRecord(long experimentId, int sequenceNum) {
        edu.mit.ilab.ilabs.type.ExperimentRecord proxyExperimentRecord = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            ExperimentRecord experimentRecord = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getRecord(operationAuthHeader, experimentId, sequenceNum);
            proxyExperimentRecord = ConvertTypes.ConvertType(experimentRecord);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyExperimentRecord;
    }

    /**
     *
     * @param experimentId
     * @param target
     * @return edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord
     */
    public edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord getRecords(long experimentId, edu.mit.ilab.ilabs.services.ArrayOfCriterion target) {
        edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord arrayOfExperimentRecord = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            ExperimentRecord[] experimentRecord = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getRecords(operationAuthHeader, experimentId, ConvertTypes.ConvertServices(target));
            arrayOfExperimentRecord = ConvertTypes.ConvertServices(experimentRecord);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfExperimentRecord;
    }

    /**
     *
     * @param experimentId
     * @param target
     * @return edu.mit.ilab.ilabs.services.ArrayOfInt
     */
    public edu.mit.ilab.ilabs.services.ArrayOfInt getRecordIDs(long experimentId, edu.mit.ilab.ilabs.services.ArrayOfCriterion target) {
        edu.mit.ilab.ilabs.services.ArrayOfInt arrayOfInt = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            int[] recordIds = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getRecordIDs(operationAuthHeader, experimentId, ConvertTypes.ConvertServices(target));
            arrayOfInt = ConvertTypes.ConvertServices(recordIds);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfInt;
    }

    /**
     *
     * @param experimentId
     * @return edu.mit.ilab.ilabs.type.Experiment
     */
    public edu.mit.ilab.ilabs.type.Experiment getExperiment(long experimentId) {
        edu.mit.ilab.ilabs.type.Experiment proxyExperiment = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            Experiment experiment = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getExperiment(operationAuthHeader, experimentId);
            proxyExperiment = ConvertTypes.ConvertType(experiment);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyExperiment;
    }

    /**
     *
     * @param expSet
     * @param filter
     * @return edu.mit.ilab.ilabs.services.ArrayOfLong
     */
    public edu.mit.ilab.ilabs.services.ArrayOfLong getExperimentIDs(edu.mit.ilab.ilabs.services.ArrayOfLong expSet, edu.mit.ilab.ilabs.services.ArrayOfCriterion filter) {
        edu.mit.ilab.ilabs.services.ArrayOfLong arrayOfLong = null;

        AgentAuthHeader agentAuthHeader = this.GetAgentAuthHeader();

        try {
            long[] experimentIds = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getExperimentIDs(agentAuthHeader,
                    ConvertTypes.ConvertServices(expSet), ConvertTypes.ConvertServices(filter));
            arrayOfLong = ConvertTypes.ConvertServices(experimentIds);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfLong;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @param attributes
     * @return edu.mit.ilab.ilabs.services.ArrayOfInt
     */
    public edu.mit.ilab.ilabs.services.ArrayOfInt addAttributes(long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute attributes) {
        edu.mit.ilab.ilabs.services.ArrayOfInt arrayOfInt = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            int[] attributeIds = this.experimentStorageAppBean.getExperimentStorageHandler().
                    addAttributes(operationAuthHeader,
                    experimentId, sequenceNum, ConvertTypes.ConvertServices(attributes));
            arrayOfInt = ConvertTypes.ConvertServices(attributeIds);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfInt;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @param attributeIds
     * @return edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute
     */
    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute getRecordAttributes(long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfInt attributeIds) {
        edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute arrayOfRecordAttribute = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            RecordAttribute[] recordAttributes = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getRecordAttributes(operationAuthHeader, experimentId, sequenceNum, ConvertTypes.ConvertServices(attributeIds));
            arrayOfRecordAttribute = ConvertTypes.ConvertServices(recordAttributes);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfRecordAttribute;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @param attributeName
     * @return edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute
     */
    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute getRecordAttributesByName(long experimentId, int sequenceNum, java.lang.String attributeName) {
        edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute arrayOfRecordAttribute = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            RecordAttribute[] recordAttributes = this.experimentStorageAppBean.getExperimentStorageHandler().
                    getRecordAttributesByName(operationAuthHeader, experimentId, sequenceNum, attributeName);
            arrayOfRecordAttribute = ConvertTypes.ConvertServices(recordAttributes);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfRecordAttribute;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @param attributeIds
     * @return edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute
     */
    public edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute deleteRecordAttributes(long experimentId, int sequenceNum, edu.mit.ilab.ilabs.services.ArrayOfInt attributeIds) {
        edu.mit.ilab.ilabs.services.ArrayOfRecordAttribute arrayOfRecordAttribute = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            RecordAttribute[] recordAttributes = this.experimentStorageAppBean.getExperimentStorageHandler().
                    deleteRecordAttributes(operationAuthHeader, experimentId, sequenceNum, ConvertTypes.ConvertServices(attributeIds));
            arrayOfRecordAttribute = ConvertTypes.ConvertServices(recordAttributes);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return arrayOfRecordAttribute;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BlobStorageHandler">
    /**
     *
     * @return edu.mit.ilab.ilabs.services.ArrayOfString
     */
    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedBlobImportProtocols() {
        edu.mit.ilab.ilabs.services.ArrayOfString proxyArrayOfString = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            String[] strings = this.experimentStorageAppBean.getBlobStorageHandler().getSupportedBlobImportProtocols(operationAuthHeader);
            proxyArrayOfString = ConvertTypes.ConvertServices(strings);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfString;
    }

    /**
     *
     * @return edu.mit.ilab.ilabs.services.ArrayOfString
     */
    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedBlobExportProtocols() {
        edu.mit.ilab.ilabs.services.ArrayOfString proxyArrayOfString = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            String[] strings = this.experimentStorageAppBean.getBlobStorageHandler().
                    getSupportedBlobExportProtocols(operationAuthHeader);
            proxyArrayOfString = ConvertTypes.ConvertServices(strings);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfString;
    }

    /**
     *
     * @return edu.mit.ilab.ilabs.services.ArrayOfString
     */
    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedChecksumAlgorithms() {
        edu.mit.ilab.ilabs.services.ArrayOfString proxyArrayOfString = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            String[] strings = this.experimentStorageAppBean.getBlobStorageHandler().
                    getSupportedChecksumAlgorithms(operationAuthHeader);
            proxyArrayOfString = ConvertTypes.ConvertServices(strings);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfString;
    }

    /**
     *
     * @param experimentId
     * @param description
     * @param byteCount
     * @param checksum
     * @param checksumAlgorithm
     * @return long
     */
    public long createBlob(long experimentId, java.lang.String description, int byteCount, java.lang.String checksum, java.lang.String checksumAlgorithm) {
        long value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    createBlob(operationAuthHeader, experimentId, description, byteCount, checksum, checksumAlgorithm);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param blobId
     * @return long
     */
    public long getBlobExperiment(long blobId) {
        long value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    getBlobExperiment(operationAuthHeader, blobId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param blobId
     * @return int
     */
    public int getBlobAssociation(long blobId) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    getBlobAssociation(operationAuthHeader, blobId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentId
     * @return edu.mit.ilab.ilabs.services.ArrayOfBlob
     */
    public edu.mit.ilab.ilabs.services.ArrayOfBlob getBlobs(long experimentId) {
        edu.mit.ilab.ilabs.services.ArrayOfBlob proxyArrayOfBlob = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            Blob[] blobs = this.experimentStorageAppBean.getBlobStorageHandler().
                    getBlobs(operationAuthHeader, experimentId);
            proxyArrayOfBlob = ConvertTypes.ConvertServices(blobs);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfBlob;
    }

    /**
     *
     * @param blobId
     * @return int
     */
    public int getBlobStatus(long blobId) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    getBlobStatus(operationAuthHeader, blobId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param blobId
     * @param blobUrl
     * @return boolean
     */
    public boolean requestBlobStorage(long blobId, java.lang.String blobUrl) {
        boolean value = false;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    requestBlobStorage(operationAuthHeader, blobId, blobUrl);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param blobId
     * @return int
     */
    public int cancelBlobStorage(long blobId) {
        int value = -1;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    cancelBlobStorage(operationAuthHeader, blobId);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param blobId
     * @param protocol
     * @param duration
     * @return java.lang.String
     */
    public java.lang.String requestBlobAccess(long blobId, java.lang.String protocol, int duration) {
        String value = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();
        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    requestBlobAccess(operationAuthHeader, blobId, protocol, duration);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param blobId
     * @param experimentId
     * @param sequenceNum
     * @return boolean
     */
    public boolean addBlobToRecord(long blobId, long experimentId, int sequenceNum) {
        boolean value = false;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            value = this.experimentStorageAppBean.getBlobStorageHandler().
                    addBlobToRecord(operationAuthHeader, blobId, experimentId, sequenceNum);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return value;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @return edu.mit.ilab.ilabs.services.ArrayOfBlob
     */
    public edu.mit.ilab.ilabs.services.ArrayOfBlob getBlobsForRecord(long experimentId, int sequenceNum) {
        edu.mit.ilab.ilabs.services.ArrayOfBlob proxyArrayOfBlob = null;

        OperationAuthHeader operationAuthHeader = this.GetOperationAuthHeader();

        try {
            Blob[] blobs = this.experimentStorageAppBean.getBlobStorageHandler().
                    getBlobsForRecord(operationAuthHeader, experimentId, sequenceNum);
            proxyArrayOfBlob = ConvertTypes.ConvertServices(blobs);
        } catch (Exception ex) {
            this.ThrowSOAPFault(ex.getMessage());
        }

        return proxyArrayOfBlob;
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
            Calendar calendar = this.experimentStorageAppBean.getProcessAgentHandler().
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
            StatusReport statusReport = this.experimentStorageAppBean.getProcessAgentHandler().
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
            this.experimentStorageAppBean.getProcessAgentHandler().
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
            value = this.experimentStorageAppBean.getProcessAgentHandler().
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
            ProcessAgent processAgent = this.experimentStorageAppBean.getProcessAgentHandler().
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
            value = this.experimentStorageAppBean.getProcessAgentHandler().
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
            value = this.experimentStorageAppBean.getProcessAgentHandler().
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
            value = this.experimentStorageAppBean.getProcessAgentHandler().
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
            value = this.experimentStorageAppBean.getProcessAgentHandler().
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
            this.experimentStorageAppBean.getProcessAgentHandler().
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
