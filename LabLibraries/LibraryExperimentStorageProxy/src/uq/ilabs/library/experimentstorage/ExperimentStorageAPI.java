/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage;

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
import uq.ilabs.experimentstorage.AgentAuthHeader;
import uq.ilabs.experimentstorage.ArrayOfBlob;
import uq.ilabs.experimentstorage.ArrayOfCriterion;
import uq.ilabs.experimentstorage.ArrayOfExperimentRecord;
import uq.ilabs.experimentstorage.ArrayOfExperimentRecord1;
import uq.ilabs.experimentstorage.ArrayOfInt;
import uq.ilabs.experimentstorage.ArrayOfLong;
import uq.ilabs.experimentstorage.ArrayOfRecordAttribute;
import uq.ilabs.experimentstorage.ArrayOfString;
import uq.ilabs.experimentstorage.ExperimentStorageProxy;
import uq.ilabs.experimentstorage.ExperimentStorageProxySoap;
import uq.ilabs.experimentstorage.ObjectFactory;
import uq.ilabs.experimentstorage.OperationAuthHeader;
import uq.ilabs.library.datatypes.Criterion;
import uq.ilabs.library.datatypes.storage.Blob;
import uq.ilabs.library.datatypes.storage.Experiment;
import uq.ilabs.library.datatypes.storage.ExperimentRecord;
import uq.ilabs.library.datatypes.storage.RecordAttribute;
import uq.ilabs.library.datatypes.storage.StorageStatus;
import uq.ilabs.library.datatypes.storage.StorageStatusCodes;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class ExperimentStorageAPI {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentStorageAPI.class.getName();
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
    private ExperimentStorageProxySoap experimentStorageProxy;
    private QName qnameAgentAuthHeader;
    private QName qnameOperationAuthHeader;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String authHeaderAgentGuid;
    private Coupon authHeaderCoupon;

    public void setAuthHeaderAgentGuid(String authHeaderAgentGuid) {
        this.authHeaderAgentGuid = authHeaderAgentGuid;
    }

    public void setAuthHeaderCoupon(Coupon authHeaderCoupon) {
        this.authHeaderCoupon = authHeaderCoupon;
    }
    //</editor-fold>

    public ExperimentStorageAPI(String serviceUrl) throws Exception {
        final String methodName = "ExperimentStorageAPI";
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
            ExperimentStorageProxy webServiceClient = new ExperimentStorageProxy();
            if (webServiceClient == null) {
                throw new NullPointerException(ExperimentStorageProxy.class.getSimpleName());
            }
            this.experimentStorageProxy = webServiceClient.getExperimentStorageProxySoap();
            ((BindingProvider) this.experimentStorageProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

            /*
             * Get authentication header QName
             */
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBElement<AgentAuthHeader> jaxbElementAgentAuthHeader = objectFactory.createAgentAuthHeader(new AgentAuthHeader());
            this.qnameAgentAuthHeader = jaxbElementAgentAuthHeader.getName();
            JAXBElement<OperationAuthHeader> jaxbElementOperationAuthHeader = objectFactory.createOperationAuthHeader(new OperationAuthHeader());
            this.qnameOperationAuthHeader = jaxbElementOperationAuthHeader.getName();

        } catch (NullPointerException | IllegalArgumentException ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @param attributes
     * @return int[]
     */
    public int[] AddAttributes(long experimentId, int sequenceNum, RecordAttribute[] attributes) {
        final String methodName = "AddAttributes";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int[] ints = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfInt arrayOfInt = this.experimentStorageProxy.addAttributes(experimentId, sequenceNum, this.ConvertType(attributes));
            ints = this.ConvertType(arrayOfInt);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return ints;
    }

    /**
     *
     * @param blobId
     * @param experimentId
     * @param sequenceNum
     * @return boolean
     */
    public boolean AddBlobToRecord(long blobId, long experimentId, int sequenceNum) {
        final String methodName = "AddBlobToRecord";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            success = this.experimentStorageProxy.addBlobToRecord(blobId, experimentId, sequenceNum);

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
     * @param experimentId
     * @param submitter
     * @param type
     * @param xmlSearchable
     * @param contents
     * @param attributes
     * @return int
     */
    public int AddRecord(long experimentId, String submitter, String type, boolean xmlSearchable, String contents, RecordAttribute[] attributes) {
        final String methodName = "AddRecord";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.addRecord(experimentId, submitter, type, xmlSearchable, contents, this.ConvertType(attributes));

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
     * @param experimentId
     * @param records
     * @return int
     */
    public int AddRecords(long experimentId, ExperimentRecord[] records) {
        final String methodName = "AddRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.addRecords(experimentId, this.ConvertType(records));

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
     * @param blobId
     * @return int
     */
    public int CancelBlobStorage(long blobId) {
        final String methodName = "CancelBlobStorage";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.cancelBlobStorage(blobId);

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
     * @param experimentId
     * @return StorageStatus
     */
    public StorageStatus CloseExperiment(long experimentId) {
        final String methodName = "CloseExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        StorageStatus storageStatus = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            uq.ilabs.experimentstorage.StorageStatus proxyStorageStatus = this.experimentStorageProxy.closeExperiment(experimentId);
            storageStatus = this.ConvertType(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
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
    public long CreateBlob(long experimentId, String description, int byteCount, String checksum, String checksumAlgorithm) {
        final String methodName = "CreateBlob";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.createBlob(experimentId, description, byteCount, checksum, checksumAlgorithm);

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
     * @param experimentId
     * @return boolean
     */
    public boolean DeleteExperiment(long experimentId) {
        final String methodName = "DeleteExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            success = this.experimentStorageProxy.deleteExperiment(experimentId);

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
     * @param experimentId
     * @param sequenceNum
     * @param attributeIds
     * @return RecordAttribute[]
     */
    public RecordAttribute[] DeleteRecordAttributes(long experimentId, int sequenceNum, int[] attributeIds) {
        final String methodName = "DeleteRecordAttributes";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        RecordAttribute[] recordAttributes = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfRecordAttribute arrayOfRecordAttribute = this.experimentStorageProxy.deleteRecordAttributes(experimentId, sequenceNum, this.ConvertType(attributeIds));
            recordAttributes = this.ConvertType(arrayOfRecordAttribute);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return recordAttributes;
    }

    /**
     *
     * @param blobId
     * @return int
     */
    public int GetBlobAssociation(long blobId) {
        final String methodName = "GetBlobAssociation";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.getBlobAssociation(blobId);

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
     * @param blobId
     * @return long
     */
    public long GetBlobExperiment(long blobId) {
        final String methodName = "GetBlobExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.getBlobExperiment(blobId);

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
     * @param blobId
     * @return int
     */
    public int GetBlobStatus(long blobId) {
        final String methodName = "GetBlobStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.getBlobStatus(blobId);

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
     * @param experimentId
     * @return Blob[]
     */
    public Blob[] GetBlobs(long experimentId) {
        final String methodName = "GetBlobs";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Blob[] blobs = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfBlob arrayOfBlob = this.experimentStorageProxy.getBlobs(experimentId);
            blobs = this.ConvertType(arrayOfBlob);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return blobs;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @return Blob[]
     */
    public Blob[] GetBlobsForRecord(long experimentId, int sequenceNum) {
        final String methodName = "GetBlobsForRecord";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Blob[] blobs = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfBlob arrayOfBlob = this.experimentStorageProxy.getBlobsForRecord(experimentId, sequenceNum);
            blobs = this.ConvertType(arrayOfBlob);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return blobs;
    }

    /**
     *
     * @param experimentId
     * @return Experiment
     */
    public Experiment GetExperiment(long experimentId) {
        final String methodName = "GetExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Experiment experiment = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            uq.ilabs.experimentstorage.Experiment proxyExperiment = this.experimentStorageProxy.getExperiment(experimentId);
            experiment = this.ConvertType(proxyExperiment);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return experiment;
    }

    /**
     *
     * @param expSet
     * @param filter
     * @return long[]
     */
    public long[] GetExperimentIDs(long[] expSet, Criterion[] filter) {
        final String methodName = "GetExperimentIDs";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        long[] longs = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            ArrayOfLong arrayOfLong = this.experimentStorageProxy.getExperimentIDs(this.ConvertType(expSet), this.ConvertType(filter));
            longs = this.ConvertType(arrayOfLong);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return longs;
    }

    /**
     *
     * @param experimentId
     * @return StorageStatus
     */
    public StorageStatus GetExperimentStatus(long experimentId) {
        final String methodName = "GetExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        StorageStatus storageStatus = null;

        try {
            uq.ilabs.experimentstorage.StorageStatus proxyStorageStatus = this.experimentStorageProxy.getExperimentStatus(experimentId);
            storageStatus = this.ConvertType(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param experimentId
     * @return int
     */
    public int GetIdleTime(long experimentId) {
        final String methodName = "GetIdleTime";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.getIdleTime(experimentId);

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
     * @param experimentId
     * @param sequenceNum
     * @return ExperimentRecord
     */
    public ExperimentRecord GetRecord(long experimentId, int sequenceNum) {
        final String methodName = "GetRecord";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ExperimentRecord experimentRecord = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            uq.ilabs.experimentstorage.ExperimentRecord proxyExperimentRecord = this.experimentStorageProxy.getRecord(experimentId, sequenceNum);
            experimentRecord = this.ConvertType(proxyExperimentRecord);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return experimentRecord;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @param attributeIds
     * @return RecordAttribute[]
     */
    public RecordAttribute[] GetRecordAttributes(long experimentId, int sequenceNum, int[] attributeIds) {
        final String methodName = "GetRecordAttributes";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        RecordAttribute[] recordAttributes = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfRecordAttribute arrayOfRecordAttribute = this.experimentStorageProxy.getRecordAttributes(experimentId, sequenceNum, this.ConvertType(attributeIds));
            recordAttributes = this.ConvertType(arrayOfRecordAttribute);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return recordAttributes;
    }

    /**
     *
     * @param experimentId
     * @param sequenceNum
     * @param attributeName
     * @return RecordAttribute[]
     */
    public RecordAttribute[] GetRecordAttributesByName(long experimentId, int sequenceNum, String attributeName) {
        final String methodName = "GetRecordAttributesByName";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        RecordAttribute[] recordAttributes = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfRecordAttribute arrayOfRecordAttribute = this.experimentStorageProxy.getRecordAttributesByName(experimentId, sequenceNum, attributeName);
            recordAttributes = this.ConvertType(arrayOfRecordAttribute);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return recordAttributes;
    }

    /**
     *
     * @param experimentId
     * @param target
     * @return int[]
     */
    public int[] GetRecordIDs(long experimentId, Criterion[] target) {
        final String methodName = "GetRecordIDs";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int[] ints = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfInt arrayOfInt = this.experimentStorageProxy.getRecordIDs(experimentId, this.ConvertType(target));
            ints = this.ConvertType(arrayOfInt);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return ints;
    }

    /**
     *
     * @param experimentId
     * @param target
     * @return ExperimentRecord[]
     */
    public ExperimentRecord[] GetRecords(long experimentId, Criterion[] target) {
        final String methodName = "GetRecords";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        ExperimentRecord[] experimentRecords = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfExperimentRecord arrayOfExperimentRecord = this.experimentStorageProxy.getRecords(experimentId, this.ConvertType(target));
            experimentRecords = this.ConvertType(arrayOfExperimentRecord);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return experimentRecords;
    }

    /**
     *
     * @return String[]
     */
    public String[] GetSupportedBlobExportProtocols() {
        final String methodName = "GetSupportedBlobExportProtocols";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String[] strings = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfString arrayOfString = this.experimentStorageProxy.getSupportedBlobExportProtocols();
            strings = this.ConvertType(arrayOfString);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return strings;
    }

    /**
     *
     * @return String[]
     */
    public String[] GetSupportedBlobImportProtocols() {
        final String methodName = "RequestBlobAccess";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String[] strings = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfString arrayOfString = this.experimentStorageProxy.getSupportedBlobImportProtocols();
            strings = this.ConvertType(arrayOfString);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return strings;
    }

    /**
     *
     * @return String[]
     */
    public String[] GetSupportedChecksumAlgorithms() {
        final String methodName = "GetSupportedChecksumAlgorithms";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String[] strings = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            ArrayOfString arrayOfString = this.experimentStorageProxy.getSupportedChecksumAlgorithms();
            strings = this.ConvertType(arrayOfString);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return strings;
    }

    /**
     *
     * @param experimentId
     * @param duration
     * @return StorageStatus
     */
    public StorageStatus OpenExperiment(long experimentId, long duration) {
        final String methodName = "OpenExperiment";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        StorageStatus storageStatus = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            uq.ilabs.experimentstorage.StorageStatus proxyStorageStatus = this.experimentStorageProxy.openExperiment(experimentId, duration);
            storageStatus = this.ConvertType(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
    }

    /**
     *
     * @param blobId
     * @param protocol
     * @param duration
     * @return String
     */
    public String RequestBlobAccess(long blobId, String protocol, int duration) {
        final String methodName = "RequestBlobAccess";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String retval = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.experimentStorageProxy.requestBlobAccess(blobId, protocol, duration);

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
     * @param blobId
     * @param blobUrl
     * @return boolean
     */
    public boolean RequestBlobStorage(long blobId, String blobUrl) {
        final String methodName = "RequestBlobStorage";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            success = this.experimentStorageProxy.requestBlobStorage(blobId, blobUrl);

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
     * @param experimentId
     * @param statusCode
     * @return StorageStatus
     */
    public StorageStatus SetExperimentStatus(long experimentId, StorageStatusCodes statusCode) {
        final String methodName = "SetExperimentStatus";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        StorageStatus storageStatus = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            uq.ilabs.experimentstorage.StorageStatus proxyStorageStatus = this.experimentStorageProxy.setExperimentStatus(experimentId, statusCode.getValue());
            storageStatus = this.ConvertType(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return storageStatus;
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
        ((BindingProvider) this.experimentStorageProxy).getRequestContext().put(this.qnameAgentAuthHeader.getLocalPart(), agentAuthHeader);
    }

    /**
     *
     */
    private void SetOperationAuthHeader() {
        /*
         * Create authentication header
         */
        OperationAuthHeader operationAuthHeader = new OperationAuthHeader();
        operationAuthHeader.setCoupon(this.ConvertType(this.authHeaderCoupon));

        /*
         * Pass the authentication header to the message handler through the message context
         */
        ((BindingProvider) this.experimentStorageProxy).getRequestContext().put(this.qnameOperationAuthHeader.getLocalPart(), operationAuthHeader);
    }

    /**
     *
     * @param ints
     * @return ArrayOfInt
     */
    private ArrayOfInt ConvertType(int[] ints) {
        ArrayOfInt arrayOfInt = null;

        if (ints != null) {
            arrayOfInt = new ArrayOfInt();
            for (int i = 0; i < ints.length; i++) {
                arrayOfInt.getInt().add(Integer.valueOf(ints[i]));
            }
        }

        return arrayOfInt;
    }

    /**
     *
     * @param arrayOfInt
     * @return int[]
     */
    private int[] ConvertType(ArrayOfInt arrayOfInt) {
        int[] ints = null;

        if (arrayOfInt != null) {
            Integer[] integerArray = arrayOfInt.getInt().toArray(new Integer[0]);
            if (integerArray != null) {
                ints = new int[integerArray.length];
                for (int i = 0; i < integerArray.length; i++) {
                    ints[i] = integerArray[i].intValue();
                }
            }
        }

        return ints;
    }

    /**
     *
     * @param longs
     * @return ArrayOfLong
     */
    private ArrayOfLong ConvertType(long[] longs) {
        ArrayOfLong arrayOfLong = null;

        if (longs != null) {
            arrayOfLong = new ArrayOfLong();
            for (int i = 0; i < longs.length; i++) {
                arrayOfLong.getLong().add(Long.valueOf(longs[i]));
            }
        }

        return arrayOfLong;
    }

    /**
     *
     * @param arrayOfLong
     * @return long[]
     */
    private long[] ConvertType(ArrayOfLong arrayOfLong) {
        long[] longs = null;

        if (arrayOfLong != null) {
            Long[] longArray = arrayOfLong.getLong().toArray(new Long[0]);
            if (longArray != null) {
                longs = new long[longArray.length];
                for (int i = 0; i < longArray.length; i++) {
                    longs[i] = longArray[i].intValue();
                }
            }
        }

        return longs;
    }

    /**
     *
     * @param arrayOfString
     * @return String[]
     */
    private String[] ConvertType(ArrayOfString arrayOfString) {
        String[] strings = null;

        if (arrayOfString != null) {
            strings = arrayOfString.getString().toArray(new String[0]);
        }

        return strings;
    }

    /**
     *
     * @param coupon
     * @return uq.ilabs.experimentstorage.Coupon
     */
    private uq.ilabs.experimentstorage.Coupon ConvertType(Coupon coupon) {
        uq.ilabs.experimentstorage.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new uq.ilabs.experimentstorage.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }

    /**
     *
     * @param proxyStorageStatus
     * @return StorageStatus
     */
    private StorageStatus ConvertType(uq.ilabs.experimentstorage.StorageStatus proxyStorageStatus) {
        StorageStatus storageStatus = null;

        if (proxyStorageStatus != null) {
            storageStatus = new StorageStatus();
            storageStatus.setExperimentId(proxyStorageStatus.getExperimentId());
            storageStatus.setStatusCode(StorageStatusCodes.ToStorageStatusCode(proxyStorageStatus.getStatus()));
            storageStatus.setRecordCount(proxyStorageStatus.getRecordCount());
            storageStatus.setDateCreated(this.ConvertType(proxyStorageStatus.getCreationTime()));
            storageStatus.setDateClosed(this.ConvertType(proxyStorageStatus.getCloseTime()));
            storageStatus.setDateModified(this.ConvertType(proxyStorageStatus.getLastModified()));
            storageStatus.setIssuerGuid(proxyStorageStatus.getIssuerGuid());
        }

        return storageStatus;
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
     * @param proxyExperiment
     * @return Experiment
     */
    private Experiment ConvertType(uq.ilabs.experimentstorage.Experiment proxyExperiment) {
        Experiment experiment = null;

        if (proxyExperiment != null) {
            experiment = new Experiment();
            experiment.setExperimentId(proxyExperiment.getExperimentId());
            experiment.setIssuerGuid(proxyExperiment.getIssuerGuid());
            experiment.setRecords(this.ConvertType(proxyExperiment.getRecords()));
        }

        return experiment;
    }

    /**
     *
     * @param proxyExperimentRecord
     * @return ExperimentRecord
     */
    private ExperimentRecord ConvertType(uq.ilabs.experimentstorage.ExperimentRecord proxyExperimentRecord) {
        ExperimentRecord experimentRecord = null;

        if (proxyExperimentRecord != null) {
            experimentRecord = new ExperimentRecord();
            experimentRecord.setType(proxyExperimentRecord.getType());
            experimentRecord.setSubmitter(proxyExperimentRecord.getSubmitter());
            experimentRecord.setSequenceNum(proxyExperimentRecord.getSequenceNum());
            experimentRecord.setDateCreated(this.ConvertType(proxyExperimentRecord.getTimestamp()));
            experimentRecord.setXmlSearchable(proxyExperimentRecord.isXmlSearchable());
            experimentRecord.setContents(proxyExperimentRecord.getContents());
        }

        return experimentRecord;
    }

    /**
     *
     * @param experimentRecord
     * @return uq.ilabs.experimentstorage.ExperimentRecord
     */
    private uq.ilabs.experimentstorage.ExperimentRecord ConvertType(ExperimentRecord experimentRecord) {
        uq.ilabs.experimentstorage.ExperimentRecord proxyExperimentRecord = null;

        if (experimentRecord != null) {
            proxyExperimentRecord = new uq.ilabs.experimentstorage.ExperimentRecord();
            proxyExperimentRecord.setType(experimentRecord.getType());
            proxyExperimentRecord.setSubmitter(experimentRecord.getSubmitter());
            proxyExperimentRecord.setSequenceNum(experimentRecord.getSequenceNum());
            proxyExperimentRecord.setTimestamp(this.ConvertType(experimentRecord.getDateCreated()));
            proxyExperimentRecord.setXmlSearchable(experimentRecord.isXmlSearchable());
            proxyExperimentRecord.setContents(experimentRecord.getContents());
        }

        return proxyExperimentRecord;
    }

    /**
     *
     * @param experimentRecords
     * @return ArrayOfExperimentRecord
     */
    private ArrayOfExperimentRecord ConvertType(ExperimentRecord[] experimentRecords) {
        ArrayOfExperimentRecord arrayOfExperimentRecord = null;

        if (experimentRecords != null) {
            arrayOfExperimentRecord = new ArrayOfExperimentRecord();
            for (int i = 0; i < experimentRecords.length; i++) {
                arrayOfExperimentRecord.getExperimentRecord().add(this.ConvertType(experimentRecords[i]));
            }
        }

        return arrayOfExperimentRecord;
    }

    /**
     *
     * @param arrayOfExperimentRecord
     * @return ExperimentRecord[]
     */
    private ExperimentRecord[] ConvertType(ArrayOfExperimentRecord arrayOfExperimentRecord) {
        ExperimentRecord[] experimentRecords = null;

        if (arrayOfExperimentRecord != null) {
            uq.ilabs.experimentstorage.ExperimentRecord[] proxyExperimentRecords = arrayOfExperimentRecord.getExperimentRecord().toArray(new uq.ilabs.experimentstorage.ExperimentRecord[0]);
            if (proxyExperimentRecords != null) {
                experimentRecords = new ExperimentRecord[proxyExperimentRecords.length];
                for (int i = 0; i < proxyExperimentRecords.length; i++) {
                    experimentRecords[i] = this.ConvertType(proxyExperimentRecords[i]);
                }
            }
        }

        return experimentRecords;
    }

    /**
     *
     * @param arrayOfExperimentRecord
     * @return ExperimentRecord[]
     */
    private ExperimentRecord[] ConvertType(ArrayOfExperimentRecord1 arrayOfExperimentRecord) {
        ExperimentRecord[] experimentRecords = null;

        if (arrayOfExperimentRecord != null) {
            uq.ilabs.experimentstorage.ExperimentRecord[] proxyExperimentRecords = arrayOfExperimentRecord.getExperimentRecord().toArray(new uq.ilabs.experimentstorage.ExperimentRecord[0]);
            if (proxyExperimentRecords != null) {
                experimentRecords = new ExperimentRecord[proxyExperimentRecords.length];
                for (int i = 0; i < proxyExperimentRecords.length; i++) {
                    experimentRecords[i] = this.ConvertType(proxyExperimentRecords[i]);
                }
            }
        }

        return experimentRecords;
    }

    /**
     *
     * @param criterion
     * @return uq.ilabs.experimentstorage.Criterion
     */
    private uq.ilabs.experimentstorage.Criterion ConvertType(Criterion criterion) {
        uq.ilabs.experimentstorage.Criterion proxyCriterion = null;

        if (criterion != null) {
            proxyCriterion = new uq.ilabs.experimentstorage.Criterion();
            proxyCriterion.setAttribute(criterion.getAttribute());
            proxyCriterion.setPredicate(criterion.getPredicate());
            proxyCriterion.setValue(criterion.getValue());
        }

        return proxyCriterion;
    }

    /**
     *
     * @param criterions
     * @return ArrayOfCriterion
     */
    private ArrayOfCriterion ConvertType(Criterion[] criterions) {
        ArrayOfCriterion arrayOfCriterion = null;

        if (criterions != null) {
            arrayOfCriterion = new ArrayOfCriterion();
            for (int i = 0; i < criterions.length; i++) {
                arrayOfCriterion.getCriterion().add(this.ConvertType(criterions[i]));
            }
        }

        return arrayOfCriterion;
    }

    /**
     *
     * @param recordAttributes
     * @return ArrayOfRecordAttribute
     */
    private ArrayOfRecordAttribute ConvertType(RecordAttribute[] recordAttributes) {
        ArrayOfRecordAttribute arrayOfRecordAttribute = null;

        if (recordAttributes != null) {
            arrayOfRecordAttribute = new ArrayOfRecordAttribute();
            for (int i = 0; i < recordAttributes.length; i++) {
                arrayOfRecordAttribute.getRecordAttribute().add(this.ConvertType(recordAttributes[i]));
            }
        }

        return arrayOfRecordAttribute;
    }

    /**
     *
     * @param recordAttribute
     * @return uq.ilabs.experimentstorage.RecordAttribute
     */
    private uq.ilabs.experimentstorage.RecordAttribute ConvertType(RecordAttribute recordAttribute) {
        uq.ilabs.experimentstorage.RecordAttribute proxyRecordAttribute = null;

        if (recordAttribute != null) {
            proxyRecordAttribute = new uq.ilabs.experimentstorage.RecordAttribute();
            proxyRecordAttribute.setName(recordAttribute.getName());
            proxyRecordAttribute.setValue(recordAttribute.getValue());
        }

        return proxyRecordAttribute;
    }

    /**
     *
     * @param proxyRecordAttribute
     * @return RecordAttribute
     */
    private RecordAttribute ConvertType(uq.ilabs.experimentstorage.RecordAttribute proxyRecordAttribute) {
        RecordAttribute recordAttribute = null;

        if (proxyRecordAttribute != null) {
            recordAttribute = new RecordAttribute();
            recordAttribute.setName(proxyRecordAttribute.getName());
            recordAttribute.setValue(proxyRecordAttribute.getValue());
        }

        return recordAttribute;
    }

    /**
     *
     * @param arrayOfRecordAttribute
     * @return RecordAttribute[]
     */
    private RecordAttribute[] ConvertType(ArrayOfRecordAttribute arrayOfRecordAttribute) {
        RecordAttribute[] recordAttributes = null;

        if (arrayOfRecordAttribute != null) {
            uq.ilabs.experimentstorage.RecordAttribute[] proxyRecordAttributes = arrayOfRecordAttribute.getRecordAttribute().toArray(new uq.ilabs.experimentstorage.RecordAttribute[0]);
            if (proxyRecordAttributes != null) {
                recordAttributes = new RecordAttribute[proxyRecordAttributes.length];
                for (int i = 0; i < proxyRecordAttributes.length; i++) {
                    recordAttributes[i] = this.ConvertType(proxyRecordAttributes[i]);
                }
            }
        }

        return recordAttributes;
    }

    /**
     *
     * @param proxyBlob
     * @return Blob
     */
    private Blob ConvertType(uq.ilabs.experimentstorage.Blob proxyBlob) {
        Blob blob = null;

        if (proxyBlob != null) {
            blob = new Blob();
            blob.setBlobId(proxyBlob.getBlobId());
            blob.setExperimentId(proxyBlob.getExperimentId());
            blob.setRecordNumber(proxyBlob.getRecordNumber());
            blob.setDateCreated(this.ConvertType(proxyBlob.getTimestamp()));
            blob.setDescription(proxyBlob.getDescription());
            blob.setMimeType(proxyBlob.getMimeType());
            blob.setByteCount(proxyBlob.getByteCount());
            blob.setChecksum(proxyBlob.getChecksum());
            blob.setChecksumAlgorithm(proxyBlob.getChecksumAlgorithm());
        }

        return blob;
    }

    /**
     *
     * @param arrayOfBlob
     * @return Blob[]
     */
    private Blob[] ConvertType(ArrayOfBlob arrayOfBlob) {
        Blob[] blobs = null;

        if (arrayOfBlob != null) {
            uq.ilabs.experimentstorage.Blob[] proxyBlobs = arrayOfBlob.getBlob().toArray(new uq.ilabs.experimentstorage.Blob[0]);
            if (proxyBlobs != null) {
                blobs = new Blob[proxyBlobs.length];
                for (int i = 0; i < proxyBlobs.length; i++) {
                    blobs[i] = this.ConvertType(proxyBlobs[i]);
                }
            }
        }

        return blobs;
    }
}
