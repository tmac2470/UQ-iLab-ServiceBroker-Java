/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.usersidescheduling;

import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.scheduling.Reservation;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.usersidescheduling.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
public class UsersideSchedulingHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = UsersideSchedulingHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    //</editor-fold>

    /**
     *
     * @param serviceManagement
     */
    public UsersideSchedulingHandler(ServiceManagement serviceManagement) {
        final String methodName = "UsersideSchedulingHandler";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    public TimePeriod[] retrieveAvailableTimePeriods(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String groupName,
            String labServerGuid, String clientGuid, Calendar proxyStartTime, Calendar proxyEndTime) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Reservation[] listReservations(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String userName,
            String labServerGuid, String labClientGuid, Calendar proxyStartTime, Calendar proxyEndTime) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String addReservation(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String userName, String groupName,
            String labServerGuid, String labClientGuid, Calendar proxyStartTime, Calendar proxyEndTime) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int revokeReservation(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String groupName,
            String labServerGuid, String labClientGuid, Calendar proxyStartTime, Calendar proxyEndTime, String message) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public Reservation redeemReservation(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid,
            String userName, String labServerGuid, String clientGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int addCredentialSet(AgentAuthHeader agentAuthHeader, String serviceBrokerGuid, String serviceBrokerName, String groupName) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int modifyCredentialSet(AgentAuthHeader agentAuthHeader, String serviceBrokerGuid, String serviceBrokerName, String groupName) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int removeCredentialSet(AgentAuthHeader agentAuthHeader, String serviceBrokerGuid, String groupName) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int addExperimentInfo(AgentAuthHeader agentAuthHeader, String labServerGuid, String labServerName,
            String labClientGuid, String labClientName, String labClientVersion, String providerName, String lssGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int modifyExperimentInfo(AgentAuthHeader agentAuthHeader, String labServerGuid, String labServerName,
            String labClientGuid, String labClientName, String labClientVersion, String providerName, String lssGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int removeExperimentInfo(AgentAuthHeader agentAuthHeader, String labServerGuid, String labClientGuid, String lssGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int addLSSInfo(AgentAuthHeader agentAuthHeader, String lssGuid, String lssName, String lssUrl) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int modifyLSSInfo(AgentAuthHeader agentAuthHeader, String lssGuid, String lssName, String lssUrl) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int removeLSSInfo(AgentAuthHeader agentAuthHeader, String lssGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
