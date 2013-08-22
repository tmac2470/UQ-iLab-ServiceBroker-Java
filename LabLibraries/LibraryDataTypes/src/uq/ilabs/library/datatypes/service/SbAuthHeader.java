/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.service;

/**
 *
 * @author uqlpayne
 */
public class SbAuthHeader {

    public static final String STR_CouponId = "couponID";
    public static final String STR_CouponPasskey = "couponPassKey";
    //
    protected long couponId;
    protected String couponPasskey;

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCouponPasskey() {
        return couponPasskey;
    }

    public void setCouponPasskey(String couponPasskey) {
        this.couponPasskey = couponPasskey;
    }
}
