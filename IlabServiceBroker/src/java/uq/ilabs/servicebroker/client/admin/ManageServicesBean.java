/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uq.ilabs.servicebroker.client.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import uq.ilabs.library.datatypes.processagent.ProcessAgent;
import uq.ilabs.library.datatypes.processagent.ServiceDescription;
import uq.ilabs.library.datatypes.ticketing.Coupon;
import uq.ilabs.library.lab.utilities.Logfile;
import uq.ilabs.library.processagent.proxy.ProcessAgentAPI;
import uq.ilabs.library.processagent.ProcessAgents;
import uq.ilabs.library.processagent.SystemSupportPayload;
import uq.ilabs.library.processagent.database.types.ProcessAgentInfo;
import uq.ilabs.library.processagent.database.types.SystemSupportInfo;
import uq.ilabs.library.servicebroker.client.Consts;
import uq.ilabs.library.servicebroker.client.ServiceBrokerSession;
import uq.ilabs.library.servicebroker.client.UserSession;
import uq.ilabs.library.servicebroker.engine.LabConsts;
import uq.ilabs.library.servicebroker.ticketissuer.TicketIssuer;
import uq.ilabs.servicebroker.client.types.ListItem;

/**
 *
 * @author uqlpayne
 */
@Named(value = "manageServicesBean")
@SessionScoped
public class ManageServicesBean implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Constants">
    private static final String STR_ClassName = ManageServicesBean.class.getName();
    private static final Level logLevel = Level.CONFIG;
    /*
     * String constants
     */
    private static final String STR_Service_arg = "Service '%s' ";
    private static final String STR_RegisterSuccessful_arg = STR_Service_arg + "registered successfully.";
    private static final String STR_ServiceNameType_arg2 = "%s:%s";
    /*
     * String constants for exception messages
     */
    private static final String STRERR_ServiceUrl = "Service Url";
    private static final String STRERR_ServicePasskey = "Service Passkey";
    private static final String STRERR_NotSpecified_arg = "%s: Not specified!";
    private static final String STRERR_RetrieveFailed_arg = STR_Service_arg + "could not be retrieved.";
    private static final String STRERR_SaveFailed_arg = STR_Service_arg + "could not be saved.";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private ServiceBrokerSession serviceBrokerSession;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private String hsomService;
    private String hitServiceName;
    private String hitServiceUrl;
    private String hitServiceGuid;
    private String hitServicePasskey;
    private String hitClientUrl;
    private String hitServiceType;
    private String htaDescription;
    private String hitLocation;
    private String hitInfoUrl;
    private String hitContactName;
    private String hitContactEmail;
    //
    private ArrayList<ListItem> serviceList;
    private boolean registered;
    private String holMessage;
    private String holMessageClass;

    public String getHsomService() {
        return hsomService;
    }

    public void setHsomService(String hsomService) {
        this.hsomService = hsomService;
    }

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

    public String getHitServiceType() {
        return hitServiceType;
    }

    public void setHitServiceType(String hitServiceType) {
        this.hitServiceType = hitServiceType;
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

    public ArrayList<ListItem> getServiceList() {
        return serviceList;
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
     * Creates a new instance of ManageServicesBean
     */
    public ManageServicesBean() {
        this.serviceBrokerSession = (ServiceBrokerSession) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(Consts.STRSSN_ServiceBroker);
    }

    /**
     *
     */
    public void pageLoad() {
        /*
         * Check if administrator is logged in
         */
        UserSession userSession = this.serviceBrokerSession.getUserSession();
        if (userSession == null || userSession.isAdmin() == false) {
            throw new ViewExpiredException();
        }

        if (FacesContext.getCurrentInstance().isPostback() == false) {
            /*
             * Not a postback, initialise page controls
             */
            this.serviceList = this.CreateServiceList();

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

        if (this.hsomService != null && this.serviceList != null && this.serviceList.size() > 0) {
            String value = this.serviceList.get(0).getValue();
            if (this.hsomService.equals(value) == false) {
                this.PopulateServiceInfo();
                this.hsomService = value;
                this.ShowMessageInfo(null);
            }
        }

        /* Navigate to the current page */
        return null;
    }

    /**
     *
     * @return String
     */
    public String actionRegister() {
        final String methodName = "actionRegister";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        /*
         * Parse the web page information
         */
        ProcessAgentInfo processAgentInfo = this.Parse(null);
        if (processAgentInfo != null) {
            try {
                /*
                 * Get self information
                 */
                ProcessAgents processAgents = this.serviceBrokerSession.getServiceManagement().getProcessAgents();
                ProcessAgent selfProcessAgent = processAgents.RetrieveSelf();

                /*
                 * Create coupons for the ProcessAgent
                 */
                TicketIssuer ticketIssuer = this.serviceBrokerSession.getServiceManagement().getTicketIssuer();
                Coupon inCoupon = ticketIssuer.CreateCoupon();
                Coupon outCoupon = ticketIssuer.CreateCoupon();

                ProcessAgentAPI processAgentAPI = null;

                try {
                    /*
                     * Pass on to ProcessAgent for processing
                     */
                    processAgentAPI = new ProcessAgentAPI(processAgentInfo.getServiceUrl());
                    processAgentAPI.setAuthHeaderInitPasskey(processAgentInfo.getOutCoupon().getPasskey());
                    ProcessAgent processAgent = processAgentAPI.InstallDomainCredentials(selfProcessAgent, inCoupon, outCoupon);
                    if (processAgent == null) {
                        throw new RuntimeException("InstallDomainCredentials() returned null");
                    }

                    /*
                     * Create the ProcessAgentInfo for the ProcessAgent
                     */
                    processAgentInfo.setProcessAgent(processAgent);
                    processAgentInfo.setInCoupon(outCoupon);
                    processAgentInfo.setOutCoupon(inCoupon);
                    processAgentInfo.setIssuerGuid(ticketIssuer.getIssuerGuid());

                    /*
                     * Create the ProcessAgentInfo
                     */
                    if (processAgents.Create(processAgentInfo) < 0) {
                        throw new RuntimeException(String.format(STRERR_SaveFailed_arg, processAgentInfo.getAgentName()));
                    }

                } catch (Exception ex) {
                    /*
                     * Delete the issued coupons
                     */
                    if (outCoupon != null) {
                        ticketIssuer.DeleteCoupon(outCoupon.getCouponId());
                    }
                    if (inCoupon != null) {
                        ticketIssuer.DeleteCoupon(inCoupon.getCouponId());
                    }

                    throw ex;
                }

                /*
                 * Register SystemSupportInfo
                 */
                SystemSupportInfo systemSupportInfo = processAgents.RetrieveSystemSupportInfoSelf();
                if (systemSupportInfo != null) {
                    SystemSupportPayload systemSupportPayload = new SystemSupportPayload(systemSupportInfo);
                    String serviceProviderInfo = systemSupportPayload.ToXmlString();
                    ServiceDescription[] serviceDescriptions = new ServiceDescription[]{
                        new ServiceDescription(serviceProviderInfo, null, ServiceDescription.STR_RequestSystemSupport)
                    };
                    processAgentAPI.setAuthHeaderAgentGuid(selfProcessAgent.getAgentGuid());
                    processAgentAPI.setAuthHeaderCoupon(inCoupon);
                    processAgentAPI.Register(hitServiceGuid, serviceDescriptions);
                }

                /*
                 * Refresh the services list and repopulate ProcessAgentInfo information
                 */
                this.serviceList = this.CreateServiceList();
                this.hsomService = processAgentInfo.getAgentName();
                this.PopulateServiceInfo();

                /*
                 * Information saved successfully
                 */
                this.registered = true;
                this.ShowMessageInfo(String.format(STR_RegisterSuccessful_arg, processAgentInfo.getAgentName()));

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
             * Information updated successfully
             */
//            this.ShowMessageInfo(String.format(STR_UpdateSuccessful_arg, processAgentInfo.getAgentName()));
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
    public String actionDelete() {
        final String methodName = "actionDelete";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Information deleted successfully
             */
//            this.ShowMessageInfo(String.format(STR_DeleteSuccessful_arg, processAgentInfo.getAgentName()));
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

        this.hsomService = (this.serviceList != null && this.serviceList.size() > 0)
                ? this.serviceList.get(0).getValue() : null;
        this.hitServiceName = null;
        this.hitServiceGuid = null;
        this.hitServiceUrl = null;
        this.hitServicePasskey = null;
        this.hitClientUrl = null;
        this.hitServiceType = null;
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
     * @return ArrayList of ListItem
     */
    private ArrayList<ListItem> CreateServiceList() {

        ArrayList<ListItem> itemsList = null;

        try {
            /*
             * Get the list of ProcessAgent names
             */
            ProcessAgents processAgents = this.serviceBrokerSession.getServiceManagement().getProcessAgents();
            String[] stringArray = processAgents.GetNamesAll();
            if (stringArray != null) {
                itemsList = new ArrayList<>();
                itemsList.add(new ListItem(LabConsts.STR_MakeSelection, LabConsts.STR_MakeSelection));
                for (String string : stringArray) {
                    ProcessAgentInfo processAgentInfo = processAgents.RetrieveByName(string);
                    if (processAgentInfo.isSelf() == false) {
                        String label = String.format(STR_ServiceNameType_arg2, processAgentInfo.getAgentType().name(), processAgentInfo.getAgentName());
                        itemsList.add(new ListItem(label, processAgentInfo.getAgentName()));
                    }
                }
            }
        } catch (Exception ex) {
            Logfile.WriteError(ex.toString());
        }

        return itemsList;
    }

    /**
     *
     */
    private void PopulateServiceInfo() {
        final String methodName = "PopulateServiceInfo";
        Logfile.WriteCalled(logLevel, STR_ClassName, methodName);

        try {
            /*
             * Retrieve the ProcessAgentInfo for the specified service
             */
            ProcessAgents processAgents = this.serviceBrokerSession.getServiceManagement().getProcessAgents();
            ProcessAgentInfo processAgentInfo = processAgents.RetrieveByName(this.hsomService);
            if (processAgentInfo == null) {
                throw new Exception(String.format(STRERR_RetrieveFailed_arg, this.hsomService));
            }

            /*
             * Update information
             */
            this.hitServiceName = processAgentInfo.getAgentName();
            this.hitServiceGuid = processAgentInfo.getAgentGuid();
            this.hitServiceUrl = processAgentInfo.getServiceUrl();
            this.hitClientUrl = processAgentInfo.getClientUrl();
            this.hitServiceType = processAgentInfo.getAgentType().toString();

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
                processAgentInfo.setOutCoupon(new Coupon());

                /*
                 * Check that WebService Url has been entered
                 */
                this.hitServiceUrl = this.hitServiceUrl.trim();
                if (this.hitServiceUrl.isEmpty() == true) {
                    throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_ServiceUrl));
                }
                processAgentInfo.setServiceUrl(this.hitServiceUrl);

                /*
                 * Check that Service Passkey has been entered
                 */
                this.hitServicePasskey = this.hitServicePasskey.trim();
                if (this.hitServicePasskey.isEmpty() == true) {
                    throw new Exception(String.format(STRERR_NotSpecified_arg, STRERR_ServicePasskey));
                }
                processAgentInfo.getOutCoupon().setPasskey(this.hitServicePasskey);
            } else {
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
            }
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
        if (message != null) {
            int index = message.indexOf('\n');
            if (index >= 0) {
                message = message.substring(0, index);
            }
        }
        this.holMessage = message;
        this.holMessageClass = Consts.STRSTL_ErrorMessage;
    }
}
