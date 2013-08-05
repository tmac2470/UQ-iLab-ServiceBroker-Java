/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.experimentstorage.client;

import java.io.Serializable;
import java.util.logging.Level;
import javax.annotation.PreDestroy;
import javax.faces.application.ViewExpiredException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import uq.ilabs.experimentstorage.service.ExperimentStorageService;
import uq.ilabs.library.experimentstorage.client.Consts;
import uq.ilabs.library.experimentstorage.client.ExperimentStorageSession;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.experimentstorage.client.UserSession;

/**
 *
 * @author uqlpayne
 */
@ManagedBean
@SessionScoped
public class ExperimentStorageBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ExperimentStorageBean.class.getName();
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
    private ExperimentStorageSession experimentStorageSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">

    /**
     *
     * @return
     */
    public String getTitle() {
        String title = null;

        if (this.experimentStorageSession != null) {
            title = this.experimentStorageSession.getTitle();
        }

        return (title != null) ? title : "";
    }

    /**
     *
     * @return
     */
    public String getVersion() {
        String version = null;

        if (this.experimentStorageSession != null) {
            version = this.experimentStorageSession.getVersion();
        }

        return (version != null) ? version : "";
    }

    /**
     *
     * @return
     */
    public String getContactEmail() {
        String contactEmail = null;

        if (this.experimentStorageSession != null) {
            contactEmail = this.experimentStorageSession.getContactEmail();
        }

        return (contactEmail != null) ? contactEmail : "";
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        String username = null;

        if (this.experimentStorageSession != null) {
            UserSession userSession = this.experimentStorageSession.getUserSession();
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

        if (this.experimentStorageSession != null) {
            UserSession userSession = this.experimentStorageSession.getUserSession();
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

        if (this.experimentStorageSession != null) {
            url = this.experimentStorageSession.getNavmenuPhotoUrl();
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

        if (this.experimentStorageSession != null) {
            UserSession userSession = this.experimentStorageSession.getUserSession();
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
        return (this.experimentStorageSession != null && this.experimentStorageSession.getUserSession() != null);
    }
    //</editor-fold>

    /**
     * Creates a new instance of ExperimentStorageBean
     */
    public ExperimentStorageBean() {
        final String methodName = "UsersideSchedulingBean";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.experimentStorageSession = (ExperimentStorageSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ExperimentStorage);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     */
    public void checkViewExpired() {
        if (this.experimentStorageSession == null) {
            throw new ViewExpiredException();
        }
    }

    @PreDestroy
    private void preDestroy() {
        final String methodName = "preDestroy";

        /*
         * Prevent from being called more than once
         */
        if (this.experimentStorageSession != null) {
            Logfile.WriteCalled(Level.INFO, STR_ClassName, methodName);

            /*
             * Check if ExperimentStorageService is running. If not, close the logger here
             * otherwise let the service close the logger when it is finished.
             */
            if (ExperimentStorageService.isInitialised() == false) {
                /*
                 * Deregister the database driver and close the logger
                 */
                this.experimentStorageSession.getDbConnection().DeRegister();
                Logfile.CloseLogger();
            }

            this.experimentStorageSession = null;

            Logfile.WriteCompleted(Level.INFO, STR_ClassName, methodName);
        }
    }
}
