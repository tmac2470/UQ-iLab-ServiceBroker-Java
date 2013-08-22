/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.labsidescheduling;

import java.util.Calendar;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.labsidescheduling.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
public class LabsideSchedulingHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = LabsideSchedulingHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    //</editor-fold>

    /**
     *
     * @param serviceManagement
     */
    public LabsideSchedulingHandler(ServiceManagement serviceManagement) {
        final String methodName = "LabsideSchedulingHandler";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            //
        } catch (Exception ex) {
            Logfile.WriteException(STR_ClassName, methodName, ex);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    public TimePeriod[] retrieveAvailableTimePeriods(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public String confirmReservation(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int redeemReservation(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int removeReservation(OperationAuthHeader operationAuthHeader, String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int addUSSInfo(AgentAuthHeader agentAuthHeader, String ussGuid, String ussName, String ussUrl, Coupon coupon) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int modifyUSSInfo(AgentAuthHeader agentAuthHeader, String ussGuid, String ussName, String ussUrl, Coupon coupon) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int removeUSSInfo(AgentAuthHeader agentAuthHeader, String ussGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int addCredentialSet(AgentAuthHeader agentAuthHeader, String serviceBrokerGuid, String serviceBrokerName, String groupName, String ussGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int modifyCredentialSet(AgentAuthHeader agentAuthHeader, String serviceBrokerGuid, String serviceBrokerName, String groupName, String ussGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int removeCredentialSet(AgentAuthHeader agentAuthHeader, String serviceBrokerGuid, String groupName, String ussGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int addExperimentInfo(AgentAuthHeader agentAuthHeader, String labServerGuid, String labServerName, String clientGuid, String clientName, String clientVersion, String providerName) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int modifyExperimentInfo(AgentAuthHeader agentAuthHeader, String labServerGuid, String labServerName, String clientGuid, String clientName, String clientVersion, String providerName) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int removeExperimentInfo(AgentAuthHeader agentAuthHeader, String labServerGuid, String clientGuid) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
