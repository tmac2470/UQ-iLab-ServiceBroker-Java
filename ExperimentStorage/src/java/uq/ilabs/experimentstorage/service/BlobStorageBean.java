/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

import java.util.logging.Level;
import javax.ejb.Singleton;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
@Singleton
public class BlobStorageBean {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = BlobStorageBean.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ExperimentId_arg = "ExperimentId: %d";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    //</editor-fold>

    /**
     * Constructor - Seems that this gets called when the project is deployed which is unexpected. To get around this,
     * check to see if the service has been initialized and this class has been initialized. Can't do logging until the
     * service has been initialized and the logger created.
     */
    public BlobStorageBean() {
        final String methodName = "BlobStorageBean";

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
                 * Create an instance of ...
                 */
            } catch (Exception ex) {
                Logfile.WriteError(ex.toString());
                throw ex;
            }

            Logfile.WriteCompleted(STR_ClassName, methodName);
        }
    }

    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedBlobImportProtocols(OperationAuthHeader operationAuthHeader) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedBlobExportProtocols(OperationAuthHeader operationAuthHeader) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfString getSupportedChecksumAlgorithms(OperationAuthHeader operationAuthHeader) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public long createBlob(OperationAuthHeader operationAuthHeader, long experimentId, String description, int byteCount, String checksum, String checksumAlgorithm) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public long getBlobExperiment(OperationAuthHeader operationAuthHeader, long blobId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getBlobAssociation(OperationAuthHeader operationAuthHeader, long blobId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @return edu.mit.ilab.ilabs.services.ArrayOfBlob
     */
    public edu.mit.ilab.ilabs.services.ArrayOfBlob getBlobs(OperationAuthHeader operationAuthHeader, long experimentId) {
        final String methodName = "getBlobs";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        edu.mit.ilab.ilabs.services.ArrayOfBlob proxyBlobs = null;

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return proxyBlobs;
    }

    public int getBlobStatus(OperationAuthHeader operationAuthHeader, long blobId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean requestBlobStorage(OperationAuthHeader operationAuthHeader, long blobId, String blobUrl) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int cancelBlobStorage(OperationAuthHeader operationAuthHeader, long blobId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String requestBlobAccess(OperationAuthHeader operationAuthHeader, long blobId, String protocol, int duration) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean addBlobToRecord(OperationAuthHeader operationAuthHeader, long blobId, long experimentId, int sequenceNum) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfBlob getBlobsForRecord(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
