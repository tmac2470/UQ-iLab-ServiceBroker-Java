/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.processagent;

import uq.ilabs.library.datatypes.ticketing.Coupon;

/**
 *
 * @author uqlpayne
 */
public class DomainCredentials {

    /**
     * The ProcessAgent
     */
    public ProcessAgent agent;
    /**
     * The coupon that will be used on all incoming messages from the domain server, it may be null.
     */
    public Coupon inCoupon;
    /**
     * The coupon that will be used on all outgoing messages to the domain server, it may be null.
     */
    public Coupon outCoupon;

    public DomainCredentials() {
    }

    public DomainCredentials(ProcessAgent agent, Coupon inCoupon, Coupon outCoupon) {
        this.agent = agent;
        this.inCoupon = inCoupon;
        this.outCoupon = outCoupon;
    }
}
