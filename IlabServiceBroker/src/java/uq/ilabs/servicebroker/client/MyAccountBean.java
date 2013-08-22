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
import uq.ilabs.library.lab.utilities.Password;
import uq.ilabs.library.lab.utilities.SmtpClient;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.database.types.UserInfo;

/**
 *
 * @author uqlpayne
 */
@Named(value = "myAccountBean")
@SessionScoped
public class MyAccountBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = MyAccountBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * Strings
     */
    private static final String STR_User_arg = "User '%s' ";
    private static final String STR_EmailSubject_arg = "[%s] Account Update Confirmation";
    private static final String STR_EmailMessage_arg3 =
            "\n Username: %s\n Name:     %s\n Email:    %s\n"
            + "\nFor security reasons, your password has not been included in this message.\n";
    private static final String STR_UpdateSuccessful_arg = STR_User_arg + "updated successfully.";
    private static final String STR_UpdateSuccessfulConfirmationSent_arg = STR_UpdateSuccessful_arg + " A confirmation has been sent to the contact email address specified.";
    /*
     * Error messages
     */
    private static final String STRERR_NotSpecified_arg = "%s - Not specified!";
    private static final String STRERR_FirstName = "First name";
    private static final String STRERR_LastName = "Last name";
    private static final String STRERR_ContactEmail = "Contact email";
    private static final String STRERR_Password = "Password";
    private static final String STRERR_ConfirmPassword = "Confirm Password";
    private static final String STRERR_PasswordMismatch = "Passwords are different!";
    private static final String STRERR_RetrieveFailed_arg = STR_User_arg + "could not be retrieved.";
    private static final String STRERR_UpdateFailed_arg = STR_User_arg + "could not be updated.";
    private static final String STRERR_SendEmailFailed = "Failed to send email!";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hitUsername;
    private String hitFirstName;
    private String hitLastname;
    private String hitContactEmail;
    private String hisPassword;
    private String hisConfirmPassword;
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

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of MyAccountBean
     */
    public MyAccountBean() {
        final String methodName = "MyAccountBean";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     */
    public void pageLoad() {
        final String methodName = "pageLoad";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Check if user is logged in
         */
        if (this.serviceBrokerSession.getUserSession() == null) {
            throw new ViewExpiredException();
        }

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.PopulateUserInfo();
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @return String
     */
    public String actionUpdate() {
        final String methodName = "actionUpdate";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Get the UserInfo for the selected Username
             */
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            UserInfo userInfo = userSession.getUsersDB().RetrieveByUsername(this.hitUsername);
            if (userInfo == null) {
                throw new NullPointerException(String.format(STRERR_RetrieveFailed_arg, this.hitUsername));
            }

            /*
             * Parse the web page information
             */
            userInfo = this.Parse(userInfo);
            if (userInfo != null) {
                /*
                 * Update the information
                 */
                if (userSession.getUsersDB().Update(userInfo) == false) {
                    throw new RuntimeException(String.format(STRERR_UpdateFailed_arg, userInfo.getUsername()));
                }

                /*
                 * Account update successful - send an email if ServiceBroker's contact email is specified
                 */
                String contactEmail = this.serviceBrokerSession.getContactEmail();
                if (contactEmail != null) {
                    SmtpClient smtpClient = new SmtpClient();
                    smtpClient.getTo().add(this.hitContactEmail);
                    smtpClient.setFrom(this.serviceBrokerSession.getContactEmail());
                    smtpClient.getCc().add(this.serviceBrokerSession.getContactEmail());
                    smtpClient.setSubject(String.format(STR_EmailSubject_arg, this.serviceBrokerSession.getTitle()));
                    smtpClient.setBody(String.format(STR_EmailMessage_arg3,
                            userInfo.getUsername(), userInfo.getFirstName() + " " + userInfo.getLastName(), userInfo.getContactEmail()));
                    if (smtpClient.Send() == false) {
                        throw new RuntimeException(STRERR_SendEmailFailed);
                    }

                    /*
                     * Information updated successfully and confirmation email sent
                     */
                    ShowMessageInfo(String.format(STR_UpdateSuccessfulConfirmationSent_arg, userInfo.getUsername()));
                } else {
                    /*
                     * Information updated successfully but no confirmation email sent
                     */
                    ShowMessageInfo(String.format(STR_UpdateSuccessful_arg, userInfo.getUsername()));
                }
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
     * @param userInfo
     * @return UserInfo
     */
    private UserInfo Parse(UserInfo userInfo) {
        final String methodName = "Parse";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
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
             * Check if a password has been entered
             */
            this.hisPassword = this.hisPassword.trim();
            if (this.hisPassword.isEmpty() == false) {
                /*
                 * Check that a confirm password has been entered
                 */
                this.hisConfirmPassword = this.hisConfirmPassword.trim();
                if (this.hisConfirmPassword.isEmpty() == true) {
                    throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_ConfirmPassword));
                }
            }

            /*
             * Check if a confirm password has been entered
             */
            this.hisConfirmPassword = this.hisConfirmPassword.trim();
            if (this.hisConfirmPassword.isEmpty() == false) {
                /*
                 * Check that a password has been entered
                 */
                this.hisPassword = this.hisPassword.trim();
                if (this.hisPassword.isEmpty() == true) {
                    throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Password));
                }
            }

            /*
             * If a password has been entered, check that the passwords match
             */
            if (this.hisPassword.isEmpty() == false) {
                if (this.hisPassword.equals(this.hisConfirmPassword) == false) {
                    throw new IllegalArgumentException(STRERR_PasswordMismatch);
                }
                userInfo.setPassword(Password.ToHash(this.hisPassword));
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
     */
    private void PopulateUserInfo() {
        final String methodName = "PopulateUserInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            UserSession userSession = this.serviceBrokerSession.getUserSession();
            UserInfo userInfo = userSession.getUsersDB().RetrieveByUsername(this.serviceBrokerSession.getUserSession().getUsername());
            this.hitUsername = userInfo.getUsername();
            this.hitFirstName = userInfo.getFirstName();
            this.hitLastname = userInfo.getLastName();
            this.hitContactEmail = userInfo.getContactEmail();

            this.ShowMessageInfo(null);
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
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
