/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker;

import java.util.logging.Level;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.servicebroker.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
public class IlabServiceBrokerHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = IlabServiceBrokerHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    //</editor-fold>

    public IlabServiceBrokerHandler(ServiceManagement serviceManagement) {
        final String methodName = "IlabServiceBrokerHandler";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.serviceManagement = serviceManagement;

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    public void saveClientData(OperationAuthHeader operationAuthHeader, String name, String itemValue) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String loadClientData(OperationAuthHeader operationAuthHeader, String name) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void deleteClientData(OperationAuthHeader operationAuthHeader, String name) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfString listClientDataItems(OperationAuthHeader operationAuthHeader) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.StorageStatus createExperiment(OperationAuthHeader operationAuthHeader, javax.xml.datatype.XMLGregorianCalendar startTime, long duration, String labServerGuid, String clientGuid, String groupName, String userName) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.StorageStatus openExperiment(OperationAuthHeader operationAuthHeader, long experimentId, long duration) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.StorageStatus agentCloseExperiment(AgentAuthHeader agentAuthHeader, edu.mit.ilab.ilabs.type.Coupon coupon, long experimentId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.StorageStatus clientCloseExperiment(OperationAuthHeader operationAuthHeader, long experimentId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.Experiment retrieveExperiment(OperationAuthHeader operationAuthHeader, long experimentID) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfExperimentSummary retrieveExperimentSummary(OperationAuthHeader operationAuthHeader, edu.mit.ilab.ilabs.services.ArrayOfCriterion carray) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.services.ArrayOfExperimentRecord retrieveExperimentRecords(OperationAuthHeader operationAuthHeader, long experimentID, edu.mit.ilab.ilabs.services.ArrayOfCriterion carray) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.Coupon requestExperimentAccess(OperationAuthHeader operationAuthHeader, long experimentId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String setAnnotation(OperationAuthHeader operationAuthHeader, int experimentId, String annotation) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String getAnnotation(OperationAuthHeader operationAuthHeader, int experimentId) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public boolean revokeReservation(AgentAuthHeader agentAuthHeader, String serviceBrokerGuid, String userName, String groupName, String labServerGuid, String labClientGuid, javax.xml.datatype.XMLGregorianCalendar startTime, javax.xml.datatype.XMLGregorianCalendar endTime, String message) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public edu.mit.ilab.ilabs.type.Coupon requestAuthorization(AgentAuthHeader agentAuthHeader, OperationAuthHeader operationAuthHeader, edu.mit.ilab.ilabs.services.ArrayOfString types, long duration, String userName, String groupName, String serviceGuid, String clientGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
