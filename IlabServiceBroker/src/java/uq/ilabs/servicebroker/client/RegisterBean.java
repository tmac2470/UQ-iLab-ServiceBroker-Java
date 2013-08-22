/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.lab.utilities.Password;
import uq.ilabs.library.lab.utilities.SmtpClient;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.types.GroupInfo;
import uq.ilabs.library.servicebroker.database.types.UserGroupInfo;
import uq.ilabs.library.servicebroker.database.types.UserInfo;
import uq.ilabs.library.servicebroker.engine.LabConsts;
import uq.ilabs.library.servicebroker.engine.types.AffiliationTypes;
import uq.ilabs.library.servicebroker.engine.types.GroupTypes;

/**
 *
 * @author uqlpayne
 */
@Named(value = "registerBean")
@SessionScoped
public class RegisterBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = RegisterBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * Strings
     */
    private static final String STR_EmailSubject_arg = "[%s] New User Registration";
    private static final String STR_EmailMessageSent = "Your registration has been forwarded to the administrator.";
    private static final String STR_EmailMessage_arg4 =
            "\n Username: %s\n Name:     %s %s\n Email:    %s\n\n"
            + STR_EmailMessageSent
            + "\nAn email will be sent to you once your request has been processed.\n";
    /*
     * Error messages
     */
    private static final String STRERR_GroupNotFound_arg = "%s - Group not found";
    private static final String STRERR_UsernameAlreadyExists = "Username already exists! Select another username.";
    private static final String STRERR_NotSpecified_arg = "%s - Not specified!";
    private static final String STRERR_Username = "Username";
    private static final String STRERR_FirstName = "First Name";
    private static final String STRERR_LastName = "Last Name";
    private static final String STRERR_ContactEmail = "Contact Email";
    private static final String STRERR_Password = "Password";
    private static final String STRERR_ConfirmPassword = "Confirm Password";
    private static final String STRERR_PasswordMismatch = "Passwords are different!";
    private static final String STRERR_AffiliationNotSelected = "Affiliation not selected!";
    private static final String STRERR_AddNewUserFailed = "Failed to add new user information!";
    private static final String STRERR_AddNewUserToGroupFailed = "Failed to add new user to group information!";
    private static final String STRERR_SendEmailFailed = "Failed to send email!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    private UserSession userSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hitUsername;
    private String hitFirstName;
    private String hitLastname;
    private String hitContactEmail;
    private String hisPassword;
    private String hisConfirmPassword;
    private String[] affiliations;
    private String hsomAffiliation;
    private String[] requestedGroups;
    private String hsomRequestedGroup;
    private String htaRegisterReason;
    private boolean registered;
    private String holMessage;
    private String holMessageClass;

    public String getHitUsername() {
        return hitUsername;
    }

    public void setHitUsername(String hitUsername) {
        this.hitUsername = hitUsername;
    }

    public String getHitFirstName() {
        return hitFirstName;
    }

    public void setHitFirstName(String hitFirstName) {
        this.hitFirstName = hitFirstName;
    }

    public String getHitLastname() {
        return hitLastname;
    }

    public void setHitLastname(String hitLastname) {
        this.hitLastname = hitLastname;
    }

    public String getHitContactEmail() {
        return hitContactEmail;
    }

    public void setHitContactEmail(String hitContactEmail) {
        this.hitContactEmail = hitContactEmail;
    }

    public String getHisPassword() {
        return hisPassword;
    }

    public void setHisPassword(String hisPassword) {
        this.hisPassword = hisPassword;
    }

    public String getHisConfirmPassword() {
        return hisConfirmPassword;
    }

    public void setHisConfirmPassword(String hisConfirmPassword) {
        this.hisConfirmPassword = hisConfirmPassword;
    }

    public String[] getAffiliations() {
        return affiliations;
    }

    public String getHsomAffiliation() {
        return hsomAffiliation;
    }

    public void setHsomAffiliation(String hsomAffiliation) {
        this.hsomAffiliation = hsomAffiliation;
    }

    public String[] getRequestedGroups() {
        return requestedGroups;
    }

    public String getHsomRequestedGroup() {
        return hsomRequestedGroup;
    }

    public void setHsomRequestedGroup(String hsomRequestedGroup) {
        this.hsomRequestedGroup = hsomRequestedGroup;
    }

    public String getHtaRegisterReason() {
        return htaRegisterReason;
    }

    public void setHtaRegisterReason(String htaRegisterReason) {
        this.htaRegisterReason = htaRegisterReason;
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of RegisterBean
     */
    public RegisterBean() {
        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
    }

    /**
     *
     */
    public void pageLoad() {
        final String methodName = "pageLoad";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.userSession = new UserSession(this.serviceBrokerSession.getDbConnection());
            this.affiliations = this.CreateAffiliationsList();
            this.requestedGroups = this.CreateRequestedGroupsList();

            /*
             * Clear the page
             */
            this.actionNew();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @return
     */
    public String actionSubmit() {
        final String methodName = "actionSubmit";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Parse the web page information
             */
            UserInfo userInfo = this.Parse();
            if (userInfo != null) {
                /*
                 * Add information for the new user
                 */
                int userId = this.userSession.getUsersDB().Add(userInfo);
                if (userId < 0) {
                    throw new RuntimeException(STRERR_AddNewUserFailed);
                }

                /*
                 * Get the request group Id if selected
                 */
                int groupId = -1;
                if (this.hsomRequestedGroup.equals(this.requestedGroups[0]) == false) {
                    /*
                     * Get the group information for the selected request group
                     */
                    ArrayList<GroupInfo> groupInfoList = this.userSession.getGroupsDB().RetrieveAllByIsRequest();
                    for (GroupInfo groupInfo : groupInfoList) {
                        /*
                         * Find the request group
                         */
                        if (groupInfo.getName().equals(this.hsomRequestedGroup)) {
                            groupId = groupInfo.getId();
                            break;
                        }
                    }
                } else {
                    /*
                     * Get the group information for the builtin newuser group
                     */
                    GroupInfo groupInfo = this.userSession.getGroupsDB().RetrieveByName(GroupTypes.STR_BuiltInNewUser);
                    groupId = groupInfo.getId();
                }
                if (groupId < 0) {
                    throw new RuntimeException(String.format(STRERR_GroupNotFound_arg, this.hsomRequestedGroup));
                }

                /*
                 * Add the new user to the specified group
                 */
                UserGroupInfo userGroupInfo = new UserGroupInfo(userId, groupId);
                if (this.userSession.getUserGroupsDB().Add(userGroupInfo) < 0) {
                    throw new RuntimeException(STRERR_AddNewUserToGroupFailed);
                }

                /*
                 * Information saved successfully - send an email
                 */
                SmtpClient smtpClient = new SmtpClient();
                smtpClient.getTo().add(userInfo.getContactEmail());
                smtpClient.setFrom(this.serviceBrokerSession.getContactEmail());
                smtpClient.getCc().add(this.serviceBrokerSession.getContactEmail());
                smtpClient.setSubject(String.format(STR_EmailSubject_arg, this.serviceBrokerSession.getTitle()));
                smtpClient.setBody(String.format(STR_EmailMessage_arg4,
                        userInfo.getUsername(), userInfo.getFirstName(), userInfo.getLastName(), userInfo.getContactEmail()));
                if (smtpClient.Send() == false) {
                    throw new RuntimeException(STRERR_SendEmailFailed);
                }

                this.ShowMessageInfo(STR_EmailMessageSent);
            }
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String
     */
    public String actionNew() {
        final String methodName = "actionNew";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Clear information
         */
        this.hitUsername = null;
        this.hitFirstName = null;
        this.hitLastname = null;
        this.hitContactEmail = null;
        this.hisPassword = null;
        this.hisConfirmPassword = null;
        this.hsomAffiliation = (this.affiliations != null && this.affiliations.length > 0) ? this.affiliations[0] : null;

        /*
         * Update controls
         */
        this.registered = false;

        this.ShowMessageInfo(null);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @param userInfo
     * @return UserInfo
     */
    private UserInfo Parse() {
        final String methodName = "Parse";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        UserInfo userInfo;

        try {
            /*
             * Check that a username has been entered
             */
            this.hitUsername = this.hitUsername.toLowerCase().trim();
            if (this.hitUsername.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Username));
            }

            /*
             * Check if username already exists
             */
            if (this.userSession.getUsersDB().RetrieveByUsername(this.hitUsername) != null) {
                throw new IllegalArgumentException(STRERR_UsernameAlreadyExists);
            }

            /*
             * Create instance of UserInfo ready to fill in
             */
            userInfo = new UserInfo();
            userInfo.setUsername(this.hitUsername);

            /*
             * Check that a first name has been entered
             */
            this.hitFirstName = this.hitFirstName.trim();
            if (this.hitFirstName.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_FirstName));
            }
            userInfo.setFirstName(this.hitFirstName);

            /*
             * Check that a last name has been entered
             */
            this.hitLastname = this.hitLastname.trim();
            if (this.hitLastname.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_LastName));
            }
            userInfo.setLastName(this.hitLastname);

            /*
             * Check that a contact email has been entered
             */
            this.hitContactEmail = this.hitContactEmail.toLowerCase().trim();
            if (this.hitContactEmail.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_ContactEmail));
            }
            userInfo.setContactEmail(this.hitContactEmail);

            /*
             * Check that a password has been entered
             */
            this.hisPassword = this.hisPassword.trim();
            if (this.hisPassword.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Password));
            }

            /*
             * Check that a confirm password has been entered
             */
            this.hisConfirmPassword = this.hisConfirmPassword.trim();
            if (this.hisConfirmPassword.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_ConfirmPassword));
            }

            /*
             * Check that the passwords match
             */
            if (this.hisPassword.equals(this.hisConfirmPassword) == false) {
                throw new IllegalArgumentException(STRERR_PasswordMismatch);
            }
            userInfo.setPassword(Password.ToHash(this.hisPassword));

            /*
             * Check that an affiliation has been selected
             */
            if (this.hsomAffiliation.equals(this.affiliations[0]) == true) {
                throw new IllegalArgumentException(STRERR_AffiliationNotSelected);
            }
            userInfo.setAffiliation(this.hsomAffiliation);

            /*
             * Optional Information
             */
            this.htaRegisterReason = this.htaRegisterReason.trim();
            if (this.hitContactEmail.isEmpty() == false) {
                userInfo.setRegisterReason(this.htaRegisterReason);
            }
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            userInfo = null;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return userInfo;
    }

    /**
     *
     * @return
     */
    public String actionCancel() {

        /* Navigate to home page */
        return Consts.STRURL_Home;
    }

    /**
     *
     * @return
     */
    private String[] CreateAffiliationsList() {

        String[] affiliationsList = null;

        try {
            /*
             * Get the list of affiliations
             */
            String[] stringArray = AffiliationTypes.STRINGS;
            if (stringArray != null) {
                affiliationsList = new String[stringArray.length + 1];
                System.arraycopy(stringArray, 0, affiliationsList, 1, stringArray.length);
                affiliationsList[0] = LabConsts.STR_MakeSelection;
            } else {
                affiliationsList = new String[]{""};
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return affiliationsList;
    }

    /**
     *
     * @return
     */
    private String[] CreateRequestedGroupsList() {

        String[] requestedGroupsList = null;

        try {
            /*
             * Get the list of Request Group names
             */
            String[] stringArray = this.userSession.getGroupsDB().GetListOfNamesByIsRequest();
            if (stringArray != null) {
                requestedGroupsList = new String[stringArray.length + 1];
                System.arraycopy(stringArray, 0, requestedGroupsList, 1, stringArray.length);
                requestedGroupsList[0] = LabConsts.STR_MakeSelection;
            } else {
                requestedGroupsList = new String[]{""};
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return requestedGroupsList;
    }

    /**
     *
     * @param message
     */
    private void ShowMessageInfo(String message) {
        this.holMessage = message;
        this.holMessageClass = Consts.STRSTL_InfoMessage;
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
