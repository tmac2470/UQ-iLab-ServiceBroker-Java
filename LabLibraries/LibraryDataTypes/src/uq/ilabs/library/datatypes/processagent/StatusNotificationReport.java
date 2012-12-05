/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.processagent;

import java.util.Calendar;

/**
 *
 * @author uqlpayne
 */
public class StatusNotificationReport {

    private int alertCode;
    private String serviceGuid;
    private Calendar timestamp;
    private String payload;

    public int getAlertCode() {
        return alertCode;
    }

    public void setAlertCode(int alertCode) {
        this.alertCode = alertCode;
    }

    public String getServiceGuid() {
        return serviceGuid;
    }

    public void setServiceGuid(String serviceGuid) {
        this.serviceGuid = serviceGuid;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
