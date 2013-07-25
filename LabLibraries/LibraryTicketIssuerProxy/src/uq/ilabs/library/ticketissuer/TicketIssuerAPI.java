/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.ticketissuer;

import java.util.logging.Level;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.soap.SOAPFaultException;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.ticketissuer.AgentAuthHeader;
import uq.ilabs.ticketissuer.ObjectFactory;
import uq.ilabs.ticketissuer.TicketIssuerProxy;
import uq.ilabs.ticketissuer.TicketIssuerProxySoap;

/**
 *
 * @author uqlpayne
 */
public class TicketIssuerAPI {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = TicketIssuerAPI.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_ServiceUrl_arg = "ServiceUrl: '%s'";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "serviceUrl";
    private static final String STRERR_MessageRetries_arg2 = "%s - Retries: %d";
    private static final String STRERR_TicketIssuerUnaccessible = "TicketIssuer is unaccessible!";
    /*
     * Constants
     */
    private static int INT_RetryCount = 3;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private TicketIssuerProxySoap ticketIssuerProxy;
    private QName qnameAgentAuthHeader;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String authHeaderAgentGuid;
    private Coupon authHeaderCoupon;
    private int retryCount;

    public void setAuthHeaderAgentGuid(String authHeaderAgentGuid) {
        this.authHeaderAgentGuid = authHeaderAgentGuid;
    }

    public void setAuthHeaderCoupon(Coupon authHeaderCoupon) {
        this.authHeaderCoupon = authHeaderCoupon;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    //</editor-fold>

    /**
     *
     * @param serviceUrl
     * @throws Exception
     */
    public TicketIssuerAPI(String serviceUrl) throws Exception {
        final String methodName = "TicketIssuerAPI";
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
            TicketIssuerProxy webServiceClient = new TicketIssuerProxy();
            this.ticketIssuerProxy = webServiceClient.getTicketIssuerProxySoap();
            ((BindingProvider) this.ticketIssuerProxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

            /*
             * Get authentication header QName
             */
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBElement<AgentAuthHeader> jaxbElementAgentAuthHeader = objectFactory.createAgentAuthHeader(new AgentAuthHeader());
            this.qnameAgentAuthHeader = jaxbElementAgentAuthHeader.getName();

            /*
             * Initialise local variables
             */
            this.retryCount = INT_RetryCount;

        } catch (NullPointerException | IllegalArgumentException ex) {
            Logfile.WriteError(ex.toString());
            throw ex;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param coupon
     * @param type
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return boolean
     */
    public boolean AddTicket(Coupon coupon, String type, String redeemerGuid, long duration, String payload) {
        final String methodName = "AddTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        int retries = this.retryCount;
        while (true) {
            try {
                /*
                 * Set authentication information and call the web service
                 */
                this.SetAgentAuthHeader();
                success = this.ticketIssuerProxy.addTicket(ConvertTypes.ConvertType(coupon), type, redeemerGuid, duration, payload);
                break;

            } catch (SOAPFaultException ex) {
                Logfile.Write(ex.getMessage());
                throw new ProtocolException(ex.getFault().getFaultString());
            } catch (Exception ex) {
                Logfile.WriteError(String.format(STRERR_MessageRetries_arg2, ex.toString(), retries));
                if (--retries == 0) {
                    throw new ProtocolException(STRERR_TicketIssuerUnaccessible);
                }
            } finally {
                this.UnsetAgentAuthHeader();
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    /**
     *
     * @param type
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return Coupon
     */
    public Coupon CreateTicket(String type, String redeemerGuid, long duration, String payload) {
        final String methodName = "CreateTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Coupon coupon = null;

        int retries = this.retryCount;
        while (true) {
            try {
                /*
                 * Set authentication information and call the web service
                 */
                this.SetAgentAuthHeader();
                uq.ilabs.ticketissuer.Coupon proxyCoupon = this.ticketIssuerProxy.createTicket(type, redeemerGuid, duration, payload);
                coupon = ConvertTypes.ConvertType(proxyCoupon);
                break;

            } catch (SOAPFaultException ex) {
                Logfile.Write(ex.getMessage());
                throw new ProtocolException(ex.getFault().getFaultString());
            } catch (Exception ex) {
                Logfile.WriteError(String.format(STRERR_MessageRetries_arg2, ex.toString(), retries));
                if (--retries == 0) {
                    throw new ProtocolException(STRERR_TicketIssuerUnaccessible);
                }
            } finally {
                this.UnsetAgentAuthHeader();
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return coupon;
    }

    /**
     *
     * @param coupon
     * @param type
     * @param redeemerGuid
     * @return Ticket
     */
    public Ticket RedeemTicket(Coupon coupon, TicketTypes ticketType, String redeemerGuid) {
        final String methodName = "RedeemTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        Ticket ticket = null;

        int retries = this.retryCount;
        while (true) {
            try {
                /*
                 * Set authentication information and call the web service
                 */
                this.SetAgentAuthHeader();
                uq.ilabs.ticketissuer.Ticket proxyTicket =
                        this.ticketIssuerProxy.redeemTicket(ConvertTypes.ConvertType(coupon), ticketType.toString(), redeemerGuid);
                ticket = ConvertTypes.ConvertType(proxyTicket);
                break;

            } catch (SOAPFaultException ex) {
                Logfile.Write(ex.getMessage());
                throw new ProtocolException(ex.getFault().getFaultString());
            } catch (Exception ex) {
                Logfile.WriteError(String.format(STRERR_MessageRetries_arg2, ex.toString(), retries));
                if (--retries == 0) {
                    throw new ProtocolException(STRERR_TicketIssuerUnaccessible);
                }
            } finally {
                this.UnsetAgentAuthHeader();
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return ticket;
    }

    /**
     *
     * @param coupon
     * @param type
     * @param redeemerGuid
     * @return boolean
     */
    public boolean RequestTicketCancellation(Coupon coupon, String type, String redeemerGuid) {
        final String methodName = "RequestTicketCancellation";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        int retries = this.retryCount;
        while (true) {
            try {
                /*
                 * Set authentication information and call the web service
                 */
                this.SetAgentAuthHeader();
                success = this.ticketIssuerProxy.requestTicketCancellation(ConvertTypes.ConvertType(coupon), type, redeemerGuid);
                break;

            } catch (SOAPFaultException ex) {
                Logfile.Write(ex.getMessage());
                throw new ProtocolException(ex.getFault().getFaultString());
            } catch (Exception ex) {
                Logfile.WriteError(String.format(STRERR_MessageRetries_arg2, ex.toString(), retries));
                if (--retries == 0) {
                    throw new ProtocolException(STRERR_TicketIssuerUnaccessible);
                }
            } finally {
                this.UnsetAgentAuthHeader();
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
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
        agentAuthHeader.setCoupon(ConvertTypes.ConvertType(this.authHeaderCoupon));

        /*
         * Pass the authentication header to the message handler through the message context
         */
        ((BindingProvider) this.ticketIssuerProxy).getRequestContext().put(this.qnameAgentAuthHeader.getLocalPart(), agentAuthHeader);
    }

    /**
     *
     */
    private void UnsetAgentAuthHeader() {
        ((BindingProvider) this.ticketIssuerProxy).getRequestContext().remove(this.qnameAgentAuthHeader.getLocalPart());
    }
}
