/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.servicebroker.database.types;

import java.util.Date;

/**
 *
 * @author uqlpayne
 */
public class UserSessionInfo {

    /**
     * The long integer Session ID.
     */
    private long sessionId;
    /**
     * ID of the User whose session is represented by the UserSession object.
     */
    private int userId;
    /**
     * Current Effective Group of the current UserSession.
     */
    private int groupId;
    /**
     * ClientID of the current UserSession, may be null.
     */
    private int clientId;
    /**
     * Time zone offset from GMT in minutes.
     */
    private int timezoneOffset;
    /**
     * Date/Time the session started.
     */
    private Date sessionStartTime;

    /*
     * Date/Time the session ended.
     */
    private Date sessionEndTime;
    /**
     * The Session Key.
     */
    public String sessionKey;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
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

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public Date getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(Date sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public Date getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(Date sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
