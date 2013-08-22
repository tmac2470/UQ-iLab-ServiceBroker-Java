/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.labsidescheduling.client;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.processagent.StatusReport;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.database.types.SystemSupportInfo;
import uq.ilabs.library.labsidescheduling.client.Consts;
import uq.ilabs.library.labsidescheduling.client.LabsideSchedulingSession;
import uq.ilabs.library.processagent.proxy.ProcessAgentAPI;

/**
 *
 * @author uqlpayne
 */
@Named(value = "selfRegistrationBean")
@SessionScoped
public class SelfRegistrationBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = SelfRegistrationBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_SaveSuccessful_arg = "%s: Information saved successfully.";
    private static final String STR_UpdateSuccessful_arg = "%s: Information updated successfully.";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceNotRegistered = "Service is not registered yet!";
    private static final String STRERR_ServiceName = "Service Name";
    private static final String STRERR_ServiceGuid = "Service Guid";
    private static final String STRERR_ServiceUrl = "Service Url";
    private static final String STRERR_ServicePasskey = "Service Passkey";
    private static final String STRERR_ClientUrl = "Client Url";
    private static final String STRERR_ContactEmail = "ContactEmail";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_SaveFailed_arg = "%s: Information could not be saved.";
    private static final String STRERR_UpdateFailed_arg = "%s: Information could not be updated.";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private final ProcessAgentTypes processAgentType = ProcessAgentTypes.LSS;
    private LabsideSchedulingSession labsideSchedulingSession;
    private ProcessAgentsDB processAgentsDB;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hitServiceName;
    private String hitServiceUrl;
    private String hitServiceGuid;
    private String hitServicePasskey;
    private String hitClientUrl;
    private String htaDescription;
    private String hitLocation;
    private String hitInfoUrl;
    private String hitContactName;
    private String hitContactEmail;
    private boolean registered;
    private boolean configured;
    private String holMessage;
    private String holMessageClass;

    public String getHitServiceName() {
        return hitServiceName;
    }

    public void setHitServiceName(String hitServiceName) {
        this.hitServiceName = hitServiceName;
    }

    public String getHitServiceUrl() {
        return hitServiceUrl;
    }

    public void setHitServiceUrl(String hitServiceUrl) {
        this.hitServiceUrl = hitServiceUrl;
    }

    public String getHitServiceGuid() {
        return hitServiceGuid;
    }

    public void setHitServiceGuid(String hitServiceGuid) {
        this.hitServiceGuid = hitServiceGuid;
    }

    public String getHitServicePasskey() {
        return hitServicePasskey;
    }

    public void setHitServicePasskey(String hitServicePasskey) {
        this.hitServicePasskey = hitServicePasskey;
    }

    public String getHitClientUrl() {
        return hitClientUrl;
    }

    public void setHitClientUrl(String hitClientUrl) {
        this.hitClientUrl = hitClientUrl;
    }

    public String getHtaDescription() {
        return htaDescription;
    }

    public void setHtaDescription(String htaDescription) {
        this.htaDescription = htaDescription;
    }

    public String getHitLocation() {
        return hitLocation;
    }

    public void setHitLocation(String hitLocation) {
        this.hitLocation = hitLocation;
    }

    public String getHitInfoUrl() {
        return hitInfoUrl;
    }

    public void setHitInfoUrl(String hitInfoUrl) {
        this.hitInfoUrl = hitInfoUrl;
    }

    public String getHitContactName() {
        return hitContactName;
    }

    public void setHitContactName(String hitContactName) {
        this.hitContactName = hitContactName;
    }

    public String getHitContactEmail() {
        return hitContactEmail;
    }

    public void setHitContactEmail(String hitContactEmail) {
        this.hitContactEmail = hitContactEmail;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isConfigured() {
        return configured;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of SelfRegistrationBean
     */
    public SelfRegistrationBean() {
        this.labsideSchedulingSession = (LabsideSchedulingSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_LabsideScheduling);
        this.processAgentsDB = this.labsideSchedulingSession.getServiceManagement().getProcessAgentsDB();
    }

    public void pageLoad() {
        /*
         * Check if user is logged in
         */
        if (this.labsideSchedulingSession.getUserSession() == null) {
            throw new ViewExpiredException();
        }

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.PopulateProcessAgentInfo();
        }
    }

    /**
     *
     * @return
     */
    public String actionCreateGuid() {

        this.hitServiceGuid = UUID.randomUUID().toString();

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionCreatePasskey() {

        this.hitServicePasskey = UUID.randomUUID().toString();

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionSave() {
        final String methodName = "actionSave";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Parse the web page information
         */
        ProcessAgentInfo processAgentInfo = this.Parse(null);
        if (processAgentInfo != null) {
            try {
                /*
                 * Check if default ProcessAgentInfo exists, if it does then delete it
                 */
                ProcessAgentInfo selfProcessAgentInfo = this.processAgentsDB.RetrieveSelf();
                if (selfProcessAgentInfo != null) {
                    this.processAgentsDB.Delete(selfProcessAgentInfo.getAgentId());
                }

                /*
                 * Save the information
                 */
                if (this.processAgentsDB.Add(processAgentInfo) < 0) {
                    throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                }

                /*
                 * Update session
                 */
                this.labsideSchedulingSession.setTitle(processAgentInfo.getAgentName());
                this.labsideSchedulingSession.setServiceGuid(processAgentInfo.getAgentGuid());
                this.labsideSchedulingSession.setContactEmail(processAgentInfo.getSystemSupportInfo().getContactEmail());

                /*
                 * Information saved successfully
                 */
                this.registered = true;
                this.configured = true;
                this.ShowMessageInfo(String.format(STR_SaveSuccessful_arg, processAgentInfo.getAgentName()));

            } catch (Exception ex) {
                this.ShowMessageError(ex.getMessage());
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionUpdate() {
        final String methodName = "actionUpdate";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Get ProcessAgentInfo for the Service
         */
        ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveSelf();

        /*
         * Parse the web page information
         */
        processAgentInfo = this.Parse(processAgentInfo);
        if (processAgentInfo != null) {
            try {
                /*
                 * Update the information
                 */
                if (this.processAgentsDB.Update(processAgentInfo) == false) {
                    throw new RuntimeException(String.format(STRERR_UpdateFailed_arg, processAgentInfo.getAgentName()));
                }
                if (this.processAgentsDB.Update(processAgentInfo.getSystemSupportInfo()) == false) {
                    throw new RuntimeException(String.format(STRERR_UpdateFailed_arg, processAgentInfo.getAgentName()));
                }

                /*
                 * Information updated successfully
                 */
                this.ShowMessageInfo(String.format(STR_UpdateSuccessful_arg, processAgentInfo.getAgentName()));

            } catch (Exception ex) {
                this.ShowMessageError(ex.getMessage());
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionTest() {
        final String methodName = "actionTest";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Get a proxy to the ProcessAgent service
             */
            ProcessAgentAPI processAgentAPI = new ProcessAgentAPI((this.hitServiceUrl));

            /*
             * Get the status of the ProcessAgent
             */
            StatusReport statusReport = processAgentAPI.GetStatus();
            if (statusReport == null) {
                throw new NullPointerException(ProcessAgentAPI.STRERR_ProcessAgentUnaccessible);
            }

            ShowMessageInfo(String.format(ProcessAgentAPI.STR_ServiceStatus_arg2,
                    statusReport.isOnline() ? ProcessAgentAPI.STR_Online : ProcessAgentAPI.STR_Offline, statusReport.getPayload()));

        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return ProcessAgentInfo
     */
    private ProcessAgentInfo Parse(ProcessAgentInfo processAgentInfo) {
        final String methodName = "Parse";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check if ProcessAgentInfo has been provided
             */
            if (processAgentInfo == null) {
                /*
                 * Create instance of ProcessAgentInfo ready to fill in
                 */
                processAgentInfo = new ProcessAgentInfo();
                processAgentInfo.setAgentType(this.processAgentType);
                processAgentInfo.setSelf(true);
                processAgentInfo.setOutCoupon(new Coupon());
                processAgentInfo.setInCoupon(new Coupon());
                processAgentInfo.setSystemSupportInfo(new SystemSupportInfo());

                /*
                 * Check that Service Name has been entered
                 */
                this.hitServiceName = this.hitServiceName.trim();
                if (this.hitServiceName.isEmpty() == true) {
                    throw new RuntimeException(String.format(STRERR_NotSpecified_arg, STRERR_ServiceName));
                }
                processAgentInfo.setAgentName(this.hitServiceName);

                /*
                 * Check that Service Guid has been entered
                 */
                this.hitServiceGuid = this.hitServiceGuid.trim();
                if (this.hitServiceGuid.isEmpty() == true) {
                    throw new RuntimeException(String.format(STRERR_NotSpecified_arg, STRERR_ServiceGuid));
                }
                processAgentInfo.setAgentGuid(this.hitServiceGuid);

                /*
                 * Check that Service Passkey has been entered
                 */
                this.hitServicePasskey = this.hitServicePasskey.trim();
                if (this.hitServicePasskey.isEmpty() == true) {
                    throw new RuntimeException(String.format(STRERR_NotSpecified_arg, STRERR_ServicePasskey));
                }
                processAgentInfo.setIssuerGuid(this.hitServicePasskey);
            }

            SystemSupportInfo systemSupportInfo = processAgentInfo.getSystemSupportInfo();

            /*
             * Check that Service Url has been entered
             */
            this.hitServiceUrl = this.hitServiceUrl.trim();
            if (this.hitServiceUrl.isEmpty() == true) {
                throw new RuntimeException(String.format(STRERR_NotSpecified_arg, STRERR_ServiceUrl));
            }
            processAgentInfo.setServiceUrl(this.hitServiceUrl);

            /*
             * Check that Client Url has been entered
             */
            this.hitClientUrl = this.hitClientUrl.trim();
            if (this.hitClientUrl.isEmpty() == true) {
                throw new RuntimeException(String.format(STRERR_NotSpecified_arg, STRERR_ClientUrl));
            }
            processAgentInfo.setClientUrl(this.hitClientUrl);

            /*
             * Check that Contact Email has been entered
             */
            this.hitContactEmail = this.hitContactEmail.trim();
            if (this.hitContactEmail.isEmpty() == true) {
                throw new RuntimeException(String.format(STRERR_NotSpecified_arg, STRERR_ContactEmail));
            }
            systemSupportInfo.setContactEmail(this.hitContactEmail);

            /*
             * Optional Information
             */
            this.htaDescription = this.htaDescription.trim();
            systemSupportInfo.setDescription(this.htaDescription.isEmpty() ? null : this.htaDescription);
            this.hitLocation = this.hitLocation.trim();
            systemSupportInfo.setLocation(this.hitLocation.isEmpty() ? null : this.hitLocation);
            this.hitInfoUrl = this.hitInfoUrl.trim();
            systemSupportInfo.setInfoUrl(this.hitInfoUrl.isEmpty() ? null : this.hitInfoUrl);
            this.hitContactName = this.hitContactName.trim();
            systemSupportInfo.setContactName(this.hitContactName.isEmpty() ? null : this.hitContactName);
        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            processAgentInfo = null;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return processAgentInfo;
    }

    /**
     *
     */
    private void PopulateProcessAgentInfo() {
        final String methodName = "PopulateProcessAgentInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Check if ProcessAgentInfo for self is registered
             */
            ProcessAgentInfo processAgentInfo = this.processAgentsDB.RetrieveSelf();
            if (processAgentInfo == null) {
                throw new RuntimeException(STRERR_ServiceNotRegistered);
            }
            this.registered = true;

            /*
             * Update web page
             */
            this.hitServiceName = processAgentInfo.getAgentName();
            this.hitServiceGuid = processAgentInfo.getAgentGuid();
            this.hitServiceUrl = processAgentInfo.getServiceUrl();
            this.hitServicePasskey = processAgentInfo.getIssuerGuid();
            this.hitClientUrl = processAgentInfo.getClientUrl();

            SystemSupportInfo systemSupportInfo = processAgentInfo.getSystemSupportInfo();
            this.htaDescription = systemSupportInfo.getDescription();
            this.hitLocation = systemSupportInfo.getLocation();
            this.hitInfoUrl = systemSupportInfo.getInfoUrl();
            this.hitContactName = systemSupportInfo.getContactName();
            this.hitContactEmail = systemSupportInfo.getContactEmail();

            /*
             * Check if ProcessAgentInfo for self is configured
             */
            if (processAgentInfo.getAgentGuid() == null || processAgentInfo.getIssuerGuid() == null) {
                throw new RuntimeException(STRERR_ServiceNotRegistered);
            }
            if (processAgentInfo.getAgentGuid().trim().isEmpty() == true || processAgentInfo.getIssuerGuid().trim().isEmpty() == true) {
                throw new RuntimeException(STRERR_ServiceNotRegistered);
            }

            this.configured = true;
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
