/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database.types;

import java.util.Calendar;
import uq.ilabs.library.datatypes.storage.StorageStatus;

/**
 *
 * @author uqlpayne
 */
public class ExperimentInfo extends StorageStatus {

    private long couponId;
    private int userId;
    private int groupId;
    private int agentId;
    private int clientId;
    private int essId;
    private Calendar dateToStart;
    private long duration;
    private String annotation;

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getEssId() {
        return essId;
    }

    public void setEssId(int essId) {
        this.essId = essId;
    }

    public Calendar getDateToStart() {
        return dateToStart;
    }

    public void setDateToStart(Calendar dateToStart) {
        this.dateToStart = dateToStart;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public ExperimentInfo() {
        super();
    }

    public ExperimentInfo(long couponId, int userId, int groupId, int agentId, int clientId, int essId) {
        super();
        this.couponId = couponId;
        this.userId = userId;
        this.groupId = groupId;
        this.agentId = agentId;
        this.clientId = clientId;
        this.essId = essId;
    }
}
