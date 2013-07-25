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
