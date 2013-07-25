/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.ticketissuer;

import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.datatypes.ticketing.Ticket;
import uq.ilabs.library.datatypes.ticketing.TicketTypes;

/**
 *
 * @author uqlpayne
 */
public class ConvertTypes {

    /**
     *
     * @param xmlGregorianCalendar
     * @return Calendar
     */
    public static Calendar ConvertType(XMLGregorianCalendar xmlGregorianCalendar) {
        Calendar calendar = null;

        if (xmlGregorianCalendar != null) {
            calendar = Calendar.getInstance();
            calendar.setTime(xmlGregorianCalendar.toGregorianCalendar().getTime());
        }

        return calendar;
    }

    /**
     *
     * @param proxyCoupon
     * @return Coupon
     */
    public static Coupon ConvertType(uq.ilabs.ticketissuer.Coupon proxyCoupon) {
        Coupon coupon = null;

        if (proxyCoupon != null) {
            coupon = new Coupon();
            coupon.setCouponId(proxyCoupon.getCouponId());
            coupon.setIssuerGuid(proxyCoupon.getIssuerGuid());
            coupon.setPasskey(proxyCoupon.getPasskey());
        }

        return coupon;
    }

    /**
     *
     * @param proxyTicket
     * @return Ticket
     */
    public static Ticket ConvertType(uq.ilabs.ticketissuer.Ticket proxyTicket) {
        Ticket ticket = null;

        if (proxyTicket != null) {
            ticket = new Ticket();
            ticket.setTicketId(proxyTicket.getTicketId());
            ticket.setTicketType(TicketTypes.ToType(proxyTicket.getType()));
            ticket.setCouponId(proxyTicket.getCouponId());
            ticket.setIssuerGuid(proxyTicket.getIssuerGuid());
            ticket.setSponsorGuid(proxyTicket.getSponsorGuid());
            ticket.setRedeemerGuid(proxyTicket.getRedeemerGuid());
            ticket.setDuration(proxyTicket.getDuration());
            ticket.setCancelled(proxyTicket.isIsCancelled());
            ticket.setPayload(proxyTicket.getPayload());
            ticket.setDateCreated(ConvertType(proxyTicket.getCreationTime()));
        }

        return ticket;
    }

    /**
     *
     * @param coupon
     * @return uq.ilabs.ticketissuer.Coupon
     */
    public static uq.ilabs.ticketissuer.Coupon ConvertType(Coupon coupon) {
        uq.ilabs.ticketissuer.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new uq.ilabs.ticketissuer.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }
}
