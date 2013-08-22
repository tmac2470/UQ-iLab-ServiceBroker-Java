/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.library.usersidescheduling.client;

import uq.ilabs.library.lab.database.DBConnection;
import uq.ilabs.library.usersidescheduling.engine.ServiceManagement;

/**
 *
 * @author uqlpayne
 */
public class UsersideSchedulingSession {

    private String title;
    private String contactEmail;
    private ServiceManagement serviceManagement;
    private UserSession userSession;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public ServiceManagement getServiceManagement() {
        return serviceManagement;
    }

    public void setServiceManagement(ServiceManagement serviceManagement) {
        this.serviceManagement = serviceManagement;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public DBConnection getDbConnection() {
        return (serviceManagement != null) ? serviceManagement.getDbConnection() : null;
    }

    public String getVersion() {
        return (serviceManagement != null) ? serviceManagement.getVersion() : null;
    }

    public String getNavMenuPhotoUrl() {
        return (serviceManagement != null) ? serviceManagement.getNavMenuPhotoUrl() : null;
    }

    public String getServiceGuid() {
        return (serviceManagement != null) ? serviceManagement.getServiceGuid() : null;
    }

    public void setServiceGuid(String serviceGuid) {
        if (serviceManagement != null) {
            serviceManagement.setServiceGuid(serviceGuid);
        }
    }
}
