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

    protected long couponID;
    protected String couponPassKey;

    public long getCouponID() {
        return couponID;
    }

    public void setCouponID(long couponID) {
        this.couponID = couponID;
    }

    public String getCouponPassKey() {
        return couponPassKey;
    }

    public void setCouponPassKey(String couponPassKey) {
        this.couponPassKey = couponPassKey;
    }

    public SbAuthHeader() {
    }
}
