/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.usersidescheduling.client;

/**
 *
 * @author uqlpayne
 */
public class UserSession {

    private String username;
    private String groupname;
    private int timezone;
    private boolean management;
    private boolean administration;
    private long couponId;
    private String passkey;
    private String issuerGuid;
    private String serviceBrokerUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public boolean isManagement() {
        return management;
    }

    public void setManagement(boolean management) {
        this.management = management;
    }

    public boolean isAdministration() {
        return administration;
    }

    public void setAdministration(boolean administration) {
        this.administration = administration;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public String getIssuerGuid() {
        return issuerGuid;
    }

    public void setIssuerGuid(String issuerGuid) {
        this.issuerGuid = issuerGuid;
    }

    public String getServiceBrokerUrl() {
        return serviceBrokerUrl;
    }

    public void setServiceBrokerUrl(String serviceBrokerUrl) {
        this.serviceBrokerUrl = serviceBrokerUrl;
    }

    public UserSession() {
        this.administration = false;
        this.management = false;
    }
}
