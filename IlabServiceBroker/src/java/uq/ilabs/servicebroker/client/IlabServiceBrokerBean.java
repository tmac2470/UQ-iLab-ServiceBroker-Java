/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client;

import java.io.Serializable;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;

/**
 *
 * @author uqlpayne
 */
@Named(value = "ilabServiceBrokerBean")
@SessionScoped
public class IlabServiceBrokerBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = IlabServiceBrokerBean.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants
     */
    private static final String STR_User_arg = "User: %s";
    private static final String STR_Group_arg = "Group: %s";
    private static final String STR_Timezone_arg3 = "Timezone: GMT%s%d:%02d";
    private static final String STR_DefaultNavMenuPhotoUrl = "./resources/img/generic_content32.jpg";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">

    /**
     *
     * @return
     */
    public String getTitle() {
        String title = null;

        if (this.serviceBrokerSession != null) {
            title = this.serviceBrokerSession.getTitle();
        }

        return (title != null) ? title : "";
    }

    /**
     *
     * @return
     */
    public String getVersion() {
        String version = null;

        if (this.serviceBrokerSession != null) {
            version = this.serviceBrokerSession.getVersion();
        }

        return (version != null) ? version : "";
    }

    /**
     *
     * @return
     */
    public String getContactEmail() {
        String contactEmail = null;

        if (this.serviceBrokerSession != null) {
            contactEmail = this.serviceBrokerSession.getContactEmail();
        }

        return (contactEmail != null) ? contactEmail : "";
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        String username = null;

        if (this.serviceBrokerSession != null) {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            if (userSession != null && userSession.getUsername() != null) {
                username = userSession.getUsername();
                if (username.length() > 0) {
                    username = String.format(STR_User_arg, username);
                }
            }
        }

        return (username != null) ? username : "";
    }

    /**
     *
     * @return
     */
    public String getGroupname() {
        String groupname = null;

        if (this.serviceBrokerSession != null) {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            if (userSession != null && userSession.getGroupname() != null) {
                groupname = userSession.getGroupname();
                if (groupname.length() > 0) {
                    groupname = String.format(STR_Group_arg, groupname);
                }
            }
        }

//        return (groupname != null) ? groupname : "";
        return groupname;
    }

    /**
     *
     * @return
     */
    public String getNavmenuPhotoUrl() {
        String url = null;

        if (this.serviceBrokerSession != null) {
            url = this.serviceBrokerSession.getNavMenuPhotoUrl();
            if (url != null) {
                url = url.trim();
            }
        }

        return (url != null) ? url : STR_DefaultNavMenuPhotoUrl;
    }

    /**
     * Get Timezone GMT +/- ?? hrs:mins
     *
     * @return
     */
    public String getTimezone() {
        String timezone = "";

        if (this.serviceBrokerSession != null) {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            if (userSession != null) {
                int totalMinutes = userSession.getTimezone();
                if (totalMinutes > Integer.MIN_VALUE) {
                    int hours = totalMinutes / 60;
                    int minutes = totalMinutes % 60;
                    timezone = String.format(STR_Timezone_arg3, (totalMinutes >= 0) ? "+" : "", hours, minutes);
                }
            }
        }

        return timezone;
    }

    /**
     *
     * @return
     */
    public boolean isLoggedIn() {
        return (this.serviceBrokerSession != null && this.serviceBrokerSession.getUserSession() != null);
    }

    public boolean isAdministrator() {
        boolean administrator = false;

        if (this.serviceBrokerSession != null) {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            if (userSession != null && userSession.getGroupname() != null) {
                administrator = userSession.isAdmin();
            }
        }

        return administrator;
    }

    public boolean isConfigured() {
        boolean configured = false;

        if (this.serviceBrokerSession != null) {
            String serviceGuid = this.serviceBrokerSession.getServiceGuid();
            if (serviceGuid != null && serviceGuid.trim().length() > 0) {
                configured = true;
            }
        }

        return configured;
    }
    //</editor-fold>

    /**
     * Creates a new instance of IlabServiceBrokerBean
     */
    public IlabServiceBrokerBean() {
        final String methodName = "ServiceBrokerClientBean";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     */
    public void checkViewExpired() {
        if (this.serviceBrokerSession == null) {
            throw new ViewExpiredException();
        }
    }
}
