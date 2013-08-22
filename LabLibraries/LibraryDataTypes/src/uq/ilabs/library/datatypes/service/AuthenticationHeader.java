/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.service;

import uq.ilabs.library.datatypes.ticketing.Coupon;

/**
 *
 * @author uqlpayne
 */
public class AuthenticationHeader {

    public static final String STR_Coupon = "coupon";
    public static final String STR_IssuerGuid = "issuerGuid";
    public static final String STR_CouponId = "couponId";
    public static final String STR_Passkey = "passkey";
    //
    protected Coupon coupon;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public AuthenticationHeader() {
    }

    public AuthenticationHeader(Coupon coupon) {
        this.coupon = coupon;
    }
}
