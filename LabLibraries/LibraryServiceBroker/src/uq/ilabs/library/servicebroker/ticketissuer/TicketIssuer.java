/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.ticketissuer;

import java.util.UUID;
import java.util.logging.Level;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;
import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.servicebroker.database.IssuedCouponsDB;
import uq.ilabs.library.servicebroker.database.IssuedTicketsDB;

/**
 *
 * @author uqlpayne
 */
public class TicketIssuer {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = TicketIssuer.class.getName();
    private static final Level logLevel = Level.FINE;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private IssuedCouponsDB issuedCouponsDB;
    private IssuedTicketsDB issuedTicketsDB;
    private String issuerGuid;

    public IssuedCouponsDB getIssuedCouponsDB() {
        return issuedCouponsDB;
    }

    public IssuedTicketsDB getIssuedTicketsDB() {
        return issuedTicketsDB;
    }

    public String getIssuerGuid() {
        return issuerGuid;
    }

    public void setIssuerGuid(String issuerGuid) {
        this.issuerGuid = issuerGuid;
    }
    //</editor-fold>

    /**
     *
     * @param dbConnection
     * @throws Exception
     */
    public TicketIssuer(DBConnection dbConnection) throws Exception {
        final String methodName = "TicketIssuer";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check that all parameters are valid
         */
        if (dbConnection == null) {
            throw new NullPointerException(DBConnection.class.getSimpleName());
        }

        /*
         * Initialise locals
         */
        this.issuedCouponsDB = new IssuedCouponsDB(dbConnection);
        this.issuedTicketsDB = new IssuedTicketsDB(dbConnection);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param coupon
     * @param ticketType
     * @param sponsorGuid
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return Ticket
     */
    public Ticket AddTicket(Coupon coupon, TicketTypes ticketType, String sponsorGuid, String redeemerGuid, long duration, String payload) {
        Ticket ticket = null;

        try {
            long couponId = coupon.getCouponId();
            Coupon issuedCoupon = this.issuedCouponsDB.Retrieve(couponId);
            if (issuedCoupon != null) {
                ticket = new Ticket(couponId, ticketType, null, sponsorGuid, redeemerGuid, duration, payload);
                long ticketId = this.issuedTicketsDB.Add(ticket);
                if (ticketId > 0) {
                    ticket = this.issuedTicketsDB.Retrieve(couponId, ticketType, redeemerGuid);
                }
            }
        } catch (Exception ex) {
        }

        return ticket;
    }

    /**
     *
     * @param ticketType
     * @param sponsorGuid
     * @param redeemerGuid
     * @param duration
     * @param payload
     * @return Coupon
     */
    public Coupon CreateTicket(TicketTypes ticketType, String sponsorGuid, String redeemerGuid, long duration, String payload) {
        Coupon coupon = null;

        try {
            /*
             * Create an issued coupon with a generated passkey
             */
            Coupon issuedCoupon = new Coupon(null, UUID.randomUUID().toString());
            long couponId = this.issuedCouponsDB.Add(issuedCoupon);
            if (couponId > 0) {
                /*
                 * Create an issued ticket
                 */
                issuedCoupon.setCouponId(couponId);
                Ticket ticket = new Ticket(couponId, ticketType, null, sponsorGuid, redeemerGuid, duration, payload);
                long ticketId = this.issuedTicketsDB.Add(ticket);
                if (ticketId > 0) {
                    coupon = issuedCoupon;
                }
            }
        } catch (Exception ex) {
        }

        return coupon;
    }

    /**
     *
     * @param couponId
     * @param ticketType
     * @param redeemerGuid
     * @return Ticket
     */
    public Ticket RetrieveTicket(long couponId, TicketTypes ticketType, String redeemerGuid) {
        Ticket ticket = null;

        try {
            ticket = this.issuedTicketsDB.Retrieve(couponId, ticketType, redeemerGuid);
            if (ticket != null) {
                ticket.setIssuerGuid(this.issuerGuid);
            }
        } catch (Exception ex) {
        }

        return ticket;
    }

    /**
     *
     * @param couponId
     * @param ticketType
     * @param redeemerGuid
     * @return boolean
     */
    public boolean CancelTicket(long couponId, TicketTypes ticketType, String redeemerGuid) {
        boolean success = false;

        try {
            Ticket ticket = this.issuedTicketsDB.Retrieve(couponId, ticketType, redeemerGuid);
            if (ticket != null) {
                success = this.issuedTicketsDB.Cancel(ticket.getTicketId());
            }
        } catch (Exception ex) {
        }

        return success;
    }

    /**
     *
     * @return Coupon
     */
    public Coupon CreateCoupon() {
        return this.CreateCoupon(this.issuerGuid, UUID.randomUUID().toString());
    }

    /**
     *
     * @param passkey
     * @return Coupon
     */
    public Coupon CreateCoupon(String passkey) {
        return this.CreateCoupon(this.issuerGuid, passkey);
    }

    /**
     *
     * @param issuerGuid
     * @param passkey
     * @return Coupon
     */
    public Coupon CreateCoupon(String issuerGuid, String passkey) {
        Coupon coupon;

        try {
            coupon = new Coupon(issuerGuid, passkey);
            long couponId = this.issuedCouponsDB.Add(coupon);
            if (couponId > 0) {
                coupon.setCouponId(couponId);
            }
        } catch (Exception ex) {
            coupon = null;
        }

        return coupon;
    }

    /**
     *
     * @param couponId
     * @return boolean
     */
    public boolean DeleteCoupon(long couponId) {
        return this.issuedCouponsDB.Delete(couponId);
    }

    /**
     *
     * @param couponId
     * @return Coupon
     */
    public Coupon RetrieveCoupon(long couponId) {
        Coupon coupon = null;

        try {
            coupon = this.issuedCouponsDB.Retrieve(couponId);
            coupon.setIssuerGuid(this.issuerGuid);
        } catch (Exception ex) {
        }

        return coupon;
    }
}
