/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage;

import java.util.logging.Level;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.storage.Blob;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class BlobStorageHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = BlobStorageHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ExperimentId_arg = "ExperimentId: %d";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private Authentication authentication;
    //</editor-fold>

    /**
     *
     * @param serviceManagement
     */
    public BlobStorageHandler(ServiceManagement serviceManagement) {
        this.authentication = new Authentication(serviceManagement);
    }

    public String[] getSupportedBlobImportProtocols(OperationAuthHeader operationAuthHeader) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String[] getSupportedBlobExportProtocols(OperationAuthHeader operationAuthHeader) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String[] getSupportedChecksumAlgorithms(OperationAuthHeader operationAuthHeader) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public long createBlob(OperationAuthHeader operationAuthHeader, long experimentId, String description, int byteCount, String checksum, String checksumAlgorithm) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public long getBlobExperiment(OperationAuthHeader operationAuthHeader, long blobId) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getBlobAssociation(OperationAuthHeader operationAuthHeader, long blobId) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     *
     * @param operationAuthHeader
     * @param experimentId
     * @return Blob[]
     */
    public Blob[] getBlobs(OperationAuthHeader operationAuthHeader, long experimentId) {
        final String methodName = "getBlobs";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName,
                String.format(STRLOG_ExperimentId_arg, experimentId));

        Blob[] blobs = null;

        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return blobs;
    }

    public int getBlobStatus(OperationAuthHeader operationAuthHeader, long blobId) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean requestBlobStorage(OperationAuthHeader operationAuthHeader, long blobId, String blobUrl) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int cancelBlobStorage(OperationAuthHeader operationAuthHeader, long blobId) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String requestBlobAccess(OperationAuthHeader operationAuthHeader, long blobId, String protocol, int duration) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean addBlobToRecord(OperationAuthHeader operationAuthHeader, long blobId, long experimentId, int sequenceNum) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.StoreRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Blob[] getBlobsForRecord(OperationAuthHeader operationAuthHeader, long experimentId, int sequenceNum) {
        this.authentication.RetrieveAndVerify(operationAuthHeader.getCoupon(), TicketTypes.RetrieveRecords);
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
