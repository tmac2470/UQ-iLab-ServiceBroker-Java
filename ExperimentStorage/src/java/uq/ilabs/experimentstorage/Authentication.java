/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage;

import java.util.logging.Level;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.experimentstorage.engine.ServiceManagement;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.Ticketing;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.ticketissuer.TicketIssuerAPI;

/**
 *
 * @author uqlpayne
 */
public class Authentication {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = Authentication.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for exception messages
     */
    private static final String STRERR_AccessDenied_arg = "ExperimentStorageService Access Denied: %s";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_Invalid_arg = "%s: Invalid!";
    private static final String STRERR_AuthHeader = "AuthHeader";
    private static final String STRERR_AgentGuid = "AgentGuid";
    private static final String STRERR_Coupon = "Coupon";
    private static final String STRERR_CouponId = "CouponId";
    private static final String STRERR_Ticket_arg = "Ticket %s: ";
    private static final String STRERR_TicketInvalid_arg = STRERR_Ticket_arg + "Invalid!";
    private static final String STRERR_TicketCancelled_arg = STRERR_Ticket_arg + "Cancelled!";
    private static final String STRERR_TicketExpired_arg = STRERR_Ticket_arg + "Expired!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    //</editor-fold>

    /**
     * Creates a new instance of Authentication
     */
    public Authentication(ServiceManagement serviceManagement) {
        this.serviceManagement = serviceManagement;
    }

    /**
     *
     * @param agentAuthHeader
     * @return Coupon
     */
    public boolean Authenticate(AgentAuthHeader agentAuthHeader) {

        boolean success = false;

        try {
            /*
             * Check that the AgentAuthHeader is specified
             */
            if (agentAuthHeader == null) {
                throw new NullPointerException(String.format(STRERR_NotSpecified_arg, STRERR_AuthHeader));
            }

            /*
             * Check that AgentGuid is specified
             */
            if (agentAuthHeader.getAgentGuid() == null) {
                throw new NullPointerException(String.format(STRERR_NotSpecified_arg, STRERR_AgentGuid));
            }

            /*
             * Check that Coupon is specified
             */
            if (agentAuthHeader.getCoupon() == null) {
                throw new NullPointerException(String.format(STRERR_NotSpecified_arg, STRERR_Coupon));
            }

            /*
             * Check that CouponId is valid
             */
            long couponId = agentAuthHeader.getCoupon().getCouponId();
            if (couponId <= 0) {
                throw new IllegalArgumentException(String.format(STRERR_Invalid_arg, STRERR_CouponId));
            }

            /*
             * Retrieve the coupon for this CouponId and AgentGuid and compare with the AuthHeader coupon
             */
            Coupon retrievedCoupon = this.serviceManagement.getTicketing().RetrieveCoupon(couponId, agentAuthHeader.getAgentGuid());
            if (retrievedCoupon != null && retrievedCoupon.equals(agentAuthHeader.getCoupon())) {
                success = true;
            }
        } catch (NullPointerException | IllegalArgumentException ex) {
            String message = String.format(STRERR_AccessDenied_arg, ex.getMessage());
            Logfile.WriteError(message);
            throw new RuntimeException(message);
        }

        return success;
    }

    /**
     *
     * @param coupon
     * @param ticketType
     * @param redeemerGuid
     * @return Ticket
     */
    public boolean RetrieveAndVerify(Coupon coupon, TicketTypes ticketType) {
        final String methodName = "RetrieveAndVerify";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        boolean success = false;

        try {
            /*
             * Check if ticket exists locally
             */
            Ticketing ticketing = this.serviceManagement.getTicketing();
            String redeemerGuid = this.serviceManagement.getServiceGuid();
            Ticket ticket = ticketing.RetrieveTicket(coupon, ticketType, redeemerGuid);
            if (ticket == null) {
                /*
                 * Get the service url for the ticket issuer
                 */
                ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveByGuid(coupon.getIssuerGuid());
                if (processAgentInfo != null) {
                    /*
                     * Get the outgoing coupon for the ticket issuer
                     */
                    Coupon outCoupon = ticketing.RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId(), coupon.getIssuerGuid());

                    /*
                     * Get ticket from ticket issuer
                     */
                    TicketIssuerAPI ticketIssuerAPI = new TicketIssuerAPI(processAgentInfo.getServiceUrl());
                    ticketIssuerAPI.setAuthHeaderAgentGuid(redeemerGuid);
                    ticketIssuerAPI.setAuthHeaderCoupon(outCoupon);
                    ticket = ticketIssuerAPI.RedeemTicket(coupon, ticketType, redeemerGuid);
                    if (ticket == null) {
                        throw new NullPointerException(String.format(STRERR_TicketInvalid_arg, ticketType.name()));
                    }

                    /*
                     * Check if ticket is valid
                     */
                    if (ticket.isCancelled() == true) {
                        throw new IllegalArgumentException(String.format(STRERR_TicketCancelled_arg, ticketType.name()));
                    }
                    if (ticket.isExpired() == true) {
                        throw new IllegalArgumentException(String.format(STRERR_TicketExpired_arg, ticketType.name()));
                    }

                    /*
                     * Check if a coupon for this ticket exists locally
                     */
                    Coupon localCoupon = ticketing.RetrieveCoupon(coupon.getCouponId(), coupon.getIssuerGuid());
                    if (localCoupon == null) {
                        /*
                         * Create a local coupon
                         */
                        ticketing.CreateCoupon(coupon.getCouponId(), coupon.getIssuerGuid(), coupon.getPasskey());
                    }
                    /*
                     * Create a local ticket
                     */
                    ticketing.CreateTicket(ticket);
                }
            }

            success = true;

        } catch (Exception ex) {
            String message = String.format(STRERR_AccessDenied_arg, ex.getMessage());
            Logfile.WriteError(message);
            throw new RuntimeException(message);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }
}
