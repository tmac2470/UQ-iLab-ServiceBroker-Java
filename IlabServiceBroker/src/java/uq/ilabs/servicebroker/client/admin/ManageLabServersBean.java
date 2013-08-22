/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client.admin;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.datatypes.processagent.ProcessAgentTypes;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.database.ProcessAgentsDB;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.database.types.SystemSupportInfo;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.database.IssuedCouponsDB;
import uq.ilabs.library.servicebroker.engine.LabConsts;

/**
 *
 * @author uqlpayne
 */
@Named(value = "manageLabServersBean")
@SessionScoped
public class ManageLabServersBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ManageLabServersBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_SaveSuccessful_arg = "LabServer '%s' saved successfully.";
    private static final String STR_UpdateSuccessful_arg = "LabServer '%s' updated successfully.";
    private static final String STR_DeleteSuccessful_arg = "LabServer '%s' deleted successfully.";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_LabServerName = "LabServer Name";
    private static final String STRERR_LabServerGuid = "LabServer Guid";
    private static final String STRERR_WebServiceUrl = "WebService Url";
    private static final String STRERR_OutgoingPasskey = "Outgoing Passkey";
    private static final String STRERR_IncomingPasskey = "Incoming Passkey";
//    private static final String STRERR_Description = "Description";
//    private static final String STRERR_LoaderScript = "Loader Script";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_AlreadyExists_arg = "LabServer '%s' already exists!";
    private static final String STRERR_RetrieveFailed_arg = "LabServer '%s' could not be retrieved.";
    private static final String STRERR_SaveFailed_arg = "LabServer '%s' could not be saved.";
    private static final String STRERR_UpdateFailed_arg = "LabServer '%s' could not be updated.";
    private static final String STRERR_DeleteFailed_arg = "LabServer '%s' could not be deleted.";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hsomLabServer;
    private String hitLabServerName;
    private String hitLabServerGuid;
    private String hitWebServiceUrl;
    private String hitOutgoingPasskey;
    private String hitIncomingPasskey;
    private String htaDescription;
    private String hitLocation;
    private String hitInfoUrl;
    private String hitContactName;
    private String hitContactEmail;
    private String[] labServers;
    private boolean registered;
    private boolean deleteDisabled;
    private String holMessage;
    private String holMessageClass;

    public String getHsomLabServer() {
        return hsomLabServer;
    }

    public void setHsomLabServer(String hsomLabServer) {
        this.hsomLabServer = hsomLabServer;
    }

    public String getHitLabServerName() {
        return hitLabServerName;
    }

    public void setHitLabServerName(String hitLabServerName) {
        this.hitLabServerName = hitLabServerName;
    }

    public String getHitLabServerGuid() {
        return hitLabServerGuid;
    }

    public void setHitLabServerGuid(String hitLabServerGuid) {
        this.hitLabServerGuid = hitLabServerGuid;
    }

    public String getHitWebServiceUrl() {
        return hitWebServiceUrl;
    }

    public void setHitWebServiceUrl(String hitWebServiceUrl) {
        this.hitWebServiceUrl = hitWebServiceUrl;
    }

    public String getHitOutgoingPasskey() {
        return hitOutgoingPasskey;
    }

    public void setHitOutgoingPasskey(String hitOutgoingPasskey) {
        this.hitOutgoingPasskey = hitOutgoingPasskey;
    }

    public String getHitIncomingPasskey() {
        return hitIncomingPasskey;
    }

    public void setHitIncomingPasskey(String hitIncomingPasskey) {
        this.hitIncomingPasskey = hitIncomingPasskey;
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

    public String[] getLabServers() {
        return labServers;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isDeleteDisabled() {
        return deleteDisabled;
    }

    public String getHolMessage() {
        return holMessage;
    }

    public String getHolMessageClass() {
        return holMessageClass;
    }
    //</editor-fold>

    /**
     * Creates a new instance of ManageLabServersBean
     */
    public ManageLabServersBean() {
        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
    }

    /**
     *
     */
    public void pageLoad() {
        /*
         * Check if user is logged in
         */
        if (this.serviceBrokerSession.getUserSession() == null || this.serviceBrokerSession.getUserSession().isAdmin() == false) {
            throw new ViewExpiredException();
        }

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.labServers = this.CreateLabServerList();

            /*
             * Clear the page
             */
            this.actionNew();
        }
    }

    /**
     *
     * @return
     */
    public String actionSelect() {

        if (this.hsomLabServer != null && this.hsomLabServer.equals(this.labServers[0]) == false) {
            this.PopulateLabServerInfo();
            this.hsomLabServer = this.labServers[0];
            this.ShowMessageInfo(null);
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionCreateGuid() {

        this.hitLabServerGuid = UUID.randomUUID().toString();

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
                long couponId;

                /*
                 * Add the Coupon for the outgoing passkey and update ProcessAgentInfo
                 */
                IssuedCouponsDB issuedCouponsDB = this.serviceBrokerSession.getServiceManagement().getTicketIssuer().getIssuedCouponsDB();
                if ((couponId = issuedCouponsDB.Add(processAgentInfo.getOutCoupon())) < 0) {
                    throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                }
                processAgentInfo.getOutCoupon().setCouponId(couponId);

                /*
                 * Check if the incoming passkey has been provided
                 */
                if (processAgentInfo.getInCoupon() != null) {
                    /*
                     * Add the Coupon for the incoming passkey and update ProcessAgentInfo
                     */
                    if ((couponId = issuedCouponsDB.Add(processAgentInfo.getInCoupon())) < 0) {
                        throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                    }
                    processAgentInfo.getInCoupon().setCouponId(couponId);
                } else {
                    processAgentInfo.setInCoupon(new Coupon());
                }

                /*
                 * Add the ProcessAgentInfo
                 */
                ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
                if (processAgentsDB.Add(processAgentInfo) < 0) {
                    throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                }

                /*
                 * Refresh the LabServer list
                 */
                this.labServers = this.CreateLabServerList();
                this.hsomLabServer = processAgentInfo.getAgentName();

                /*
                 * Information saved successfully
                 */
                this.registered = true;
                this.ShowMessageInfo(String.format(STR_SaveSuccessful_arg, processAgentInfo.getAgentName()));

            } catch (Exception ex) {
                String message = ex.getMessage();
                ShowMessageError(message != null ? message : ex.toString());
                Logfile.WriteError(ex.toString());
            }
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
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
             * Retrieve the ProcessAgentInfo for the specified LabServer
             */
            ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
            ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveByName(this.hitLabServerName);
            if (processAgentInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hitLabServerName));
            }

            /*
             * Retrieve the coupon for the outgoing passkey
             */
            IssuedCouponsDB issuedCouponsDB = this.serviceBrokerSession.getServiceManagement().getTicketIssuer().getIssuedCouponsDB();
            Coupon coupon = issuedCouponsDB.Retrieve(processAgentInfo.getOutCoupon().getCouponId());
            processAgentInfo.setOutCoupon(coupon != null ? coupon : new Coupon());

            /*
             * Retrieve the coupon for the incoming passket if it exists
             */
            coupon = issuedCouponsDB.Retrieve(processAgentInfo.getInCoupon().getCouponId());
            processAgentInfo.setInCoupon(coupon != null ? coupon : new Coupon());

            /*
             * Parse the web page information
             */
            processAgentInfo = this.Parse(processAgentInfo);
            if (processAgentInfo != null) {
                /*
                 * Update the coupon for the outgoing passkey
                 */
                if (issuedCouponsDB.Update(processAgentInfo.getOutCoupon()) == false) {
                    throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                }

                /*
                 * Check if a coupon already exists for the incoming passkey
                 */
                coupon = processAgentInfo.getInCoupon();
                if (coupon.getCouponId() > 0) {
                    if (coupon.getPasskey() == null) {
                        /*
                         * Deleting the passkey so delete the coupon
                         */
                        if (issuedCouponsDB.Delete(coupon.getCouponId()) == false) {
                            throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                        }
                        processAgentInfo.setInCoupon(new Coupon());
                    } else {
                        /*
                         * Update the coupon with the passkey
                         */
                        if (issuedCouponsDB.Update(coupon) == false) {
                            throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                        }
                    }
                } else if (coupon.getPasskey() != null) {
                    /*
                     * Add a Coupon for the incoming passkey
                     */
                    long couponId = issuedCouponsDB.Add(coupon);
                    if (couponId < 0) {
                        throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                    }
                    processAgentInfo.getInCoupon().setCouponId(couponId);
                }

                /*
                 * Update the ProcessAgentInfo
                 */
                if (processAgentsDB.Update(processAgentInfo) == false) {
                    throw new Exception(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                }

                /*
                 * Update the SystemSupportInfo
                 */
                if (processAgentsDB.Update(processAgentInfo.getSystemSupportInfo()) == false) {
                    throw new Exception(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                }

                /*
                 * Information updated successfully
                 */
                this.ShowMessageInfo(String.format(STR_UpdateSuccessful_arg, processAgentInfo.getAgentName()));
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
     * @return
     */
    public String actionDelete() {
        final String methodName = "actionDelete";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Get the ProcessAgentInfo for the selected process agent
             */
            ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
            ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveByName(this.hitLabServerName);
            if (processAgentInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hitLabServerName));
            }

            /*
             * Delete the LabServer
             */
            if (processAgentsDB.Delete(processAgentInfo.getAgentId()) == false) {
                throw new Exception(String.format(STRERR_DeleteFailed_arg, processAgentInfo.getAgentName()));
            }

            /*
             * Refresh the LabServer list and clear the page
             */
            this.labServers = this.CreateLabServerList();
            this.actionNew();

            /*
             * Information deleted successfully
             */
            this.ShowMessageInfo(String.format(STR_DeleteSuccessful_arg, processAgentInfo.getAgentName()));

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
     * @return String
     */
    public String actionNew() {
        final String methodName = "actionNew";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        this.hsomLabServer = this.labServers[0];
        this.hitLabServerName = null;
        this.hitLabServerGuid = null;
        this.hitWebServiceUrl = null;
        this.hitOutgoingPasskey = null;
        this.hitIncomingPasskey = null;
        this.htaDescription = null;
        this.hitLocation = null;
        this.hitInfoUrl = null;
        this.hitContactName = null;
        this.hitContactEmail = null;

        /*
         * Update controls
         */
        this.registered = false;

        this.ShowMessageInfo(null);

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return
     */
    public String actionExport() {
        final String methodName = "actionExport";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Download the result string as an Excel csv file
             */
//            String filename = String.format(STR_CsvFilename_arg2, this.labClientSession.getTitle(), this.retrievedExperimentId);
//            String attachmentCsv = String.format(Consts.STRRSP_AttachmentCsv_arg, filename);
//            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//            externalContext.setResponseContentType(Consts.STRRSP_ContentTypeCsv);
//            externalContext.setResponseHeader(Consts.STRRSP_Disposition, attachmentCsv);
//            try (Writer writer = externalContext.getResponseOutputWriter()) {
//                writer.write(this.csvExperimentResults);
//            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String[]
     */
    private String[] CreateLabServerList() {

        String[] labServerList = null;

        try {
            /*
             * Get the list of LabServer names
             */
            ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
            String[] stringArray = processAgentsDB.GetListOfNamesByType(ProcessAgentTypes.BLS);
            if (stringArray != null) {
                labServerList = new String[stringArray.length + 1];
                System.arraycopy(stringArray, 0, labServerList, 1, stringArray.length);
                labServerList[0] = LabConsts.STR_MakeSelection;
            } else {
                labServerList = new String[]{""};
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return labServerList;
    }

    /**
     *
     */
    private void PopulateLabServerInfo() {
        final String methodName = "PopulateLabServerInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Retrieve the ProcessAgentInfo for the specified LabServer
             */
            ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
            ProcessAgentInfo processAgentInfo = processAgentsDB.RetrieveByName(this.hsomLabServer);
            if (processAgentInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hsomLabServer));
            }

            /*
             * Retrieve the coupon for the outgoing passkey
             */
            IssuedCouponsDB issuedCouponsDB = this.serviceBrokerSession.getServiceManagement().getTicketIssuer().getIssuedCouponsDB();
            Coupon coupon = issuedCouponsDB.Retrieve(processAgentInfo.getOutCoupon().getCouponId());
            processAgentInfo.setOutCoupon(coupon != null ? coupon : new Coupon());

            /*
             * Retrieve the coupon for the incoming passkey if it exists
             */
            coupon = issuedCouponsDB.Retrieve(processAgentInfo.getInCoupon().getCouponId());
            processAgentInfo.setInCoupon(coupon != null ? coupon : new Coupon());

            /*
             * Update information
             */
            this.hitLabServerName = processAgentInfo.getAgentName();
            this.hitLabServerGuid = processAgentInfo.getAgentGuid();
            this.hitWebServiceUrl = processAgentInfo.getServiceUrl();
            this.hitOutgoingPasskey = processAgentInfo.getOutCoupon().getPasskey();
            this.hitIncomingPasskey = processAgentInfo.getInCoupon().getPasskey();

            SystemSupportInfo systemSupportInfo = processAgentInfo.getSystemSupportInfo();
            this.htaDescription = systemSupportInfo.getDescription();
            this.hitLocation = systemSupportInfo.getLocation();
            this.hitInfoUrl = systemSupportInfo.getInfoUrl();
            this.hitContactName = systemSupportInfo.getContactName();
            this.hitContactEmail = systemSupportInfo.getContactEmail();

            /*
             * Update controls
             */
            this.registered = true;
            this.deleteDisabled = false;
        } catch (Exception ex) {
            ShowMessageError(ex.getMessage());
            Logfile.WriteError(ex.toString());
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);
    }

    /**
     *
     * @param processAgentInfo
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
                processAgentInfo.setAgentType(ProcessAgentTypes.BLS);
                processAgentInfo.setOutCoupon(new Coupon());

                /*
                 * Check that LabServer Name has been entered
                 */
                this.hitLabServerName = this.hitLabServerName.trim();
                if (this.hitLabServerName.isEmpty() == true) {
                    throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_LabServerName));
                }
                processAgentInfo.setAgentName(this.hitLabServerName);

                /*
                 * Check if LabServer Name already exists
                 */
                ProcessAgentsDB processAgentsDB = this.serviceBrokerSession.getServiceManagement().getProcessAgentsDB();
                if (processAgentsDB.RetrieveByName(this.hitLabServerName) != null) {
                    throw new Exception(String.format(STRERR_AlreadyExists_arg, this.hitLabServerName));
                }

                /*
                 * Check that LabServer Guid has been entered
                 */
                this.hitLabServerGuid = this.hitLabServerGuid.trim();
                if (this.hitLabServerGuid.isEmpty() == true) {
                    throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_LabServerGuid));
                }
                processAgentInfo.setAgentGuid(this.hitLabServerGuid);
            }

            /*
             * Check that WebService Url has been entered
             */
            this.hitWebServiceUrl = this.hitWebServiceUrl.trim();
            if (this.hitWebServiceUrl.isEmpty() == true) {
                throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_WebServiceUrl));
            }
            processAgentInfo.setServiceUrl(this.hitWebServiceUrl);

            /*
             * Check that Outgoing Passkey has been entered
             */
            this.hitOutgoingPasskey = this.hitOutgoingPasskey.trim();
            if (this.hitOutgoingPasskey.isEmpty() == true) {
                throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_OutgoingPasskey));
            }
            processAgentInfo.getOutCoupon().setPasskey(this.hitOutgoingPasskey);

            /*
             * Check if the Incoming Passkey has been entered - optional
             */
            this.hitIncomingPasskey = this.hitIncomingPasskey.trim();
            if (this.hitIncomingPasskey.isEmpty() == false) {
                processAgentInfo.setInCoupon(new Coupon(this.hitIncomingPasskey));
            }

            /*
             * Optional Information
             */
            SystemSupportInfo systemSupportInfo = processAgentInfo.getSystemSupportInfo();
            this.htaDescription = this.htaDescription.trim();
            systemSupportInfo.setDescription(this.htaDescription.isEmpty() ? null : this.htaDescription);
            this.hitLocation = this.hitLocation.trim();
            systemSupportInfo.setLocation(this.hitLocation.isEmpty() ? null : this.hitLocation);
            this.hitInfoUrl = this.hitInfoUrl.trim();
            systemSupportInfo.setInfoUrl(this.hitInfoUrl.isEmpty() ? null : this.hitInfoUrl);
            this.hitContactName = this.hitContactName.trim();
            systemSupportInfo.setContactName(this.hitContactName.isEmpty() ? null : this.hitContactName);
            this.hitContactEmail = this.hitContactEmail.trim();
            systemSupportInfo.setContactEmail(this.hitContactEmail.isEmpty() ? null : this.hitContactEmail);

        } catch (Exception ex) {
            this.ShowMessageError(ex.getMessage());
            processAgentInfo = null;
        }

        Logfile.WriteCompleted(logLevel, STR_ClassName, methodName);

        return processAgentInfo;
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
