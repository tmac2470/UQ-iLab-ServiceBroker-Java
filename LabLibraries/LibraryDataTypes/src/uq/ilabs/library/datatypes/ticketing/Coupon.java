/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.ticketing;

/**
 *
 * @author uqlpayne
 */
public class Coupon {

    private long couponId;
    private String issuerGuid;
    private String passkey;
    private boolean cancelled;

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

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Coupon() {
        this.couponId = -1;
        this.cancelled = false;
    }

    public Coupon(long couponId) {
        this.couponId = couponId;
        this.cancelled = false;
    }

    public Coupon(String passkey) {
        this();
        this.passkey = passkey;
    }

    public Coupon(String issuerGuid, String passkey) {
        this(passkey);
        this.issuerGuid = issuerGuid;
        this.passkey = passkey;
    }

    public Coupon(long couponId, String issuerGuid, String passkey) {
        this(issuerGuid, passkey);
        this.couponId = couponId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj instanceof Coupon) {
            Coupon coupon = (Coupon) obj;
            isEqual = this.couponId == coupon.getCouponId()
                    && this.issuerGuid != null && this.issuerGuid.equalsIgnoreCase(coupon.getIssuerGuid())
                    && this.passkey != null && this.passkey.equalsIgnoreCase(coupon.getPasskey());
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.couponId ^ (this.couponId >>> 32));
        hash = 17 * hash + (this.issuerGuid != null ? this.issuerGuid.hashCode() : 0);
        hash = 17 * hash + (this.passkey != null ? this.passkey.hashCode() : 0);
        return hash;
    }
}
