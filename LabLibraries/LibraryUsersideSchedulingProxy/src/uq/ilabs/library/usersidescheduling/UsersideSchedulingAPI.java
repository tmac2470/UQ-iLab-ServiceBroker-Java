/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.usersidescheduling;

import edu.mit.ilab.ilabs.usersidescheduling.proxy.UsersideSchedulingProxy;
import edu.mit.ilab.ilabs.usersidescheduling.proxy.UsersideSchedulingProxySoap;
import java.util.Calendar;
import java.util.logging.Level;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.scheduling.Reservation;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import java.util.Map;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;

/**
 *
 * @author uqlpayne
 */
public class UsersideSchedulingAPI {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = UsersideSchedulingAPI.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ServiceUrl_arg = "ServiceUrl: '%s'";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "serviceUrl";
    private static final String STRERR_UsersideSchedulingUnaccessible = "UsersideScheduling is unaccessible!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private UsersideSchedulingProxySoap usersideSchedulingProxy;
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

    /**
     *
     * @param serviceUrl
     * @throws Exception
     */
    public UsersideSchedulingAPI(String serviceUrl) throws Exception {
        final String methodName = "UsersideSchedulingAPI";
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
            UsersideSchedulingProxy webServiceClient = new UsersideSchedulingProxy();
            this.usersideSchedulingProxy = webServiceClient.getUsersideSchedulingProxySoap();
            ((BindingProvider) this.usersideSchedulingProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        } catch (NullPointerException | IllegalArgumentException ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param serviceBrokerName
     * @param groupName
     * @return int
     */
    public int AddCredentialSet(String serviceBrokerGuid, String serviceBrokerName, String groupName) {
        final String methodName = "AddCredentialSet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.addCredentialSet(serviceBrokerGuid, serviceBrokerName, groupName);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param labServerGuid
     * @param labServerName
     * @param labClientGuid
     * @param labClientName
     * @param labClientVersion
     * @param providerName
     * @param lssGuid
     * @return int
     */
    public int AddExperimentInfo(String labServerGuid, String labServerName, String labClientGuid, String labClientName, String labClientVersion, String providerName, String lssGuid) {
        final String methodName = "AddExperimentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.addExperimentInfo(labServerGuid, labServerName, labClientGuid, labClientName, labClientVersion, providerName, lssGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param lssGuid
     * @param lssName
     * @param lssUrl
     * @return int
     */
    public int AddLSSInfo(String lssGuid, String lssName, String lssUrl) {
        final String methodName = "AddLSSInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.addLSSInfo(lssGuid, lssName, lssUrl);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
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
     * @return String
     */
    public String AddReservation(String serviceBrokerGuid, String userName, String groupName, String labServerGuid, String labClientGuid, Calendar startTime, Calendar endTime) {
        final String methodName = "AddReservation";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String retval = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.usersideSchedulingProxy.addReservation(serviceBrokerGuid, userName, groupName, labServerGuid, labClientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param userName
     * @param labServerGuid
     * @param labClientGuid
     * @param startTime
     * @param endTime
     * @return Reservation[]
     */
    public Reservation[] ListReservations(String serviceBrokerGuid, String userName, String labServerGuid, String labClientGuid, Calendar startTime, Calendar endTime) {
        final String methodName = "ListReservations";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Reservation reservations[] = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            edu.mit.ilab.ilabs.usersidescheduling.proxy.ArrayOfReservation proxyArrayOfReservation = this.usersideSchedulingProxy.listReservations(
                    serviceBrokerGuid, userName, labServerGuid, labClientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
            reservations = ConvertTypes.Convert(proxyArrayOfReservation);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return reservations;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param serviceBrokerName
     * @param groupName
     * @return int
     */
    public int ModifyCredentialSet(String serviceBrokerGuid, String serviceBrokerName, String groupName) {
        final String methodName = "ModifyCredentialSet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.modifyCredentialSet(serviceBrokerGuid, serviceBrokerName, groupName);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param labServerGuid
     * @param labServerName
     * @param labClientGuid
     * @param labClientName
     * @param labClientVersion
     * @param providerName
     * @param lssGuid
     * @return int
     */
    public int ModifyExperimentInfo(String labServerGuid, String labServerName, String labClientGuid, String labClientName, String labClientVersion, String providerName, String lssGuid) {
        final String methodName = "ModifyExperimentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.modifyExperimentInfo(labServerGuid, labServerName, labClientGuid, labClientName, labClientVersion, providerName, lssGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param lssGuid
     * @param lssName
     * @param lssUrl
     * @return int
     */
    public int ModifyLSSInfo(String lssGuid, String lssName, String lssUrl) {
        final String methodName = "ModifyLSSInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.modifyLSSInfo(lssGuid, lssName, lssUrl);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param userName
     * @param labServerGuid
     * @param clientGuid
     * @return Reservation
     */
    public Reservation redeemReservation(String serviceBrokerGuid, String userName, String labServerGuid, String clientGuid) {
        final String methodName = "AddCredentialSet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Reservation reservation = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            edu.mit.ilab.ilabs.usersidescheduling.proxy.Reservation proxyReservation = this.usersideSchedulingProxy.redeemReservation(serviceBrokerGuid, userName, labServerGuid, clientGuid);
            reservation = ConvertTypes.Convert(proxyReservation);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return reservation;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @return int
     */
    public int RemoveCredentialSet(String serviceBrokerGuid, String groupName) {
        final String methodName = "RemoveCredentialSet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.removeCredentialSet(serviceBrokerGuid, groupName);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param labServerGuid
     * @param labClientGuid
     * @param lssGuid
     * @return int
     */
    public int RemoveExperimentInfo(String labServerGuid, String labClientGuid, String lssGuid) {
        final String methodName = "RemoveExperimentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.removeExperimentInfo(labServerGuid, labClientGuid, lssGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param lssGuid
     * @return int
     */
    public int RemoveLSSInfo(String lssGuid) {
        final String methodName = "RemoveLSSInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.usersideSchedulingProxy.removeLSSInfo(lssGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return TimePeriod[]
     */
    public TimePeriod[] RetrieveAvailableTimePeriods(String serviceBrokerGuid, String groupName, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        final String methodName = "RetrieveAvailableTimePeriods";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        TimePeriod timePeriods[] = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            edu.mit.ilab.ilabs.usersidescheduling.proxy.ArrayOfTimePeriod proxyArrayOfTimePeriod = this.usersideSchedulingProxy.retrieveAvailableTimePeriods(
                    serviceBrokerGuid, groupName, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
            timePeriods = ConvertTypes.Convert(proxyArrayOfTimePeriod);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return timePeriods;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param labServerGuid
     * @param labClientGuid
     * @param startTime
     * @param endTime
     * @param message
     * @return int
     */
    public int RevokeReservation(String serviceBrokerGuid, String groupName, String labServerGuid, String labClientGuid, Calendar startTime, Calendar endTime, String message) {
        final String methodName = "RevokeReservation";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.usersideSchedulingProxy.revokeReservation(
                    serviceBrokerGuid, groupName, labServerGuid, labClientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime), message);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_UsersideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
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
        agentAuthHeader.setCoupon(this.authHeaderCoupon);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        Map<String, Object> requestContext = ((BindingProvider) this.usersideSchedulingProxy).getRequestContext();
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
        operationAuthHeader.setCoupon(this.authHeaderCoupon);

        /*
         * Pass the authentication header to the message handler through the message context
         */
        Map<String, Object> requestContext = ((BindingProvider) this.usersideSchedulingProxy).getRequestContext();
        requestContext.put(OperationAuthHeader.class.getSimpleName(), operationAuthHeader);
    }

    /**
     *
     */
    private void UnsetAgentAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.usersideSchedulingProxy).getRequestContext();
        requestContext.remove(AgentAuthHeader.class.getSimpleName());
    }

    /**
     *
     */
    private void UnsetOperationAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.usersideSchedulingProxy).getRequestContext();
        requestContext.remove(OperationAuthHeader.class.getSimpleName());
    }
}
