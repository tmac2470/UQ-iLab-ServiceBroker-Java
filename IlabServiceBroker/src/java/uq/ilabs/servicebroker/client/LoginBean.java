/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.lab.utilities.Password;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.UsersDB;
import uq.ilabs.library.servicebroker.database.types.GroupInfo;
import uq.ilabs.library.servicebroker.database.types.UserGroupInfo;
import uq.ilabs.library.servicebroker.database.types.UserInfo;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;

/**
 *
 * @author uqlpayne
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = LoginBean.class.getName();
    private static final Level logLevel = Level.FINE;
    /*
     * String constants for logfile messages
     */
    private static final String STRLOG_LoginUser_arg = "Login - User: %s";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_NotSpecified_arg = "%s - Not specified!";
    private static final String STRERR_Username = "Username";
    private static final String STRERR_Password = "Password";
    private static final String STRERR_LoginFailed = "Login failed: ";
    private static final String STRERR_UnknownUsername = STRERR_LoginFailed + "Unknown username!";
    private static final String STRERR_IncorrectPassword = STRERR_LoginFailed + "Incorrect password!";
    private static final String STRERR_AccountIsLocked = "Account is locked!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hitUsername;
    private String hisPassword;
    private String holMessage;
    private String holMessageClass;

    public String getHitUsername() {
        return hitUsername;
    }

    public void setHitUsername(String hitUsername) {
        this.hitUsername = hitUsername;
    }

    public String getHisPassword() {
        return hisPassword;
    }

    public void setHisPassword(String hisPassword) {
        this.hisPassword = hisPassword;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
    }

    /**
     *
     * @return
     */
    public String actionLogin() {
        final String methodName = "actionLogin";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check that a username has been entered
             */
            this.hitUsername = this.hitUsername.toLowerCase().trim();
            if (this.hitUsername.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Username));
            }

            /*
             * Check that a password has been entered
             */
            this.hisPassword = this.hisPassword.trim();
            if (this.hisPassword.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Password));
            }

            /*
             * Check if username exists
             */
            UsersDB usersDB = new UsersDB(this.serviceBrokerSession.getDbConnection());
            UserInfo userInfo = usersDB.RetrieveByUsername(this.hitUsername);
            if (userInfo == null) {
                throw new RuntimeException(STRERR_UnknownUsername);
            }

            /*
             * Check password
             */
            if (Password.ToHash(this.hisPassword).equals(userInfo.getPassword()) == false) {
                throw new RuntimeException(STRERR_IncorrectPassword);
            }

            /*
             * Check if account is locked
             */
            if (userInfo.isAccountLocked() == true) {
                throw new Exception(STRERR_AccountIsLocked);
            }

            Logfile.Write(Level.INFO, String.format(STRLOG_LoginUser_arg, userInfo.getUsername()));

            /*
             * Create user session information
             */
            UserSession userSession = new UserSession(this.serviceBrokerSession.getDbConnection());
            userSession.setUsername(userInfo.getUsername());
            userSession.setUserId(userInfo.getUserId());

            /*
             * Get the user's timezone
             */
            Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            int timezone = 0;
            try {
                timezone = Integer.parseInt(parameterMap.get(Consts.STRPRM_Timezone));
            } catch (NumberFormatException ex) {
            }
            userSession.setTimezone(timezone);

            /*
             * Get the groups that the user belongs to
             */
            ArrayList<UserGroupInfo> userGroupInfos = userSession.getUserGroupsDB().RetrieveByUserId(userInfo.getUserId());

            /*
             * Check if the user only belongs to one group
             */
            String redirectUrl = Consts.STRURL_MyGroups;
            if (userGroupInfos.size() == 1) {
                /*
                 * Get the group information and update the user session
                 */
                int groupId = userGroupInfos.get(0).getGroupId();
                GroupInfo groupInfo = userSession.getGroupsDB().RetrieveById(groupId);

                /*
                 * Check if this is the SuperUser group
                 */
                if (groupInfo.getName().equals(GroupTypes.STR_BuiltInSuperUser) == true) {
                    userSession.setGroupname(groupInfo.getName());
                    userSession.setGroupId(groupId);
                    userSession.setAdmin(true);
                    redirectUrl = Consts.STRURL_Home;
                }
            }

            /*
             * Add the user session information to the session
             */
            this.serviceBrokerSession.setUserSession(userSession);

            /*
             * Redirect to the specified page
             */
            FacesContext.getCurrentInstance().getExternalContext().redirect(redirectUrl);

        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /*
         * Navigate to the specified page, if null then stay on same page
         */
        return null;
    }

    /**
     *
     * @param message
     */
    private void ShowMessageError(String message) {
        this.holMessage = message;
        this.holMessageClass = Consts.STRSTL_ErrorMessage;
    }
}
