/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker;

import java.util.logging.Level;
import uq.ilabs.library.datatypes.service.AgentAuthHeader;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.servicebroker.engine.ServiceManagement;
import uq.ilabs.library.ticketissuer.TicketIssuerAPI;

/**
 *
 * @author uqlpayne
 */
public class TicketIssuerHandler {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = TicketIssuerHandler.class.getName();
    private static final Level logLevel = Level.INFO;
    /*
     * String constants for exception messages
     */
    private static final String STRERR_AccessDenied_arg = "TicketIssuerService Access Denied: %s";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_Invalid_arg = " %s: Invalid!";
    private static final String STRERR_AgentAuthHeader = "AgentAuthHeader";
    private static final String STRERR_ProcessAgentGuid = "ProcessAgentGuid";
    private static final String STRERR_ProcessAgentCoupon = "ProcessAgentCoupon";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceManagement serviceManagement;
    //</editor-fold>

    /**
     *
     * @param serviceManagement
     */
    public TicketIssuerHandler(ServiceManagement serviceManagement) {
        this.serviceManagement = serviceManagement;
    }

    /**
     *
     * @param agentAuthHeader
     * @param coupon
     * @param ticketType
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return Ticket
     */
    public Ticket addTicket(AgentAuthHeader agentAuthHeader, Coupon coupon, TicketTypes ticketType, String redeemerGuid, long duration, String payload) {
        final String STR_MethodName = "addTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, STR_MethodName);

        Ticket ticket = null;

        this.Authenticate(agentAuthHeader);

        try {
            ticket = this.serviceManagement.getTicketIssuer().AddTicket(coupon, ticketType, agentAuthHeader.getAgentGuid(), redeemerGuid, duration, payload);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, STR_MethodName);

        return ticket;
    }

    /**
     *
     * @param agentAuthHeader
     * @param type
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return Coupon
     */
    public Coupon createTicket(AgentAuthHeader agentAuthHeader, String type, String redeemerGuid, long duration, String payload) {
        final String STR_MethodName = "createTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, STR_MethodName);

        Coupon coupon = null;

        this.Authenticate(agentAuthHeader);

        try {
            TicketTypes ticketType = TicketTypes.ToType(type);
            coupon = this.serviceManagement.getTicketIssuer().CreateTicket(ticketType, agentAuthHeader.getAgentGuid(), redeemerGuid, duration, payload);
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, STR_MethodName);

        return coupon;
    }

    /**
     *
     * @param agentAuthHeader
     * @param proxyCoupon
     * @param type
     * @param redeemerGuid
     * @return Ticket
     */
    public Ticket redeemTicket(AgentAuthHeader agentAuthHeader, Coupon coupon, String type, String redeemerGuid) {
        final String STR_MethodName = "redeemTicket";
        Logfile.WriteCalled(logLevel, STR_ClassName, STR_MethodName);

        Ticket ticket = null;

        this.Authenticate(agentAuthHeader);

        try {
            String issuerGuid = coupon.getIssuerGuid();
            TicketTypes ticketType = TicketTypes.ToType(type);

            /*
             * Check if this is the issuer of the ticket
             */
            if (this.serviceManagement.getServiceGuid().equals(issuerGuid) == true) {
                ticket = this.serviceManagement.getTicketIssuer().RetrieveTicket(coupon.getCouponId(), ticketType, redeemerGuid);
            } else {
                /*
                 * Get the issuer of the ticket
                 */
                ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveByGuid(issuerGuid);
                if (processAgentInfo == null) {
                    throw new NullPointerException();
                }

                /*
                 * Get the coupon for the issuer
                 */
                coupon = this.serviceManagement.getTicketIssuer().RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId());
                if (coupon == null) {
                    throw new NullPointerException();
                }

                /*
                 * Get the ticket from the issuer
                 */
                TicketIssuerAPI ticketIssuerAPI = new TicketIssuerAPI(processAgentInfo.getServiceUrl());
                ticketIssuerAPI.setAuthHeaderAgentGuid(this.serviceManagement.getServiceGuid());
                ticketIssuerAPI.setAuthHeaderCoupon(coupon);
                ticket = ticketIssuerAPI.RedeemTicket(coupon, ticketType, redeemerGuid);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, STR_MethodName);

        return ticket;
    }

    /**
     *
     * @param agentAuthHeader
     * @param coupon
     * @param type
     * @param redeemerGuid
     * @return boolean
     */
    public boolean requestTicketCancellation(AgentAuthHeader agentAuthHeader, Coupon coupon, String type, String redeemerGuid) {
        final String STR_MethodName = "requestTicketCancellation";
        Logfile.WriteCalled(logLevel, STR_ClassName, STR_MethodName);

        boolean value = false;

        this.Authenticate(agentAuthHeader);

        try {
            String issuerGuid = coupon.getIssuerGuid();
            TicketTypes ticketType = TicketTypes.ToType(type);

            /*
             * Check if this is the issuer of the ticket
             */
            if (this.serviceManagement.getServiceGuid().equals(issuerGuid) == true) {
                value = this.serviceManagement.getTicketIssuer().CancelTicket(coupon.getCouponId(), ticketType, redeemerGuid);
            } else {
                /*
                 * Get the issuer of the ticket
                 */
                ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveByGuid(issuerGuid);
                if (processAgentInfo == null) {
                    throw new NullPointerException();
                }

                /*
                 * Get the coupon for the issuer
                 */
                coupon = this.serviceManagement.getTicketIssuer().RetrieveCoupon(processAgentInfo.getOutCoupon().getCouponId());
                if (coupon == null) {
                    throw new NullPointerException();
                }

                /*
                 * Get the ticket from the issuer
                 */
                TicketIssuerAPI ticketIssuerAPI = new TicketIssuerAPI(processAgentInfo.getServiceUrl());
                ticketIssuerAPI.setAuthHeaderAgentGuid(this.serviceManagement.getServiceGuid());
                ticketIssuerAPI.setAuthHeaderCoupon(coupon);
                value = ticketIssuerAPI.RequestTicketCancellation(coupon, type, redeemerGuid);
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, STR_MethodName);

        return value;
    }

    //================================================================================================================//
    /**
     *
     * @param sbAuthHeader
     * @return Coupon
     */
    private Coupon Authenticate(AgentAuthHeader agentAuthHeader) {
        Coupon coupon = null;

        try {
            /*
             * Check that AgentAuthHeader is specified
             */
            if (agentAuthHeader == null) {
                throw new NullPointerException(String.format(STRERR_NotSpecified_arg, STRERR_AgentAuthHeader));
            }

            /*
             * Get the ProcessAgentInfo for the specified AgentGuid
             */
            ProcessAgentInfo processAgentInfo = this.serviceManagement.getProcessAgentsDB().RetrieveByGuid(agentAuthHeader.getAgentGuid());
            if (processAgentInfo == null) {
                throw new NullPointerException(String.format(STRERR_Invalid_arg, STRERR_ProcessAgentGuid));
            }

            /*
             * Get the coupon for the incoming request by the process agent
             */
            coupon = this.serviceManagement.getTicketing().RetrieveCoupon(processAgentInfo.getInCoupon().getCouponId(), processAgentInfo.getIssuerGuid());
            if (coupon == null) {
                throw new NullPointerException(String.format(STRERR_Invalid_arg, STRERR_ProcessAgentCoupon));
            }

            /*
             * Check coupon
             */

        } catch (Exception ex) {
            String message = String.format(STRERR_AccessDenied_arg, ex.getMessage());
            Logfile.WriteError(message);
            throw new RuntimeException(message);
        }

        return coupon;
    }
}
