/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.service;

import java.util.logging.Level;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.soap.SOAPFaultException;
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
@Singleton
@LocalBean
public class ExperimentStorageServiceBean {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentStorageServiceBean.class.getName();
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
    private boolean initialised;
    private ServiceManagement serviceManagement;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String qnameAgentAuthHeaderLocalPart;
    private String qnameInitAuthHeaderLocalPart;
    private String qnameOperationAuthHeaderLocalPart;

    public String getQnameAgentAuthHeaderLocalPart() {
        return qnameAgentAuthHeaderLocalPart;
    }

    public String getQnameInitAuthHeaderLocalPart() {
        return qnameInitAuthHeaderLocalPart;
    }

    public String getQnameOperationAuthHeaderLocalPart() {
        return qnameOperationAuthHeaderLocalPart;
    }
    //</editor-fold>

    /**
     * Constructor - Seems that this gets called when the project is deployed which is unexpected. To get around this,
     * check to see if the service has been initialized and this class has been initialized. Can't do logging until the
     * service has been initialized and the logger created.
     */
    public ExperimentStorageServiceBean() {
        final String methodName = "ExperimentStorageServiceBean";

        /*
         * Check if initialisation needs to be done
         */
        if (ExperimentStorageService.isInitialised() == true && this.initialised == false) {
            Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

            try {
                /*
                 * Create an instance of the ServiceManagement
                 */
                this.serviceManagement = new ServiceManagement(ExperimentStorageService.getConfigProperties());
                ExperimentStorageService.setServiceManagement(this.serviceManagement);

                /*
                 * Create authentication header QNames
                 */
                edu.mit.ilab.ilabs.type.ObjectFactory objectFactory = new edu.mit.ilab.ilabs.type.ObjectFactory();
                JAXBElement<edu.mit.ilab.ilabs.type.AgentAuthHeader> jaxbElementAgentAuthHeader =
                        objectFactory.createAgentAuthHeader(new edu.mit.ilab.ilabs.type.AgentAuthHeader());
                this.qnameAgentAuthHeaderLocalPart = jaxbElementAgentAuthHeader.getName().getLocalPart();
                JAXBElement<edu.mit.ilab.ilabs.type.InitAuthHeader> jaxbElementInitAuthHeader =
                        objectFactory.createInitAuthHeader(new edu.mit.ilab.ilabs.type.InitAuthHeader());
                this.qnameInitAuthHeaderLocalPart = jaxbElementInitAuthHeader.getName().getLocalPart();
                JAXBElement<edu.mit.ilab.ilabs.type.OperationAuthHeader> jaxbElementOperationAuthHeader =
                        objectFactory.createOperationAuthHeader(new edu.mit.ilab.ilabs.type.OperationAuthHeader());
                this.qnameOperationAuthHeaderLocalPart = jaxbElementOperationAuthHeader.getName().getLocalPart();

            } catch (Exception ex) {
                Logfile.WriteError(ex.toString());
            }

            Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
        }
    }

    //================================================================================================================//
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
                throw new ProtocolException(String.format(STRERR_NotSpecified_arg, STRERR_AuthHeader));
            }

            /*
             * Check that AgentGuid is specified
             */
            if (agentAuthHeader.getAgentGuid() == null) {
                throw new ProtocolException(String.format(STRERR_NotSpecified_arg, STRERR_AgentGuid));
            }

            /*
             * Check that Coupon is specified
             */
            if (agentAuthHeader.getCoupon() == null) {
                throw new ProtocolException(String.format(STRERR_NotSpecified_arg, STRERR_Coupon));
            }

            /*
             * Check that CouponId is valid
             */
            long couponId = agentAuthHeader.getCoupon().getCouponId();
            if (couponId <= 0) {
                throw new ProtocolException(String.format(STRERR_Invalid_arg, STRERR_CouponId));
            }

            /*
             * Retrieve the coupon for this CouponId and AgentGuid and compare with the AuthHeader coupon
             */
            Coupon retrievedCoupon = this.serviceManagement.getTicketing().RetrieveCoupon(couponId, agentAuthHeader.getAgentGuid());
            if (retrievedCoupon != null && retrievedCoupon.equals(agentAuthHeader.getCoupon())) {
                success = true;
            }
        } catch (Exception ex) {
            String message = String.format(STRERR_AccessDenied_arg, ex.getMessage());
            Logfile.WriteError(message);
            this.ThrowSoapFault(message);
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
                        throw new ProtocolException(String.format(STRERR_TicketInvalid_arg, ticketType.name()));
                    }

                    /*
                     * Check if ticket is valid
                     */
                    if (ticket.isCancelled() == true) {
                        throw new ProtocolException(String.format(STRERR_TicketCancelled_arg, ticketType.name()));
                    }
                    if (ticket.isExpired() == true) {
                        throw new ProtocolException(String.format(STRERR_TicketExpired_arg, ticketType.name()));
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
            this.ThrowSoapFault(message);
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return success;
    }

    //================================================================================================================//
    /**
     *
     * @param message
     */
    private void ThrowSoapFault(String message) {
        /*
         * Create a SOAPFaultException to be thrown back to the caller
         */
        try {
            SOAPFault fault = SOAPFactory.newInstance().createFault();
            fault.setFaultString(message);
            throw new SOAPFaultException(fault);
        } catch (SOAPException e) {
            Logfile.WriteError(e.getMessage());
        }
    }

    /**
     *
     */
    @PreDestroy
    private void preDestroy() {
        final String methodName = "preDestroy";

        /*
         * Prevent from being called more than once
         */
        if (this.serviceManagement != null) {
            Logfile.WriteCalled(Level.INFO, STR_ClassName, methodName);

            /*
             * Deregister the database driver and close the logger
             */
            this.serviceManagement.getDbConnection().DeRegister();
            Logfile.CloseLogger();

            this.serviceManagement = null;

            Logfile.WriteCompleted(Level.INFO, STR_ClassName, methodName);
        }
    }
}
