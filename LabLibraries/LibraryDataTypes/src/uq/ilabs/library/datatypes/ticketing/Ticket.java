/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.ticketing;

import java.util.Calendar;

/**
 *
 * @author uqlpayne
 */
public class Ticket {

    /**
     * Unique ticket ID
     */
    private long ticketId;
    /**
     * The type of ticket
     */
    private TicketTypes ticketType;
    /**
     * The ID of the coupon defining which ticket collection this ticket is a member of
     */
    private long couponId;
    /**
     * The ticket issuer
     */
    private String issuerGuid;
    /**
     * Guid of the processAgent requesting the creation of this ticket
     */
    private String sponsorGuid;
    /**
     * Guid of the processAgent that will be processing the specified operation
     */
    private String redeemerGuid;
    /**
     * Creation time of the ticket stored as UTC
     */
    private Calendar dateCreated;
    /**
     * Ticket duration, the number of seconds before the ticket expires. Negative one ( -1 ) is used to define a ticket
     * that never expires.
     */
    private long duration;
    /**
     *
     */
    private boolean cancelled;
    /**
     * The ticket body
     */
    private String payload;

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public TicketTypes getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypes ticketType) {
        this.ticketType = ticketType;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getIssuerGuid() {
        return issuerGuid;
    }

    public void setIssuerGuid(String issuerGuid) {
        this.issuerGuid = issuerGuid;
    }

    public String getSponsorGuid() {
        return sponsorGuid;
    }

    public void setSponsorGuid(String sponsorGuid) {
        this.sponsorGuid = sponsorGuid;
    }

    public String getRedeemerGuid() {
        return redeemerGuid;
    }

    public void setRedeemerGuid(String redeemerGuid) {
        this.redeemerGuid = redeemerGuid;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isExpired() {
        return this.IsExpired();
    }

    public Ticket() {
    }

    public Ticket(long couponId, TicketTypes ticketType, String issuerGuid, String sponsorGuid, String redeemerGuid, long duration, String payload) {
        this.ticketId = -1;
        this.ticketType = ticketType;
        this.couponId = couponId;
        this.issuerGuid = issuerGuid;
        this.sponsorGuid = sponsorGuid;
        this.redeemerGuid = redeemerGuid;
        this.duration = duration;
        this.payload = payload;
        this.cancelled = false;
    }

    /**
     *
     * @return boolean
     */
    private boolean IsExpired() {
        return false;
    }
//        public boolean IsExpired()
//        {
//            boolean state = false;
//            if (duration != -1)
//            {
//                // Documentation states that Add does not alter the original value
//                if (creationTime.AddTicks(duration * TimeSpan.TicksPerSecond) < DateTime.UtcNow)
//                    state = true;
//            }
//            return state;
//        }
//
//        public long SecondsToExpire()
//        {
//            long remaining = long.MaxValue;
//            if (duration != -1)
//            {
//                TimeSpan ts = creationTime.AddTicks(duration * TimeSpan.TicksPerSecond).Subtract(DateTime.UtcNow);
//                remaining = ts.Ticks / TimeSpan.TicksPerSecond;
//            }
//            return remaining;
//        }
}
