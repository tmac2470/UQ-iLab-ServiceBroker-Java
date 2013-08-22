/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.usersidescheduling.client;

import java.io.Serializable;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.usersidescheduling.client.Consts;
import uq.ilabs.library.usersidescheduling.client.UsersideSchedulingSession;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.usersidescheduling.client.UserSession;

/**
 *
 * @author uqlpayne
 */
@Named(value = "usersideSchedulingBean")
@SessionScoped
public class UsersideSchedulingBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = UsersideSchedulingBean.class.getName();
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
    private UsersideSchedulingSession usersideSchedulingSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">

    /**
     *
     * @return
     */
    public String getTitle() {
        String title = null;

        if (this.usersideSchedulingSession != null) {
            title = this.usersideSchedulingSession.getTitle();
        }

        return (title != null) ? title : "";
    }

    /**
     *
     * @return
     */
    public String getVersion() {
        String version = null;

        if (this.usersideSchedulingSession != null) {
            version = this.usersideSchedulingSession.getVersion();
        }

        return (version != null) ? version : "";
    }

    /**
     *
     * @return
     */
    public String getContactEmail() {
        String contactEmail = null;

        if (this.usersideSchedulingSession != null) {
            contactEmail = this.usersideSchedulingSession.getContactEmail();
        }

        return (contactEmail != null) ? contactEmail : "";
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        String username = null;

        if (this.usersideSchedulingSession != null) {
            UserSession userSession = this.usersideSchedulingSession.getUserSession();
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

        if (this.usersideSchedulingSession != null) {
            UserSession userSession = this.usersideSchedulingSession.getUserSession();
            if (userSession != null && userSession.getGroupname() != null) {
                groupname = userSession.getGroupname();
                if (groupname.length() > 0) {
                    groupname = String.format(STR_Group_arg, groupname);
                }
            }
        }

        return (groupname != null) ? groupname : "";
    }

    /**
     *
     * @return
     */
    public String getNavmenuPhotoUrl() {
        String url = null;

        if (this.usersideSchedulingSession != null) {
            url = this.usersideSchedulingSession.getNavMenuPhotoUrl();
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

        if (this.usersideSchedulingSession != null) {
            UserSession userSession = this.usersideSchedulingSession.getUserSession();
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
        return (this.usersideSchedulingSession != null
                && this.usersideSchedulingSession.getUserSession() != null);
    }

    /**
     *
     * @return
     */
    public boolean isAdministration() {
        return (this.usersideSchedulingSession != null
                && this.usersideSchedulingSession.getUserSession() != null
                && this.usersideSchedulingSession.getUserSession().isAdministration() == true);
    }

    /**
     *
     * @return
     */
    public boolean isManagement() {
        return (this.usersideSchedulingSession != null
                && this.usersideSchedulingSession.getUserSession() != null
                && this.usersideSchedulingSession.getUserSession().isManagement() == true);
    }

    /**
     *
     * @return
     */
    public String getServiceBrokerUrl() {
        String serviceBrokerUrl = null;

        if (this.usersideSchedulingSession != null) {
            UserSession userSession = this.usersideSchedulingSession.getUserSession();
            if (userSession != null) {
                serviceBrokerUrl = userSession.getServiceBrokerUrl();
            }
        }

        return (serviceBrokerUrl != null) ? serviceBrokerUrl : "";
    }
    //</editor-fold>

    /**
     * Creates a new instance of UsersideSchedulingBean
     */
    public UsersideSchedulingBean() {
        final String methodName = "UsersideSchedulingBean";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.usersideSchedulingSession = (UsersideSchedulingSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_UsersideScheduling);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     */
    public void checkViewExpired() {
        if (this.usersideSchedulingSession == null) {
            throw new ViewExpiredException();
        }
    }
}
