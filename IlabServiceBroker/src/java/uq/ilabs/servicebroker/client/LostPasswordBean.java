/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.lab.utilities.Password;
import uq.ilabs.library.lab.utilities.SmtpClient;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.database.UsersDB;
import uq.ilabs.library.servicebroker.database.types.UserInfo;

/**
 *
 * @author uqlpayne
 */
@Named(value = "lostPasswordBean")
@SessionScoped
public class LostPasswordBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = LostPasswordBean.class.getName();
    /*
     * String constants
     */
    private static final String STR_EmailSubject_arg = "[%s] - Recover Password";
    private static final String STR_EmailMessage_arg3 =
            "\nUsername:      %s\nContact Email: %s\n"
            + "\nA new password has been generated. For security reasons, please login and use the 'My Account' page to change your password.\n"
            + "\nNew Password:  %s\n";
    private static final String STR_Confirmed = "A new password has been generated and emailed to you.";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_NotSpecified_arg = "%s - Not specified!";
    private static final String STRERR_Unknown_arg = "%s - Unknown!";
    private static final String STRERR_Username = "Username";
    private static final String STRERR_ContactEmail = "Contact email";
    private static final String STRERR_UseRegisteredContactEmail = " Please use your registered contact email.";
    private static final String STRERR_UpdateFailed = "Failed to update user information!";
    private static final String STRERR_SendEmailFailed = "Failed to send email!";
    /*
     * Error messages
     */
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    private UsersDB usersDB;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hitUsername;
    private String hitContactEmail;
    private String holMessage;
    private String holMessageClass;

    public String getHitUsername() {
        return hitUsername;
    }

    public void setHitUsername(String hitUsername) {
        this.hitUsername = hitUsername;
    }

    public String getHitContactEmail() {
        return hitContactEmail;
    }

    public void setHitContactEmail(String hitContactEmail) {
        this.hitContactEmail = hitContactEmail;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of LostPasswordBean
     */
    public LostPasswordBean() {
        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
        this.usersDB = this.serviceBrokerSession.getServiceManagement().getUsersDB();
    }

    /**
     *
     * @return
     */
    public String actionSubmit() {
        try {
            /*
             * Check that a username has been entered
             */
            this.hitUsername = this.hitUsername.toLowerCase().trim();
            if (this.hitUsername.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_Username));
            }

            /*
             * Check that a contact email has been entered
             */
            this.hitContactEmail = this.hitContactEmail.toLowerCase().trim();
            if (this.hitContactEmail.isEmpty() == true) {
                throw new IllegalArgumentException(String.format(STRERR_NotSpecified_arg, STRERR_ContactEmail));
            }

            /*
             * Check if username exists
             */
            UserInfo userInfo = this.usersDB.RetrieveByUsername(this.hitUsername);
            if (userInfo == null) {
                throw new RuntimeException(String.format(STRERR_Unknown_arg, STRERR_Username));
            }

            /*
             * Check if the email address supplied matches the one in the database
             */
            if (this.hitContactEmail.equals(userInfo.getContactEmail()) == false) {
                throw new IllegalArgumentException(String.format(STRERR_Unknown_arg, STRERR_ContactEmail) + STRERR_UseRegisteredContactEmail);
            }

            /*
             * Create a new password
             */
            String password = Password.CreateRandom();
            userInfo.setPassword(Password.ToHash(password));

            /*
             * Update user information
             */
            if (this.usersDB.Update(userInfo) == false) {
                throw new RuntimeException(STRERR_UpdateFailed);
            }

            /*
             * Password generation successful - send an email
             */
            SmtpClient smtpClient = new SmtpClient();
            smtpClient.getTo().add(this.hitContactEmail);
            smtpClient.setFrom(this.serviceBrokerSession.getContactEmail());
            smtpClient.getCc().add(this.serviceBrokerSession.getContactEmail());
            smtpClient.setSubject(String.format(STR_EmailSubject_arg, this.serviceBrokerSession.getTitle()));
            smtpClient.setBody(String.format(STR_EmailMessage_arg3,
                    userInfo.getUsername(), userInfo.getContactEmail(), password));
            if (smtpClient.Send() == false) {
                throw new RuntimeException(STRERR_SendEmailFailed);
            }

            this.ShowMessageInfo(STR_Confirmed);

        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
        }

        /* Navigate to the current page */
        return null;
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
