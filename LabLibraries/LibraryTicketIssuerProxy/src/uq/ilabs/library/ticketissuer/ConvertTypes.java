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
    public static Calendar Convert(XMLGregorianCalendar xmlGregorianCalendar) {
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
    public static Coupon Convert(edu.mit.ilab.ilabs.ticketissuer.proxy.Coupon proxyCoupon) {
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
    public static Ticket Convert(edu.mit.ilab.ilabs.ticketissuer.proxy.Ticket proxyTicket) {
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
            ticket.setDateCreated(Convert(proxyTicket.getCreationTime()));
        }

        return ticket;
    }

    /**
     *
     * @param coupon
     * @return edu.mit.ilab.ilabs.ticketissuer.proxy.Coupon
     */
    public static edu.mit.ilab.ilabs.ticketissuer.proxy.Coupon Convert(Coupon coupon) {
        edu.mit.ilab.ilabs.ticketissuer.proxy.Coupon proxyCoupon = null;

        if (coupon != null) {
            proxyCoupon = new edu.mit.ilab.ilabs.ticketissuer.proxy.Coupon();
            proxyCoupon.setCouponId(coupon.getCouponId());
            proxyCoupon.setIssuerGuid(coupon.getIssuerGuid());
            proxyCoupon.setPasskey(coupon.getPasskey());
        }

        return proxyCoupon;
    }
}
