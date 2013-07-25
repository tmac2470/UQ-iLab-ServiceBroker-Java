/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.datatypes.processagent;

/**
 *
 * @author uqlpayne
 */
public class StatusReport {

    private boolean online;
    private String serviceGuid;
    private String payload;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getServiceGuid() {
        return serviceGuid;
    }

    public void setServiceGuid(String serviceGuid) {
        this.serviceGuid = serviceGuid;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public StatusReport() {
    }

    public StatusReport(boolean online, String serviceGuid, String payload) {
        this.online = online;
        this.serviceGuid = serviceGuid;
        this.payload = payload;
    }
}
