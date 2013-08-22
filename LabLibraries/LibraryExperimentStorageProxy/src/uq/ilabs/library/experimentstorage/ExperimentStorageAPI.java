/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage;

import edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentStorageProxy;
import edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentStorageProxySoap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.Criterion;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
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
    private static final String STRERR_ExperimentStorageUnaccessible = "ExperimentStorage is unaccessible!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ExperimentStorageProxySoap experimentStorageProxy;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String agentAuthHeaderAgentGuid;
    private Coupon agentAuthHeaderCoupon;
    private Coupon operationAuthHeaderCoupon;

    public void setAgentAuthHeaderAgentGuid(String agentAuthHeaderAgentGuid) {
        this.agentAuthHeaderAgentGuid = agentAuthHeaderAgentGuid;
    }

    public void setAgentAuthHeaderCoupon(Coupon agentAuthHeaderCoupon) {
        this.agentAuthHeaderCoupon = agentAuthHeaderCoupon;
    }

    public void setOperationAuthHeaderCoupon(Coupon operationAuthHeaderCoupon) {
        this.operationAuthHeaderCoupon = operationAuthHeaderCoupon;
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
            this.experimentStorageProxy = webServiceClient.getExperimentStorageProxySoap();
            ((BindingProvider) this.experimentStorageProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfInt proxyArrayOfInt =
                    this.experimentStorageProxy.addAttributes(experimentId, sequenceNum, ConvertTypes.Convert(attributes));
            ints = ConvertTypes.Convert(proxyArrayOfInt);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            retval = this.experimentStorageProxy.addRecord(experimentId, submitter, type, xmlSearchable, contents, ConvertTypes.Convert(attributes));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            retval = this.experimentStorageProxy.addRecords(experimentId, ConvertTypes.Convert(records));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.StorageStatus proxyStorageStatus =
                    this.experimentStorageProxy.closeExperiment(experimentId);
            storageStatus = ConvertTypes.Convert(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfRecordAttribute proxyArrayOfRecordAttribute =
                    this.experimentStorageProxy.deleteRecordAttributes(experimentId, sequenceNum, ConvertTypes.Convert(attributeIds));
            recordAttributes = ConvertTypes.Convert(proxyArrayOfRecordAttribute);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfBlob proxyArrayOfBlob =
                    this.experimentStorageProxy.getBlobs(experimentId);
            blobs = ConvertTypes.Convert(proxyArrayOfBlob);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfBlob proxyArrayOfBlob =
                    this.experimentStorageProxy.getBlobsForRecord(experimentId, sequenceNum);
            blobs = ConvertTypes.Convert(proxyArrayOfBlob);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.Experiment proxyExperiment =
                    this.experimentStorageProxy.getExperiment(experimentId);
            experiment = ConvertTypes.Convert(proxyExperiment);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfLong proxyArrayOfLong =
                    this.experimentStorageProxy.getExperimentIDs(ConvertTypes.Convert(expSet), ConvertTypes.Convert(filter));
            longs = ConvertTypes.Convert(proxyArrayOfLong);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
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
            /*
             * Set the authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            edu.mit.ilab.ilabs.experimentstorage.proxy.StorageStatus proxyStorageStatus =
                    this.experimentStorageProxy.getExperimentStatus(experimentId);
            storageStatus = ConvertTypes.Convert(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ExperimentRecord proxyExperimentRecord =
                    this.experimentStorageProxy.getRecord(experimentId, sequenceNum);
            experimentRecord = ConvertTypes.Convert(proxyExperimentRecord);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfRecordAttribute proxyArrayOfRecordAttribute =
                    this.experimentStorageProxy.getRecordAttributes(experimentId, sequenceNum, ConvertTypes.Convert(attributeIds));
            recordAttributes = ConvertTypes.Convert(proxyArrayOfRecordAttribute);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfRecordAttribute proxyArrayOfRecordAttribute =
                    this.experimentStorageProxy.getRecordAttributesByName(experimentId, sequenceNum, attributeName);
            recordAttributes = ConvertTypes.Convert(proxyArrayOfRecordAttribute);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfInt proxyArrayOfInt =
                    this.experimentStorageProxy.getRecordIDs(experimentId, ConvertTypes.Convert(target));
            ints = ConvertTypes.Convert(proxyArrayOfInt);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfExperimentRecord proxyArrayOfExperimentRecord =
                    this.experimentStorageProxy.getRecords(experimentId, ConvertTypes.Convert(target));
            experimentRecords = ConvertTypes.Convert(proxyArrayOfExperimentRecord);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfString proxyArrayOfString =
                    this.experimentStorageProxy.getSupportedBlobExportProtocols();
            strings = ConvertTypes.Convert(proxyArrayOfString);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfString proxyArrayOfString =
                    this.experimentStorageProxy.getSupportedBlobImportProtocols();
            strings = ConvertTypes.Convert(proxyArrayOfString);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.ArrayOfString proxyArrayOfString =
                    this.experimentStorageProxy.getSupportedChecksumAlgorithms();
            strings = ConvertTypes.Convert(proxyArrayOfString);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.StorageStatus proxyStorageStatus =
                    this.experimentStorageProxy.openExperiment(experimentId, duration);
            storageStatus = ConvertTypes.Convert(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
            edu.mit.ilab.ilabs.experimentstorage.proxy.StorageStatus proxyStorageStatus =
                    this.experimentStorageProxy.setExperimentStatus(experimentId, statusCode.getValue());
            storageStatus = ConvertTypes.Convert(proxyStorageStatus);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_ExperimentStorageUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
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
        agentAuthHeader.setAgentGuid(this.agentAuthHeaderAgentGuid);
        agentAuthHeader.setCoupon(this.agentAuthHeaderCoupon);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        Map<String, Object> requestContext = ((BindingProvider) this.experimentStorageProxy).getRequestContext();
        requestContext.put(AgentAuthHeader.class.getSimpleName(), agentAuthHeader);
    }

    /**
     *
     */
    private void SetOperationAuthHeader() {
        /*
         * Create authentication header
         */
        OperationAuthHeader operationAuthHeader = new OperationAuthHeader();
        operationAuthHeader.setCoupon(this.operationAuthHeaderCoupon);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        Map<String, Object> requestContext = ((BindingProvider) this.experimentStorageProxy).getRequestContext();
        requestContext.put(OperationAuthHeader.class.getSimpleName(), operationAuthHeader);
    }

    /**
     *
     */
    private void UnsetAgentAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.experimentStorageProxy).getRequestContext();
        requestContext.remove(AgentAuthHeader.class.getSimpleName());
    }

    /**
     *
     */
    private void UnsetOperationAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.experimentStorageProxy).getRequestContext();
        requestContext.remove(OperationAuthHeader.class.getSimpleName());
    }
}
