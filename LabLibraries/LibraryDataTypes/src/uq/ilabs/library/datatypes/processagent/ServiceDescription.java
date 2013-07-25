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
public class ServiceDescription {

    public static final String STR_RequestSystemSupport = "requestSystemSupport";
    /**
     * An XML-encoded string that describes the services provided by the service provider described by this registry
     * entry.
     */
    private String serviceProviderInfo;
    /**
     * An optional coupon that may be used to redeem the described service.
     */
    private Coupon coupon;
    /**
     * An optional XML-encoded string that provides information required by the consumer. This information may be parsed
     * by the consuming site and may be reformatted for use. Part of this field may be used by a remote service broker
     * to route the service call.
     */
    private String consumerInfo;

    public String getServiceProviderInfo() {
        return serviceProviderInfo;
    }

    public void setServiceProviderInfo(String serviceProviderInfo) {
        this.serviceProviderInfo = serviceProviderInfo;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getConsumerInfo() {
        return consumerInfo;
    }

    public void setConsumerInfo(String consumerInfo) {
        this.consumerInfo = consumerInfo;
    }

    public ServiceDescription() {
    }

    public ServiceDescription(String serviceProviderInfo, Coupon coupon, String consumerInfo) {
        this.serviceProviderInfo = serviceProviderInfo;
        this.coupon = coupon;
        this.consumerInfo = consumerInfo;
    }
}
