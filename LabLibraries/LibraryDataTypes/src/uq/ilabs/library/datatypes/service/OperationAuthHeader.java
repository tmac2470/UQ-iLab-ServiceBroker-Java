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
public class OperationAuthHeader extends AuthenticationHeader {

    public OperationAuthHeader() {
    }

    public OperationAuthHeader(Coupon coupon) {
        super(coupon);
    }
}
