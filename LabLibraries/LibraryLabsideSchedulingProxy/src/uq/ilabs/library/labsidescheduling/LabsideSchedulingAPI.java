/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.labsidescheduling;

import edu.mit.ilab.ilabs.labsidescheduling.proxy.LabsideSchedulingProxy;
import edu.mit.ilab.ilabs.labsidescheduling.proxy.LabsideSchedulingProxySoap;
import java.util.Calendar;
import java.util.logging.Level;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Map;
import uq.ilabs.library.datatypes.scheduling.TimePeriod;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.service.OperationAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;

/**
 *
 * @author uqlpayne
 */
public class LabsideSchedulingAPI {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = LabsideSchedulingAPI.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ServiceUrl_arg = "ServiceUrl: '%s'";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "serviceUrl";
    private static final String STRERR_LabsideSchedulingUnaccessible = "LabsideScheduling is unaccessible!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private LabsideSchedulingProxySoap labsideSchedulingProxy;
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
    public LabsideSchedulingAPI(String serviceUrl) throws Exception {
        final String methodName = "LabsideSchedulingAPI";
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
            LabsideSchedulingProxy webServiceClient = new LabsideSchedulingProxy();
            this.labsideSchedulingProxy = webServiceClient.getLabsideSchedulingProxySoap();
            ((BindingProvider) this.labsideSchedulingProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

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
     * @param ussGuid
     * @return int
     */
    public int AddCredentialSet(String serviceBrokerGuid, String serviceBrokerName, String groupName, String ussGuid) {
        final String methodName = "AddCredentialSet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.addCredentialSet(serviceBrokerGuid, serviceBrokerName, groupName, ussGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
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
     * @param clientGuid
     * @param clientName
     * @param clientVersion
     * @param providerName
     * @return int
     */
    public int AddExperimentInfo(String labServerGuid, String labServerName, String clientGuid, String clientName, String clientVersion, String providerName) {
        final String methodName = "AddExperimentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.addExperimentInfo(labServerGuid, labServerName, clientGuid, clientName, clientVersion, providerName);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param ussGuid
     * @param ussName
     * @param ussUrl
     * @param coupon
     * @return int
     */
    public int AddUSSInfo(String ussGuid, String ussName, String ussUrl, Coupon coupon) {
        final String methodName = "AddUSSInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.addUSSInfo(ussGuid, ussName, ussUrl, ConvertTypes.Convert(coupon));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
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
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return String
     */
    public String ConfirmReservation(String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        final String methodName = "ConfirmReservation";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        String retval = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.labsideSchedulingProxy.confirmReservation(
                    serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param serviceBrokerName
     * @param groupName
     * @param ussGuid
     * @return int
     */
    public int ModifyCredentialSet(String serviceBrokerGuid, String serviceBrokerName, String groupName, String ussGuid) {
        final String methodName = "ModifyCredentialSet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.modifyCredentialSet(serviceBrokerGuid, serviceBrokerName, groupName, ussGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
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
     * @param clientGuid
     * @param clientName
     * @param clientVersion
     * @param providerName
     * @return int
     */
    public int ModifyExperimentInfo(String labServerGuid, String labServerName, String clientGuid, String clientName, String clientVersion, String providerName) {
        final String methodName = "ModifyExperimentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.modifyExperimentInfo(labServerGuid, labServerName, clientGuid, clientName, clientVersion, providerName);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param ussGuid
     * @param ussName
     * @param ussUrl
     * @param coupon
     * @return int
     */
    public int ModifyUSSInfo(String ussGuid, String ussName, String ussUrl, Coupon coupon) {
        final String methodName = "ModifyUSSInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.modifyUSSInfo(ussGuid, ussName, ussUrl, ConvertTypes.Convert(coupon));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
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
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return int
     */
    public int RedeemReservation(String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        final String methodName = "RedeemReservation";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.labsideSchedulingProxy.redeemReservation(
                    serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param serviceBrokerGuid
     * @param groupName
     * @param ussGuid
     * @return int
     */
    public int RemoveCredentialSet(String serviceBrokerGuid, String groupName, String ussGuid) {
        final String methodName = "RemoveCredentialSet";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.removeCredentialSet(serviceBrokerGuid, groupName, ussGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
        } finally {
            this.UnsetAgentAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param labServerGuid
     * @param clientGuid
     * @return int
     */
    public int RemoveExperimentInfo(String labServerGuid, String clientGuid) {
        final String methodName = "RemoveExperimentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.removeExperimentInfo(labServerGuid, clientGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
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
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return int
     */
    public int RemoveReservation(String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        final String methodName = "RemoveReservation";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            retval = this.labsideSchedulingProxy.removeReservation(
                    serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return retval;
    }

    /**
     *
     * @param ussGuid
     * @return int
     */
    public int RemoveUSSInfo(String ussGuid) {
        final String methodName = "RemoveUSSInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        int retval = -1;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetAgentAuthHeader();
            retval = this.labsideSchedulingProxy.removeUSSInfo(ussGuid);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
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
     * @param ussGuid
     * @param labServerGuid
     * @param clientGuid
     * @param startTime
     * @param endTime
     * @return TimePeriod[]
     */
    public TimePeriod[] RetrieveAvailableTimePeriods(String serviceBrokerGuid, String groupName, String ussGuid, String labServerGuid, String clientGuid, Calendar startTime, Calendar endTime) {
        final String methodName = "RetrieveAvailableTimePeriods";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        TimePeriod timePeriods[] = null;

        try {
            /*
             * Set authentication information and call the web service
             */
            this.SetOperationAuthHeader();
            edu.mit.ilab.ilabs.labsidescheduling.proxy.ArrayOfTimePeriod proxyArrayOfTimePeriod = this.labsideSchedulingProxy.retrieveAvailableTimePeriods(
                    serviceBrokerGuid, groupName, ussGuid, labServerGuid, clientGuid, ConvertTypes.Convert(startTime), ConvertTypes.Convert(endTime));
            timePeriods = ConvertTypes.Convert(proxyArrayOfTimePeriod);

        } catch (SOAPFaultException ex) {
            Logfile.Write(ex.getMessage());
            throw new WebServiceException(ex.getFault().getFaultString());
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
            throw new WebServiceException(STRERR_LabsideSchedulingUnaccessible);
        } finally {
            this.UnsetOperationAuthHeader();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return timePeriods;
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
        Map<String, Object> requestContext = ((BindingProvider) this.labsideSchedulingProxy).getRequestContext();
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
        Map<String, Object> requestContext = ((BindingProvider) this.labsideSchedulingProxy).getRequestContext();
        requestContext.put(OperationAuthHeader.class.getSimpleName(), operationAuthHeader);
    }

    /**
     *
     */
    private void UnsetAgentAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.labsideSchedulingProxy).getRequestContext();
        requestContext.remove(AgentAuthHeader.class.getSimpleName());
    }

    /**
     *
     */
    private void UnsetOperationAuthHeader() {
        Map<String, Object> requestContext = ((BindingProvider) this.labsideSchedulingProxy).getRequestContext();
        requestContext.remove(OperationAuthHeader.class.getSimpleName());
    }
}
