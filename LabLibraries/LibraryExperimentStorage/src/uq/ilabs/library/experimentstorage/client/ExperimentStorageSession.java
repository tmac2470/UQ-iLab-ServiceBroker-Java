/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.experimentstorage.client;

import uq.ilabs.library.lab.database.DBConnection;

/**
 *
 * @author uqlpayne
 */
public class ExperimentStorageSession {

    private String title;
    private String version;
    private String navmenuPhotoUrl;
    private String guid;
    private String contactEmail;
    private UserSession userSession;
    private DBConnection dbConnection;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNavmenuPhotoUrl() {
        return navmenuPhotoUrl;
    }

    public void setNavmenuPhotoUrl(String navmenuPhotoUrl) {
        this.navmenuPhotoUrl = navmenuPhotoUrl;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
